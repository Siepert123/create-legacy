package com.melonstudios.createlegacy.objects.item;

import com.melonstudios.createlegacy.util.IMetaName;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockVariants extends ItemBlock {
    public ItemBlockVariants(Block block) {
        super(block);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        if (((ItemBlock) stack.getItem()).getBlock() instanceof IMetaName) {
            return ((IMetaName) ((ItemBlock) stack.getItem()).getBlock()).getUnlocalizedName(stack);
        } else {
            return getUnlocalizedName();
        }
    }
}
