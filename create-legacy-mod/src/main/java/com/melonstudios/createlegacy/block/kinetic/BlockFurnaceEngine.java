package com.melonstudios.createlegacy.block.kinetic;

import com.melonstudios.createapi.CreateAPI;
import com.melonstudios.createlegacy.block.IGoggleInfo;
import com.melonstudios.createlegacy.block.IWrenchable;
import com.melonstudios.createlegacy.tileentity.TileEntityFlywheel;
import com.melonstudios.createlegacy.util.IMetaName;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class BlockFurnaceEngine extends AbstractBlockKinetic implements IMetaName, IWrenchable, IGoggleInfo {
    public BlockFurnaceEngine() {
        super("furnace_engine");

        setSoundType(SoundType.METAL);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced) {
        super.addInformation(stack, player, tooltip, advanced);
        tooltip.add(CreateAPI.stressCapacityTooltip(64));
    }

    @Override
    public NonNullList<String> getGoggleInformation(World world, BlockPos pos, IBlockState state) {
        if (state.getValue(VARIANT) == Variant.ENGINE) return IGoggleInfo.EMPTY;
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof TileEntityFlywheel) {
            TileEntityFlywheel flywheel = (TileEntityFlywheel) te;
            if (flywheel.generatedRPM() != 0) return NonNullList.from("", flywheel.capacityGoggleInfo());
        }
        return IGoggleInfo.EMPTY;
    }

    @Override
    public boolean onWrenched(World world, BlockPos pos, IBlockState state, EnumFacing side, EntityPlayer wrenchHolder) {
        if (EnumFacing.Axis.Y.apply(side)) {
            world.setBlockState(pos, state.withProperty(FACING, state.getValue(FACING).rotateY()));
        }
        return false;
    }

    public enum Variant implements IStringSerializable {
        ENGINE, FLYWHEEL;

        public int getId() {
            return ordinal();
        }
        public static Variant fromId(int id) {
            return values()[id % 2];
        }

        @Override
        public String getName() {
            return this == ENGINE ? "engine" : "flywheel";
        }
    }
    public static final PropertyEnum<EnumFacing> FACING = PropertyEnum.create("facing", EnumFacing.class,
            EnumFacing.NORTH, EnumFacing.EAST, EnumFacing.SOUTH, EnumFacing.WEST);
    public static final PropertyEnum<Variant> VARIANT = PropertyEnum.create("variant", Variant.class);

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING, VARIANT);
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        return new ItemStack(this, 1, state.getValue(VARIANT).getId());
    }

    @Override
    public int damageDropped(IBlockState state) {
        return state.getValue(VARIANT).getId();
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        return getDefaultState()
                .withProperty(FACING, placer.getHorizontalFacing().getOpposite())
                .withProperty(VARIANT, Variant.fromId(placer.getHeldItem(hand).getMetadata()));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        switch (state.getValue(VARIANT)) {
            case ENGINE:
                return state.getValue(FACING).getHorizontalIndex();
            case FLYWHEEL:
                return state.getValue(FACING).getHorizontalIndex() + 4;
        }
        return 0;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta % 4)).withProperty(VARIANT, Variant.fromId(meta / 4));
    }

    @Override
    public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items) {
        items.add(new ItemStack(this, 1, 0));
        items.add(new ItemStack(this, 1, 1));
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return meta > 3 ? new TileEntityFlywheel() : null;
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return stack.getMetadata() == 0 ? "tile.create.furnace_engine" : "tile.create.flywheel";
    }

    @Override
    public Material getMaterial(IBlockState state) {
        return Material.IRON;
    }
}
