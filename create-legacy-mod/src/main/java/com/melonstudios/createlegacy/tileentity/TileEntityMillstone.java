package com.melonstudios.createlegacy.tileentity;

import com.melonstudios.createlegacy.network.PacketUpdateMillstone;
import com.melonstudios.createlegacy.recipe.MillingRecipes;
import com.melonstudios.createlegacy.tileentity.abstractions.AbstractTileEntityKinetic;
import com.melonstudios.createlegacy.util.EnumKineticConnectionType;
import com.melonstudios.createlegacy.util.RecipeEntry;
import com.melonstudios.createlegacy.util.registries.ModSoundEvents;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;

import java.util.List;
import java.util.Random;

/**
 * <h1>AHHHHHHHHHHHHH</h1>
 */
public class TileEntityMillstone extends AbstractTileEntityKinetic implements ISidedInventory {
    ItemStack currentlyMilling = ItemStack.EMPTY;
    ItemStack outputMain = ItemStack.EMPTY;
    ItemStack additionalOutput = ItemStack.EMPTY;
    ItemStack otherAdditionalOutput = ItemStack.EMPTY;

    private static final int[] topFaceSlots = new int[]{0};
    private static final int[] sideFaceSlots = new int[]{0,1,2,3};

    public boolean renderParticles() {
        return !currentlyMilling.isEmpty() && speed() != 0
                && MillingRecipes.hasResult(currentlyMilling)
                && (compareStacks(MillingRecipes.getResults(currentlyMilling)[0].getValue1(),
                MillingRecipes.getResults(currentlyMilling)[1].getValue1(),
                MillingRecipes.getResults(currentlyMilling)[2].getValue1()) || outputsEmpty());
    }

    protected int progress;

    public void drop() {
        spawnItem(currentlyMilling.copy());
        spawnItem(outputMain.copy());
        spawnItem(additionalOutput.copy());
        spawnItem(otherAdditionalOutput.copy());
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);

        compound.setInteger("progress", progress);

        NBTTagCompound inventory = new NBTTagCompound();

        saveItemStack(inventory, currentlyMilling, "CurrentlyMilling");
        saveItemStack(inventory, outputMain, "OutputMain");
        saveItemStack(inventory, additionalOutput, "AdditionalOutput");
        saveItemStack(inventory, otherAdditionalOutput, "OtherAdditionalOutput");

        if (!inventory.hasNoTags()) compound.setTag("Inventory", inventory);

        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);

        progress = compound.getInteger("progress");

        if (compound.hasKey("Inventory")) {
            NBTTagCompound inventory = compound.getCompoundTag("Inventory");

            currentlyMilling = readItemStack(inventory, "CurrentlyMilling");
            outputMain = readItemStack(inventory, "OutputMain");
            additionalOutput = readItemStack(inventory, "AdditionalOutput");
            otherAdditionalOutput = readItemStack(inventory, "OtherAdditionalOutput");
        }
    }

    protected boolean outputsEmpty() {
        return outputMain.isEmpty() && additionalOutput.isEmpty() && otherAdditionalOutput.isEmpty();
    }
    protected boolean compareStacks(ItemStack stack1, ItemStack stack2, ItemStack stack3) {
        boolean b0 = outputMain.isEmpty() || stack1.isItemEqual(outputMain);
        boolean b1 = additionalOutput.isEmpty() || stack2.isItemEqual(additionalOutput);
        boolean b2 = otherAdditionalOutput.isEmpty() || stack3.isItemEqual(otherAdditionalOutput);
        boolean b3 = outputsEmpty();
        return b0 && b1 && b2;
    }

    public boolean active = false;
    protected boolean renderParticlesParticles() {
        return active;
    }

    @Override
    protected void tick() {
        if (world.isRemote) {
            if (renderParticlesParticles()) {
                Random random = world.rand;
                world.spawnParticle(EnumParticleTypes.CRIT,
                        pos.getX() + random.nextFloat(),
                        pos.getY() + 0.5,
                        pos.getZ() + random.nextFloat(),
                        0, 0, 0);
            }
        } else {
            if (renderParticles()) {
                if (world.getTotalWorldTime() % 5 == 0) {
                    world.playSound(null, pos,
                            ModSoundEvents.BLOCK_MILLSTONE_AMBIENT, SoundCategory.BLOCKS,
                            1.0f, 1.0f);
                    world.playSound(null, pos,
                            ModSoundEvents.BLOCK_MILLSTONE_AMBIENT, SoundCategory.BLOCKS,
                            1.0f, 1.1f);
                    world.playSound(null, pos,
                            ModSoundEvents.BLOCK_MILLSTONE_AMBIENT, SoundCategory.BLOCKS,
                            1.0f, 0.9f);
                }
                if (progress > MillingRecipes.getWork(currentlyMilling)) {
                    RecipeEntry[] results = MillingRecipes.getResults(currentlyMilling);
                    Random random = world.rand;

                    if (!results[0].getValue1().isEmpty()) {
                        if (results[0].getValue2() >= random.nextFloat()) {
                            ItemStack stack = results[0].getValue1();
                            if (outputMain.isEmpty()) outputMain = stack;
                            else {
                                outputMain.setCount(outputMain.getCount() + stack.getCount());
                            }
                        }
                    }
                    if (!results[1].getValue1().isEmpty()) {
                        if (results[1].getValue2() >= random.nextFloat()) {
                            ItemStack stack = results[1].getValue1();
                            if (additionalOutput.isEmpty()) additionalOutput = stack;
                            else {
                                additionalOutput.setCount(additionalOutput.getCount() + stack.getCount());
                            }
                        }
                    }
                    if (!results[2].getValue1().isEmpty()) {
                        if (results[2].getValue2() >= random.nextFloat()) {
                            ItemStack stack = results[2].getValue1();
                            if (otherAdditionalOutput.isEmpty()) otherAdditionalOutput = stack;
                            else {
                                otherAdditionalOutput.setCount(otherAdditionalOutput.getCount() + stack.getCount());
                            }
                        }
                    }
                    currentlyMilling.shrink(1);
                    markDirty();
                    progress = 0;


                    clean();
                } else {
                    progress += getWorkTick();
                    markDirty();
                }
                PacketUpdateMillstone.sendToPlayersNearby(this, 32);
            }

            if (currentlyMilling.isEmpty()) {
                progress = 0;

                List<EntityItem> groundItems = world.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(pos.up()));

                for (EntityItem groundItem : groundItems) {
                    ItemStack item = groundItem.getItem();

                    if (MillingRecipes.hasResult(item)) {
                        currentlyMilling = item;
                        groundItem.setDead();
                        markDirty();
                        break;
                    }
                }
            }

            PacketUpdateMillstone.sendToPlayersNearby(this, 32);
        }
    }

    protected void clean() {
        if (currentlyMilling.isEmpty()) currentlyMilling = ItemStack.EMPTY;
        if (outputMain.isEmpty()) currentlyMilling = ItemStack.EMPTY;
        if (additionalOutput.isEmpty()) additionalOutput = ItemStack.EMPTY;
        if (otherAdditionalOutput.isEmpty()) otherAdditionalOutput = ItemStack.EMPTY;
    }

    @Override
    protected String namePlate() {
        return "Millstone";
    }

    @Override
    public EnumKineticConnectionType getConnectionType(EnumFacing side) {
        return side == EnumFacing.DOWN ? connection(1) : side.getAxis() != EnumFacing.Axis.Y ? connection(2) : connection(0);
    }

    @Override
    public int[] getSlotsForFace(EnumFacing side) {
        return side == EnumFacing.UP ? topFaceSlots : sideFaceSlots;
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
        return index == 0 && direction != EnumFacing.DOWN && MillingRecipes.hasResult(itemStackIn);
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
        return index != 0 && direction != EnumFacing.UP;
    }

    @Override
    public int getSizeInventory() {
        return 4;
    }

    @Override
    public boolean isEmpty() {
        return outputsEmpty();
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        switch (index) {
            case 0: return currentlyMilling;
            case 1: return outputMain;
            case 2: return additionalOutput;
            case 3: return otherAdditionalOutput;
            default: throw new IllegalArgumentException("Invalid slot index: " + index);
        }
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        switch (index) {
            case 0: return currentlyMilling.splitStack(count);
            case 1: return outputMain.splitStack(count);
            case 2: return additionalOutput.splitStack(count);
            case 3: return otherAdditionalOutput.splitStack(count);
            default: throw new IllegalArgumentException("Invalid slot index: " + index);
        }
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        ItemStack stack;
        switch (index) {
            case 0: stack = currentlyMilling; currentlyMilling = ItemStack.EMPTY; break;
            case 1: stack = outputMain; outputMain = ItemStack.EMPTY; break;
            case 2: stack = additionalOutput; additionalOutput = ItemStack.EMPTY; break;
            case 3: stack = otherAdditionalOutput; otherAdditionalOutput = ItemStack.EMPTY; break;
            default: throw new IllegalArgumentException("Invalid slot index: " + index);
        }
        return stack;
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        switch (index) {
            case 0: currentlyMilling = stack; break;
            case 1: outputMain = stack; break;
            case 2: additionalOutput = stack; break;
            case 3: otherAdditionalOutput = stack; break;
            default: throw new IllegalArgumentException("Invalid slot index: " + index);
        }
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
        return index == 0 && MillingRecipes.hasResult(stack);
    }

    @Override
    public int getField(int id) {
        return 0;
    }

    @Override
    public void setField(int id, int value) {

    }

    @Override
    public int getFieldCount() {
        return 0;
    }

    @Override
    public void clear() {
        currentlyMilling = ItemStack.EMPTY;
        outputMain = ItemStack.EMPTY;
        additionalOutput = ItemStack.EMPTY;
        otherAdditionalOutput = ItemStack.EMPTY;
    }

    @Override
    public String getName() {
        return "Millstone";
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }

    @Override
    public float consumedStressMarkiplier() {
        return 8.0f;
    }
}
