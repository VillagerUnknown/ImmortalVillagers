package me.villagerunknown.immortalvillagers.feature;

import me.villagerunknown.immortalvillagers.Immortalvillagers;
import me.villagerunknown.platform.util.EntityUtil;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.minecraft.entity.EntityType;
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

public class nitwitEducationFeature {
	
	public static List<Item> EDUCATION_ITEMS = List.of(
			Items.BOOK
	);
	
	public static List<Item> STUPIDIFICATION_ITEMS = List.of(
			Items.SHORT_GRASS,
			Items.TALL_GRASS
	);
	
	public static void execute() {
		registerEducationEvent();
	}
	
	private static void registerEducationEvent() {
		UseEntityCallback.EVENT.register(( player, world, hand, entity, hitResult ) -> {
			if( world.isClient() ) {
				return ActionResult.PASS;
			} // if
			
			if( entity.getType().equals( EntityType.VILLAGER ) ) {
				ItemStack itemStack = player.getStackInHand( hand );
				VillagerEntity villager = (VillagerEntity) entity;
				VillagerProfession profession = villager.getVillagerData().getProfession();
				if( Immortalvillagers.CONFIG.enableNitwitEducation && profession.equals( VillagerProfession.NITWIT ) && EDUCATION_ITEMS.contains( itemStack.getItem() ) ) {
					itemStack.decrementUnlessCreative( 1, player );
					villager.setVillagerData( villager.getVillagerData().withProfession( VillagerProfession.NONE ) );
					world.playSoundFromEntity( villager, SoundEvents.ENTITY_VILLAGER_CELEBRATE, SoundCategory.NEUTRAL, 1, 1 );
					EntityUtil.spawnParticles( villager, 1.5F, ParticleTypes.HAPPY_VILLAGER, 10, 0.05, 0.05, 0.05, 0.5);
					
					return ActionResult.SUCCESS;
				} else if( Immortalvillagers.CONFIG.enableVillagerStupidification && profession.equals( VillagerProfession.NONE ) && STUPIDIFICATION_ITEMS.contains( itemStack.getItem() ) ) {
					itemStack.decrementUnlessCreative( 1, player );
					villager.setVillagerData( villager.getVillagerData().withProfession( VillagerProfession.NITWIT ) );
					world.playSoundFromEntity( villager, SoundEvents.ENTITY_VILLAGER_HURT, SoundCategory.NEUTRAL, 1, 1 );
					EntityUtil.spawnParticles( villager, 1.5F, ParticleTypes.HAPPY_VILLAGER, 10, 0.05, 0.05, 0.05, 0.5);
					
					return ActionResult.SUCCESS;
				} // if, else if
			} // if
			
			return ActionResult.PASS;
		});
	}
	
}
