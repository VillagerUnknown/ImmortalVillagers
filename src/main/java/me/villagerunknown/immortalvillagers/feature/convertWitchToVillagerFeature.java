package me.villagerunknown.immortalvillagers.feature;

import me.villagerunknown.immortalvillagers.Immortalvillagers;
import me.villagerunknown.platform.util.EntityUtil;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.village.ReputationEventType;
import net.minecraft.world.entity.monster.Witch;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

import java.util.Iterator;
import java.util.List;

public class convertWitchToVillagerFeature {
	
	public static List<Item> CONVERSION_ITEMS = List.of(
			Items.GOLDEN_APPLE,
			Items.ENCHANTED_GOLDEN_APPLE
	);
	
	public static void execute() {
		registerConversionEvent();
	}
	
	private static void registerConversionEvent() {
		UseEntityCallback.EVENT.register(( player, world, hand, entity, hitResult ) -> {
			if( world.isClientSide() ) {
				return InteractionResult.PASS;
			} // if
			
			if( entity.getType().equals( EntityType.WITCH ) ) {
				ItemStack itemStack = player.getItemInHand( hand );
				Witch witch = (Witch) entity;
				
				if( Immortalvillagers.CONFIG.enableWitchToVillagerConversion && witch.hasEffect( MobEffects.WEAKNESS ) && CONVERSION_ITEMS.contains( itemStack.getItem() ) ) {
					itemStack.consume( 1, player );
					
					witch.convertTo( EntityType.VILLAGER, ConversionParams.single(witch, true, true), (villager) -> {
						ServerLevel serverLevel = world.getServer().getLevel( world.dimension() );
						
						for(EquipmentSlot undroppedSlot : witch.dropPreservedEquipment(serverLevel, (stack) -> !EnchantmentHelper.has(stack, EnchantmentEffectComponents.PREVENT_ARMOR_CHANGE))) {
							SlotAccess offsetSlot = villager.getSlot(undroppedSlot.getIndex() + 300);
							if (offsetSlot != null) {
								offsetSlot.set(witch.getItemBySlot(undroppedSlot));
							}
						}
						
						villager.setVillagerXp(0);
						villager.finalizeSpawn(serverLevel, serverLevel.getCurrentDifficultyAt( villager.getOnPos() ), EntitySpawnReason.CONVERSION, (SpawnGroupData)null);
						villager.refreshBrain( serverLevel );
						
						villager.addEffect(new MobEffectInstance(MobEffects.NAUSEA, 200, 0));
						if (!witch.isSilent()) {
							world.levelEvent((Entity)null, 1027, witch.blockPosition(), 0);
						}
					} );
					
					EntityUtil.playSound( witch, SoundEvents.WITCH_HURT, SoundSource.NEUTRAL, 1, 1, false );
					EntityUtil.spawnParticles( witch, 1.5F, ParticleTypes.HAPPY_VILLAGER, 10, 0.5, 0.5, 0.5, 0.5);
					
					EntityUtil.reportConversionToLog( Immortalvillagers.LOGGER, witch, player );
					
					return InteractionResult.SUCCESS;
				} // if
			} // if
			
			return InteractionResult.PASS;
		});
	}
	
}
