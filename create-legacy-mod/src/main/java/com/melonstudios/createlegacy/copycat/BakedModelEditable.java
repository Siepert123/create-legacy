package com.melonstudios.createlegacy.copycat;

import epicsquid.mysticallib.model.CustomModelBase;
import epicsquid.mysticallib.model.DefaultTransformations;
import epicsquid.mysticallib.model.ModelUtil;
import epicsquid.mysticallib.model.block.BakedModelBlock;
import epicsquid.mysticallib.model.parts.Cube;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.property.IExtendedBlockState;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.vecmath.Matrix4f;
import java.util.*;
import java.util.function.Function;

public abstract class BakedModelEditable extends BakedModelBlock {
    public Map<String, List<BakedQuad>> data = new HashMap<>();
    Cube cube;

    public BakedModelEditable(@Nullable IModelState state, @Nonnull VertexFormat format, @Nonnull Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter, @Nonnull CustomModelBase model) {
        super(state, format, bakedTextureGetter, model);
        cube = ModelUtil
                .makeCube(format, 0, 0, 0, 1, 1, 1, ModelUtil.FULL_FACES,
                        new TextureAtlasSprite[]{texwest, texeast, texdown, texup, texnorth, texsouth}, 0);
    }

    public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand) {
        List<BakedQuad> finalQuads = new ArrayList<>();
        if (state instanceof IExtendedBlockState && state.getBlock() instanceof ICopycatBlock) {
            IBlockState texState = ((IExtendedBlockState) state).getValue(((ICopycatBlock)state.getBlock()).getStateProperty());
            String dataId = (texState == null ? "null" : texState.toString()) + "_" + state.toString() + "_" + (side == null ? "null" : side.toString()) + (
                    MinecraftForgeClient.getRenderLayer() == null ?
                            "null" :
                            MinecraftForgeClient.getRenderLayer().toString());
            if (!data.containsKey(dataId)) {
                List<BakedQuad> quads = new ArrayList<>();
                TextureAtlasSprite[] sprites = new TextureAtlasSprite[] { getParticleTexture() };
                int[] tintIndices = new int[] { 0 };
                boolean usingDefault = true;
                if (texState != null && texState.getBlock() != Blocks.AIR) {
                    IBakedModel model = Minecraft.getMinecraft().getBlockRendererDispatcher().getModelForState(texState);
                    sprites[0] = model.getParticleTexture();
                    List<BakedQuad> texQuads = model.getQuads(texState, side, rand);
                    if (texQuads.size() > 0) {
                        sprites = new TextureAtlasSprite[texQuads.size()];
                        tintIndices = new int[texQuads.size()];
                        for (int i = 0; i < texQuads.size(); i++) {
                            if (texQuads.get(i).hasTintIndex()) {
                                tintIndices[i] = texQuads.get(i).getTintIndex();
                            } else {
                                tintIndices[i] = -1;
                            }
                            sprites[i] = texQuads.get(i).getSprite();
                        }
                    }
                    usingDefault = false;
                }
                if (usingDefault && MinecraftForgeClient.getRenderLayer() == BlockRenderLayer.CUTOUT_MIPPED
                        || !usingDefault && MinecraftForgeClient.getRenderLayer() == texState.getBlock().getBlockLayer()) {
                    for (int i = 0; i < sprites.length; i++) {
                        addGeometry(quads, side, state, new TextureAtlasSprite[] { sprites[i], sprites[i], sprites[i], sprites[i], sprites[i], sprites[i] },
                                tintIndices[i]);
                    }
                }
                data.put(dataId, quads);
                finalQuads.addAll(quads);
            } else if (!dataId.equals("null")) {
                finalQuads.addAll(data.get(dataId));
            }
        }
        if (state == null) {
            addItemModel(finalQuads, side);
        }
        return finalQuads;
    }

    public abstract void addItemModel(List<BakedQuad> quads, EnumFacing side);

    public abstract void addGeometry(List<BakedQuad> quads, EnumFacing side, IBlockState state, TextureAtlasSprite[] texes, int tintIndex);

    @Override
    public boolean isAmbientOcclusion() {
        return true;
    }

    @Override
    public boolean isGui3d() {
        return true;
    }

    @Override
    public boolean isBuiltInRenderer() {
        return false;
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {
        return particle;
    }

    @Override
    public ItemOverrideList getOverrides() {
        return new ItemOverrideList(Arrays.asList());
    }

    @Override
    public Pair<? extends IBakedModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType type) {
        Matrix4f matrix = null;
        if (DefaultTransformations.blockTransforms.containsKey(type)) {
            matrix = DefaultTransformations.blockTransforms.get(type).getMatrix();
            return Pair.of(this, matrix);
        }
        return net.minecraftforge.client.ForgeHooksClient.handlePerspective(this, type);
    }
}
