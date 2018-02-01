/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import javax.net.ssl.*;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.Scanner;
import javax.net.SocketFactory;

/**
 *
 * @author Simona
 */
public class Server {

    public static final int PORT = 50001;
    public static final int PORT1 = 50004;

    DataWrapper dW;
    SmartHomeServer sH;
    ServerThread sT;
    Socket socket;
    Socket socket1;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, Exception {
        // TODO code application logic here
        new Server().runServer();
    }

    public void runServer() throws IOException {

        SocketFactory newSF = SSLSocketFactory.getDefault();
        dW = new DataWrapper();
        ServerSocket s = new ServerSocket(PORT);
        ServerSocket s1 = new ServerSocket(PORT1);
        System.out.println("Der Server ist eingeschaltet und bereit für Verbindungen...");

        HomeServerHandler hsh = new HomeServerHandler(s1, dW, PORT1);
        hsh.start();

        while (true) {
            Socket socket = s.accept();
            new ServerThread(dW, socket).start();
            
        }
    }

}
