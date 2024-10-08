package com.siepert.createlegacy.blocks.kinetic;

import com.siepert.createapi.CreateAPI;
import com.siepert.createlegacy.CreateLegacy;
import com.siepert.createlegacy.CreateLegacyConfigHolder;
import com.siepert.createlegacy.blocks.KineticBlock;
import com.siepert.createlegacy.mainRegistry.ModBlocks;
import com.siepert.createlegacy.mainRegistry.ModItems;
import com.siepert.createlegacy.tileentity.TileEntityFan;
import com.siepert.createlegacy.util.IHasModel;
import com.siepert.createapi.IKineticActor;
import com.siepert.createapi.IWrenchable;
import com.siepert.createlegacy.CreateLegacyModData;
import com.siepert.createlegacy.util.handlers.recipes.WashingRecipes;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
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
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("deprecation")
@MethodsReturnNonnullByDefault
public class BlockFan extends KineticBlock implements IHasModel, IWrenchable, ITileEntityProvider {
    public static final PropertyEnum<EnumFacing> FACING = PropertyEnum.create("facing", EnumFacing.class);
    public BlockFan(String name) {
        super(name);
        this.translucent = true;
        this.blockSoundType = SoundType.WOOD;
        this.fullBlock = false;
        setLightOpacity(0);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced) {
        tooltip.add(CreateAPI.stressImpactTooltip(CreateLegacyConfigHolder.kineticConfig.encasedFanStressImpact));
    }

    @Override
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public int getMetaFromState(@Nonnull IBlockState state) {
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
    public IBlockState getStateForPlacement(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull EnumFacing facing,
                                            float hitX, float hitY, float hitZ, int meta, @Nonnull EntityLivingBase placer, EnumHand hand) {
        if (placer != null) {
            if (placer.isSneaking()) {
                return super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer, hand).withProperty(FACING, facing.getOpposite());
            }
            return super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer, hand).withProperty(FACING,
                EnumFacing.getFacingFromVector((float) placer.getLookVec().x, (float) placer.getLookVec().y, (float) placer.getLookVec().z)
                        .getOpposite());
        }
        return super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer, hand).withProperty(FACING, facing.getOpposite());
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
    public boolean rotateBlock(World world, BlockPos pos, EnumFacing side) {
        IBlockState state = world.getBlockState(pos);

        if (side.getAxis() == state.getValue(FACING).getAxis()) return false;

        world.setBlockState(pos, state.withProperty(FACING, state.getValue(FACING).rotateAround(side.getAxis())), 3);

        return true;
    }
    @Override
    public boolean onWrenched(World worldIn, BlockPos pos, IBlockState state, EnumFacing side, EntityPlayer playerIn) {
        return rotateBlock(worldIn, pos, side);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityFan();
    }
}
