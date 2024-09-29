package com.melonstudios.createlegacy.event;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public final class SchematicPlacementEvent extends Event {
    public final IBlockState[][][] structure;
    public final World world;
    public final BlockPos pos;
    public final EntityPlayer placer;

    public SchematicPlacementEvent(World world, BlockPos pos, IBlockState[][][] structure, EntityPlayer player) {
        this.structure = structure;
        this.world = world;
        this.pos = pos;
        this.placer = player;
    }
}
