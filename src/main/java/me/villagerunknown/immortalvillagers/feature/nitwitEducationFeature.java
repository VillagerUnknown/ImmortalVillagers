package me.villagerunknown.immortalvillagers.feature;

import me.villagerunknown.immortalvillagers.Immortalvillagers;
import me.villagerunknown.platform.util.EntityUtil;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.entity.npc.villager.VillagerProfession;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

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
			if( world.isClientSide() ) {
				return InteractionResult.PASS;
			} // if
			
			if( entity.getType().equals( EntityTypes.VILLAGER ) ) {
				ItemStack itemStack = player.getItemInHand( hand );
				Villager villager = (Villager) entity;
				VillagerProfession profession = villager.getVillagerData().profession().value();
				
				if( Immortalvillagers.CONFIG.enableNitwitEducation && profession.name().getString().equalsIgnoreCase("nitwit") && EDUCATION_ITEMS.contains( itemStack.getItem() ) ) {
					return convertVillager( player, hand, villager, VillagerProfession.NONE, SoundEvents.VILLAGER_CELEBRATE, ParticleTypes.HAPPY_VILLAGER );
				} else if( Immortalvillagers.CONFIG.enableVillagerStupidification && profession.name().getString().equalsIgnoreCase("villager") && STUPIDIFICATION_ITEMS.contains( itemStack.getItem() ) && itemStack.getItemName().getString().equalsIgnoreCase( ITEM_STRING ) ) {
					return convertVillager( player, hand, villager, VillagerProfession.NITWIT, SoundEvents.VILLAGER_HURT, ParticleTypes.ANGRY_VILLAGER );
				} // if, else if
			} // if
			
			return InteractionResult.PASS;
		});
	}
	
	private static InteractionResult convertVillager(Player player, InteractionHand hand, Villager villager, ResourceKey<VillagerProfession> profession, SoundEvent sound, SimpleParticleType particle ) {
		Level world = player.level();
		ItemStack itemStack = player.getItemInHand( hand );
		
		itemStack.consume( 1, player );
		
		Holder<VillagerProfession> professionEntry = villager.getVillagerData().profession();
		
		villager.setVillagerData( villager.getVillagerData().withProfession( professionEntry ) );
		
		EntityUtil.playSound( villager, sound, SoundSource.NEUTRAL, 1, 1, false );
		EntityUtil.spawnParticles( villager, 1.5F, particle, 10, 0.5, 0.5, 0.5, 0.5);
		
		EntityUtil.reportConversionToLog( Immortalvillagers.LOGGER, villager, player );
		
		return InteractionResult.SUCCESS;
	}
	
}
