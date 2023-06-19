package org.knockknock.messenger.ObserverPattern.interfaces;

public interface ISubject {
    void sub(IObserver iObserver);
    void unsub(IObserver iObserver);
    void notifyObservers(String message);

}
