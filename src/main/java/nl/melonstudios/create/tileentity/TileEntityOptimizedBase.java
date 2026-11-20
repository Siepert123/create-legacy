package nl.melonstudios.create.tileentity;

import com.melonstudios.melonlib.tileentity.ISyncedTE;
import com.melonstudios.melonlib.tileentity.TileEntityCachedRenderBB;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import nl.melonstudios.create.util.interfaces.IStateFindable;
import nl.melonstudios.ponder.IVirtualizable;

public abstract class TileEntityOptimizedBase extends TileEntityCachedRenderBB implements ISyncedTE, ITickable, IStateFindable, IVirtualizable {
    private int tickRateLazy, tickCounterLazy;
    private boolean requestedSynchronization = false;
    private boolean syncNextTick = false;
    private boolean requestSyncNextTick = false;
    private boolean initialized = false;
    private boolean unloadedChunk = false;
    private boolean preventNextRemoval = false;
    private int clientDelay = -1;
    public void preventNextRemoval() {
        this.preventNextRemoval = true;
    }

    protected void syncNextTick() {
        this.syncNextTick = true;
    }

    @Override
    public void onChunkUnload() {
        super.onChunkUnload();
        this.unloadedChunk = true;
    }

    @Override
    public void invalidate() {
        super.invalidate();
        if (!this.unloadedChunk) {
            if (!this.preventNextRemoval) this.remove();
            this.preventNextRemoval = false;
        }
    }

    /**
     * Changes the lazy tick rate for this tile entity.
     * @param rate The new lazy tick rate
     * @since 1.6
     */
    protected void setTickRateLazy(int rate) {
        this.tickRateLazy = this.tickCounterLazy = rate;
    }

    @Override
    public void sync() {
        if (this.isVirtual()) return;
        this.markDirty();
        if (this.pos != null)
            this.requestedSynchronization = true;
        else this.syncNextTick = true;
    }

    @Override
    public void requestSync() {
        if (this.isVirtual()) return;
        if (this.pos != null)
            ISyncedTE.super.requestSync();
        else this.requestSyncNextTick = true;
    }

    @Override
    public void onLoad() {
        if (this.world.isRemote) this.requestSync();
    }

    @Override
    public boolean compressPacketNBT() {
        return false;
    }

    @Override
    public final void update() {
        if (!this.world.isRemote) {
            if (!this.initialized) {
                this.initialize();
                this.markDirty();
                this.initialized = true;
            }
            if (this.syncNextTick) {
                this.syncNextTick = false;
                this.sync();
            }
        }
        if (this.world.isRemote) {
            if (!this.initialized) {
                this.initializeClient();
                this.initialized = true;
            }
            if (this.requestSyncNextTick) {
                this.requestSyncNextTick = false;
                this.requestSync();
            }
            if (this.clientDelay > 0) {
                this.clientDelay--;
                if (this.clientDelay == 0) {
                    this.requestSync();
                    this.clientDelay = -1;
                }
            }
        }
        this.tick();
        if (this.tickCounterLazy-- <= 0) {
            this.tickCounterLazy = this.tickRateLazy;
            this.tickLazy();
        }
        if (!this.world.isRemote && this.requestedSynchronization && !this.isVirtual()) {
            this.requestedSynchronization = false;
            ISyncedTE.super.sync();
        }
    }

    public void destroy() {}

    public void remove() {}

    public void initialize() {
        this.tickLazy();
    }
    public void initializeClient() {

    }
    /**
     * Called every tick
     * @since 1.6
     */
    public abstract void tick();

    /**
     * Called every lazy tick
     * @since 1.6
     * @see #setTickRateLazy(int)
     */
    public abstract void tickLazy();

    @Override
    public IBlockState getState() {
        return this.getBlockType().getStateFromMeta(this.getBlockMetadata());
    }

    private boolean virtualized = false;
    @Override
    public void markAsVirtual() {
        this.virtualized = true;
    }
    @Override
    public boolean isVirtual() {
        return this.virtualized;
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        return this.writeToNBT(new NBTTagCompound());
    }
}
