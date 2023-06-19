package org.knockknock.messenger.ObserverPattern;

import org.knockknock.messenger.ObserverPattern.interfaces.IObserver;
import org.knockknock.messenger.ObserverPattern.interfaces.ISubject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class MessengerSubject implements ISubject {
    private List<IObserver> observers = new ArrayList<>();

    public void run(Socket socket) throws IOException {
        try(
                PrintWriter out =
                        new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()))
                ){
            while(true){
                    out.println("Pošalji svoju poruku podanicima:");
                    String clientMessage = "";
                    while(clientMessage.equals("")){
                        clientMessage = in.readLine();
                    }
                    notifyObservers(clientMessage);
                    clientMessage = "";
                    out.println("Želiš li ostati kralj?(y/n)");
                    while(clientMessage.equals("")){
                        clientMessage = in.readLine();
                    }
                    if(clientMessage.equals("n")){
                        socket.close();
                        break;
                    }
            }

        }catch (IOException e){
            e.printStackTrace();
        }


    }

    @Override
    public void sub(IObserver iObserver) {
        if(!observers.contains(iObserver)){
            observers.add(iObserver);
        }
    }

    @Override
    public void unsub(IObserver iObserver) {
        if(observers.contains(iObserver)){
            observers.remove(iObserver);
        }
    }

    @Override
    public void notifyObservers(String message) {
        if(!observers.isEmpty()){
            observers.forEach(
                    observer -> observer.update(message)
            );
        }
    }
}
