package com.melonstudios.createapi.addon;

import com.melonstudios.createapi.CreateAPI;
import com.melonstudios.createapi.annotation.UntestedCode;
import com.melonstudios.createlegacy.util.DisplayLink;
import net.minecraftforge.fml.common.discovery.ASMDataTable;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import javax.annotation.Nonnull;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Processes create addons
 * @author moddingforreal
 * @since 0.1.0
 */
@UntestedCode("Create Legacy addon processor: hasn't been tested yet")
public class AddonProcessor {
    /**
     * Logs an addon's metadata
     * @since 0.1.0
     * @param addonClass The annotated class to check the metadata of
     */
    public static void logCreateAddonMetadata(@Nonnull Class<?> addonClass) {
        DisplayLink.debug("Querying data from class %s", addonClass.getCanonicalName());
        if (addonClass.isAnnotationPresent(CreateAddon.class)) {
            CreateAddon annotation = addonClass.getAnnotation(CreateAddon.class);

            DisplayLink.debug("Mod ID: %s", annotation.modid());
            DisplayLink.debug("Create version: %s", annotation.createVersion());
            DisplayLink.debug("Kinetic version: %s", annotation.kineticVersion());
            DisplayLink.debug("Load priority: %s", annotation.loadPriority());
        }
    }

    /**
     * Discovers create addons
     * @since 0.1.0
     * @param event Event to get the ASMDataTable from
     * @return All classes annotated with <code>CreateAddon</code>
     */
    public static List<Class<?>> discoverAddons(FMLPreInitializationEvent event, String annotationClass) {
        Set<ASMDataTable.ASMData> classes = event.getAsmData().getAll(annotationClass);
        List<Class<?>> annotated = new ArrayList<>();
        for (ASMDataTable.ASMData data : classes) {
            try {
                Class<?> clazz = Class.forName(data.getClassName());
                annotated.add(clazz);
                DisplayLink.debug("Found create addon: " + clazz.getCanonicalName());

                logCreateAddonMetadata(clazz);
            } catch (Exception e) {
                DisplayLink.error("Something went wrong during addon loading!");
                e.printStackTrace();
            }
        }
        return annotated;
    }

    /**
     * Checks which addons are valid
     * @since 0.1.0
     * @param classList A list of classes annotated with <code>@CreateAddon</code>
     * @return A list of all valid addons
     */
    public static List<Class<ICreateAddon>> getValidAddons(List<Class<?>> classList) {
        List<Class<ICreateAddon>> addons = new ArrayList<>();
        for (Class<?> clazz : classList) {
            CreateAddon ann = clazz.getAnnotation(CreateAddon.class);
            if (ann.createVersion() != CreateAPI.getVersion()
                || ann.kineticVersion() != CreateAPI.getKineticVersion())
                continue; // Only if versions match up check the addon further
            if (ICreateAddon.class.isAssignableFrom(clazz))
                addons.add((Class<ICreateAddon>) clazz);
        }
        return addons;
    }

    /**
     * Checks if possible and instantiates the addon classes
     * @param classList List of create addon classes
     * @return List of create addon instances
     */
    public static List<ICreateAddon> getAddonInstances(List<Class<ICreateAddon>> classList) {
        List<ICreateAddon> addons = new ArrayList<>();
        for (Class<ICreateAddon> clazz : classList) {
            try {
                Constructor<ICreateAddon> c = clazz.getConstructor();
                addons.add(c.newInstance());
            } catch (NoSuchMethodException | SecurityException e) {
                DisplayLink.error("Couldn't get a parameterless default constructor for class: %s", clazz.getCanonicalName());
                e.printStackTrace();
            } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
                DisplayLink.error("Couldn't invoke parameterless default constructor for class: %s", clazz.getCanonicalName());
                e.printStackTrace();
            }
        }
        return addons;
    }

    /**
     * Sorts the addons list by priority
     * @author Siepert123
     * @author moddingforreal
     * @since 0.1.0
     * @param addonsList List of unsorted addons
     * @return Addons sorted by priority
     */
    public static List<ICreateAddon> sortAddons(List<ICreateAddon> addonsList) {
        List<ICreateAddon> addonsByPriority = new ArrayList<>();
        int lowestInt = 0;
        int highestInt = 0;
        for (ICreateAddon addon : addonsList) {
            CreateAddon annotation = addon.getClass().getAnnotation(CreateAddon.class);

            if (annotation.loadPriority() < lowestInt) lowestInt = annotation.loadPriority();
            if (annotation.loadPriority() > highestInt) highestInt = annotation.loadPriority();

        }

        for (int i = lowestInt; i <= highestInt; i++) {
            for (ICreateAddon addon : addonsList) {
                CreateAddon annotation = addon.getClass().getAnnotation(CreateAddon.class);

                if (annotation.loadPriority() == i) addonsByPriority.add(addon);
            }
        }
        return addonsByPriority;
    }
}
