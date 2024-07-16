package com.siepert.createlegacy.tileentity;

import com.siepert.createlegacy.blocks.kinetic.BlockFunnel;
import com.siepert.createlegacy.util.EnumHorizontalFacing;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

public class TileEntityFunnel extends TileEntity implements ITickable {
    @Override
    public void update() {
        EnumFacing facing = world.getBlockState(pos).getValue(BlockFunnel.FACING).toVanillaFacing();
        boolean extracting = world.getBlockState(pos).getValue(BlockFunnel.EXTRACTING);

        BlockPos target = pos.offset(facing.getOpposite());

        TileEntity targetEntity = world.getTileEntity(target);

        boolean alreadyItem = !world.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(pos)).isEmpty();
        if (extracting && !alreadyItem) {
            if (targetEntity != null) {
                if (targetEntity instanceof ISidedInventory) {
                    for (int i = 0; i < ((ISidedInventory) targetEntity).getSizeInventory(); i++) {
                        if (((ISidedInventory) targetEntity).canExtractItem(i, ((ISidedInventory) targetEntity).getStackInSlot(i), facing)
                            && !((ISidedInventory) targetEntity).getStackInSlot(i).isEmpty()) {
                            ItemStack stack = ((ISidedInventory) targetEntity).removeStackFromSlot(i);
                            EntityItem entityItem = new EntityItem(world, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, stack);
                            entityItem.setVelocity(0, 0, 0);
                            world.spawnEntity(entityItem);
                            break;
                        }
                    }
                } else if (targetEntity instanceof IInventory) {
                    for (int i = 0; i < ((IInventory) targetEntity).getSizeInventory(); i++) {
                        if (!((ISidedInventory) targetEntity).getStackInSlot(i).isEmpty()) {
                            ItemStack stack = ((ISidedInventory) targetEntity).removeStackFromSlot(i);
                            EntityItem entityItem = new EntityItem(world, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, stack);
                            entityItem.setVelocity(0, 0, 0);
                            world.spawnEntity(entityItem);
                            break;
                        }
                    }
                }
            }
        }
    }
}
