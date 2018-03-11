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
 * Das ist die Main-Klasse. Sie enthält die allerwichtigsten Verbindungen und
 * Methoden
 *
 * @author Simona
 */
public class Server {

    /**
     * Hier werden die Ports geöffnet, die die Verbindung mit den Clients
     * ermöglichen 50001: für die SWING- und Web Clients
     */
    public static final int PORT = 50001;
    public static final int PORT1 = 50004;
    public static final int PORT2 = 50005;

    /**
     * neue Instanzvariablen für jede Klasse und die Sockets erstellt
     */
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

    /**
     * in der run-Methode werden neue Instanzen für die andere Klassen erstellt
     * hier startet der Thread, der für die Verbindung mit dem Arduino zuständig
     * ist
     *
     * @throws IOException, keine Input/Output-Fehler
     */
    public void runServer() throws IOException {

        SocketFactory newSF = SSLSocketFactory.getDefault();
        dW = new DataWrapper();
        ServerSocket s = new ServerSocket(PORT);
        ServerSocket s1 = new ServerSocket(PORT1);
        System.out.println("Der Server ist eingeschaltet und bereit für Verbindungen...");

        HomeServerHandler hsh = new HomeServerHandler(s1, dW, PORT1);
        hsh.start();

        /**
         * in dieser while-Schleife startet nur die Verbindung mit den SWING-
         * und WEB Clients
         */
        while (true) {
            Socket socket = s.accept();
            new ServerThread(dW, socket).start();

        }
    }

}
