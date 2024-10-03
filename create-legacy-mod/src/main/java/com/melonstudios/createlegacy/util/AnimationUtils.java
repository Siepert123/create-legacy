package com.melonstudios.createlegacy.util;

public final class AnimationUtils {

    public static float smoothen(float previousAngle, float nextAngle, float partialTick) {
        float f1 = nextAngle * partialTick;
        float f2 = previousAngle * (1 - partialTick);
        return f1 + f2;
    }
}
