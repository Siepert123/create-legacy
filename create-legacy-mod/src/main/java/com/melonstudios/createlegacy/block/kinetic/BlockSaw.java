package com.melonstudios.createlegacy.block.kinetic;

import com.melonstudios.createlegacy.recipe.SawingRecipes;
import com.melonstudios.createlegacy.tileentity.TileEntitySaw;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockSaw extends AbstractBlockKinetic {
    public BlockSaw() {
        super("saw");
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntitySaw();
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack stack = playerIn.getHeldItem(hand);

        if (hand == EnumHand.OFF_HAND) {
            if (facing == EnumFacing.UP) {
                TileEntitySaw saw = (TileEntitySaw) worldIn.getTileEntity(pos);

                saw.setFilter(stack.copy());
                if (stack.isEmpty()) {
                    playerIn.sendStatusMessage(new TextComponentString("Filter cleared"), true);
                } else {
                    playerIn.sendStatusMessage(new TextComponentString("Filter set to " + stack.getDisplayName()), true);
                }
            }
            return true;
        }

        if (SawingRecipes.hasResult(stack)) {
            if (!worldIn.isRemote) {
                TileEntitySaw saw = (TileEntitySaw) worldIn.getTileEntity(pos);
                ItemStack result = SawingRecipes.getResult(stack, saw.getIndex(), saw.getFilter());
                if (result.isEmpty()) return true;
                EntityItem item = new EntityItem(worldIn, playerIn.posX, playerIn.posY, playerIn.posZ,
                        result.copy());
                item.setPickupDelay(0);
                item.setVelocity(0, 0, 0);
                worldIn.spawnEntity(item);
                stack.shrink(1);
                saw.increaseIndex();
            }
            return true;
        } else return false;
    }

    @Override
    public MapColor getMapColor(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        return MapColor.WOOD;
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }
}
