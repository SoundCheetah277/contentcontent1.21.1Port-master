package com.eightsidedsquare.contentcontent.common.entity;

import com.eightsidedsquare.contentcontent.core.ContentEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public class DisplayCaseBlockEntity extends BlockEntity {
   @Nullable
   private ItemStack stack;

   public DisplayCaseBlockEntity(BlockPos pos, BlockState state) {
      super(ContentEntities.DISPLAY_CASE, pos, state);
      this.setStack(ItemStack.EMPTY);
   }

   public void setStack(@Nullable ItemStack stack) {
      this.stack = stack != null ? stack : ItemStack.EMPTY;
      this.markDirty();
      if (this.world != null) {
         this.world.updateListeners(this.pos, this.getCachedState(), this.getCachedState(), 3);
      }
   }

   public ItemStack getStack() {
      return this.stack != null ? this.stack : ItemStack.EMPTY;
   }

   protected void writeNbt(NbtCompound nbt) {
      nbt.put("Item", this.getStack().writeNbt(new NbtCompound()));
   }

   public void readNbt(NbtCompound nbt) {
      this.setStack(ItemStack.fromNbt(nbt.getCompound("Item")));
   }

   public NbtCompound toInitialChunkDataNbt() {
      return this.createNbt();
   }

   @Nullable
   public Packet<ClientPlayPacketListener> toUpdatePacket() {
      return BlockEntityUpdateS2CPacket.create(this);
   }
}
