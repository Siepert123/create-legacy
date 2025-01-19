package com.melonstudios.createlegacy.block.kinetic;

import com.melonstudios.createapi.CreateAPI;
import com.melonstudios.createlegacy.block.IGoggleInfo;
import com.melonstudios.createlegacy.recipe.SawingRecipes;
import com.melonstudios.createlegacy.tileentity.TileEntitySaw;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class BlockSaw extends AbstractBlockKinetic implements IGoggleInfo {
    public BlockSaw() {
        super("saw");
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced) {
        super.addInformation(stack, player, tooltip, advanced);
        tooltip.add(CreateAPI.stressImpactTooltip(8));
    }

    @Override
    public NonNullList<String> getGoggleInformation(World world, BlockPos pos, IBlockState state) {
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof TileEntitySaw) {
            TileEntitySaw saw = (TileEntitySaw) te;
            if (saw.speed() != 0) return NonNullList.from("", saw.stressGoggleInfo());
        }
        return IGoggleInfo.EMPTY;
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
                if (saw == null) throw new NullPointerException(
                        String.format("Mechanical saw at [%s,%s,%s] is null!", pos.getX(), pos.getY(), pos.getZ())
                );
                if (saw.speed() < 64) {
                    playerIn.sendStatusMessage(new TextComponentString("Needs 64 RPM to work (until later notice)"), true);
                    return false;
                }
                ItemStack result = SawingRecipes.getResult(stack, saw.getIndex(), saw.getFilter());
                if (result.isEmpty()) return true;
                EntityItem item = new EntityItem(worldIn, playerIn.posX, playerIn.posY, playerIn.posZ,
                        result.copy());
                item.setPickupDelay(0);
                item.motionX = item.motionY = item.motionZ = 0;
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
