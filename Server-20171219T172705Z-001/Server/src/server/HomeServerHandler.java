/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author User
 */
public class HomeServerHandler extends Thread {
    DataWrapper dW;
    int PORT1;
    ServerSocket s1;

    public HomeServerHandler(ServerSocket s1, DataWrapper dW, int PORT1) {
        this.dW = dW;
        this.PORT1 = PORT1;
        this.s1 = s1;
    }
 
    
    public void run() {
        while(true){
            Socket socket1;
            try {
                socket1 = s1.accept();
                new SmartHomeServer(dW,socket1).start();
            } catch (IOException ex) {
                Logger.getLogger(HomeServerHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}
