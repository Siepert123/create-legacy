package com.siepert.createlegacy.blocks.kinetic;

import com.siepert.createlegacy.CreateLegacy;
import com.siepert.createlegacy.mainRegistry.ModBlocks;
import com.siepert.createlegacy.mainRegistry.ModItems;
import com.siepert.createlegacy.util.IHasModel;
import com.siepert.createlegacy.util.IHasRotation;
import com.siepert.createlegacy.util.IKineticActor;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("deprecation")
public class BlockWaterWheel extends Block implements IHasModel, IKineticActor, ITileEntityProvider {
    public static final PropertyEnum<EnumFacing.Axis> AXIS = PropertyEnum.create("axis", EnumFacing.Axis.class);

    public BlockWaterWheel(String name) {
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
        ModItems.ITEMS.add(new ItemBlock(this).setRegistryName(this.getRegistryName()));


    }

    @Override
    public void registerModels() {
        CreateLegacy.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory");
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        if (state.getValue(AXIS) == EnumFacing.Axis.Z) return 1;
        return 0;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        if (meta == 0) return this.getDefaultState().withProperty(AXIS, EnumFacing.Axis.X);
        if (meta == 1) return this.getDefaultState().withProperty(AXIS, EnumFacing.Axis.Z);
        CreateLegacy.logger.error("Illegal meta for block Water Wheel: {}", meta);
        return this.getStateFromMeta(0);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] {AXIS});
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing,
                                            float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        return this.getDefaultState().withProperty(AXIS, placer.getHorizontalFacing().getAxis());
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
    public void passRotation(World worldIn, BlockPos pos, EnumFacing source, List<BlockPos> iteratedBlocks, boolean srcIsCog, boolean srcCogIsHorizontal) {

        if (srcIsCog) return;

        IBlockState myState = worldIn.getBlockState(pos);

        if (source.getAxis() != myState.getValue(AXIS)) return;

        iteratedBlocks.add(pos);

        Block block = worldIn.getBlockState(pos.offset(source.getOpposite())).getBlock();
        if (block instanceof IKineticActor) {
            ((IKineticActor) block).passRotation(worldIn, pos, source, iteratedBlocks, false, false);
        }
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return null;
    }
}
