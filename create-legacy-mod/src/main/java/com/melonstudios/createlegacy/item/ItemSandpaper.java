package com.melonstudios.createlegacy.item;

import com.melonstudios.createlegacy.CreateLegacy;
import com.melonstudios.createlegacy.recipe.SandingRecipes;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ItemSandpaper extends Item {
    public ItemSandpaper() {
        super();

        setRegistryName("sandpaper");
        setUnlocalizedName("create.sandpaper");

        setCreativeTab(CreateLegacy.TAB_KINETICS);

        setMaxDamage(8);
        setHasSubtypes(false);
        setMaxStackSize(1);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        ItemStack stack = playerIn.getHeldItem(handIn);
        ItemStack otherStack = playerIn.getHeldItem(EnumHand.OFF_HAND);

        if (handIn != EnumHand.MAIN_HAND) return ActionResult.newResult(EnumActionResult.PASS, stack);

        if (SandingRecipes.hasResult(otherStack)) {
            if (!worldIn.isRemote) {
                ItemStack result = SandingRecipes.getResult(otherStack);

                EntityItem item = new EntityItem(worldIn,
                        playerIn.posX,
                        playerIn.posY,
                        playerIn.posZ,
                        result);

                item.setVelocity(0, 0, 0);

                worldIn.spawnEntity(item);

                otherStack.shrink(1);
                stack.damageItem(1, playerIn);
            }

            return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
        }

        return ActionResult.newResult(EnumActionResult.FAIL, stack);
    }
}
