package com.melonstudios.createlegacy.block.kinetic;

import com.melonstudios.createlegacy.block.IGoggleInfo;
import com.melonstudios.createlegacy.tileentity.TileEntityDepot;
import com.melonstudios.melonlib.misc.AABB;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockDepot extends AbstractBlockKinetic implements IGoggleInfo {
    public BlockDepot() {
        super("depot");
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityDepot();
    }

    @Override
    public NonNullList<String> getGoggleInformation(World world, BlockPos pos, IBlockState state) {
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof TileEntityDepot) {
            TileEntityDepot depot = (TileEntityDepot) te;
            NonNullList<String> stuff = NonNullList.create();
            if (!depot.getStack().isEmpty()) {
                stuff.add(depot.getStack().getCount() + "\u00D7" + depot.getStack().getDisplayName());
            }
            if (!depot.getOutput().isEmpty()) {
                stuff.add(depot.getOutput().getCount() + "\u00D7 " + depot.getOutput().getDisplayName());
            }
            if (!depot.getOutput2().isEmpty()) {
                stuff.add(depot.getOutput2().getCount() + "\u00D7 " + depot.getOutput2().getDisplayName());
            }
            return stuff;
        }
        return IGoggleInfo.EMPTY;
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
                if (!depot.getOutput().isEmpty() || !depot.getOutput2().isEmpty()) {
                    {
                        ItemStack stack = depot.getOutput().copy();
                        if (!stack.isEmpty()) {
                            depot.setOutput(ItemStack.EMPTY);
                            if (!worldIn.isRemote) {
                                EntityItem item = new EntityItem(worldIn, playerIn.posX, playerIn.posY, playerIn.posZ, stack);
                                item.motionX = item.motionY = item.motionZ = 0;
                                item.setNoPickupDelay();
                                worldIn.spawnEntity(item);
                            }
                        }
                    }
                    {
                        ItemStack stack = depot.getOutput2().copy();
                        if (!stack.isEmpty()) {
                            depot.setOutput2(ItemStack.EMPTY);
                            if (!worldIn.isRemote) {
                                EntityItem item = new EntityItem(worldIn, playerIn.posX, playerIn.posY, playerIn.posZ, stack);
                                item.motionX = item.motionY = item.motionZ = 0;
                                item.setNoPickupDelay();
                                worldIn.spawnEntity(item);
                            }
                        }
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
            EntityItem item3 = new EntityItem(worldIn, pos.getX(), pos.getY(), pos.getZ(), depot.getOutput2());
            worldIn.spawnEntity(item1);
            worldIn.spawnEntity(item2);
            worldIn.spawnEntity(item3);
        }
        super.breakBlock(worldIn, pos, state);
    }

    private static final AxisAlignedBB aabb = AABB.create(0, 0, 0, 16, 14, 16);
    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return aabb;
    }
}
