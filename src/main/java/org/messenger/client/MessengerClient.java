package org.messenger.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.Socket;

public class MessengerClient {
    public static void main(String[] args) throws IOException {
        try (
                DatagramSocket UDPSocket = new DatagramSocket(null);
                BufferedReader stdIn =
                        new BufferedReader(new InputStreamReader(System.in))
        ) {
            String hostName;
            int portNumber;
            byte[] buf = new byte[256];

            UDPSocket.setReuseAddress(true);
            UDPSocket.bind(new InetSocketAddress(4444));

            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            UDPSocket.receive(packet);

            String received = new String(packet.getData(), 0, packet.getLength());
            String[] data = received.split("__", 2);

            hostName = data[0];
            portNumber = Integer.parseInt(data[1]);

            //socket and out are used after in MessengerClientReadServerThread, so they are closed there
            Socket socket = new Socket(hostName, portNumber);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            new MessengerClientReadServerThread(socket, hostName).start();

            while (true) {
                String clientMessage = stdIn.readLine();
                out.println(clientMessage);
                if (clientMessage.equals("close")) {
                    break;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
