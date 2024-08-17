package com.siepert.createlegacy.integration;

import com.siepert.createapi.network.IKineticTE;
import com.siepert.createlegacy.CreateLegacyModData;
import com.siepert.createlegacy.blocks.kinetic.BlockNetworkMeter;
import com.siepert.createlegacy.tileentity.TileEntityAxle;
import com.siepert.createlegacy.tileentity.TileEntitySpeedometer;
import com.siepert.createlegacy.tileentity.TileEntityStressometer;
import com.siepert.createlegacy.util.handlers.NeverTouchThisCodeAgain;
import mcjty.theoneprobe.TheOneProbe;
import mcjty.theoneprobe.api.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Optional;

import javax.annotation.Nullable;

/**
 * IProbeInfoProvider providing info for kinetic blocks
 *
 * @author moddingforreal
 * @see com.siepert.createapi.network.IKineticTE
 * */
@NeverTouchThisCodeAgain // Unless you change the kinetic system of course!
@Optional.InterfaceList({ @Optional.Interface(iface="mcjty.theoneprobe.api.IProbeInfoProvider", modid="theoneprobe")})
public class KineticInfoProvider implements IProbeInfoProvider {
    public static void registerProbeInfoProvider() {
        Object oneProbe = TheOneProbe.theOneProbeImp;
        ((ITheOneProbe) oneProbe).registerProvider(new KineticInfoProvider());
    }
    @Override
    public String getID() {
        return CreateLegacyModData.MOD_ID + ":kinetic_info_provider";
    }

    @Override
    public void addProbeInfo(ProbeMode probeMode, IProbeInfo probeInfo, EntityPlayer player, World world,
                             IBlockState blockState, IProbeHitData probeHitData) {
        IKineticTE kineticTile = getKineticBlock(blockState, world, probeHitData); // Check if block is IKineticTE
        if (kineticTile == null)
            return;
        if (blockState.getBlock() instanceof BlockNetworkMeter) { // If it's a meter block, we can display network stats
            attachNetworkMeterInfo(world.getTileEntity(probeHitData.getPos()), probeInfo);
            return;
        }
        if ((kineticTile instanceof TileEntityAxle) && !player.isSneaking()) // Axle info only if player is sneaking
            return;
        attachKineticTileInfo(kineticTile, probeInfo, player.isSneaking()); // Otherwise just generic info
    }

    private void attachNetworkMeterInfo(TileEntity networkMeterTile, IProbeInfo probeInfo) {
        if (networkMeterTile instanceof TileEntitySpeedometer) {
            probeInfo.text("\u00a7aNetwork: " + ((TileEntitySpeedometer) networkMeterTile).getMessage() + "\u00a7r");
        } else if (networkMeterTile instanceof TileEntityStressometer) {
            probeInfo.text("\u00a7eNetwork: " + ((TileEntityStressometer) networkMeterTile).getMessage() + "\u00a7r");
            if (((TileEntityStressometer) networkMeterTile).getLastContext() == null) // Can't get last context if it doesnt exist
                return;
            if (((TileEntityStressometer) networkMeterTile).getLastContext().infiniteSU) {
                probeInfo.text("\u00a7bInfinite stress capacity\u00a7r");
            } else if (((TileEntityStressometer) networkMeterTile).getLastContext().isNetworkOverstressed()) {
                probeInfo.text("\u00a7cNetwork Overstressed\u00a7r");
            }
        }
    }

    private void attachKineticTileInfo(IKineticTE kineticTile, IProbeInfo probeInfo, boolean playerSneaking) {
        if (kineticTile.isConsumer()) {
            probeInfo.text("\u00a7dStress impact: " + kineticTile.getStressImpact() + " SU/RS\u00a7r");
            if (kineticTile.getMinimalSpeed() > 0 && playerSneaking) {
                probeInfo.text("\u00a77Minimal speed: " + kineticTile.getMinimalSpeed() + " RS\u00a7r");
            }
        } else if (kineticTile.isGenerator()) {
            if (!playerSneaking) {
                probeInfo.text("\u00a7dStress capacity: " +
                        kineticTile.getStressCapacity() + " SU/RS\u00a7r");
            }
            if (playerSneaking) {
                probeInfo.text("\u00a7dStress capacity at current speed: " +
                        (kineticTile.getStressCapacity() * kineticTile.getProducedSpeed()) + " SU\u00a7r");
            }
            probeInfo.text("\u00a75Speed produced: " + kineticTile.getProducedSpeed() + " RS\u00a7r");
        }
    }

    /**
     * @return <code>null</code> if there is no <code>IKineticTE</code>, otherwise the associated <code>IKineticTE</code>
     * */
    @Nullable
    private IKineticTE getKineticBlock(IBlockState blockState, World world, IProbeHitData probeHitData) {
        if (!blockState.getBlock().hasTileEntity(blockState))
            return null;
        TileEntity tileEntity = world.getTileEntity(probeHitData.getPos());
        if (tileEntity == null)
            return null;
        if(!(tileEntity instanceof IKineticTE))
            return null;
        return (IKineticTE) tileEntity;
    }
}
