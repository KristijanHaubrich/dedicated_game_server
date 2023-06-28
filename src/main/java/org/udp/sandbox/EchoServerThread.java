package org.udp.sandbox;

import java.io.IOException;
import java.net.*;

//UDP PLAYGROUND
public class EchoServerThread extends Thread{
    private final InetAddress address;
    private final String msg;
    private final int port;
    public EchoServerThread(InetAddress address,String msg,int port) {
        super("EchoServerThread");
        this.address = address;
        this.msg = msg;
        this.port = port;
    }
    @Override
    public void run(){
        try(
                DatagramSocket socket = new DatagramSocket()
        )
        {
            socket.setBroadcast(true);
            while(true){
                byte[] buffer = msg.getBytes();
                DatagramPacket packet
                        = new DatagramPacket(buffer, buffer.length, address,port);
                socket.send(packet);
                Thread.sleep( 1000);
            }

        } catch (IOException e) {
            e.printStackTrace();}
        catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }

    }

}
