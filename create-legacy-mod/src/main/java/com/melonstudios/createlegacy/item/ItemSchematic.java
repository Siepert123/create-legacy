package com.melonstudios.createlegacy.item;

import com.melonstudios.createlegacy.CreateConfig;
import com.melonstudios.createlegacy.CreateLegacy;
import com.melonstudios.createlegacy.event.SchematicPlacementEvent;
import com.melonstudios.createlegacy.schematic.InvalidSchematicSizeException;
import com.melonstudios.createlegacy.schematic.SchematicEncodingSystem;
import com.melonstudios.createlegacy.schematic.SchematicSaveHelper;
import com.melonstudios.createlegacy.util.DisplayLink;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@ParametersAreNonnullByDefault
public class ItemSchematic extends Item {
    public ItemSchematic() {
        super();

        setRegistryName("schematic");
        setUnlocalizedName("create.schematic");

        setMaxDamage(0);
        setMaxStackSize(1);
        setHasSubtypes(true);

        setCreativeTab(CreateLegacy.TAB_KINETICS);
    }

    @Override
    @Nonnull
    public String getUnlocalizedName(ItemStack stack) { // very long code!!
        return stack.getMetadata() == 0 ? "item.create.schematic_empty" : stack.getMetadata() == 1 ? "item.create.schematic_writeable" : stack.getMetadata() == 2 ? "item.create.schematic" : "null";
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (this.isInCreativeTab(tab)) {
            items.add(new ItemStack(this, 1, 0));
            items.add(new ItemStack(this, 1, 1));
        }
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        if (stack.getMetadata() == 1) {
            if (stack.getTagCompound() != null) {
                if (stack.getTagCompound().hasKey("posNBT")) {
                    NBTTagCompound nbt = stack.getTagCompound().getCompoundTag("posNBT");
                    tooltip.add(String.format("Start pos: %s, %s, %s",
                            nbt.getInteger("startX"),
                            nbt.getInteger("startY"),
                            nbt.getInteger("startZ")));
                } else {
                    tooltip.add("Current mode: COPY");
                    tooltip.add("File mode is WIP!!");
                }
            } else {
                tooltip.add("Current mode: COPY");
                tooltip.add("File mode is WIP!!");
            }
        }
        if (stack.getMetadata() == 2) {
            if (stack.getTagCompound() == null) tooltip.add("Invalid schematic!");
        }
    }

    @Override
    @Nonnull
    public final EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack stack = player.getHeldItem(hand);
        if (stack.getMetadata() == 0) return EnumActionResult.PASS;

        if (stack.getTagCompound() == null) stack.setTagCompound(new NBTTagCompound()); //prevent error

        if (stack.getMetadata() == 1) handleWrite(player, world, pos, hand, stack);
        if (stack.getMetadata() == 2) handlePlacement(player, world, pos, hand, stack, facing);

        return EnumActionResult.SUCCESS;
    }

    protected void handleWrite(EntityPlayer player, World world, BlockPos pos, EnumHand hand, ItemStack stack) {
        NBTTagCompound nbt = stack.getTagCompound();

        assert nbt != null;
        boolean finish = nbt.hasKey("posNBT");

        if (!finish) {
            NBTTagCompound posNBT = new NBTTagCompound();

            posNBT.setInteger("startX", pos.getX());
            posNBT.setInteger("startY", pos.getY());
            posNBT.setInteger("startZ", pos.getZ());

            nbt.setTag("posNBT", posNBT);
        } else {
            NBTTagCompound posNBT = nbt.getCompoundTag("posNBT");

            int startX = posNBT.getInteger("startX");
            int startY = posNBT.getInteger("startY");
            int startZ = posNBT.getInteger("startZ");

            int endX = pos.getX();
            int endY = pos.getY();
            int endZ = pos.getZ();

            if (Math.abs(startX - endX) > 255 || Math.abs(startY - endY) > 255 || Math.abs(startZ - endZ) > 255) {
                nbt.removeTag("posNBT");
                player.sendStatusMessage(new TextComponentString("Schematic size too large!"), true);
                return;
            }

            posNBT.setInteger("startX", Math.min(startX, endX));
            posNBT.setInteger("startY", Math.min(startY, endY));
            posNBT.setInteger("startZ", Math.min(startZ, endZ));

            posNBT.setInteger("endX", Math.max(startX, endX));
            posNBT.setInteger("endY", Math.max(startY, endY));
            posNBT.setInteger("endZ", Math.max(startZ, endZ));

            save(stack, world);
            ItemStack newStack = new ItemStack(this, 1, 2);
            newStack.setTagCompound(nbt);
            player.setHeldItem(hand, newStack);
            if (world.isRemote) world.playSound(player, player.posX, player.posY, player.posZ,
                    SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, SoundCategory.NEUTRAL, 1.0f, 1.0f);
        }
    }
    protected void save(ItemStack stack, World world) {
        assert stack.getTagCompound() != null;
        NBTTagCompound nbt = stack.getTagCompound().getCompoundTag("posNBT");

        int sizeX = nbt.getInteger("endX") - nbt.getInteger("startX") + 1;
        int sizeY = nbt.getInteger("endY") - nbt.getInteger("startY") + 1;
        int sizeZ = nbt.getInteger("endZ") - nbt.getInteger("startZ") + 1;

        IBlockState[][][] structure = new IBlockState[sizeX][sizeY][sizeZ];

        for (int x = nbt.getInteger("startX"); x <= nbt.getInteger("endX"); x++) {
            for (int y = nbt.getInteger("startY"); y <= nbt.getInteger("endY"); y++) {
                for (int z = nbt.getInteger("startZ"); z <= nbt.getInteger("endZ"); z++) {
                    structure[x-nbt.getInteger("startX")][y-nbt.getInteger("startY")][z-nbt.getInteger("startZ")]
                            = world.getBlockState(new BlockPos(x, y, z));
                }
            }
        }
        try {
            byte[] structure_data = SchematicEncodingSystem.encode(structure);

            File file = new File(SchematicSaveHelper.schematicsDir(), "schematic_test.bin");
            stack.getTagCompound().setString("schematicFile", "schematic_test");

            FileOutputStream outputStream = new FileOutputStream(file);

            outputStream.write(structure_data);

            outputStream.close();
        } catch (InvalidSchematicSizeException e) {
            throw new RuntimeException("Could not save schematic!!", e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    protected void handlePlacement(EntityPlayer player, World world, BlockPos pos, EnumHand hand, ItemStack stack, EnumFacing facing) {
        if (stack.getTagCompound() == null || !CreateConfig.allowInstantSchematicPlacement) return;

        if (world.isRemote || !player.isCreative()) return;
        if (!player.isSneaking()) { //Place ON the block
            pos = pos.offset(facing);

            NBTTagCompound nbt = stack.getTagCompound().getCompoundTag("posNBT");

            int sizeX = nbt.getInteger("endX") - nbt.getInteger("startX") + 1;
            int sizeY = nbt.getInteger("endY") - nbt.getInteger("startY") + 1;
            int sizeZ = nbt.getInteger("endZ") - nbt.getInteger("startZ") + 1;

            IBlockState[][][] structure = new IBlockState[sizeX][sizeY][sizeZ];

            SchematicPlacementEvent event = new SchematicPlacementEvent(world, pos, structure, player);
            MinecraftForge.EVENT_BUS.post(event);

            if (event.isCanceled()) return;

            for (int x = nbt.getInteger("startX"); x <= nbt.getInteger("endX"); x++) {
                for (int y = nbt.getInteger("startY"); y <= nbt.getInteger("endY"); y++) {
                    for (int z = nbt.getInteger("startZ"); z <= nbt.getInteger("endZ"); z++) {
                        structure[x-nbt.getInteger("startX")][y-nbt.getInteger("startY")][z-nbt.getInteger("startZ")]
                                = world.getBlockState(new BlockPos(x, y, z));
                    }
                }
            }

            for (int x = 0; x < structure.length; x++) {
                for (int y = 0; y < structure[0].length; y++) {
                    for (int z = 0; z < structure[0][0].length; z++) {
                        world.setBlockState(pos.east(x).up(y).south(z), structure[x][y][z], 3);
                    }
                }
            }
        } else { //Place IN the block
            NBTTagCompound nbt = stack.getTagCompound().getCompoundTag("posNBT");

            int sizeX = nbt.getInteger("endX") - nbt.getInteger("startX") + 1;
            int sizeY = nbt.getInteger("endY") - nbt.getInteger("startY") + 1;
            int sizeZ = nbt.getInteger("endZ") - nbt.getInteger("startZ") + 1;

            IBlockState[][][] structure = new IBlockState[sizeX][sizeY][sizeZ];

            SchematicPlacementEvent event = new SchematicPlacementEvent(world, pos, structure, player);
            MinecraftForge.EVENT_BUS.post(event);

            if (event.isCanceled()) return;

            for (int x = nbt.getInteger("startX"); x <= nbt.getInteger("endX"); x++) {
                for (int y = nbt.getInteger("startY"); y <= nbt.getInteger("endY"); y++) {
                    for (int z = nbt.getInteger("startZ"); z <= nbt.getInteger("endZ"); z++) {
                        structure[x-nbt.getInteger("startX")][y-nbt.getInteger("startY")][z-nbt.getInteger("startZ")]
                                = world.getBlockState(new BlockPos(x, y, z));
                    }
                }
            }

            for (int x = 0; x < structure.length; x++) {
                for (int y = 0; y < structure[0].length; y++) {
                    for (int z = 0; z < structure[0][0].length; z++) {
                        world.setBlockState(pos.east(x).up(y).south(z), structure[x][y][z], 3);
                    }
                }
            }
        }
    }
}
