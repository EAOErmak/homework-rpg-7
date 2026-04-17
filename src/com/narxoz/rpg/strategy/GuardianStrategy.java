package com.narxoz.rpg.strategy;

/**
 * Sacrifices offense in exchange for stronger defense.
 */
public class GuardianStrategy implements CombatStrategy {

    @Override
    public int calculateDamage(int basePower) {
        return Math.max(1, (basePower * 80 + 50) / 100);
    }

    @Override
    public int calculateDefense(int baseDefense) {
        return Math.max(1, (baseDefense * 150 + 50) / 100);
    }

    @Override
    public String getName() {
        return "Guardian Stance";
    }
}
