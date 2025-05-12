package me.villagerunknown.immortalvillagers;

import me.villagerunknown.immortalvillagers.feature.convertWitchToVillagerFeature;
import me.villagerunknown.immortalvillagers.feature.nitwitEducationFeature;
import me.villagerunknown.immortalvillagers.feature.resetVillagerTradesFeature;
import me.villagerunknown.platform.Platform;
import me.villagerunknown.immortalvillagers.feature.preventDamageToVillagersFeature;
import me.villagerunknown.platform.PlatformMod;
import me.villagerunknown.platform.manager.featureManager;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;

public class Immortalvillagers implements ModInitializer {
	
	public static PlatformMod<ImmortalvillagersConfigData> MOD = Platform.register( "immortalvillagers", Immortalvillagers.class, ImmortalvillagersConfigData.class );
	public static String MOD_ID = MOD.getModId();
	public static Logger LOGGER = MOD.getLogger();
	public static ImmortalvillagersConfigData CONFIG = MOD.getConfig();
	
	@Override
	public void onInitialize() {
		// # Register mod with Platform
		Platform.init_mod( MOD );
		
		// # Activate Features
		featureManager.addFeature( "prevent-damage-to-villagers", preventDamageToVillagersFeature::execute );
		featureManager.addFeature( "convert-witch-to-villager", convertWitchToVillagerFeature::execute );
		featureManager.addFeature( "nitwit-education", nitwitEducationFeature::execute );
		featureManager.addFeature( "reset-villager-trades", resetVillagerTradesFeature::execute );
		
		// # Load Features
		featureManager.loadFeatures();
	}
	
}
