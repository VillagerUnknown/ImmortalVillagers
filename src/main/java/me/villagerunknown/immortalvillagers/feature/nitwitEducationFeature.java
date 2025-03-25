package me.villagerunknown.immortalvillagers.feature;

import me.villagerunknown.immortalvillagers.Immortalvillagers;
import me.villagerunknown.platform.util.EntityUtil;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.village.VillagerProfession;
import net.minecraft.world.World;

import java.util.List;

public class nitwitEducationFeature {
	
	public static String ITEM_STRING = Immortalvillagers.CONFIG.villagerStupidificationItemName;
	
	public static List<Item> EDUCATION_ITEMS = List.of(
			Items.BOOK
	);
	
	public static List<Item> STUPIDIFICATION_ITEMS = List.of(
			Items.STICK
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
					return convertVillager( player, hand, villager, VillagerProfession.NONE, SoundEvents.ENTITY_VILLAGER_CELEBRATE, ParticleTypes.HAPPY_VILLAGER );
				} else if( Immortalvillagers.CONFIG.enableVillagerStupidification && profession.equals( VillagerProfession.NONE ) && STUPIDIFICATION_ITEMS.contains( itemStack.getItem() ) && itemStack.getName().getString().equalsIgnoreCase( ITEM_STRING ) ) {
					return convertVillager( player, hand, villager, VillagerProfession.NITWIT, SoundEvents.ENTITY_VILLAGER_HURT, ParticleTypes.ANGRY_VILLAGER );
				} // if, else if
			} // if
			
			return ActionResult.PASS;
		});
	}
	
	private static ActionResult convertVillager( PlayerEntity player, Hand hand, VillagerEntity villager, VillagerProfession profession, SoundEvent sound, ParticleEffect particle ) {
		World world = player.getWorld();
		ItemStack itemStack = player.getStackInHand( hand );
		
		itemStack.decrementUnlessCreative( 1, player );
		
		villager.setVillagerData( villager.getVillagerData().withProfession( profession ) );
		
		world.playSoundFromEntity( villager, sound, SoundCategory.NEUTRAL, 1, 1 );
		EntityUtil.spawnParticles( villager, 1.5F, particle, 10, 0.5, 0.5, 0.5, 0.5);
		
		EntityUtil.reportConversionToLog( Immortalvillagers.LOGGER, villager, player );
		
		return ActionResult.SUCCESS;
	}
	
}
