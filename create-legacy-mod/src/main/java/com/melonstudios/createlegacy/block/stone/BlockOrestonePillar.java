package com.melonstudios.createlegacy.block.stone;

import com.melonstudios.createlegacy.block.ModBlocks;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public final class BlockOrestonePillar extends AbstractBlockOrestone {
    public BlockOrestonePillar(EnumFacing.Axis axis) {
        super("orestone_pillar_" + axis.getName());
    }

    @Override
    protected String getOrestonePrefix() {
        return "orestone_pillar";
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        if (!placer.isSneaking() && world.getBlockState(pos.offset(facing.getOpposite())).getBlock() instanceof BlockOrestonePillar) {
            return world.getBlockState(pos.offset(facing.getOpposite())).withProperty(STONE_TYPE, StoneType.fromID(meta));
        }
        switch (facing.getAxis()) {
            case X:
                return ModBlocks.ORESTONE_PILLAR_X.getDefaultState()
                        .withProperty(STONE_TYPE, StoneType.fromID(meta));
            case Y:
                return ModBlocks.ORESTONE_PILLAR_Y.getDefaultState()
                        .withProperty(STONE_TYPE, StoneType.fromID(meta));
            case Z:
                return ModBlocks.ORESTONE_PILLAR_Z.getDefaultState()
                        .withProperty(STONE_TYPE, StoneType.fromID(meta));
        }
        return getDefaultState().withProperty(STONE_TYPE, StoneType.fromID(meta));
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        return new ItemStack(ModBlocks.ORESTONE_PILLAR_Y, 1, state.getValue(STONE_TYPE).getID());
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Item.getItemFromBlock(ModBlocks.ORESTONE_PILLAR_Y);
    }

    @Override
    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
        return new ItemStack(ModBlocks.ORESTONE_PILLAR_Y, 1, state.getValue(STONE_TYPE).getID());
    }
}
