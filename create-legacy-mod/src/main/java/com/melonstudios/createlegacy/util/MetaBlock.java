package com.melonstudios.createlegacy.util;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;

public class MetaBlock {
    private final Block block;
    private final int meta;
    public MetaBlock(Block block, int meta) {
        this.block = block;
        this.meta = meta;
    }
    public MetaBlock(IBlockState state) {
        this(state.getBlock(), state.getBlock().getMetaFromState(state));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof MetaBlock) {
            MetaBlock block = (MetaBlock) obj;
            return block.block.equals(this.block) && block.meta == this.meta;
        }
        return false;
    }
}
