package com.siepert.createlegacy.blocks.kinetic;

import com.siepert.createlegacy.CreateLegacy;
import com.siepert.createlegacy.mainRegistry.ModBlocks;
import com.siepert.createlegacy.mainRegistry.ModItems;
import com.siepert.createlegacy.util.IHasModel;
import com.siepert.createapi.IKineticActor;
import com.siepert.createapi.IWrenchable;
import com.siepert.createlegacy.util.Reference;
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
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
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
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("deprecation")
@MethodsReturnNonnullByDefault
public class BlockFan extends Block implements IHasModel, IKineticActor, IWrenchable {
    public static final PropertyEnum<EnumFacing> FACING = PropertyEnum.create("facing", EnumFacing.class);
    public BlockFan(String name) {
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

        public static @Nullable ProcessingType getType(IBlockState blockstateIn) {
            if (blockstateIn.getBlock().equals(Blocks.WATER) || blockstateIn.getBlock().equals(Blocks.FLOWING_WATER)) {
                return WASH;
            }
            if (blockstateIn.getBlock().equals(Blocks.LAVA) || blockstateIn.getBlock().equals(Blocks.FLOWING_LAVA)
                    || blockstateIn.getBlock().equals(Blocks.FIRE)) {
                return SMELT;
            }
            if (blockstateIn.getBlock() instanceof BlockBlazeBurner) {
                if (blockstateIn.getValue(BlockBlazeBurner.STATE).getMeta() > 1) return SMELT;
            }
            return null;
        }
    }

    @Override
    public void passRotation(World worldIn, BlockPos pos, EnumFacing source, List<BlockPos> iteratedBlocks,
                             boolean srcIsCog, boolean srcCogIsHorizontal, boolean inverseRotation) {
        if (srcIsCog) return; //We don't accept a cog as input, we need a shaft!

        IBlockState myState = worldIn.getBlockState(pos);


        if (source == myState.getValue(FACING).getOpposite()) {
            iteratedBlocks.add(pos);

            boolean isABlower = (!PROCESSORS.contains(worldIn.getBlockState(pos.offset(source.getOpposite())).getBlock()) && !(worldIn.getBlockState(pos.offset(source.getOpposite())).getBlock() instanceof BlockBlazeBurner));

            BlockPos posFront = pos.offset(source.getOpposite());

            double particleVX = source.getOpposite().getFrontOffsetX() * 2;
            double particleVY = source.getOpposite().getFrontOffsetY() * 2;
            double particleVZ = source.getOpposite().getFrontOffsetZ() * 2;

            if (isABlower) {
                if (!worldIn.getBlockState(posFront).getMaterial().blocksMovement()
                        || isBlockNotObscure(worldIn.getBlockState(pos.offset(source.getOpposite())))) {
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

                if (!worldIn.getBlockState(pos.offset(source.getOpposite(), 2)).getMaterial().blocksMovement()
                        || isBlockNotObscure(worldIn.getBlockState(pos.offset(source.getOpposite(), 2)))) {
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

                if (!worldIn.getBlockState(pos.offset(source.getOpposite(), 3)).getMaterial().blocksMovement()
                        || isBlockNotObscure(worldIn.getBlockState(pos.offset(source.getOpposite(), 3)))) {
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

            ProcessingType whatsTheProcess = ProcessingType.getType(worldIn.getBlockState(posFront));

            if (whatsTheProcess != null) {
                for (int i = 0; i < 5; i++) {
                    BlockPos startProcessPos = pos.offset(source.getOpposite(), 1 + i);

                    if (isBlockNotObscure(worldIn.getBlockState(startProcessPos))
                            || !worldIn.getBlockState(startProcessPos).getMaterial().blocksMovement()) {

                        if (whatsTheProcess == ProcessingType.WASH) {

                            worldIn.spawnParticle(EnumParticleTypes.CLOUD,
                                    posFront.getX() + 0.5,
                                    posFront.getY() + 0.5,
                                    posFront.getZ() + 0.5,
                                    particleVX / 3,
                                    particleVY / 3,
                                    particleVZ / 3);
                            for (int j = 0; j < Reference.random.nextInt(4) + 4; j++) {
                                worldIn.spawnParticle(EnumParticleTypes.CLOUD,
                                        posFront.getX() + Reference.random.nextFloat(),
                                        posFront.getY() + Reference.random.nextFloat(),
                                        posFront.getZ() + Reference.random.nextFloat(),
                                        particleVX / 3,
                                        particleVY / 3,
                                        particleVZ / 3);
                            }
                            for (int j = 0; j < Reference.random.nextInt(4); j++) {
                                worldIn.spawnParticle(EnumParticleTypes.WATER_BUBBLE,
                                        posFront.getX() + Reference.random.nextFloat(),
                                        posFront.getY() + Reference.random.nextFloat(),
                                        posFront.getZ() + Reference.random.nextFloat(),
                                        particleVX / 3,
                                        particleVY / 3,
                                        particleVZ / 3);
                            }
                        }
                        if (whatsTheProcess == ProcessingType.SMELT) {
                            worldIn.spawnParticle(EnumParticleTypes.SMOKE_LARGE,
                                    posFront.getX() + 0.5,
                                    posFront.getY() + 0.5,
                                    posFront.getZ() + 0.5,
                                    particleVX / 3,
                                    particleVY / 3,
                                    particleVZ / 3);
                            for (int j = 0; j < Reference.random.nextInt(4) + 4; j++) {
                                worldIn.spawnParticle(EnumParticleTypes.SMOKE_LARGE,
                                        posFront.getX() + Reference.random.nextFloat(),
                                        posFront.getY() + Reference.random.nextFloat(),
                                        posFront.getZ() + Reference.random.nextFloat(),
                                        particleVX / 3,
                                        particleVY / 3,
                                        particleVZ / 3);
                            }

                        }

                        AxisAlignedBB itemSearchArea = new AxisAlignedBB(startProcessPos.offset(source.getOpposite()));
                        List<EntityItem> foundItems = worldIn.getEntitiesWithinAABB(EntityItem.class, itemSearchArea);

                        List<Entity> foundEntities = worldIn.getEntitiesWithinAABB(Entity.class, itemSearchArea);

                        if (whatsTheProcess == ProcessingType.SMELT) {
                            for (Entity entity : foundEntities) {
                                if (!(entity instanceof EntityItem)) {
                                    entity.setFire(5);
                                }
                            }
                        }

                        for (EntityItem entityItem : foundItems) {
                            if (worldIn.isRemote) {
                                if (whatsTheProcess == ProcessingType.WASH) {
                                    for (int k = 0; k < Reference.random.nextInt(10); k++) {
                                        worldIn.spawnParticle(EnumParticleTypes.WATER_SPLASH,
                                                entityItem.posX, entityItem.posY + 0.2, entityItem.posZ,
                                                0, Reference.random.nextFloat(), 0);
                                    }
                                }
                                if (whatsTheProcess == ProcessingType.SMELT) {
                                    worldIn.spawnParticle(EnumParticleTypes.FLAME,
                                            entityItem.posX, entityItem.posY + 0.2, entityItem.posZ,
                                            0, 0, 0);

                                    worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL,
                                            entityItem.posX, entityItem.posY + 0.2, entityItem.posZ,
                                            0, Reference.random.nextFloat() / 2, 0);
                                    worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL,
                                            entityItem.posX, entityItem.posY + 0.2, entityItem.posZ,
                                            0, Reference.random.nextFloat() / 2, 0);
                                    worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL,
                                            entityItem.posX, entityItem.posY + 0.2, entityItem.posZ,
                                            0, Reference.random.nextFloat() / 2, 0);
                                }


                            }
                            if (!worldIn.isRemote) {
                                if (whatsTheProcess == ProcessingType.SMELT) {
                                    SmeltResultSet resultSet = applicateSmelt(entityItem.getItem(), whatsTheProcess);
                                    if (resultSet.hasRecipe) {
                                        entityItem.getItem().shrink(1);
                                        EntityItem resultEntityItem = new EntityItem(worldIn, entityItem.posX, entityItem.posY, entityItem.posZ,
                                                resultSet.stack);
                                        resultEntityItem.setVelocity(0, 0, 0);
                                        worldIn.spawnEntity(resultEntityItem);

                                        worldIn.playSound(null, entityItem.posX, entityItem.posY, entityItem.posZ,
                                                SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.AMBIENT, 0.25f, 1.0f);

                                        if (entityItem.getItem().getCount() == 0 || entityItem.getItem().isEmpty()) {
                                            entityItem.setDead();
                                        }
                                    }
                                }
                                if (whatsTheProcess == ProcessingType.WASH) {
                                    WashResultSet resultSet = applicateWash(entityItem.getItem(), whatsTheProcess);
                                    assert resultSet != null;
                                    if (resultSet.hasRecipe) {
                                        entityItem.getItem().shrink(1);
                                        EntityItem resultEntityItem = new EntityItem(worldIn, entityItem.posX, entityItem.posY, entityItem.posZ,
                                                resultSet.stack);
                                        resultEntityItem.setVelocity(0, 0, 0);
                                        worldIn.spawnEntity(resultEntityItem);

                                        if (resultSet.hasOptional()) {
                                            EntityItem resultEntityItemOptional = new EntityItem(worldIn, entityItem.posX, entityItem.posY, entityItem.posZ,
                                                    resultSet.stackOptional);
                                            resultEntityItemOptional.setVelocity(0, 0, 0);
                                            worldIn.spawnEntity(resultEntityItemOptional);
                                        }

                                        worldIn.playSound(null, entityItem.posX, entityItem.posY, entityItem.posZ,
                                                SoundEvents.ENTITY_GENERIC_SPLASH, SoundCategory.AMBIENT, 0.25f, 1.0f);

                                        if (entityItem.getItem().getCount() == 0 || entityItem.getItem().isEmpty()) {
                                            entityItem.setDead();
                                        }
                                    }
                                }
                            }
                        }
                    } else return;
                }
            }
        }
    }

    private boolean isBlockNotObscure(IBlockState stateOfOhio) {
        List<Block> predef_allowances = new ArrayList<Block>();
        predef_allowances.add(Blocks.STANDING_SIGN);
        predef_allowances.add(Blocks.WALL_SIGN);
        predef_allowances.add(Blocks.LADDER);
        predef_allowances.add(Blocks.IRON_BARS);
        predef_allowances.add(ModBlocks.BLAZE_BURNER);

        if (predef_allowances.contains(stateOfOhio.getBlock()))return true;
        return false;
    }

    public @Nullable SmeltResultSet applicateSmelt(ItemStack stack, ProcessingType type) {
        if (stack.isEmpty()) {
            return new SmeltResultSet(stack, false);
        }
        else {
            ItemStack itemstack = FurnaceRecipes.instance().getSmeltingResult(stack);

            if (itemstack.isEmpty()) {
                return new SmeltResultSet(stack, false);
            } else {
                ItemStack itemstack1 = itemstack.copy();
                itemstack1.setCount(itemstack.getCount());
                return new SmeltResultSet(itemstack1, true);
            }
        }
    }
    public @Nullable WashResultSet applicateWash(ItemStack stack, ProcessingType type) {
        if (stack.isEmpty()) {
            return new WashResultSet(stack, false);
        }
        else {
            ItemStack itemstack = WashingRecipes.instance().getWashingResult(stack);
            ItemStack stackOpt = WashingRecipes.instance().getOptionalResult(stack);

            if (itemstack.isEmpty() && stackOpt.isEmpty()) {
                return new WashResultSet(stack, false);
            } else {
                ItemStack itemstack1 = itemstack.copy();
                itemstack1.setCount(itemstack.getCount());
                ItemStack itemstack2 = stackOpt.copy();
                itemstack2.setCount(stackOpt.getCount());

                if (Reference.random.nextInt(5) == 0) {
                    return new WashResultSet(itemstack1, itemstack2, true);
                }
                return new WashResultSet(itemstack1, true);
            }
        }
    }

    private class SmeltResultSet {
        ItemStack stack;
        boolean hasRecipe;
        private SmeltResultSet(ItemStack stack, boolean hasRecipe) {
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
    private class WashResultSet {
        ItemStack stack, stackOptional;
        boolean hasRecipe, hasOptional;

        private WashResultSet(ItemStack stack, boolean hasRecipe) {
            this.stack = stack;
            this.stackOptional = ItemStack.EMPTY;
            this.hasOptional = false;
            this.hasRecipe = hasRecipe;
        }

        private WashResultSet(ItemStack stack, ItemStack stackOptional, boolean hasRecipe) {
            this.stack = stack;
            this.stackOptional = stackOptional;
            this.hasOptional = true;
            this.hasRecipe = hasRecipe;
        }

        public boolean hasRecipe() {
            return hasRecipe;
        }

        public ItemStack getResult() {
            return stack;
        }
        public ItemStack getResultOptional() {
            return stackOptional;
        }


        public boolean hasOptional() {
            return hasOptional;
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
