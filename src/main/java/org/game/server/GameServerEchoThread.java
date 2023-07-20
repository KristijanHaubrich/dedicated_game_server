package org.game.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.concurrent.atomic.AtomicBoolean;

public class GameServerEchoThread extends Thread{
    private final String serverAddress;
    private final String serverPort;
    private final AtomicBoolean isMainThreadServerUp;

    public GameServerEchoThread(String serverAddress, String serverPort, AtomicBoolean isMainServerThreadUp) {
        super("GameServerEchoThread");
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
        this.isMainThreadServerUp = isMainServerThreadUp;
    }

    @Override
    public void run() {
        try (
                DatagramSocket socket = new DatagramSocket()
        ) {
            socket.setBroadcast(true);
            while (isMainThreadServerUp.get()) {
                String msg = serverAddress + "__" + serverPort;
                byte[] buffer = msg.getBytes();

                InetAddress address = InetAddress.getByName("255.255.255.255");

                DatagramPacket packet
                        = new DatagramPacket(buffer, buffer.length, address, 4444);
                socket.send(packet);
                Thread.sleep(1000);
            }
            //last echo to last client to close his GameClientEhoThread
            String msg = serverAddress + "__" + serverPort;
            byte[] buffer = msg.getBytes();

            InetAddress address = InetAddress.getByName("255.255.255.255");

            socket.setBroadcast(true);
            DatagramPacket packet
                    = new DatagramPacket(buffer, buffer.length, address, 4444);
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
    }
}
