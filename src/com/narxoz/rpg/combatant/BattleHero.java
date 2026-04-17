package com.narxoz.rpg.combatant;

import com.narxoz.rpg.strategy.CombatStrategy;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Hero extension that can fight with interchangeable strategies
 * and optionally switch them on predefined rounds.
 */
public class BattleHero extends Hero {

    private CombatStrategy strategy;
    private final Map<Integer, CombatStrategy> scheduledStrategyChanges = new HashMap<>();

    public BattleHero(String name, int hp, int attackPower, int defense, CombatStrategy strategy) {
        super(name, hp, attackPower, defense);
        this.strategy = Objects.requireNonNull(strategy, "strategy");
    }

    public CombatStrategy getStrategy() {
        return strategy;
    }

    public void switchStrategy(CombatStrategy newStrategy) {
        strategy = Objects.requireNonNull(newStrategy, "newStrategy");
    }

    public void scheduleStrategySwitch(int round, CombatStrategy newStrategy) {
        if (round < 1) {
            throw new IllegalArgumentException("Round must be positive.");
        }
        scheduledStrategyChanges.put(round, Objects.requireNonNull(newStrategy, "newStrategy"));
    }

    public CombatStrategy applyScheduledStrategy(int round) {
        CombatStrategy plannedStrategy = scheduledStrategyChanges.remove(round);
        if (plannedStrategy == null) {
            return null;
        }
        if (plannedStrategy.getName().equals(strategy.getName())) {
            return null;
        }
        strategy = plannedStrategy;
        return strategy;
    }

    public int getEffectiveAttackPower() {
        return strategy.calculateDamage(getAttackPower());
    }

    public int getEffectiveDefense() {
        return strategy.calculateDefense(getDefense());
    }
}
