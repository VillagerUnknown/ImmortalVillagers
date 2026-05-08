package me.villagerunknown.immortalvillagers.feature;

import me.villagerunknown.immortalvillagers.Immortalvillagers;
import me.villagerunknown.platform.util.EntityUtil;
import me.villagerunknown.platform.util.MathUtil;
import me.villagerunknown.platform.util.PositionUtil;
import me.villagerunknown.platform.util.WorldUtil;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ConversionParams;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.zombie.Zombie;
import net.minecraft.world.entity.monster.zombie.ZombieVillager;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class preventDamageToVillagersFeature {
	
	public static List<EntityType<?>> zombieConversionTypes = List.of(
			EntityType.HUSK,
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
				if (entity instanceof Villager) {
					// Cancel the damage if the entity is a villager
					return false;
				} // if
			} else {
				if (entity instanceof Villager) {
					// Send a message on damage
					if( Immortalvillagers.CONFIG.reportVillagerDamageToLogs ) {
						Entity damageSourceEntity = damageSource.getEntity();
						
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
			Entity sourceEntity = damageSource.getEntity();
			
			if( null != sourceEntity && zombieConversionTypes.contains( sourceEntity.getType() ) && MathUtil.hasChance( Immortalvillagers.CONFIG.zombieConversionChance ) ) {
				convertToZombie((Villager) entity, (Zombie) sourceEntity);
				
				return false;
			} // if
			
			if( Immortalvillagers.CONFIG.enableVillagerDamageButRespawn ) {
				
				if (entity instanceof Villager) {
					// Find the bed position
					BlockPos bedPos = PositionUtil.findNearestBed(entity, Immortalvillagers.CONFIG.maxSearchRadiusInBlocks);
					
					if( null != bedPos ) {
						EntityUtil.simulateDeath( entity );
						
						// Play villager death sound
						EntityUtil.playSound( entity, SoundEvents.VILLAGER_DEATH, SoundSource.NEUTRAL, 1.0F, 1.0F, false );
						
						// Teleport the original villager
						EntityUtil.teleport(entity, new Vec3(bedPos.getX() + 0.5, bedPos.getY() + 1, bedPos.getZ() + 0.5));
					} else {
						EntityUtil.simulateTotemDeath( entity );
						
						// Apply speed temporarily
						EntityUtil.addStatusEffect( entity, MobEffects.SPEED, 5, 0, true, false, false );
						EntityUtil.addStatusEffect( entity, MobEffects.ABSORPTION, 5, 0, true, false, false );
					} // if, else
					
					// Report the death
					if( Immortalvillagers.CONFIG.reportVillagerRespawnsToLogs ) {
						Entity damageSourceEntity = damageSource.getEntity();
						
						if( null != damageSourceEntity ) {
							EntityUtil.reportKillToLog( Immortalvillagers.LOGGER, entity, damageSourceEntity );
						} // if
					} // if
					
					EntityUtil.setHealthToMax( entity );
					
					return false;
				} // if
				
			} //if
			
			return true;
		});
	}
	
	public static Zombie convertToZombie( Villager villager, Zombie zombieEntity ) {
		ServerLevel level = WorldUtil.getServerWorld(villager.level());
		
		ZombieVillager zombieVillagerEntity = (ZombieVillager)villager.convertTo(EntityType.ZOMBIE_VILLAGER, ConversionParams.single(villager, true, true), (zombie) -> {
			zombie.finalizeSpawn(level, level.getCurrentDifficultyAt(zombie.blockPosition()), EntitySpawnReason.CONVERSION, new Zombie.ZombieGroupData(false, true));
			zombie.setVillagerData(villager.getVillagerData());
			zombie.setGossips(villager.getGossips().copy());
			zombie.setTradeOffers(villager.getOffers().copy());
			zombie.setVillagerXp(villager.getVillagerXp());
			if (!zombie.isSilent()) {
				level.levelEvent((Entity)null, 1026, zombie.blockPosition(), 0);
			}
			
		});
		
		if (zombieVillagerEntity != null) {
			if( Immortalvillagers.CONFIG.reportVillagerConversionsToLogs ) {
				EntityUtil.reportConversionToLog( Immortalvillagers.LOGGER, villager, zombieEntity );
			} // if
		} // if
		
		return zombieVillagerEntity;
	}
	
}
