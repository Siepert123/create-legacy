package com.melonstudios.createlegacy.util.registries;

import net.minecraft.util.DamageSource;

public final class ModDamageSources {
    public static final DamageSource DRILLING = (new DamageSource("drilling")).setDamageBypassesArmor();
    public static final DamageSource CUTTING = (new DamageSource("cutting")).setDamageBypassesArmor();
    public static final DamageSource CRUSHING = (new DamageSource("crushing")).setDamageIsAbsolute().setDamageBypassesArmor();
}
