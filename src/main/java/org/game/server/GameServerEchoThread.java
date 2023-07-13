package org.game.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.concurrent.atomic.AtomicBoolean;

public class GameServerEchoThread extends Thread{
    private final String server_address;
    private final String server_port;
    private final AtomicBoolean isServerUp;

    public GameServerEchoThread(String server_address, String server_port, AtomicBoolean isServerUp) {
        super("GameServerEchoThread");
        this.server_address = server_address;
        this.server_port = server_port;
        this.isServerUp = isServerUp;
    }

    @Override
    public void run() {
        try (
                DatagramSocket socket = new DatagramSocket()
        ) {
            socket.setBroadcast(true);
            while (isServerUp.get()) {
                String msg = server_address + "__" + server_port;
                byte[] buffer = msg.getBytes();

                InetAddress address = InetAddress.getByName("255.255.255.255");

                socket.setBroadcast(true);
                DatagramPacket packet
                        = new DatagramPacket(buffer, buffer.length, address, 4444);
                socket.send(packet);
                Thread.sleep(1000);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
    }
}
