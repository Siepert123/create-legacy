package com.siepert.createlegacy.items;

import com.siepert.createlegacy.CreateLegacy;
import com.siepert.createlegacy.mainRegistry.ModItems;
import com.siepert.createlegacy.util.IHasModel;
import com.siepert.createlegacy.util.handlers.ModSoundHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

public class ItemSandpaper extends Item implements IHasModel {

    public ItemSandpaper() {
        setUnlocalizedName("create:sandpaper");
        setRegistryName("sandpaper");
        setCreativeTab(CreateLegacy.TAB_CREATE);
        setMaxDamage(15);
        setMaxStackSize(1);

        ModItems.ITEMS.add(this);
    }

    @Override
    public void registerModels() {
        CreateLegacy.proxy.registerItemRenderer(this, 0, "inventory");
    }


    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        if (!worldIn.isRemote) {
            if (playerIn.inventory.getFirstEmptyStack() == -1) return super.onItemRightClick(worldIn, playerIn, handIn);
            ItemStack leftHandStack = playerIn.getHeldItem(EnumHand.OFF_HAND);
            ItemStack rightHandStack = playerIn.getHeldItem(EnumHand.MAIN_HAND);
            ItemStack leftHandStackCopy = leftHandStack.copy();
            leftHandStackCopy.setCount(1);
            if (leftHandStack.getItemDamage() == 19 && leftHandStack.getItem() == ModItems.INGREDIENT) {
                leftHandStack.shrink(1);
                rightHandStack.damageItem(1, playerIn);
                    playerIn.addItemStackToInventory(new ItemStack(ModItems.INGREDIENT, 1, 20));
                worldIn.playSound(null, playerIn.posX, playerIn.posY + 0.5, playerIn.posZ, ModSoundHandler.ITEM_SANDPAPER_USED,
                        SoundCategory.AMBIENT, 1.0f, 1.0f);
                return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
            }
        }
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }
}
