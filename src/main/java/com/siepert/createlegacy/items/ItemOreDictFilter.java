package com.siepert.createlegacy.items;

import com.siepert.createlegacy.CreateLegacy;
import com.siepert.createlegacy.mainRegistry.ModItems;
import com.siepert.createlegacy.util.IHasModel;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nullable;
import java.util.List;

public class ItemOreDictFilter extends Item implements IHasModel {
    public ItemOreDictFilter() {
        setRegistryName("filter_ore_dict");
        setUnlocalizedName("create:filter_ore_dict");
        setCreativeTab(CreateLegacy.TAB_CREATE);

        setMaxStackSize(1);

        ModItems.ITEMS.add(this);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer player, EnumHand hand) {
        if (hand != EnumHand.MAIN_HAND) return super.onItemRightClick(worldIn, player, hand);
        ItemStack theStack = player.getHeldItem(EnumHand.MAIN_HAND);
        ItemStack notTheStack = player.getHeldItem(EnumHand.OFF_HAND);
        if (!worldIn.isRemote) {
            deNullify(theStack);
            NBTTagCompound itemNBT = theStack.getTagCompound();
            if (notTheStack.isEmpty()) {
                if (player.isSneaking()) {
                    itemNBT.setString("Filter", "");
                    player.sendStatusMessage(new TextComponentString("Filter removed"), true);
                } else {
                    String filter = itemNBT.getString("Filter");
                    if (filter == "") {
                        player.sendStatusMessage(new TextComponentString("No filter set"), true);
                    } else {
                    player.sendStatusMessage(new TextComponentString("Filter: " + itemNBT.getString("Filter")), true);
                    }
                }
            } else {
                if (OreDictionary.getOreIDs(notTheStack).length != 0) {
                    String dict = OreDictionary.getOreName(OreDictionary.getOreIDs(notTheStack)[0]);
                    itemNBT.setString("Filter", dict);
                    player.sendStatusMessage(new TextComponentString("Filter set to " + dict), true);
                } else {
                    player.sendStatusMessage(new TextComponentString("That item does not have an OreDict tag"), true);
                }
            }
            theStack.setTagCompound(itemNBT);
        }
        return new ActionResult(EnumActionResult.SUCCESS, theStack);
    }

    @Override
    public void registerModels() {
        CreateLegacy.proxy.registerItemRenderer(this, 0, "inventory");
    }

    public static void deNullify(ItemStack stack) {
        NBTTagCompound itemNBT = stack.getTagCompound();

        if (itemNBT == null) {
            itemNBT = new NBTTagCompound();
            stack.setTagCompound(itemNBT);
        }
    }

    public static boolean itemMatches(ItemStack filter, ItemStack stack) {
        String filterName = filter.getTagCompound().getString("Filter");

        if (OreDictionary.doesOreNameExist(filterName)) {
            for (ItemStack stack1 : OreDictionary.getOres(filterName)) {
                if (stack1.isItemEqual(stack)) return true;
            }
        }

        return false;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        deNullify(stack);
        String currentFilterTag = stack.getTagCompound().getString("Filter");

        if (currentFilterTag == "") {
            tooltip.add("No filter set");
        } else {
            tooltip.add("Filter: " + currentFilterTag);
        }
    }
}
