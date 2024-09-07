package com.siepert.createapi.addons.annotation;

import com.siepert.createapi.TestCode;

import javax.annotation.Nullable;
import java.lang.annotation.*;

/**
 * Classes annotated with <code>{@literal @}CreateAddon</code> need to supply (the addon's)
 * modid and the versions of create and the kinetic system that it was made for.
 * Values can be retrieved from {@link com.siepert.createlegacy.CreateLegacyModData CreateLegacyModData}<br>
 * Optionally, a loadPriority can be supplied, this will otherwise <u>default to 0.</u><br>
 * <br>
 * <u>Example:</u><br>
 * <pre><code>
 * {@literal @}CreateAddon(modid = "exampleaddon", createVersion = 8, kineticVersion = 3)
 *  public class MyCreateAddon() {
 *      // your code here
 *  }
 * </code></pre>
 *
 * Annotated classes will be automagically discovered using Forge's {@link net.minecraftforge.fml.common.discovery.ASMDataTable ASMDataTable}.<br>
 * Can implement {@link com.siepert.createapi.addons.ICreateAddon ICreateAddon} or extend {@link com.siepert.createapi.addons.CreateAddon CreateAddon} for backwards compatibility.<br>
 * Implementing the interface is <strong>not</strong> required anymore, it is however <em>recommended</em>.<br>
 *
 * @author moddingforreal
 * @see com.siepert.createapi.addons.ICreateAddon
 * @see com.siepert.createapi.addons.CreateAddon
 * @see com.siepert.createapi.CreateAPI
 * @see com.siepert.createlegacy.CreateLegacyModData
 * */
@Target(ElementType.TYPE) // Only works with classes
@Retention(RetentionPolicy.RUNTIME) // Keep this in the resulting jar
@Documented // This should also probably be documented in any generated documents
@TestCode(explanation = "Didn't have time to thoroughly test this")
public @interface CreateAddon {
    /** @return The addon's modid */
    String modid();
    /**
     * @return The target create version; appropriate values can be found in {@link com.siepert.createlegacy.CreateLegacyModData CreateLegacyModData}
     * @see com.siepert.createlegacy.CreateLegacyModData
     * */
    int createVersion(); // I purposely omitted defaults for create & kinetic version to force addon devs to look it up!
    /**
     * @return The target kinetic system version; appropriate values can be found in {@link com.siepert.createlegacy.CreateLegacyModData CreateLegacyModData}
     * @see com.siepert.createlegacy.CreateLegacyModData
     * */
    int kineticVersion();
    /** @return The addon's load priority; higher values result in being loaded before addons with lower values.<br>Defaults to 0 */
    int loadPriority() default 0;
}
