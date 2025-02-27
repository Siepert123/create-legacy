package com.melonstudios.createlegacy.block.kinetic;

import com.melonstudios.createapi.CreateAPI;
import com.melonstudios.createlegacy.block.IGoggleInfo;
import com.melonstudios.createlegacy.tileentity.TileEntityMillstone;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class BlockMillstone extends AbstractBlockKinetic implements IGoggleInfo {
    public BlockMillstone() {
        super("millstone");
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced) {
        super.addInformation(stack, player, tooltip, advanced);
        tooltip.add(CreateAPI.stressImpactTooltip(8));
    }

    @Override
    public NonNullList<String> getGoggleInformation(World world, BlockPos pos, IBlockState state) {
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof TileEntityMillstone) {
            TileEntityMillstone millstone = (TileEntityMillstone) te;
            if (millstone.speed() != 0) return NonNullList.from("", millstone.stressGoggleInfo());
        }
        return IGoggleInfo.EMPTY;
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityMillstone();
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    protected static TileEntityMillstone getTE(World world, BlockPos pos) {
        return (TileEntityMillstone) world.getTileEntity(pos);
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        TileEntityMillstone millstone = getTE(worldIn, pos);
        millstone.drop();

        super.breakBlock(worldIn, pos, state);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        TileEntityMillstone millstone = getTE(worldIn, pos);

        if (playerIn.getHeldItem(hand).isEmpty()) {
            if (!worldIn.isRemote) {
                if (millstone.getStackInSlot(1).isEmpty()
                        && millstone.getStackInSlot(2).isEmpty()
                        && millstone.getStackInSlot(3).isEmpty()) {
                    worldIn.spawnEntity(new EntityItem(worldIn, playerIn.posX, playerIn.posY, playerIn.posZ, millstone.getStackInSlot(0)));
                    millstone.removeStackFromSlot(0);
                } else {
                    worldIn.spawnEntity(new EntityItem(worldIn, playerIn.posX, playerIn.posY, playerIn.posZ, millstone.getStackInSlot(1)));
                    worldIn.spawnEntity(new EntityItem(worldIn, playerIn.posX, playerIn.posY, playerIn.posZ, millstone.getStackInSlot(2)));
                    worldIn.spawnEntity(new EntityItem(worldIn, playerIn.posX, playerIn.posY, playerIn.posZ, millstone.getStackInSlot(3)));
                    millstone.removeStackFromSlot(1);
                    millstone.removeStackFromSlot(2);
                    millstone.removeStackFromSlot(3);
                }
            }
            return true;
        }
        return false;
    }
}
