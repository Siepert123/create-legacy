package com.melonstudios.createlegacy.core.proxy;

import net.minecraft.item.Item;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class CommonProxy {
    public void setItemModel(Item item, int meta, String file) {}
    public void setItemModel(Item item, String file) {}
    public void setItemModel(Item item) {}


    public MinecraftServer getMCServer() {
        return FMLCommonHandler.instance().getMinecraftServerInstance(); // Works when remote?
    }
}
