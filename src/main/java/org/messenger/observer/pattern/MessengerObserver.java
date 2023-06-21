package org.messenger.observer.pattern;

import org.messenger.observer.pattern.interfaces.IObserver;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class MessengerObserver implements IObserver {
    private final Socket socket;

    public MessengerObserver(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void update(String message) {
        //try with resourses nakon prve poruke ovdje bi blokiro slanje ostalih
        try{
            PrintWriter out =
                    new PrintWriter(socket.getOutputStream(), true);
            out.println(message);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
