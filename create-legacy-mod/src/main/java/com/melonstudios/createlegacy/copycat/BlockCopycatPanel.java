package com.melonstudios.createlegacy.copycat;

import com.melonstudios.createlegacy.CreateLegacy;
import com.melonstudios.melonlib.misc.AABB;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockCopycatPanel extends BlockCopycat {
    public static final PropertyDirection FACING = PropertyDirection.create("facing");

    private static final AxisAlignedBB[] boundingBoxes = {
            AABB.create(0, 13, 0, 16, 16, 16),
            AABB.create(0, 0, 0, 16, 3, 16),
            AABB.create(0, 0, 13, 16, 16, 16),
            AABB.create(0, 0, 0, 16, 16, 3),
            AABB.create(13, 0, 0, 16, 16, 16),
            AABB.create(0, 0, 0, 3, 16, 16),
    };

    public BlockCopycatPanel() {
        super("copycat_panel");
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, COPYCATTING, FACING);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return boundingBoxes[state.getValue(FACING).getIndex()];
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        if (placer.isSneaking()) {
            return getDefaultState().withProperty(FACING, facing);
        }
        if (world.getBlockState(pos.offset(facing.getOpposite())).getBlock() instanceof BlockCopycatPanel) {
            return world.getBlockState(pos.offset(facing.getOpposite())).withProperty(COPYCATTING, false);
        }
        return getDefaultState().withProperty(FACING, EnumFacing.getDirectionFromEntityLiving(pos, placer));
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        boolean copycatted = meta / 6 != 0;
        EnumFacing facing = EnumFacing.getFront(meta % 6);
        return getDefaultState().withProperty(COPYCATTING, copycatted).withProperty(FACING, facing);
    }

    private void scale(int x, int y, int z) {
        GlStateManager.scale(x / 16.0, y / 16.0, z / 16.0);
    }
    private void offset(int x, int y, int z) {
        GlStateManager.translate(x / 16.0, y / 16.0, z / 16.0);
    }

    @Override
    public void render(IBlockState state, TileEntityCopycat te, double x, double y, double z) {
        GlStateManager.translate(x, y, z);
        switch (state.getValue(FACING)) {
            case DOWN: {
                offset(0, 13, 0);
                scale(16, 3, 16);
                break;
            }
            case UP: {
                scale(16, 3, 16);
                break;
            }
            case NORTH: {
                offset(0, 0, 13);
                scale(16, 16, 3);
                break;
            }
            case SOUTH: {
                scale(16, 16, 3);
                break;
            }
            case EAST: {
                scale(3, 16, 16);
                break;
            }
            case WEST: {
                offset(13, 0, 0);
                scale(3, 16, 16);
                break;
            }
        }

        IBakedModel model = Minecraft.getMinecraft()
                .getBlockRendererDispatcher().getModelForState(te.copyState);
        GlStateManager.rotate(-90, 0, 1, 0);
        Minecraft.getMinecraft().getBlockRendererDispatcher()
                .getBlockModelRenderer().renderModelBrightness(model, te.copyState, 1.0f, true);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return (state.getValue(COPYCATTING) ? 6 : 0) + state.getValue(FACING).getIndex();
    }
}
