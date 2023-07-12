package org.game.client;
public class GameClient {
    public static void main(String[] args){
        new GameClientEchoThread().start();
        new GameClientPickServerThread().start();
    }
}
