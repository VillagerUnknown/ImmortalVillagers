package me.villagerunknown.immortalvillagers;

import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "villagerunknown-immortalvillagers")
public class ImmortalvillagersConfigData implements me.shedaniel.autoconfig.ConfigData {
	
	/**
	 * General
	 */
	
	@ConfigEntry.Category("General")
	public int maxSearchRadius = 64;
	
	/**
	 * Villagers
	 */
	
	@ConfigEntry.Category("Villagers")
	public boolean enableVillagerDamageButRespawn = false;
	
	@ConfigEntry.Category("Villagers")
	public boolean reportVillagerDamageToLogs = false;
	
	@ConfigEntry.Category("Villagers")
	public boolean reportVillagerRespawnsToLogs = false;
	
}
