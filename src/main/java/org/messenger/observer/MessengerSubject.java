package org.messenger.observer;

import org.messenger.observer.interfaces.IObserver;
import org.messenger.observer.interfaces.ISubject;
import java.util.HashSet;
import java.util.Set;

public class MessengerSubject implements ISubject {
    private final Set<IObserver> observers = new HashSet<>();
    private static final MessengerSubject instance = new MessengerSubject();

    private MessengerSubject() {
    }

    public static MessengerSubject getInstance() {
        return instance;
    }

    @Override
    public void subscribe(IObserver iObserver) {
        if (!observers.contains(iObserver)) {
            observers.add(iObserver);
        }
    }

    @Override
    public void unsubscribe(IObserver iObserver) {
        observers.remove(iObserver);
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
}
