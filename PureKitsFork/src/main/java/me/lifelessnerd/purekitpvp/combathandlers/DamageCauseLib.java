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

                damageCauseTranslations.put("CONTACT","a Cactus");
                damageCauseTranslations.put("SUFFOCATION"," suffocation");
                damageCauseTranslations.put("FALL"," fall damage");
                damageCauseTranslations.put("FIRE"," fire");
                damageCauseTranslations.put("FIRE_TICK"," fire");
                damageCauseTranslations.put("MELTING"," lava");
                damageCauseTranslations.put("LAVA"," lava");
                damageCauseTranslations.put("DROWNING"," drowning");
                damageCauseTranslations.put("BLOCK_EXPLOSION"," an explosion");
                damageCauseTranslations.put("ENTITY_EXPLOSION"," an explosion");
                damageCauseTranslations.put("VOID"," the void");
                damageCauseTranslations.put("LIGHTNING"," a lightning strike");
                damageCauseTranslations.put("SUICIDE"," yourself");
                damageCauseTranslations.put("STARVATION"," hunger");
                damageCauseTranslations.put("POISON"," poison");
                damageCauseTranslations.put("MAGIC"," Harming Potions");
                damageCauseTranslations.put("WITHER"," the wither effect");
                damageCauseTranslations.put("FALLING_BLOCK"," a falling block");
                damageCauseTranslations.put("THORNS"," thorns (how?)");
                damageCauseTranslations.put("DRAGON_BREATH"," Dragon's Breath");
                damageCauseTranslations.put("CUSTOM"," a mysterious force");
                damageCauseTranslations.put("FLY_INTO_WALL"," your inability to fly");
                damageCauseTranslations.put("HOT_FLOOR"," magma");
                damageCauseTranslations.put("CRAMMING"," crowding");
                damageCauseTranslations.put("DRYOUT"," a lack of water");
                damageCauseTranslations.put("FREEZE"," freezing");
                damageCauseTranslations.put("SONIC_BOOM"," a Warden");
        }
}

