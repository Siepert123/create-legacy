package com.melonstudios.createlegacy.tileentity;

import com.melonstudios.createlegacy.CreateLegacy;
import com.melonstudios.createlegacy.block.BlockRender;
import com.melonstudios.createlegacy.block.ModBlocks;
import com.melonstudios.createlegacy.block.kinetic.BlockPress;
import com.melonstudios.createlegacy.network.PacketUpdatePress;
import com.melonstudios.createlegacy.recipe.PressingRecipes;
import com.melonstudios.createlegacy.tileentity.abstractions.AbstractTileEntityKinetic;
import com.melonstudios.createlegacy.util.EnumKineticConnectionType;
import com.melonstudios.createlegacy.util.registries.ModSoundEvents;
import com.melonstudios.melonlib.misc.AdvancementUtil;
import net.minecraft.advancements.Advancement;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;

import java.util.List;

public class TileEntityPress extends AbstractTileEntityKinetic {
    @Override
    protected String namePlate() {
        return "Press";
    }

    public final EnumFacing.Axis axis() {
        return getState().getValue(BlockPress.AXIS);
    }
    public final IBlockState getAssociatedPressPart() {
        return ModBlocks.RENDER.getDefaultState()
                .withProperty(BlockRender.TYPE,
                        axis() == EnumFacing.Axis.X ? BlockRender.Type.PRESS_X : BlockRender.Type.PRESS_Z);
    }

    protected int previousProgress = 0;
    protected int progress = 0;
    protected final int maxProgress = 1280;
    public int getProgress() {
        return progress;
    }
    public void setProgress(int n) {
        this.progress = n;
    }
    public int getPreviousProgress() {
        return previousProgress;
    }
    public void setPreviousProgress(int n) {
        previousProgress = n;
    }

    boolean b = false;
    private void increaseProgress(int n) {
        previousProgress = progress;
        progress += Math.abs(n);
    }

    protected void playPressingSFX() {
        world.playSound(null, pos,
                ModSoundEvents.BLOCK_PRESS_ACTIVATE,
                SoundCategory.BLOCKS, 1.0f, Math.abs(speed()) / 64f);
    }

    final static private boolean ENABLE_DEPOT = true;
    @Override
    protected void tick() {
        if (!world.isRemote) {
            if (hasRecipe() || b) {
                markDirty();
                if (progress < maxProgress) increaseProgress(Math.round(speed()));
                else {
                    previousProgress = 0;
                    progress = 0;
                    b = false;
                }
                if (progress > maxProgress / 2 && !b) {
                    b = true;
                    TileEntity entity = world.getTileEntity(pos.down(2));
                    if (entity instanceof TileEntityDepot && ENABLE_DEPOT) {
                        TileEntityDepot depot = (TileEntityDepot) entity;

                        ItemStack input = depot.getStack();
                        ItemStack existingResult = depot.getOutput();

                        ItemStack result = PressingRecipes.getResult(input).copy();
                        if (existingResult.isItemEqual(result)) {
                            if (existingResult.getCount() < 64) {
                                existingResult.grow(1);
                                input.shrink(1);
                                playPressingSFX();
                            }
                        } else if (existingResult.isEmpty()) {
                            depot.setOutput(result);
                            input.shrink(1);
                            playPressingSFX();
                        }
                    } else {
                        ItemStack input = getItemOnDepotOrGround();

                        EntityItem item = new EntityItem(world,
                                pos.getX() + 0.5, pos.getY() - 1, pos.getZ() + 0.5,
                                PressingRecipes.getResult(input).copy());
                        item.motionX = item.motionY = item.motionZ = 0;

                        world.spawnEntity(item);
                        input.shrink(1);

                        playPressingSFX();
                    }
                    {
                        Advancement advancement = AdvancementUtil.getAdvancement(new ResourceLocation("create", "machines/press"));
                        List<EntityPlayerMP> players = world.getEntities(EntityPlayerMP.class, (player) -> player.getDistanceSq(pos) < 256);
                        for (EntityPlayerMP player : players) {
                            AdvancementUtil.grantAdvancement(player, advancement);
                        }
                    }
                }
                PacketUpdatePress.sendToPlayersNearby(this, 32);
            } else {
                previousProgress = progress;
            }
        }
        previousProgress = progress;
    }

    protected final IBlockState depotBlock = ModBlocks.DEPOT.getDefaultState();
    protected ItemStack getItemOnDepotOrGround() {
        if (world.getBlockState(pos.down(2)) == depotBlock) {
            TileEntity entity = world.getTileEntity(pos.down(2));
            if (entity instanceof TileEntityDepot) {
                TileEntityDepot depot = (TileEntityDepot) entity;

                boolean flag = depot.output.isEmpty() || PressingRecipes.getResult(depot.stack).isItemEqual(depot.output);
                if (flag) return depot.stack;

                return ItemStack.EMPTY;
            }
        } else {
            List<EntityItem> items = world.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(pos.down()));

            for (EntityItem entityItem : items) {
                ItemStack stack = entityItem.getItem();

                if (PressingRecipes.hasResult(stack)) {
                    return stack;
                }
            }
        }

        return ItemStack.EMPTY;
    }

    protected boolean hasRecipe() {
        return PressingRecipes.hasResult(getItemOnDepotOrGround());
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);

        compound.setInteger("progress", progress);

        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);

        progress = compound.getInteger("progress");
    }

    @Override
    public EnumKineticConnectionType getConnectionType(EnumFacing side) {
        return side.getAxis() == axis() ? connection(1) : connection(0);
    }
    protected float convertProgress(int progress) {
        if (progress > maxProgress / 2) {
            return (progress / (maxProgress / 2f) -2);
        }
        return progress / (maxProgress / 2f) * -1;
    }
    protected float getPreviousYOffset() {
        return convertProgress(previousProgress);
    }
    protected float getCurrentYOffset() {
        return convertProgress(progress);
    }

    public double getPressYOffset(float part) {
        return MathHelper.clampedLerp(getPreviousYOffset(),
                getCurrentYOffset(), part);
    }

    @Override
    public float consumedStressMarkiplier() {
        return 16.0f;
    }
}
