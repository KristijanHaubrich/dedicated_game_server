package org.messenger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;

public class MessengerClient {
    public static void main(String[] args) throws IOException {

        DatagramSocket UDPSocket = new DatagramSocket(4444);
        UDPSocket.setReuseAddress(true);
        byte[] buf = new byte[256];

        DatagramPacket packet = new DatagramPacket(buf,buf.length);
        UDPSocket.receive(packet);

        String received = new String(packet.getData(),0,packet.getLength());
        String[] data = received.split("__",2);

        String hostName = data[0];
        int portNumber = Integer.parseInt(data[1]);

        UDPSocket.close();

        //socket se zatvori u MessengerClientThreadu jer tamo mora primiti zadnju poruku
        Socket socket = new Socket(hostName,portNumber);
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
