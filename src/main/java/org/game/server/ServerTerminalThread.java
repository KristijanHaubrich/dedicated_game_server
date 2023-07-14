package org.game.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.atomic.AtomicBoolean;

public class ServerTerminalThread extends Thread{
    private final AtomicBoolean isServerUp;
    ServerTerminalThread(AtomicBoolean isServerUp){
        super("ServerTerminalThread");
        this.isServerUp = isServerUp;
    }

    @Override
    public void run() {
       try(
               BufferedReader stdIn =
                       new BufferedReader(new InputStreamReader(System.in))
               ){
           String input;
           while(true){
               //close server
               if((input = stdIn.readLine()) != null && input.equals("close")){
                   this.isServerUp.set(false);
                   break;
               }
           }
       }catch (IOException e){
           e.printStackTrace();
       }
    }
}
