package com.melonstudios.createlegacy.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.IStringSerializable;

/**
 * A block that should not be treated as a block but more like a model holder.
 */
public class BlockRender extends Block {
    public BlockRender() {
        super(Material.ROCK);

        setRegistryName("render");
        setUnlocalizedName("create.render");

        setHardness(-1.0f);
        setResistance(36000000.0f);
    }

    public enum Type implements IStringSerializable {
        WATERWHEEL_X("waterwheel_x"),
        WATERWHEEL_Z("waterwheel_z"),

        SAWBLADE_X("sawblade_x"),
        SAWBLADE_Z("sawblade_z"),

        GAUGE_N("gauge_n"),
        GAUGE_E("gauge_e"),
        GAUGE_S("gauge_s"),
        GAUGE_W("gauge_w"),

        SHAFT_U("shaft_u"),
        SHAFT_D("shaft_d"),
        SHAFT_N("shaft_n"),
        SHAFT_E("shaft_e"),
        SHAFT_S("shaft_s"),
        SHAFT_W("shaft_w"),

        FLYWHEEL_N("flywheel_n"),
        FLYWHEEL_E("flywheel_e"),
        FLYWHEEL_S("flywheel_s"),
        FLYWHEEL_W("flywheel_w"),

        PRESS_X("press_x"),
        PRESS_Z("press_z"),

        MILLSTONE("millstone"),

        FAN_U("fan_u"),
        FAN_D("fan_d"),
        FAN_N("fan_n"),
        FAN_E("fan_e"),
        FAN_S("fan_s"),
        FAN_W("fan_w"),

        DRILL_U("drill_u"),
        DRILL_D("drill_d"),
        DRILL_N("drill_n"),
        DRILL_E("drill_e"),
        DRILL_S("drill_s"),
        DRILL_W("drill_w"),

        SAWBLADE_Y("sawblade_y");

        private final String name;
        private final int id;
        Type(String name) {
            this.name = name;
            this.id = ordinal();
        }

        public int getId() {
            return id;
        }
        public static Type fromId(int id) {
            return values()[id % values().length];
        }

        @Override
        public String getName() {
            return name;
        }
    }
    public static final PropertyEnum<Type> TYPE = PropertyEnum.create("type", Type.class);

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, TYPE);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return 0;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState();
    }
}
