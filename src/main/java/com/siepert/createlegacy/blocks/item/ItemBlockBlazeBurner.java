package com.siepert.createlegacy.blocks.item;

import com.siepert.createlegacy.mainRegistry.ModBlocks;
import com.siepert.createlegacy.util.IMetaName;
import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

/**Useful class for having blocks with subtypes having items. */
public class ItemBlockBlazeBurner extends ItemBlock {
    public ItemBlockBlazeBurner(Block block) {
        super(block);
        setHasSubtypes(true);
        setMaxDamage(0);
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer playerIn, EntityLivingBase target, EnumHand hand) {
        if (target instanceof EntityBlaze) {
            stack.shrink(1);
            target.setDead();
            playerIn.addItemStackToInventory(new ItemStack(Item.getItemFromBlock(ModBlocks.BLAZE_BURNER), 1, 1));
            playerIn.playSound(SoundEvents.ENTITY_BLAZE_AMBIENT, 0.5f, 1.0f);
            return true;
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

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        if (stack.getMetadata() != 0) {
            tooltip.add("Likes coal");
        }
    }
}
