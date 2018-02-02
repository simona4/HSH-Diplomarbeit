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
 * Diese Klasse ist ein Thread und ist auch für die Kommunikation zwischen dem
 * Arduino und dem Server verantwortlich. Sie erweitert die Klasse Thread
 *
 * @author User
 */
public class HomeServerHandler extends Thread {

    /**
     * Es gibt eine globale Variable für den Port und Instanzvariablen für die
     * DataWrapper-Klasse und den ServerSocket
     */
    DataWrapper dW;
    int PORT1;
    ServerSocket s1;

    /**
     * Es wird ein parametrisierter Konstruktor erstellt Es enthält
     * Referenzvariablen, die verschiedene Variablen referenzieren und dadurch
     * kann man sie besser erkennen
     *
     * @param s1
     * @param dW
     * @param PORT1
     */
    public HomeServerHandler(ServerSocket s1, DataWrapper dW, int PORT1) {
        this.dW = dW;
        this.PORT1 = PORT1;
        this.s1 = s1;
    }

    /**
     * Die Methode run() enthält die Verbindung mit der Klasse, die
     * Kommunikation des Servers mit dem Arduino passiert Dieser Thread wird
     * gestartet Falls es Input/Output Exceptions passiert werden, werden sie
     * mit einem catch-Block abgefangen
     */
    public void run() {
        while (true) {
            Socket socket1;
            try {
                socket1 = s1.accept();
                new SmartHomeServer(dW, socket1).start();
            } catch (IOException ex) {
                Logger.getLogger(HomeServerHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
