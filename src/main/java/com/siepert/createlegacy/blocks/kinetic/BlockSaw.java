package com.siepert.createlegacy.blocks.kinetic;

import com.siepert.createlegacy.CreateLegacy;
import com.siepert.createlegacy.mainRegistry.ModBlocks;
import com.siepert.createlegacy.mainRegistry.ModItems;
import com.siepert.createlegacy.util.IHasModel;
import com.siepert.createapi.IKineticActor;
import com.siepert.createapi.IWrenchable;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("deprecation")
@MethodsReturnNonnullByDefault
public class BlockSaw extends Block implements IHasModel, IKineticActor, IWrenchable {
    public static final PropertyEnum<EnumFacing> FACING = PropertyEnum.create("facing", EnumFacing.class);
    public BlockSaw(String name) {
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
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
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
    public IBlockState getStateForPlacement(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        if (placer.isSneaking()) {
            return super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer, hand).withProperty(FACING, facing.getOpposite());
        }
        return super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer, hand).withProperty(FACING,
                EnumFacing.getFacingFromVector((float) placer.getLookVec().x, (float) placer.getLookVec().y, (float) placer.getLookVec().z).getOpposite());
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
    public void passRotation(World worldIn, BlockPos pos, EnumFacing source, List<BlockPos> iteratedBlocks,
                             boolean srcIsCog, boolean srcCogIsHorizontal, boolean inverseRotation) {


        if (srcIsCog) return; //We don't accept a cog as input, we need a shaft!

        IBlockState myState = worldIn.getBlockState(pos);
        Logger logger = CreateLegacy.logger;


        if (source == myState.getValue(FACING).getOpposite()) {
            iteratedBlocks.add(pos);
            BlockPos newPos = new BlockPos(pos.offset(source.getOpposite()));
            List<BlockPos> treeMap = new ArrayList<>();

            if (isBlockALog(worldIn.getBlockState(newPos).getBlock())) {
                treeMap.add(pos.offset(source.getOpposite()));
                extendTreeMap(worldIn, pos.offset(source.getOpposite()), treeMap, source.getOpposite());
            }

            for (BlockPos thePos : treeMap) {
                worldIn.getBlockState(thePos).getBlock().dropBlockAsItem(worldIn, thePos, worldIn.getBlockState(thePos), 0);
                worldIn.playSound(null, thePos, worldIn.getBlockState(thePos).getBlock().getSoundType().getBreakSound(),
                        SoundCategory.BLOCKS, 1.0f, 1.0f);
                worldIn.setBlockState(thePos, Blocks.AIR.getDefaultState(), 0);
            }

            AxisAlignedBB axisAlignedBB = new AxisAlignedBB(newPos);

            List<EntityLivingBase> entities = worldIn.getEntitiesWithinAABB(EntityLivingBase.class, axisAlignedBB);

            for (EntityLivingBase entity : entities) {
                if (entity instanceof EntityPlayer) {
                    if (!((EntityPlayer) entity).isCreative()) {
                        entity.setHealth(entity.getHealth() - 4);
                        entity.performHurtAnimation();
                        worldIn.playSound(null,
                                entity.posX, entity.posY, entity.posZ,
                                SoundEvents.ENTITY_PLAYER_HURT,
                                entity.getSoundCategory(), 1.0f, 1.0f);
                    }
                } else {
                    entity.setHealth(entity.getHealth() - 4);
                    entity.performHurtAnimation();
                    worldIn.playSound(null,
                            entity.posX, entity.posY, entity.posZ,
                            SoundEvents.ENTITY_GENERIC_HURT,
                            entity.getSoundCategory(), 1.0f, 1.0f);
                }
            }
        }
    }

    private boolean isBlockALog(Block theBlock) {
        if (theBlock == Blocks.LOG  || theBlock == Blocks.LOG2) return true;
        if (OreDictionary.getOres("logWood").contains(new ItemStack(theBlock))) return true;
        return OreDictionary.getOres("log").contains(new ItemStack(theBlock));
    }

    private boolean isBlockALeaf(Block theBlock) {
        if (theBlock == Blocks.LEAVES  || theBlock == Blocks.LEAVES2) return true;
        return OreDictionary.getOres("treeLeaves").contains(new ItemStack(theBlock));
    }

    private void extendTreeMap(World worldIn, BlockPos pos, List<BlockPos> treeMap, EnumFacing source) {
        for (EnumFacing facing : EnumFacing.values()) {
            if (facing != source && !treeMap.contains(pos.offset(facing))) {
                Block blockNow = worldIn.getBlockState(pos.offset(facing)).getBlock();
                if (isBlockALog(blockNow)) {
                    treeMap.add(pos.offset(facing));
                    extendTreeMap(worldIn, pos.offset(facing), treeMap, facing.getOpposite());
                }
            }
        }
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
}
