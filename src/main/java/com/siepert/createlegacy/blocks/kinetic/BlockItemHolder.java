package com.siepert.createlegacy.blocks.kinetic;

import com.google.common.collect.Lists;
import com.siepert.createlegacy.CreateLegacy;
import com.siepert.createlegacy.blocks.item.ItemBlockVariants;
import com.siepert.createlegacy.mainRegistry.ModBlocks;
import com.siepert.createlegacy.mainRegistry.ModItems;
import com.siepert.createlegacy.util.IHasModel;
import com.siepert.createlegacy.util.IMetaName;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

@SuppressWarnings("deprecation")
@MethodsReturnNonnullByDefault
public class BlockItemHolder extends Block implements IHasModel, IMetaName {
    public static final AxisAlignedBB HITBOX_DEPOT = new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 13.0 / 16.0, 1.0);

    public static final AxisAlignedBB HITBOX_BASIN_DOWN = new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 2.0 / 16.0, 1.0);
    public static final AxisAlignedBB HITBOX_BASIN_X_POS = new AxisAlignedBB(0.0, 0.0, 0.0, 2.0 / 16.0, 1.0, 1.0);
    public static final AxisAlignedBB HITBOX_BASIN_X_NEG = new AxisAlignedBB(14.0 / 16.0, 0.0, 0.0, 1.0, 1.0, 1.0);
    public static final AxisAlignedBB HITBOX_BASIN_Z_POS = new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 1.0, 2.0 / 16.0);
    public static final AxisAlignedBB HITBOX_BASIN_Z_NEG = new AxisAlignedBB(0.0, 0.0, 14.0 / 16.0, 1.0, 1.0, 1.0);

    @Override
    public String getSpecialName(ItemStack stack) {
        if (stack.getItemDamage() == 0) {
            return Variant.DEPOT.unlocalizedName;
        }
        return Variant.BASIN.unlocalizedName;
    }

    @Override
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    public enum Variant implements IStringSerializable {
        DEPOT(0, "depot"), BASIN(1, "basin");

        Variant(int meta, String unlocalizedName) {
            this.meta = meta;
            this.unlocalizedName = unlocalizedName;
        }
        final int meta;
        final String unlocalizedName;

        @Override
        public String getName() {
            return unlocalizedName;
        }

        public static Variant defaultState() {
            return Variant.DEPOT;
        }

        public static Variant fromMeta(int meta) {
            for (Variant variant : Variant.values()) {
                if (variant.meta == meta) return variant;
            }
            return defaultState();
        }
        public int toMeta() {
            return this.meta;
        }
    }
    public static final PropertyEnum<Variant> VARIANT = PropertyEnum.create("variant", Variant.class);
    public BlockItemHolder(String name) {
        super(Material.ROCK);
        this.translucent = true;
        this.blockSoundType = SoundType.WOOD;
        this.fullBlock = false;
        setLightOpacity(0);

        setUnlocalizedName("create:");
        setRegistryName(name);
        setCreativeTab(CreateLegacy.TAB_CREATE);

        setHarvestLevel("axe", 0);

        setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, Variant.DEPOT));

        setHardness(1);
        setResistance(2);
        ModBlocks.BLOCKS.add(this);
        ModItems.ITEMS.add(new ItemBlockVariants(this).setRegistryName(this.getRegistryName()));
    }




    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(VARIANT).toMeta();
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(VARIANT, Variant.fromMeta(meta));
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] {VARIANT});
    }

    @Override
    public IBlockState getStateForPlacement(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        return this.getDefaultState().withProperty(VARIANT, Variant.fromMeta(placer.getHeldItem(hand).getItemDamage()));
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        doTheItemThingy(worldIn, pos, state, true);
        return true;
    }

    @Override
    public void onFallenUpon(World worldIn, BlockPos pos, Entity entityIn, float fallDistance) {
        IBlockState myState = worldIn.getBlockState(pos);
        doTheItemThingy(worldIn, pos, myState, false);
    }

    private void doTheItemThingy(World worldIn, BlockPos pos, IBlockState state, boolean pickupDelay) {
        if (!worldIn.isRemote) {
            boolean isBasin = state.getValue(VARIANT) == Variant.BASIN;
            if (isBasin) {
                AxisAlignedBB searchPlace = new AxisAlignedBB(pos);
                List<EntityItem> itemsToRelocate = worldIn.getEntitiesWithinAABB(EntityItem.class, searchPlace);

                for (EntityItem entityItem : itemsToRelocate) {
                    if (!(entityItem.posX == pos.getX() + 0.5 && entityItem.posZ == pos.getZ() + 0.5)) {
                        entityItem.setVelocity(0, 0, 0);
                        entityItem.setPosition(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
                        if (pickupDelay) entityItem.setPickupDelay(50);
                        entityItem.setNoDespawn();
                    }
                }
            } else {
                AxisAlignedBB searchPlace = new AxisAlignedBB(pos.up());
                List<EntityItem> itemsToRelocate = worldIn.getEntitiesWithinAABB(EntityItem.class, searchPlace);

                for (EntityItem entityItem : itemsToRelocate) {
                    if (!(entityItem.posX == pos.getX() + 0.5 && entityItem.posZ == pos.getZ() + 0.5)) {
                        entityItem.setVelocity(0, 0, 0);
                        entityItem.setPosition(pos.getX() + 0.5, pos.up().getY(), pos.getZ() + 0.5);
                        if (pickupDelay) entityItem.setPickupDelay(50);
                        entityItem.setNoDespawn();
                    }
                }
            }
        }
    }

    @Override
    public SoundType getSoundType(IBlockState state, World world, BlockPos pos, @Nullable Entity entity) {
        switch (state.getValue(VARIANT)) {
            case BASIN:
                return SoundType.ANVIL;
            case DEPOT:
                return SoundType.WOOD;
        }
        return SoundType.WOOD;
    }

    @Override
    public void registerModels() {
        CreateLegacy.proxy.registerVariantRenderer(Item.getItemFromBlock(this), 0,
                "item_holder/depot", "inventory");
        CreateLegacy.proxy.registerVariantRenderer(Item.getItemFromBlock(this), 1,
                "item_holder/basin", "inventory");
    }

    @Override
    public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items) {
        items.add(new ItemStack(this, 1, 0));
        items.add(new ItemStack(this, 1, 1));
    }

    @Override
    public int damageDropped(IBlockState state) {
        return state.getValue(VARIANT).toMeta();
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
    public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean isActualState) {


        for (AxisAlignedBB axisalignedbb : getCollisionBoxList(state))
        {
            addCollisionBoxToList(pos, entityBox, collidingBoxes, axisalignedbb);
        }
    }


    private static List<AxisAlignedBB> getCollisionBoxList(IBlockState bstate) {
        List<AxisAlignedBB> list = Lists.<AxisAlignedBB>newArrayList();
        boolean flag = bstate.getValue(VARIANT) == Variant.BASIN;
        if (flag) {
            list.add(HITBOX_BASIN_X_POS);
            list.add(HITBOX_BASIN_Z_POS);
            list.add(HITBOX_BASIN_X_NEG);
            list.add(HITBOX_BASIN_Z_NEG);
            list.add(HITBOX_BASIN_DOWN);
        } else list.add(HITBOX_DEPOT);

        return list;
    }

    private static final AxisAlignedBB BB_DEPOT = new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 13.0 / 16.0, 1.0);

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        if (state.getValue(VARIANT) == Variant.DEPOT) return BB_DEPOT;
        return Block.FULL_BLOCK_AABB;
    }
}
