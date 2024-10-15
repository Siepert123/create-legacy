package com.melonstudios.createlegacy.copycat;

import epicsquid.mysticallib.tile.TileBase;


import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntityCopycat extends TileBase {
    public IBlockState state = Blocks.AIR.getDefaultState();
    public ItemStack stack = ItemStack.EMPTY;
    private boolean hasGlowstone = false;

    @Override
    public boolean activate(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state, @Nonnull EntityPlayer player, @Nonnull EnumHand hand, @Nonnull EnumFacing side, float hitX, float hitY, float hitZ) {
        if (hand == EnumHand.OFF_HAND) {
            return false;
        }
        if ((this.state.getBlock() == Blocks.AIR) && !player.getHeldItemMainhand().isEmpty() && Block.getBlockFromItem(player.getHeldItemMainhand().getItem()) != Blocks.AIR) {
            Block b = Block.getBlockFromItem(player.getHeldItemMainhand().getItem());
            if (b != null && !(b instanceof ICopycatBlock) && b != Blocks.AIR) {
                IBlockState newState = b.getStateForPlacement(world, pos, side, hitX, hitY, hitZ, player.getHeldItemMainhand().getMetadata(), player);
                if (newState != this.state) {
                    this.state = newState;
                    markDirty();
                    this.stack = ItemStack.EMPTY;
                    world.notifyBlockUpdate(pos, state, newState, 8);
                    world.playSound(player, pos, SoundEvents.ENTITY_ITEMFRAME_ADD_ITEM, SoundCategory.BLOCKS, 1f, 1f);
                    markDirty();
                    return true;
                }
            }
        } else if (player.isSneaking() && player.getHeldItemMainhand().isEmpty()) {
            stack = ItemStack.EMPTY;
            this.state = Blocks.AIR.getDefaultState();
            markDirty();
            world.notifyBlockUpdate(pos, state, state, 8);
            world.playSound(player, pos, SoundEvents.ENTITY_ITEMFRAME_REMOVE_ITEM, SoundCategory.BLOCKS, 1f, 1f);
            return true;
        }
        return false;
    }

    @Override
    public void breakBlock(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state, @Nullable EntityPlayer player) {
        super.breakBlock(world, pos, state, player);
        if (!world.isRemote && (player == null || !player.capabilities.isCreativeMode)) {
            world.spawnEntity(new EntityItem(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, stack));
            if (hasGlowstone) {
                world.spawnEntity(new EntityItem(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, new ItemStack(Items.GLOWSTONE_DUST)));
            }
        }
    }

    @Override
    public boolean shouldRefresh(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState oldState, @Nonnull IBlockState newState) {
        return oldState.getBlock() != newState.getBlock();
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        state = NBTUtil.readBlockState(tag.getCompoundTag("state"));
        stack = new ItemStack(tag.getCompoundTag("stack"));
    }

    @Override
    @Nonnull
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        NBTTagCompound tag = super.writeToNBT(nbt);
        tag.setTag("state", NBTUtil.writeBlockState(new NBTTagCompound(), state));
        tag.setTag("stack", stack.writeToNBT(new NBTTagCompound()));
        return tag;
    }
}
