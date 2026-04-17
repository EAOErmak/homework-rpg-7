package com.narxoz.rpg.observer;

/**
 * Prints every event that flows through the encounter.
 */
public class BattleLogger implements GameObserver {

    @Override
    public void onEvent(GameEvent event) {
        switch (event.getType()) {
            case ATTACK_LANDED ->
                    System.out.printf("[LOG] %s lands a hit for %d damage.%n", event.getSourceName(), event.getValue());
            case HERO_LOW_HP ->
                    System.out.printf("[LOG] %s falls into critical range at %d HP.%n", event.getSourceName(), event.getValue());
            case HERO_DIED ->
                    System.out.printf("[LOG] %s has fallen in battle.%n", event.getSourceName());
            case BOSS_PHASE_CHANGED ->
                    System.out.printf("[LOG] %s triggers boss phase %d.%n", event.getSourceName(), event.getValue());
            case BOSS_DEFEATED ->
                    System.out.printf("[LOG] %s has been defeated.%n", event.getSourceName());
        }
    }
}
