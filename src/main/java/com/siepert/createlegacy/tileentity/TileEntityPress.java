package com.siepert.createlegacy.tileentity;

import com.siepert.createapi.network.IKineticTE;
import com.siepert.createapi.network.KineticBlockInstance;
import com.siepert.createapi.network.NetworkContext;
import com.siepert.createlegacy.CreateLegacy;
import com.siepert.createlegacy.CreateLegacyConfigHolder;
import com.siepert.createlegacy.CreateLegacyModData;
import com.siepert.createlegacy.blocks.kinetic.BlockBlazeBurner;
import com.siepert.createlegacy.blocks.kinetic.BlockItemHolder;
import com.siepert.createlegacy.blocks.kinetic.BlockMechanicalPress;
import com.siepert.createlegacy.util.handlers.ModSoundHandler;
import com.siepert.createlegacy.util.handlers.recipes.CompactingRecipes;
import com.siepert.createlegacy.util.handlers.recipes.PressingRecipes;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;

import java.util.List;

import static com.siepert.createlegacy.blocks.kinetic.BlockMechanicalPress.EXTENDED;

public class TileEntityPress extends TileEntity implements IKineticTE {

    int pressingProgress = 0;

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);

        compound.setInteger("Progress", pressingProgress);

        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);

        pressingProgress = compound.getInteger("Progress");
    }

    @Override
    public double getStressImpact() {
        return CreateLegacyConfigHolder.kineticConfig.mechanicalPressStressImpact;
    }

    @Override
    public boolean isConsumer() {
        return true;
    }

    @Override
    public double getStressCapacity() {
        return 0;
    }

    @Override
    public int getProducedSpeed() {
        return 0;
    }

    @Override
    public void kineticTick(NetworkContext context) {
        boolean isCompactor = world.getBlockState(pos.down(2)).getBlock() instanceof BlockItemHolder;
        if (isCompactor) {
            isCompactor = world.getBlockState(pos.down(2)).getValue(BlockItemHolder.VARIANT) == BlockItemHolder.Variant.BASIN;
        }

        BlockBlazeBurner.State heatState;
        if (isCompactor && world.getBlockState(pos.down(3)).getBlock() instanceof BlockBlazeBurner) {
            heatState = world.getBlockState(pos.down(3)).getValue(BlockBlazeBurner.STATE);
        } else heatState = BlockBlazeBurner.State.EMPTY;

        if (!world.isRemote) {
            IBlockState state = world.getBlockState(pos);
            if (pressingProgress > 92) {
                if (!state.getValue(EXTENDED)) {
                    TileEntity entity = world.getTileEntity(pos);

                    world.setBlockState(pos, state.withProperty(EXTENDED, true), 3);

                    if (entity != null) {
                        entity.validate();
                        world.setTileEntity(pos, entity);
                    }
                }
            } else {
                if (state.getValue(EXTENDED)) {
                    TileEntity entity = world.getTileEntity(pos);

                    world.setBlockState(pos, state.withProperty(EXTENDED, false), 3);

                    if (entity != null) {
                        entity.validate();
                        world.setTileEntity(pos, entity);
                    }
                }
            }
        }

        if (!world.getBlockState(pos.down()).getMaterial().blocksMovement() && !world.isRemote && pressingProgress == 0) {
            if (!isCompactor) {
                AxisAlignedBB itemSearchArea = new AxisAlignedBB(pos.down());
                List<EntityItem> foundItems = world.getEntitiesWithinAABB(EntityItem.class, itemSearchArea);

                for (EntityItem entityItem : foundItems) {
                    if (apply(entityItem.getItem()).hasRecipe) {
                        pressingProgress = 100;
                        EntityItem resultEntityItem = new EntityItem(world, pos.getX() + 0.5, pos.down().getY(), pos.getZ() + 0.5,
                                apply(entityItem.getItem()).stack);
                        entityItem.getItem().shrink(1);
                        resultEntityItem.setVelocity(0, 0, 0);
                        resultEntityItem.setNoDespawn();
                        world.spawnEntity(resultEntityItem);
                        if (entityItem.getItem().getCount() == 0 || entityItem.getItem().isEmpty()) {
                            entityItem.setDead();
                        }

                        float pitch;
                        if (CreateLegacyModData.random.nextInt(100) == 0) {
                            pitch = 0.1f;
                        } else pitch = 0.8f;
                        world.playSound(null, pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D,
                                ModSoundHandler.BLOCK_PRESS_ACTIVATION, SoundCategory.BLOCKS, 1.0f, pitch);
                        return;
                    }
                }
                return;
            }
            AxisAlignedBB itemSearchArea = new AxisAlignedBB(pos.down(2));
            List<EntityItem> foundItems = world.getEntitiesWithinAABB(EntityItem.class, itemSearchArea);

            for (EntityItem entityItem : foundItems) {
                if (applyCompact(entityItem.getItem(), heatState).hasRecipe) {
                    pressingProgress = 100;
                    EntityItem resultEntityItem = new EntityItem(world, pos.getX() + 0.5, pos.down(2).getY() + 0.2, pos.getZ() + 0.5,
                            applyCompact(entityItem.getItem(), heatState).getResult());
                    entityItem.getItem().shrink(applyCompact(entityItem.getItem(), heatState).cost);
                    resultEntityItem.setVelocity(0, 0, 0);
                    resultEntityItem.setNoDespawn();
                    world.spawnEntity(resultEntityItem);
                    if (entityItem.getItem().getCount() == 0 || entityItem.getItem().isEmpty()) {
                        entityItem.setDead();
                    }

                    float pitch;
                    if (CreateLegacyModData.random.nextInt(100) == 0) {
                        pitch = 0.1f;
                    } else pitch = 0.8f;
                    world.playSound(null, pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, ModSoundHandler.BLOCK_PRESS_ACTIVATION, SoundCategory.BLOCKS, 1.0f, pitch);
                    return;
                }
            }
        }

        if (pressingProgress > 0) {
            pressingProgress -= Math.max(context.networkSpeed / 16, 1);
        }
        if (pressingProgress < 0) {
            pressingProgress = 0;
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

            if (itemstack.isEmpty()) {
                return new PressingResultSet(stack, false);
            }
            else {
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
    public void setUpdated() {

    }

    @Override
    public void passNetwork(NetworkContext context, EnumFacing source, boolean srcIsCog, boolean srcCogIsHorizontal, boolean inverted) {
        if (srcIsCog) return;
        IBlockState state = world.getBlockState(pos);

        if (source.getAxis() == state.getValue(BlockMechanicalPress.AXIS)) {
            context.addKineticBlockInstance(new KineticBlockInstance(pos, inverted));

            TileEntity entity = world.getTileEntity(pos.offset(source.getOpposite()));

            if (entity instanceof IKineticTE) {
                ((IKineticTE) entity).passNetwork(context, source, false, false, inverted);
            }
        }
    }
}
