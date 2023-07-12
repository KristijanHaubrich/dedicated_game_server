package org.game.client;

import org.game.components.ServerCollector;
import org.game.components.ServerConnection;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

public class GameClientEchoThread extends Thread{
    public  GameClientEchoThread(){
        super("GameClientEchoThread");
    }
    @Override
    public void run(){
        try (
                DatagramSocket UDPSocket = new DatagramSocket(null);
        ) {
            String hostName;
            int portNumber;
            byte[] buf = new byte[256];

            UDPSocket.setReuseAddress(true);
            UDPSocket.bind(new InetSocketAddress(4444));
            while(true){
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                UDPSocket.receive(packet);

                String received = new String(packet.getData(), 0, packet.getLength());
                String[] data = received.split("__", 2);

                hostName = data[0];
                portNumber = Integer.parseInt(data[1]);

                boolean alreadyExist = false;
                for (ServerConnection serverConnection : ServerCollector.getInstance().getServerConnections()) {
                    if(serverConnection.getPortNumber() == portNumber){
                        alreadyExist = true;
                    }
                }
                 if(!alreadyExist){
                     ServerConnection serverConnection = new ServerConnection(hostName,portNumber);
                     ServerCollector.getInstance().addConnection(serverConnection);
                 }


            }

        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
