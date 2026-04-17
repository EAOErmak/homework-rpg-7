package com.narxoz.rpg.strategy;

/**
 * Converts defense into heavier damage output.
 */
public class BerserkerStrategy implements CombatStrategy {

    @Override
    public int calculateDamage(int basePower) {
        return Math.max(1, (basePower * 140 + 50) / 100);
    }

    @Override
    public int calculateDefense(int baseDefense) {
        return Math.max(0, (baseDefense * 70 + 50) / 100);
    }

    @Override
    public String getName() {
        return "Berserker Stance";
    }
}
