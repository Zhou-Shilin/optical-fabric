package net.lpcamors.optical.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import net.createmod.catnip.render.CachedBuffers;
import net.createmod.catnip.render.SuperByteBuffer;
import net.lpcamors.optical.blocks.IBeamSource;
import net.lpcamors.optical.blocks.optical_source.OpticalSourceBlock;
import net.lpcamors.optical.blocks.optical_source.OpticalSourceBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class OpticalSourceRenderer extends KineticBlockEntityRenderer<OpticalSourceBlockEntity> {

     public OpticalSourceRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public boolean shouldRenderOffScreen(OpticalSourceBlockEntity p_112306_) {
        return true;
    }

    @Override
    protected void renderSafe(OpticalSourceBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        super.renderSafe(be, partialTicks, ms, buffer, light, overlay);


        if(be.shouldRendererLaserBeam()) {
            IBeamSource.ClientSide.renderLaserBeam(be, be.getBlockState(), ms, buffer);
        }
    }

    @Override
    public boolean shouldRender(OpticalSourceBlockEntity p_173568_, Vec3 p_173569_) {
        return true;
    }

    @Override
    public int getViewDistance() {
        return 256;
    }



    @Override
    protected SuperByteBuffer getRotatedModel(OpticalSourceBlockEntity opticalLaserSourceBlockEntity, BlockState state) {
        return CachedBuffers.partialFacing(AllPartialModels.SHAFT_HALF, state, state
                .getValue(OpticalSourceBlock.HORIZONTAL_FACING)
                .getOpposite());
    }

}
