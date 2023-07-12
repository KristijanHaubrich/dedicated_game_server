package org.game.server;

import org.game.components.GameMessageDecoder;
import org.game.components.Quad;
import org.game.observer.GameObserver;
import org.game.observer.GameSubject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
        GameObserver currentgameObserver = new GameObserver(this.socket);
        gameSubject.subscribe(currentgameObserver);

        try(
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(
                                socket.getInputStream()));
                ){
            Boolean isConnected = true;
            while(isConnected){
                String clientInput;
                if((clientInput = in.readLine()) != null){
                    if(clientInput.equals("close")) isConnected = false;
                    else{
                        Quad quad = GameMessageDecoder.getInstance().decodeMessage(clientInput);
                        quad.setQuadId("klijent"+clientId);
                        String finalMessage = quad.toString();
                        gameSubject.notifyObservers(finalMessage,currentgameObserver);
                    }
                }
            }

        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
