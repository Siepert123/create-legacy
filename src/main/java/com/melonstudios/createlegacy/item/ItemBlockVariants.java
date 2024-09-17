package com.melonstudios.createlegacy.item;

import com.melonstudios.createlegacy.util.IMetaName;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class ItemBlockVariants extends ItemBlock {
    public ItemBlockVariants(Block block) {
        super(block);

        setHasSubtypes(true);
        setMaxDamage(0);

        setCreativeTab(block.getCreativeTabToDisplayOn());
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
