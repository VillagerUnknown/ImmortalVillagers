# VillagerUnknown's Immortal Villagers

Prevents villagers from taking damage, dying, and turning into zombies or witches.

The general idea of this mod is to help you prevent the villager life-cycle from ending. 

It includes optional features that allow you to convert villagers in various ways. 
Turn a normal villager into a nitwit with a stupid stick, educate a nitwit by giving them a book, or convert witches to villagers like you would a zombie villager.

Since this mod prevents villagers from dying, you might not always be satisfied with their trades. 
That's why this mod also includes an optional feature to cycle all of a villager's trades with a configurable named Emerald or Emerald Block. 
An Emerald Block will reset all of a Villager's trades while an Emerald will only reset the last trades unlocked.

**Features**

* Villagers cannot die and can optionally "respawn" at the closest bed.
* Optionally reset Villager Trades by crouching and giving them an Emerald or Emerald Block with a configurable name.
* Optionally convert Nitwits to Villagers by interacting with them using a Book.
* Optionally convert Villagers to Nitwits by interacting with them using a Stick with a configurable name.
* Configurable Zombie and Witch conversion chances.
* Optionally convert Witches to Villagers by giving them a Golden Apple or Enchanted Golden Apple while they're under the effects of Weakness.

**Options:**

* maxSearchRadiusInBlocks - Radius in blocks a villager respawn will look for a bed. (Default: 64)

* reportVillagerDamageToLogs - Report Damage to Villagers to Logs. (Default: false)
* reportVillagerRespawnsToLogs - Report Respawns of Villagers to Logs. (Default: false)
* reportVillagerConversionsToLogs - Report Villager Conversions to Logs. (Default: false)

* enableVillagerDamageButRespawn - Allow damage to Villagers but Respawn Villagers at the Nearest Bed. (Default: false)
* enableNitwitEducation - Allow converting Nitwits into Villagers without a profession by interacting with them while holding a Book. (Default: false)
* enableVillagerStupidification - Allow converting Villagers into Nitwits by interacting with them while holding a Stick renamed to the set config value. (Default: false)
* villagerStupidificationItemName - The required name of the Stick to convert Villagers to Nitwits. Ignores case. (Default: stupid) 
* enableVillagerTradesReset - Allow Resetting Trades for Villagers by crouching and interacting with them while holding an Emerald or Emerald Block renamed to the set config value. (Default: false)
* villagerTradesResetItemName - The required name of the Emerald or Emerald Block to Reset Villager Trades. Ignores case. (Default: reset)

* zombieConversionChance - Chance for Zombie Conversion (Default: 0)
* witchConversionChance - Chance for Witch Conversion (Default: 0)
* enableWitchToVillagerConversion - Allow converting Witches into Villagers by giving them a Golden Apple or Enchanted Golden Apple while they're under the effects of Weakness. (Default: false)

## Support

_Note: If a Villager gets converted to a Witch and then converted back to a Villager, they will not have the same trades._

- Request features and report bugs at https://github.com/VillagerUnknown/ImmortalVillagers/issues
- View the changelog at https://github.com/VillagerUnknown/ImmortalVillagers/blob/main/CHANGELOG.md