package com.eightsidedsquare.contentcontent.common.entity;

import com.eightsidedsquare.contentcontent.core.ContentEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.RegistryWrapper;
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

   @Override
   protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
      super.writeNbt(nbt, registryLookup);
      if (!this.getStack().isEmpty()) {
         // Save the item with registry reference info
         nbt.put("Item", this.getStack().encode(registryLookup));
      }
   }

   @Override
   public void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
      super.readNbt(nbt, registryLookup);
      if (nbt.contains("Item", NbtElement.COMPOUND_TYPE)) {
         this.setStack(ItemStack.fromNbt(registryLookup, nbt.getCompound("Item")).orElse(ItemStack.EMPTY));
      } else {
         this.setStack(ItemStack.EMPTY);
      }
   }
   @Override
   public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registryLookup) {
      return this.createNbt(registryLookup);
   }

   @Nullable
   public Packet<ClientPlayPacketListener> toUpdatePacket() {
      return BlockEntityUpdateS2CPacket.create(this);
   }
}
