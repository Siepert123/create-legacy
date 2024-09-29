package com.melonstudios.createlegacy.block.stone;

public final class BlockPolishedOrestone extends AbstractBlockOrestone {
    public BlockPolishedOrestone() {
        super("orestone_polished");
    }

    @Override
    protected String getOrestonePrefix() {
        return "polished";
    }
}
