package org.messenger.observer;

import org.messenger.observer.interfaces.IObserver;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class MessengerObserver implements IObserver {
    private PrintWriter out;
    private String clientId;
    public MessengerObserver(Socket socket,int clientId) {
        try {
            this.out =
                    new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.clientId = "Klijent"+clientId;
    }

    @Override
    public void update(String message) {
        out.println(message);
    }

    @Override
    public String getId() {
        return clientId;
    }
}
