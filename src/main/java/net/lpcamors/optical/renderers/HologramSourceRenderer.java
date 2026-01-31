package net.lpcamors.optical.renderers;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import dev.engine_room.flywheel.lib.transform.TransformStack;
import net.createmod.catnip.render.CachedBuffers;
import net.createmod.catnip.render.SuperByteBuffer;
import net.lpcamors.optical.COPartialModels;
import net.lpcamors.optical.COUtils;
import net.lpcamors.optical.blocks.hologram_source.HologramSourceBlockEntity;
import net.lpcamors.optical.blocks.optical_source.BeamHelper;
import net.lpcamors.optical.config.COConfigs;
import net.lpcamors.optical.items.COItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HalfTransparentBlock;
import net.minecraft.world.level.block.StainedGlassPaneBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;


import java.util.List;
import java.util.Optional;

public class HologramSourceRenderer  extends SafeBlockEntityRenderer<HologramSourceBlockEntity> {

    private static final ModelResourceLocation TRIDENT_MODEL = ModelResourceLocation.vanilla("trident", "inventory");
    private static final ModelResourceLocation SPYGLASS_MODEL = ModelResourceLocation.vanilla("spyglass", "inventory");

    public HologramSourceRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    protected void renderSafe(HologramSourceBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {

        BlockState state = be.getBlockState();
        if (!be.isActive()) return;

        BeamHelper.BeamProperties beamProperties = be.getOptionalBeamProperties().get();
        Vec3i color = beamProperties.color;
        ms.pushPose();
        SuperByteBuffer cube = CachedBuffers.partial(COPartialModels.HOLOGRAM_BEAM, state).center().light(light)
                .color(color.getX(), color.getY(), color.getZ(), 255);
        cube.uncenter().renderInto(ms, buffer.getBuffer(RenderType.translucent()));
        cube.center().scale(1.1F).uncenter().renderInto(ms, buffer.getBuffer(getBeamRenderType()));
        ms.popPose();

        ItemStack heldItem = be.getItemStack();
        if (!be.isController() || heldItem.isEmpty()) return;

        ItemRenderer itemRenderer = Minecraft.getInstance()
                .getItemRenderer();
        boolean blockItem = itemRenderer.getModel(heldItem, null, null, 0)
                .isGui3d();
        double radius = (blockItem ? 1.3 : 0.7);
        if(heldItem.is(Items.TRIDENT) || heldItem.is(Items.SPYGLASS)) radius *= 0.5;
        Vec3 vec3 = be.getProjectionBox().getCenter().subtract(Vec3.atLowerCornerOf(be.getBlockPos()));
        ms.pushPose();
        TransformStack.of(ms).translate(vec3).scale((float) (be.getConnectionLength() * radius));
        BakedModel bakedmodel = itemRenderer.getModel(heldItem, be.getLevel(), null, 0);
        //ms.translate(-0.5, -0.5,-0.5);
        double ticks = 0.05D * (be.getLevel().getDayTime() + partialTicks);

        double yOffset = 0.1F * Math.cos(ticks);
        double rot = ticks;
        if(be.hasFixedAngle()){
            rot = be.getFixedAngle() * Math.PI / 180;
            yOffset = 0;
        } else if(be.getMode().equals(HologramSourceBlockEntity.Mode.ROTATING_CLOCKWISE)) {
            rot *= -1;
        }

        float normalAlpha = COConfigs.client().hologramDisplay.normalTransparency.getF();
        float additiveAlpha = COConfigs.client().hologramDisplay.additiveTransparency.getF();
        normalAlpha *= COConfigs.client().hologramDisplay.generalTransparency.getF();
        additiveAlpha *= COConfigs.client().hologramDisplay.generalTransparency.getF();

        this.render(beamProperties, itemRenderer, heldItem, ItemDisplayContext.FIXED, false, ms, buffer, LightTexture.FULL_BRIGHT, overlay, bakedmodel,
                buffer.getBuffer(RenderType.translucent()), normalAlpha, rot, yOffset);

        ms.popPose();
        ms.pushPose();
        TransformStack.of(ms).translate(vec3).scale((float) (be.getConnectionLength() * radius * 1.1));//;
        BakedModel bakedmodel1 = itemRenderer.getModel(heldItem, be.getLevel(), null, 0);
        this.render(beamProperties, itemRenderer, heldItem, ItemDisplayContext.FIXED, false, ms, buffer, LightTexture.FULL_BRIGHT, overlay, bakedmodel1,
                buffer.getBuffer(getBeamRenderType()), additiveAlpha, rot, yOffset);
        ms.popPose();

    }

    public void render(BeamHelper.BeamProperties beamProperties, ItemRenderer renderer, ItemStack itemStack,
                       ItemDisplayContext itemDisplayContext, boolean p_115146_, PoseStack ms, MultiBufferSource buffer, int light, int overlay,
                       BakedModel bakedModel, VertexConsumer vertexConsumer, float alpha, double rotation, double yOffset) {
        if (!itemStack.isEmpty()) {
            ms.pushPose();
            boolean flag = itemDisplayContext == ItemDisplayContext.GUI || itemDisplayContext == ItemDisplayContext.GROUND || itemDisplayContext == ItemDisplayContext.FIXED;

            if (flag) {
                if (itemStack.is(Items.TRIDENT)) {
                    bakedModel = renderer.getItemModelShaper().getModelManager().getModel(TRIDENT_MODEL);
                } else if (itemStack.is(Items.SPYGLASS)) {
                    bakedModel = renderer.getItemModelShaper().getModelManager().getModel(SPYGLASS_MODEL);
                }
            }

            // Apply model transformation for the given display context (Fabric equivalent of ForgeHooksClient.handleCameraTransforms)
            bakedModel.getTransforms().getTransform(itemDisplayContext).apply(p_115146_, ms);
            ms.translate(-0.5F, -0.5F, -0.5F);

            TransformStack.of(ms)
                    .translate(0,yOffset,0)
                    .rotateCentered((float) rotation, Direction.UP)

            ;//;
            if (!bakedModel.isCustomRenderer() && (!itemStack.is(Items.TRIDENT) || flag)) {
                // In Fabric/vanilla, render the model directly instead of using Forge's getRenderPasses
                this.renderModelLists(beamProperties, renderer, bakedModel, itemStack, light, overlay, ms, vertexConsumer, alpha);
            }
            // TODO: Custom item renderer support removed - Fabric does not have IClientItemExtensions equivalent
            // Original Forge code: IClientItemExtensions.of(itemStack).getCustomRenderer().renderByItem(...)

            ms.popPose();
        }
    }

    public RenderType getBeamRenderType(){
        RenderType renderType = RenderType.create("create_optical:hologram", DefaultVertexFormat.BLOCK,
                VertexFormat.Mode.QUADS, 256, true, true, RenderType.CompositeState.builder()
                        .setTextureState(new RenderStateShard.TextureStateShard(TextureAtlas.LOCATION_BLOCKS, false, false))
                        .setShaderState(new RenderStateShard.ShaderStateShard(GameRenderer::getRendertypeSolidShader))
                        .setTransparencyState(new RenderStateShard.TransparencyStateShard("hologram_transparency", () -> {
                            RenderSystem.enableBlend();
                            //RenderSystem.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
                            RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
                        }, () -> {
                            RenderSystem.disableBlend();
                            RenderSystem.defaultBlendFunc();
                        }))
                        .setCullState(new RenderStateShard.CullStateShard(false))
                        .setLightmapState(new RenderStateShard.LightmapStateShard(true))
                        .setOverlayState(new RenderStateShard.OverlayStateShard(false))
                        .createCompositeState(true));
        return renderType;
    }

    public void renderModelLists(BeamHelper.BeamProperties beamProperties, ItemRenderer renderer, BakedModel p_115190_, ItemStack p_115191_, int p_115192_, int p_115193_, PoseStack p_115194_, VertexConsumer p_115195_, float alpha) {
        RandomSource randomsource = RandomSource.create();
        long i = 42L;

        for(Direction direction : Direction.values()) {
            randomsource.setSeed(42L);
            this.renderQuadList(beamProperties, p_115194_, p_115195_, p_115190_.getQuads((BlockState)null, direction, randomsource), p_115191_, p_115192_, p_115193_, alpha);
        }

        randomsource.setSeed(42L);
        this.renderQuadList(beamProperties, p_115194_, p_115195_, p_115190_.getQuads((BlockState)null, (Direction)null, randomsource), p_115191_, p_115192_, p_115193_, alpha);
    }

    public void renderQuadList(BeamHelper.BeamProperties beamProperties, PoseStack p_115163_, VertexConsumer p_115164_, List<BakedQuad> p_115165_, ItemStack p_115166_, int p_115167_, int p_115168_, float alpha) {
        PoseStack.Pose posestack$pose = p_115163_.last();
        Vec3i color = beamProperties.color;
        // Apply alpha to color channels since vanilla putBulkData doesn't have alpha parameter
        float r = (float) (color.getX() / 255D) * alpha;
        float g = (float) (color.getY() / 255D) * alpha;
        float b = (float) (color.getZ() / 255D) * alpha;
        // Vanilla putBulkData expects float[] for brightness per vertex (4 vertices per quad)
        float[] brightness = new float[]{1.0F, 1.0F, 1.0F, 1.0F};
        int[] lightmap = new int[]{p_115167_, p_115167_, p_115167_, p_115167_};
        for(BakedQuad bakedquad : p_115165_) {
            p_115164_.putBulkData(posestack$pose, bakedquad, brightness, r, g, b, lightmap, p_115168_, true);
        }

    }

    @Override
    public int getViewDistance() {
        return 512;
    }
}
