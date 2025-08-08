package com.eightsidedsquare.contentcontent.mixin;

import com.eightsidedsquare.contentcontent.core.ContentSounds;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.NoteBlock;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({NoteBlock.class})
public class NoteBlockMixin {
   @Shadow
   @Final
   public static IntProperty NOTE;

   @Nullable
   private SoundEvent getCustomNote(Block block) {
      if (block.equals(Blocks.RAW_COPPER_BLOCK)) {
         return ContentSounds.NOTE_HORN;
      } else {
         return !block.equals(Blocks.SKELETON_SKULL) && !block.equals(Blocks.SKELETON_WALL_SKULL) ? null : ContentSounds.NOTE_ELECTRIC_GUITAR;
      }
   }

   @Inject(
      method = {"onSyncedBlockEvent"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void onSyncedBlockEvent(BlockState state, World world, BlockPos pos, int type, int data, CallbackInfoReturnable<Boolean> cir) {
      SoundEvent customNote = this.getCustomNote(world.getBlockState(pos.down()).getBlock());
      if (customNote != null) {
         int i = (Integer)state.get(NOTE);
         float f = (float)Math.pow(2.0, (i - 12) / 12.0);
         world.playSound(null, pos, customNote, SoundCategory.RECORDS, 3.0F, f);
         world.addParticle(ParticleTypes.NOTE, pos.getX() + 0.5, pos.getY() + 1.2, pos.getZ() + 0.5, i / 24.0F, 0.0, 0.0);
         cir.setReturnValue(true);
      }
   }
}
