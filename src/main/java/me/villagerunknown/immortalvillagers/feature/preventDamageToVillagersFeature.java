package me.villagerunknown.immortalvillagers.feature;

import me.villagerunknown.immortalvillagers.Immortalvillagers;
import me.villagerunknown.platform.util.EntityUtil;
import me.villagerunknown.platform.util.MathUtil;
import me.villagerunknown.platform.util.PositionUtil;
import me.villagerunknown.platform.util.WorldUtil;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.mob.ZombieVillagerEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.nbt.NbtOps;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.List;

public class preventDamageToVillagersFeature {
	
	public static List<EntityType<?>> zombieConversionTypes = List.of(
			EntityType.ZOMBIE,
			EntityType.ZOMBIE_VILLAGER
	);
	
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
			Entity sourceEntity = damageSource.getSource();
			
			if( null != sourceEntity && zombieConversionTypes.contains( sourceEntity.getType() ) && MathUtil.hasChance( Immortalvillagers.CONFIG.zombieConversionChance ) ) {
				convertToZombie((VillagerEntity) entity, (ZombieEntity) sourceEntity);
				
				return false;
			} // if
			
			if( Immortalvillagers.CONFIG.enableVillagerDamageButRespawn ) {
				
				if (entity instanceof VillagerEntity) {
					// Find the bed position
					BlockPos bedPos = PositionUtil.findNearestBed(entity, Immortalvillagers.CONFIG.maxSearchRadiusInBlocks);
					
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
				
			} //if
			
			return true;
		});
	}
	
	public static ZombieEntity convertToZombie( VillagerEntity villagerEntity, ZombieEntity zombieEntity ) {
		ServerWorld world = WorldUtil.getServerWorld( villagerEntity.getWorld() );
		
		ZombieVillagerEntity zombieVillagerEntity = villagerEntity.convertTo(EntityType.ZOMBIE_VILLAGER, false);
		
		if (zombieVillagerEntity != null) {
			zombieVillagerEntity.initialize(world, world.getLocalDifficulty(zombieVillagerEntity.getBlockPos()), SpawnReason.CONVERSION, new ZombieEntity.ZombieData(false, true));
			zombieVillagerEntity.setVillagerData(villagerEntity.getVillagerData());
			zombieVillagerEntity.setGossipData(villagerEntity.getGossip().serialize(NbtOps.INSTANCE));
			zombieVillagerEntity.setOfferData(villagerEntity.getOffers().copy());
			zombieVillagerEntity.setXp(villagerEntity.getExperience());
			if (!zombieEntity.isSilent()) {
				world.syncWorldEvent(null, 1026, zombieEntity.getBlockPos(), 0);
			} // if
			
			if( Immortalvillagers.CONFIG.reportVillagerConversionsToLogs ) {
				EntityUtil.reportConversionToLog( Immortalvillagers.LOGGER, villagerEntity, zombieEntity );
			} // if
		} // if
		
		return zombieVillagerEntity;
	}
	
}
