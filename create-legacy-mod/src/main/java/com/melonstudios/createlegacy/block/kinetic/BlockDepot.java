package com.melonstudios.createlegacy.block.kinetic;

import com.melonstudios.createlegacy.tileentity.TileEntityDepot;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockDepot extends AbstractBlockKinetic {
    public BlockDepot() {
        super("depot");
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityDepot();
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        TileEntity entity = worldIn.getTileEntity(pos);

        if (entity instanceof TileEntityDepot) {
            TileEntityDepot depot = (TileEntityDepot) entity;

            if (!playerIn.getHeldItem(hand).isEmpty()) {
                if (depot.getStack().isEmpty()) {
                    depot.setStack(playerIn.getHeldItem(hand));
                    playerIn.setHeldItem(hand, ItemStack.EMPTY);
                    return true;
                }
            } else {
                if (!depot.getOutput().isEmpty()) {
                    ItemStack stack = depot.getOutput().copy();
                    depot.setOutput(ItemStack.EMPTY);
                    if (!worldIn.isRemote) {
                        EntityItem item = new EntityItem(worldIn, playerIn.posX, playerIn.posY, playerIn.posZ, stack);
                        item.motionX = item.motionY = item.motionZ = 0;
                        item.setNoPickupDelay();
                        worldIn.spawnEntity(item);
                    }
                    return true;
                } else if (!depot.getStack().isEmpty()) {
                    ItemStack stack = depot.getStack().copy();
                    depot.setStack(ItemStack.EMPTY);
                    if (!worldIn.isRemote) {
                        EntityItem item = new EntityItem(worldIn, playerIn.posX, playerIn.posY, playerIn.posZ, stack);
                        item.motionX = item.motionY = item.motionZ = 0;
                        item.setNoPickupDelay();
                        worldIn.spawnEntity(item);
                    }
                    return true;
                } else return false;
            }
        }

        return false;
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        TileEntityDepot depot = (TileEntityDepot) worldIn.getTileEntity(pos);
        if (depot != null) {
            EntityItem item1 = new EntityItem(worldIn, pos.getX(), pos.getY(), pos.getZ(), depot.getStack());
            EntityItem item2 = new EntityItem(worldIn, pos.getX(), pos.getY(), pos.getZ(), depot.getOutput());
            worldIn.spawnEntity(item1);
            worldIn.spawnEntity(item2);
        }
        super.breakBlock(worldIn, pos, state);
    }
}
