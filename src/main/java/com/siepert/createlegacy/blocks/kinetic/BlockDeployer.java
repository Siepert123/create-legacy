package com.siepert.createlegacy.blocks.kinetic;

import com.siepert.createapi.CreateAPI;
import com.siepert.createlegacy.CreateLegacy;
import com.siepert.createlegacy.CreateLegacyConfigHolder;
import com.siepert.createlegacy.mainRegistry.ModBlocks;
import com.siepert.createlegacy.mainRegistry.ModItems;
import com.siepert.createlegacy.tabs.DeployerPlayerSim;
import com.siepert.createlegacy.tileentity.TileEntityDeployer;
import com.siepert.createlegacy.util.EnumHorizontalFacing;
import com.siepert.createlegacy.util.IHasModel;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class BlockDeployer extends Block implements IHasModel, ITileEntityProvider {

    public static final PropertyEnum<EnumHorizontalFacing> FACING = PropertyEnum.create("facing", EnumHorizontalFacing.class);
    public static final PropertyBool EXTENDED = PropertyBool.create("extended");

    public BlockDeployer() {
        super(Material.ROCK);

        setRegistryName("deployer");
        setUnlocalizedName("create:deployer");

        setCreativeTab(CreateLegacy.TAB_CREATE);
        setHarvestLevel("axe", 0);
        setDefaultState(this.blockState.getBaseState().withProperty(EXTENDED, false).withProperty(FACING, EnumHorizontalFacing.NORTH));

        setHardness(1);
        setResistance(2);
        ModBlocks.BLOCKS.add(this);
        ModItems.ITEMS.add(new ItemBlock(this).setRegistryName(this.getRegistryName()));
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced) {
        tooltip.add(CreateAPI.stressImpactTooltip(CreateLegacyConfigHolder.kineticConfig.deployerStressImpact));
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
    public void registerModels() {
        CreateLegacy.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory");
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityDeployer();
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        if (placer != null) {
            return getDefaultState().withProperty(FACING, EnumHorizontalFacing.fromVanillaFacing(placer.getHorizontalFacing().getOpposite()));
        }
        return getDefaultState().withProperty(FACING, EnumHorizontalFacing.fromVanillaFacing(facing.getOpposite()));
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING, EXTENDED);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(FACING, EnumHorizontalFacing.fromIndex(meta));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(FACING).index();
    }

    @Override
    public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) {
        TileEntityDeployer deployer = (TileEntityDeployer) worldIn.getTileEntity(pos);

        if (deployer == null) return;

        if (!deployer.getStackInSlot(0).isEmpty()) {
            EntityItem item1 = new EntityItem(worldIn,
                    pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                    deployer.getStackInSlot(0));
            worldIn.spawnEntity(item1);
        }

        if (!deployer.getStackInSlot(0).isEmpty()) {
            EntityItem item2 = new EntityItem(worldIn,
                    pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                    deployer.getStackInSlot(1));
            worldIn.spawnEntity(item2);
        }
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        if (placer instanceof EntityPlayer) {
            TileEntityDeployer deployer = (TileEntityDeployer) worldIn.getTileEntity(pos);

            if (deployer != null) {
                DeployerPlayerSim sim = new DeployerPlayerSim((EntityPlayer) placer);
                sim.setData(worldIn.getBlockState(pos).getValue(FACING).toVanillaFacing());
                deployer.setPlacer(sim);
            }
        }
    }
}
