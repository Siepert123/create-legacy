package nl.melonstudios.create.block.generator;

import com.melonstudios.melonlib.blockdict.BlockDictionary;
import com.melonstudios.melonlib.item.IMetaName;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import nl.melonstudios.create.block.BlockKineticHorizontalBase;
import nl.melonstudios.create.tileentity.generator.TileEntityFurnaceEngine;
import nl.melonstudios.create.tileentity.generator.TileEntityFurnaceFlywheel;

import javax.annotation.Nullable;

public class BlockFurnaceEngine extends BlockKineticHorizontalBase implements ITileEntityProvider, IMetaName {
	public enum Variant implements IStringSerializable {
		ENGINE("engine", 0),
		FLYWHEEL("flywheel", 1);

		private final String name;
		private final int id;
		Variant(String name, int id) {
			this.name = name;
			this.id = id;
		}

		@Override
		public String getName() {
			return this.name;
		}
		public int getId() {
			return this.id;
		}

		private static final Variant[] VALUES = {
				ENGINE, FLYWHEEL,
		};
		public static Variant byId(int id) {
			if (id < 0 || id >= VALUES.length) return ENGINE;
			return VALUES[id];
		}
	}

	public static final PropertyEnum<Variant> VARIANT = PropertyEnum.create("variant", Variant.class);

	public BlockFurnaceEngine() {
		super(Material.IRON, MapColor.GOLD);
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, FACING, VARIANT);
	}

	@Override
	public boolean hasShaftTowards(World world, BlockPos pos, IBlockState state, EnumFacing side) {
		return state.getValue(VARIANT) == Variant.FLYWHEEL && state.getValue(FACING).getOpposite() == side;
	}

	@Override
	public EnumFacing.Axis getRotationAxis(IBlockState state) {
		return state.getValue(FACING).getAxis();
	}

	@Nullable
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return (meta & 0b0100) != 0 ? new TileEntityFurnaceFlywheel() : new TileEntityFurnaceEngine();
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(FACING).getHorizontalIndex() | (state.getValue(VARIANT).getId() << 2);
	}
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState()
				.withProperty(FACING, EnumFacing.getHorizontal(meta & 0b0011))
				.withProperty(VARIANT, Variant.byId((meta >> 2) & 0b0001));
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return stack.getMetadata() != 0 ? "tile.create.flywheel" : "tile.create.furnace_engine";
	}

	@Override
	public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items) {
		items.add(new ItemStack(this, 1, 0));
		items.add(new ItemStack(this, 1, 1));
	}

	@Override
	public int damageDropped(IBlockState state) {
		return state.getValue(VARIANT).getId();
	}

	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ,
											int meta, EntityLivingBase placer, EnumHand hand) {
		return super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer, hand).withProperty(VARIANT, Variant.byId(meta));
	}

	@Override
	public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor) {
		super.onNeighborChange(world, pos, neighbor);
		IBlockState state = world.getBlockState(pos);
		if (state.getValue(VARIANT) == Variant.ENGINE) {
			EnumFacing facing = state.getValue(FACING);
			BlockPos flywheel = pos.offset(facing, 2);
			BlockPos furnace = pos.offset(facing, -1);
			boolean active = BlockDictionary.isBlockTagged(world.getBlockState(furnace), "create:powersFurnaceEngine");
			IBlockState stateAtFlywheel = world.getBlockState(flywheel);
			if (stateAtFlywheel.getBlock() == this
					&& stateAtFlywheel.getValue(VARIANT) == Variant.FLYWHEEL
					&& stateAtFlywheel.getValue(FACING) == state.getValue(FACING).rotateY()) {
				TileEntityFurnaceFlywheel te = (TileEntityFurnaceFlywheel) world.getTileEntity(flywheel);
				te.updatePoweredState(active);
				te.updateEngineState(true);
			}
		}
	}

	@Override
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
		super.onBlockAdded(worldIn, pos, state);
		this.onNeighborChange(worldIn, pos, pos);
	}
}
