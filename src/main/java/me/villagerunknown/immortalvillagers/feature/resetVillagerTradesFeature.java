package me.villagerunknown.immortalvillagers.feature;

import me.villagerunknown.immortalvillagers.Immortalvillagers;
import me.villagerunknown.platform.util.EntityUtil;
import me.villagerunknown.platform.util.MerchantEntityUtil;
import me.villagerunknown.platform.util.VillagerUtil;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.entity.npc.villager.VillagerProfession;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class resetVillagerTradesFeature {
	
	public static String RESET_STRING = Immortalvillagers.CONFIG.villagerTradesResetItemName;
	
	public static List<Item> RESET_ITEMS = List.of(
			Items.EMERALD,
			Items.EMERALD_BLOCK
	);
	
	public static List<ResourceKey<VillagerProfession>> NO_RESET_PROFESSIONS = List.of(
			VillagerProfession.NONE,
			VillagerProfession.NITWIT
	);
	
	public static void execute() {
		registerTradeResetEvent();
	}
	
	private static void registerTradeResetEvent() {
		UseEntityCallback.EVENT.register(( player, world, hand, entity, hitResult ) -> {
			if( world.isClientSide() ) {
				return InteractionResult.PASS;
			} // if
			
			if( Immortalvillagers.CONFIG.enableVillagerTradesReset && entity.getType().equals( EntityType.VILLAGER ) && player.isCrouching() ) {
				ItemStack itemStack = player.getItemInHand( hand );
				Villager villager = (Villager) entity;
				
				Holder<VillagerProfession> profession = villager.getVillagerData().profession();
				
				Optional<ResourceKey<VillagerProfession>> professionKey = profession.unwrapKey();
				
				if( professionKey.isPresent() ) {
					@Nullable Component customName = itemStack.getCustomName();
					
					if( null != customName ) {
						
						if( !NO_RESET_PROFESSIONS.contains( professionKey.get() ) && RESET_ITEMS.contains( itemStack.getItem() ) && customName.getString().equalsIgnoreCase( RESET_STRING ) ) {
							itemStack.consume( 1, player );
							Immortalvillagers.LOGGER.info("reset");
							if( itemStack.getItem().equals( Items.EMERALD ) ) {
								VillagerUtil.resetTrades( villager, villager.getVillagerData().level() );
							} else if( itemStack.getItem().equals( Items.EMERALD_BLOCK ) ) {
								VillagerUtil.resetAllTrades( villager );
							} // if, else if
							
							EntityUtil.playSound( villager, SoundEvents.VILLAGER_TRADE, SoundSource.NEUTRAL, 1, 1, false );
							EntityUtil.spawnParticles( villager, 1.5F, ParticleTypes.HAPPY_VILLAGER, 10, 0.5, 0.5, 0.5, 0.5);
							
							return InteractionResult.SUCCESS;
						} // if
						
					} // if
					
				} // if
				
			} // if
			
			return InteractionResult.PASS;
		});
	}
	
}
