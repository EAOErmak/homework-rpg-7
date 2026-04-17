package com.narxoz.rpg.engine;

import com.narxoz.rpg.combatant.BattleHero;
import com.narxoz.rpg.combatant.DungeonBoss;
import com.narxoz.rpg.observer.GameEvent;
import com.narxoz.rpg.observer.GameEventBus;
import com.narxoz.rpg.observer.GameEventType;
import com.narxoz.rpg.strategy.CombatStrategy;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Round-based encounter runner for the dungeon boss fight.
 */
public class DungeonEngine {

    private final List<BattleHero> heroes;
    private final DungeonBoss boss;
    private final GameEventBus eventBus;
    private final int maxRounds;

    public DungeonEngine(List<BattleHero> heroes, DungeonBoss boss, GameEventBus eventBus, int maxRounds) {
        this.heroes = List.copyOf(heroes);
        this.boss = Objects.requireNonNull(boss, "boss");
        this.eventBus = Objects.requireNonNull(eventBus, "eventBus");
        this.maxRounds = maxRounds;
    }

    public EncounterResult runEncounter() {
        Set<String> lowHpAlerts = new HashSet<>();
        Set<String> fallenHeroes = new HashSet<>();
        int roundsPlayed = 0;

        while (roundsPlayed < maxRounds && boss.isAlive() && hasLivingHeroes()) {
            roundsPlayed++;
            System.out.println();
            System.out.printf("=== Round %d ===%n", roundsPlayed);

            applyPlannedHeroStrategyChanges(roundsPlayed);
            runHeroTurn();

            if (!boss.isAlive()) {
                break;
            }

            runBossTurn(lowHpAlerts, fallenHeroes);
        }

        if (roundsPlayed == maxRounds && boss.isAlive() && hasLivingHeroes()) {
            System.out.println("[ENGINE] The encounter hit the round limit. The heroes retreat.");
        }

        return new EncounterResult(!boss.isAlive(), roundsPlayed, countLivingHeroes());
    }

    private void applyPlannedHeroStrategyChanges(int round) {
        for (BattleHero hero : heroes) {
            if (!hero.isAlive()) {
                continue;
            }

            CombatStrategy newStrategy = hero.applyScheduledStrategy(round);
            if (newStrategy != null) {
                System.out.printf("[TACTICS] %s switches to %s.%n", hero.getName(), newStrategy.getName());
            }
        }
    }

    private void runHeroTurn() {
        for (BattleHero hero : heroes) {
            if (!hero.isAlive() || !boss.isAlive()) {
                continue;
            }

            int damage = Math.max(1, hero.getEffectiveAttackPower() - boss.getEffectiveDefense());
            eventBus.publish(new GameEvent(GameEventType.ATTACK_LANDED, hero.getName(), damage));
            boss.takeDamage(damage);
        }
    }

    private void runBossTurn(Set<String> lowHpAlerts, Set<String> fallenHeroes) {
        for (BattleHero hero : heroes) {
            if (!hero.isAlive()) {
                continue;
            }

            int damage = Math.max(1, boss.getEffectiveAttackPower() - hero.getEffectiveDefense());
            eventBus.publish(new GameEvent(GameEventType.ATTACK_LANDED, boss.getName(), damage));
            hero.takeDamage(damage);
            evaluateHeroState(hero, lowHpAlerts, fallenHeroes);
        }
    }

    private void evaluateHeroState(BattleHero hero, Set<String> lowHpAlerts, Set<String> fallenHeroes) {
        if (!hero.isAlive()) {
            lowHpAlerts.remove(hero.getName());
            if (fallenHeroes.add(hero.getName())) {
                eventBus.publish(new GameEvent(GameEventType.HERO_DIED, hero.getName(), 0));
            }
            return;
        }

        if (hero.getHp() * 100 < hero.getMaxHp() * 30) {
            if (!lowHpAlerts.contains(hero.getName())) {
                eventBus.publish(new GameEvent(GameEventType.HERO_LOW_HP, hero.getName(), hero.getHp()));
            }

            if (hero.getHp() * 100 < hero.getMaxHp() * 30) {
                lowHpAlerts.add(hero.getName());
            } else {
                lowHpAlerts.remove(hero.getName());
            }
            return;
        }

        lowHpAlerts.remove(hero.getName());
    }

    private boolean hasLivingHeroes() {
        return countLivingHeroes() > 0;
    }

    private int countLivingHeroes() {
        int count = 0;
        for (BattleHero hero : heroes) {
            if (hero.isAlive()) {
                count++;
            }
        }
        return count;
    }
}
