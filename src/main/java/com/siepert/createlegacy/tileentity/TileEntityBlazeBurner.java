package com.siepert.createlegacy.tileentity;

import com.siepert.createlegacy.blocks.kinetic.BlockBlazeBurner;
import com.siepert.createlegacy.util.Reference;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;

public class TileEntityBlazeBurner extends TileEntity implements ITickable {
    private int remainingBurnTime;

    public enum CookLevel {
        PASSIVE, HEATED, SEETHING
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setInteger("burnTime", remainingBurnTime);
        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        remainingBurnTime = compound.getInteger("burnTime");
    }

    @Override
    public void update() {
        IBlockState myState = world.getBlockState(this.getPos());
        if (myState.getValue(BlockBlazeBurner.STATE).getMeta() != 0) {
            if (myState.getValue(BlockBlazeBurner.SCHEDULE) == 1) {
                remainingBurnTime = 1200;
                world.playSound(null,
                        pos.getX() + 0.5,
                        pos.getY() + 0.5,
                        pos.getZ() + 0.5,
                        SoundEvents.ITEM_FIRECHARGE_USE,
                        SoundCategory.BLOCKS, 0.8f, 1.0f);
            } else if (myState.getValue(BlockBlazeBurner.SCHEDULE) == 2) {
                remainingBurnTime = 3600;
                world.playSound(null,
                        pos.getX() + 0.5,
                        pos.getY() + 0.5,
                        pos.getZ() + 0.5,
                        SoundEvents.ITEM_FIRECHARGE_USE,
                        SoundCategory.BLOCKS, 1.3f, 0.7f);
            }
            if (!world.isRemote) {
                IBlockState myNewState = world.getBlockState(pos).withProperty(BlockBlazeBurner.SCHEDULE, 0);
                if (remainingBurnTime > 2400) {
                    if (world.getTotalWorldTime() % 15 == 0) {
                        if (Reference.random.nextInt(3) == 0) world.playSound(null,
                                pos.getX() + 0.5,
                                pos.getY() + 0.5,
                                pos.getZ() + 0.5,
                                SoundEvents.ENTITY_BLAZE_BURN,
                                SoundCategory.BLOCKS,
                                0.7f, 1.24f);
                        if (Reference.random.nextInt(7) == 0) world.playSound(null,
                                pos.getX() + 0.5,
                                pos.getY() + 0.5,
                                pos.getZ() + 0.5,
                                SoundEvents.ENTITY_BLAZE_AMBIENT,
                                SoundCategory.BLOCKS,
                                0.3f, 1.0f);
                    }
                    remainingBurnTime--;
                    BlockBlazeBurner.setState(myNewState.withProperty(BlockBlazeBurner.STATE, BlockBlazeBurner.State.COPE_SEETHE_MALD), world, pos);
                } else if (remainingBurnTime > 0) {
                    if (world.getTotalWorldTime() % 35 == 0) {
                        if (Reference.random.nextInt(3) == 0) world.playSound(null,
                                pos.getX() + 0.5,
                                pos.getY() + 0.5,
                                pos.getZ() + 0.5,
                                SoundEvents.ENTITY_BLAZE_BURN,
                                SoundCategory.BLOCKS,
                                0.7f, 1.24f);
                        if (Reference.random.nextInt(7) == 0) world.playSound(null,
                                pos.getX() + 0.5,
                                pos.getY() + 0.5,
                                pos.getZ() + 0.5,
                                SoundEvents.ENTITY_BLAZE_AMBIENT,
                                SoundCategory.BLOCKS,
                                0.3f, 1.0f);
                    }
                    remainingBurnTime--;
                    BlockBlazeBurner.setState(myNewState.withProperty(BlockBlazeBurner.STATE, BlockBlazeBurner.State.HEATED), world, pos);
                } else {
                    BlockBlazeBurner.setState(myNewState.withProperty(BlockBlazeBurner.STATE, BlockBlazeBurner.State.PASSIVE), world, pos);
                }
            }
            world.markBlockRangeForRenderUpdate(pos.east().north().down(), pos.west().south().up());
        }
    }
}
