package me.villagerunknown.immortalvillagers.feature;

import me.villagerunknown.immortalvillagers.Immortalvillagers;
import me.villagerunknown.platform.util.EntityUtil;
import me.villagerunknown.platform.util.PositionUtil;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class preventDamageToVillagersFeature {
	
	public static void execute() {
		registerInvincibleVillagers();
		registerRespawnableVillagers();
	}
	
	private static void registerInvincibleVillagers() {
		ServerLivingEntityEvents.ALLOW_DAMAGE.register((entity, damageSource, amount) -> {
			if( !Immortalvillagers.CONFIG.enableVillagerDamageButRespawn ) {
				if (entity instanceof VillagerEntity) {
					// Cancel the damage if the entity is a villager
					return false;
				} // if
			} else {
				if (entity instanceof VillagerEntity) {
					// Send a message on damage
					if( Immortalvillagers.CONFIG.reportVillagerDamageToLogs ) {
						Entity damageSourceEntity = damageSource.getSource();
						
						if( null != damageSourceEntity ) {
							EntityUtil.reportAttackToLog( Immortalvillagers.LOGGER, entity, damageSourceEntity );
						} // if
					} // if
				} // if
			} // if, else
			
			return true;
		});
	}
	
	private static void registerRespawnableVillagers() {
		ServerLivingEntityEvents.ALLOW_DEATH.register((entity, damageSource, amount) -> {
			if( Immortalvillagers.CONFIG.enableVillagerDamageButRespawn ) {
				
				if (entity instanceof VillagerEntity) {
					// Find the bed position
					BlockPos bedPos = PositionUtil.findNearestBed(entity, Immortalvillagers.CONFIG.maxSearchRadius);
					
					if( null != bedPos ) {
						// Make the villager invisible (dead)
						EntityUtil.addStatusEffect(entity, StatusEffects.INVISIBILITY, 7, 1, false, false, false);
						
						// Add particles to the world simulating death
						EntityUtil.spawnParticles( entity, 1, ParticleTypes.CLOUD.getType(), 20, 0.1, 0.1, 0.1, 0.005 );
						
						// Teleport the original villager
						EntityUtil.teleport(entity, new Vec3d(bedPos.getX() + 0.5, bedPos.getY() + 1, bedPos.getZ() + 0.5));
						
						// Play villager death sound
						EntityUtil.playSound( entity, SoundEvents.ENTITY_VILLAGER_DEATH, SoundCategory.NEUTRAL, 1.0F, 1.0F, false );
						
						// Report the death
						if( Immortalvillagers.CONFIG.reportVillagerRespawnsToLogs ) {
							Entity damageSourceEntity = damageSource.getSource();
							
							if( null != damageSourceEntity ) {
								EntityUtil.reportKillToLog( Immortalvillagers.LOGGER, entity, damageSourceEntity );
							} // if
						} // if
					} // if
					
					EntityUtil.setHealthToMax( entity );
					
					return false;
				} // if
				
			} // if
			
			return true;
		});
	}
	
}
