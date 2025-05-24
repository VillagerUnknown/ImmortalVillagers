package me.villagerunknown.immortalvillagers.feature;

import me.villagerunknown.immortalvillagers.Immortalvillagers;
import me.villagerunknown.platform.util.EntityUtil;
import me.villagerunknown.platform.util.MerchantEntityUtil;
import me.villagerunknown.platform.util.VillagerUtil;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.village.TradeOfferList;
import net.minecraft.village.VillagerData;
import net.minecraft.village.VillagerProfession;

import java.util.List;

public class resetVillagerTradesFeature {
	
	public static String RESET_STRING = Immortalvillagers.CONFIG.villagerTradesResetItemName;
	
	public static List<Item> RESET_ITEMS = List.of(
			Items.EMERALD,
			Items.EMERALD_BLOCK
	);
	
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
				
				if( !NO_RESET_PROFESSIONS.contains( profession ) && RESET_ITEMS.contains( itemStack.getItem() ) && itemStack.getName().getString().equalsIgnoreCase( RESET_STRING ) ) {
					itemStack.decrementUnlessCreative( 1, player );
					
					if( itemStack.getItem().equals( Items.EMERALD ) ) {
						VillagerUtil.resetTrades( villager, villager.getVillagerData().getLevel() );
					} else if( itemStack.getItem().equals( Items.EMERALD_BLOCK ) ) {
						VillagerUtil.resetAllTrades( villager );
					} // if
					
					world.playSoundFromEntity( villager, SoundEvents.ENTITY_VILLAGER_TRADE, SoundCategory.NEUTRAL, 1, 1 );
					EntityUtil.spawnParticles( villager, 1.5F, ParticleTypes.HAPPY_VILLAGER, 10, 0.5, 0.5, 0.5, 0.5);
					
					return ActionResult.SUCCESS;
				} // if
			} // if
			
			return ActionResult.PASS;
		});
	}
	
}
