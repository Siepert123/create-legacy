package com.siepert.createlegacy.tileentity;

import com.mojang.authlib.GameProfile;
import com.siepert.createapi.network.IKineticTE;
import com.siepert.createapi.network.KineticBlockInstance;
import com.siepert.createapi.network.NetworkContext;
import com.siepert.createlegacy.CreateLegacy;
import com.siepert.createlegacy.CreateLegacyConfigHolder;
import com.siepert.createlegacy.tabs.DeployerPlayerSim;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

import java.util.UUID;

import static com.siepert.createlegacy.blocks.kinetic.BlockDeployer.EXTENDED;
import static com.siepert.createlegacy.blocks.kinetic.BlockDeployer.FACING;

public class TileEntityDeployer extends TileEntity implements IKineticTE, ISidedInventory {
    int cooldown = 0;

    ItemStack useStack;
    ItemStack trashStack;

    EntityPlayer player;

    public void setPlacer(EntityPlayer placer) {
        this.player = placer;
    }

    public TileEntityDeployer() {
        useStack = ItemStack.EMPTY;
        trashStack = ItemStack.EMPTY;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);

        compound.setInteger("Cooldown", cooldown);

        if (player != null) {
            compound.setString("PlacerUUID", player.getGameProfile().getId().toString());
        }

        if (!useStack.isEmpty()) {
            NBTTagCompound nbt = new NBTTagCompound();
            useStack.writeToNBT(nbt);
            compound.setTag("UseStack", nbt);
        }
        if (!trashStack.isEmpty()) {
            NBTTagCompound nbt = new NBTTagCompound();
            trashStack.writeToNBT(nbt);
            compound.setTag("TrashStack", nbt);
        }

        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);

        cooldown = compound.getInteger("Cooldown");

        if (compound.hasKey("PlacerUUID")) {
            try {
                EntityPlayer thing = new EntityPlayer(world, new GameProfile(UUID.fromString(compound.getString("PlacerUUID")), "placer")) {
                    @Override
                    public boolean isSpectator() {
                        return false;
                    }

                    @Override
                    public boolean isCreative() {
                        return false;
                    }
                };

                player = new DeployerPlayerSim(thing);
                ((DeployerPlayerSim) player).setData(world.getBlockState(pos).getValue(FACING).toVanillaFacing());
            } catch (NullPointerException nullpointer) {
                CreateLegacy.logger.error("Could not load placer in deployer at {} {} {}", pos.getX(), pos.getY(), pos.getZ());
            }
        }

        if (compound.hasKey("UseStack")) {
            useStack = new ItemStack(compound.getCompoundTag("UseStack"));
        } else trashStack = ItemStack.EMPTY;

        if (compound.hasKey("TrashStack")) {
            trashStack = new ItemStack(compound.getCompoundTag("TrashStack"));
        } else trashStack = ItemStack.EMPTY;
    }

    @Override
    public double getStressImpact() {
        return CreateLegacyConfigHolder.kineticConfig.deployerStressImpact;
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
        if (context.networkSpeed == 0) return;

        EnumFacing facing = world.getBlockState(pos).getValue(FACING).toVanillaFacing();

        BlockPos actPos = pos.offset(facing, 2);

        boolean mayPlace = world.getBlockState(actPos).getMaterial().isReplaceable() && useStack.getItem() instanceof ItemBlock;
        boolean halted = false;
        for (EnumFacing facing1 : EnumFacing.VALUES) {
            if (world.getRedstonePower(pos, facing1) > 0) halted = true;
        }

        if (!halted) {
            if (cooldown > 100) {
                try {
                    if (mayPlace) {
                        IBlockState place = ((ItemBlock) useStack.getItem()).getBlock()
                                .getStateForPlacement(world, actPos, facing.getOpposite(), 0.5f, 0.5f, 0.5f, useStack.getMetadata(),
                                        player, EnumHand.MAIN_HAND);

                        world.setBlockState(actPos, place, 3);
                        world.playEvent(2001, actPos, Block.getStateId(place));
                        useStack.shrink(1);
                    } else {
                        boolean pass = false;
                        try {
                            if (!world.getBlockState(actPos).getBlock().onBlockActivated(
                                    world, actPos, world.getBlockState(actPos), player, EnumHand.MAIN_HAND,
                                    facing.getOpposite(), 0.5f, 0.5f, 0.5f
                            )) {
                                pass = true;
                                if (!(useStack.getItem() instanceof ItemBlock))
                                    useStack.getItem().onItemUse(player, world, actPos, EnumHand.MAIN_HAND, facing.getOpposite(),
                                        0.5f, 0.5f, 0.5f);
                            }
                        } catch (NullPointerException e) {
                            if (!pass && !(useStack.getItem() instanceof ItemBlock)) {
                                useStack.getItem().onItemUse(player, world, actPos, EnumHand.MAIN_HAND, facing.getOpposite(),
                                        0.5f, 0.5f, 0.5f);
                            }
                        }
                    }
                } catch (NullPointerException ignored) {

                }
                IBlockState state = world.getBlockState(pos);

                if (!state.getValue(EXTENDED) || !CreateLegacyConfigHolder.otherConfig.enableBlockstatePerformance) {
                    TileEntity entity = world.getTileEntity(pos);

                    world.setBlockState(pos, state.withProperty(EXTENDED, true), 3);

                    if (entity != null) {
                        entity.validate();
                        world.setTileEntity(pos, entity);
                    }
                }
                cooldown = 0;
            } else {
                IBlockState state = world.getBlockState(pos);

                if (cooldown < 97 && cooldown > 2) {
                    if (state.getValue(EXTENDED) || !CreateLegacyConfigHolder.otherConfig.enableBlockstatePerformance) {
                        TileEntity entity = world.getTileEntity(pos);

                        world.setBlockState(pos, state.withProperty(EXTENDED, false), 3);

                        if (entity != null) {
                            entity.validate();
                            world.setTileEntity(pos, entity);
                        }
                    }
                } else {
                    if (!state.getValue(EXTENDED) || !CreateLegacyConfigHolder.otherConfig.enableBlockstatePerformance) {
                        TileEntity entity = world.getTileEntity(pos);

                        world.setBlockState(pos, state.withProperty(EXTENDED, true), 3);

                        if (entity != null) {
                            entity.validate();
                            world.setTileEntity(pos, entity);
                        }
                    }
                }
            }
        }

        if (!halted) cooldown += Math.max(context.networkSpeed / 16, 1);
    }

    @Override
    public void setUpdated() {

    }

    @Override
    public void passNetwork(NetworkContext context, EnumFacing source, boolean srcIsCog, boolean srcCogIsHorizontal, boolean inverted) {
        IBlockState state = world.getBlockState(pos);

        if (srcIsCog) return;

        if (source.getAxis() == state.getValue(FACING).toVanillaFacing().rotateAround(EnumFacing.Axis.Y).getAxis()) {
            context.addKineticBlockInstance(new KineticBlockInstance(pos, inverted));

            TileEntity entity = world.getTileEntity(pos.offset(source.getOpposite()));

            if (entity instanceof IKineticTE) {
                ((IKineticTE) entity).passNetwork(context, source, false, false, inverted);
            }
        }
    }

    @Override
    public int[] getSlotsForFace(EnumFacing side) {
        if (side == EnumFacing.DOWN) return new int[]{1};
        return new int[]{0};
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
        if (index == 1) return false;
        return direction != EnumFacing.DOWN;
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
        if (index == 0) return false;
        return direction == EnumFacing.DOWN;
    }

    @Override
    public int getSizeInventory() {
        return 2;
    }

    @Override
    public boolean isEmpty() {
        if (!useStack.isEmpty()) return false;
        return trashStack.isEmpty();
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        if (index == 0) return useStack;
        if (index == 1) return trashStack;
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        return getStackInSlot(index).splitStack(count);
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        ItemStack toReturn;
        if (index > 1) return ItemStack.EMPTY;
        if (index == 0) {
            toReturn = useStack.copy();
            useStack = ItemStack.EMPTY;
        } else {
            toReturn = trashStack.copy();
            trashStack = ItemStack.EMPTY;
        }
        return toReturn;
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        if (index == 0) {
            useStack = stack;
        }
        if (index == 1) {
            trashStack = stack;
        }
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer player) {
        return false;
    }

    @Override
    public void openInventory(EntityPlayer player) {

    }

    @Override
    public void closeInventory(EntityPlayer player) {

    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return index == 0;
    }

    @Override
    public int getField(int id) {
        return 0;
    }

    @Override
    public void setField(int id, int value) {

    }

    @Override
    public int getFieldCount() {
        return 0;
    }

    @Override
    public void clear() {
        useStack = ItemStack.EMPTY;
        trashStack = ItemStack.EMPTY;
    }

    @Override
    public String getName() {
        return "Deployer";
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }
}
