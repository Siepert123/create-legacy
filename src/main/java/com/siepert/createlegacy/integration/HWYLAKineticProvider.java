package com.siepert.createlegacy.integration;

import com.siepert.createapi.Spaghetti;
import com.siepert.createapi.network.IKineticTE;
import com.siepert.createlegacy.CreateLegacyModData;
import com.siepert.createlegacy.blocks.kinetic.BlockNetworkMeter;
import com.siepert.createlegacy.tileentity.TileEntityAxle;
import com.siepert.createlegacy.tileentity.TileEntityGearbox;
import com.siepert.createlegacy.tileentity.TileEntitySpeedometer;
import com.siepert.createlegacy.tileentity.TileEntityStressometer;
import com.siepert.createlegacy.util.handlers.NeverTouchThisCodeAgain;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

@Spaghetti(why = "From looking at other mods' implementations, it seems this is how to do it")
@NeverTouchThisCodeAgain // Unless you change Kinetic interfaces ofc
@Optional.InterfaceList({ @Optional.Interface(iface="mcp.mobius.waila.api.IWailaDataProvider", modid="waila")})
public class HWYLAKineticProvider implements IWailaDataProvider {
    public static final HWYLAKineticProvider INSTANCE = new HWYLAKineticProvider();

    @Override
    @SideOnly(Side.CLIENT)
    public List<String> getWailaBody(ItemStack itemStack, List<String> tooltip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        if (!config.getConfig(CreateLegacyModData.MOD_ID + ".kinetic") || !accessor.getNBTData().hasKey("KineticBlock", Constants.NBT.TAG_COMPOUND))
        {
            return tooltip;
        }
        NBTTagCompound tag = accessor.getNBTData().getCompoundTag("KineticBlock");
        if (tag.getBoolean("isnetworkmeter")) {
            setWailaNetworkMeterBody(tooltip, tag);
        } else {
            setWailaKineticBody(tooltip, tag, accessor.getPlayer().isSneaking());
        }
        return tooltip;
    }

    private void setWailaNetworkMeterBody(List<String> tooltip, NBTTagCompound tag) {
        tooltip.add(tag.getString("networkInfo"));
        if (tag.getString("networkMeterType").equals("speedometer")) {
            // Pass, as speedometer doesn't have any more relevant info
        } else if (tag.getString("networkMeterType").equals("stressometer")) {
            if (tag.getBoolean("infinitestress")) {
                tooltip.add("\u00a7b" + I18n.format("create.kineticinfo.infinitestress") + "\u00a7r");
            } else if (tag.getBoolean("overstressed")) {
                tooltip.add("\u00a7c" + I18n.format("create.kineticinfo.overstressed") + "\u00a7r");
            }
        }
    }

    private void setWailaKineticBody(List<String> tooltip, NBTTagCompound tag, boolean playerSneaking) {
        if (tag.getBoolean("isConsumer")) {
            tooltip.add("\u00a7d" + I18n.format("create.kineticinfo.stressimpact") + ": " + tag.getDouble("stressimpact") + " SU/RS\u00a7r");
            if (playerSneaking) {
                tooltip.add("\u00a77" + I18n.format("create.kineticinfo.minimalspeed") + ": " + tag.getInteger("minimalspeed") + " RS\u00a7r");
            }
        }
        if (tag.getBoolean("isGenerator")) {
            if (!playerSneaking) {
                tooltip.add("\u00a7d" + I18n.format("create.kineticinfo.stresscapacity") + ": " +
                        tag.getDouble("stresscapacity") + " SU/RS\u00a7r");
            } else {
                tooltip.add("\u00a7d" + I18n.format("create.kineticinfo.stresscapacity_atspeed") + ": " +
                        tag.getDouble("stresscapatspeed") + " SU\u00a7r");
            }
            tooltip.add("\u00a75" + I18n.format("create.kineticinfo.speedproduced") + ": " + tag.getInteger("speedproduced") + " RS\u00a7r");
        }
    }

    @Override
    public NBTTagCompound getNBTData(EntityPlayerMP player, TileEntity te, NBTTagCompound tag, World world, BlockPos pos) {
        if (!(te instanceof IKineticTE))
            return tag;
        if ((te instanceof TileEntityAxle || te instanceof TileEntityGearbox) && !player.isSneaking())
            return tag;
        IKineticTE kineticBlock = (IKineticTE) te;
        NBTTagCompound compound = new NBTTagCompound();
        if (world.getBlockState(pos).getBlock() instanceof BlockNetworkMeter) {
            setNetworkMeterBlockNBT(player, (IKineticTE) te, compound, world, (BlockNetworkMeter) world.getBlockState(pos).getBlock());
            compound.setBoolean("isnetworkmeter", true);
        } else {
            setKineticBlockNBT(player, (IKineticTE) te, compound, world, world.getBlockState(pos).getBlock());
            compound.setBoolean("isnetworkmeter", false);
        }
        tag.setTag("KineticBlock", compound);
        return tag;
    }

    private void setNetworkMeterBlockNBT(EntityPlayerMP player, IKineticTE te, NBTTagCompound tag, World world, BlockNetworkMeter block) {
        // Have to do strings until we change the network meters to have another method to give me the required info or smth
        if (te instanceof TileEntitySpeedometer) {
            tag.setString("networkInfo", "\u00a7aNetwork: " + ((TileEntitySpeedometer) te).getMessageServer() + "\u00a7r");
            tag.setString("networkMeterType", "speedometer");
        } else if (te instanceof TileEntityStressometer) {
            tag.setString("networkInfo", "\u00a7eNetwork: " + ((TileEntityStressometer) te).getMessageServer() + "\u00a7r");
            if (((TileEntityStressometer) te).getLastContext() == null) // Can't get last context if it doesn't exist
                return;
            tag.setBoolean("infinitestress", ((TileEntityStressometer) te).getLastContext().infiniteSU);
            tag.setBoolean("overstressed", ((TileEntityStressometer) te).getLastContext().isNetworkOverstressed());
            tag.setString("networkMeterType", "stressometer");
        }
    }

    private void setKineticBlockNBT(EntityPlayerMP player, IKineticTE te, NBTTagCompound tag, World world, Block block) {
        if (te.isConsumer()) {
            tag.setBoolean("isConsumer", true);
            tag.setDouble("stressimpact", te.getStressImpact());
            tag.setInteger("minimalspeed", te.getMinimalSpeed());
        }
        if (te.isGenerator()) {
            tag.setBoolean("isGenerator", true);
            tag.setDouble("stresscapacity", te.getStressCapacity());
            tag.setDouble("stresscapatspeed", te.getStressCapacity() * te.getProducedSpeed());
            tag.setInteger("speedproduced", te.getProducedSpeed());
        }
    }
}
