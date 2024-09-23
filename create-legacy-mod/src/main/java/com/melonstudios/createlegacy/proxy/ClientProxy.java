package com.melonstudios.createlegacy.proxy;

import com.melonstudios.createlegacy.CreateLegacy;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;

public class ClientProxy extends CommonProxy {

    public void setItemModel(Item item, int meta, String file) {
        ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(new ResourceLocation(CreateLegacy.MOD_ID, file), "inventory"));
    }
    public void setItemModel(Item item, String file) {
        setItemModel(item, 0, file);
    }
    public void setItemModel(Item item) {
        setItemModel(item, item.getRegistryName().getResourcePath());
    }

}
