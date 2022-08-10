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
I would recommend not editing the kits.yml unless you know how to check if your YAML is valid, because the file may reset when wrong YAML is parsed.
For the loottables.yml, follow the format that is provided in the example in the file. Write lore bare, and the displayname with single apostrophes (like so: '&6Lootchest'). 

## How to create a kit
*Also see the video that covers this topic below.*

Have all the items you want to be in your kit in your inventory, and type the command. A kit will be created with the arguments and automatically added to the kit menu (/kit). You either provide a kill item in the base command, or, if you want the kill item to be a special item (i.e. a golden head or a random loot chest), use the /purekitpvp getcustomitem command to get that item, and run the command /purekitpvp setkillitem \<kitName> while holding that item.
The contents of the kit will be shown in a clean hoverbox when choosing the kit, which will categorize the first 3 items in your hotbar as weapons, the rest of the inventory as items, the armor as armor and the offhand as offhand. If the kit has no armor, it will tell you aswell. A piece of lore can be set in the kits.yml. 
Basic potions that can be found in the inventory are translated as well, although they do not show their potency and lenght, due to how these items work in spigot. Custom potions created with commands however do work perfectly and show potency and lenght, so I recommend to create your own potions with commands when working with them.

## How to create a loot table
*Also see the video that covers this topic below.*

Place a chest and put all items you want to be in your loottable in the chest. The chance of each item will be 0.1 by default. You can change this in the loottables.yml file, but if you want you can just put 2 identical items in the chest if you want the chance to be 0.2. When finished, run the command /purekitpvp createloottable \<name> \<lore> \<displayName>. The lore and displayname are important for the item this loot table with be attached to. Now you may change the chance values of each item in the loottables.yml file. You may also change the lore and displayname to get multi-line text. Check if your YAML is valid before reloading the plugin (or back it up), otherwise the file may reset (!). To get the item with the loot table, run the command /purekitpvp getcustomitem random_chest \<loottable>. With this item in your inventory you can use it in a kit, or as kill item.
## Video
https://youtu.be/wZcZ4DcRfgU

## Known issues
I am sure there will be issues, but finding all of them takes a lot of testing time. Please submit any problems!

If there are any problems, create a message below, add them on GitHub, [here](https://github.com/LifelessNerd/PureKitPvP), or DM me on [Twitter](https://twitter.com/nerdlifeless).

