package com.narxoz.rpg.observer;

import com.narxoz.rpg.combatant.BattleHero;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Tracks and prints a compact summary of hero status changes.
 */
public class HeroStatusMonitor implements GameObserver {

    private final List<BattleHero> party;

    public HeroStatusMonitor(List<BattleHero> party) {
        this.party = List.copyOf(party);
    }

    @Override
    public void onEvent(GameEvent event) {
        if (event.getType() != GameEventType.HERO_LOW_HP && event.getType() != GameEventType.HERO_DIED) {
            return;
        }

        String summary = party.stream()
                .map(this::formatHeroStatus)
                .collect(Collectors.joining(" | "));

        System.out.printf("[STATUS] Triggered by %s: %s%n", event.getSourceName(), summary);
    }

    private String formatHeroStatus(BattleHero hero) {
        String condition;
        if (!hero.isAlive()) {
            condition = "DOWN";
        } else if (hero.getHp() * 100 < hero.getMaxHp() * 30) {
            condition = "LOW";
        } else {
            condition = "STABLE";
        }

        return String.format(
                "%s %d/%d HP [%s | %s]",
                hero.getName(),
                hero.getHp(),
                hero.getMaxHp(),
                condition,
                hero.getStrategy().getName()
        );
    }
}
