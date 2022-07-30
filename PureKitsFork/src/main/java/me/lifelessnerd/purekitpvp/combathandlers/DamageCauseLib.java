package me.lifelessnerd.purekitpvp.combathandlers;

import java.util.HashMap;

public class DamageCauseLib {

        public HashMap<String, String> deathMessages = new HashMap<>();

        public HashMap<String, String> damageCauseTranslations = new HashMap<>();

        public DamageCauseLib() {
                deathMessages.put("CONTACT"," was pricked to death!");
                deathMessages.put("SUFFOCATION"," needed some oxygen");
                deathMessages.put("FALL"," feel from a high (or low) place");
                deathMessages.put("FIRE"," burnt to death");
                deathMessages.put("FIRE_TICK"," slowly burned to death");
                deathMessages.put("MELTING"," was cooked alive");
                deathMessages.put("LAVA"," took a dive in a lava-pool");
                deathMessages.put("DROWNING"," ran out of oxygen");
                deathMessages.put("BLOCK_EXPLOSION"," died to an explosion");
                deathMessages.put("ENTITY_EXPLOSION"," died to an explosion");
                deathMessages.put("VOID"," feel into the abyss");
                deathMessages.put("LIGHTNING"," got very unlucky");
                deathMessages.put("SUICIDE"," committed suicide");
                deathMessages.put("STARVATION"," died of hunger");
                deathMessages.put("POISON"," died of poison");
                deathMessages.put("MAGIC"," was cursed and died");
                deathMessages.put("WITHER"," withered away");
                deathMessages.put("FALLING_BLOCK"," got squished by a block");
                deathMessages.put("THORNS"," was killed by thorns (how?)");
                deathMessages.put("DRAGON_BREATH"," was killed by Dragon's Breath");
                deathMessages.put("CUSTOM"," was killed by a mysterious force");
                deathMessages.put("FLY_INTO_WALL"," experienced kinetic energy");
                deathMessages.put("HOT_FLOOR"," stood on magma for too long");
                deathMessages.put("CRAMMING"," was squished to death");
                deathMessages.put("DRYOUT"," had no more water");
                deathMessages.put("FREEZE"," froze to death");
                deathMessages.put("SONIC_BOOM"," got pummeled by a Warden");

                damageCauseTranslations.put("CONTACT","Cactus");
                damageCauseTranslations.put("SUFFOCATION","Suffocation");
                damageCauseTranslations.put("FALL"," Fall Damage");
                damageCauseTranslations.put("FIRE"," Fire");
                damageCauseTranslations.put("FIRE_TICK"," Fire");
                damageCauseTranslations.put("MELTING"," Lava");
                damageCauseTranslations.put("LAVA"," Lava");
                damageCauseTranslations.put("DROWNING"," Drowning");
                damageCauseTranslations.put("BLOCK_EXPLOSION"," Explosion");
                damageCauseTranslations.put("ENTITY_EXPLOSION"," Explosion");
                damageCauseTranslations.put("VOID"," Void");
                damageCauseTranslations.put("LIGHTNING"," Lightning");
                damageCauseTranslations.put("SUICIDE"," Yourself");
                damageCauseTranslations.put("STARVATION"," Hunger");
                damageCauseTranslations.put("POISON"," Poison");
                damageCauseTranslations.put("MAGIC"," Harming Effect");
                damageCauseTranslations.put("WITHER"," Wither Effect");
                damageCauseTranslations.put("FALLING_BLOCK"," Falling Blocks");
                damageCauseTranslations.put("THORNS"," Thorns");
                damageCauseTranslations.put("DRAGON_BREATH"," Dragon's Breath");
                damageCauseTranslations.put("CUSTOM"," ???");
                damageCauseTranslations.put("FLY_INTO_WALL"," Kinetic Energy");
                damageCauseTranslations.put("HOT_FLOOR"," Magma");
                damageCauseTranslations.put("CRAMMING"," Crowding");
                damageCauseTranslations.put("DRYOUT"," Dryout");
                damageCauseTranslations.put("FREEZE"," Freezing");
                damageCauseTranslations.put("SONIC_BOOM"," Warden");
        }
}

