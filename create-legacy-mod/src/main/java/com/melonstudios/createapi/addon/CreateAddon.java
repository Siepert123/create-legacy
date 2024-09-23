package com.melonstudios.createapi.addon;

import java.lang.annotation.*;

/**
 * <strong>Annotated classes <u><em>must</em> implement</u> {@link com.melonstudios.createapi.addon.ICreateAddon ICreateAddon}</strong><br>
 * <p>
 * Classes annotated with <code>{@literal @}CreateAddon</code> <em>need</em> to supply (the addon's)
 * modid and the versions of create and the kinetic system that it was made for.<br>
 * Values can be retrieved from {@link com.melonstudios.createlegacy.CreateLegacy CreateLegacy}<br>
 * Optionally, a loadPriority can be supplied, this will otherwise <u>default to 0.</u><br>
 * </p><br>
 * <p>
 * <u>Example:</u><br>
 * <pre><code>
 * {@literal @}CreateAddon(modid = "exampleaddon", createVersion = 8, kineticVersion = 3)
 *  public class MyCreateAddon() {
 *      // your code here
 *  }
 * </code></pre></p>
 *
 * Annotated classes will be automagically discovered using Forge's {@link net.minecraftforge.fml.common.discovery.ASMDataTable ASMDataTable}.<br>
 *
 * @author moddingforreal
 * @since 0.1.0
 * @see com.melonstudios.createapi.addon.ICreateAddon
 * @see com.melonstudios.createapi.addon.CreateAddon
 * @see com.melonstudios.createapi.CreateAPI
 * @see com.melonstudios.createlegacy.CreateLegacy
 * */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CreateAddon { //TODO: document this
    String modid();

    int createVersion();
    int kineticVersion();

    int loadPriority() default 0;
}
