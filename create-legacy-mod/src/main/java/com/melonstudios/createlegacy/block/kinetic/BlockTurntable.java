package com.melonstudios.createlegacy.block.kinetic;

import com.melonstudios.createapi.CreateAPI;
import com.melonstudios.createlegacy.block.IGoggleInfo;
import com.melonstudios.createlegacy.tileentity.TileEntityTurntable;
import com.melonstudios.melonlib.misc.AABB;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class BlockTurntable extends AbstractBlockKinetic implements IGoggleInfo {
    public BlockTurntable() {
        super("turntable");
    }

    @Override
    public NonNullList<String> getGoggleInformation(World world, BlockPos pos, IBlockState state) {
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof TileEntityTurntable) {
            TileEntityTurntable turntable = (TileEntityTurntable) te;
            if (turntable.speed() != 0) return NonNullList.from("", turntable.stressGoggleInfo());
        }
        return IGoggleInfo.EMPTY;
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityTurntable();
    }

    private static final AxisAlignedBB aabb = AABB.create(0, 0, 0, 16, 8 ,16);

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return aabb;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced) {
        super.addInformation(stack, player, tooltip, advanced);
        tooltip.add(CreateAPI.stressImpactTooltip(1));
        tooltip.add("");
        tooltip.add(CreateAPI.translateToLocal("tile.create.turntable.desc"));
    }
}
