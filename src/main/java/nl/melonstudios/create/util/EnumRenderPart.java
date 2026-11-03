package nl.melonstudios.create.util;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import nl.melonstudios.create.block.BlockBlazeBurner;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Objects;

@SuppressWarnings("all")
public enum EnumRenderPart implements IStringSerializable {
    SAW_X,
    SAW_Y,
    SAW_Z,

    HALF_SHAFT_DOWN,
    HALF_SHAFT_UP,
    HALF_SHAFT_NORTH,
    HALF_SHAFT_SOUTH,
    HALF_SHAFT_WEST,
    HALF_SHAFT_EAST,

    DIAL,

    DRILL_HEAD_DOWN,
    DRILL_HEAD_UP,
    DRILL_HEAD_NORTH,
    DRILL_HEAD_SOUTH,
    DRILL_HEAD_WEST,
    DRILL_HEAD_EAST,

    WATER_WHEEL_DOWN,
    WATER_WHEEL_UP,
    WATER_WHEEL_NORTH,
    WATER_WHEEL_SOUTH,
    WATER_WHEEL_WEST,
    WATER_WHEEL_EAST,

    BEARING_PLATE_DOWN,
    BEARING_PLATE_UP,
    BEARING_PLATE_NORTH,
    BEARING_PLATE_SOUTH,
    BEARING_PLATE_WEST,
    BEARING_PLATE_EAST,

    CLOCKWISE_ROTATION_INDICATOR,
    COUNTERCLOCKWISE_ROTATION_INDICATOR,

    PRESS_X,
    PRESS_Z,

    DEPLOYER_DOWN,
    DEPLOYER_UP,
    DEPLOYER_NORTH,
    DEPLOYER_SOUTH,
    DEPLOYER_WEST,
    DEPLOYER_EAST,

    DEPLOYER_HOLDING_DOWN,
    DEPLOYER_HOLDING_UP,
    DEPLOYER_HOLDING_NORTH,
    DEPLOYER_HOLDING_SOUTH,
    DEPLOYER_HOLDING_WEST,
    DEPLOYER_HOLDING_EAST,

    HARVESTER,

    SUCTION_CUP,
    SHAFTLESS_COG_X,
    SHAFTLESS_COG_Y,
    SHAFTLESS_COG_Z,

    WHISK,

    BLAZE_SMOULDERING,
    BLAZE_KINDLED,
    BLAZE_SEETHING,

    CRAFTER_COVER_NORTH,
    CRAFTER_COVER_EAST,
    CRAFTER_COVER_SOUTH,
    CRAFTER_COVER_WEST,

    MILLSTONE,

    FLYWHEEL_N,
    FLYWHEEL_E,
    FLYWHEEL_S,
    FLYWHEEL_W,
    LOWER_ROTATING_CONNECTOR,
    LOWER_SLIDING_CONNECTOR,
    UPPER_ROTATING_CONNECTOR,
    UPPER_SLIDING_CONNECTOR,
    FURNACE_HAT,
    ;

    private final int id = this.ordinal();
    private final String name = this.toString().toLowerCase();

    @Override
    @Nonnull
    public String getName() {
        return this.name;
    }
    public int getID() {
        return this.id;
    }

    private static final HashMap<String, EnumRenderPart> NAME_TO_ENUM = new HashMap<>();

    private static final EnumRenderPart[] SAWS = new EnumRenderPart[3];
    private static final EnumRenderPart[] HALF_SHAFTS = new EnumRenderPart[6];
    private static final EnumRenderPart[] DRILL_HEADS = new EnumRenderPart[6];
    private static final EnumRenderPart[] WATER_WHEELS = new EnumRenderPart[6];
    private static final EnumRenderPart[] BEARING_PLATES = new EnumRenderPart[6];
    private static final EnumRenderPart[] DEPLOYERS = new EnumRenderPart[6];
    private static final EnumRenderPart[] DEPLOYER_HOLDINGS = new EnumRenderPart[6];
    private static final EnumRenderPart[] SHAFTLESS_COGS = new EnumRenderPart[3];
    private static final EnumRenderPart[] CRAFTER_COVERS = new EnumRenderPart[4];

    public static EnumRenderPart byID(int id) {
        return values()[id % values().length];
    }
    public static EnumRenderPart byName(String name) {
        return Objects.requireNonNull(NAME_TO_ENUM.get(name), "No such enum with name " + name);
    }

    static {
        for (EnumRenderPart erp : EnumRenderPart.values()) {
            NAME_TO_ENUM.put(erp.name, erp);
        }

        SAWS[0] = SAW_X;
        SAWS[1] = SAW_Y;
        SAWS[2] = SAW_Z;

        HALF_SHAFTS[0] = HALF_SHAFT_DOWN;
        HALF_SHAFTS[1] = HALF_SHAFT_UP;
        HALF_SHAFTS[2] = HALF_SHAFT_NORTH;
        HALF_SHAFTS[3] = HALF_SHAFT_SOUTH;
        HALF_SHAFTS[4] = HALF_SHAFT_WEST;
        HALF_SHAFTS[5] = HALF_SHAFT_EAST;

        DRILL_HEADS[0] = DRILL_HEAD_DOWN;
        DRILL_HEADS[1] = DRILL_HEAD_UP;
        DRILL_HEADS[2] = DRILL_HEAD_NORTH;
        DRILL_HEADS[3] = DRILL_HEAD_SOUTH;
        DRILL_HEADS[4] = DRILL_HEAD_WEST;
        DRILL_HEADS[5] = DRILL_HEAD_EAST;

        WATER_WHEELS[0] = WATER_WHEEL_DOWN;
        WATER_WHEELS[1] = WATER_WHEEL_UP;
        WATER_WHEELS[2] = WATER_WHEEL_NORTH;
        WATER_WHEELS[3] = WATER_WHEEL_SOUTH;
        WATER_WHEELS[4] = WATER_WHEEL_WEST;
        WATER_WHEELS[5] = WATER_WHEEL_EAST;

        BEARING_PLATES[0] = BEARING_PLATE_DOWN;
        BEARING_PLATES[1] = BEARING_PLATE_UP;
        BEARING_PLATES[2] = BEARING_PLATE_NORTH;
        BEARING_PLATES[3] = BEARING_PLATE_SOUTH;
        BEARING_PLATES[4] = BEARING_PLATE_WEST;
        BEARING_PLATES[5] = BEARING_PLATE_EAST;

        DEPLOYERS[0] = DEPLOYER_DOWN;
        DEPLOYERS[1] = DEPLOYER_UP;
        DEPLOYERS[2] = DEPLOYER_NORTH;
        DEPLOYERS[3] = DEPLOYER_SOUTH;
        DEPLOYERS[4] = DEPLOYER_WEST;
        DEPLOYERS[5] = DEPLOYER_EAST;

        DEPLOYER_HOLDINGS[0] = DEPLOYER_HOLDING_DOWN;
        DEPLOYER_HOLDINGS[1] = DEPLOYER_HOLDING_UP;
        DEPLOYER_HOLDINGS[2] = DEPLOYER_HOLDING_NORTH;
        DEPLOYER_HOLDINGS[3] = DEPLOYER_HOLDING_SOUTH;
        DEPLOYER_HOLDINGS[4] = DEPLOYER_HOLDING_WEST;
        DEPLOYER_HOLDINGS[5] = DEPLOYER_HOLDING_EAST;

        SHAFTLESS_COGS[0] = SHAFTLESS_COG_X;
        SHAFTLESS_COGS[1] = SHAFTLESS_COG_Y;
        SHAFTLESS_COGS[2] = SHAFTLESS_COG_Z;

        CRAFTER_COVERS[0] = CRAFTER_COVER_SOUTH;
        CRAFTER_COVERS[1] = CRAFTER_COVER_WEST;
        CRAFTER_COVERS[2] = CRAFTER_COVER_NORTH;
        CRAFTER_COVERS[3] = CRAFTER_COVER_EAST;
    }

    public static EnumRenderPart getSaw(EnumFacing.Axis axis) {
        return SAWS[axis.ordinal()];
    }
    public static EnumRenderPart getHalfShaft(EnumFacing facing) {
        return HALF_SHAFTS[facing.getIndex()];
    }
    public static EnumRenderPart getDrillHead(EnumFacing facing) {
        return DRILL_HEADS[facing.getIndex()];
    }
    public static EnumRenderPart getWaterWheel(EnumFacing facing) {
        return WATER_WHEELS[facing.getIndex()];
    }
    public static EnumRenderPart getBearingPlate(EnumFacing facing) {
        return BEARING_PLATES[facing.getIndex()];
    }
    public static EnumRenderPart getDeployer(EnumFacing facing, boolean itemEmpty) {
        return itemEmpty ? DEPLOYERS[facing.getIndex()] : DEPLOYER_HOLDINGS[facing.getIndex()];
    }
    public static EnumRenderPart getShaftlessCog(EnumFacing.Axis axis) {
        return SHAFTLESS_COGS[axis.ordinal()];
    }
    public static EnumRenderPart getBlaze(BlockBlazeBurner.Variant variant) {
        switch (variant) {
            case PASSIVE:
                return BLAZE_SMOULDERING;
            case HEATED:
                return BLAZE_KINDLED;
            case SUPERHEATED:
                return BLAZE_SEETHING;
            default:
                throw new IllegalArgumentException("Unexpected value: " + variant);
        }
    }
    public static EnumRenderPart getCraftingCover(EnumFacing facing) {
        return CRAFTER_COVERS[facing.getHorizontalIndex()];
    }
    public static EnumRenderPart getFurnaceFlywheel(EnumFacing outputFacing) {
        switch (outputFacing) {
            case NORTH:
                return FLYWHEEL_N;
            case EAST:
                return FLYWHEEL_E;
            case SOUTH:
                return FLYWHEEL_S;
            case WEST:
                return FLYWHEEL_W;
            default:
                throw new IllegalArgumentException("Unexpected value: " + outputFacing);
        }
    }
}
