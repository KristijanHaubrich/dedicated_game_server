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
import java.util.concurrent.atomic.AtomicBoolean;

public class GameServerHandleClientThread extends Thread{
    private final Socket socket;
    private final AtomicBoolean isServerUp;
    private final String clientId;
    GameServerHandleClientThread(Socket clientSocket, int clientId, AtomicBoolean isServerUp){
        super("GameServerHandleClientThread");
        this.socket = clientSocket;
        this.isServerUp = isServerUp;
        this.clientId = "klijent"+clientId;
    }

    @Override
    public void run(){
        GameSubject gameSubject = GameSubject.getInstance();
        GameObserver currentGameObserver = new GameObserver(this.socket,this.clientId);
        gameSubject.subscribe(currentGameObserver);

        try(
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(
                                socket.getInputStream()));
                 PrintWriter out =
                        new PrintWriter(socket.getOutputStream(), true)

                ){
            while(true){
                String clientInput;
                if((clientInput = in.readLine()) != null){
                    //closing thread on client command
                    if(clientInput.equals("close") || !this.isServerUp.get()) {
                        gameSubject.unsubscribe(currentGameObserver);
                        gameSubject.notifyObservers("remove__"+this.clientId);
                        out.println("close");
                        break;
                    }
                    //start process of fetching positions of other clients on client command
                    else if(clientInput.equals("sendInitialPositions")){
                        gameSubject.requestPositions(this.clientId);
                    }
                    //get position of current client and send it to other clients
                    else{
                        String[] data = clientInput.split("__",15);
                        Quad quad = GameMessageDecoder.getInstance().decodeMessage(data);
                        quad.setQuadId(this.clientId);

                        if(data[0].equals("sendingPositionFor")){
                            GameSubject.getInstance().sendPosition(data[1],quad.toString());
                        }else{
                            String finalMessage = quad.toString();
                            gameSubject.notifyObservers(finalMessage,currentGameObserver);
                        }
                    }
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
