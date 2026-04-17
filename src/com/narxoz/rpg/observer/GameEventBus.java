package com.narxoz.rpg.observer;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Simple synchronous publisher for game events.
 */
public class GameEventBus {

    private final Set<GameObserver> observers = new LinkedHashSet<>();

    public void registerObserver(GameObserver observer) {
        observers.add(Objects.requireNonNull(observer, "observer"));
    }

    public void unregisterObserver(GameObserver observer) {
        observers.remove(observer);
    }

    public void publish(GameEvent event) {
        for (GameObserver observer : List.copyOf(observers)) {
            observer.onEvent(event);
        }
    }
}
