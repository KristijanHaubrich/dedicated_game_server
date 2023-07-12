package org.game.components;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

public class ServerProxy {
    private final Set<Quad> quads = new HashSet<>();
    private PrintWriter out = null;
    private Boolean isOpenConnection = true;
    private static final ServerProxy instance = new ServerProxy();
    private ServerProxy() {}
    public static ServerProxy getInstance() {
        return instance;
    }

    public void addQuad(Quad quad){
        synchronized (quads){
            quads.add(quad);
        }
    }
    public void removeQuad(Quad quad){
        synchronized (quads){
            quads.remove(quad);
        }
    }
    public Set<Quad> getQuads() {
        synchronized (quads){
            return quads;
        }
    }
    public Boolean alreadyExist(String quadId){
        synchronized (quads){
            for (Quad quad : quads) {
                if(quad.getQuadId().equals(quadId)) return true;
            }
            return false;
        }
    }
    public void notifyServer(String msg){
        this.out.println(msg);
    }
    public void closeConnection(){
        this.isOpenConnection = false;
    }
    public Boolean isOpenConnection(){
        return this.isOpenConnection;
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
