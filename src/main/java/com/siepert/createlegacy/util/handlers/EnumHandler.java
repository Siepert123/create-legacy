package com.siepert.createlegacy.util.handlers;

import com.siepert.createapi.Spaghetti;
import net.minecraft.util.IStringSerializable;

/**A main class holding most of the enums used for block variants.
 * @see ModSoundHandler
 * @see RecipeHandler
 * @see RegistryHandler*/

@Spaghetti(why = "this is horrible ;-;")
public class EnumHandler {
    public static enum OreEnumType implements IStringSerializable {
        //actual ores
        COPPER(0, "copper"),
        ZINC(1, "zinc");


        private static final OreEnumType[] META_LOOKUP = new OreEnumType[values().length];
        private final int meta;
        private final String name, unlocalizedName;


        private OreEnumType(int meta, String name) {
            this(meta, name, name);
        }

        private OreEnumType(int meta, String name, String unlocalizedName) {
            this.meta = meta;
            this.name = name;
            this.unlocalizedName = unlocalizedName;
        }

        @Override
        public String getName() {
            return this.name;
        }

        public int getMeta() {
            return meta;
        }

        public String getUnlocalizedName() {
            return unlocalizedName;
        }

        @Override
        public String toString() {
            return this.name;
        }

        public static OreEnumType byMetaData(int meta) {
            return META_LOOKUP[meta];
        }

        static {
            for(OreEnumType oreEnumType : values()) {
                META_LOOKUP[oreEnumType.getMeta()] = oreEnumType;
            }
        }
    }

    public static enum MaterialStorageEnumType implements IStringSerializable {

        COPPER(0, "copper"),
        ZINC(1, "zinc"),
        BRASS(2, "brass"),
        ANDESITE_ALLOY(3, "andesite_alloy");


        private static final MaterialStorageEnumType[] META_LOOKUP = new MaterialStorageEnumType[values().length];
        private final int meta;
        private final String name, unlocalizedName;


        private MaterialStorageEnumType(int meta, String name) {
            this(meta, name, name);
        }

        private MaterialStorageEnumType(int meta, String name, String unlocalizedName) {
            this.meta = meta;
            this.name = name;
            this.unlocalizedName = unlocalizedName;
        }

        @Override
        public String getName() {
            return this.name;
        }

        public int getMeta() {
            return meta;
        }

        public String getUnlocalizedName() {
            return unlocalizedName;
        }

        @Override
        public String toString() {
            return this.name;
        }

        public static MaterialStorageEnumType byMetaData(int meta) {
            return META_LOOKUP[meta];
        }

        static {
            for(MaterialStorageEnumType materialStorageEnumType : values()) {
                META_LOOKUP[materialStorageEnumType.getMeta()] = materialStorageEnumType;
            }
        }
    }

    public static enum CasingMaterialEnumType implements IStringSerializable {

        ANDESITE(0, "andesite"),
        COPPER(1, "copper"),
        BRASS(2, "brass"),
        TRAIN(3, "train");


        private static final CasingMaterialEnumType[] META_LOOKUP = new CasingMaterialEnumType[values().length];
        private final int meta;
        private final String name, unlocalizedName;


        private CasingMaterialEnumType(int meta, String name) {
            this(meta, name, name);
        }

        private CasingMaterialEnumType(int meta, String name, String unlocalizedName) {
            this.meta = meta;
            this.name = name;
            this.unlocalizedName = unlocalizedName;
        }

        @Override
        public String getName() {
            return this.name;
        }

        public int getMeta() {
            return meta;
        }

        public String getUnlocalizedName() {
            return unlocalizedName;
        }

        @Override
        public String toString() {
            return this.name;
        }

        public static CasingMaterialEnumType byMetaData(int meta) {
            return META_LOOKUP[meta];
        }

        static {
            for(CasingMaterialEnumType CasingMaterialEnumType : values()) {
                META_LOOKUP[CasingMaterialEnumType.getMeta()] = CasingMaterialEnumType;
            }
        }
    }

    public static enum StoneEnumType implements IStringSerializable {

        CALCITE(0, "calcite"),
        TUFF(1, "tuff"),
        ASURINE(2, "asurine"),
        CRIMSITE(3, "crimsite"),
        LIMESTONE(4, "limestone"),
        OCHRUM(5, "ochrum"),
        SCORCHIA(6, "scorchia"),
        SCORIA(7, "scoria"),
        VERIDIUM(8, "veridium");


        private static final StoneEnumType[] META_LOOKUP = new StoneEnumType[values().length];
        private final int meta;
        private final String name, unlocalizedName;


        private StoneEnumType(int meta, String name) {
            this(meta, name, name);
        }

        private StoneEnumType(int meta, String name, String unlocalizedName) {
            this.meta = meta;
            this.name = name;
            this.unlocalizedName = unlocalizedName;
        }

        @Override
        public String getName() {
            return this.name;
        }

        public int getMeta() {
            return meta;
        }

        public String getUnlocalizedName() {
            return unlocalizedName;
        }

        @Override
        public String toString() {
            return this.name;
        }

        public static StoneEnumType byMetaData(int meta) {
            return META_LOOKUP[meta];
        }

        static {
            for(StoneEnumType stoneEnumType : values()) {
                META_LOOKUP[stoneEnumType.getMeta()] = stoneEnumType;
            }
        }
    }

    public static enum DecoStoneEnumType implements IStringSerializable {

        ASURINE(0, "asurine"),
        CALCITE(1, "calcite"),
        CRIMSITE(2, "crimsite"),
        DEEPSLATE(3, "deepslate"),
        DRIPSTONE(4, "dripstone"),
        LIMESTONE(5, "limestone"),
        OCHRUM(6, "ochrum"),
        SCORCHIA(7, "scorchia"),
        SCORIA(8, "scoria"),
        TUFF(9, "tuff"),
        VERIDIUM(10, "veridium"),
        ANDESITE(11, "andesite"),
        DIORITE(12, "diorite"),
        GRANITE(13, "granite");


        private static final DecoStoneEnumType[] META_LOOKUP = new DecoStoneEnumType[values().length];
        private final int meta;
        private final String name, unlocalizedName;


        private DecoStoneEnumType(int meta, String name) {
            this(meta, name, name);
        }

        private DecoStoneEnumType(int meta, String name, String unlocalizedName) {
            this.meta = meta;
            this.name = name;
            this.unlocalizedName = unlocalizedName;
        }

        @Override
        public String getName() {
            return this.name;
        }

        public int getMeta() {
            return meta;
        }

        public String getUnlocalizedName() {
            return unlocalizedName;
        }

        @Override
        public String toString() {
            return this.name;
        }

        public static DecoStoneEnumType byMetaData(int meta) {
            return META_LOOKUP[meta];
        }

        static {
            for(DecoStoneEnumType decoStoneEnumType : values()) {
                META_LOOKUP[decoStoneEnumType.getMeta()] = decoStoneEnumType;
            }
        }
    }

    public static enum KineticUtilityEnumType implements IStringSerializable {
        //actual ores
        GEARBOX(0, "gearbox"),
        CLUTCH(1, "clutch"),
        GEARSHIFT(2, "gearshift"),
        AXLE_ENCASED_ANDESITE(3, "axle_encased_andesite"),
        AXLE_ENCASED_BRASS(4, "axle_encased_brass");


        private static final KineticUtilityEnumType[] META_LOOKUP = new KineticUtilityEnumType[values().length];
        private final int meta;
        private final String name, unlocalizedName;


        private KineticUtilityEnumType(int meta, String name) {
            this(meta, name, name);
        }

        private KineticUtilityEnumType(int meta, String name, String unlocalizedName) {
            this.meta = meta;
            this.name = name;
            this.unlocalizedName = unlocalizedName;
        }

        @Override
        public String getName() {
            return this.name;
        }

        public int getMeta() {
            return meta;
        }

        public String getUnlocalizedName() {
            return unlocalizedName;
        }

        @Override
        public String toString() {
            return this.name;
        }

        public static KineticUtilityEnumType byMetaData(int meta) {
            return META_LOOKUP[meta];
        }

        static {
            for(KineticUtilityEnumType kineticUtilityEnumType : values()) {
                META_LOOKUP[kineticUtilityEnumType.getMeta()] = kineticUtilityEnumType;
            }
        }
    }
}
