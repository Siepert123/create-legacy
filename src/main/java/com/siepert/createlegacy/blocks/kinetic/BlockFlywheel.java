package com.siepert.createlegacy.blocks.kinetic;

import com.siepert.createlegacy.CreateLegacy;
import com.siepert.createlegacy.blocks.item.ItemBlockVariants;
import com.siepert.createlegacy.mainRegistry.ModBlocks;
import com.siepert.createlegacy.mainRegistry.ModItems;
import com.siepert.createlegacy.util.IHasModel;
import com.siepert.createlegacy.util.IHasRotation;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockFlywheel extends Block implements IHasModel, IHasRotation, ITileEntityProvider {
    public enum Variant implements IStringSerializable {
        ENGINE(0, "engine"), FLYWHEEL(1, "flywheel");
        public final int meta;
        public final String name;
        Variant(int meta, String name) {
            this.meta = meta;
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }

        public static Variant fromMeta(int meta) {
            for (Variant v : Variant.values()) {
                if (v.meta == meta) {
                    return v;
                }
            }
            return Variant.ENGINE;
        }
    }
    public static final PropertyEnum<Variant> VARIANT = PropertyEnum.create("variant", Variant.class);
    public BlockFlywheel() {
        super(Material.IRON);
        this.translucent = true;
        this.blockSoundType = SoundType.METAL;
        this.fullBlock = false;
        setLightOpacity(0);

        setUnlocalizedName("create:" + "furnace_");
        setRegistryName("furnace_engine");
        setCreativeTab(CreateLegacy.TAB_CREATE);
        setHarvestLevel("pickaxe", 0);
        setHardness(2);
        setResistance(3);
        ModBlocks.BLOCKS.add(this);
        ModItems.ITEMS.add(new ItemBlockVariants(this).setRegistryName(this.getRegistryName()));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(VARIANT).meta;
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
    public void registerModels() {
        CreateLegacy.proxy.registerVariantRenderer(Item.getItemFromBlock(this), 0, "furnace_engine", "inventory");
        CreateLegacy.proxy.registerVariantRenderer(Item.getItemFromBlock(this), 1, "flywheel", "inventory");
    }

    @Override
    public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items) {
        items.add(new ItemStack(Item.getItemFromBlock(this), 1, 0));
        items.add(new ItemStack(Item.getItemFromBlock(this), 1, 1));
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        if (state.getValue(VARIANT) == Variant.ENGINE) return new ItemStack(this, 1, 0);
        return new ItemStack(this, 1, 1);
    }


    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return null;
    }
}
