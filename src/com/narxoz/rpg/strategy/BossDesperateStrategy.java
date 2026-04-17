package com.narxoz.rpg.strategy;

/**
 * Final boss phase that prioritizes damage above everything else.
 */
public class BossDesperateStrategy implements CombatStrategy {

    @Override
    public int calculateDamage(int basePower) {
        return Math.max(1, (basePower * 160 + 50) / 100);
    }

    @Override
    public int calculateDefense(int baseDefense) {
        return Math.max(0, (baseDefense * 40 + 50) / 100);
    }

    @Override
    public String getName() {
        return "Last Stand Frenzy";
    }
}
