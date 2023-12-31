package org.messenger.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

public class MessengerClientReadServerThread extends Thread {
    private final Socket socket;
    private final String hostName;

    public MessengerClientReadServerThread(Socket socket, String hostName) {
        super("MessengerClientThread");
        this.socket = socket;
        this.hostName = hostName;
    }

    @Override
    public void run() {
        try (
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()))
        ) {
            String fromServer;

            while (true) {
                if ((fromServer = in.readLine()) != null) {
                    System.out.println(fromServer);
                    if (fromServer.equals("Napuštaš razgovor")) {
                        System.out.println("-----------------------------------------------------------------------------------");
                        socket.close();
                        break;
                    }
                }
            }

        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " +
                    hostName);
            e.printStackTrace();
            System.exit(1);
        }
    }


}
