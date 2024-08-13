package com.siepert.createlegacy.tileentity;

import com.siepert.createapi.network.IKineticTE;
import com.siepert.createapi.network.KineticBlockInstance;
import com.siepert.createapi.network.NetworkContext;
import com.siepert.createlegacy.CreateLegacy;
import com.siepert.createlegacy.CreateLegacyConfigHolder;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

import static com.siepert.createlegacy.blocks.kinetic.BlockDeployer.FACING;

public class TileEntityDeployer extends TileEntity implements IKineticTE, ISidedInventory {
    int cooldown = 0;

    ItemStack useStack;
    ItemStack trashStack;

    EntityPlayer player;

    public void setPlacer(EntityPlayer placer) {
        this.player = placer;
    }

    public TileEntityDeployer() {
        useStack = ItemStack.EMPTY;
        trashStack = ItemStack.EMPTY;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);

        compound.setInteger("Cooldown", cooldown);

        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);

        cooldown = compound.getInteger("Cooldown");
    }

    @Override
    public double getStressImpact() {
        return CreateLegacyConfigHolder.kineticConfig.deployerStressImpact;
    }

    @Override
    public boolean isConsumer() {
        return true;
    }

    @Override
    public double getStressCapacity() {
        return 0;
    }

    @Override
    public int getProducedSpeed() {
        return 0;
    }

    @Override
    public void kineticTick(NetworkContext context) {
        if (context.networkSpeed == 0) return;

        EnumFacing facing = world.getBlockState(pos).getValue(FACING).toVanillaFacing();

        BlockPos actPos = pos.offset(facing, 2);

        boolean mayPlace = world.getBlockState(actPos).getMaterial().isReplaceable() && useStack.getItem() instanceof ItemBlock;

        boolean mayUse = !(useStack.getItem() instanceof ItemBlock);

        if (mayPlace || mayUse) {

            if (cooldown > 100) {
                try {
                    if (mayPlace) {
                        IBlockState place = ((ItemBlock) useStack.getItem()).getBlock()
                                .getStateForPlacement(world, actPos, facing.getOpposite(), 0.5f, 0.5f, 0.5f, useStack.getMetadata(),
                                        player, EnumHand.MAIN_HAND);

                        world.setBlockState(actPos, place, 3);
                        world.playEvent(2001, actPos, Block.getStateId(place));
                        useStack.shrink(1);
                    } else {
                        useStack.getItem().onItemUse(player, world, actPos, EnumHand.MAIN_HAND, facing.getOpposite(), 0.5f, 0.5f, 0.5f);
                    }
                } catch (NullPointerException ignored) {

                }
                cooldown = 0;
            }

            cooldown += Math.max(context.networkSpeed / 16, 1);
        } else cooldown = 0;
    }

    @Override
    public void setUpdated() {

    }

    @Override
    public void passNetwork(NetworkContext context, EnumFacing source, boolean srcIsCog, boolean srcCogIsHorizontal, boolean inverted) {
        IBlockState state = world.getBlockState(pos);

        if (srcIsCog) return;

        if (source.getAxis() == state.getValue(FACING).toVanillaFacing().rotateAround(EnumFacing.Axis.Y).getAxis()) {
            context.addKineticBlockInstance(new KineticBlockInstance(pos, inverted));

            TileEntity entity = world.getTileEntity(pos.offset(source.getOpposite()));

            if (entity instanceof IKineticTE) {
                ((IKineticTE) entity).passNetwork(context, source, false, false, inverted);
            }
        }
    }

    @Override
    public int[] getSlotsForFace(EnumFacing side) {
        if (side == EnumFacing.DOWN) return new int[]{1};
        return new int[]{0};
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
        if (index == 1) return false;
        return direction != EnumFacing.DOWN;
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
        if (index == 0) return false;
        return direction == EnumFacing.DOWN;
    }

    @Override
    public int getSizeInventory() {
        return 2;
    }

    @Override
    public boolean isEmpty() {
        if (!useStack.isEmpty()) return false;
        return trashStack.isEmpty();
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        if (index == 0) return useStack;
        if (index == 1) return trashStack;
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        return getStackInSlot(index).splitStack(count);
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        ItemStack toReturn;
        if (index > 1) return ItemStack.EMPTY;
        if (index == 0) {
            toReturn = useStack.copy();
            useStack = ItemStack.EMPTY;
        } else {
            toReturn = trashStack.copy();
            trashStack = ItemStack.EMPTY;
        }
        return toReturn;
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        if (index == 0) {
            useStack = stack;
        }
        if (index == 1) {
            trashStack = stack;
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
        return index == 0;
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
        useStack = ItemStack.EMPTY;
        trashStack = ItemStack.EMPTY;
    }

    @Override
    public String getName() {
        return "Deployer";
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }
}
