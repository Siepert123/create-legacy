package com.melonstudios.createlegacy.block.kinetic;

import com.melonstudios.createlegacy.block.IWrenchable;
import com.melonstudios.createlegacy.block.ModBlocks;
import com.melonstudios.createlegacy.tileentity.TileEntityShaft;
import com.melonstudios.melonlib.item.IMetaName;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
@SuppressWarnings("deprecation")
public class BlockEncasedShaft extends AbstractBlockKinetic implements IMetaName, IWrenchable {
    public BlockEncasedShaft() {
        super("encased_shaft");
    }

    public static final PropertyBool BRASS = PropertyBool.create("brass");
    public static final PropertyEnum<EnumFacing.Axis> AXIS = BlockRotator.AXIS;

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, AXIS, BRASS);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return (state.getValue(AXIS).ordinal()) + (state.getValue(BRASS) ? 3 : 0);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(AXIS, EnumFacing.Axis.values()[meta % 3])
                .withProperty(BRASS, meta / 3 == 1);
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target,
                                  World world, BlockPos pos, EntityPlayer player) {
        return new ItemStack(this, 1, state.getValue(BRASS) ? 1 : 0);
    }

    @Override
    public int damageDropped(IBlockState state) {
        return state.getValue(BRASS) ? 1 : 0;
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing,
                                            float hitX, float hitY, float hitZ, int meta,
                                            EntityLivingBase placer, EnumHand hand) {
        return getDefaultState().withProperty(BRASS, meta != 0)
                .withProperty(AXIS, facing.getAxis());
    }

    @Override
    public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items) {
        items.add(new ItemStack(this, 1, 0));
        items.add(new ItemStack(this, 1, 1));
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return stack.getMetadata() == 0 ? "tile.create.shaft_encased_andesite" : "tile.create.shaft_encased_brass";
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityShaft();
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    public boolean onWrenched(World world, BlockPos pos, IBlockState state, EnumFacing side, EntityPlayer wrenchHolder) {
        EnumFacing.Axis axis = state.getValue(AXIS);
        boolean brass = state.getValue(BRASS);

        world.setBlockState(pos, ModBlocks.ROTATOR.getDefaultState()
                .withProperty(BlockRotator.VARIANT, BlockRotator.Variant.SHAFT)
                .withProperty(BlockRotator.AXIS, axis));
        if(!world.isRemote) {
            EntityItem item = new EntityItem(world, wrenchHolder.posX, wrenchHolder.posY, wrenchHolder.posZ,
                    new ItemStack(ModBlocks.CASING, 1, brass ? 2 : 0));
            item.motionX = item.motionY = item.motionZ = 0;
            world.spawnEntity(item);
        }

        return true;
    }
}
