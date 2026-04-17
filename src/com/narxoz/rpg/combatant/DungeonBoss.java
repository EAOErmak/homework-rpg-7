package com.narxoz.rpg.combatant;

import com.narxoz.rpg.observer.GameEvent;
import com.narxoz.rpg.observer.GameEventBus;
import com.narxoz.rpg.observer.GameEventType;
import com.narxoz.rpg.observer.GameObserver;
import com.narxoz.rpg.strategy.BossAggressiveStrategy;
import com.narxoz.rpg.strategy.BossDesperateStrategy;
import com.narxoz.rpg.strategy.BossMeasuredStrategy;
import com.narxoz.rpg.strategy.CombatStrategy;

import java.util.Objects;

/**
 * Multi-phase boss that publishes battle events and updates
 * its own strategy only through observer notifications.
 */
public class DungeonBoss implements GameObserver {

    private final String name;
    private final int maxHp;
    private final int attackPower;
    private final int defense;
    private final GameEventBus eventBus;

    private int hp;
    private int phase;
    private CombatStrategy strategy;

    public DungeonBoss(String name, int hp, int attackPower, int defense, CombatStrategy strategy, GameEventBus eventBus) {
        this.name = Objects.requireNonNull(name, "name");
        this.hp = hp;
        this.maxHp = hp;
        this.attackPower = attackPower;
        this.defense = defense;
        this.strategy = Objects.requireNonNull(strategy, "strategy");
        this.eventBus = Objects.requireNonNull(eventBus, "eventBus");
        this.phase = 1;
    }

    public String getName() {
        return name;
    }

    public int getHp() {
        return hp;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public int getAttackPower() {
        return attackPower;
    }

    public int getDefense() {
        return defense;
    }

    public int getPhase() {
        return phase;
    }

    public CombatStrategy getStrategy() {
        return strategy;
    }

    public boolean isAlive() {
        return hp > 0;
    }

    public int getEffectiveAttackPower() {
        return strategy.calculateDamage(attackPower);
    }

    public int getEffectiveDefense() {
        return strategy.calculateDefense(defense);
    }

    public void takeDamage(int amount) {
        if (!isAlive()) {
            return;
        }

        hp = Math.max(0, hp - Math.max(0, amount));
        publishPhaseTransitionsIfNeeded();

        if (hp == 0) {
            eventBus.publish(new GameEvent(GameEventType.BOSS_DEFEATED, name, 0));
        }
    }

    @Override
    public void onEvent(GameEvent event) {
        if (event.getType() != GameEventType.BOSS_PHASE_CHANGED) {
            return;
        }
        if (!name.equals(event.getSourceName())) {
            return;
        }
        if (event.getValue() == phase) {
            return;
        }

        phase = event.getValue();
        strategy = createStrategyForPhase(phase);
        System.out.printf("[BOSS] %s enters phase %d and adopts %s.%n", name, phase, strategy.getName());
    }

    private void publishPhaseTransitionsIfNeeded() {
        if (phase == 1 && hp * 100 <= maxHp * 60) {
            eventBus.publish(new GameEvent(GameEventType.BOSS_PHASE_CHANGED, name, 2));
        }
        if (phase == 2 && hp * 100 < maxHp * 30) {
            eventBus.publish(new GameEvent(GameEventType.BOSS_PHASE_CHANGED, name, 3));
        }
    }

    private CombatStrategy createStrategyForPhase(int newPhase) {
        return switch (newPhase) {
            case 2 -> new BossAggressiveStrategy();
            case 3 -> new BossDesperateStrategy();
            default -> new BossMeasuredStrategy();
        };
    }
}
