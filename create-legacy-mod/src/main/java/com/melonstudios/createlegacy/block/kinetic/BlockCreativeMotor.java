package com.melonstudios.createlegacy.block.kinetic;

import com.melonstudios.createapi.CreateAPI;
import com.melonstudios.createlegacy.block.IGoggleInfo;
import com.melonstudios.createlegacy.tileentity.TileEntityCreativeMotor;
import com.melonstudios.createlegacy.tileentity.TileEntityFan;
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

public class BlockCreativeMotor extends AbstractBlockKinetic implements IGoggleInfo {
    public BlockCreativeMotor() {
        super("creative_motor");
    }

    public static final PropertyEnum<EnumFacing> FACING = PropertyEnum.create("facing", EnumFacing.class);

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING);
    }

    @Override
    public NonNullList<String> getGoggleInformation(World world, BlockPos pos, IBlockState state) {
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof TileEntityCreativeMotor) {
            TileEntityCreativeMotor motor = (TileEntityCreativeMotor) te;
            if (motor.generatedRPM() != 0) return NonNullList.from("", motor.capacityGoggleInfo());
        }
        return IGoggleInfo.EMPTY;
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
        if (!placer.isSneaking()) {
            return getDefaultState().withProperty(FACING, EnumFacing.getDirectionFromEntityLiving(pos, placer));
        }
        return getDefaultState().withProperty(FACING, facing.getOpposite());
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityCreativeMotor();
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced) {
        super.addInformation(stack, player, tooltip, advanced);
        tooltip.add(CreateAPI.stressCapacityTooltip(Short.MAX_VALUE));
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (facing == state.getValue(FACING).getOpposite()) {
            TileEntity te = worldIn.getTileEntity(pos);
            if (te instanceof TileEntityCreativeMotor) {
                TileEntityCreativeMotor motor = (TileEntityCreativeMotor) te;
                boolean flag;
                if (playerIn.isSneaking()) {
                    flag = motor.decreaseRequestedSpeed();
                } else {
                    flag = motor.increaseRequestedSpeed();
                }
                motor.updateClients();
                return flag;
            }
        }
        return false;
    }

    @Override
    public boolean onWrenched(World world, BlockPos pos, IBlockState state, EnumFacing side, EntityPlayer wrenchHolder) {
        if (state.getValue(FACING).getAxis().apply(side)) return false;
        TileEntity te = world.getTileEntity(pos);
        world.setBlockState(pos, state.withProperty(FACING, state.getValue(FACING).rotateAround(side.getAxis())));
        if (te != null) {
            te.validate();
            world.setTileEntity(pos, te);
        }
        return true;
    }
}
