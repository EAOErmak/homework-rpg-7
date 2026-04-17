package com.narxoz.rpg.observer;

import com.narxoz.rpg.combatant.BattleHero;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Provides emergency healing when a hero drops to low HP.
 */
public class PartySupport implements GameObserver {

    private final List<BattleHero> party;
    private final int healAmount;
    private final Random random;

    public PartySupport(List<BattleHero> party, int healAmount, long seed) {
        this.party = List.copyOf(party);
        this.healAmount = healAmount;
        this.random = new Random(seed);
    }

    @Override
    public void onEvent(GameEvent event) {
        if (event.getType() != GameEventType.HERO_LOW_HP) {
            return;
        }

        List<BattleHero> livingHeroes = new ArrayList<>();
        List<BattleHero> damagedHeroes = new ArrayList<>();
        for (BattleHero hero : party) {
            if (!hero.isAlive()) {
                continue;
            }
            livingHeroes.add(hero);
            if (hero.getHp() < hero.getMaxHp()) {
                damagedHeroes.add(hero);
            }
        }

        List<BattleHero> candidates = damagedHeroes.isEmpty() ? livingHeroes : damagedHeroes;
        if (candidates.isEmpty()) {
            return;
        }

        BattleHero target = candidates.get(random.nextInt(candidates.size()));
        int hpBeforeHeal = target.getHp();
        target.heal(healAmount);
        int healedAmount = target.getHp() - hpBeforeHeal;

        System.out.printf(
                "[SUPPORT] A guardian spirit restores %d HP to %s after %s drops low.%n",
                healedAmount,
                target.getName(),
                event.getSourceName()
        );
    }
}
