package com.melonstudios.createlegacy.block.redstone;

import com.melonstudios.createlegacy.CreateLegacy;
import com.melonstudios.createlegacy.block.ModBlocks;
import net.minecraft.block.BlockRedstoneDiode;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;

public abstract class AbstractBlockRedstoneCircuit extends BlockRedstoneDiode {
    protected AbstractBlockRedstoneCircuit(boolean powered, String registry) {
        super(powered);

        setRegistryName(powered ? "powered_" + registry : registry);
        setUnlocalizedName("create." + registry);

        setHardness(0.5f);
        setResistance(2.0f);

        setCreativeTab(CreateLegacy.TAB_KINETICS);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(FACING).getHorizontalIndex();
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(FACING, EnumFacing.HORIZONTALS[meta % 4]);
    }

    public static void models() {
        CreateLegacy.setItemModel(Item.getItemFromBlock(ModBlocks.PULSE_EXTENDER));
    }
}
