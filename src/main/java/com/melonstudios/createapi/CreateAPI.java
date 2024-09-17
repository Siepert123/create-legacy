package com.melonstudios.createapi;

import com.melonstudios.createapi.addon.AddonProcessor;
import com.melonstudios.createapi.addon.ICreateAddon;
import com.melonstudios.createapi.constant.ReflectionConstants;
import com.melonstudios.createlegacy.CreateLegacy;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.util.ArrayList;
import java.util.IllegalFormatException;
import java.util.List;

@SuppressWarnings("deprecation")
public final class CreateAPI {
    private static final List<ICreateAddon> ADDONS = new ArrayList<>();
    private static final List<ICreateAddon> ADDONS_BY_PRIORITY = new ArrayList<>();
    public static List<ICreateAddon> getAddons() {
        return ADDONS_BY_PRIORITY;
    }

    /**
     * Discovers, instances and readies all create addons
     *
     * @author moddingforreal
     * @param event Forge event to get the ASMDataTable from
     */
    public static void discoverAndSortAddons(FMLPreInitializationEvent event) {
        ADDONS.addAll(AddonProcessor.getAddonInstances(
                    AddonProcessor.getValidAddons(
                    AddonProcessor.discoverAddons(event, ReflectionConstants.createAddonClasspath))));
        ADDONS_BY_PRIORITY.addAll(AddonProcessor.sortAddons(ADDONS));
    }

    public static int getVersion() {
        return CreateLegacy.VERSION_NUM;
    }
    public static int getKineticVersion() {
        return CreateLegacy.KINETIC_VERSION_NUM;
    }

    public static String translateToLocal(String key) {
        if (I18n.canTranslate(key)) return I18n.translateToLocal(key);
        else return I18n.translateToFallback(key);
    }

    public static String translateToLocalFormatted(String key, Object... format) {
        String s = translateToLocal(key);
        try {
            return String.format(s, format);
        } catch (IllegalFormatException e) {
            return "Format Error: " + s;
        }
    }

    public static int longToIntSafe(long j) {
        if (j > Integer.MAX_VALUE) return Integer.MAX_VALUE;
        return (int) j;
    }


    public static String stressImpactTooltip(double impact) {
        return CreateAPI.translateToLocalFormatted("tooltip.stressImpact", impact);
    }

    public static String stressCapacityTooltip(double capacity) {
        return CreateAPI.translateToLocalFormatted("tooltip.stressCapacity", capacity);
    }
}
