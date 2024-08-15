package com.siepert.createlegacy.tileentity;

import com.mojang.authlib.GameProfile;
import com.siepert.createapi.network.IKineticTE;
import com.siepert.createapi.network.KineticBlockInstance;
import com.siepert.createapi.network.NetworkContext;
import com.siepert.createlegacy.CreateLegacyConfigHolder;
import com.siepert.createlegacy.blocks.kinetic.BlockDrill;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

import java.util.List;

import static com.siepert.createlegacy.blocks.kinetic.BlockDrill.FACING;

public class TileEntityDrill extends TileEntity implements IKineticTE {

    int breakingProgress = 0;
    int requiredBreakingProgress = 0;

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);

        breakingProgress = compound.getInteger("BreakingProgress");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);

        compound.setInteger("BreakingProgress", breakingProgress);

        return compound;
    }

    @Override
    public double getStressImpact() {
        return CreateLegacyConfigHolder.kineticConfig.mechanicalDrillStressImpact;
    }

    @Override
    public boolean isConsumer() {
        return true;
    }

    @Override
    public double getStressCapacity() {
        return 0;
    }

    @Override
    public int getProducedSpeed() {
        return 0;
    }

    @Override
    public void kineticTick(NetworkContext context) {
        IBlockState state = world.getBlockState(pos);

        EnumFacing source = state.getValue(FACING).getOpposite();

        BlockPos newPos = new BlockPos(pos.offset(source.getOpposite()));
        if (!world.getBlockState(pos.offset(source.getOpposite())).getMaterial().isReplaceable()
                && world.getBlockState(pos.offset(source.getOpposite())).getBlockHardness(world, newPos) != -1.0f) {
            requiredBreakingProgress = (int) (world.getBlockState(newPos).getBlockHardness(world, newPos) * 25);

            if (breakingProgress >= requiredBreakingProgress) {
                world.getBlockState(newPos).getBlock().dropBlockAsItem(world, newPos, world.getBlockState(newPos), 0);
                try {
                    world.getBlockState(newPos).getBlock().onBlockHarvested(world, newPos, world.getBlockState(newPos),
                            world.getClosestPlayer(pos.getX(), pos.getY(), pos.getZ(), 128, false));
                } catch (NullPointerException ignored) {

                }
                world.playEvent(2001, newPos, Block.getStateId(world.getBlockState(newPos)));
                world.setBlockState(newPos, Blocks.AIR.getDefaultState());
                breakingProgress = 0;
            } else {
                breakingProgress += Math.max(context.networkSpeed / 16, 1);
            }
        } else {
            breakingProgress = 0;
        }

        if (world.getTotalWorldTime() % 10 == 0 && !world.isRemote) {
            AxisAlignedBB axisAlignedBB = new AxisAlignedBB(newPos);

            List<EntityLivingBase> entities = world.getEntitiesWithinAABB(EntityLivingBase.class, axisAlignedBB);

            for (EntityLivingBase entity : entities) {
                if (entity instanceof EntityPlayer) {
                    if (!((EntityPlayer) entity).isCreative()) {
                        entity.setHealth(entity.getHealth() - (float) context.networkSpeed / 32);
                        entity.performHurtAnimation();
                        world.playSound(null,
                                entity.posX, entity.posY, entity.posZ,
                                SoundEvents.ENTITY_PLAYER_HURT,
                                entity.getSoundCategory(), 1.0f, 1.0f);
                    }
                } else {
                    entity.setHealth(entity.getHealth() - (float) context.networkSpeed / 32);
                    entity.performHurtAnimation();
                    world.playSound(null,
                            entity.posX, entity.posY, entity.posZ,
                            SoundEvents.ENTITY_GENERIC_HURT,
                            entity.getSoundCategory(), 1.0f, 1.0f);
                }
            }
        }
    }

    @Override
    public void setUpdated() {

    }

    @Override
    public void passNetwork(NetworkContext context, EnumFacing source, boolean srcIsCog, boolean srcCogIsHorizontal, boolean inverted) {
        if (srcIsCog) return;

        IBlockState state = world.getBlockState(pos);

        if (source == state.getValue(FACING).getOpposite()) {
            context.addKineticBlockInstance(new KineticBlockInstance(pos, inverted));
        }
    }
}
