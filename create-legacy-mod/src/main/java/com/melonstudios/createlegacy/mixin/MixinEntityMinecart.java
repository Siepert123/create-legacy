package com.melonstudios.createlegacy.mixin;

import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.util.math.AxisAlignedBB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityMinecart.class)
public abstract class MixinEntityMinecart {

    @Inject(method = "getMaximumSpeed", at = @At("RETURN"), cancellable = true)
    public void injectGetMaximumSpeed(CallbackInfoReturnable<Float> cir) {
        // Set the return value to 0.0 to change the maximum speed of the minecart
        cir.setReturnValue(0.0f);
    }
}
