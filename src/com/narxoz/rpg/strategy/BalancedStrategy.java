package com.narxoz.rpg.strategy;

/**
 * Baseline strategy that keeps the combatant's raw stats unchanged.
 */
public class BalancedStrategy implements CombatStrategy {

    @Override
    public int calculateDamage(int basePower) {
        return basePower;
    }

    @Override
    public int calculateDefense(int baseDefense) {
        return baseDefense;
    }

    @Override
    public String getName() {
        return "Balanced Stance";
    }
}
