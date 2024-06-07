package com.siepert.createlegacy.util;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public interface IKineticActor {
    /**Passes a rotational current through the block
     * DEPRECATED - Use passRotation instead!
     * @param worldIn   The world where this block is in
     * @param pos       The position of this block
     * @param source    The direction where the current originated from*/
    @Deprecated
    public void act(World worldIn, BlockPos pos, EnumFacing source);

    /**Passes a rotational current through the block.
     * Should be extremely bug-free. Please code your block correctly
     * @param worldIn               The world of the block
     * @param pos                   The position of the block
     * @param source                The direction the force came from
     * @param iteratedBlocks        List of block positions the current has already visited
     * @param srcIsCog              Is true when the input force comes from cog teeth.
     * @param srcCogIsHorizontal    If srcIsCog is true, this says whether those teeth were vertical or horizontal.*/
    public void passRotation(World worldIn, BlockPos pos, EnumFacing source, List<BlockPos> iteratedBlocks, boolean srcIsCog, boolean srcCogIsHorizontal);
}
