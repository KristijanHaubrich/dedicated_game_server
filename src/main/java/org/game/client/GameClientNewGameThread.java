package org.game.client;

import org.game.components.GameMessageDecoder;
import org.game.components.Quad;
import org.game.components.ServerConnection;
import org.game.components.ServerProxy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
                        new InputStreamReader(socket.getInputStream()))
        ) {
            ServerProxy serverProxy = ServerProxy.getInstance();
            serverProxy.setOutput(socket);

            new GameThread().start();

            String serverInput;
            while (serverProxy.isOpenConnection()) {
                if ((serverInput = in.readLine()) != null) {
                    Quad quad = GameMessageDecoder.getInstance().decodeMessage(serverInput);

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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

