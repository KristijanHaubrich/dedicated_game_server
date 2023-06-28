package org.messenger.observer.pattern;

import org.messenger.observer.pattern.interfaces.IObserver;
import org.messenger.observer.pattern.interfaces.ISubject;

import java.util.ArrayList;
import java.util.List;

public class MessengerSubject implements ISubject {
    private final List<IObserver> observers = new ArrayList<>();
    private static MessengerSubject instance = null;
    private MessengerSubject(){}
    public static MessengerSubject getInstance() {
        if(instance == null){
            instance = new MessengerSubject();
            return instance;
        }
        return instance;
    }

    @Override
    public void subscribe(IObserver iObserver) {
        if(!observers.contains(iObserver)){
            observers.add(iObserver);
        }
    }
    @Override
    public void unsubscribe(IObserver iObserver) {
            observers.remove(iObserver);
    }
    @Override
    public void notifyObservers(String message, IObserver blackListedObserver) {
        if(!observers.isEmpty()){
            synchronized (observers){
                observers.forEach(
                        observer ->{
                            if(observer != blackListedObserver){
                                observer.update(message);
                            }
                        }
                );
            }
        }
    }
    @Override
    public void notifyObservers(String message){
        if(!observers.isEmpty()){
            synchronized (observers){
                observers.forEach(
                        observer -> observer.update(message)
                );
            }
        }
    }
}
