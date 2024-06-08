package com.siepert.createlegacy.blocks.kinetic;

import com.siepert.createlegacy.CreateLegacy;
import com.siepert.createlegacy.mainRegistry.ModBlocks;
import com.siepert.createlegacy.mainRegistry.ModItems;
import com.siepert.createlegacy.util.IHasModel;
import com.siepert.createlegacy.util.IKineticActor;
import com.siepert.createlegacy.util.Reference;
import com.siepert.createlegacy.util.handlers.ModSoundHandler;
import com.siepert.createlegacy.util.handlers.recipes.PressingRecipes;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootContext;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Random;

@SuppressWarnings("deprecation")
@MethodsReturnNonnullByDefault
public class BlockMechanicalPress extends Block implements IHasModel, IKineticActor {
    public static final PropertyEnum<EnumFacing.Axis> AXIS = PropertyEnum.create("axis", EnumFacing.Axis.class);
    public BlockMechanicalPress(String name) {
        super(Material.ROCK);
        this.translucent = true;
        this.blockSoundType = SoundType.WOOD;
        this.fullBlock = false;
        setLightOpacity(0);

        setUnlocalizedName(name);
        setRegistryName(name);
        setCreativeTab(CreateLegacy.TAB_CREATE);
        setHarvestLevel("axe", 0);

        setDefaultState(this.blockState.getBaseState().withProperty(AXIS, EnumFacing.Axis.X));

        setHardness(1);
        setResistance(2);
        ModBlocks.BLOCKS.add(this);
        ModItems.ITEMS.add(new ItemBlock(this).setRegistryName(this.getRegistryName()));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        switch (state.getValue(AXIS)) {
            case X:
                return 0;
            case Z:
                return 1;
        }
        return 0;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        switch (meta) {
            case 0:
                return this.getDefaultState().withProperty(AXIS, EnumFacing.Axis.X);
            case 1:
                return this.getDefaultState().withProperty(AXIS, EnumFacing.Axis.Z);
            default:
                return this.getDefaultState();
        }
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] {AXIS});
    }

    @Override
    public IBlockState getStateForPlacement(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        return this.getDefaultState().withProperty(AXIS, placer.getHorizontalFacing().getAxis());
    }

    @Override
    public void registerModels() {
        CreateLegacy.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory");
    }

    @Override
    public boolean isFullBlock(IBlockState state) {
        return false;
    }
    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }
    @Override
    public boolean isTranslucent(IBlockState state) {
        return true;
    }
    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public void passRotation(World worldIn, BlockPos pos, EnumFacing source, List<BlockPos> iteratedBlocks, boolean srcIsCog, boolean srcCogIsHorizontal) {


        if (srcIsCog) return; //We don't accept a cog as input, we need a shaft!

        IBlockState myState = worldIn.getBlockState(pos);


        if (source.getAxis() == myState.getValue(AXIS)) {
            iteratedBlocks.add(pos);
            if (!worldIn.getBlockState(pos.down()).getMaterial().blocksMovement() && !worldIn.isRemote) {
                AxisAlignedBB itemSearchArea = new AxisAlignedBB(pos.down());
                List<EntityItem> foundItems = worldIn.getEntitiesWithinAABB(EntityItem.class, itemSearchArea);

                for (EntityItem entityItem : foundItems) {
                    if (apply(entityItem.getItem()).hasRecipe) {
                        EntityItem resultEntityItem = new EntityItem(worldIn, pos.getX() + 0.5, pos.down().getY(), pos.getZ() + 0.5,
                                apply(entityItem.getItem()).stack);
                        resultEntityItem.setVelocity(0, 0, 0);
                        worldIn.spawnEntity(resultEntityItem);
                        entityItem.getItem().shrink(1);
                        if (entityItem.getItem().getCount() == 0 || entityItem.getItem().isEmpty()) {
                            entityItem.setDead();
                        }

                        float pitch;
                        if (Reference.random.nextInt(100) == 0) {
                            pitch = 0.1f;
                        } else pitch = 0.8f;
                        worldIn.playSound(null, pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, ModSoundHandler.BLOCK_PRESS_ACTIVATION, SoundCategory.BLOCKS, 1.0f, pitch);
                        return;
                    }
                }
            }
        }
    }

    public ResultSet apply(ItemStack stack)
    {
        if (stack.isEmpty())
        {
            return new ResultSet(stack, false);
        }
        else
        {
            ItemStack itemstack = PressingRecipes.instance().getPressingResult(stack);

            if (itemstack.isEmpty())
            {
                CreateLegacy.logger.warn("Couldn't press {} because there is no pressing recipe", (Object)stack);
                return new ResultSet(stack, false);
            }
            else
            {
                ItemStack itemstack1 = itemstack.copy();
                itemstack1.setCount(itemstack.getCount());
                return new ResultSet(itemstack1, true);
            }
        }
    }

    private class ResultSet {
        ItemStack stack;
        boolean hasRecipe;
        private ResultSet(ItemStack stack, boolean hasRecipe) {
            this.stack = stack;
            this.hasRecipe = hasRecipe;
        }

        public boolean hasRecipe() {
            return hasRecipe;
        }

        public ItemStack getResult() {
            return stack;
        }
    }
}
