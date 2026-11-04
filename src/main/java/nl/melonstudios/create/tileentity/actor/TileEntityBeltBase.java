package nl.melonstudios.create.tileentity.actor;

import com.melonstudios.melonlib.misc.StackUtil;
import com.melonstudios.melonlib.network.TrackedByteBuf;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import nl.melonstudios.create.block.actor.BlockBeltBase;
import nl.melonstudios.create.block.actor.BlockBeltStraight;
import nl.melonstudios.create.block.state.EnumBeltPart;
import nl.melonstudios.create.block.state.EnumFunnelState;
import nl.melonstudios.create.init.BlockInit;
import nl.melonstudios.create.tileentity.TileEntityKinetic;
import nl.melonstudios.create.tileentity.funnel.TileEntityFunnelBase;
import nl.melonstudios.create.tileentity.funnel.TileEntityFunnelWall;
import nl.melonstudios.create.tileentity.marker.ITopOpenInventory;

import javax.annotation.Nullable;
import java.io.IOException;

public abstract class TileEntityBeltBase extends TileEntityKinetic implements ITopOpenInventory {
    public EnumDyeColor color = null;

    public void applyColor(@Nullable EnumDyeColor color) {
        this.applyColorInternal(color, this.pos);
    }

    private void applyColorInternal(@Nullable EnumDyeColor color, BlockPos src) {
        this.color = color;
        this.sync();
        EnumBeltPart part = this.getState().getValue(BlockBeltBase.PART);
        if (this.block() instanceof BlockBeltStraight && this.getState().getValue(BlockBeltStraight.VERTICAL)) {
            if (part != EnumBeltPart.END) {
                BlockPos p = this.pos.up();
                if (!src.equals(p)) {
                    TileEntity te = this.world.getTileEntity(p);
                    if (te instanceof TileEntityBeltBase) {
                        ((TileEntityBeltBase) te).applyColorInternal(color, this.pos);
                    }
                }
            }
            if (part != EnumBeltPart.START) {
                BlockPos n = this.pos.down();
                if (!src.equals(n)) {
                    TileEntity te = this.world.getTileEntity(n);
                    if (te instanceof TileEntityBeltBase) {
                        ((TileEntityBeltBase) te).applyColorInternal(color, this.pos);
                    }
                }
            }
        } else {
            EnumFacing.Axis axis = this.block().getTransportAxis(this.getState());
            if (part != EnumBeltPart.END) {
                BlockPos p = this.getOffsetPosition(this.pos, EnumFacing.getFacingFromAxis(EnumFacing.AxisDirection.POSITIVE, axis));
                if (!src.equals(p)) {
                    TileEntity te = this.world.getTileEntity(p);
                    if (te instanceof TileEntityBeltBase) {
                        ((TileEntityBeltBase) te).applyColorInternal(color, this.pos);
                    }
                }
            }
            if (part != EnumBeltPart.START) {
                BlockPos n = this.getOffsetPosition(this.pos, EnumFacing.getFacingFromAxis(EnumFacing.AxisDirection.NEGATIVE, axis));
                if (!src.equals(n)) {
                    TileEntity te = this.world.getTileEntity(n);
                    if (te instanceof TileEntityBeltBase) {
                        ((TileEntityBeltBase) te).applyColorInternal(color, this.pos);
                    }
                }
            }
        }
    }

    @Deprecated //Only one is enough
    private final InventoryManager[] inventories = new InventoryManager[7];

    public ItemStack left = ItemStack.EMPTY;
    public ItemStack right = ItemStack.EMPTY;

    public double leftPos = 0.0;
    public double rightPos = 0.0;
    public double leftPosOld = 0.0;
    public double rightPosOld = 0.0;

    public TileEntityBeltBase() {
        for (EnumFacing side : EnumFacing.VALUES) {
            this.inventories[side.getIndex()] = new InventoryManager(side);
        }
        this.inventories[6] = new InventoryManager(null);
    }

    protected BlockBeltBase block() {
        return (BlockBeltBase) this.getBlockType();
    }

    public long lastUpdateTick = -1;
    @Override
    public void tick() {
        this.lastUpdateTick = this.world.getTotalWorldTime();

        super.tick();

        this.leftPosOld = this.leftPos;
        this.rightPosOld = this.rightPos;
        double speed = this.getSpeed() * 0.0625 * 0.05;
        if (speed != 0.0) {
            this.markDirty();
            EnumFacing.Axis transportAxis = this.block().getTransportAxis(this.getState());
            if (transportAxis == EnumFacing.Axis.X) speed *= -1;
            EnumFacing positive = EnumFacing.getFacingFromAxis(EnumFacing.AxisDirection.POSITIVE, transportAxis);
            EnumFacing negative = EnumFacing.getFacingFromAxis(EnumFacing.AxisDirection.NEGATIVE, transportAxis);

            if (speed > 0.0) { //update positive first
                update:
                if (!this.right.isEmpty()) {
                    if (this.rightPos < 1.0) this.rightPos += speed;
                    if (this.rightPos >= 1.0) {
                        {
                            TileEntity funnel = this.world.getTileEntity(this.pos.up());
                            if (funnel instanceof TileEntityFunnelBase) {
                                TileEntityFunnelBase te = (TileEntityFunnelBase) funnel;
                                if (te.allowsInsertionFromBelt()) {
                                    this.right = te.insertIntoFunnel(this.right, false);
                                    this.sync();
                                    if (this.right.isEmpty()) {
                                        this.right = ItemStack.EMPTY;
                                        break update;
                                    }
                                }
                            }
                        }

                        BlockPos pos = this.getOffsetPosition(this.pos, positive);
                        TileEntity te = this.world.getTileEntity(pos);
                        if (te instanceof TileEntityBeltBase && this.getState().getValue(BlockBeltBase.PART) != EnumBeltPart.END) {
                            TileEntityBeltBase belt = (TileEntityBeltBase) te;
                            if (belt.left.isEmpty()) {
                                belt.left = this.right;
                                this.right = ItemStack.EMPTY;
                                belt.leftPosOld = this.rightPosOld - 1.0;
                                belt.leftPos = this.rightPos - 1.0;
                                if (belt.lastUpdateTick != this.lastUpdateTick) {
                                    belt.leftPosOld -= speed;
                                    belt.leftPos -= speed;
                                }
                            } else {
                                this.rightPos = 1.0;
                            }
                        } else if (te instanceof ITopOpenInventory) {
                            ITopOpenInventory inv = (ITopOpenInventory) te;
                            this.right = inv.tryInsertItem(this.right, negative);
                            this.rightPos = 1.0;
                            if (this.right.isEmpty()) {
                                this.right = ItemStack.EMPTY;
                                this.sync();
                            }
                        } else {
                            this.rightPos = 1.0;
                            IBlockState state = this.world.getBlockState(pos);
                            if (state.getMaterial().isReplaceable()) {
                                if (!this.world.isRemote) {
                                    double dx = this.pos.getX() + 0.5 + positive.getFrontOffsetX() * 0.6;
                                    double dy = this.pos.getY() + 0.85;
                                    double dz = this.pos.getZ() + 0.5 + positive.getFrontOffsetZ() * 0.6;
                                    StackUtil.spawnItemWithVelocity(this.world, dx, dy, dz, this.right.copy(),
                                            positive.getFrontOffsetX() * Math.abs(speed),
                                            0.2,
                                            positive.getFrontOffsetZ() * Math.abs(speed)
                                    );
                                }
                                this.right = ItemStack.EMPTY;
                                this.sync();
                            }
                        }
                    }
                }
                if (!this.left.isEmpty()) {
                    if (this.leftPos < 1.0) this.leftPos += speed;
                    if (this.leftPos >= 1.0) {
                        if (this.right.isEmpty() && this.allowItemToPass(this.left)) {
                            this.right = this.left;
                            this.rightPosOld = this.leftPosOld - 1.0;
                            this.rightPos = this.leftPos - 1.0;
                            this.left = ItemStack.EMPTY;
                        } else {
                            this.leftPos = 1.0;
                        }
                    }
                }
            } else { //update negative first
                update:
                if (!this.left.isEmpty()) {
                    if (this.leftPos > 0.0) this.leftPos += speed;
                    if (this.leftPos <= 0.0) {
                        {
                            TileEntity funnel = this.world.getTileEntity(this.pos.up());
                            if (funnel instanceof TileEntityFunnelBase) {
                                TileEntityFunnelBase te = (TileEntityFunnelBase) funnel;
                                if (te.allowsInsertionFromBelt()) {
                                    this.left = te.insertIntoFunnel(this.left, false);
                                    this.sync();
                                    if (this.left.isEmpty()) {
                                        this.left = ItemStack.EMPTY;
                                        break update;
                                    }
                                }
                            }
                        }

                        BlockPos pos = this.getOffsetPosition(this.pos, negative);
                        TileEntity te = this.world.getTileEntity(pos);
                        if (te instanceof TileEntityBeltBase && this.getState().getValue(BlockBeltBase.PART) != EnumBeltPart.START) {
                            TileEntityBeltBase belt = (TileEntityBeltBase) te;
                            if (belt.right.isEmpty()) {
                                belt.right = this.left;
                                this.left = ItemStack.EMPTY;
                                belt.rightPosOld = this.leftPosOld + 1.0;
                                belt.rightPos = this.leftPos + 1.0;
                                if (belt.lastUpdateTick != this.lastUpdateTick) {
                                    belt.rightPosOld -= speed;
                                    belt.rightPos -= speed;
                                }
                            } else {
                                this.leftPos = 0.0;
                            }
                        } else if (te instanceof ITopOpenInventory) {
                            ITopOpenInventory inv = (ITopOpenInventory) te;
                            this.left = inv.tryInsertItem(this.left, positive);
                            this.leftPos = 0.0;
                            if (this.left.isEmpty()) {
                                this.left = ItemStack.EMPTY;
                                this.sync();
                            }
                        } else {
                            this.leftPos = 0.0;
                            IBlockState state = this.world.getBlockState(pos);
                            if (state.getMaterial().isReplaceable()) {
                                if (!this.world.isRemote) {
                                    double dx = this.pos.getX() + 0.5 + negative.getFrontOffsetX() * 0.6;
                                    double dy = this.pos.getY() + 0.85;
                                    double dz = this.pos.getZ() + 0.5 + negative.getFrontOffsetZ() * 0.6;
                                    StackUtil.spawnItemWithVelocity(this.world, dx, dy, dz, this.left.copy(),
                                            negative.getFrontOffsetX() * Math.abs(speed),
                                            0.2,
                                            negative.getFrontOffsetZ() * Math.abs(speed)
                                    );
                                }
                                this.left = ItemStack.EMPTY;
                                this.sync();
                            }
                        }
                    }
                }
                if (!this.right.isEmpty()) {
                    if (this.rightPos > 0.0) this.rightPos += speed;
                    if (this.rightPos <= 0.0) {
                        if (this.left.isEmpty() && this.allowItemToPass(this.right)) {
                            this.left = this.right;
                            this.leftPosOld = this.rightPosOld + 1.0;
                            this.leftPos = this.rightPos + 1.0;
                            this.right = ItemStack.EMPTY;
                        } else {
                            this.rightPos = 0.0;
                        }
                    }
                }
            }
        }
    }

    @Override
    public void destroy() {
        StackUtil.dropItemsAt(this.world, this.pos, this.left, this.right);
        if (this.getState().getValue(BlockBeltBase.PART) != EnumBeltPart.MIDDLE) {
            StackUtil.dropItemsAt(this.world, this.pos, new ItemStack(BlockInit.SHAFT)); //TODO: simply have it place the original shaft
        }
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) return this.block().isFunctional(this.getState());
        return super.hasCapability(capability, facing);
    }

    @Nullable
    @Override
    @SuppressWarnings("unchecked")
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && this.block().isFunctional(this.getState())) {
            return (T)(facing == null ? this.inventories[6] : this.inventories[facing.getIndex()]);
        }
        return super.getCapability(capability, facing);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);

        if (!this.left.isEmpty()) {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setDouble("pos", this.leftPos);
            this.left.writeToNBT(tag);
            nbt.setTag("LeftItem", tag);
        }
        if (!this.right.isEmpty()) {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setDouble("pos", this.rightPos);
            this.right.writeToNBT(tag);
            nbt.setTag("RightItem", tag);
        }

        if (this.color != null) {
            nbt.setInteger("color", this.color.getMetadata());
        }

        return nbt;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);

        if (nbt.hasKey("LeftItem", 10)) {
            NBTTagCompound tag = nbt.getCompoundTag("LeftItem");
            this.leftPosOld = this.leftPos = tag.getDouble("pos");
            this.left = new ItemStack(tag);
        } else this.left = ItemStack.EMPTY;
        if (nbt.hasKey("RightItem", 10)) {
            NBTTagCompound tag = nbt.getCompoundTag("RightItem");
            this.rightPosOld = this.rightPos = tag.getDouble("pos");
            this.right = new ItemStack(tag);
        } else this.right = ItemStack.EMPTY;

        if (nbt.hasKey("color")) this.color = EnumDyeColor.byMetadata(nbt.getInteger("color"));
        else this.color = null;
    }

    @Override
    public void writePacket(TrackedByteBuf buf) throws IOException {
        super.writePacket(buf);

        if (!this.left.isEmpty()) {
            buf.writeBoolean(true);
            StackUtil.writeItemStack(this.left, buf, true, true);
            buf.writeDouble(this.leftPos);
        } else {
            buf.writeBoolean(false);
        }
        if (!this.right.isEmpty()) {
            buf.writeBoolean(true);
            StackUtil.writeItemStack(this.right, buf, true, true);
            buf.writeDouble(this.rightPos);
        } else {
            buf.writeBoolean(false);
        }

        buf.writeByte(this.color != null ? this.color.getMetadata() : -1);
    }

    @Override
    public void readPacket(ByteBuf buf) throws IOException {
        super.readPacket(buf);

        if (buf.readBoolean()) {
            this.left = StackUtil.readItemStack(buf, true, true);
            this.leftPos = buf.readDouble();
        } else this.left = ItemStack.EMPTY;
        if (buf.readBoolean()) {
            this.right = StackUtil.readItemStack(buf, true, true);
            this.rightPos = buf.readDouble();
        } else this.right = ItemStack.EMPTY;

        byte color = buf.readByte();
        this.color = color == -1 ? null : EnumDyeColor.byMetadata(color);
    }

    protected boolean flipped() {
        return false;
    }
    public final boolean getFlag() {
        return this.getSpeed() > 0.0F != this.flipped();
    }

    @Override
    public ItemStack tryInsertItem(ItemStack stack) {
        if (this.block().isFunctional(this.getState()) && this.getSpeed() != 0.0F) {
            if (this.getFlag()) {
                if (this.left.isEmpty()) {
                    this.left = stack.copy();
                    this.leftPosOld = this.leftPos = 0.5;
                    this.sync();
                    return ItemStack.EMPTY;
                }
            } else {
                if (this.right.isEmpty()) {
                    this.right = stack.copy();
                    this.rightPosOld = this.rightPos = 0.5;
                    this.sync();
                    return ItemStack.EMPTY;
                }
            }
        }
        return stack;
    }

    @Override
    public boolean isInsertionSlotEmpty(ItemStack stack) {
        if (this.block().isFunctional(this.getState()) && this.getSpeed() != 0.0F) {
            if (this.getFlag()) {
                return this.left.isEmpty();
            } else return this.right.isEmpty();
        }
        return false;
    }

    @Override
    public ItemStack tryInsertItem(ItemStack stack, @Nullable EnumFacing side) {
        if (this.getSpeed() != 0.0F && side != null && this.block().isFunctional(this.getState())) {
            if (EnumFacing.getFacingFromAxis(this.getFlag() ? EnumFacing.AxisDirection.NEGATIVE : EnumFacing.AxisDirection.POSITIVE,
                    this.block().getTransportAxis(this.getState())) == side) {
                if (this.getFlag()) {
                    if (this.left.isEmpty()) {
                        this.left = stack.copy();
                        this.leftPosOld = this.leftPos = 0.0;
                        this.sync();
                        return ItemStack.EMPTY;
                    }
                } else {
                    if (this.right.isEmpty()) {
                        this.right = stack.copy();
                        this.rightPosOld = this.rightPos = 1.0;
                        this.sync();
                        return ItemStack.EMPTY;
                    }
                }
            }
        }
        return this.tryInsertItem(stack);
    }

    protected BlockPos getOffsetPosition(BlockPos pos, EnumFacing side) {
        return pos.offset(side);
    }

    public double getLeftPos(double delta) {
        return MathHelper.clampedLerp(this.leftPosOld, this.leftPos, delta) * 0.5 - 0.5;
    }
    public double getRightPos(double delta) {
        return MathHelper.clampedLerp(this.rightPosOld, this.rightPos, delta) * 0.5;
    }

    protected boolean allowItemToPass(ItemStack stack) {
        return true;
    }

    //TODO: rewrite this
    private class InventoryManager implements IItemHandler {
        private final EnumFacing side;
        private InventoryManager(@Nullable EnumFacing side) {
            this.side = side;
        }

        private TileEntityBeltBase self() {
            return TileEntityBeltBase.this;
        }
        private boolean flag() {
            return self().getFlag();
        }

        @Override
        public int getSlots() {
            return 1;
        }

        @Override
        public ItemStack getStackInSlot(int slot) {
            if (self().getSpeed() == 0.0F) return ItemStack.EMPTY;
            return this.flag() ? self().left : self().right;
        }

        @Override
        public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
            if (self().getSpeed() == 0.0F) return stack;
            if (stack.isEmpty()) return ItemStack.EMPTY;
            if (this.flag()) {
                if (self().left.isEmpty()) {
                    if (simulate) return ItemStack.EMPTY;
                    self().left = stack.copy();
                    self().sync();
                    return ItemStack.EMPTY;
                }
            } else {
                if (self().right.isEmpty()) {
                    if (simulate) return ItemStack.EMPTY;
                    self().right = stack.copy();
                    self().sync();
                    return ItemStack.EMPTY;
                }
            }
            return stack;
        }

        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            if (self().getSpeed() == 0.0F || amount <= 0) return ItemStack.EMPTY;
            if (this.flag()) {
                if (self().right.isEmpty()) return ItemStack.EMPTY;
                ItemStack copy = self().right.copy();
                ItemStack ret = copy.splitStack(amount);
                if (simulate) return ret;
                self().right = copy;
                self().sync();
                return ret;
            } else {
                if (self().left.isEmpty()) return ItemStack.EMPTY;
                ItemStack copy = self().left.copy();
                ItemStack ret = copy.splitStack(amount);
                if (simulate) return ret;
                self().left = copy;
                self().sync();
                return ret;
            }
        }

        @Override
        public int getSlotLimit(int slot) {
            return 64;
        }
    }
}
