package com.eightsidedsquare.contentcontent.common.network;

import com.eightsidedsquare.contentcontent.client.ContentClient;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.network.packet.s2c.common.CustomPayloadS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class MagnetTargetClearS2CPacket {
   public static final Identifier ID = Identifier.of("contentcontent", "magnet_target_clear");

   // Minimal CustomPayload wrapper for this packet
   public record BasicPayload(Identifier id, PacketByteBuf buf) implements CustomPayload {
      @Override
      public Id<? extends CustomPayload> getId() {
         return new Id<>(id);
      }

      public void write(PacketByteBuf buffer) {
         buffer.writeBytes(buf.array());
      }
   }

   // Send from server → client
   public static void send(ServerPlayerEntity player) {
      PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
      BasicPayload payload = new BasicPayload(ID, buf);
      player.networkHandler.sendPacket(new CustomPayloadS2CPacket(payload));
   }

   // Client handler
   public static void handler(MinecraftClient client, ClientPlayNetworkHandler handler,
                              PacketByteBuf buf, PacketSender packetSender) {
      client.execute(() -> {
         ContentClient.magnetTarget = null;
      });
   }
}
