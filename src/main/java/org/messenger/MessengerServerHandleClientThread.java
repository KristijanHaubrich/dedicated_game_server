package org.messenger;

import org.messenger.observer.MessengerObserver;
import org.messenger.observer.MessengerSubject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class MessengerServerHandleClientThread extends Thread {
    private final Socket socket;
    private final int clientId;

    public MessengerServerHandleClientThread(Socket socket, int clientId) {
        super("MessengerServerThread");
        this.socket = socket;
        this.clientId = clientId;
    }

    @Override
    public void run() {
        try (
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(
                                socket.getInputStream()));
                PrintWriter out =
                        new PrintWriter(socket.getOutputStream(), true)
        ) {
            MessengerSubject messengerSubject = MessengerSubject.getInstance();
            MessengerObserver clientObserver = new MessengerObserver(socket);
            out.println("Dobrodošli u Poslovni Chat. Ime: " + clientId + ". Klijent (za napuštanje chat-a unesite 'close')\n-----------------------------------------------------------------------------------");
            messengerSubject.notifyObservers("{ Priključio se " + clientId + ". klijent }", clientObserver);
            messengerSubject.subscribe(clientObserver);

            while (true) {
                String clientMessage;
                if ((clientMessage = in.readLine()) != null) {
                    if (clientMessage.equals("close")) {
                        messengerSubject.unsubscribe(clientObserver);
                        messengerSubject.notifyObservers("{ " + clientId + ". klijent napušta chat }");
                        out.println("Napuštaš razgovor");
                        break;
                    }
                    messengerSubject.notifyObservers(clientId + ". klijent: " + clientMessage, clientObserver);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}