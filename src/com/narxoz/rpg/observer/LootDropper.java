package com.narxoz.rpg.observer;

import java.util.Random;

/**
 * Generates loot when the boss changes phases or dies.
 */
public class LootDropper implements GameObserver {

    private static final String[] PHASE_TWO_LOOT = {
            "Jagged Iron Sigil",
            "Rune of the Second Gate",
            "Cracked War Banner"
    };

    private static final String[] PHASE_THREE_LOOT = {
            "Emberfang Talisman",
            "Bloodmarked Gauntlet",
            "Ashen Core Fragment"
    };

    private static final String[] FINAL_LOOT = {
            "Crown of the Cursed Warden",
            "Vault Key of the Deep Cells",
            "Soulsteel Great Rune"
    };

    private final Random random;

    public LootDropper(long seed) {
        this.random = new Random(seed);
    }

    @Override
    public void onEvent(GameEvent event) {
        if (event.getType() == GameEventType.BOSS_PHASE_CHANGED) {
            String loot = event.getValue() == 2 ? randomLoot(PHASE_TWO_LOOT) : randomLoot(PHASE_THREE_LOOT);
            System.out.printf("[LOOT] Phase %d reward dropped: %s%n", event.getValue(), loot);
            return;
        }

        if (event.getType() == GameEventType.BOSS_DEFEATED) {
            System.out.printf("[LOOT] Final reward dropped: %s%n", randomLoot(FINAL_LOOT));
        }
    }

    private String randomLoot(String[] lootTable) {
        return lootTable[random.nextInt(lootTable.length)];
    }
}
