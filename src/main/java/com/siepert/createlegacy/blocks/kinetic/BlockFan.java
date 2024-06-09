package com.siepert.createlegacy.blocks.kinetic;

import com.siepert.createlegacy.CreateLegacy;
import com.siepert.createlegacy.mainRegistry.ModBlocks;
import com.siepert.createlegacy.mainRegistry.ModItems;
import com.siepert.createlegacy.util.IHasModel;
import com.siepert.createlegacy.util.IKineticActor;
import com.siepert.createlegacy.util.Reference;
import com.siepert.createlegacy.util.handlers.ModSoundHandler;
import com.siepert.createlegacy.util.handlers.recipes.PressingRecipes;
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
import net.minecraft.init.Blocks;
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
import java.nio.channels.IllegalSelectorException;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("deprecation")
@MethodsReturnNonnullByDefault
public class BlockFan extends Block implements IHasModel, IKineticActor {
    public static final PropertyEnum<EnumFacing> FACING = PropertyEnum.create("facing", EnumFacing.class);
    public BlockFan(String name) {
        super(Material.ROCK);
        this.translucent = true;
        this.blockSoundType = SoundType.WOOD;
        this.fullBlock = false;
        setLightOpacity(0);

        setUnlocalizedName(name);
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



    private static final List<Block> PROCESSORS = new ArrayList<>();

    static {
        PROCESSORS.add(Blocks.WATER);
        PROCESSORS.add(Blocks.FLOWING_WATER);
        PROCESSORS.add(Blocks.LAVA);
        PROCESSORS.add(Blocks.FLOWING_LAVA);
        PROCESSORS.add(Blocks.FIRE);
    }

    private enum ProcessingType {
        WASH, SMELT;

        public static @Nullable ProcessingType getType(Block blockIn) {
            if (blockIn.equals(Blocks.WATER) || blockIn.equals(Blocks.FLOWING_WATER)) {
                return WASH;
            }
            if (blockIn.equals(Blocks.LAVA) || blockIn.equals(Blocks.FLOWING_LAVA)|| blockIn.equals(Blocks.FIRE)) {
                return SMELT;
            }
            return null;
        }
    }

    @Override
    public void passRotation(World worldIn, BlockPos pos, EnumFacing source, List<BlockPos> iteratedBlocks, boolean srcIsCog, boolean srcCogIsHorizontal) {
        if (srcIsCog) return; //We don't accept a cog as input, we need a shaft!

        IBlockState myState = worldIn.getBlockState(pos);


        if (source == myState.getValue(FACING).getOpposite()) {
            iteratedBlocks.add(pos);

            boolean isABlower = !PROCESSORS.contains(worldIn.getBlockState(pos.offset(source.getOpposite())).getBlock());

            BlockPos posFront = pos.offset(source.getOpposite());

            double particleVX = source.getOpposite().getFrontOffsetX() * 2;
            double particleVY = source.getOpposite().getFrontOffsetY() * 2;
            double particleVZ = source.getOpposite().getFrontOffsetZ() * 2;

            if (isABlower) {
                if (!worldIn.getBlockState(posFront).getMaterial().blocksMovement()) {
                    worldIn.spawnParticle(EnumParticleTypes.CLOUD,
                            posFront.getX() + 0.5,
                            posFront.getY() + 0.5,
                            posFront.getZ() + 0.5,
                            particleVX,
                            particleVY,
                            particleVZ);
                    for (int i = 0; i < Reference.random.nextInt(4) + 4; i++) {
                        worldIn.spawnParticle(EnumParticleTypes.CLOUD,
                                posFront.getX() + Reference.random.nextFloat(),
                                posFront.getY() + Reference.random.nextFloat(),
                                posFront.getZ() + Reference.random.nextFloat(),
                                particleVX,
                                particleVY,
                                particleVZ);
                    }


                    AxisAlignedBB searchField = new AxisAlignedBB(pos.offset(source.getOpposite()));

                    List<Entity> foundEntities = worldIn.getEntitiesWithinAABB(Entity.class, searchField);

                    for (Entity entity : foundEntities) {
                        double baseVX = entity.motionX;
                        double baseVY = entity.motionY;
                        double baseVZ = entity.motionZ;

                        switch (source.getOpposite()) {
                            case UP:
                                baseVY += 1.5;
                                entity.fallDistance = 0.0f;
                                break;
                            case DOWN:
                                baseVY -= 1.5;
                                break;
                            case NORTH:
                                baseVZ -= 1.5;
                                break;
                            case EAST:
                                baseVX += 1.5;
                                break;
                            case SOUTH:
                                baseVZ += 1.5;
                                break;
                            case WEST:
                                baseVX -= 1.5;
                                break;
                        }

                        entity.setVelocity(baseVX, baseVY, baseVZ);
                    }
                } else return;

                if (!worldIn.getBlockState(pos.offset(source.getOpposite(), 2)).getMaterial().blocksMovement()) {
                    AxisAlignedBB searchField = new AxisAlignedBB(pos.offset(source.getOpposite(), 2));

                    List<Entity> foundEntities = worldIn.getEntitiesWithinAABB(Entity.class, searchField);

                    for (Entity entity : foundEntities) {
                        double baseVX = entity.motionX;
                        double baseVY = entity.motionY;
                        double baseVZ = entity.motionZ;

                        switch (source.getOpposite()) {
                            case UP:
                                baseVY += 1;
                                entity.fallDistance = 0.0f;
                                break;
                            case DOWN:
                                baseVY -= 1;
                                break;
                            case NORTH:
                                baseVZ -= 1;
                                break;
                            case EAST:
                                baseVX += 1;
                                break;
                            case SOUTH:
                                baseVZ += 1;
                                break;
                            case WEST:
                                baseVX -= 1;
                                break;
                        }

                        entity.setVelocity(baseVX, baseVY, baseVZ);
                    }
                } else return;

                if (!worldIn.getBlockState(pos.offset(source.getOpposite(), 3)).getMaterial().blocksMovement()) {
                    AxisAlignedBB searchField = new AxisAlignedBB(pos.offset(source.getOpposite(), 3));

                    List<Entity> foundEntities = worldIn.getEntitiesWithinAABB(Entity.class, searchField);

                    for (Entity entity : foundEntities) {
                        double baseVX = entity.motionX;
                        double baseVY = entity.motionY;
                        double baseVZ = entity.motionZ;

                        switch (source.getOpposite()) {
                            case UP:
                                baseVY += 0.5;
                                entity.fallDistance = 0.0f;
                                break;
                            case DOWN:
                                baseVY -= 0.5;
                                break;
                            case NORTH:
                                baseVZ -= 0.5;
                                break;
                            case EAST:
                                baseVX += 0.5;
                                break;
                            case SOUTH:
                                baseVZ += 0.5;
                                break;
                            case WEST:
                                baseVX -= 0.5;
                                break;
                        }

                        entity.setVelocity(baseVX, baseVY, baseVZ);
                    }
                } else return;
            }

            ProcessingType whatsTheProcess = ProcessingType.getType(worldIn.getBlockState(posFront).getBlock());

            if (whatsTheProcess != null) {
                BlockPos startProcessPos = pos.offset(source.getOpposite(), 2);
                int processingLength = 0;

                Block blockNow = worldIn.getBlockState(startProcessPos).getBlock();

                while (processingLength < 6 && !blockNow.getMaterial(worldIn.getBlockState(startProcessPos.offset(source.getOpposite(), processingLength))).blocksMovement()) {
                    processingLength++;
                    blockNow = worldIn.getBlockState(startProcessPos.offset(source.getOpposite(), processingLength)).getBlock();
                }

                BlockPos endPos = startProcessPos.offset(source.getOpposite(), processingLength);

                AxisAlignedBB itemSearchArea = new AxisAlignedBB(startProcessPos, endPos);
                List<EntityItem> foundItems = worldIn.getEntitiesWithinAABB(EntityItem.class, itemSearchArea);

                for (EntityItem entityItem : foundItems) {
                    if (apply(entityItem.getItem(), whatsTheProcess).hasRecipe) {
                        EntityItem resultEntityItem = new EntityItem(worldIn, entityItem.posX, entityItem.posY, entityItem.posZ,
                                apply(entityItem.getItem(), whatsTheProcess).stack);
                        resultEntityItem.setVelocity(0, 0, 0);
                        worldIn.spawnEntity(resultEntityItem);
                        entityItem.getItem().shrink(1);
                        if (entityItem.getItem().getCount() == 0 || entityItem.getItem().isEmpty()) {
                            entityItem.setDead();
                        }
                    }
                }
            }
        }
    }

    public @Nullable ResultSet apply(ItemStack stack, ProcessingType type) {
        if (stack.isEmpty()) {
            return new ResultSet(stack, false);
        }
        else {
            if (type == ProcessingType.WASH) {
                ItemStack itemstack = WashingRecipes.instance().getWashingResult(stack);

                if (itemstack.isEmpty()) {
                    CreateLegacy.logger.warn("Couldn't wash {} because there is no washing recipe", (Object) stack);
                    return new ResultSet(stack, false);
                } else {
                    ItemStack itemstack1 = itemstack.copy();
                    itemstack1.setCount(itemstack.getCount());
                    return new ResultSet(itemstack1, true);
                }
            }
            if (type == ProcessingType.SMELT) {
                ItemStack itemstack = FurnaceRecipes.instance().getSmeltingResult(stack);

                if (itemstack.isEmpty()) {
                    CreateLegacy.logger.warn("Couldn't wash {} because there is no washing recipe", (Object) stack);
                    return new ResultSet(stack, false);
                } else {
                    ItemStack itemstack1 = itemstack.copy();
                    itemstack1.setCount(itemstack.getCount());
                    return new ResultSet(itemstack1, true);
                }
            }
            return null;
        }
    }

    private class ResultSet {
        ItemStack stack;
        boolean hasRecipe;
        private ResultSet(ItemStack stack, boolean hasRecipe) {
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
}
