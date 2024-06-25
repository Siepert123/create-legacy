package com.siepert.createlegacy.blocks.kinetic;

import com.siepert.createlegacy.CreateLegacy;
import com.siepert.createlegacy.mainRegistry.ModBlocks;
import com.siepert.createlegacy.mainRegistry.ModItems;
import com.siepert.createlegacy.util.IHasModel;
import com.siepert.createapi.IKineticActor;
import com.siepert.createapi.IWrenchable;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class BlockBelt extends Block implements IHasModel, IKineticActor, IWrenchable {
    public static final PropertyBool HAS_AXLE = PropertyBool.create("has_axle");
    public static final PropertyEnum<EnumFacing.Axis> AXIS = PropertyEnum.create("axis", EnumFacing.Axis.class);
    public BlockBelt(String name) {
        super(Material.CLOTH, MapColor.BLACK);
        this.translucent = true;
        this.blockSoundType = SoundType.CLOTH;
        this.fullBlock = false;
        setLightOpacity(0);

        setDefaultState(this.blockState.getBaseState().withProperty(HAS_AXLE, false).withProperty(AXIS, EnumFacing.Axis.X));

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
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int meta = 0;
        if (state.getValue(HAS_AXLE)) meta += 2;
        if (state.getValue(AXIS) == EnumFacing.Axis.Z) meta++;
        return meta;
    }
    public static final AxisAlignedBB bb = new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 14.0 / 16.0, 1.0);
    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return bb;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        switch (meta) {
            case 0:
                return this.getDefaultState().withProperty(AXIS, EnumFacing.Axis.X).withProperty(HAS_AXLE, false);
            case 1:
                return this.getDefaultState().withProperty(AXIS, EnumFacing.Axis.Z).withProperty(HAS_AXLE, false);
            case 2:
                return this.getDefaultState().withProperty(AXIS, EnumFacing.Axis.X).withProperty(HAS_AXLE, true);
            case 3:
                return this.getDefaultState().withProperty(AXIS, EnumFacing.Axis.Z).withProperty(HAS_AXLE, true);
        }
        return this.getStateFromMeta(0);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] {HAS_AXLE, AXIS});
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
    public void passRotation(World worldIn, BlockPos pos, EnumFacing source, List<BlockPos> iteratedBlocks,
                             boolean srcIsCog, boolean srcCogIsHorizontal, boolean inverseRotation) {
        if (source.getAxis() == EnumFacing.Axis.Y) return;
        if (srcIsCog) return;

        IBlockState myState = worldIn.getBlockState(pos);

        if (source.getAxis() == myState.getValue(AXIS)) return;

        if (myState.getValue(HAS_AXLE)) {
            iteratedBlocks.add(pos);
        } else return;

        //Now that we made sure the connection is valid we can continue

        EnumFacing.AxisDirection direction;

        if (inverseRotation) direction = EnumFacing.AxisDirection.NEGATIVE;
        else direction = EnumFacing.AxisDirection.POSITIVE;

        EnumFacing movement = EnumFacing.getFacingFromAxis(direction, myState.getValue(AXIS));

        AxisAlignedBB bb = new AxisAlignedBB(pos.up());

        List<Entity> entities = worldIn.getEntitiesWithinAABB(Entity.class, bb);

        for (Entity entity : entities) {
            if (!(entity instanceof EntityItem)) {
                entity.addVelocity(movement.getFrontOffsetX() / 5.0f,
                        0,
                        movement.getFrontOffsetZ() / 5.0f);
            } else {
                ItemStack stack = ((EntityItem) entity).getItem();
                Block processor = worldIn.getBlockState(pos.up(2)).getBlock();

                ((EntityItem) entity).setNoDespawn();
                ((EntityItem) entity).setDefaultPickupDelay();

                if (processor instanceof BlockMechanicalPress) {
                    if (!((BlockMechanicalPress) processor).apply(stack).hasRecipe()) {
                        entity.addVelocity(movement.getFrontOffsetX() / 5.0f,
                                0,
                                movement.getFrontOffsetZ() / 5.0f);
                    } else {
                        entity.setPosition(pos.getX() + 0.5,
                                pos.getY() + 1.0,
                                pos.getZ() + 0.5);
                    }
                } else {
                    entity.addVelocity(movement.getFrontOffsetX() / 5.0f,
                            0,
                            movement.getFrontOffsetZ() / 5.0f);
                }
            }
        }

        List<BlockPos> iteratedBelts = new ArrayList<>();
        iteratedBelts.add(pos);
        if (worldIn.getBlockState(pos.offset(movement)).getBlock() instanceof BlockBelt) {
            commenceItemMovement(worldIn, pos.offset(movement), movement.getOpposite(), iteratedBelts, movement, iteratedBlocks, inverseRotation);
        }
        if (worldIn.getBlockState(pos.offset(movement.getOpposite())).getBlock() instanceof BlockBelt) {
            commenceItemMovement(worldIn, pos.offset(movement.getOpposite()), movement, iteratedBelts, movement, iteratedBlocks, inverseRotation);
        }

        if (worldIn.getBlockState(pos.offset(source.getOpposite())).getBlock() instanceof IKineticActor) {
            ((IKineticActor) worldIn.getBlockState(pos.offset(source.getOpposite())).getBlock())
                    .passRotation(worldIn, pos.offset(source.getOpposite()), source, iteratedBlocks, false, false, inverseRotation);
        }
    }

    public void commenceItemMovement(World worldIn, BlockPos pos, EnumFacing source, List<BlockPos> iteratedBelts, EnumFacing movement, List<BlockPos> iteratedBlocks, boolean inv) {
        iteratedBelts.add(pos);

        IBlockState myState = worldIn.getBlockState(pos);

        if (!worldIn.isRemote) {
            AxisAlignedBB bb = new AxisAlignedBB(pos.up());

            List<Entity> entities = worldIn.getEntitiesWithinAABB(Entity.class, bb);

            for (Entity entity : entities) {
                if (!(entity instanceof EntityItem)) {
                    entity.addVelocity(movement.getFrontOffsetX() / 5.0f,
                            0,
                            movement.getFrontOffsetZ() / 5.0f);
                } else {
                    ItemStack stack = ((EntityItem) entity).getItem();
                    Block processor = worldIn.getBlockState(pos.up(2)).getBlock();

                    ((EntityItem) entity).setNoDespawn();
                    ((EntityItem) entity).setDefaultPickupDelay();

                    if (processor instanceof BlockMechanicalPress) {
                        if (!((BlockMechanicalPress) processor).apply(stack).hasRecipe()) {
                            entity.addVelocity(movement.getFrontOffsetX() / 5.0f,
                                    0,
                                    movement.getFrontOffsetZ() / 5.0f);
                        } else {
                            entity.setPosition(pos.getX() + 0.5,
                                    pos.getY() + 1.0,
                                    pos.getZ() + 0.5);
                        }
                    } else {
                        entity.addVelocity(movement.getFrontOffsetX() / 5.0f,
                                0,
                                movement.getFrontOffsetZ() / 5.0f);
                    }
                }
            }
        }

        if (worldIn.isRemote) {
            AxisAlignedBB bb = new AxisAlignedBB(pos.up());

            List<EntityPlayer> entities = worldIn.getEntitiesWithinAABB(EntityPlayer.class, bb);

            for (EntityPlayer entity : entities) {
                entity.addVelocity(movement.getFrontOffsetX() / 5.0f,
                        movement.getFrontOffsetY() / 5.0f,
                        movement.getFrontOffsetZ() / 5.0f);
            }
        }

        if (worldIn.getBlockState(pos.offset(source.getOpposite())).getBlock() instanceof BlockBelt
                && !iteratedBelts.contains(pos.offset(source.getOpposite()))) {
            if (worldIn.getBlockState(pos.offset(source.getOpposite())).getValue(AXIS) == worldIn.getBlockState(pos).getValue(AXIS)) {
                ((BlockBelt) worldIn.getBlockState(pos.offset(source.getOpposite())).getBlock())
                        .commenceItemMovement(worldIn, pos.offset(source.getOpposite()), source, iteratedBelts, movement, iteratedBlocks, inv);
            }
        }

        if (worldIn.getBlockState(pos).getBlock() instanceof BlockBelt) {
            if (myState.getValue(HAS_AXLE)) {
                Block blockAtP = worldIn.getBlockState(pos.offset(fromAx(rotateAxis(myState.getValue(AXIS)), true))).getBlock();
                Block blockAtN = worldIn.getBlockState(pos.offset(fromAx(rotateAxis(myState.getValue(AXIS)), false))).getBlock();

                if (blockAtP instanceof IKineticActor && !iteratedBlocks.contains(pos.offset(fromAx(rotateAxis(myState.getValue(AXIS)), true)))) {
                    ((IKineticActor) blockAtP).passRotation(worldIn, pos.offset(fromAx(rotateAxis(myState.getValue(AXIS)), true)),
                            fromAx(myState.getValue(AXIS), false), iteratedBlocks,
                            false, false, inv);
                }
                if (blockAtN instanceof IKineticActor && !iteratedBlocks.contains(pos.offset(fromAx(rotateAxis(myState.getValue(AXIS)), false)))) {
                    ((IKineticActor) blockAtN).passRotation(worldIn, pos.offset(fromAx(rotateAxis(myState.getValue(AXIS)), false)),
                            fromAx(rotateAxis(myState.getValue(AXIS)), true), iteratedBlocks,
                            false, false, inv);
                }
            }
        } else CreateLegacy.logger.error("Why am I not a belt?? {}", pos);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (playerIn.getHeldItem(hand).getItem() == Item.getItemFromBlock(ModBlocks.AXLE) && !state.getValue(HAS_AXLE)) {
            worldIn.setBlockState(pos, state.withProperty(HAS_AXLE, true));
            return true;
        }
        return false;
    }

    public EnumFacing.Axis rotateAxis(EnumFacing.Axis axis) {
        if (axis == EnumFacing.Axis.X) return EnumFacing.Axis.Z;
        return EnumFacing.Axis.X;
    }

    public EnumFacing fromAx(EnumFacing.Axis axis, boolean b) {
        if (b) return EnumFacing.getFacingFromAxis(EnumFacing.AxisDirection.POSITIVE, axis);
        return EnumFacing.getFacingFromAxis(EnumFacing.AxisDirection.NEGATIVE, axis);
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
    public boolean rotateBlock(World world, BlockPos pos, EnumFacing axis) {
        IBlockState state = world.getBlockState(pos);
        TileEntity tileEntity = world.getTileEntity(pos);

        if (state.getValue(AXIS) == EnumFacing.Axis.X) {
            world.setBlockState(pos, state.withProperty(AXIS, EnumFacing.Axis.Z), 3);
        } else {
            world.setBlockState(pos, state.withProperty(AXIS, EnumFacing.Axis.X), 3);
        }

        if (tileEntity != null) {
            tileEntity.validate();
            world.setTileEntity(pos, tileEntity);
        }

        return true;
    }
    @Override
    public boolean onWrenched(World worldIn, BlockPos pos, IBlockState state, EnumFacing side, EntityPlayer playerIn) {
        rotateBlock(worldIn, pos, side);

        return true;
    }
}
