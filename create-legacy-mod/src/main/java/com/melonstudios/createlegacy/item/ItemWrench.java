package com.melonstudios.createlegacy.item;

import com.melonstudios.createlegacy.CreateLegacy;
import com.melonstudios.createlegacy.block.IWrenchable;
import com.melonstudios.createlegacy.util.BlockTagHelper;
import com.melonstudios.createlegacy.util.registries.ModSoundEvents;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemWrench extends Item {
    public ItemWrench() {
        super();

        setRegistryName("wrench");
        setUnlocalizedName("create.wrench");

        setMaxStackSize(1);

        setCreativeTab(CreateLegacy.TAB_KINETICS);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (player.isSneaking()) {
            IBlockState state = worldIn.getBlockState(pos);
            if (BlockTagHelper.hasTag(state, "create:wrenchPickup")) {
                if (!worldIn.isRemote) {
                    worldIn.playEvent(2001, pos, Block.getStateId(state));
                    ItemStack dropped = new ItemStack(state.getBlock().getItemDropped(state, worldIn.rand, 0),
                            1, state.getBlock().damageDropped(state));
                    EntityItem item = new EntityItem(worldIn, player.posX, player.posY, player.posZ, dropped);
                    item.setVelocity(0, 0, 0);
                    item.setNoPickupDelay();
                    worldIn.spawnEntity(item);
                    worldIn.setBlockToAir(pos);
                    worldIn.playSound(null, pos, ModSoundEvents.ITEM_WRENCH_USED_ROTATE, SoundCategory.PLAYERS, 1.0f, 1.0f);
                    worldIn.playSound(null, pos, ModSoundEvents.ITEM_WRENCH_USED_DISMANTLE, SoundCategory.PLAYERS, 1.0f, 1.0f);
                }
                return EnumActionResult.SUCCESS;
            }
        } else {
            IBlockState state = worldIn.getBlockState(pos);
            if (state.getBlock() instanceof IWrenchable) {
                if (((IWrenchable) state.getBlock()).onWrenched(worldIn, pos, state, side, player)) {
                    worldIn.playSound(null, pos, ModSoundEvents.ITEM_WRENCH_USED_ROTATE, SoundCategory.PLAYERS, 1.0f, 1.0f);
                    return EnumActionResult.SUCCESS;
                }
            }
        }
        return EnumActionResult.PASS;
    }
}
