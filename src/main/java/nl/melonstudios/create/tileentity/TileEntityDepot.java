package nl.melonstudios.create.tileentity;

import com.melonstudios.melonlib.misc.StackUtil;
import com.melonstudios.melonlib.network.TrackedByteBuf;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import nl.melonstudios.create.tileentity.marker.IDepot;
import nl.melonstudios.create.tileentity.marker.ITopOpenInventory;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.List;

public class TileEntityDepot extends TileEntityOptimizedBase implements ITopOpenInventory, IDepot, IItemHandler {
    public float randomizedItemRotation;
    public TileEntityDepot() {
        this.setTickRateLazy(10);
    }

    public ItemStack mainItem = ItemStack.EMPTY;
    public ItemStack[] additionalItems = {
            ItemStack.EMPTY,
            ItemStack.EMPTY,
            ItemStack.EMPTY,
            ItemStack.EMPTY,
            ItemStack.EMPTY,
            ItemStack.EMPTY,
            ItemStack.EMPTY,
            ItemStack.EMPTY,
    };

    @Override
    public void initializeClient() {
        super.initializeClient();

        this.randomizedItemRotation = this.world.rand.nextInt(360);
    }

    @Override
    public void tick() {

    }

    @Override
    public void tickLazy() {
        if (this.mainItem.isEmpty()) {
            AxisAlignedBB aabb = new AxisAlignedBB(this.pos);
            List<EntityItem> items = this.world.getEntities(EntityItem.class,
                    (entity) -> entity.getEntityBoundingBox().intersects(aabb));
            if (!items.isEmpty()) {
                EntityItem select = items.get(0);
                this.handleSteppedOn(select);
            }
        }
    }
    public void handleSteppedOn(EntityItem entityItem) {
        if (this.mainItem.isEmpty() && !entityItem.isDead && !entityItem.getItem().isEmpty()) {
            ItemStack over = this.tryInsertItem(entityItem.getItem());
            if (over.isEmpty()) {
                entityItem.setDead();
            } else entityItem.setItem(over);
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        NBTTagCompound nbt = super.writeToNBT(compound);

        if (!this.mainItem.isEmpty()) nbt.setTag("MainItem", this.mainItem.writeToNBT(new NBTTagCompound()));
        for (int i = 0; i < 8; i++) {
            if (!this.additionalItems[i].isEmpty()) nbt.setTag("AdditionalItem" + i, this.additionalItems[i].writeToNBT(new NBTTagCompound()));
        }

        return nbt;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);

        if (nbt.hasKey("MainItem", 10)) {
            this.mainItem = new ItemStack(nbt.getCompoundTag("MainItem"));
        } else this.mainItem = ItemStack.EMPTY;
        for (int i = 0; i < 8; i++) {
            if (nbt.hasKey("AdditionalItem" + i, 10)) {
                this.additionalItems[i] = new ItemStack(nbt.getCompoundTag("AdditionalItem" + i));
            } else this.additionalItems[i] = ItemStack.EMPTY;
        }
    }

    @Override
    public void writePacket(TrackedByteBuf buf) throws IOException {
        if (this.mainItem.isEmpty()) {
            buf.writeBoolean(false);
        } else {
            buf.writeBoolean(true);
            StackUtil.writeItemStack(this.mainItem, buf, true, true);
        }

        for (int i = 0; i < 8; i++) {
            if (this.additionalItems[i].isEmpty()) {
                buf.writeBoolean(false);
            } else {
                buf.writeBoolean(true);
                StackUtil.writeItemStack(this.additionalItems[i], buf, true, true);
            }
        }
    }

    @Override
    public void readPacket(ByteBuf buf) throws IOException  {
        if (buf.readBoolean()) {
            this.mainItem = StackUtil.readItemStack(buf, true, true);
        } else this.mainItem = ItemStack.EMPTY;
    }

    @Override
    public NBTTagCompound writePacket() {
        NBTTagCompound nbt = new NBTTagCompound();

        if (!this.mainItem.isEmpty()) nbt.setTag("MainItem", this.mainItem.writeToNBT(new NBTTagCompound()));
        for (int i = 0; i < 8; i++) {
            if (!this.additionalItems[i].isEmpty()) nbt.setTag("AdditionalItem" + i, this.additionalItems[i].writeToNBT(new NBTTagCompound()));
        }

        return nbt;
    }

    @Override
    public void readPacket(NBTTagCompound nbt) {
        if (nbt.hasKey("MainItem", 10)) {
            this.mainItem = new ItemStack(nbt.getCompoundTag("MainItem"));
        } else this.mainItem = ItemStack.EMPTY;
        for (int i = 0; i < 8; i++) {
            if (nbt.hasKey("AdditionalItem" + i, 10)) {
                this.additionalItems[i] = new ItemStack(nbt.getCompoundTag("AdditionalItem" + i));
            } else this.additionalItems[i] = ItemStack.EMPTY;
        }
    }

    public boolean isEmpty() {
        if (!this.mainItem.isEmpty()) return false;
        for (int i = 0; i < 8; i++) {
            if (!this.additionalItems[i].isEmpty()) return false;
        }
        return true;
    }

    @Override
    public ItemStack tryInsertItem(ItemStack stack) {
        if (stack.isEmpty()) return ItemStack.EMPTY;
        if (!this.mainItem.isEmpty()) return stack;
        this.mainItem = stack.copy();
        this.sync();
        return ItemStack.EMPTY;
    }

    @Override
    public boolean isInsertionSlotEmpty(ItemStack stack) {
        return this.mainItem.isEmpty();
    }

    @Override
    public void destroy() {
        super.destroy();

        StackUtil.dropItemsAt(this.world, this.pos, this.mainItem);
        StackUtil.dropItemsAt(this.world, this.pos, this.additionalItems);
    }

    @Override
    public ItemStack getPresentedItem() {
        return this.mainItem;
    }

    @Override
    public void setPresentedItem(ItemStack stack) {
        this.mainItem = stack;
        this.sync();
    }

    @Override
    public void decreasePresentedAndAddOutput(ItemStack output) {
        this.mainItem.shrink(1);
        if (this.mainItem.isEmpty()) {
            this.mainItem = output;
        } else {
            for (int i = 0; i < 8; i++) {
                if (this.additionalItems[i].isEmpty()) {
                    this.additionalItems[i] = output;
                    output = null;
                    break;
                } else {
                    if (this.additionalItems[i].getCount() < this.additionalItems[i].getMaxStackSize()) {
                        int size = Math.min(this.additionalItems[i].getMaxStackSize() - this.additionalItems[i].getCount(), output.getCount());
                        if (ItemStack.areItemsEqual(this.additionalItems[i], output) &&
                                ItemStack.areItemStackTagsEqual(this.additionalItems[i], output)) {
                            this.additionalItems[i].grow(size);
                            output.shrink(size);
                            if (output.isEmpty()) {
                                output = null;
                                break;
                            }
                        }
                    }
                }
            }
            if (output != null && !this.world.isRemote) {
                StackUtil.spawnItemDefaultVelocity(
                        this.world,
                        this.pos.getX() + 0.5,
                        this.pos.getY() + 1.0,
                        this.pos.getZ() + 0.5,
                        output
                );
            }
        }
        this.sync();
    }

    @Override
    public double getItemHeight() {
        return 0.75;
    }

    @Override
    public boolean isWool() {
        return false;
    }

    @Override
    public ItemStack takePresented(int count) {
        this.sync();
        return this.mainItem.splitStack(count);
    }

    @Override
    public int getSlots() {
        return 9;
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return slot == 0 ? this.mainItem : this.additionalItems[slot - 1];
    }

    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        if (stack.isEmpty()) return ItemStack.EMPTY;
        if (slot != 0) return stack;

        int space = mainItem.isEmpty() ? getSlotLimit(slot) : mainItem.getMaxStackSize() - mainItem.getCount();
        if (space == 0) return stack;

        int tryInputCount = stack.getCount();
        int inputCount = Math.min(space, tryInputCount);

        if (!simulate) {
            if (mainItem.isEmpty()) {
                ItemStack inputStack = stack.copy();
                inputStack.setCount(inputCount);
                mainItem = inputStack;
            } else {
                mainItem.grow(inputCount);
            }
            sync();
        }

        if (inputCount == stack.getCount()) {
            return ItemStack.EMPTY;
        } else {
            ItemStack outputStack = stack.copy();
            outputStack.shrink(inputCount);
            return outputStack;
        }
    }

    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (amount == 0) return ItemStack.EMPTY;
        ItemStack target = slot == 0 ? this.mainItem : this.additionalItems[slot - 1];
        ItemStack copy = simulate ? target.copy() : target;
        if (!simulate) this.sync();
        return copy.splitStack(amount);
    }

    @Override
    public int getSlotLimit(int slot) {
        return 64;
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
        return slot == 0;
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
    }

    @Nullable
    @Override
    @SuppressWarnings("unchecked")
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) return (T)this;
        return super.getCapability(capability, facing);
    }
}
