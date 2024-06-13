package com.siepert.createlegacy.blocks.kinetic;

import com.siepert.createlegacy.CreateLegacy;
import com.siepert.createlegacy.blocks.item.ItemBlockBlazeBurner;
import com.siepert.createlegacy.blocks.item.ItemBlockVariants;
import com.siepert.createlegacy.mainRegistry.ModBlocks;
import com.siepert.createlegacy.mainRegistry.ModItems;
import com.siepert.createlegacy.tileentity.TileEntityBlazeBurner;
import com.siepert.createlegacy.util.IHasModel;
import com.siepert.createlegacy.util.IMetaName;
import com.siepert.createlegacy.util.Reference;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;

@SuppressWarnings("deprecation")
@MethodsReturnNonnullByDefault
public class BlockBlazeBurner extends Block implements IHasModel, IMetaName, ITileEntityProvider {
    @Override
    public String getSpecialName(ItemStack stack) {
        return State.fromMeta(stack.getItemDamage()).getName();
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Item.getItemFromBlock(ModBlocks.BLAZE_BURNER);
    }

    @Override
    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
        return new ItemStack(ModBlocks.BLAZE_BURNER, 1, state.getValue(STATE).getMeta() == 0 ? 0 : 1);
    }

    public enum State implements IStringSerializable {
        EMPTY(0, "empty"), PASSIVE(1, "passive"), HEATED(2, "heated"), COPE_SEETHE_MALD(3, "superheated");
        public final String name;
        public final int meta;
        State(int meta, String name) {
            this.meta = meta;
            this.name = name;
        }
        State(int meta) {
            this.meta = meta;
            this.name = this.toString();
        }
        @Override
        public String getName() {
            return name;
        }

        public int getMeta() {
            return meta;
        }

        public static State fromMeta(int meta) {
            for (State state : State.values()) {
                if (state.getMeta() == meta) {
                    return state;
                }
            }
            return State.EMPTY;
        }

        public static boolean compareStates(State input, State match) {
            return input.getMeta() >= match.getMeta();
        }

        public static State fromCookLevel(TileEntityBlazeBurner.CookLevel cookLevel) {
            switch (cookLevel) {
                case PASSIVE:
                    return State.PASSIVE;
                case HEATED:
                    return State.HEATED;
                case SEETHING:
                    return State.COPE_SEETHE_MALD;
            }
            return State.PASSIVE;
        }
    }
    public static final PropertyEnum<State> STATE = PropertyEnum.create("state", State.class);
    public static final PropertyInteger SCHEDULE = PropertyInteger.create("schedule", 0, 2);
    public BlockBlazeBurner(String name) {
        super(Material.IRON);
        this.translucent = true;
        this.blockSoundType = SoundType.METAL;
        this.fullBlock = false;
        setLightOpacity(0);

        setUnlocalizedName("create:" + name + "_");
        setRegistryName(name);
        setCreativeTab(CreateLegacy.TAB_CREATE);
        setHarvestLevel("pickaxe", 0);

        setDefaultState(this.blockState.getBaseState().withProperty(STATE, State.EMPTY));

        setHardness(1);
        setResistance(2);
        ModBlocks.BLOCKS.add(this);
        ModItems.ITEMS.add(new ItemBlockBlazeBurner(this).setRegistryName(this.getRegistryName()));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(STATE).getMeta() * 3 + state.getValue(SCHEDULE);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(STATE, State.fromMeta(meta / 3)).withProperty(SCHEDULE, meta % 3);
    }

    @Override
    public int damageDropped(IBlockState state) {
        if (state.getValue(STATE).getMeta() == 0) return 0;
        return 1;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] {STATE, SCHEDULE});
    }

    @Override
    public IBlockState getStateForPlacement(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        return this.getDefaultState().withProperty(STATE, State.fromMeta(placer.getHeldItem(hand).getItemDamage()));
    }

    @Override
    public void registerModels() {
        CreateLegacy.proxy.registerVariantRenderer(Item.getItemFromBlock(this), 0, "blaze_burner/empty", "inventory");
        CreateLegacy.proxy.registerVariantRenderer(Item.getItemFromBlock(this), 1, "blaze_burner/filled", "inventory");
    }

    @Override
    public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items) {
        items.add(new ItemStack(Item.getItemFromBlock(this), 1, 0));
        items.add(new ItemStack(Item.getItemFromBlock(this), 1, 1));
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
    public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
        switch (state.getValue(STATE)) {
            case EMPTY:
                return 0;
            case PASSIVE:
                return 3;
            case HEATED:
                return 9;
            case COPE_SEETHE_MALD:
                return 15;
        }
        return 0;
    }

    @Override
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }


    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (state.getValue(STATE) != State.EMPTY) {
            if (playerIn.getHeldItem(hand).getItem() == Items.COAL && state.getValue(STATE) == State.PASSIVE) {
                if (!worldIn.isRemote) {
                    worldIn.setBlockState(pos, state.withProperty(SCHEDULE, 1), 0);;
                    if (!playerIn.isCreative()) {
                        playerIn.getHeldItem(hand).shrink(1);
                    }
                }
                worldIn.playSound(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                        SoundEvents.ENTITY_BLAZE_AMBIENT, SoundCategory.BLOCKS, 1.0f, 1.0f);
                return true;
            }
            if (playerIn.getHeldItem(hand).getItem() == ModItems.SCRUMPTIOUS_FOOD && playerIn.getHeldItem(hand).getItemDamage() == 7) {
                if (state.getValue(STATE) == State.PASSIVE || state.getValue(STATE) == State.HEATED) {
                    if (!worldIn.isRemote) {
                        worldIn.setBlockState(pos, state.withProperty(SCHEDULE, 2), 0);
                        if (!playerIn.isCreative()) {
                            playerIn.getHeldItem(hand).shrink(1);
                        }
                    }
                    worldIn.playSound(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                            SoundEvents.ENTITY_BLAZE_AMBIENT, SoundCategory.BLOCKS, 1.0f, 1.0f);
                    return true;
                }
            }
        }
        return false;
    }


    @Override
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        if (stateIn.getValue(STATE).getMeta() > 1) {
            for (int i = 0; i < rand.nextInt(25) + 25; i++) {
                worldIn.spawnParticle(EnumParticleTypes.FLAME,
                        pos.getX() + rand.nextFloat(),
                        pos.getY() + rand.nextFloat(),
                        pos.getZ() + rand.nextFloat(),
                        0, rand.nextFloat() / 16, 0);
            }
            if (stateIn.getValue(STATE).getMeta() > 2) {
                for (int i = 0; i < rand.nextInt(25) + 25; i++) {
                    worldIn.spawnParticle(EnumParticleTypes.FLAME,
                            pos.getX() + rand.nextFloat(),
                            pos.getY() + rand.nextFloat(),
                            pos.getZ() + rand.nextFloat(),
                            0, rand.nextFloat() / 16, 0);
                }
            }
        }
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        if (state.getValue(STATE).equals(State.EMPTY)) return new ItemStack(this, 1, 0);
        return new ItemStack(this, 1, 1);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityBlazeBurner();
    }

    public static void setState(IBlockState state, World worldIn, BlockPos pos) {
        TileEntity tileEntity = worldIn.getTileEntity(pos);
        worldIn.setBlockState(pos, state, 3);
        if (tileEntity != null) {
            tileEntity.validate();
            worldIn.setTileEntity(pos, tileEntity);
        }
    }
}
