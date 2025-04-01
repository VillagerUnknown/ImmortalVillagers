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
	
	public static PlatformMod<ImmortalvillagersConfigData> MOD = null;
	public static String MOD_ID = null;
	public static Logger LOGGER = null;
	public static ImmortalvillagersConfigData CONFIG = null;
	
	@Override
	public void onInitialize() {
		// # Register Mod w/ Platform
		MOD = Platform.register( "immortalvillagers", Immortalvillagers.class, ImmortalvillagersConfigData.class );
		
		MOD_ID = MOD.getModId();
		LOGGER = MOD.getLogger();
		CONFIG = MOD.getConfig();
		
		// # Initialize Mod
		init();
	}
	
	private static void init() {
		Platform.init_mod( MOD );
		
		// # Activate Features
		featureManager.addFeature( "preventDamageToVillagers", preventDamageToVillagersFeature::execute );
		featureManager.addFeature( "convertWitchToVillager", convertWitchToVillagerFeature::execute );
		featureManager.addFeature( "nitwitEducation", nitwitEducationFeature::execute );
		featureManager.addFeature( "resetVillagerTrades", resetVillagerTradesFeature::execute );
		
		// # Load Features
		featureManager.loadFeatures();
	}
}
