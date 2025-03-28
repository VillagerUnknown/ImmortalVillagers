package me.villagerunknown.immortalvillagers.feature;

import me.villagerunknown.immortalvillagers.Immortalvillagers;
import me.villagerunknown.platform.util.EntityUtil;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.component.EnchantmentEffectComponentTypes;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.conversion.EntityConversionContext;
import net.minecraft.entity.conversion.EntityConversionType;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.WitchEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.village.VillagerProfession;
import net.minecraft.world.ServerWorldAccess;

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
			if( world.isClient() ) {
				return ActionResult.PASS;
			} // if
			
			if( entity.getType().equals( EntityType.WITCH ) ) {
				ItemStack itemStack = player.getStackInHand( hand );
				WitchEntity witch = (WitchEntity) entity;
				
				if( Immortalvillagers.CONFIG.enableWitchToVillagerConversion && witch.hasStatusEffect(StatusEffects.WEAKNESS ) && CONVERSION_ITEMS.contains( itemStack.getItem() ) ) {
					itemStack.decrementUnlessCreative( 1, player );
					
					witch.convertTo( EntityType.VILLAGER, EntityConversionContext.create(witch, false, false), (villager) -> {
						Iterator var3 = witch.dropEquipment((ServerWorld) world, (stack) -> {
							return !EnchantmentHelper.hasAnyEnchantmentsWith(stack, EnchantmentEffectComponentTypes.PREVENT_ARMOR_CHANGE);
						}).iterator();
						
						while(var3.hasNext()) {
							EquipmentSlot equipmentSlot = (EquipmentSlot)var3.next();
							StackReference stackReference = villager.getStackReference(equipmentSlot.getEntitySlotId() + 300);
							stackReference.set(witch.getEquippedStack(equipmentSlot));
						}
						
						villager.initialize((ServerWorldAccess) world, world.getLocalDifficulty(villager.getBlockPos()), SpawnReason.CONVERSION, (EntityData)null);
						villager.reinitializeBrain((ServerWorld) world);
						
						villager.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 200, 0));
						if (!witch.isSilent()) {
							world.syncWorldEvent((PlayerEntity)null, 1027, witch.getBlockPos(), 0);
						}
					} );
					
					world.playSoundFromEntity( witch, SoundEvents.ENTITY_WITCH_HURT, SoundCategory.NEUTRAL, 1, 1 );
					EntityUtil.spawnParticles( witch, 1.5F, ParticleTypes.HAPPY_VILLAGER, 10, 0.5, 0.5, 0.5, 0.5);
					
					EntityUtil.reportConversionToLog( Immortalvillagers.LOGGER, witch, player );
					
					return ActionResult.SUCCESS;
				} // if
			} // if
			
			return ActionResult.PASS;
		});
	}
	
}
