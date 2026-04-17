package com.narxoz.rpg.strategy;

/**
 * Mid-phase boss strategy that increases attack pressure.
 */
public class BossAggressiveStrategy implements CombatStrategy {

    @Override
    public int calculateDamage(int basePower) {
        return Math.max(1, (basePower * 120 + 50) / 100);
    }

    @Override
    public int calculateDefense(int baseDefense) {
        return Math.max(1, (baseDefense * 90 + 50) / 100);
    }

    @Override
    public String getName() {
        return "Ruthless Assault";
    }
}
