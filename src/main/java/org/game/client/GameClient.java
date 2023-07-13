package org.game.client;

import java.util.concurrent.atomic.AtomicBoolean;

public class GameClient {
    public static void main(String[] args){
        AtomicBoolean isServerPicked = new AtomicBoolean(false);
        new GameClientEchoThread(isServerPicked).start();
        new GameClientPickServerThread(isServerPicked).start();
    }
}
