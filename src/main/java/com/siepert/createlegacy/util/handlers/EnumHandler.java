package com.siepert.createlegacy.util.handlers;

import net.minecraft.util.IStringSerializable;

public class EnumHandler {
    public static enum OreEnumType implements IStringSerializable {
        //actual ores
        COPPER(0, "copper"),
        ZINC(1, "zinc"),

        //Stone types
        ASURINE(2, "asurine"),
        CRIMSITE(3, "crimsite"),
        LIMESTONE(4, "limestone"),
        OCHRUM(5, "ochrum"),
        SCORCHIA(6, "scorchia"),
        SCORIA(7, "scoria"),
        VERIDIUM(8, "veridium");


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
}
