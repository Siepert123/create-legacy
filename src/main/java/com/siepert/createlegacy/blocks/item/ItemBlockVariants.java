package com.siepert.createlegacy.blocks.item;

import com.siepert.createlegacy.util.IMetaName;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

/**Useful class for having blocks with subtypes having items. */
public class ItemBlockVariants extends ItemBlock {
    public ItemBlockVariants(Block block) {
        super(block);
        setHasSubtypes(true);
        setMaxDamage(0);
    }

    @Override
    public int getMetadata(int damage) {
        return damage;
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return super.getUnlocalizedName() + ((IMetaName)this.block).getSpecialName(stack);
    }
}
