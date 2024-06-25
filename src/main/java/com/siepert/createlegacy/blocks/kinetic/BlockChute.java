package com.siepert.createlegacy.blocks.kinetic;

import com.google.common.collect.Lists;
import com.siepert.createlegacy.CreateLegacy;
import com.siepert.createlegacy.mainRegistry.ModBlocks;
import com.siepert.createlegacy.mainRegistry.ModItems;
import com.siepert.createlegacy.util.IHasModel;
import com.siepert.createapi.IWrenchable;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class BlockChute extends Block implements IHasModel, IWrenchable {
    public static final PropertyBool WINDOW = PropertyBool.create("window");
    private static final double thing0 = 1.0 / 16.0;
    private static final double thing1 = 15.0 / 16.0;
    public static final AxisAlignedBB BB_X_POS = new AxisAlignedBB(thing0, 0.0, thing0, 2.0 / 16.0, 1.0, thing1);
    public static final AxisAlignedBB BB_X_NEG = new AxisAlignedBB(thing1, 0.0, 0.0, thing1, 1.0, thing1);
    public static final AxisAlignedBB BB_Z_POS = new AxisAlignedBB(thing0, 0.0, thing0, thing1, 1.0, 2.0 / 16.0);
    public static final AxisAlignedBB BB_Z_NEG = new AxisAlignedBB(thing0, 0.0, 14.0 / 16.0, thing1, 1.0, thing1);
    public BlockChute(String name) {
        super(Material.IRON, MapColor.BLACK);
        this.translucent = true;
        this.blockSoundType = SoundType.METAL;
        this.fullBlock = false;
        setLightOpacity(0);

        setDefaultState(this.blockState.getBaseState().withProperty(WINDOW, false));

        setUnlocalizedName("create:" + name);
        setRegistryName(name);
        setCreativeTab(CreateLegacy.TAB_CREATE);
        setHarvestLevel("pickaxe", 0);
        setHardness(1);
        setResistance(2);
        ModBlocks.BLOCKS.add(this);
        ModItems.ITEMS.add(new ItemBlock(this).setRegistryName(this.getRegistryName()));
    }

    @Override
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        if (state.getValue(WINDOW)) return 1;
        return 0;
    }
    public static final AxisAlignedBB bb = new AxisAlignedBB(thing0, 0.0, thing0, thing1, 1.0, thing1);
    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return bb;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        if (meta == 1) return this.getDefaultState().withProperty(WINDOW, true);
        return this.getDefaultState().withProperty(WINDOW, false);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] {WINDOW});
    }

    @Override
    public IBlockState getStateForPlacement(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        return this.getDefaultState();
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
    public boolean onWrenched(World worldIn, BlockPos pos, IBlockState state, EnumFacing side, EntityPlayer playerIn) {
        worldIn.setBlockState(pos, state.cycleProperty(WINDOW));
        return true;
    }

    @Override
    public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean isActualState) {


        for (AxisAlignedBB axisalignedbb : getCollisionBoxList())
        {
            addCollisionBoxToList(pos, entityBox, collidingBoxes, axisalignedbb);
        }
    }


    private static List<AxisAlignedBB> getCollisionBoxList() {
        List<AxisAlignedBB> list = Lists.<AxisAlignedBB>newArrayList();

        list.add(BB_X_POS);
        list.add(BB_Z_POS);
        list.add(BB_X_NEG);
        list.add(BB_Z_NEG);

        return list;
    }
}
