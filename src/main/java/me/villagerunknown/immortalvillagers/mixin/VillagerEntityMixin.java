package me.villagerunknown.immortalvillagers.mixin;

import me.villagerunknown.immortalvillagers.Immortalvillagers;
import me.villagerunknown.platform.util.MathUtil;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(VillagerEntity.class)
public class VillagerEntityMixin {
	
	@Inject(method = "onStruckByLightning", at = @At("HEAD"), cancellable = true)
	public void onStruckByLightning(ServerWorld world, LightningEntity lightning, CallbackInfo ci) {
		if( !MathUtil.hasChance( Immortalvillagers.CONFIG.witchConversionChance ) ) {
			ci.cancel();
		} // if
	}

}
