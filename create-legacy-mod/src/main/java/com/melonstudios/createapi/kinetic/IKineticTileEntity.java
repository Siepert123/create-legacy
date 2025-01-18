package com.melonstudios.createapi.kinetic;

import com.melonstudios.createapi.network.NetworkContext;
import com.melonstudios.createlegacy.block.BlockRender;
import com.melonstudios.createlegacy.block.ModBlocks;
import com.melonstudios.createlegacy.util.EnumKineticConnectionType;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Random;

/**
 * Implementing classes must extend {@link net.minecraft.tileentity.TileEntity}<br>
 * Sample implementation at {@link com.melonstudios.createlegacy.tileentity.abstractions.AbstractTileEntityKinetic}
 *
 * @since 0.1.0
 * @author Siepert123, moddingforreal
 * */
public interface IKineticTileEntity {
    /**
     * Clears all information stored in the TileEntity
     * @since 0.1.0
     * */
    void clearInfo();

    /**
     * @return Flickers as an int
     * */
    int getFlickers();

    /**
     * @return Speed of the TE
     * */
    float speed();

    /**
     * @return Work tick (int)
     * */
    int getWorkTick();

    /**
     * @param speed Speed to set TE to
     * */
    void updateSpeed(float speed);

    /**
     * @param pos Source position
     * */
    void setSource(BlockPos pos);

    /**
     * @return Source BlockPos
     * */
    @Nullable
    BlockPos getSource();

    /*
     *
     * *
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
    }*/

    /**
     * What connection type is at that side?
     * @param side Side
     * @return The connection type
     */
    public abstract EnumKineticConnectionType getConnectionType(EnumFacing side);

    /**
     * Some network functions that are mandatory yes
     * @param context network context
     */
    void networkFunc(NetworkContext context);
    /**
     * no one ever uses this
     * @param context context
     */
    @Deprecated
    void kineticTick(NetworkContext context);

    /**
     * Pass the network! Usually no need to @Override :3
     * @param src source te (unused)
     * @param srcDir source direction (unused)
     * @param context network context
     * @param inverted inverted rotation?
     */
    default void passNetwork(IKineticTileEntity src, EnumFacing srcDir, NetworkContext context, boolean inverted) {
        if (enforceNonInversion() && inverted) {
            getWorld().playEvent(2001, getPos(), Block.getStateId(getState()));
            getState().getBlock().dropBlockAsItem(getWorld(), getPos(), getState(), 0);
            getWorld().setBlockToAir(getPos());
        } else if (context.checked(this)) {
            if (context.isInverted(this) != inverted) {
                getWorld().playEvent(2001, getPos(), Block.getStateId(getState()));
                getState().getBlock().dropBlockAsItem(getWorld(), getPos(), getState(), 0);
                getWorld().setBlockToAir(getPos());
            }
        } else {
            context.add(this, inverted);
            for (EnumFacing dir : EnumFacing.VALUES) {
                IKineticTileEntity te = getTE(getPos().offset(dir));
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

    default boolean mayConnect(IKineticTileEntity other, EnumFacing mySide, EnumFacing otherSide) {
        if (other == null) return false;
        if (((TileEntity)this).getWorld().getBlockState(((TileEntity)other).getPos()).getBlock() == Blocks.AIR) return false;
        if (((TileEntity)this).getWorld().getBlockState(((TileEntity)this).getPos()).getBlock() == Blocks.AIR) return false;
        return getConnectionType(mySide) != EnumKineticConnectionType.NONE
                && getConnectionType(mySide).compare(other.getConnectionType(otherSide));
    }

    /**
     * @return TileEntity at pos
     * */
    IKineticTileEntity getTE(BlockPos pos);

    /**
     * Sets this TE to updated
     * */
    void setUpdated();

    /**
     * @return Whether it has been updated
     * */
    boolean isUpdated();

    /**
     * @return The RPM this TE "generates"
     * */
    default float generatedRPM() {
        return 0.0f;
    }

    /**
     * @return The SU Markiplier output by this TE
     * */
    default float generatedSUMarkiplier() {
        return 0.0f;
    }

    /**
     * @return Whether this TE is a generator
     * */
    default boolean isGenerator() {
        return generatedRPM() != 0;
    }

    /**
     * @return How many stress units this TE consumes
     * */
    default float consumedStressMarkiplier() {
        return 0.0f;
    }

    /**
     * @return Whether this TE is a consumer
     * */
    default boolean isConsumer() {
        return consumedStressMarkiplier() != 0;
    }

    static EnumKineticConnectionType connection(int id) {
        return EnumKineticConnectionType.values()[id];
    }
    static IBlockState renderingPart(int id) {
        return ModBlocks.RENDER.getDefaultState().withProperty(BlockRender.TYPE, BlockRender.Type.fromId(id));
    }

    default boolean enforceNonInversion() {
        return false;
    }

    static void saveItemStack(NBTTagCompound father, ItemStack stack, String registry) {
        if (!stack.isEmpty() && father != null) {
            NBTTagCompound nbt = new NBTTagCompound();
            stack.writeToNBT(nbt);
            father.setTag(registry, nbt);
        }
    }
    static ItemStack readItemStack(NBTTagCompound father, String registry) {
        if (father.hasKey(registry)) {
            return new ItemStack(father.getCompoundTag(registry));
        }
        return ItemStack.EMPTY;
    }

    /**
     * @return the position of this TE's block
     * */
    BlockPos getPos();

    /**
     * @return the BlockState of this TE's block
     * */
    IBlockState getState();

    /**
     * @return the World of this TE's block
     * */
    World getWorld();

    /**
     * @return the IBlockState
     * @deprecated
     * */
    @Deprecated
    default IBlockState State() {
        return this.getState();
    }

    /*
     * Spawns a new item at the tile entity
     * @param stack Itemstack to spawn
     *
    protected void spawnItem(ItemStack stack) {
        if (!stack.isEmpty()) world.spawnEntity(new EntityItem(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, stack));
    }*/

    Random random();
}
