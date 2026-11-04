package nl.melonstudios.create.tileentity.funnel;

import com.melonstudios.melonlib.network.TrackedByteBuf;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.items.IItemHandler;
import nl.melonstudios.create.block.state.EnumFunnelState;
import nl.melonstudios.create.tileentity.marker.IDepot;
import nl.melonstudios.create.tileentity.marker.ITopOpenInventory;
import nl.melonstudios.create.util.filter.IItemFilter;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.List;

public class TileEntityFunnelWall extends TileEntityFunnelBase implements ITickable {
    public int cooldown = 0;
    public TileEntityFunnelWall() {
        super();
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt.setInteger("cooldown", this.cooldown);
        return super.writeToNBT(nbt);
    }
    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        this.cooldown = nbt.getInteger("cooldown");
        super.readFromNBT(nbt);
    }

    @Override
    public boolean isPowered(int meta) {
        return (meta & 0b1000) != 0;
    }
    public EnumFunnelState getFunnelState(int meta) {
        return EnumFunnelState.VALUES[(meta >> 2) & 0b0001];
    }
    public EnumFacing getFacing(int meta) {
        return EnumFacing.getHorizontal(meta & 0b0011);
    }

    protected TileEntity depot = null;
    protected void getDepot() {
        if (this.depot == null || this.depot.isInvalid()) {
            this.depot = this.world.getTileEntity(this.pos.down());
        }
    }

    @Nullable
    protected IItemFilter getFilter() {
        return null;
    }
    protected int getExtractionAmount() {
        return 1;
    }
    protected boolean isExtractionAmountExact() {
        return false;
    }

    @Override
    public void tick() {
        if (!this.world.isRemote) {
            if (this.isPowered()) {
                if (this.cooldown != 0) {
                    this.cooldown = 0;
                    this.markDirty();
                }
            } else {
                if (this.cooldown > 0) {
                    this.cooldown--;
                    this.markDirty();
                } else {
                    IItemHandler inventory = this.getInventory(this.getFacing(this.getBlockMetadata()).getOpposite());
                    if (inventory != null && inventory.getSlots() > 0) {
                        EnumFunnelState state = this.getFunnelState(this.getBlockMetadata());
                        this.tick(inventory, state);
                    }
                }
            }
        }
    }

    private BlockPos lastAABBPos = null;
    private AxisAlignedBB lastAABB = null;
    private AxisAlignedBB getEntityAABB() {
        if (this.lastAABB == null || (!this.pos.equals(this.lastAABBPos))) {
            this.lastAABB = new AxisAlignedBB(this.pos);
            this.lastAABBPos = this.pos.toImmutable();
        }
        return this.lastAABB;
    }

    @SuppressWarnings("unchecked")
    private <DEPOT extends TileEntity & IDepot, TOP_OPEN extends TileEntity & ITopOpenInventory> void tick(IItemHandler inventory, EnumFunnelState state) {
        this.getDepot();
        if (state == EnumFunnelState.INSERTING) {
            if (this.depot instanceof IDepot) {
                DEPOT depot = (DEPOT) this.depot;
                ItemStack presented = depot.getPresentedItem();
                if (presented.isEmpty() || (this.getFilter() != null && this.getFilter().matches(presented))) return;
                ItemStack copy = presented.copy();
                for (int i = 0; i < inventory.getSlots(); i++) {
                    copy = inventory.insertItem(i, copy, false);
                    if (copy.isEmpty()) break;
                }
                depot.setPresentedItem(copy.isEmpty() ? ItemStack.EMPTY : copy);
                this.cooldown = 2;
                this.markDirty();
            } else {
                List<EntityItem> candidates = this.world.getEntitiesWithinAABB(EntityItem.class, this.getEntityAABB());
                for (EntityItem entity : candidates) {
                    if (entity.isDead) continue;
                    ItemStack stack = entity.getItem();
                    if (stack.isEmpty()) {
                        this.world.removeEntity(entity);
                        continue;
                    }
                    if (this.getFilter() != null && this.getFilter().matches(stack)) continue;
                    ItemStack copy = stack.copy();
                    for (int i = 0; i < inventory.getSlots(); i++) {
                        copy = inventory.insertItem(i, copy, false);
                        if (copy.isEmpty()) break;
                    }
                    if (!ItemStack.areItemStacksEqual(stack, copy)) {
                        if (copy.isEmpty()) {
                            this.world.removeEntity(entity);
                            entity.setItem(ItemStack.EMPTY);
                        } else entity.setItem(copy);
                        this.cooldown = 10;
                        this.markDirty();
                        break;
                    }
                }
            }
        } else {
            if (this.depot instanceof ITopOpenInventory) {
                TOP_OPEN topOpen = (TOP_OPEN) this.depot;
                if (topOpen.isInsertionSlotEmpty(ItemStack.EMPTY)) {
                    for (int i = 0; i < inventory.getSlots(); i++) {
                        ItemStack stack = inventory.extractItem(i, this.getExtractionAmount(), true);
                        if (stack.isEmpty() || (this.isExtractionAmountExact() && stack.getCount() != this.getExtractionAmount())) continue;
                        if (this.getFilter() != null && !this.getFilter().matches(stack)) continue;
                        if (!topOpen.isInsertionSlotEmpty(stack)) continue;
                        stack = inventory.extractItem(i, this.getExtractionAmount(), false);
                        stack = topOpen.tryInsertItem(stack, this.getFacing(this.getBlockMetadata()).getOpposite());
                        if (!stack.isEmpty()) {

                        }
                        this.cooldown = 2;
                        this.markDirty();
                        break;
                    }
                }
            } else {
                List<EntityItem> candidates = this.world.getEntitiesWithinAABB(EntityItem.class, this.getEntityAABB());
                if (candidates.isEmpty()) {
                    for (int i = 0; i < inventory.getSlots(); i++) {
                        ItemStack stack = inventory.extractItem(i, this.getExtractionAmount(), true);
                        if (stack.isEmpty() || (this.isExtractionAmountExact() && stack.getCount() != this.getExtractionAmount())) continue;
                        if (this.getFilter() != null && !this.getFilter().matches(stack)) continue;
                        stack = inventory.extractItem(i, this.getExtractionAmount(), false);
                        EntityItem entity = new EntityItem(this.world,
                                this.pos.getX() + 0.5, this.pos.getY() + 0.5, this.pos.getZ() + 0.5,
                                stack.copy()
                        );
                        entity.motionX = entity.motionY = entity.motionZ = 0.0;
                        this.world.spawnEntity(entity);
                        this.cooldown = 10;
                        this.markDirty();
                        break;
                    }
                }
            }
        }
    }

    @Override
    public void writePacket(TrackedByteBuf buf) throws IOException {

    }

    @Override
    public void readPacket(ByteBuf buf) throws IOException {

    }
}
