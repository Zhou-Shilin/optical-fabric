package net.lpcamors.optical.network;

import net.lpcamors.optical.blocks.hologram_source.HologramSourceBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;

public class ConfigureHologramSourcePacket {

    private BlockPos pos;
    private CompoundTag tag;

    public ConfigureHologramSourcePacket(FriendlyByteBuf buffer) {
        this.pos = buffer.readBlockPos();
        this.tag = buffer.readNbt();
    }

    public ConfigureHologramSourcePacket(BlockPos blockPos, CompoundTag tag) {
        this.pos = blockPos;
        this.tag = tag;
    }

    public void write(FriendlyByteBuf buffer) {
        buffer.writeBlockPos(pos);
        buffer.writeNbt(tag);
    }

    public void handle(ServerPlayer player) {
        if (player == null || player.level() == null) return;
        
        BlockEntity blockEntity = player.level().getBlockEntity(pos);
        if (!(blockEntity instanceof HologramSourceBlockEntity be)) return;
        
        // Check if player is close enough
        if (player.distanceToSqr(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5) > 64) return;
        
        try {
            HologramSourceBlockEntity controller = be.getController();
            controller.setMode(tag.getInt("ModeIndex"));
            controller.setFixedAngle(tag.getInt("Angle"));
            be.sendData();
            controller.onConnection(controller.getBlockPos(), false, opBe -> opBe.ifPresent(be1 -> be1.updateConnection(controller)));
        } catch (Exception ex) {
            System.out.println("Unable to apply hologram configuration: " + ex.getMessage());
        }
    }
}
