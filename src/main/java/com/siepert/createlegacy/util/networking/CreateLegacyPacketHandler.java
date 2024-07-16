package com.siepert.createlegacy.util.networking;

import com.siepert.createlegacy.CreateLegacyModData;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

public class CreateLegacyPacketHandler {
    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(CreateLegacyModData.MOD_ID);
}
