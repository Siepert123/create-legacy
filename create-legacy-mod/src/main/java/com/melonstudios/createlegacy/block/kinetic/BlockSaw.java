package com.melonstudios.createlegacy.block.kinetic;

import com.melonstudios.createlegacy.CreateLegacy;
import com.melonstudios.createlegacy.recipe.SawingRecipes;
import com.melonstudios.createlegacy.tileentity.TileEntitySaw;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockSaw extends Block implements ITileEntityProvider {
    public BlockSaw() {
        super(Material.ROCK, MapColor.WOOD);

        setRegistryName("saw");
        setUnlocalizedName("create.saw");

        setSoundType(SoundType.WOOD);

        setCreativeTab(CreateLegacy.TAB_KINETICS);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntitySaw();
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack stack = playerIn.getHeldItem(hand);

        if (SawingRecipes.hasResult(stack)) {
            if (!worldIn.isRemote) {
                TileEntitySaw saw = (TileEntitySaw) worldIn.getTileEntity(pos);
                ItemStack result = SawingRecipes.getResult(stack, saw.getIndex());
                EntityItem item = new EntityItem(worldIn, playerIn.posX, playerIn.posY, playerIn.posZ,
                        result.copy());
                item.setPickupDelay(0);
                item.setVelocity(0, 0, 0);
                worldIn.spawnEntity(item);
                stack.shrink(1);
                saw.increaseIndex();
            }
            return true;
        } else return false;
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public int getLightOpacity(IBlockState state, IBlockAccess world, BlockPos pos) {
        return 0;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullBlock(IBlockState state) {
        return false;
    }

    @Override
    public boolean isNormalCube(IBlockState state, IBlockAccess world, BlockPos pos) {
        return false;
    }

    @Override
    public boolean isTranslucent(IBlockState state) {
        return true;
    }

    @Override
    public boolean isNormalCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }
}
