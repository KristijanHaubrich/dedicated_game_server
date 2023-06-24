package org.messenger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class MessengerClient {
    public static void main(String[] args) throws IOException {

        if (args.length != 2) {
            System.err.println(
                    "Usage: java EchoClient <host name> <port number>");
            System.exit(1);
        }

        String hostName = args[0];
        int portNumber = Integer.parseInt(args[1]);
        //socket se zatvori u MessengerClientThreadu jer tamo mora primiti zadnju poruku
        Socket socket = new Socket(hostName, portNumber);
        try(
                BufferedReader stdIn =
                        new BufferedReader(new InputStreamReader(System.in));
        ){
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            new MessengerClientReadServerThread(socket,hostName).start();

            while(true){
                String clientMessage = stdIn.readLine();
                out.println(clientMessage);
                if(clientMessage.equals("close")){
                    break;
                }
            }

        }
        catch (IOException e){
            e.printStackTrace();
        }

    }
}
