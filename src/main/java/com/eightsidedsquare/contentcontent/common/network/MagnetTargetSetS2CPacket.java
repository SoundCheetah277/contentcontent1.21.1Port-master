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
import net.minecraft.util.math.GlobalPos;

public class MagnetTargetSetS2CPacket {
   public static final Identifier ID = Identifier.of("contentcontent", "magnet_target_set");

   // Simple payload record for the packet
   public record BasicPayload(Identifier id, PacketByteBuf buf) implements CustomPayload {
      @Override
      public Id<? extends CustomPayload> getId() {
         return new Id<>(id);
      }

      public void write(PacketByteBuf buffer) {
         buffer.writeBytes(buf.array());
      }
   }

   // Server-side: send packet
   public static void send(ServerPlayerEntity player, GlobalPos pos) {
      PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
      buf.writeGlobalPos(pos);
      BasicPayload payload = new BasicPayload(ID, buf);
      player.networkHandler.sendPacket(new CustomPayloadS2CPacket(payload));
   }

   // Client-side handler (register this with the channel ID somewhere in your ClientModInitializer)
   public static void handler(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender packetSender) {
      client.execute(() -> {
         ContentClient.magnetTarget = buf.readGlobalPos();
      });
   }
}