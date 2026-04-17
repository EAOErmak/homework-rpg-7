package com.narxoz.rpg.observer;

import java.util.HashSet;
import java.util.Set;

/**
 * Unlocks simple encounter achievements as milestones are reached.
 */
public class AchievementTracker implements GameObserver {

    private final Set<String> unlockedAchievements = new HashSet<>();
    private int landedAttacks;

    @Override
    public void onEvent(GameEvent event) {
        switch (event.getType()) {
            case ATTACK_LANDED -> handleAttack();
            case HERO_DIED -> unlock("costly-victory", "A Costly Victory");
            case BOSS_DEFEATED -> unlock("dungeon-cleared", "Dungeon Cleared");
            default -> {
            }
        }
    }

    private void handleAttack() {
        landedAttacks++;

        if (landedAttacks >= 1) {
            unlock("opening-strike", "Opening Strike");
        }
        if (landedAttacks >= 6) {
            unlock("battle-rhythm", "Battle Rhythm");
        }
    }

    private void unlock(String key, String title) {
        if (unlockedAchievements.add(key)) {
            System.out.printf("[ACHIEVEMENT] Unlocked: %s%n", title);
        }
    }
}
