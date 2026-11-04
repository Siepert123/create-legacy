package nl.melonstudios.create.tileentity.actor;

import com.melonstudios.melonlib.misc.StackUtil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import nl.melonstudios.create.block.actor.BlockBeltStraight;
import nl.melonstudios.create.block.state.EnumBeltPart;
import nl.melonstudios.create.tileentity.TileEntityKinetic;
import nl.melonstudios.create.tileentity.marker.IDepot;
import nl.melonstudios.create.tileentity.marker.IHaltBeltContents;

public class TileEntityBeltStraight extends TileEntityBeltBase implements IDepot {
    public TileEntityBeltStraight() {
        super();
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    public float propagateRotationTo(TileEntityKinetic target, IBlockState stateFrom, IBlockState stateTo, BlockPos diff, boolean connectedViaAxes, boolean connectedViaCogs) {
        if (target instanceof TileEntityBeltStraight && !connectedViaAxes) {
            if (stateFrom.getValue(BlockBeltStraight.AXIS) != stateTo.getValue(BlockBeltStraight.AXIS)) return 0.0F;
            if (stateFrom.getValue(BlockBeltStraight.VERTICAL) != stateTo.getValue(BlockBeltStraight.VERTICAL)) return 0.0F;
            EnumFacing.Axis axis = stateFrom.getValue(BlockBeltStraight.AXIS);
            boolean vertical = stateFrom.getValue(BlockBeltStraight.VERTICAL);
            EnumBeltPart part = stateFrom.getValue(BlockBeltStraight.PART);
            if (diff.getX() != 0) {
                if (axis != EnumFacing.Axis.X || vertical) return 0.0F;
                if (diff.getX() > 0 && part == EnumBeltPart.END) return 0.0F;
                if (diff.getX() < 0 && part == EnumBeltPart.START) return 0.0F;
                return 1.0F;
            }
            if (diff.getZ() != 0) {
                if (axis != EnumFacing.Axis.Z || vertical) return 0.0F;
                if (diff.getZ() > 0 && part == EnumBeltPart.END) return 0.0F;
                if (diff.getZ() < 0 && part == EnumBeltPart.START) return 0.0F;
                return 1.0F;
            }
            if (diff.getY() != 0) {
                if (!vertical) return 0.0F;
                if (diff.getZ() > 0 && part == EnumBeltPart.END) return 0.0F;
                if (diff.getZ() < 0 && part == EnumBeltPart.START) return 0.0F;
                return 1.0F;
            }
        }
        return 0.0F;
    }

    @Override
    protected boolean flipped() {
        return this.getState().getValue(BlockBeltStraight.AXIS) == EnumFacing.Axis.X;
    }

    protected final BlockPos.MutableBlockPos actorPos = new BlockPos.MutableBlockPos();
    protected TileEntity actor = null;
    @Override
    protected boolean allowItemToPass(ItemStack stack) {
        if (this.actor == null || this.actor.isInvalid() || !this.actorPos.equals(this.actor.getPos())) {
            this.actorPos.setPos(this.pos.getX(), this.pos.getY() + 2, this.pos.getZ());
            this.actor = this.world.getTileEntity(this.actorPos);
            if (!this.world.isBlockLoaded(this.actorPos, false)) return false;
        }
        if (this.actor instanceof IHaltBeltContents) {
            IHaltBeltContents halt = (IHaltBeltContents) this.actor;
            return !halt.shouldHaltItem(stack);
        }
        return true;
    }

    //TODO: Redo the inventory so this will work

    @Override
    public ItemStack getPresentedItem() {
        if (this.getSpeed() == 0.0F) return ItemStack.EMPTY;
        if (this.getFlag()) {
            return this.leftPos == 1.0 ? this.left : ItemStack.EMPTY;
        } else {
            return this.rightPos == 0.0 ? this.right : ItemStack.EMPTY;
        }
    }

    @Override
    public void setPresentedItem(ItemStack stack) {
        if (this.getSpeed() == 0.0F) return;
        if (this.getFlag()) {
            this.left = stack;
        } else {
            this.right = stack;
        }
        this.sync();
    }

    @Override
    public void decreasePresentedAndAddOutput(ItemStack output) {
        if (this.getSpeed() == 0.0F) throw new UnsupportedOperationException("How did this even happen?");
        if (this.getFlag()) {
            boolean flag = true;
            this.left.shrink(1);
            if (this.left.isEmpty()) this.left = ItemStack.EMPTY;
            if (this.right.isEmpty()) this.right = output.copy();
            else if (ItemStack.areItemsEqual(this.right, output) && ItemStack.areItemStackTagsEqual(this.right, output)) {
                this.right.grow(output.getCount());
                flag = false;
            } else StackUtil.spawnItemWithVelocity(this.world,
                    this.pos.getX() + 0.5, this.pos.getY() + 1.0, this.pos.getZ() + 0.5,
                    output, this.world.rand.nextGaussian(), 0.4, this.world.rand.nextGaussian());
            if (flag) this.rightPos = this.rightPosOld = 0.0;
        } else {
            boolean flag = true;
            this.right.shrink(1);
            if (this.right.isEmpty()) this.right = ItemStack.EMPTY;
            if (this.left.isEmpty()) this.left = output.copy();
            else if (ItemStack.areItemsEqual(this.left, output) && ItemStack.areItemStackTagsEqual(this.left, output)) {
                this.left.grow(output.getCount());
                flag = false;
            } else StackUtil.spawnItemWithVelocity(this.world,
                    this.pos.getX() + 0.5, this.pos.getY() + 1.0, this.pos.getZ() + 0.5,
                    output, this.world.rand.nextGaussian(), 0.4, this.world.rand.nextGaussian());
            if (flag) this.leftPos = this.leftPosOld = 1.0;
        }
        this.sync();
    }

    @Override
    public double getItemHeight() {
        return 0.75;
    }

    @Override
    public boolean isWool() {
        return true;
    }

    @Override
    public ItemStack takePresented(int count) {
        if (this.getSpeed() == 0.0F) return ItemStack.EMPTY;
        this.sync();
        return this.getFlag() ? this.left.splitStack(count) : this.right.splitStack(count);
    }

    @Override
    public boolean allowTaking() {
        return false;
    }
}
