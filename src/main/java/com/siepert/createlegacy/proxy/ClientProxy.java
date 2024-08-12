package com.siepert.createlegacy.proxy;

import com.siepert.createlegacy.CreateLegacyModData;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;

import javax.annotation.Nonnull;

public class ClientProxy extends CommonProxy {

    @Override
    public void registerItemRenderer(Item item, int meta, String id) {
        ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(item.getRegistryName(), id));
    }

    @Override
    public void registerVariantRenderer(@Nonnull Item item, int meta, String filename, String id) {
        ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(new ResourceLocation(CreateLegacyModData.MOD_ID, filename), id));
    }
}
