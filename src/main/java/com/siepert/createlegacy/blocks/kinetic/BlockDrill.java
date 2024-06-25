package com.siepert.createlegacy.blocks.kinetic;

import com.siepert.createlegacy.CreateLegacy;
import com.siepert.createlegacy.mainRegistry.ModBlocks;
import com.siepert.createlegacy.mainRegistry.ModItems;
import com.siepert.createlegacy.util.IHasModel;
import com.siepert.createapi.IKineticActor;
import com.siepert.createapi.IWrenchable;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.List;

@SuppressWarnings("deprecation")
@MethodsReturnNonnullByDefault
public class BlockDrill extends Block implements IHasModel, IKineticActor, IWrenchable {
    public static final PropertyEnum<EnumFacing> FACING = PropertyEnum.create("facing", EnumFacing.class);
    public BlockDrill(String name) {
        super(Material.ROCK);
        this.translucent = true;
        this.blockSoundType = SoundType.WOOD;
        this.fullBlock = false;
        setLightOpacity(0);

        setUnlocalizedName("create:" + name);
        setRegistryName(name);
        setCreativeTab(CreateLegacy.TAB_CREATE);
        setHarvestLevel("axe", 0);
        setHardness(1);
        setResistance(2);
        ModBlocks.BLOCKS.add(this);
        ModItems.ITEMS.add(new ItemBlock(this).setRegistryName(this.getRegistryName()));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(FACING).getIndex();
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(FACING, EnumFacing.getFront(meta));
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] {FACING});
    }

    @Override
    public IBlockState getStateForPlacement(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        if (placer.isSneaking()) {
            return super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer, hand).withProperty(FACING, facing.getOpposite());
        }
        return super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer, hand).withProperty(FACING,
                EnumFacing.getFacingFromVector((float) placer.getLookVec().x, (float) placer.getLookVec().y, (float) placer.getLookVec().z).getOpposite());
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
    public void passRotation(World worldIn, BlockPos pos, EnumFacing source, List<BlockPos> iteratedBlocks,
                             boolean srcIsCog, boolean srcCogIsHorizontal, boolean inverseRotation) {


        if (srcIsCog) return; //We don't accept a cog as input, we need a shaft!

        IBlockState myState = worldIn.getBlockState(pos);


        if (source == myState.getValue(FACING).getOpposite()) {
            iteratedBlocks.add(pos);
            BlockPos newPos = new BlockPos(pos.offset(source.getOpposite()));
            if (!worldIn.getBlockState(pos.offset(source.getOpposite())).getMaterial().isReplaceable()
                    && worldIn.getBlockState(pos.offset(source.getOpposite())).getBlockHardness(worldIn, newPos) != -1.0f) {
                worldIn.getBlockState(newPos).getBlock().dropBlockAsItem(worldIn, newPos, worldIn.getBlockState(newPos), 0);
                worldIn.playSound(null, newPos, worldIn.getBlockState(newPos).getBlock().getSoundType().getBreakSound(),
                        SoundCategory.BLOCKS, 1.0f, 1.0f);
                worldIn.setBlockState(newPos, Blocks.AIR.getDefaultState());
            }

            AxisAlignedBB axisAlignedBB = new AxisAlignedBB(newPos);

            List<EntityLivingBase> entities = worldIn.getEntitiesWithinAABB(EntityLivingBase.class, axisAlignedBB);

            for (EntityLivingBase entity : entities) {
                if (entity instanceof EntityPlayer) {
                    if (!((EntityPlayer) entity).isCreative()) {
                        entity.setHealth(entity.getHealth() - 4);
                        entity.performHurtAnimation();
                        worldIn.playSound(null,
                                entity.posX, entity.posY, entity.posZ,
                                SoundEvents.ENTITY_PLAYER_HURT,
                                entity.getSoundCategory(), 1.0f, 1.0f);
                    }
                } else {
                    entity.setHealth(entity.getHealth() - 4);
                    entity.performHurtAnimation();
                    worldIn.playSound(null,
                            entity.posX, entity.posY, entity.posZ,
                            SoundEvents.ENTITY_GENERIC_HURT,
                            entity.getSoundCategory(), 1.0f, 1.0f);
                }
            }
        }
    }

    @Override
    public boolean rotateBlock(World world, BlockPos pos, EnumFacing side) {
        IBlockState state = world.getBlockState(pos);

        if (side.getAxis() == state.getValue(FACING).getAxis()) return false;

        world.setBlockState(pos, state.withProperty(FACING, state.getValue(FACING).rotateAround(side.getAxis())), 3);

        return true;
    }
    @Override
    public boolean onWrenched(World worldIn, BlockPos pos, IBlockState state, EnumFacing side, EntityPlayer playerIn) {
        return rotateBlock(worldIn, pos, side);
    }
}
