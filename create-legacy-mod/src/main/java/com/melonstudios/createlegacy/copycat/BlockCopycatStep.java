package com.melonstudios.createlegacy.copycat;

import com.melonstudios.melonlib.misc.AABB;
import net.minecraft.block.properties.PropertyEnum;
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

public class BlockCopycatStep extends BlockCopycat {
    public static final PropertyEnum<EnumFacing> FACING = PropertyEnum.create(
            "facing", EnumFacing.class, EnumFacing.HORIZONTALS
    );

    private static final AxisAlignedBB[] boundingBoxes = {
            AABB.create(0, 0, 0, 16, 8, 8),
            AABB.create(8, 0, 0, 16, 8, 16),
            AABB.create(0, 0, 8, 16, 8, 16),
            AABB.create(0, 0, 0, 8, 8, 16),
    };

    public BlockCopycatStep() {
        super("copycat_step");
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, COPYCATTING, FACING);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return boundingBoxes[state.getValue(FACING).getHorizontalIndex()];
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        if (!placer.isSneaking()) {
            if (world.getBlockState(pos.offset(facing.getOpposite())).getBlock() instanceof BlockCopycatStep) {
                return world.getBlockState(pos.offset(facing.getOpposite())).withProperty(COPYCATTING, false);
            }
        }
        return getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite());
    }

    @Override
    public void render(IBlockState state, TileEntityCopycat te, double x, double y, double z) {
        GlStateManager.translate(x, y, z);
        switch (state.getValue(FACING)) {
            case NORTH: {
                GlStateManager.translate(0, 0, 0.5);
                GlStateManager.scale(1, 0.5, 0.5);
                break;
            }
            case SOUTH: {
                GlStateManager.scale(1, 0.5, 0.5);
                break;
            }
            case EAST: {
                GlStateManager.scale(0.5, 0.5, 1);
                break;
            }
            case WEST: {
                GlStateManager.translate(0.5, 0, 0);
                GlStateManager.scale(0.5, 0.5, 1);
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
        return (state.getValue(COPYCATTING) ? 4 : 0) + state.getValue(FACING).getIndex();
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        boolean copycatted = meta / 4 != 0;
        EnumFacing facing = EnumFacing.getHorizontal(meta % 4);
        return getDefaultState().withProperty(COPYCATTING, copycatted).withProperty(FACING, facing);
    }
}
