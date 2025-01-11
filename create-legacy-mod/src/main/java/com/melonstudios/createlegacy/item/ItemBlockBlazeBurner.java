package com.melonstudios.createlegacy.item;

import com.melonstudios.createlegacy.block.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemBlockBlazeBurner extends ItemBlockVariants {
    public ItemBlockBlazeBurner(Block block) {
        super(block);
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer player, EntityLivingBase entity, EnumHand hand) {
        if (stack.getMetadata() == 0 && entity instanceof EntityBlaze) {
            entity.setDead();
            stack.shrink(1);
            if (!player.world.isRemote) {
                EntityItem item = new EntityItem(player.world, player.posX, player.posY, player.posZ,
                        new ItemStack(ModBlocks.BLAZE_BURNER, 1, 1));
                item.motionX = item.motionY = item.motionZ = 0;
                player.world.spawnEntity(item);
                player.world.playSound(null,
                        player.posX, player.posY, player.posZ,
                        SoundEvents.ENTITY_BLAZE_AMBIENT, SoundCategory.NEUTRAL,
                        1, 1);
            }
            return true;
        }
        return false;
    }
}
