package org.knockknock.messenger;

import org.knockknock.messenger.observer.pattern.MessengerObserver;
import org.knockknock.messenger.observer.pattern.MessengerSubject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class MessengerServerThread extends Thread {
    private final Socket socket;
    private final MessengerSubject messengerSubject;
    private final MessengerObserver clientObserver;
    private final int clientId;
    public MessengerServerThread(Socket socket, MessengerSubject messengerSubject, MessengerObserver clientObserver, int clientId) {
        super("MessengerServerThread");
        this.socket = socket;
        this.messengerSubject = messengerSubject;
        this.clientObserver = clientObserver;
        this.clientId = clientId;
    }
    public void run() {
        try(
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(
                                socket.getInputStream()));
                 PrintWriter out =
                         new PrintWriter(socket.getOutputStream(), true);
                )
        {
            out.println("Dobrodošli u Poslovni Chat (za napuštanje chat-a unesite 'close')\n-----------------------------------------------------------------------------------");
            messengerSubject.notifyObservers("{ Priključio se " + clientId + ". klijent }",clientObserver);
            messengerSubject.subscribe(clientObserver);

            while(true){
                String clientMessage;
                if((clientMessage = in.readLine()) != null){
                    if(clientMessage.equals("close")){
                        messengerSubject.unsubscribe(clientObserver);
                        messengerSubject.notifyObservers("{ "+ clientId + ". klijent napušta chat }");
                        out.println("Napuštaš razgovor");
                        break;
                    }
                    messengerSubject.notifyObservers(clientId + ". klijent: " + clientMessage,clientObserver);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
