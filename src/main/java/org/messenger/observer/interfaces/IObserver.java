package org.messenger.observer.interfaces;

public interface IObserver {
    void update(String message);
    String getId();
}
