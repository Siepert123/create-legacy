package com.siepert.createlegacy.integration;

import com.siepert.createlegacy.CreateLegacyModData;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.IWailaRegistrar;
import mcp.mobius.waila.api.WailaPlugin;
import net.minecraft.block.Block;

@WailaPlugin
public class WailaKineticPlugin implements IWailaPlugin {
    @Override
    public void register(IWailaRegistrar registrar) {
        registrar.registerBodyProvider(HWYLAKineticProvider.INSTANCE, Block.class);
        registrar.registerNBTProvider(HWYLAKineticProvider.INSTANCE, Block.class);
        registrar.addConfig(CreateLegacyModData.MOD_ID, CreateLegacyModData.MOD_ID + ".kinetic", true);
    }
}
