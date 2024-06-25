package com.siepert.createlegacy.blocks.kinetic;

import com.siepert.createlegacy.CreateLegacy;
import com.siepert.createlegacy.mainRegistry.ModBlocks;
import com.siepert.createlegacy.mainRegistry.ModItems;
import com.siepert.createlegacy.util.IHasModel;
import com.siepert.createapi.IKineticActor;
import com.siepert.createapi.IWrenchable;
import com.siepert.createlegacy.util.Reference;
import com.siepert.createlegacy.util.handlers.ModSoundHandler;
import com.siepert.createlegacy.util.handlers.recipes.CompactingRecipes;
import com.siepert.createlegacy.util.handlers.recipes.PressingRecipes;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.List;

@SuppressWarnings("deprecation")
@MethodsReturnNonnullByDefault
public class BlockMechanicalPress extends Block implements IHasModel, IKineticActor, IWrenchable {
    private static final AxisAlignedBB BB = new AxisAlignedBB(0.0, 2.0 / 16.0, 0.0, 1.0, 1.0, 1.0);
    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return BB;
    }

    public static final PropertyEnum<EnumFacing.Axis> AXIS = PropertyEnum.create("axis", EnumFacing.Axis.class);
    public BlockMechanicalPress(String name) {
        super(Material.ROCK);
        this.translucent = true;
        this.blockSoundType = SoundType.WOOD;
        this.fullBlock = false;
        setLightOpacity(0);

        setUnlocalizedName("create:" + name);
        setRegistryName(name);
        setCreativeTab(CreateLegacy.TAB_CREATE);
        setHarvestLevel("axe", 0);

        setDefaultState(this.blockState.getBaseState().withProperty(AXIS, EnumFacing.Axis.X));

        setHardness(1);
        setResistance(2);
        ModBlocks.BLOCKS.add(this);
        ModItems.ITEMS.add(new ItemBlock(this).setRegistryName(this.getRegistryName()));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        switch (state.getValue(AXIS)) {
            case X:
                return 0;
            case Z:
                return 1;
        }
        return 0;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        switch (meta) {
            case 0:
                return this.getDefaultState().withProperty(AXIS, EnumFacing.Axis.X);
            case 1:
                return this.getDefaultState().withProperty(AXIS, EnumFacing.Axis.Z);
            default:
                return this.getDefaultState();
        }
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] {AXIS});
    }

    @Override
    public IBlockState getStateForPlacement(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        return this.getDefaultState().withProperty(AXIS, placer.getHorizontalFacing().getAxis());
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


        if (source.getAxis() == myState.getValue(AXIS)) {
            iteratedBlocks.add(pos);

            Block aBlock = worldIn.getBlockState(pos.offset(source.getOpposite())).getBlock();

            if (aBlock instanceof IKineticActor) {
                ((IKineticActor) aBlock).passRotation(worldIn, pos.offset(source.getOpposite()), source, iteratedBlocks,
                        false, false, inverseRotation);
            }

            boolean isCompactor = worldIn.getBlockState(pos.down(2)).getBlock() instanceof BlockItemHolder;
            if (isCompactor) {
                isCompactor = worldIn.getBlockState(pos.down(2)).getValue(BlockItemHolder.VARIANT) == BlockItemHolder.Variant.BASIN;
            }

            BlockBlazeBurner.State heatState;
            if (isCompactor && worldIn.getBlockState(pos.down(3)).getBlock() instanceof BlockBlazeBurner) {
                heatState = worldIn.getBlockState(pos.down(3)).getValue(BlockBlazeBurner.STATE);
            } else heatState = BlockBlazeBurner.State.EMPTY;


            if (!worldIn.getBlockState(pos.down()).getMaterial().blocksMovement() && !worldIn.isRemote) {
                if (!isCompactor) {
                    AxisAlignedBB itemSearchArea = new AxisAlignedBB(pos.down());
                    List<EntityItem> foundItems = worldIn.getEntitiesWithinAABB(EntityItem.class, itemSearchArea);

                    for (EntityItem entityItem : foundItems) {
                        if (apply(entityItem.getItem()).hasRecipe) {
                            EntityItem resultEntityItem = new EntityItem(worldIn, pos.getX() + 0.5, pos.down().getY(), pos.getZ() + 0.5,
                                    apply(entityItem.getItem()).stack);
                            entityItem.getItem().shrink(1);
                            resultEntityItem.setVelocity(0, 0, 0);
                            resultEntityItem.setNoDespawn();
                            worldIn.spawnEntity(resultEntityItem);
                            if (entityItem.getItem().getCount() == 0 || entityItem.getItem().isEmpty()) {
                                entityItem.setDead();
                            }

                            float pitch;
                            if (Reference.random.nextInt(100) == 0) {
                                pitch = 0.1f;
                            } else pitch = 0.8f;
                            worldIn.playSound(null, pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D,
                                    ModSoundHandler.BLOCK_PRESS_ACTIVATION, SoundCategory.BLOCKS, 1.0f, pitch);
                            return;
                        }
                    }
                    return;
                }
                AxisAlignedBB itemSearchArea = new AxisAlignedBB(pos.down(2));
                List<EntityItem> foundItems = worldIn.getEntitiesWithinAABB(EntityItem.class, itemSearchArea);

                for (EntityItem entityItem : foundItems) {
                    if (applyCompact(entityItem.getItem(), heatState).hasRecipe) {
                        EntityItem resultEntityItem = new EntityItem(worldIn, pos.getX() + 0.5, pos.down(2).getY() + 0.2, pos.getZ() + 0.5,
                                applyCompact(entityItem.getItem(), heatState).getResult());
                        entityItem.getItem().shrink(applyCompact(entityItem.getItem(), heatState).cost);
                        resultEntityItem.setVelocity(0, 0, 0);
                        resultEntityItem.setNoDespawn();
                        worldIn.spawnEntity(resultEntityItem);
                        if (entityItem.getItem().getCount() == 0 || entityItem.getItem().isEmpty()) {
                            entityItem.setDead();
                        }

                        float pitch;
                        if (Reference.random.nextInt(100) == 0) {
                            pitch = 0.1f;
                        } else pitch = 0.8f;
                        worldIn.playSound(null, pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, ModSoundHandler.BLOCK_PRESS_ACTIVATION, SoundCategory.BLOCKS, 1.0f, pitch);
                        return;
                    }
                }
            }
        }
    }

    public PressingResultSet apply(ItemStack stack) {
        if (stack.isEmpty())
        {
            return new PressingResultSet(stack, false);
        }
        else
        {
            ItemStack itemstack = PressingRecipes.instance().getPressingResult(stack);

            if (itemstack.isEmpty())
            {
                CreateLegacy.logger.warn("Couldn't press {} because there is no pressing recipe", (Object)stack);
                return new PressingResultSet(stack, false);
            }
            else
            {
                ItemStack itemstack1 = itemstack.copy();
                itemstack1.setCount(itemstack.getCount());
                return new PressingResultSet(itemstack1, true);
            }
        }
    }

    public CompactingResultSet applyCompact(ItemStack stack, BlockBlazeBurner.State heatState) {
        if (stack.isEmpty()) {
            return new CompactingResultSet(stack, 0, false);
        } else {
            ItemStack itemstack = CompactingRecipes.instance().getCompactingResult(stack);
            BlockBlazeBurner.State heatMin = CompactingRecipes.instance().getHeatRequirement(stack);
            int cost = CompactingRecipes.instance().getCompactingCost(stack);

            if (itemstack.isEmpty()) {
                return new CompactingResultSet(stack, 0, false);
            } else {
                if (BlockBlazeBurner.State.compareStates(heatState, heatMin) && stack.getCount() >= cost) {
                    ItemStack itemstack1 = itemstack.copy();
                    itemstack1.setCount(itemstack.getCount());
                    return new CompactingResultSet(itemstack1, cost, true);
                }
            }
        }
        return new CompactingResultSet(stack, 0, false);
    }

    public static class PressingResultSet {
        ItemStack stack;
        boolean hasRecipe;
        private PressingResultSet(ItemStack stack, boolean hasRecipe) {
            this.stack = stack;
            this.hasRecipe = hasRecipe;
        }

        public boolean hasRecipe() {
            return hasRecipe;
        }

        public ItemStack getResult() {
            return stack;
        }
    }

    private static class CompactingResultSet {
        int cost;
        ItemStack result;
        boolean hasRecipe;
        private CompactingResultSet(ItemStack result, int cost, boolean hasRecipe) {
            this.result = result;
            this.hasRecipe = hasRecipe;
            this.cost = cost;
        }

        public boolean hasRecipe() {
            return hasRecipe;
        }
        public ItemStack getResult() {
            return result;
        }
    }

    @Override
    public boolean rotateBlock(World world, BlockPos pos, EnumFacing axis) {
        IBlockState state = world.getBlockState(pos);
        TileEntity tileEntity = world.getTileEntity(pos);

        if (state.getValue(AXIS) == EnumFacing.Axis.X) {
            world.setBlockState(pos, state.withProperty(AXIS, EnumFacing.Axis.Z), 3);
        } else {
            world.setBlockState(pos, state.withProperty(AXIS, EnumFacing.Axis.X), 3);
        }

        if (tileEntity != null) {
            tileEntity.validate();
            world.setTileEntity(pos, tileEntity);
        }

        return true;
    }
    @Override
    public boolean onWrenched(World worldIn, BlockPos pos, IBlockState state, EnumFacing side, EntityPlayer playerIn) {
        rotateBlock(worldIn, pos, side);

        return true;
    }
}
