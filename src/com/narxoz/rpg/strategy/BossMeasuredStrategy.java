package com.narxoz.rpg.strategy;

/**
 * Conservative opening phase for the boss.
 */
public class BossMeasuredStrategy implements CombatStrategy {

    @Override
    public int calculateDamage(int basePower) {
        return Math.max(1, (basePower * 95 + 50) / 100);
    }

    @Override
    public int calculateDefense(int baseDefense) {
        return Math.max(1, (baseDefense * 125 + 50) / 100);
    }

    @Override
    public String getName() {
        return "Measured Dominion";
    }
}
