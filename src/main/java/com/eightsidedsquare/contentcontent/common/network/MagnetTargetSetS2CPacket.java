package com.eightsidedsquare.contentcontent.common.network;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.network.packet.s2c.common.CustomPayloadS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.GlobalPos;
public class MagnetTargetSetS2CPacket {
   public static final Identifier ID = Identifier.of("contentcontent", "magnet_target_set");

   // Move the record definition to be the main payload class
   public record Payload(GlobalPos pos) implements CustomPayload {
      public static final CustomPayload.Id<Payload> ID = new CustomPayload.Id<>(Identifier.of("contentcontent", "magnet_target_set"));
      public static final PacketCodec<RegistryByteBuf, Payload> CODEC =
              PacketCodec.tuple(GlobalPos.PACKET_CODEC, Payload::pos, Payload::new);

      @Override
      public CustomPayload.Id<? extends CustomPayload> getId() {
         return ID;
      }
   }

   // Make CODEC accessible from outer class
   public static final PacketCodec<RegistryByteBuf, Payload> CODEC = Payload.CODEC;
   public static final CustomPayload.Id<Payload> PAYLOAD_ID = Payload.ID;

   // Server-side: send packet
   public static void send(ServerPlayerEntity player, GlobalPos pos) {
      Payload payload = new Payload(pos);
      player.networkHandler.sendPacket(new CustomPayloadS2CPacket(payload));
   }
}