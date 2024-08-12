package com.siepert.createlegacy.tileentity;

import com.siepert.createapi.IKineticActor;
import com.siepert.createapi.IKineticTE;
import com.siepert.createapi.KineticBlockInstance;
import com.siepert.createapi.NetworkContext;
import com.siepert.createlegacy.CreateLegacyConfigHolder;
import com.siepert.createlegacy.CreateLegacyModData;
import com.siepert.createlegacy.util.handlers.ModSoundHandler;
import com.siepert.createlegacy.util.handlers.NeverTouchThisCodeAgain;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;

import static com.siepert.createlegacy.blocks.kinetic.BlockCogwheel.*;

public class TileEntityCogwheel extends TileEntity implements IKineticTE {
    @Override
    public double getStressImpact() {
        return CreateLegacyConfigHolder.kineticConfig.cogwheelStressImpact;
    }

    @Override
    public double getStressCapacity() {
        return 0;
    }

    @Override
    public int getProducedSpeed() {
        return 0;
    }

    @Override
    public void kineticTick(NetworkContext context) {

    }

    @Override
    public void setUpdated() {

    }

    @NeverTouchThisCodeAgain
    @Override
    public void passNetwork(NetworkContext context, EnumFacing source, boolean srcIsCog, boolean srcCogIsHorizontal, boolean inverted) {
        IBlockState myState = world.getBlockState(pos);

        if (context.getInstanceAtPos(pos) != null) return;

        if (isCognectionValid(myState, source, srcIsCog, srcCogIsHorizontal)) {
            if (!inverted) {
                context.addKineticBlockInstance(new KineticBlockInstance(pos, false));
            } else {
                context.addKineticBlockInstance(new KineticBlockInstance(pos, true));
            }

            if (CreateLegacyModData.random.nextInt(100) == 0)
                world.playSound(null, pos.getX() + 0.5,
                        pos.getY() + 0.5, pos.getZ() + 0.5,
                        ModSoundHandler.BLOCK_COGWHEEL_AMBIENT_2, SoundCategory.BLOCKS,
                        0.1f, 1.0f);
            for (EnumFacing facing : EnumFacing.values()) {
                if (facing != source && context.getInstanceAtPos(pos.offset(facing)) == null) {
                    boolean srcCog = facing.getAxis() != myState.getValue(AXIS);
                    boolean srcCogH = myState.getValue(AXIS) == EnumFacing.Axis.Y;
                    TileEntity entity = world.getTileEntity(pos.offset(facing));
                    if (entity instanceof IKineticTE) {
                        if (!srcCog) {
                            ((IKineticTE) entity).passNetwork(context, facing.getOpposite(),
                                    false, false, inverted);
                        } else {
                            ((IKineticTE) entity).passNetwork(context, facing.getOpposite(),
                                    true, srcCogH, !inverted);
                        }
                    }
                }
            }
        }
    }
}
