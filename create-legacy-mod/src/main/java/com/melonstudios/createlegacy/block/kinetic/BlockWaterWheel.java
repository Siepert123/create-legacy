package com.melonstudios.createlegacy.block.kinetic;

import com.melonstudios.createapi.CreateAPI;
import com.melonstudios.createlegacy.block.IGoggleInfo;
import com.melonstudios.createlegacy.block.IWrenchable;
import com.melonstudios.createlegacy.tileentity.TileEntityCreativeMotor;
import com.melonstudios.createlegacy.tileentity.TileEntityWaterWheel;
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
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class BlockWaterWheel extends AbstractBlockKinetic implements IWrenchable, IGoggleInfo {
    public BlockWaterWheel() {
        super("water_wheel");
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced) {
        super.addInformation(stack, player, tooltip, advanced);
        tooltip.add(CreateAPI.stressCapacityTooltip(32));
    }

    @Override
    public NonNullList<String> getGoggleInformation(World world, BlockPos pos, IBlockState state) {
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof TileEntityWaterWheel) {
            TileEntityWaterWheel waterWheel = (TileEntityWaterWheel) te;
            if (waterWheel.generatedRPM() != 0) return NonNullList.from("", waterWheel.capacityGoggleInfo());
        }
        return IGoggleInfo.EMPTY;
    }

    public static final PropertyEnum<EnumFacing.Axis> AXIS = PropertyEnum.create("axis", EnumFacing.Axis.class,
            EnumFacing.Axis.X, EnumFacing.Axis.Z);

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, AXIS);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityWaterWheel();
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(AXIS) == EnumFacing.Axis.X ? 0 : 1;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(AXIS, meta == 0 ? EnumFacing.Axis.X : EnumFacing.Axis.Z);
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        return getDefaultState().withProperty(AXIS, placer.getHorizontalFacing().getAxis());
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    public boolean onWrenched(World world, BlockPos pos, IBlockState state, EnumFacing side, EntityPlayer wrenchHolder) {
        world.setBlockState(pos, state.withProperty(AXIS, state.getValue(AXIS) == EnumFacing.Axis.X ? EnumFacing.Axis.Z : EnumFacing.Axis.X));
        return true;
    }
}
