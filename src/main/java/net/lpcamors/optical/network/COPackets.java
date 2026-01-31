package net.lpcamors.optical.network;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.lpcamors.optical.CreateOptical;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public class COPackets {

    public static final ResourceLocation CONFIGURE_HOLOGRAM = CreateOptical.loc("configure_hologram");

    public static void register() {
        // Server-side packet receiver
        ServerPlayNetworking.registerGlobalReceiver(CONFIGURE_HOLOGRAM, (server, player, handler, buf, responseSender) -> {
            ConfigureHologramSourcePacket packet = new ConfigureHologramSourcePacket(buf);
            server.execute(() -> packet.handle(player));
        });
    }

    public static void registerClient() {
        // Client-side packet registration if needed for server->client packets
    }

    public static void sendToServer(ConfigureHologramSourcePacket packet) {
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        packet.write(buf);
        ClientPlayNetworking.send(CONFIGURE_HOLOGRAM, buf);
    }
}
