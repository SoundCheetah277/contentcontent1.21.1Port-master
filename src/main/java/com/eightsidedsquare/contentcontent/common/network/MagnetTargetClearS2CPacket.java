package com.eightsidedsquare.contentcontent.common.network;

import com.eightsidedsquare.contentcontent.client.ContentClient;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.network.packet.s2c.common.CustomPayloadS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
public class MagnetTargetClearS2CPacket {
   public static final Identifier ID = Identifier.of("contentcontent", "magnet_target_clear");

   public record Payload() implements CustomPayload {
      public static final CustomPayload.Id<Payload> ID = new CustomPayload.Id<>(Identifier.of("contentcontent", "magnet_target_clear"));
      public static final PacketCodec<RegistryByteBuf, Payload> CODEC =
              PacketCodec.unit(new Payload());

      @Override
      public CustomPayload.Id<? extends CustomPayload> getId() {
         return ID;
      }
   }

   // Make CODEC accessible from outer class
   public static final PacketCodec<RegistryByteBuf, Payload> CODEC = Payload.CODEC;
   public static final CustomPayload.Id<Payload> PAYLOAD_ID = Payload.ID;

   // Send from server → client
   public static void send(ServerPlayerEntity player) {
      Payload payload = new Payload();
      player.networkHandler.sendPacket(new CustomPayloadS2CPacket(payload));
   }
}