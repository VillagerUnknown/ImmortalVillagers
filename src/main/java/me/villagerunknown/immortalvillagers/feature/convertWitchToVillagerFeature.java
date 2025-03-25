package me.villagerunknown.immortalvillagers.feature;

import me.villagerunknown.immortalvillagers.Immortalvillagers;
import me.villagerunknown.platform.util.EntityUtil;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.WitchEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.village.VillagerProfession;

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
					
					witch.convertTo( EntityType.VILLAGER, true );
					
					world.playSoundFromEntity( witch, SoundEvents.ENTITY_WITCH_HURT, SoundCategory.NEUTRAL, 1, 1 );
					EntityUtil.spawnParticles( witch, 1.5F, ParticleTypes.HAPPY_VILLAGER, 10, 0.05, 0.05, 0.05, 0.5);
					
					return ActionResult.SUCCESS;
				} // if
			} // if
			
			return ActionResult.PASS;
		});
	}
	
}
