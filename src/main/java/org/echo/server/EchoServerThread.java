package org.echo.server;

import java.io.IOException;
import java.net.*;

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
            throw new RuntimeException(e);}
        catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }

    }

}
