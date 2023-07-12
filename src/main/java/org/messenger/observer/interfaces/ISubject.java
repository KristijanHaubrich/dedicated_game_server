package org.messenger.observer.interfaces;

public interface ISubject {
    void subscribe(IObserver iObserver);

    void unsubscribe(IObserver iObserver);

    void notifyObservers(String message, IObserver observer);

    void notifyObservers(String message);

}
