package com.melonstudios.createlegacy.block.kinetic;

import com.melonstudios.createlegacy.block.IWrenchable;
import com.melonstudios.createlegacy.tileentity.TileEntityGearbox;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class BlockGearbox extends AbstractBlockKinetic implements IWrenchable {
    public BlockGearbox() {
        super("gearbox");
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced) {
        tooltip.add(I18n.translateToLocal("tooltip.wip"));
    }

    public static final PropertyEnum<EnumFacing.Axis> AXIS = PropertyEnum.create("axis", EnumFacing.Axis.class);

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, AXIS);
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        int met = placer.getHeldItem(hand).getMetadata();
        if (met == 1) {
            return getDefaultState().withProperty(AXIS, placer.getHorizontalFacing().rotateY().getAxis());
        }
        return getDefaultState().withProperty(AXIS, EnumFacing.Axis.Y);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(AXIS).ordinal();
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(AXIS, EnumFacing.Axis.values()[meta % 3]);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityGearbox();
    }

    @Override
    public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items) {
        items.add(new ItemStack(this, 1, 0));
        items.add(new ItemStack(this, 1, 1)); //flipped gearbox item
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    public boolean onWrenched(World world, BlockPos pos, IBlockState state, EnumFacing side, EntityPlayer wrenchHolder) {
        EnumFacing.Axis axis = state.getValue(AXIS);
        if (axis.apply(side)) return false;
        EnumFacing.Axis axisNew = EnumFacing.getFacingFromAxis(EnumFacing.AxisDirection.POSITIVE, axis).rotateAround(side.getAxis()).getAxis();
        world.setBlockState(pos, state.withProperty(AXIS, axisNew));
        return true;
    }
}
