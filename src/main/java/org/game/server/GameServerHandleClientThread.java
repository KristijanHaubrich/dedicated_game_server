package org.game.server;

import org.game.components.GameMessageDecoder;
import org.game.components.Quad;
import org.game.observer.GameObserver;
import org.game.observer.GameSubject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class GameServerHandleClientThread extends Thread{
    private final Socket socket;
    private final int clientId;
    GameServerHandleClientThread(Socket clientSocket,int clientId){
        super("GameServerHandleClientThread");
        this.socket = clientSocket;
        this.clientId = clientId;
    }

    @Override
    public void run(){
        GameSubject gameSubject = GameSubject.getInstance();
        GameObserver currentGameObserver = new GameObserver(this.socket);
        gameSubject.subscribe(currentGameObserver);

        try(
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(
                                socket.getInputStream()));
                 PrintWriter out =
                        new PrintWriter(socket.getOutputStream(), true);

                ){

            while(true){
                String clientInput;
                String CLIENT_ID = "klijent"+clientId;
                if((clientInput = in.readLine()) != null){
                    if(clientInput.equals("close")) {
                        gameSubject.unsubscribe(currentGameObserver);
                        gameSubject.notifyObservers("remove__"+CLIENT_ID);
                        out.println("close");
                        break;
                    }
                    else{
                        Quad quad = GameMessageDecoder.getInstance().decodeMessage(clientInput);
                        quad.setQuadId(CLIENT_ID);
                        String finalMessage = quad.toString();
                        gameSubject.notifyObservers(finalMessage,currentGameObserver);
                    }
                }
            }

        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
