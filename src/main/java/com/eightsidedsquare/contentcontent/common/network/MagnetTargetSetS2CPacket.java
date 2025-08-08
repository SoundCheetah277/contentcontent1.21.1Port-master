package com.eightsidedsquare.contentcontent.common.network;

import com.eightsidedsquare.contentcontent.client.ContentClient;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.GlobalPos;

public class MagnetTargetSetS2CPacket {
   public static final Identifier ID = Identifier.of("contentcontent", "magnet_target_set");

   public static void send(ServerPlayerEntity player, GlobalPos pos) {
      PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
      buf.writeGlobalPos(pos);
      ServerPlayNetworking.send(player, ID, buf);
   }

   public static void handler(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender packetSender) {
      ContentClient.magnetTarget = buf.readGlobalPos();
   }
}
