package com.siepert.createlegacy.blocks.item;

import com.siepert.createlegacy.mainRegistry.ModBlocks;
import com.siepert.createlegacy.util.IMetaName;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

/**Useful class for having blocks with subtypes having items. */
public class ItemBlockBlazeBurner extends ItemBlock {
    public ItemBlockBlazeBurner(Block block) {
        super(block);
        setHasSubtypes(true);
        setMaxDamage(0);
    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
        if (attacker instanceof EntityPlayer) {
            if (target instanceof EntityBlaze) {
                stack.shrink(1);
                target.setDead();
                ((EntityPlayer) attacker).addItemStackToInventory(new ItemStack(Item.getItemFromBlock(ModBlocks.BLAZE_BURNER), 1, 1));
                return true;
            }
        }
        return false;
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
