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
	 * Logging
	 */
	
	@ConfigEntry.Category("Logging")
	public boolean reportVillagerDamageToLogs = false;
	
	@ConfigEntry.Category("Logging")
	public boolean reportVillagerRespawnsToLogs = false;
	
	@ConfigEntry.Category("Logging")
	public boolean reportVillagerConversionsToLogs = false;
	
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
	public String villagerStupidificationItemName = "nitwits for dummies";
	
	@ConfigEntry.Category("Villagers")
	public boolean enableVillagerTradesReset = false;
	
	@ConfigEntry.Category("Villagers")
	public String villagerTradesResetItemName = "reset";
	
	/**
	 * Conversions
	 */
	
	@ConfigEntry.Category("Conversions")
	public float zombieConversionChance = 0F;
	
	@ConfigEntry.Category("Conversions")
	public float witchConversionChance = 0F;
	
	@ConfigEntry.Category("Conversions")
	public boolean enableWitchToVillagerConversion = false;
	
}
