package com.melonstudios.createlegacy.block;

import com.melonstudios.createlegacy.CreateLegacy;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
@SuppressWarnings("deprecation")
public class BlockBlazeBurnerDeco extends Block {
    public BlockBlazeBurnerDeco() {
        super(Material.IRON);

        setRegistryName("blaze_burner_lit");
        setUnlocalizedName("create.blaze_burner_lit");

        setHarvestLevel("pickaxe", 1);

        setHardness(2.0f);
        setResistance(6.0f);

        setTickRandomly(true);

        setSoundType(SoundType.METAL);
        setCreativeTab(CreateLegacy.TAB_KINETICS);
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        return new ItemStack(ModBlocks.BLAZE_BURNER, 1, 0);
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Item.getItemFromBlock(ModBlocks.BLAZE_BURNER);
    }

    @Override
    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
        return new ItemStack(ModBlocks.BLAZE_BURNER, 1, 0);
    }

    @Override
    public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
        return 15;
    }

    @Override
    public int getLightValue(IBlockState state) {
        return 15;
    }

    @Override
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        for (int i = 0; i < 5; i++) {
            worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL,
                    pos.getX() + rand.nextDouble(),
                    pos.getY() + 0.2 + rand.nextDouble() * 0.4,
                    pos.getZ() + rand.nextDouble(),
                    0, rand.nextDouble() * 0.01, 0);
        }
    }

    @Override
    public void randomTick(World worldIn, BlockPos pos, IBlockState state, Random random) {
        worldIn.playSound(null, pos,
                SoundEvents.BLOCK_FIRE_AMBIENT, SoundCategory.BLOCKS,
                1, 0.8f + random.nextFloat() * 0.4f);
    }

    @Override
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    //region ?
    @Override
    public int getLightOpacity(IBlockState state, IBlockAccess world, BlockPos pos) {
        return 0;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullBlock(IBlockState state) {
        return false;
    }

    @Override
    public boolean isNormalCube(IBlockState state, IBlockAccess world, BlockPos pos) {
        return false;
    }

    @Override
    public boolean isTranslucent(IBlockState state) {
        return true;
    }

    @Override
    public boolean isNormalCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }
    //endregion
}
