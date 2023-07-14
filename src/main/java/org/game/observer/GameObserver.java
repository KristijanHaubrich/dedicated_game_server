package org.game.observer;

import org.messenger.observer.interfaces.IObserver;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class GameObserver implements IObserver {
    private PrintWriter out;
    private final String clientId;
    public GameObserver(Socket socket,String clientId) {
        this.clientId = clientId;
        try {
            this.out =
                    new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
