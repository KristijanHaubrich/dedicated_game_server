package org.messenger.observer.pattern;

import org.messenger.observer.pattern.interfaces.IObserver;
import org.messenger.observer.pattern.interfaces.ISubject;

import java.util.ArrayList;
import java.util.List;

public class MessengerSubject implements ISubject {
    private final List<IObserver> observers = new ArrayList<>();
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
            observers.forEach(
                    observer ->{
                        if(observer != blackListedObserver){
                            observer.update(message);
                        }
                    }
            );
        }
    }
    @Override
    public void notifyObservers(String message){
        if(!observers.isEmpty()){
            observers.forEach(
                    observer -> observer.update(message)
            );
        }
    }
}