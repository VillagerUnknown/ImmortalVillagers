package me.villagerunknown.immortalvillagers.mixin;

import me.villagerunknown.immortalvillagers.Immortalvillagers;
import me.villagerunknown.platform.util.MathUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.npc.villager.Villager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Villager.class)
public class VillagerEntityMixin {
	
	@Inject(method = "thunderHit", at = @At("HEAD"), cancellable = true)
	public void thunderHit(ServerLevel level, LightningBolt lightningBolt, CallbackInfo ci) {
		if( !MathUtil.hasChance( Immortalvillagers.CONFIG.witchConversionChance ) ) {
			ci.cancel();
		} // if
	}

}
