package nl.melonstudios.create.block.funnel;

import com.melonstudios.melonlib.misc.AABB;
import com.melonstudios.melonlib.misc.BlockStateProperties;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import nl.melonstudios.create.block.state.EnumFunnelState;
import nl.melonstudios.create.kinetics.contraption.IWrenchable;
import nl.melonstudios.create.tileentity.funnel.TileEntityFunnelBase;
import nl.melonstudios.create.tileentity.funnel.TileEntityFunnelWall;
import nl.melonstudios.create.tileentity.funnel.TileEntityFunnelWallAdvanced;
import nl.melonstudios.create.tileentity.marker.IDepot;
import nl.melonstudios.create.tileentity.marker.ITopOpenInventory;
import nl.melonstudios.create.util.FunnelSet;

import javax.annotation.Nullable;
import java.util.List;

@SuppressWarnings("deprecation")
public class BlockFunnelWall extends BlockFunnelBase implements IWrenchable {
    public static final PropertyDirection FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final PropertyEnum<EnumFunnelState> FUNNEL_STATE = EnumFunnelState.STATE_PROPERTY;
    public static final PropertyBool TALL = PropertyBool.create("tall");

    public BlockFunnelWall(String set, boolean advanced) {
        super(set, advanced);
    }

    @Override
    protected void addStateProperties(List<IProperty<?>> properties) {
        super.addStateProperties(properties);
        properties.add(FACING);
        properties.add(FUNNEL_STATE);
        properties.add(TALL);
    }

    public static final AxisAlignedBB[] BOUNDING_BOXES = {
            AABB.create(0, 0, 0, 16, 16, 6),
            AABB.create(10, 0, 0, 16, 16, 16),
            AABB.create(0, 0, 10, 16, 16, 16),
            AABB.create(0, 0, 0, 6, 16, 16)
    };

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return BOUNDING_BOXES[state.getValue(FACING).getHorizontalIndex()];
    }

    @Nullable
    @Override
    public TileEntityFunnelBase createNewTileEntity(World worldIn, int meta) {
        return this.isAdvanced ? new TileEntityFunnelWallAdvanced() : new TileEntityFunnelWall();
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState()
                .withProperty(FACING, EnumFacing.getHorizontal(meta & 3))
                .withProperty(FUNNEL_STATE, (meta & 0b0100) != 0 ? EnumFunnelState.EXTRACTING : EnumFunnelState.INSERTING)
                .withProperty(POWERED, (meta & 0b1000) != 0);
    }
    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(FACING).getHorizontalIndex() |
                (state.getValue(FUNNEL_STATE).getId() << 2) |
                (state.getValue(POWERED) ? 0b1000 : 0b0000);
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        TileEntity below = worldIn.getTileEntity(pos.down());
        return state.withProperty(TALL, below instanceof ITopOpenInventory || below instanceof IDepot);
    }

    @Override
    public boolean onWrenched(World world, BlockPos pos, IBlockState state, EnumFacing side, float hitX, float hitY, float hitZ) {
        world.setBlockState(pos, state.cycleProperty(FUNNEL_STATE));
        return true;
    }
}
