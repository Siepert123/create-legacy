package com.melonstudios.createlegacy.tileentity;

import com.melonstudios.createapi.kinetic.INeedsRecalculating;
import com.melonstudios.createlegacy.CreateLegacy;
import com.melonstudios.createlegacy.block.BlockRender;
import com.melonstudios.createlegacy.block.ModBlocks;
import com.melonstudios.createlegacy.block.kinetic.BlockDrill;
import com.melonstudios.createlegacy.tileentity.abstractions.AbstractTileEntityKinetic;
import com.melonstudios.createlegacy.util.AdvancementUtil;
import com.melonstudios.createlegacy.util.EnumKineticConnectionType;
import com.melonstudios.createlegacy.util.registries.ModDamageSources;
import com.melonstudios.melonlib.misc.ServerHack;
import net.minecraft.advancements.Advancement;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;

import java.util.List;

public class TileEntityDrill extends AbstractTileEntityKinetic implements INeedsRecalculating {
    @Override
    protected String namePlate() {
        return "Drill";
    }

    protected final EnumFacing facing() {
        return getState().getValue(BlockDrill.FACING);
    }

    @Override
    public EnumKineticConnectionType getConnectionType(EnumFacing side) {
        return facing().getOpposite() == side ? connection(1) : connection(0);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);

        compound.setFloat("progress", drillingProgress);
        compound.setFloat("maxProgress", maxDrillingProgress);

        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);

        drillingProgress = compound.getFloat("progress");
        maxDrillingProgress = compound.getFloat("maxProgress");
    }

    @Override
    protected void onFirstActivation() {
        if (!world.isRemote) {
            Advancement advancement = ServerHack.getServer().getAdvancementManager()
                    .getAdvancement(new ResourceLocation("create", "machines/drill"));
            List<EntityPlayerMP> players = world.getEntities(EntityPlayerMP.class, (player) -> player.getDistanceSq(pos) < 256);
            for (EntityPlayerMP player : players) {
                AdvancementUtil.grantAchievement(player, advancement);
            }
        }
    }

    public IBlockState getAssociatedDrillPart() {
        IBlockState render = ModBlocks.RENDER.getDefaultState();
        switch (facing()) {
            case UP: return render.withProperty(BlockRender.TYPE, BlockRender.Type.DRILL_U);
            case DOWN: return render.withProperty(BlockRender.TYPE, BlockRender.Type.DRILL_D);
            case NORTH: return render.withProperty(BlockRender.TYPE, BlockRender.Type.DRILL_N);
            case EAST: return render.withProperty(BlockRender.TYPE, BlockRender.Type.DRILL_E);
            case SOUTH: return render.withProperty(BlockRender.TYPE, BlockRender.Type.DRILL_S);
            case WEST: return render.withProperty(BlockRender.TYPE, BlockRender.Type.DRILL_W);
        }
        return render;
    }

    protected float maxDrillingProgress = 0;
    protected float drillingProgress = 0;
    protected IBlockState drilling = Blocks.AIR.getDefaultState();

    @Override
    protected void tick() {
        IBlockState front = world.getBlockState(pos.offset(facing()));
        if (front != drilling) {
            drillingProgress = 0;
            drilling = front;
            maxDrillingProgress = drilling.getBlockHardness(world, pos.offset(facing()));
        }

        if (maxDrillingProgress < 0 || !drilling.getMaterial().blocksMovement()) drillingProgress = 0;

        if (drilling.getMaterial().blocksMovement() && world.getTotalWorldTime() % 10 == 0 && speed() != 0)
            world.playSound(null, pos.offset(facing()),
                drilling.getBlock().getSoundType(drilling, world, pos.offset(facing()), null).getHitSound(),
                SoundCategory.BLOCKS, 1.0f, 1.0f);

        if (drillingProgress > maxDrillingProgress && maxDrillingProgress > 0) {
            world.playEvent(2001, pos.offset(facing()), Block.getStateId(drilling));
            drilling.getBlock().dropBlockAsItem(world, pos.offset(facing()), drilling, 0);
            world.setBlockToAir(pos.offset(facing()));
            drillingProgress = 0;
            maxDrillingProgress = 0;
        }

        drillingProgress += Math.abs(speed()) / (64f * 20f);

        List<EntityLivingBase> livingBases = world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(pos.offset(facing())));

        for (EntityLivingBase entityLivingBase : livingBases) {
            if (entityLivingBase.hurtResistantTime <= 0) entityLivingBase.attackEntityFrom(ModDamageSources.DRILLING, calculateDamage());
        }
    }

    protected final float calculateDamage() {
        return Math.abs(speed()) / 16f;
    }

    @Override
    public void onLoad() {
        super.onLoad();
        recalculate();
    }

    @Override
    public float consumedStressMarkiplier() {
        return 6.0f;
    }

    @Override
    public void recalculate() {
        drilling = world.getBlockState(pos.offset(facing()));
        maxDrillingProgress = drilling.getBlockHardness(world, pos.offset(facing()));
    }
}
