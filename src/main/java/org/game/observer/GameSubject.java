package org.game.observer;

import org.messenger.observer.interfaces.IObserver;
import org.messenger.observer.interfaces.ISubject;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class GameSubject implements ISubject {
    private static final GameSubject instance = new GameSubject();
    private final Set<IObserver> observers = new HashSet<>();

    private GameSubject() {
    }

    public static GameSubject getInstance() {
        return instance;
    }

    @Override
    public void subscribe(IObserver iObserver) {
        observers.add(iObserver);
    }

    @Override
    public void unsubscribe(IObserver iObserver) {
        for (Iterator<IObserver> iterator = observers.iterator(); iterator.hasNext(); ) {
            IObserver currentObserver = iterator.next();
            if (currentObserver == iObserver) {
                iterator.remove();
            }
        }
    }

    @Override
    public void notifyObservers(String message, IObserver sendingObserver) {
        if (!observers.isEmpty()) {
            synchronized (observers) {
                observers.forEach(
                        observer -> {
                            if (observer != sendingObserver) {
                                observer.update(message);
                            }
                        }
                );
            }
        }
    }

    @Override
    public void notifyObservers(String message) {
        if (!observers.isEmpty()) {
            synchronized (observers) {
                observers.forEach(
                        observer -> observer.update(message)
                );
            }
        }
    }

    public void sendPosition(String clientId, String quad) {
        synchronized (observers) {
            if (!observers.isEmpty()) {
                for (IObserver observer : observers) {
                    if (observer.getId().equals(clientId)) observer.update(quad);
                }
            }
        }
    }

    public void requestPositions(String clientId) {
        synchronized (observers) {
            if (!observers.isEmpty()) {
                for (IObserver observer : observers) {
                    if (!observer.getId().equals(clientId)) {
                        observer.update("sendPositionFor__" + clientId);
                    }
                }
            }
        }
    }
}
