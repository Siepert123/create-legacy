package com.melonstudios.createlegacy.tileentity;

import com.melonstudios.createapi.network.NetworkContext;
import com.melonstudios.createlegacy.CreateLegacy;
import com.melonstudios.createlegacy.block.BlockRender;
import com.melonstudios.createlegacy.block.kinetic.BlockFurnaceEngine;
import com.melonstudios.createlegacy.tileentity.abstractions.AbstractTileEntityKinetic;
import com.melonstudios.createlegacy.util.AdvancementUtil;
import com.melonstudios.createlegacy.util.EnumKineticConnectionType;
import com.melonstudios.melonlib.blockdict.BlockDictionary;
import com.melonstudios.melonlib.misc.AABB;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.AdvancementCommand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;

import java.util.List;

public class TileEntityFlywheel extends AbstractTileEntityKinetic {
    @Override
    protected String namePlate() {
        return "Flywheel";
    }

    public IBlockState getAssociatedFlywheelPart() {
        switch (facing()) {
            case NORTH: return renderingPart(17);
            case EAST: return renderingPart(14);
            case SOUTH: return renderingPart(15);
            case WEST: return renderingPart(16);
        }
        return renderingPart(14);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);

        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
    }

    @Override
    public EnumKineticConnectionType getConnectionType(EnumFacing side) {
        return getState().getValue(BlockFurnaceEngine.FACING).getOpposite() == side ? connection(1) : connection(0);
    }

    @Override
    protected void onFirstActivation() {
        if (!world.isRemote) {
            Advancement advancement = CreateLegacy.serverHack.getAdvancementManager() //Replace with actual advancement
                    .getAdvancement(new ResourceLocation("minecraft", "adventure/kill_a_mob"));
            if (advancement != null) {
                List<EntityPlayer> players = world.getEntities(EntityPlayer.class, (player) -> player.getDistanceSq(pos) < 256);
                for (EntityPlayer player : players) {
                    if (player instanceof EntityPlayerMP) {
                        AdvancementUtil.grantAchievement((EntityPlayerMP) player, advancement);
                    }
                }
            }
        }
    }

    @Override
    protected void tick() {
        if ((world.getTotalWorldTime() & 1) == 0) updateConnection();
        if (generatedRPM() == 0 || isUpdated()) return;
        NetworkContext context = new NetworkContext(world);
        passNetwork(null, null, context, false);
        context.start();

        if (world.getTotalWorldTime() % 20 == 0) {
            world.playSound(null, pos.offset(facing().rotateY()).getX() + 0.5, pos.offset(facing().rotateY()).getY() + 0.5, pos.offset(facing().rotateY()).getZ() + 0.5,
                    SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.01f, 1.0f);
        }
        world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL,
                pos.offset(facing().rotateY(), 2).getX() + world.rand.nextFloat(),
                pos.offset(facing().rotateY(), 2).getY() + world.rand.nextFloat(),
                pos.offset(facing().rotateY(), 2).getZ() + world.rand.nextFloat(),
                0, 0.1f, 0);
    }

    @Override
    public float generatedSUMarkiplier() {
        return 256.0f;
    }

    protected EnumFacing facing() {
        return getState().getValue(BlockFurnaceEngine.FACING);
    }

    @Override
    public float generatedRPM() {
        return validGenerator() ? 64.0f : 0.0f;
    }

    protected boolean validGenerator() {
        IBlockState state = world.getBlockState(pos.offset(facing().rotateY(), 2));
        if (state.getBlock() instanceof BlockFurnaceEngine) {
            if (state.getValue(BlockFurnaceEngine.VARIANT) == BlockFurnaceEngine.Variant.ENGINE) {
                if (state.getValue(BlockFurnaceEngine.FACING) == facing().rotateYCCW()) {
                    return BlockDictionary.isBlockTagged(world.getBlockState(pos.offset(facing().rotateY(), 3)), "create:furnaceEnginePowering");
                }
            }
        }
        return false;
    }
    private boolean connection = false;
    public boolean connects() {
        return connection;
    }
    private void updateConnection() {
        IBlockState state = world.getBlockState(pos.offset(facing().rotateY(), 2));
        if (state.getBlock() instanceof BlockFurnaceEngine) {
            if (state.getValue(BlockFurnaceEngine.VARIANT) == BlockFurnaceEngine.Variant.ENGINE) {
                connection = state.getValue(BlockFurnaceEngine.FACING) == facing().rotateYCCW();
                return;
            }
        }
        connection = false;
    }

    private final IBlockState[] renderParts = new IBlockState[4];
    public IBlockState renderPart(int id) {
        if (renderParts[id] == null) {
            switch (id) {
                case 0: renderParts[id] = BlockRender.getRenderPart(BlockRender.Type.LOWER_ROTATING_CONNECTOR); break;
                case 1: renderParts[id] = BlockRender.getRenderPart(BlockRender.Type.LOWER_SLIDING_CONNECTOR); break;
                case 2: renderParts[id] = BlockRender.getRenderPart(BlockRender.Type.UPPER_ROTATING_CONNECTOR); break;
                case 3: renderParts[id] = BlockRender.getRenderPart(BlockRender.Type.UPPER_SLIDING_CONNECTOR); break;
            }
        }
        return renderParts[id];
    }

    public int getRotationDeg() {
        switch (facing()) {
            case NORTH: return 90;
            case EAST: return 0;
            case SOUTH: return -90;
            case WEST: return 180;
        }
        return 0;
    }
}
