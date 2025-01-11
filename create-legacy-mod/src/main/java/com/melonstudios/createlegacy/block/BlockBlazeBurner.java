package com.melonstudios.createlegacy.block;

import com.melonstudios.createlegacy.CreateLegacy;
import com.melonstudios.createlegacy.item.ModItems;
import com.melonstudios.createlegacy.tileentity.TileEntityBlazeBurner;
import com.melonstudios.createlegacy.util.EnumBlazeLevel;
import com.melonstudios.createlegacy.util.IMetaName;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
@SuppressWarnings("deprecation")
public class BlockBlazeBurner extends Block implements ITileEntityProvider, IMetaName, IHeatProvider {
    public BlockBlazeBurner() {
        super(Material.IRON);

        setRegistryName("blaze_burner");
        setUnlocalizedName("create.blaze_burner");

        setHarvestLevel("pickaxe", 1);

        setHardness(5.0f);
        setResistance(10.0f);

        setTickRandomly(true);

        setSoundType(SoundType.METAL);
        setCreativeTab(CreateLegacy.TAB_KINETICS);
    }

    public static final PropertyBool HAS_BLAZE = PropertyBool.create("has_blaze");

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, HAS_BLAZE);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(HAS_BLAZE) ? 1 : 0;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(HAS_BLAZE, meta == 1);
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        return getDefaultState().withProperty(HAS_BLAZE, placer.getHeldItem(hand).getMetadata() == 1);
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        return new ItemStack(this, 1, state.getValue(HAS_BLAZE) ? 1 : 0);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack stack = playerIn.getHeldItem(hand);
        if (!worldIn.getBlockState(pos).getValue(HAS_BLAZE)) {
            if (stack.getItem() == Items.FLINT_AND_STEEL) {
                worldIn.setBlockState(pos, ModBlocks.BLAZE_BURNER_LIT.getDefaultState(), 3);
                if (!worldIn.isRemote) {
                    stack.damageItem(1, playerIn);
                    worldIn.playSound(null, pos,
                            SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS,
                            1, 1);
                }
                return true;
            } else if (stack.getItem() == Items.FIRE_CHARGE) {
                worldIn.setBlockState(pos, ModBlocks.BLAZE_BURNER_LIT.getDefaultState(), 3);
                if (!worldIn.isRemote) {
                    stack.shrink(1);
                    worldIn.playSound(null, pos,
                            SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS,
                            1, 1);
                }
                return true;
            }
        } else if (!stack.isEmpty()) {
            TileEntity te = worldIn.getTileEntity(pos);
            if (te instanceof TileEntityBlazeBurner) {
                TileEntityBlazeBurner burner = (TileEntityBlazeBurner) te;
                if (stack.getItem() == ModItems.FOOD && stack.getMetadata() == 3) {
                    burner.creativeCake();
                    if (!worldIn.isRemote) {
                        worldIn.playSound(null, pos,
                                SoundEvents.ENTITY_BLAZE_AMBIENT, SoundCategory.BLOCKS,
                                1, 0.9f + worldIn.rand.nextFloat() * 0.2f);
                    } else {
                        for (int i = 0; i < 20; i++) {
                            worldIn.spawnParticle(EnumParticleTypes.FLAME,
                                    pos.getX() + worldIn.rand.nextDouble(),
                                    pos.getY() + worldIn.rand.nextDouble(),
                                    pos.getZ() + worldIn.rand.nextDouble(),
                                    0, worldIn.rand.nextDouble() * 0.01, 0);
                        }
                    }
                    return true;
                } else {
                    for (int id : OreDictionary.getOreIDs(stack)) {
                        if (OreDictionary.getOreID("create:blazeBurnerSuperheat") == id) {
                            boolean flag = burner.superheat();
                            if (flag) {
                                if (!worldIn.isRemote) {
                                    stack.shrink(1);
                                    worldIn.playSound(null, pos,
                                            SoundEvents.ENTITY_BLAZE_AMBIENT, SoundCategory.BLOCKS,
                                            1, 0.9f + worldIn.rand.nextFloat() * 0.2f);
                                } else {
                                    for (int i = 0; i < 20; i++) {
                                        worldIn.spawnParticle(EnumParticleTypes.FLAME,
                                                pos.getX() + worldIn.rand.nextDouble(),
                                                pos.getY() + worldIn.rand.nextDouble(),
                                                pos.getZ() + worldIn.rand.nextDouble(),
                                                0, worldIn.rand.nextDouble() * 0.01, 0);
                                    }
                                }
                                return true;
                            }
                        }
                    }
                    int cookTime = TileEntityFurnace.getItemBurnTime(stack);
                    if (cookTime > 0) {
                        burner.addTicks(cookTime);
                        if (!worldIn.isRemote) {
                            stack.shrink(1);
                            worldIn.playSound(null, pos,
                                    SoundEvents.ENTITY_BLAZE_AMBIENT, SoundCategory.BLOCKS,
                                    1, 0.9f + worldIn.rand.nextFloat() * 0.2f);
                        } else {
                            for (int i = 0; i < 20; i++) {
                                worldIn.spawnParticle(EnumParticleTypes.FLAME,
                                        pos.getX() + worldIn.rand.nextDouble(),
                                        pos.getY() + worldIn.rand.nextDouble(),
                                        pos.getZ() + worldIn.rand.nextDouble(),
                                        0, worldIn.rand.nextDouble() * 0.01, 0);
                            }
                        }
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        if (!stateIn.getValue(HAS_BLAZE)) return;
        TileEntity te = worldIn.getTileEntity(pos);
        if (te instanceof TileEntityBlazeBurner) {
            TileEntityBlazeBurner burner = (TileEntityBlazeBurner) te;
            if (burner.getBlazeLevel().ordinal() > 1) {
                for (int i = 0; i < rand.nextInt(10); i++) {
                    worldIn.spawnParticle(EnumParticleTypes.FLAME,
                            pos.getX() + rand.nextDouble(),
                            pos.getY() + rand.nextDouble(),
                            pos.getZ() + rand.nextDouble(),
                            0, rand.nextDouble() * 0.01, 0);
                }
            }
        }
        for (int i = 0; i < 5; i++) {
            worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL,
                    pos.getX() + rand.nextDouble(),
                    pos.getY() + 0.2 + rand.nextDouble() * 0.4,
                    pos.getZ() + rand.nextDouble(),
                    0, rand.nextDouble() * 0.01, 0);
        }
    }

    @Override
    public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items) {
        items.add(new ItemStack(this, 1, 0));
        items.add(new ItemStack(this, 1, 1));
    }

    @Override
    public int damageDropped(IBlockState state) {
        return state.getValue(HAS_BLAZE) ? 1 : 0;
    }

    public static void items() {
        CreateLegacy.setItemModel(ModBlocks.BLAZE_BURNER, 0, "blaze_burner/empty");
        CreateLegacy.setItemModel(ModBlocks.BLAZE_BURNER, 1, "blaze_burner/filled");
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return stack.getMetadata() == 0 ? "tile.create.blaze_burner_empty" : "tile.create.blaze_burner";
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return meta == 1 ? new TileEntityBlazeBurner() : null;
    }

    @Override
    public int getHeatLevel(World world, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);
        if (state.getValue(HAS_BLAZE)) {
            TileEntity te = world.getTileEntity(pos);
            if (te instanceof TileEntityBlazeBurner) {
                return ((TileEntityBlazeBurner)te).getBlazeLevel().ordinal();
            }
        }
        return -1;
    }

    @Override
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public int getLightValue(IBlockState state) {
        return state.getValue(HAS_BLAZE) ? 15 : 0;
    }

    @Override
    public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof TileEntityBlazeBurner) {
            if (((TileEntityBlazeBurner)te).getBlazeLevel().ordinal() > 1) return 15;
            if (((TileEntityBlazeBurner)te).getBlazeLevel() == EnumBlazeLevel.PASSIVE) return 7;
        }
        return 0;
    }

    @Override
    public float getAmbientOcclusionLightValue(IBlockState state) {
        return 1;
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
