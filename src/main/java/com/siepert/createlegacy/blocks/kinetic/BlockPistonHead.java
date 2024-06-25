package com.siepert.createlegacy.blocks.kinetic;

import com.siepert.createapi.IKineticActor;
import com.siepert.createapi.IWrenchable;
import com.siepert.createlegacy.CreateLegacy;
import com.siepert.createlegacy.mainRegistry.ModBlocks;
import com.siepert.createlegacy.mainRegistry.ModItems;
import com.siepert.createlegacy.util.IHasModel;
import com.siepert.createlegacy.util.Reference;
import com.siepert.createlegacy.util.handlers.recipes.WashingRecipes;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@SuppressWarnings("deprecation")
@MethodsReturnNonnullByDefault
public class BlockPistonHead extends Block implements IHasModel {
    public static final PropertyEnum<EnumFacing> FACING = PropertyEnum.create("facing", EnumFacing.class);
    public BlockPistonHead(String name) {
        super(Material.ROCK);
        this.translucent = true;
        this.blockSoundType = SoundType.WOOD;
        this.fullBlock = false;
        setLightOpacity(0);

        setUnlocalizedName("create:" + name);
        setRegistryName(name);
        setCreativeTab(CreateLegacy.TAB_CREATE);
        setHarvestLevel("axe", 0);
        setHardness(1);
        setResistance(2);
        ModBlocks.BLOCKS.add(this);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(FACING).getIndex();
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(FACING, EnumFacing.getFront(meta));
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] {FACING});
    }

    @Override
    public void registerModels() {
        CreateLegacy.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory");
    }

    @Override
    public boolean isFullBlock(IBlockState state) {
        return false;
    }
    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }
    @Override
    public boolean isTranslucent(IBlockState state) {
        return true;
    }
    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        EnumFacing along = state.getValue(FACING).getOpposite();

        BlockPos.MutableBlockPos blockPos = (BlockPos.MutableBlockPos) pos.offset(along);
        IBlockState state1 = worldIn.getBlockState(blockPos);

        while (state1.getBlock() == ModBlocks.PISTON_ERECTOR) {
            if (state1.getValue(BlockPistonErector.AXIS) != state.getValue(FACING).getAxis()) {
                worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
                ModBlocks.PISTON_ERECTOR.dropBlockAsItem(worldIn, pos, ModBlocks.PISTON_ERECTOR.getDefaultState(), 0);
                return;
            }
            blockPos.move(along);
            state1 = worldIn.getBlockState(blockPos);
        }
        if (state1.getBlock() != ModBlocks.PISTON) {
            worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
            ModBlocks.PISTON_ERECTOR.dropBlockAsItem(worldIn, pos, ModBlocks.PISTON_ERECTOR.getDefaultState(), 0);
            return;
        }
        if (state1.getValue(FACING) != state.getValue(FACING)) {
            worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
            ModBlocks.PISTON_ERECTOR.dropBlockAsItem(worldIn, pos, ModBlocks.PISTON_ERECTOR.getDefaultState(), 0);
        }
    }
}
