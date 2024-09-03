package com.siepert.createlegacy.tileentity;

import com.siepert.createapi.network.IKineticTE;
import com.siepert.createapi.network.KineticBlockInstance;
import com.siepert.createapi.network.NetworkContext;
import com.siepert.createlegacy.CreateLegacyConfigHolder;
import com.siepert.createlegacy.blocks.kinetic.BlockSaw;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.List;

public class TileEntitySaw extends TileEntity implements IKineticTE {
    @Override
    public double getStressImpact() {
        return CreateLegacyConfigHolder.kineticConfig.mechanicalSawStressImpact;
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

    private boolean isBlockALog(Block theBlock) {
        if (theBlock == Blocks.LOG  || theBlock == Blocks.LOG2) return true;
        if (OreDictionary.getOres("logWood").contains(new ItemStack(theBlock))) return true;
        return OreDictionary.getOres("log").contains(new ItemStack(theBlock));
    }

    private boolean isBlockALeaf(Block theBlock) {
        if (theBlock == Blocks.LEAVES  || theBlock == Blocks.LEAVES2) return true;
        return OreDictionary.getOres("treeLeaves").contains(new ItemStack(theBlock));
    }

    private void extendTreeMap(World worldIn, BlockPos pos, List<BlockPos> treeMap, EnumFacing source) {
        for (EnumFacing facing : EnumFacing.values()) {
            if (facing != source && !treeMap.contains(pos.offset(facing))) {
                Block blockNow = worldIn.getBlockState(pos.offset(facing)).getBlock();
                if (isBlockALog(blockNow)) {
                    treeMap.add(pos.offset(facing));
                    extendTreeMap(worldIn, pos.offset(facing), treeMap, facing.getOpposite());
                }
            }
        }
    }

    @Override
    public void kineticTick(NetworkContext context) {
        lastKineticTick = world.getTotalWorldTime();
        lastSpeed = context.networkSpeed;

        EnumFacing source = world.getBlockState(pos).getValue(BlockSaw.FACING).getOpposite();
        BlockPos newPos = new BlockPos(pos.offset(source.getOpposite()));

        if (world.getTotalWorldTime() % 20 == 3) {
            List<BlockPos> treeMap = new ArrayList<>();

            if (isBlockALog(world.getBlockState(newPos).getBlock())) {
                treeMap.add(pos.offset(source.getOpposite()));
                extendTreeMap(world, pos.offset(source.getOpposite()), treeMap, source.getOpposite());
            }

            for (BlockPos thePos : treeMap) {
                world.getBlockState(thePos).getBlock().dropBlockAsItem(world, thePos, world.getBlockState(thePos), 0);
                world.playEvent(2001, thePos, Block.getStateId(world.getBlockState(thePos)));
                world.setBlockState(thePos, Blocks.AIR.getDefaultState(), 0);
            }
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



    long lastKineticTick = 0;
    int lastSpeed = 0;

    @Override
    public int getRS() {
        return world.getTotalWorldTime() == lastKineticTick + 1 ? lastSpeed : 0;
    }

    @Override
    public void setUpdated() {

    }

    @Override
    public void passNetwork(NetworkContext context, EnumFacing source, boolean srcIsCog, boolean srcCogIsHorizontal, boolean inverted) {
        if (srcIsCog) return;

        IBlockState state = world.getBlockState(pos);

        if (source == state.getValue(BlockSaw.FACING).getOpposite()) {
            context.addKineticBlockInstance(new KineticBlockInstance(pos, inverted));
        }
    }
}
