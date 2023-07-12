package org.game.observer;

import org.messenger.observer.interfaces.IObserver;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class GameObserver implements IObserver {
    private PrintWriter out;
    public GameObserver(Socket socket) {
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
}
