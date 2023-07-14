package org.game.client;

import org.game.components.GameMessageDecoder;
import org.game.components.Quad;
import org.game.components.ServerConnection;
import org.game.components.ServerProxy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class GameClientNewGameThread extends Thread {
    private final ServerConnection serverConnection;

    public GameClientNewGameThread(ServerConnection serverConnection) {
        super("GameClientNewGameThread");
        this.serverConnection = serverConnection;
    }

    @Override
    public void run() {
        try (
                Socket socket = new Socket(this.serverConnection.getHostname(), this.serverConnection.getPortNumber());
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
                PrintWriter out =
                        new PrintWriter(socket.getOutputStream(), true)
        ) {
            ServerProxy serverProxy = ServerProxy.getInstance();
            serverProxy.setOutput(socket);

            new GameThread().start();
            String serverInput;
            //tell server to fetch positions of other clients upon initialization
            out.println("sendInitialPositions");
            while (true) {
                if ((serverInput = in.readLine()) != null) {
                    //close thread after server detected GameThread is closed (can't use boolean bc readLine() stops thread and for that time thread don't see boolean changes)
                    if (serverInput.equals("close")) break;

                    String[] data = serverInput.split("__", 13);
                    //send position of current client to specific client via server
                    if (data[0].equals("sendPositionFor")) {
                        for (Quad quad : ServerProxy.getInstance().getQuads()) {
                            if (quad.getQuadId().equals("my_avatar")) {
                                out.println("sendingPositionFor__" + data[1] + "__" + quad);
                            }
                        }
                    }
                    //remove avatar of specific client from the game
                    else if (data[0].equals("remove")) {
                        ServerProxy.getInstance().removeQuad(data[1]);
                    }
                    //change position of specific avatar in the game
                    else {
                        Quad quad = GameMessageDecoder.getInstance().decodeMessage(data);
                        if (serverProxy.alreadyExist(quad.getQuadId())) {

                            for (Quad quad1 : ServerProxy.getInstance().getQuads()) {

                                if (quad1.getQuadId().equals(quad.getQuadId())) {
                                    quad1.updateQuad(quad.getVertices());
                                }
                            }

                        } else {
                            serverProxy.addQuad(quad);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

