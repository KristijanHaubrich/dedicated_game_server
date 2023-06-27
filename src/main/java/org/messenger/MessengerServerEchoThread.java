package org.messenger;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
public class MessengerServerEchoThread extends Thread {
    private final String server_address;
    private final String server_port;
    public MessengerServerEchoThread(String server_address, String server_port){
        this.server_address = server_address;
        this.server_port = server_port;
    }

    public void run(){
        try(
                DatagramSocket socket = new DatagramSocket()
        )
        {
            socket.setBroadcast(true);
            while(true){
                String msg = server_address + "__" + server_port;
                byte[] buffer = msg.getBytes();

                InetAddress address = InetAddress.getByName("255.255.255.255");

                socket.setBroadcast(true);
                DatagramPacket packet
                        = new DatagramPacket(buffer, buffer.length,address,4444);
                socket.send(packet);
                Thread.sleep( 1000);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);}
        catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
    }
}
