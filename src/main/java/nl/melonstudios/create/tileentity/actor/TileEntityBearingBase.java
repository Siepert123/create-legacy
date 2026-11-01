package nl.melonstudios.create.tileentity.actor;

import com.melonstudios.melonlib.misc.AABB;
import com.melonstudios.melonlib.network.TrackedByteBuf;
import io.netty.buffer.ByteBuf;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import nl.melonstudios.create.CreateLegacy;
import nl.melonstudios.create.block.actor.BlockBearingBase;
import nl.melonstudios.create.entity.EntityContraptionBearing;
import nl.melonstudios.create.init.SoundInit;
import nl.melonstudios.create.kinetics.contraption.*;
import nl.melonstudios.create.tileentity.TileEntityKinetic;
import nl.melonstudios.create.util.Utils;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public abstract class TileEntityBearingBase extends TileEntityKinetic implements ITileEntityWithContraption {
    public ContraptionResult.AssemblyFailure lastFailure = null;
    public UUID attachedContraptionUUID = null;
    public EntityContraptionBearing attachedContraptionEntity = null;

    public boolean tryAssemble() {
        if (this.isAssembled()) {
            this.enableDisassembly();
            return true;
        } else if (this.getSpeed() != 0.0F) {
            this.enableAssembly();
            return true;
        }
        return false;
    }

    public boolean isFlipped() {
        return false;
    }
    protected boolean mightAssemble = false;
    protected boolean mightDisassemble = false;
    protected boolean pausedLastTick = false;
    protected boolean isPausedThisTick = false;

    public void enableAssembly() {
        if (this.world.isRemote) return;
        this.mightAssemble = true;
    }
    public void enableDisassembly() {
        if (this.world.isRemote) return;
        this.mightDisassemble = true;
    }

    @Nullable
    protected final EntityContraptionBearing getAttachedContraption() {
        return this.attachedContraptionEntity;
    }

    @Override
    public void onAssembly() {
        super.onAssembly();

        if (this.isAssembled()) this.disassemble();
    }

    public void emergencyDisassemble() {
        this.assemblyChanged = true;
        this.mightDisassemble = this.mightAssemble = false;
        if (this.attachedContraptionEntity != null) {
            this.attachedContraptionEntity.placeBlocks();
            this.world.removeEntity(this.attachedContraptionEntity);
            //this.world.onEntityRemoved(this.attachedContraptionEntity);
            this.world.playSound(null, this.pos, SoundInit.contraption_disassemble, SoundCategory.BLOCKS, 1.0F, 1.0F);
        }
        this.attachedContraptionEntity = null;
        this.attachedContraptionUUID = null;
    }
    public boolean disassemble() {
        if (this.assemblyChanged) return false;
        if (this.world.isRemote) return true;
        //if (this.world.isRemote) return true;
        this.assemblyChanged = true;
        if (this.attachedContraptionEntity != null) {
            this.attachedContraptionEntity.placeBlocks();
            this.attachedContraptionEntity.bearing = null;
            this.attachedContraptionEntity.bearingPos = null;
            this.world.removeEntity(this.attachedContraptionEntity);
            //this.world.onEntityRemoved(this.attachedContraptionEntity);
        }
        this.attachedContraptionUUID = null;
        this.attachedContraptionEntity = null;
        Utils.setBlockKineticTESafe(this.world, this.pos, this.getState().withProperty(BlockBearingBase.ASSEMBLED, false), 3);
        this.angle = 0.0F;
        this.sync();
        this.world.playSound(null, this.pos, SoundInit.contraption_disassemble, SoundCategory.BLOCKS, 1.0F, 1.0F);
        return true;
    }
    protected boolean assemble() {
        if (this.assemblyChanged) return false;
        if (this.world.isRemote) return true;
        this.assemblyChanged = true;
        EntityContraptionBearing bearing = new EntityContraptionBearing(this);
        ContraptionResult result = this.assembleContraption(bearing);
        if (result.hasFailed()) {
            bearing.setDead();
            this.lastFailure = result.getError();
            this.sync();
            return true;
        }
        this.lastFailure = null;
        bearing.contraption = result.getContraption();
        this.world.spawnEntity(bearing);
        this.attachedContraptionEntity = bearing;
        this.attachedContraptionUUID = bearing.getPersistentID();
        Utils.setBlockKineticTESafe(this.world, this.pos, this.getState().withProperty(BlockBearingBase.ASSEMBLED, true), 3);
        this.angle = 0.0F;
        this.sync();
        this.world.playSound(null, this.pos, SoundInit.contraption_assemble, SoundCategory.BLOCKS, 1.0F, 1.0F);
        this.world.playSound(null, this.pos, SoundInit.contraption_assemble_compound, SoundCategory.BLOCKS, 0.25F, 1.1F);
        return true;
    }

    @Override
    public void destroy() {
        super.destroy();
        this.assemblyChanged = false;
        if (this.isAssembled()) this.emergencyDisassemble();
    }

    protected ContraptionResult assembleContraption(IContraptionHolder holder) {
        return Contraption.assemble(holder, this.pos.offset(this.getFacing()), this.pos, this.getContraptionChecker());
    }

    protected ContraptionAssemblyChecker getContraptionChecker() {
        return ContraptionAssembly.NO_CHECKER;
    }

    public EnumFacing getFacing() {
        return this.getState().getValue(BlockBearingBase.FACING);
    }
    public boolean isAssembled() {
        return this.getState().getValue(BlockBearingBase.ASSEMBLED);
    }

    protected boolean useGeneratedSpeedForContraption() {
        return false;
    }

    public boolean assemblyChanged = false;
    public float angleOld = 0.0F;
    public float angle = 0.0F;
    @Override
    public void tick() {
        this.assemblyChanged = false;
        this.angleOld = this.angle;
        super.tick();

        if (this.mightAssemble && (this.getSpeed() != 0.0F || this.getBearingType() == BearingType.WINDMILL) && !this.isAssembled()) {
            this.assemble();
        }
        if (this.mightDisassemble && this.isAssembled()) {
            this.disassemble();
        }

        this.mightAssemble = this.mightDisassemble = false;

        if (this.attachedContraptionEntity != null && (this.attachedContraptionEntity.isDead || this.attachedContraptionEntity.world != this.world)) {
            CreateLegacy.logger.debug("gng {}", this.world.isRemote);
            this.attachedContraptionEntity = null;
        }
        if (this.attachedContraptionUUID != null && this.attachedContraptionEntity == null) {
            List<EntityContraptionBearing> list = this.world.getEntitiesWithinAABB(EntityContraptionBearing.class, AABB.wrap(this.pos, 3),
                    (e) -> e.getPersistentID().equals(this.attachedContraptionUUID));
            if (!list.isEmpty()) {
                CreateLegacy.logger.debug("Attached contraption! {}", this.world.isRemote);
                this.attachedContraptionEntity = list.get(0);
            } else CreateLegacy.logger.debug("hello? :( {}", this.world.isRemote);
        }
        if (!this.world.isRemote) {
            if (this.attachedContraptionEntity != null && this.attachedContraptionEntity.isDead) {
                this.attachedContraptionUUID = null;
                this.attachedContraptionEntity = null;
                this.sync();
            }
        }

        if (this.isAssembled() && !this.overstressed) {
            if (!this.isPausedThisTick) {
                if (this.useGeneratedSpeedForContraption()) {
                    this.angle += this.getGeneratedSpeed() * 0.3F;
                } else {
                    this.angle += this.getSpeed() * 0.3F;
                }

                if (this.angleOld > 360.0F && this.angle > 360.0F) {
                    this.angleOld %= 360.0F;
                    this.angle %= 360.0F;
                } else if (this.angleOld < -360.0F && this.angle < -360.0F) {
                    this.angleOld %= 360.0F;
                    this.angle %= 360.0F;
                }
            }
            EntityContraptionBearing entity = this.getAttachedContraption();
            if (entity != null) {
                entity.bearing = this;
                entity.bearingPos = this.pos;
                entity.cachedAngleOld = this.angleOld;
                entity.cachedAngle = this.angle;
            } else CreateLegacy.logger.error("vro {}", this.world.isRemote);
            if (!this.world.isRemote && (this.world.getTotalWorldTime() & 63) == 0) {
                //Synchronize every so often to make sure it is equal at all times
                this.sync();
            }
            this.markDirty();
        }
        if (this.pausedLastTick != this.isPausedThisTick) this.sync();
        this.pausedLastTick = this.isPausedThisTick;
        this.isPausedThisTick = false;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        NBTTagCompound nbt = super.writeToNBT(compound);

        nbt.setFloat("angle", this.angle);
        if (this.attachedContraptionUUID != null) nbt.setUniqueId("AttachedContraptionUUID", this.attachedContraptionUUID);

        return nbt;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);

        this.angle = nbt.getFloat("angle");
        if (nbt.hasKey("AttachedContraptionUUIDLeast")) {
            this.attachedContraptionUUID = nbt.getUniqueId("AttachedContraptionUUID");
        } else {
            this.attachedContraptionUUID = null;
            this.attachedContraptionEntity = null;
        }
    }

    @Override
    public void writePacket(TrackedByteBuf buf) throws IOException {
        super.writePacket(buf);
        buf.writeFloat(this.angle);
        if (this.lastFailure != null) {
            String err = this.lastFailure.error;
            buf.writeInt(err.length());
            buf.internal().writeCharSequence(err, StandardCharsets.UTF_8);
            buf.append(err.length());
        } else {
            buf.writeInt(0);
        }
        if (this.attachedContraptionUUID != null) {
            buf.writeBoolean(true);
            buf.writeLong(this.attachedContraptionUUID.getMostSignificantBits());
            buf.writeLong(this.attachedContraptionUUID.getLeastSignificantBits());
        } else buf.writeBoolean(false);
    }

    @Override
    public void readPacket(ByteBuf buf) throws IOException {
        super.readPacket(buf);
        this.angle = buf.readFloat();
        int len = buf.readInt();
        if (len > 0) {
            this.lastFailure = new ContraptionResult.AssemblyFailure(buf.readCharSequence(len, StandardCharsets.UTF_8).toString());
        } else this.lastFailure = null;
        if (buf.readBoolean()) {
            this.attachedContraptionUUID = new UUID(buf.readLong(), buf.readLong());
        } else {
            this.attachedContraptionUUID = null;
        }
        this.attachedContraptionEntity = null;
    }

    @Override
    public void collectCollisions(AxisAlignedBB aabb, List<AxisAlignedBB> collisions) {

    }

    public void pauseContraption() {
        this.isPausedThisTick = true;
    }

    public BearingType getBearingType() {
        return BearingType.BEARING;
    }
    public enum BearingType {
        BEARING, WINDMILL,
    }
}
