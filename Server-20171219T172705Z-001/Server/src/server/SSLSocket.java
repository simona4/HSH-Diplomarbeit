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
 * @author Simona Simoni
 */
public class SSLSocket extends Thread{
    /**
     * Es gibt eine globale Variable für den Port und Instanzvariablen für die
     * DataWrapper-Klasse und den ServerSocket
     */
    DataWrapper dW;
    int PORT2;
    ServerSocket s2;

    /**
     * Es wird ein parametrisierter Konstruktor erstellt Es enthält
     * Referenzvariablen, die verschiedene Variablen referenzieren und dadurch
     * kann man sie besser erkennen
     *
     * @param s2
     * @param dW
     * @param PORT2
     */
    public SSLSocket(ServerSocket s2, DataWrapper dW, int PORT2) {
        this.dW = dW;
        this.PORT2 = PORT2;
        this.s2 = s2;
    }

    /**
     * Die Methode run() enthält die Verbindung mit der Klasse, die
     * Kommunikation des Servers mit dem Arduino passiert Dieser Thread wird
     * gestartet Falls es Input/Output Exceptions passiert werden, werden sie
     * mit einem catch-Block abgefangen
     */
    public void run() {
        while (true) {
            Socket s;
            try {
                s = s2.accept();
                new SmartHomeServer(dW, s).start();
            } catch (IOException ex) {
                Logger.getLogger(HomeServerHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
