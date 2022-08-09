![\[Banner\]](https://i.imgur.com/tl2TQuw.png)
# PureKitPvP

PureKitPvP lets you create a kit pvp environment quickly, with your own custom kits. These kits work similar to the plugin PureKits, but since that plugin is abandoned, I decided to extend it with other features. A quick overview of all features:
- Death Recaps
- Kill Credit on void/environmental damage
- Assist credit
- Tracks Killstreaks, Kills, Deaths and a lot more stats
- Level system based on kills, deaths and assists
- Kits can be made in-game
- Kits can have a kill item, an item that will be aquired on kill
- Pick only one kit per minecraft life (reset after death)
- Per kit permissions
- Clean Kit GUI
- Golden Heads (can be set as kit items or/and as kill items)
- Random Loot Chests that can be set as kit item or as kill item
- Create these random loot chests ingame, change percentages in config
- See Kit usage stats for balancing
- Mobs can be used in kits, they despawn when the player has died
- Quickly die when falling in the void

## Commands
All commands can be seen in-game via _/purekitpvp help_.

### Admin commands
* **/purekitpvp createkit \<kitName> \<displayColor> \<kitIcon> \<kitPermission> \<killItem>** 
	* Create a kit with the contents of your inventory
		* \<kitIcon> / \<killItem> - _All material names within the autocomplete menu_
			* Use _air_ if you don't want a kill item  
* **/purekitpvp deletekit\<kitName>** 
	*  Remove a kit 

* **/purekitpvp resetkit** 
	* Reset your kit (dying will also reset your kit)

* **/purekitpvp setkillitem \<kitName>** 
	* Set the killitem to the item in your hand

* **/purekitpvp createloottable \<name> \<lore> \<displayName>** 
	* Create a loottable of the chest you are looking at
		* \<lore> - _The lore on the item chest that will be right clicked for the random items_
		* \<displayName> -_The displayname on the item chest that will be right clicked for the random items_
		* I suggest editing the above values in the loottables config, because multi-line is possible there
			* Make sure to follow the format in the loottables config, as YAML can be quite finicky 

* **/purekitpvp getcustomitem \<golden_head/random_chest>** 
	* Get the custom item to use in a kit
		* \<random_chest> requires a loottable name

* **/purekitpvp getkitstats** 
	* Outputs kit usage stats to see what kit is most popular (for balancing)

* **/purekitpvp help/info/reload** 
	* Self explanatory

### Player commands
* **/kit** 
	* Show Kit GUI
![\[IMG\]](https://i.imgur.com/JLFvel3.png)

* **/getkit <kitName>** 
	* Get a kit directly

* **/stats <player>** 
	* Show stats of any player
![enter image description here](https://i.imgur.com/ATOzlVD.png)

* **/suicide** 
	* Commit suicide (if enabled)

## Files
There are several files, most are used internally to track stats and such. The config has some feature toggles that are explained there. 

## Known issues
Currently no known issues.

If there are any problems, create a message below, add them on GitHub, [here](https://github.com/LifelessNerd/PureKitPvP), or DM me on [Twitter](https://twitter.com/nerdlifeless).

