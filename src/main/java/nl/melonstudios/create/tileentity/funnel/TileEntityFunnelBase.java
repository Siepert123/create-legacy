package nl.melonstudios.create.tileentity.funnel;

import com.melonstudios.melonlib.tileentity.TileEntityCachedRenderBB;
import com.melonstudios.melonlib.tileentity.TileEntityOptimizedBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import nl.melonstudios.create.util.interfaces.IStateFindable;

import javax.annotation.Nullable;

@SuppressWarnings("deprecation")
public abstract class TileEntityFunnelBase extends TileEntityOptimizedBase implements IStateFindable {
    public TileEntityFunnelBase() {
        super();
    }

    @Override
    public IBlockState getState() {
        return this.getBlockType().getStateFromMeta(this.getBlockMetadata());
    }
    @Nullable
    public IItemHandler getInventory(EnumFacing side) {
        BlockPos off = this.pos.offset(side);
        TileEntity te = this.world.getTileEntity(off);
        EnumFacing opp = side.getOpposite();
        if (te != null && te.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, opp)) {
            return te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, opp);
        }
        return null;
    }

    protected abstract boolean isPowered(int meta);
    public boolean isPowered() {
        return this.isPowered(this.getBlockMetadata());
    }

    @Override
    public void tickLazy() {}

    public ItemStack insertIntoFunnel(ItemStack stack, boolean limit) {
        return stack;
    }
    public boolean allowsInsertionFromBelt() {
        return false;
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate) {
        return !oldState.getBlock().isAssociatedBlock(newSate.getBlock());
    }
}
