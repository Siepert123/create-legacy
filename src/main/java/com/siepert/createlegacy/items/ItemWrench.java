package com.siepert.createlegacy.items;

import com.siepert.createlegacy.CreateLegacy;
import com.siepert.createlegacy.mainRegistry.ModItems;
import com.siepert.createlegacy.util.IHasModel;
import com.siepert.createapi.IWrenchable;
import com.siepert.createlegacy.util.Reference;
import com.siepert.createlegacy.util.handlers.ModSoundHandler;
import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemWrench extends Item implements IHasModel {
    public ItemWrench() {
        setUnlocalizedName("create:wrench");
        setRegistryName("wrench");
        setMaxStackSize(1);
        setCreativeTab(CreateLegacy.TAB_CREATE);

        ModItems.ITEMS.add(this);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        Block lookingAt = worldIn.getBlockState(pos).getBlock();
        if (!player.isSneaking()) {
            if (lookingAt instanceof IWrenchable) {
                if (((IWrenchable) lookingAt).onWrenched(worldIn, pos, worldIn.getBlockState(pos), facing, player)) {
                    worldIn.playSound(null, pos.getX() + 0.5,
                            pos.getY() + 0.5, pos.getZ() + 0.5,
                            ModSoundHandler.ITEM_WRENCH_ROTATE, SoundCategory.BLOCKS,
                            1.0f, 1.0f);
                    return EnumActionResult.SUCCESS;
                }
            }
            return EnumActionResult.PASS;
        } else if (player.isSneaking()) {
            if (Reference.WRENCHABLES.contains(lookingAt)) {
                if (!player.isCreative()) {
                    NonNullList<ItemStack> stacks = NonNullList.create();
                    lookingAt.getDrops(stacks, worldIn, pos, worldIn.getBlockState(pos), 0);
                    //lookingAt.dropBlockAsItem(worldIn, pos, worldIn.getBlockState(pos), 0);
                    for (ItemStack stack : stacks) {
                        if (player.inventory.getFirstEmptyStack() != -1) {
                            player.addItemStackToInventory(stack);
                        } else {
                            EntityItem item = new EntityItem(worldIn, player.posX,
                                    player.posY, player.posZ);
                            item.setItem(stack);
                            item.setPickupDelay(0);
                            worldIn.spawnEntity(item);
                        }
                    }
                    worldIn.playSound(null, pos.getX() + 0.5,
                            pos.getY() + 0.5, pos.getZ() + 0.5,
                            ModSoundHandler.ITEM_WRENCH_DISMANTLE, SoundCategory.BLOCKS,
                            1.0f, 1.0f);
                }
                worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
                worldIn.playSound(null, pos.getX() + 0.5,
                        pos.getY() + 0.5, pos.getZ() + 0.5,
                        ModSoundHandler.ITEM_WRENCH_ROTATE, SoundCategory.BLOCKS,
                        1.0f, 1.0f);
                worldIn.playSound(null, pos.getX() + 0.5,
                        pos.getY() + 0.5, pos.getZ() + 0.5,
                        worldIn.getBlockState(pos).getBlock().getSoundType().getBreakSound(), SoundCategory.BLOCKS,
                        1.0f, 1.0f);

                return EnumActionResult.SUCCESS;
            }
        }
        return EnumActionResult.PASS;
    }

    @Override
    public boolean isDamageable() {
        return false;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add("");
        tooltip.add("Right-Click to:");
        tooltip.add("Rotate or Configure the block you're looking at");
    }

    @Override
    public void registerModels() {
        CreateLegacy.proxy.registerItemRenderer(this, 0, "inventory");
    }
}
