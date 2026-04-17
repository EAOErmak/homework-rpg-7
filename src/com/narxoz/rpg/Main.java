package com.narxoz.rpg;

import com.narxoz.rpg.combatant.BattleHero;
import com.narxoz.rpg.combatant.DungeonBoss;
import com.narxoz.rpg.engine.DungeonEngine;
import com.narxoz.rpg.engine.EncounterResult;
import com.narxoz.rpg.observer.AchievementTracker;
import com.narxoz.rpg.observer.BattleLogger;
import com.narxoz.rpg.observer.GameEventBus;
import com.narxoz.rpg.observer.HeroStatusMonitor;
import com.narxoz.rpg.observer.LootDropper;
import com.narxoz.rpg.observer.PartySupport;
import com.narxoz.rpg.strategy.BalancedStrategy;
import com.narxoz.rpg.strategy.BerserkerStrategy;
import com.narxoz.rpg.strategy.BossMeasuredStrategy;
import com.narxoz.rpg.strategy.GuardianStrategy;

import java.util.List;

/**
 * Entry point for Homework 7 - The Cursed Dungeon: Boss Encounter System.
 */
public class Main {

    public static void main(String[] args) {
        GameEventBus eventBus = new GameEventBus();

        BattleHero aria = new BattleHero("Aria", 120, 30, 12, new BerserkerStrategy());
        BattleHero borin = new BattleHero("Borin", 160, 24, 18, new GuardianStrategy());
        BattleHero cyra = new BattleHero("Cyra", 110, 26, 10, new BalancedStrategy());
        cyra.scheduleStrategySwitch(3, new BerserkerStrategy());

        List<BattleHero> heroes = List.of(aria, borin, cyra);

        DungeonBoss boss = new DungeonBoss(
                "The Cursed Warden",
                260,
                34,
                8,
                new BossMeasuredStrategy(),
                eventBus
        );

        eventBus.registerObserver(new BattleLogger());
        eventBus.registerObserver(boss);
        eventBus.registerObserver(new AchievementTracker());
        eventBus.registerObserver(new PartySupport(heroes, 18, 7L));
        eventBus.registerObserver(new HeroStatusMonitor(heroes));
        eventBus.registerObserver(new LootDropper(21L));

        printEncounterSetup(heroes, boss);

        DungeonEngine engine = new DungeonEngine(heroes, boss, eventBus, 10);
        EncounterResult result = engine.runEncounter();

        System.out.println();
        System.out.println("=== Encounter Result ===");
        System.out.printf("Heroes won: %s%n", result.isHeroesWon() ? "yes" : "no");
        System.out.printf("Rounds played: %d%n", result.getRoundsPlayed());
        System.out.printf("Surviving heroes: %d%n", result.getSurvivingHeroes());
    }

    private static void printEncounterSetup(List<BattleHero> heroes, DungeonBoss boss) {
        System.out.println("=== The Cursed Dungeon Boss Encounter ===");
        for (BattleHero hero : heroes) {
            System.out.printf(
                    "Hero %-5s HP:%3d ATK:%2d DEF:%2d Strategy:%s%n",
                    hero.getName(),
                    hero.getMaxHp(),
                    hero.getAttackPower(),
                    hero.getDefense(),
                    hero.getStrategy().getName()
            );
        }
        System.out.printf(
                "Boss %-18s HP:%3d ATK:%2d DEF:%2d Strategy:%s%n",
                boss.getName(),
                boss.getMaxHp(),
                boss.getAttackPower(),
                boss.getDefense(),
                boss.getStrategy().getName()
        );
        System.out.println("Planned tactic change: Cyra switches to Berserker Stance in round 3.");
    }
}
