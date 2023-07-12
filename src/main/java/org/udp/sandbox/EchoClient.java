package org.udp.sandbox;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

//UDP PLAYGROUND
public class EchoClient {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage: java KnockKnockServer <port number>");
            System.exit(1);
        }
        int listeningPort = Integer.parseInt(args[0]);
        try (
                DatagramSocket socket = new DatagramSocket(listeningPort)
        ) {
            byte[] buf = new byte[256];

            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            socket.receive(packet);

            String received = new String(packet.getData(), 0, packet.getLength());

            String[] msg = received.split("__", 2);
            System.out.println("Server address: " + msg[0] + " Server port: " + msg[1]);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
