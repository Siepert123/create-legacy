package com.siepert.createapi;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public interface IKineticActor {

    /**
     * Passes a rotational current through the block.
     * <p>
     * Should be extremely bug-free. Please code your block correctly
     * @param worldIn               The world of the block
     * @param pos                   The position of the block
     * @param source                The direction the force came from
     * @param iteratedBlocks        List of block positions the current has already visited. Only add your position to this list once your block has actually confirmed that the kinetic input is valid!
     * @param srcIsCog              Is true when the input force comes from cog teeth.
     * @param srcCogIsHorizontal    If srcIsCog is true, this says whether those teeth were vertical or horizontal.
     * @param inverseRotation       Is the rotation inverted? Usually you can just pass this parameter, except in special cases like with cogwheels.
     * */
    public void passRotation(World worldIn, BlockPos pos, EnumFacing source, List<BlockPos> iteratedBlocks,
                             boolean srcIsCog, boolean srcCogIsHorizontal, boolean inverseRotation);
}
