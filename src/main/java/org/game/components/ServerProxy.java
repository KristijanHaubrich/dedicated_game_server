package org.game.components;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class ServerProxy {
    private static final ServerProxy instance = new ServerProxy();
    private final Set<Quad> quads = new HashSet<>();
    private PrintWriter out = null;

    private ServerProxy() {
    }

    public static ServerProxy getInstance() {
        return instance;
    }

    public void addQuad(Quad quad) {
        synchronized (quads) {
            quads.add(quad);
        }
    }

    public void removeQuad(String quadId) {
        synchronized (quads) {
            for (Iterator<Quad> iterator = quads.iterator(); iterator.hasNext(); ) {
                Quad quad = iterator.next();
                if (quad.getQuadId().equals(quadId)) {
                    iterator.remove();
                }
            }
        }
    }

    public Set<Quad> getQuads() {
        synchronized (quads) {
            return quads;
        }
    }

    public Boolean alreadyExist(String quadId) {
        synchronized (quads) {
            for (Quad quad : quads) {
                if (quad.getQuadId().equals(quadId)) return true;
            }
            return false;
        }
    }

    public void notifyServer(String msg) {
        if (!msg.equals("close")) {
            synchronized (quads) {
                String[] data = msg.split("__", 13);
                Quad myQuad = GameMessageDecoder.getInstance().decodeMessage(data);
                for (Quad quad : quads) {
                    if (quad.getQuadId().equals(myQuad.getQuadId())) quad.updateQuad(myQuad.getVertices());
                }
            }
        }
        this.out.println(msg);
    }

    public void setOutput(Socket socket) {
        try {
            this.out =
                    new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
