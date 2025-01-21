package com.melonstudios.createlegacy.item;

import com.melonstudios.createlegacy.CreateLegacy;
import com.melonstudios.createlegacy.tileentity.TileEntitySpeedometer;
import com.melonstudios.createlegacy.tileentity.TileEntityStressometer;
import com.melonstudios.createlegacy.util.AdvancementUtil;
import com.melonstudios.melonlib.misc.ServerHack;
import net.minecraft.advancements.Advancement;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class ItemGoggles extends ItemArmor {
    public ItemGoggles() {
        super(ArmorMaterial.GOLD, 1, EntityEquipmentSlot.HEAD);
        setRegistryName("goggles");
        setUnlocalizedName("create.goggles");
        setMaxStackSize(1);
        setMaxDamage(0);

        setCreativeTab(CreateLegacy.TAB_KINETICS);
    }

    @Override
    public boolean isDamageable() {
        return false;
    }

    @Override
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
        return false;
    }

    @Nullable
    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
        return "create:textures/armor/goggles.png";
    }

    @Override
    public int getItemEnchantability() {
        return 0;
    }

    @Override
    public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack) {
        if (!world.isRemote && player instanceof EntityPlayerMP) {
            RayTraceResult result = player.rayTrace(4, 0);
            if (result != null && result.typeOfHit == RayTraceResult.Type.BLOCK) {
                BlockPos pos = result.getBlockPos();
                TileEntity te = world.getTileEntity(pos);
                if (te instanceof TileEntitySpeedometer) {
                    Advancement advancement = ServerHack.getServer().getAdvancementManager()
                            .getAdvancement(new ResourceLocation("create", "goggles_speed"));
                    AdvancementUtil.grantAchievement((EntityPlayerMP) player, advancement);
                } else if (te instanceof TileEntityStressometer) {
                    Advancement advancement = ServerHack.getServer().getAdvancementManager()
                            .getAdvancement(new ResourceLocation("create", "goggles_stress"));
                    AdvancementUtil.grantAchievement((EntityPlayerMP) player, advancement);
                }
            }
        }
    }
}
