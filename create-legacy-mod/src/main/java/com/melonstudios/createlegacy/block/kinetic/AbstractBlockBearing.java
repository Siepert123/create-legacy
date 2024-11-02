package com.melonstudios.createlegacy.block.kinetic;

import com.melonstudios.createlegacy.tileentity.abstractions.AbstractTileEntityBearing;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public abstract class AbstractBlockBearing extends AbstractBlockKinetic {
    protected AbstractBlockBearing(String registry) {
        super(registry);
    }

    public static final PropertyEnum<EnumFacing> FACING = PropertyEnum.create("facing", EnumFacing.class);
    public static final PropertyBool ACTIVE = PropertyBool.create("active");

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING, ACTIVE);
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        if (placer.isSneaking()) {
            return getDefaultState().withProperty(FACING, facing.getOpposite());
        } else {
            return getDefaultState().withProperty(FACING, EnumFacing.getDirectionFromEntityLiving(pos, placer));
        }
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(FACING).getIndex() + (state.getValue(ACTIVE) ? 6 : 0);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(FACING, EnumFacing.VALUES[meta % 6]).withProperty(ACTIVE, meta / 6 != 0);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced) {
        tooltip.add(I18n.translateToLocal("tooltip.wip"));

        if (advanced.isAdvanced()) {
            tooltip.add("");
            tooltip.add("Max structure size is 256x256x256 btw");
        }
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        TileEntity entity = worldIn.getTileEntity(pos);

        if (entity instanceof AbstractTileEntityBearing && playerIn.getHeldItem(hand).isEmpty()) {
            AbstractTileEntityBearing bearing = (AbstractTileEntityBearing) entity;
            if (playerIn.isSneaking()) {
                if (!worldIn.isRemote) bearing.cycleAssemblyMode();
                if (!worldIn.isRemote) playerIn.sendStatusMessage(new TextComponentString(bearing.getAssemblyMode().toString()), true);
            } else {
                if (bearing.isAssembled()) {
                    bearing.disassemble();
                } else bearing.assemble();
            }
            return true;
        }

        return false;
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        TileEntity entity = worldIn.getTileEntity(pos);

        if (entity instanceof AbstractTileEntityBearing) {
            ((AbstractTileEntityBearing) entity).breakBlock();
        }

        super.breakBlock(worldIn, pos, state);
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    @Nullable
    @Override
    public abstract AbstractTileEntityBearing createNewTileEntity(World worldIn, int meta);
}
