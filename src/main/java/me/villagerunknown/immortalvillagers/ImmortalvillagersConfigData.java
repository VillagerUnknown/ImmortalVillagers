package me.villagerunknown.immortalvillagers;

import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "villagerunknown-immortalvillagers")
public class ImmortalvillagersConfigData implements me.shedaniel.autoconfig.ConfigData {
	
	/**
	 * General
	 */
	
	@ConfigEntry.Category("General")
	public int maxSearchRadiusInBlocks = 64;
	
	/**
	 * Villagers
	 */
	
	@ConfigEntry.Category("Villagers")
	public boolean enableVillagerDamageButRespawn = false;
	
	@ConfigEntry.Category("Villagers")
	public boolean enableNitwitEducation = false;
	
	@ConfigEntry.Category("Villagers")
	public boolean enableVillagerStupidification = false;
	
	@ConfigEntry.Category("Villagers")
	public float zombieConversionChance = 0F;
	
	@ConfigEntry.Category("Villagers")
	public float witchConversionChance = 0F;
	
	/**
	 * Logging
	 */
	
	@ConfigEntry.Category("Logging")
	public boolean reportVillagerDamageToLogs = false;
	
	@ConfigEntry.Category("Logging")
	public boolean reportVillagerRespawnsToLogs = false;
	
	@ConfigEntry.Category("Logging")
	public boolean reportVillagerConversionsToLogs = false;
	
}
