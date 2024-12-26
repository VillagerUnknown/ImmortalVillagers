package me.villagerunknown.immortalvillagers.client;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.autoconfig.AutoConfig;
import me.villagerunknown.immortalvillagers.ImmortalvillagersConfigData;

public class ModMenuIntegration implements ModMenuApi {
	
	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory() {
		return parent -> AutoConfig.getConfigScreen(ImmortalvillagersConfigData.class, parent).get();
	}
	
}
