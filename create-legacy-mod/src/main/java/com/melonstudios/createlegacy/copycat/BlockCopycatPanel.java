package com.melonstudios.createlegacy.copycat;

import com.melonstudios.createlegacy.CreateLegacy;
import epicsquid.mysticallib.model.block.BakedModelBlock;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
@SuppressWarnings("deprecation")
public class BlockCopycatPanel extends BlockCopycatBase implements ICopycatBlock, ITileEntityProvider {
    public BlockCopycatPanel() {
        super("copycat_panel");
    }

    @Override
    public IUnlistedProperty<IBlockState> getStateProperty() {
        return STATE_PROP;
    }

    public static final PropertyEnum<EnumFacing> FACING = PropertyEnum.create("facing", EnumFacing.class);

    @Override
    protected BlockStateContainer createBlockState() {
        return new ExtendedBlockState(this, new IProperty[]{FACING}, new IUnlistedProperty[]{STATE_PROP});
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(FACING).getIndex();
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(FACING, EnumFacing.getFront(meta % 6));
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        IBlockState placedOn = world.getBlockState(pos.offset(facing.getOpposite()));
        if (placedOn.getBlock() instanceof BlockCopycatPanel && !placer.isSneaking()) {
            return getDefaultState().withProperty(FACING, placedOn.getValue(FACING));
        } else {
            if (placer.isSneaking()) {
                return getDefaultState().withProperty(FACING, facing);
            } else {
                return getDefaultState().withProperty(FACING, EnumFacing.getDirectionFromEntityLiving(pos, placer));
            }
        }
    }

    @Override
    public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos) {
        TileEntity entity = world.getTileEntity(pos);
        IBlockState actual = getActualState(state, world, pos);
        if (entity instanceof TileEntityCopycat && actual instanceof IExtendedBlockState) {
            return ((IExtendedBlockState) actual).withProperty(STATE_PROP, ((TileEntityCopycat) entity).state);
        }
        return state;
    }

    private static final UnlistedPropertyState STATE_PROP = new UnlistedPropertyState();

    public static class UnlistedPropertyState implements IUnlistedProperty<IBlockState> {

        @Override
        @Nonnull
        public String getName() {
            return "stateprop";
        }

        @Override
        public boolean isValid(@Nonnull IBlockState value) {
            return true;
        }

        @Override
        @Nonnull
        public Class<IBlockState> getType() {
            return IBlockState.class;
        }

        @Override
        @Nonnull
        public String valueToString(@Nonnull IBlockState value) {
            return value.toString();
        }
    }

    @Nonnull
    @Override
    protected Class<? extends BakedModelBlock> getModelClass() {
        return BakedModelCopycatPanel.class;
    }
}
