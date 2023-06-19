package org.knockknock.messenger.ObserverPattern;

import org.knockknock.messenger.ObserverPattern.interfaces.IObserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class MessengerObserver implements IObserver {
    private Socket socket;
    public MessengerObserver(Socket socket){
       this.socket = socket;
    }
    @Override
    public void update(String message){
        try{
            PrintWriter out =
                    new PrintWriter(socket.getOutputStream(), true);
            {
                out.println(message);
            }

        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
