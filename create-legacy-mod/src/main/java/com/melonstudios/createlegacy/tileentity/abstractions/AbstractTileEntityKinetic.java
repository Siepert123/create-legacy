package com.melonstudios.createlegacy.tileentity.abstractions;

import com.melonstudios.createapi.kinetic.IKineticTileEntity;
import com.melonstudios.createapi.kinetic.IStateFindable;
import com.melonstudios.createapi.network.NetworkContext;
import com.melonstudios.createlegacy.CreateConfig;
import com.melonstudios.createlegacy.block.BlockRender;
import com.melonstudios.createlegacy.block.ModBlocks;
import com.melonstudios.createlegacy.util.EnumKineticConnectionType;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;

public abstract class AbstractTileEntityKinetic extends TileEntity implements ITickable, IStateFindable, IKineticTileEntity {

    public @Nullable BlockPos source;
    public @Nullable IBlockState sourceState;
    public boolean updateSpeed;
    protected float speed = 0;
    protected float newSpeed = 0;

    protected int flickers = 0;

    protected AbstractTileEntityKinetic() {
        super();
        updateSpeed = true;
    }

    public void clearInfo() {
        source = null;
        sourceState = null;
        updateSpeed = false;
        speed = flickers = 0;
    }

    public int getFlickers() {
        return flickers;
    }

    private boolean updateLoad = true;

    @Override
    public void onLoad() {
        super.onLoad();
    }

    public float speed() {
        return this.speed;
    }
    public int getWorkTick() {
        return Math.round(Math.abs(speed()));
    }
    public void updateSpeed(float speed) {
        this.speed = speed;
        updateSpeed = true;
    }
    public void setSource(BlockPos pos) {
        this.source = pos;
        this.sourceState = world.getBlockState(pos);
    }
    @Nullable
    public BlockPos getSource() {
        return this.source;
    }

    @Override
    public double getMaxRenderDistanceSquared() {
        return CreateConfig.kineticBlocksRenderDistanceSquared;
    }

    @Override
    @Nonnull
    public AxisAlignedBB getRenderBoundingBox() {
        return INFINITE_EXTENT_AABB;
    }

    protected abstract String namePlate();

    @Nullable
    @Override
    public ITextComponent getDisplayName() {
        return new TextComponentString(String.format("%s (%s RPM)", namePlate(), speed()));
    }

    public final boolean shifted(@Nullable EnumFacing.Axis axis) {
        if (axis == null) return shifted(EnumFacing.Axis.Y);
        switch (axis) {
            case X:
                return Math.abs(pos.getY()) % 2 == Math.abs(pos.getZ()) % 2;
            case Y:
                return Math.abs(pos.getX()) % 2 == Math.abs(pos.getZ()) % 2;
            case Z:
                return Math.abs(pos.getY()) % 2 == Math.abs(pos.getX()) % 2;
        }
        return false;
    }

    /**
     * What connection type is at that side?
     * @param side Side
     * @return The connection type
     */
    public abstract EnumKineticConnectionType getConnectionType(EnumFacing side);

    private int nextCheck;
    private long lastUpdate;

    @Override
    public final void update() {
        if (world.getTotalWorldTime() >= lastUpdate + nextCheck) clearInfo();
        if (updated) updated = false;
        if (flickers > 0) flickers--;

        if (updateLoad) {
            updateLoad = false;
            world.getBlockState(pos).getBlock().onBlockAdded(world, pos, world.getBlockState(pos));
        }

        tick();
    }

    /**
     * Some network functions that are mandatory yes
     * @param context network context
     */
    public final void networkFunc(NetworkContext context) {
        nextCheck = 2;
        lastUpdate = world.getTotalWorldTime();

        kineticTick(context);
    }

    protected void tick() {

    }

    /**
     * no one ever uses this
     * @param context context
     */
    @Deprecated
    public void kineticTick(NetworkContext context) {

    }

    /**
     * Pass the network! Usually no need to @Override :3
     * @param src source te (unused)
     * @param srcDir source direction (unused)
     * @param context network context
     * @param inverted inverted rotation?
     */
    public void passNetwork(IKineticTileEntity src, EnumFacing srcDir, NetworkContext context, boolean inverted) {
        if (enforceNoInversion && isInverted(context.speed() * (inverted ? -1 : 1))) {
            world.playEvent(2001, pos, Block.getStateId(getState()));
            getState().getBlock().dropBlockAsItem(world, pos, getState(), 0);
            world.setBlockToAir(pos);
        } else if (context.checked(this)) {
            if (context.isInverted(this) != inverted) {
                world.playEvent(2001, pos, Block.getStateId(getState()));
                getState().getBlock().dropBlockAsItem(world, pos, getState(), 0);
                world.setBlockToAir(pos);
            }
        } else {
            context.add(this, inverted);
            for (EnumFacing dir : EnumFacing.VALUES) {
                IKineticTileEntity te = getTE(pos.offset(dir));
                if (mayConnect(te, dir, dir.getOpposite())) {
                    if (getConnectionType(dir).inverts()) {
                        te.passNetwork(this, dir.getOpposite(), context, !inverted);
                    } else {
                        te.passNetwork(this, dir.getOpposite(), context, inverted);
                    }
                }
            }
        }
    }

    public final IKineticTileEntity getTE(BlockPos pos) {
        TileEntity entity = world.getTileEntity(pos);
        return entity instanceof IKineticTileEntity ? (IKineticTileEntity) entity : null;
    }

    @Override
    public final IBlockState getState() {
        return world.getBlockState(pos);
    }


    private boolean updated = false;
    public final void setUpdated() {
        updated = false;
    }
    public final boolean isUpdated() {
        return updated;
    }

    public boolean mayConnect(IKineticTileEntity other, EnumFacing mySide, EnumFacing otherSide) {
        if (other == null) return false;
        return getConnectionType(mySide) != EnumKineticConnectionType.NONE
                && getConnectionType(mySide).compare(other.getConnectionType(otherSide));
    }

    @Override
    public final boolean hasFastRenderer() {
        return false;
    }

    protected boolean isInverted(float speedIn) {
        if (generatedRPM() > 0 && speedIn < 0) return true;
        if (generatedRPM() < 0 && speedIn > 0) return true;
        return false;
    }

    public float generatedRPM() {
        return 0.0f;
    }
    public float generatedSUMarkiplier() {
        return 0.0f;
    }
    public final boolean isGenerator() {
        return generatedRPM() != 0;
    }
    public float consumedStressMarkiplier() {
        return 0.0f;
    }
    public final boolean isConsumer() {
        return consumedStressMarkiplier() != 0;
    }

    protected static EnumKineticConnectionType connection(int id) {
        return EnumKineticConnectionType.values()[id];
    }
    protected static IBlockState renderingPart(int id) {
        return ModBlocks.RENDER.getDefaultState().withProperty(BlockRender.TYPE, BlockRender.Type.fromId(id));
    }

    public boolean enforceNonInversion() {
        return false;
    }

    protected static void saveItemStack(NBTTagCompound father, ItemStack stack, String registry) {
        if (!stack.isEmpty() && father != null) {
            NBTTagCompound nbt = new NBTTagCompound();
            stack.writeToNBT(nbt);
            father.setTag(registry, nbt);
        }
    }
    protected static ItemStack readItemStack(NBTTagCompound father, String registry) {
        if (father.hasKey(registry)) {
            return new ItemStack(father.getCompoundTag(registry));
        }
        return ItemStack.EMPTY;
    }

    /**
     * Spawns a new item at the tile entity
     * @param stack Itemstack to spawn
     */
    protected void spawnItem(ItemStack stack) {
        if (!stack.isEmpty()) world.spawnEntity(new EntityItem(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, stack));
    }

    @Override
    public Random random() {
        return world.rand;
    }


    public boolean enforceNoInversion = false;
}
