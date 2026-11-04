package nl.melonstudios.create.tileentity.funnel;

import com.melonstudios.melonlib.network.TrackedByteBuf;
import com.melonstudios.melonlib.tileentity.ISyncedTE;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;
import nl.melonstudios.create.init.ItemInit;
import nl.melonstudios.create.tileentity.marker.ITileEntityWithSubInteractions;
import nl.melonstudios.create.util.SubInteractionBox;
import nl.melonstudios.create.util.filter.IItemFilter;
import nl.melonstudios.create.util.filter.ItemFilterExact;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;

public class TileEntityFunnelWallAdvanced extends TileEntityFunnelWall implements ISyncedTE, ITileEntityWithSubInteractions {
    private Collection<SubInteractionBox> subInteractionBoxes = null;

    public IItemFilter filter = null;
    public int extractionAmount = 64;
    public boolean extractionAmountExact = false;

    public TileEntityFunnelWallAdvanced() {
        super();
    }

    @Override
    public void updateContainingBlockInfo() {
        super.updateContainingBlockInfo();

        this.subInteractionBoxes = null;
    }

    @Nullable
    @Override
    public IItemFilter getFilter() {
        return this.filter;
    }
    @Override
    protected int getExtractionAmount() {
        return this.extractionAmount;
    }
    @Override
    protected boolean isExtractionAmountExact() {
        return this.extractionAmountExact;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);

        if (this.filter != null) {
            nbt.setTag("Filter", this.filter.serialize(new NBTTagCompound()));
        }
        nbt.setInteger("extraction", this.extractionAmount);
        nbt.setBoolean("extractionExact", this.extractionAmountExact);

        return nbt;
    }
    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);

        if (nbt.hasKey("Filter", 10)) {
            this.filter = IItemFilter.deserialize(nbt.getCompoundTag("Filter"));
        } else this.filter = null;
        this.extractionAmount = Math.min(Math.max(nbt.getInteger("extraction"), 1), 64);
        this.extractionAmountExact = nbt.getBoolean("extractionExact");
    }

    @Override
    public void writePacket(TrackedByteBuf buf) throws IOException {
        if (this.filter != null) {
            buf.writeBoolean(true);
            this.filter.serialize(buf);
        } else buf.writeBoolean(false);
        buf.writeByte(this.extractionAmount);
        buf.writeBoolean(this.extractionAmountExact);
    }
    @Override
    public void readPacket(ByteBuf buf) throws IOException {
        if (buf.readBoolean()) {
            this.filter = IItemFilter.deserialize(buf);
        } else this.filter = null;
        this.extractionAmount = buf.readUnsignedByte();
        this.extractionAmountExact = buf.readBoolean();
    }

    @Override
    public ItemStack insertIntoFunnel(ItemStack stack, boolean limit) {
        if (stack.isEmpty()) return ItemStack.EMPTY;
        if (this.extractionAmountExact && limit && this.extractionAmount != stack.getCount()) return stack;
        if (this.filter != null && !this.filter.matches(stack)) return stack;
        IItemHandler inv = this.getInventory(this.getFacing(this.getBlockMetadata()).getOpposite());
        if (inv != null && inv.getSlots() > 0) {
            for (int i = 0; i < inv.getSlots() && !stack.isEmpty(); i++) {
                stack = inv.insertItem(i, stack, false);
            }
        }
        return stack.isEmpty() ? ItemStack.EMPTY : stack;
    }

    private void createSubInteractions() {
        this.subInteractionBoxes = Collections.singleton(
                SubInteractionBox.Helper.forFunnel(this.getFacing(this.getBlockMetadata()),
                        0.25F, false, new FunnelInteraction(this)));
    }
    @Override
    public Collection<SubInteractionBox> getSubInteractionBoxes() {
        if (this.subInteractionBoxes == null) this.createSubInteractions();
        return this.subInteractionBoxes;
    }

    private static class FunnelInteraction implements SubInteractionBox.ScrollInteraction {
        private final TileEntityFunnelWallAdvanced te;
        public FunnelInteraction(TileEntityFunnelWallAdvanced te) {
            this.te = te;
        }

        @Override
        public boolean interact(@Nullable EntityPlayer player, boolean sneaking, ItemStack held) {
            if (sneaking) return false;
            if (held.isEmpty()) {
                this.te.world.playSound(null, this.te.pos, SoundEvents.ENTITY_ITEMFRAME_REMOVE_ITEM,
                        SoundCategory.BLOCKS, 1.0F, 1.0F);
                this.te.filter = null;
                this.te.sync();
            } else {
                if (held.getItem() == ItemInit.WRENCH) {
                    this.te.world.playSound(null, this.te.pos, SoundEvents.ENTITY_ITEMFRAME_ROTATE_ITEM,
                            SoundCategory.BLOCKS, 1.0F, 1.0F);
                    this.te.extractionAmountExact = !this.te.extractionAmountExact;
                    this.te.sync();
                } else {
                    this.te.world.playSound(null, this.te.pos, SoundEvents.ENTITY_ITEMFRAME_ADD_ITEM,
                            SoundCategory.BLOCKS, 1.0F, 1.0F);
                    this.te.filter = new ItemFilterExact(held);
                    this.te.sync();
                }
            }
            return true;
        }

        @Override
        public boolean scroll(EntityPlayer player, boolean sneaking, ItemStack held, int direction) {
            if (held.isEmpty() || held.getItem() != ItemInit.WRENCH) return false;
            direction = MathHelper.floor(Math.signum(direction));
            int old = this.te.extractionAmount;
            if (sneaking) {
                int temp = (this.te.extractionAmount >> 4) << 4;
                this.te.extractionAmount = Math.max(Math.min(temp + (direction << 3), 64), 1);
            } else {
                this.te.extractionAmount = Math.max(Math.min(this.te.extractionAmount + direction, 64), 1);
            }
            if (this.te.extractionAmount != old) this.te.sync();
            return true;
        }
    }
}
