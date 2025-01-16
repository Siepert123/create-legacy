package com.melonstudios.createlegacy.tileentity;

import com.google.common.base.Predicate;
import com.melonstudios.createapi.kinetic.INeedsRecalculating;
import com.melonstudios.createapi.network.NetworkContext;
import com.melonstudios.createlegacy.block.BlockRender;
import com.melonstudios.createlegacy.block.ModBlocks;
import com.melonstudios.createlegacy.block.kinetic.BlockFan;
import com.melonstudios.createlegacy.recipe.WashingRecipes;
import com.melonstudios.createlegacy.tileentity.abstractions.AbstractTileEntityKinetic;
import com.melonstudios.createlegacy.util.*;
import com.melonstudios.melonlib.misc.MetaBlock;
import com.melonstudios.melonlib.predicates.*;
import net.minecraft.block.Block;
import net.minecraft.block.BlockColored;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TileEntityFan extends AbstractTileEntityKinetic implements INeedsRecalculating {
    @Override
    protected String namePlate() {
        return "Fan";
    }

    @Override
    public float consumedStressMarkiplier() {
        return 6;
    }

    @Override
    public float generatedSUMarkiplier() {
        return 2;
    }

    @Override
    public float generatedRPM() {
        if (facing() == EnumFacing.DOWN) {
            if (world.isBlockIndirectlyGettingPowered(pos) > 0 || world.isBlockPowered(pos)) {
                if (HeatHelper.isBlockPassivelyHeated(world, pos.down())) {
                    return 8;
                }
            }
        }
        return 0;
    }

    @Override
    public EnumKineticConnectionType getConnectionType(EnumFacing side) {
        return facing().getOpposite() == side ? connection(1) : connection(0);
    }
    protected EnumFacing facing() {
        return getState().getValue(BlockFan.FACING);
    }
    public IBlockState getAssociatedPropellerPart() {
        IBlockState render = ModBlocks.RENDER.getDefaultState();
        switch (facing()) {
            case UP: return render.withProperty(BlockRender.TYPE, BlockRender.Type.FAN_U);
            case DOWN: return render.withProperty(BlockRender.TYPE, BlockRender.Type.FAN_D);
            case NORTH: return render.withProperty(BlockRender.TYPE, BlockRender.Type.FAN_N);
            case EAST: return render.withProperty(BlockRender.TYPE, BlockRender.Type.FAN_E);
            case SOUTH: return render.withProperty(BlockRender.TYPE, BlockRender.Type.FAN_S);
            case WEST: return render.withProperty(BlockRender.TYPE, BlockRender.Type.FAN_W);
        }
        return render;
    }

    protected AirCurrent current;

    @Override
    protected void tick() {
        if (generatedRPM() > 0) {
            if (!isUpdated()) {
                NetworkContext context = new NetworkContext(world);
                passNetwork(null, null, context, false);
                context.start();
            }
        } else {
            if (current == null) current = new AirCurrent(this);
            else current.tick();
        }
    }

    protected static double max(double d0, double d1) {
        if (Math.abs(d0) > Math.abs(d1)) return d0;
        return d1;
    }

    @Override
    public void recalculate() {
        current = new AirCurrent(this);
    }

    public static class AirCurrent {
        public final World world;
        public final BlockPos source;
        public final EnumFacing facing;
        public final TileEntityFan fan;
        public float strength = 0.0f;
        public int maxDistance = 0;
        public int actualMaxDistance = 0;
        public int catalyst = -1;

        public AirCurrent(TileEntityFan fan) {
            this.world = fan.getWorld();
            this.source = fan.getPos();
            this.facing = fan.facing();
            this.fan = fan;

            recalculate();
        }

        public void recalculate() {
            this.strength = fan.speed() / 1024f;
            this.maxDistance = this.actualMaxDistance = Math.min(Math.max(Math.round(Math.abs(fan.speed()) / 8), 4), 16);
            IBlockState possibleCatalyst = world.getBlockState(source.offset(facing, 1));
            if (strength > 0) {
                if (anyMatch(WASHING_CATALYSTS, possibleCatalyst)) {
                    catalyst = 0;
                } else if (anyMatch(COOKING_CATALYSTS, possibleCatalyst)) {
                    catalyst = 1;
                } else if (anyMatch(HAUNTING_CATALYSTS, possibleCatalyst)) {
                    catalyst = 2;
                } else catalyst = -1;
            } else catalyst = -1;

            for (int i = 1; i <= maxDistance; i++) {
                IBlockState state = world.getBlockState(source.offset(facing, i));
                if (state.getMaterial().blocksMovement()
                        && state.getBlock().canCollideCheck(state, false)
                        && state.getCollisionBoundingBox(world, source.offset(facing, i)) != Block.NULL_AABB
                        && !anyMatch(FAN_PASSES, state)) {
                    actualMaxDistance = i;
                    break;
                }
            }
        }

        public void tick() {
            if (fan.speed() == 0) return;
            recalculate();
            final AxisAlignedBB boundingBox = new AxisAlignedBB(source, source.offset(facing, actualMaxDistance).add(1, 1, 1));

            List<Entity> entities = world.getEntitiesWithinAABB(Entity.class, boundingBox);

            for (Entity entity : entities) {
                double dist = Math.sqrt(entity.getDistanceSq(source));
                double part = dist / actualMaxDistance;
                double str = Math.sqrt(1 - part * part) * strength;
                if (!Double.isNaN(str) && !Double.isInfinite(str)) {
                    entity.motionX = max(entity.motionX / 1.2, str * facing.getFrontOffsetX());
                    entity.motionY = max(entity.motionY / 1.2, str * facing.getFrontOffsetY());
                    entity.motionZ = max(entity.motionZ / 1.2, str * facing.getFrontOffsetZ());
                    if (str > 0.1) entity.fallDistance = 0.0f;
                    if (catalyst == 0) entity.extinguish();
                    if (catalyst == 1) entity.setFire(5);
                }
            }

            if (world.isRemote) createWindParticles();

            if (catalyst != -1) {
                for (int i = 1; i < actualMaxDistance; i++) {
                    TileEntity te = world.getTileEntity(source.offset(facing, i).down());
                    if (te instanceof TileEntityDepot) {
                        TileEntityDepot depot = (TileEntityDepot) te;
                        if (depot.getOutput().isEmpty() && depot.getOutput2().isEmpty() && hasRecipe(depot.getStack())) {
                            if (catalyst == 1) {
                                world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL,
                                        depot.getPos().getX() + 0.5,
                                        depot.getPos().getY() + 0.9,
                                        depot.getPos().getZ() + 0.5,
                                        0, 0, 0);
                            } else if (catalyst == 0) {
                                world.spawnParticle(EnumParticleTypes.WATER_SPLASH,
                                        depot.getPos().getX() + 0.5,
                                        depot.getPos().getY() + 0.9,
                                        depot.getPos().getZ() + 0.5,
                                        0, 0, 0);
                            }
                            if (depot.processingProgress > 50 * ((int)Math.ceil(depot.getStack().getCount() / 8f))) {
                                depotFunc(depot);
                                depot.processingProgress = 0;
                            } else depot.processingProgress ++;
                        }
                    }
                    if (world.isRemote) createCatalystParticles(i);
                }
            }
        }

        private void createWindParticles() {
            int stateId = catalyst == 0 ?
                    Block.getStateId(Blocks.WOOL.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.BLUE)) :
                    (catalyst == 1 ?
                            Block.getStateId(Blocks.WOOL.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.BLACK)) :
                            Block.getStateId(Blocks.WOOL.getDefaultState()));
            for (int i = 1; i < actualMaxDistance; i++) {
                if (world.rand.nextFloat() < 0.2f) {
                    BlockPos pos = source.offset(facing, i);
                    EnumParticleTypes particle = EnumParticleTypes.FALLING_DUST;
                    for (int j = 0; j < world.rand.nextInt(2) + 1; j++) {
                        world.spawnParticle(particle,
                                pos.getX() + world.rand.nextDouble(),
                                pos.getY() + world.rand.nextDouble(),
                                pos.getZ() + world.rand.nextDouble(),
                                facing.getFrontOffsetX() * 0.1f,
                                facing.getFrontOffsetY() * 0.1f,
                                facing.getFrontOffsetZ() * 0.1f,
                                stateId);
                    }
                }
            }
        }
        private void createCatalystParticles(int i) {
            if (world.rand.nextFloat() < 0.1f) {
                BlockPos pos = source.offset(facing, i);
                EnumParticleTypes particle = catalyst == 0 ? EnumParticleTypes.DRIP_WATER : (catalyst == 1 ? EnumParticleTypes.FLAME : EnumParticleTypes.NOTE);
                for (int j = 0; j < world.rand.nextInt(2) + 1; j++) {
                    world.spawnParticle(particle,
                            pos.getX() + world.rand.nextDouble(),
                            pos.getY() + world.rand.nextDouble(),
                            pos.getZ() + world.rand.nextDouble(),
                            0, 0, 0);
                }
            }
        }
        private boolean hasRecipe(ItemStack stack) {
            if (catalyst == 0) {
                return WashingRecipes.hasResult(stack);
            } else if (catalyst == 1) {
                return !FurnaceRecipes.instance().getSmeltingResult(stack).isEmpty();
            } else if (catalyst == 2) {
                return false; //No haunting (yet)
            }
            return false;
        }

        private int computateStack(int perStack, int stacks, float chance) {
            int result = 0;
            Random random = world.rand;
            for (int i = 0; i < stacks; i++) {
                if (random.nextFloat() < chance) result += perStack;
            }
            return result;
        }

        private void depotFunc(TileEntityDepot te) {
            if (catalyst == 0) {
                world.playSound(null, te.getPos(),
                        SoundEvents.ENTITY_GENERIC_SPLASH, SoundCategory.BLOCKS,
                        0.5f, 0.9f + world.rand.nextFloat() * 0.2f);
                SimpleTuple<ItemStack, Float>[] results = WashingRecipes.getResults(te.getStack());
                if (results.length >= 1) {
                    ItemStack result = results[0].getValue1().copy();
                    result.setCount(computateStack(result.getCount(), te.getStack().getCount(), results[0].getValue2()));
                    te.setOutput(result);
                    if (results.length >= 2) {
                        ItemStack result2 = results[1].getValue1().copy();
                        result2.setCount(computateStack(result2.getCount(), te.getStack().getCount(), results[1].getValue2()));
                        te.setOutput2(result2);
                    }
                }
                te.setStack(ItemStack.EMPTY);
            } else if (catalyst == 1) {
                world.playSound(null, te.getPos(),
                        SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS,
                        0.5f, 0.9f + world.rand.nextFloat() * 0.2f);
                ItemStack result = FurnaceRecipes.instance().getSmeltingResult(te.getStack()).copy();
                result.setCount(result.getCount() * te.getStack().getCount());
                te.setOutput(result);
                te.setStack(ItemStack.EMPTY);
            } else if (catalyst == 2) {
                //TODO: haunting
            }
        }
    }

    protected static final List<Predicate<IBlockState>> WASHING_CATALYSTS = new ArrayList<>();
    protected static final List<Predicate<IBlockState>> COOKING_CATALYSTS = new ArrayList<>();
    protected static final List<Predicate<IBlockState>> HAUNTING_CATALYSTS = new ArrayList<>();
    protected static final List<Predicate<IBlockState>> FAN_PASSES = new ArrayList<>();

    public static void addWashingCatalyst(Predicate<IBlockState> predicate) {
        WASHING_CATALYSTS.add(predicate);
    }
    public static void addCookingCatalyst(Predicate<IBlockState> predicate) {
        COOKING_CATALYSTS.add(predicate);
    }
    public static void addHauntingCatalyst(Predicate<IBlockState> predicate) {
        HAUNTING_CATALYSTS.add(predicate);
    }
    public static void addFanPass(Predicate<IBlockState> predicate) {
        FAN_PASSES.add(predicate);
    }

    private static boolean anyMatch(Iterable<Predicate<IBlockState>> predicates, IBlockState state) {
        for (Predicate<IBlockState> predicate : predicates) {
            if (predicate.test(state)) return true;
        }
        return false;
    }

    static {
        addWashingCatalyst(StatePredicateWater.instance);

        addCookingCatalyst(StatePredicateLava.instance);
        addCookingCatalyst(StatePredicateFire.instance);
        addCookingCatalyst(new StatePredicateMetaBlock(MetaBlock.of(ModBlocks.BLAZE_BURNER, 1)));
        addCookingCatalyst(new StatePredicateBlock(ModBlocks.BLAZE_BURNER_LIT));

        addFanPass(StatePredicateFence.instance);
        addFanPass(StatePredicateFenceGate.instance);
        addFanPass(new StatePredicateBlockDict("create:fanPass"));
    }
}
