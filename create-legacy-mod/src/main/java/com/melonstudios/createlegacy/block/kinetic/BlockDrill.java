package com.melonstudios.createlegacy.block.kinetic;

import com.melonstudios.createapi.CreateAPI;
import com.melonstudios.createlegacy.block.IGoggleInfo;
import com.melonstudios.createlegacy.block.IWrenchable;
import com.melonstudios.createlegacy.tileentity.TileEntityDrill;
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
import java.util.Random;

public class BlockDrill extends AbstractBlockKinetic implements IWrenchable, IGoggleInfo {
    public BlockDrill() {
        super("drill");

        needsRandomTick = true;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced) {
        super.addInformation(stack, player, tooltip, advanced);
        tooltip.add(CreateAPI.stressImpactTooltip(6));
    }

    @Override
    public NonNullList<String> getGoggleInformation(World world, BlockPos pos, IBlockState state) {
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof TileEntityDrill) {
            TileEntityDrill drill = (TileEntityDrill) te;
            if (drill.speed() != 0) return NonNullList.from("", drill.stressGoggleInfo());
        }
        return IGoggleInfo.EMPTY;
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        super.updateTick(worldIn, pos, state, rand);

        TileEntityDrill drill = (TileEntityDrill) worldIn.getTileEntity(pos);

        if (drill != null) drill.recalculate();
    }

    public static final PropertyEnum<EnumFacing> FACING = PropertyEnum.create("facing", EnumFacing.class);

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(FACING).getIndex();
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(FACING, EnumFacing.getFront(meta));
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        if (placer.isSneaking()) return getDefaultState().withProperty(FACING, facing.getOpposite());
        return getDefaultState().withProperty(FACING, EnumFacing.getDirectionFromEntityLiving(pos, placer));
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityDrill();
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    public boolean onWrenched(World world, BlockPos pos, IBlockState state, EnumFacing side, EntityPlayer wrenchHolder) {
        EnumFacing facing = state.getValue(FACING);
        if (facing.getAxis().apply(side)) return false;
        world.setBlockState(pos, state.withProperty(FACING, facing.rotateAround(side.getAxis())));
        return true;
    }
}
