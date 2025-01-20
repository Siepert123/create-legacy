package com.melonstudios.createlegacy.util;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.entity.player.EntityPlayerMP;

public class AdvancementUtil {
    public static void grantAchievement(EntityPlayerMP player, Advancement advancement) {
        PlayerAdvancements advancements = player.getAdvancements();
        AdvancementProgress progress = advancements.getProgress(advancement);

        for (String criterion : progress.getRemaningCriteria()) {
            advancements.grantCriterion(advancement, criterion);
        }
    }
}
