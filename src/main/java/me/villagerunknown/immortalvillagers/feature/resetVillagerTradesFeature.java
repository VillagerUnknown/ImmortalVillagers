package me.villagerunknown.immortalvillagers.feature;

import me.villagerunknown.immortalvillagers.Immortalvillagers;
import me.villagerunknown.platform.util.EntityUtil;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.village.TradeOfferList;
import net.minecraft.village.VillagerData;
import net.minecraft.village.VillagerProfession;

import java.util.List;

public class resetVillagerTradesFeature {
	
	public static String RESET_STRING = Immortalvillagers.CONFIG.villagerTradesResetItemName;
	
	public static List<VillagerProfession> NO_RESET_PROFESSIONS = List.of(
			VillagerProfession.NONE,
			VillagerProfession.NITWIT
	);
	
	public static void execute() {
		registerTradeResetEvent();
	}
	
	private static void registerTradeResetEvent() {
		UseEntityCallback.EVENT.register(( player, world, hand, entity, hitResult ) -> {
			if( world.isClient() ) {
				return ActionResult.PASS;
			} // if
			
			if( Immortalvillagers.CONFIG.enableVillagerTradesReset && entity.getType().equals( EntityType.VILLAGER ) && player.isSneaking() ) {
				ItemStack itemStack = player.getStackInHand( hand );
				VillagerEntity villager = (VillagerEntity) entity;
				VillagerProfession profession = villager.getVillagerData().getProfession();
				
				if( !NO_RESET_PROFESSIONS.contains( profession ) && itemStack.getItem().equals( Items.PAPER ) && itemStack.getName().equals( Text.of( RESET_STRING ) ) ) {
					itemStack.decrementUnlessCreative( 1, player );
					
					villager.setOffers(null);
					
					int level = villager.getVillagerData().getLevel();
					TradeOfferList offers = new TradeOfferList();
					
					for (int i = VillagerData.MIN_LEVEL; i <= level; i++) {
						villager.setVillagerData( villager.getVillagerData().withLevel( i ) );
						
						offers.addAll( villager.getOffers() );
						villager.setOffers(null);
					} // for
					
					villager.setOffers( offers );
					
					villager.playWorkSound();
					EntityUtil.spawnParticles( villager, 1.5F, ParticleTypes.HAPPY_VILLAGER, 10, 0.05, 0.05, 0.05, 0.5);
					
					return ActionResult.SUCCESS;
				} // if
			} // if
			
			return ActionResult.PASS;
		});
	}
	
}
