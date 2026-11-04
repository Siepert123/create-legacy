package nl.melonstudios.create.tileentity.marker;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public interface IDepot {
    ItemStack getPresentedItem();
    void setPresentedItem(ItemStack stack);
    void decreasePresentedAndAddOutput(ItemStack output);
    double getItemHeight();
    boolean isWool();
    ItemStack takePresented(int count);
    default boolean allowTaking() {
        return true;
    }

    @Nullable
    static IDepot get(World world, BlockPos pos) {
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof IDepot) return (IDepot) te;
        return null;
    }
}
