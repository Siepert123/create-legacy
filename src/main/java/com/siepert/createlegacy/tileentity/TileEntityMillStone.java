package com.siepert.createlegacy.tileentity;

import com.siepert.createapi.IKineticTE;
import com.siepert.createlegacy.CreateLegacy;
import com.siepert.createlegacy.blocks.kinetic.BlockMillStone;
import com.siepert.createlegacy.mainRegistry.ModBlocks;
import com.siepert.createlegacy.util.Reference;
import com.siepert.createlegacy.util.handlers.ModSoundHandler;
import com.siepert.createlegacy.util.handlers.recipes.MillingRecipes;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;

import java.util.List;

public class TileEntityMillStone extends TileEntity implements ITickable, IKineticTE, ISidedInventory {
    private int currentMillProgress;
    private int maxMillProgress;
    private ItemStack currentlyMilling;
    private ItemStack output;
    private ItemStack outputOptional;
    private boolean hasRecipe;
    private int speed;

    private void visualize() {
        world.spawnParticle(EnumParticleTypes.CRIT,
                pos.getX() + Reference.random.nextFloat(),
                pos.getY() + Reference.random.nextFloat(),
                pos.getZ() + Reference.random.nextFloat(),
                0, 0, 0);
    }

    @Override
    public void update() {
        MillingRecipes.ResultSet set = MillingRecipes.apply(currentlyMilling);
        speed = 64;
        hasRecipe = set.hasRecipe();
        if (hasRecipe) {
            maxMillProgress = set.getMillTime();
            if (areTheConditionsOK(set)) {
                handleTheRecipeStuff(set);
                currentMillProgress++;
                if (world.getTotalWorldTime() % 5 == 0) {
                    world.playSound(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                            ModSoundHandler.BLOCK_MILLSTONE_AMBIENT, SoundCategory.BLOCKS, 0.5f, 1.0f);
                    world.playSound(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                            ModSoundHandler.BLOCK_MILLSTONE_AMBIENT, SoundCategory.BLOCKS, 0.4f, 1.0f);
                    world.playSound(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                            ModSoundHandler.BLOCK_MILLSTONE_AMBIENT, SoundCategory.BLOCKS, 0.4f, 1.0f);
                }
                BlockMillStone.setState(world, pos, world.getBlockState(pos).withProperty(BlockMillStone.ACTIVE, true));
            } else {
                BlockMillStone.setState(world, pos, world.getBlockState(pos).withProperty(BlockMillStone.ACTIVE, false));
            }
        } else {
            resetAll();
        }

        if (currentlyMilling.isEmpty()) {
            AxisAlignedBB bb = new AxisAlignedBB(pos.up());

            List<EntityItem> items = world.getEntitiesWithinAABB(EntityItem.class, bb);

            for (EntityItem item : items) {
                if (currentlyMilling.isEmpty()) {
                    if (MillingRecipes.apply(item.getItem()).hasRecipe()) {
                        setCurrentlyMilling(item.getItem());
                        item.setDead();
                    }
                }
            }
        }

        if (world.getBlockState(pos).getValue(BlockMillStone.ACTIVE) && world.isRemote) {
            visualize();
        }

        markDirty();
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setInteger("CurrentMillProgress", currentMillProgress);
        compound.setInteger("MaximumMillProgress", maxMillProgress);

        if (!currentlyMilling.isEmpty()) {
            NBTTagCompound inputStackNBT = new NBTTagCompound();
            currentlyMilling.writeToNBT(inputStackNBT);
            compound.setTag("CurrentlyMilling", inputStackNBT);
        }
        if (!output.isEmpty()) {
            NBTTagCompound outputStackNBT = new NBTTagCompound();
            output.writeToNBT(outputStackNBT);
            compound.setTag("Output", outputStackNBT);
        }
        if (!outputOptional.isEmpty()) {
            NBTTagCompound outputOptionalStackNBT = new NBTTagCompound();
            outputOptional.writeToNBT(outputOptionalStackNBT);
            compound.setTag("OutputOptional", outputOptionalStackNBT);
        }
        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        currentMillProgress = compound.getInteger("CurrentMillProgress");
        maxMillProgress = compound.getInteger("MaximumMillProgress");
        if (compound.hasKey("CurrentlyMilling")) {
            currentlyMilling = new ItemStack(compound.getCompoundTag("CurrentlyMilling"));
        } else {
            currentlyMilling = ItemStack.EMPTY;
        }

        if (compound.hasKey("Output")) {
            output = new ItemStack(compound.getCompoundTag("Output"));
        } else {
            output = ItemStack.EMPTY;
        }

        if (compound.hasKey("OutputOptional")) {
            outputOptional = new ItemStack(compound.getCompoundTag("OutputOptional"));
        } else {
            outputOptional = ItemStack.EMPTY;
        }
    }

    public boolean hasOutput() {
        return !output.isEmpty();
    }
    public boolean hasOutputOptional() {
        return !outputOptional.isEmpty();
    }
    public boolean isMilling() {
        return !currentlyMilling.isEmpty();
    }


    public ItemStack getCurrentlyMilling() {
        return currentlyMilling;
    }
    public void setCurrentlyMilling(ItemStack currentlyMilling) {
        this.currentlyMilling = currentlyMilling;
        markDirty();
    }

    public ItemStack getOutput() {
        return output;
    }
    public ItemStack getOutputOptional() {
        return outputOptional;
    }

    public void removeItemStacksToPlayer(World worldIn, EntityPlayer player) {
        if (output.isEmpty() && outputOptional.isEmpty()) {
            EntityItem item = new EntityItem(worldIn, player.posX, player.posY, player.posZ);
            item.setNoPickupDelay();
            item.setItem(currentlyMilling.copy());
            currentlyMilling = ItemStack.EMPTY;
            worldIn.spawnEntity(item);
            return;
        }
        if (!output.isEmpty()) {
            EntityItem item = new EntityItem(worldIn, player.posX, player.posY, player.posZ);
            item.setNoPickupDelay();
            item.setItem(output.copy());
            output = ItemStack.EMPTY;
            worldIn.spawnEntity(item);
        }
        if (!outputOptional.isEmpty()) {
            EntityItem item = new EntityItem(worldIn, player.posX, player.posY, player.posZ);
            item.setNoPickupDelay();
            item.setItem(outputOptional.copy());
            outputOptional = ItemStack.EMPTY;
            worldIn.spawnEntity(item);
        }
    }

    private void resetAll() {
        this.maxMillProgress = 0;
        this.currentMillProgress = 0;
    }
    private boolean areTheConditionsOK(MillingRecipes.ResultSet set) {
        boolean flag = output.isEmpty() || set.getResult().isItemEqual(output);
        boolean flag1 = outputOptional.isEmpty() || set.getResultOptional().isItemEqual(outputOptional);
        boolean flag2 = outputOptional.getCount() < outputOptional.getMaxStackSize() && output.getCount() < output.getMaxStackSize();
        return flag && flag1 && hasRecipe && flag2;
    }
    private void handleTheRecipeStuff(MillingRecipes.ResultSet set) {
        if (currentMillProgress >= maxMillProgress) {
            currentMillProgress = 0;

            currentlyMilling.shrink(1);

            if (!output.isEmpty()) {
                output.setCount(output.getCount() + set.getResult().getCount());
            } else {
                output = set.getResult().copy();
            }

            if (set.hasOptional() && Reference.random.nextInt(100) < set.getPercentage()) {
                if (!outputOptional.isEmpty()) {
                    outputOptional.setCount(outputOptional.getCount() + set.getResultOptional().getCount());
                } else {
                    outputOptional = set.getResultOptional().copy();
                }
            }
        }
    }


    public TileEntityMillStone() {
        this.currentlyMilling = ItemStack.EMPTY;
        this.output = ItemStack.EMPTY;
        this.outputOptional = ItemStack.EMPTY;
    }

    public void dropItems() {
        if (!world.isRemote) {
            if (!currentlyMilling.isEmpty()) {
                EntityItem item = new EntityItem(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
                item.setItem(currentlyMilling.copy());
                world.spawnEntity(item);
            }
            if (!output.isEmpty()) {
                EntityItem item = new EntityItem(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
                item.setItem(output.copy());
                world.spawnEntity(item);
            }
            if (!outputOptional.isEmpty()) {
                EntityItem item = new EntityItem(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
                item.setItem(outputOptional.copy());
                world.spawnEntity(item);
            }
        }
    }

    @Override
    public int getConsumedSU() {
        return speed * 4;
    }

    @Override
    public int getSpeed() {
        return speed;
    }

    private static final int SLOT_INPUT = 0;
    private static final int SLOT_OUTPUT = 1;
    private static final int SLOT_OUTPUT_OPTIONAL = 2;
    TileEntityFurnace furnace;
    @Override
    public int[] getSlotsForFace(EnumFacing side) {
        if (side == EnumFacing.UP) return new int[]{SLOT_INPUT};
        return new int[]{SLOT_OUTPUT, SLOT_OUTPUT_OPTIONAL};
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
        if (direction == EnumFacing.UP) {
            return isItemValidForSlot(index, itemStackIn);
        }
        return false;
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
        if (index == 0) {
            return false;
        }
        return direction != EnumFacing.UP;
    }

    @Override
    public int getSizeInventory() {
        return 3;
    }

    @Override
    public boolean isEmpty() {
        if (!currentlyMilling.isEmpty()) return false;
        if (!output.isEmpty()) return false;
        if (!outputOptional.isEmpty()) return false;
        return true;
    }

    private static ItemStack handleIllegalIndex(int index, int max) {
        CreateLegacy.logger.error("Tried getting stack in slot id {} while max allowed is {}", index, max);
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        switch (index) {
            case 0:
                return currentlyMilling;
            case 1:
                return output;
            case 2:
                return outputOptional;
        }
        return handleIllegalIndex(index, 2);
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        switch (index) {
            case 0:
                return currentlyMilling.splitStack(count);
            case 1:
                return output.splitStack(count);
            case 2:
                return outputOptional.splitStack(count);
        }
        return handleIllegalIndex(index, 2);
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        switch (index) {
            case 0:
                ItemStack stack = currentlyMilling.copy();
                currentlyMilling = ItemStack.EMPTY;
                return stack;
            case 1:
                ItemStack stack0 = output.copy();
                output = ItemStack.EMPTY;
                return stack0;
            case 2:
                ItemStack stack1 = outputOptional.copy();
                outputOptional = ItemStack.EMPTY;
                return stack1;
        }
        return handleIllegalIndex(index, 2);
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        switch (index) {
            case 0:
                currentlyMilling = stack;
                break;
            case 1:
                output = stack;
                break;
            case 2:
                outputOptional = stack;
                break;
        }
        handleIllegalIndex(index, 2);
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer player) {
        return false;
    }

    @Override
    public void openInventory(EntityPlayer player) {

    }

    @Override
    public void closeInventory(EntityPlayer player) {

    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        if (index == 0) {
            return MillingRecipes.apply(stack).hasRecipe();
        }
        return false;
    }

    @Override
    public int getField(int id) {
        switch (id) {
            case 0:
                return currentMillProgress;
            case 1:
                return maxMillProgress;
        }
        return 0;
    }

    @Override
    public void setField(int id, int value) {
        switch (id) {
            case 0:
                currentMillProgress = value;
                break;
            case 1:
                maxMillProgress = value;
                break;
        }
    }

    @Override
    public int getFieldCount() {
        return 2;
    }

    @Override
    public void clear() {
        currentlyMilling = ItemStack.EMPTY;
        output = ItemStack.EMPTY;
        outputOptional = ItemStack.EMPTY;
    }

    @Override
    public String getName() {
        return "Millstone";
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }
}
