/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import com.sun.net.ssl.internal.ssl.Provider;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLSocket;

/**
 *
 * @author Simona Simoni
 */
public class SSLSocketHandler extends Thread {
    /**
     * Es gibt eine globale Variable für den Port und Instanzvariablen für die
     * DataWrapper-Klasse und den ServerSocket
     */
    DataWrapper dW;
    int PORT2;
    SSLServerSocket s2;

    /**
     * Es wird ein parametrisierter Konstruktor erstellt Es enthält
     * Referenzvariablen, die verschiedene Variablen referenzieren und dadurch
     * kann man sie besser erkennen
     *
     * @param s2
     * @param dW
     * @param PORT2
     */
    public SSLSocketHandler(SSLServerSocket s2, DataWrapper dW, int PORT2) {
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
    @Override
    public void run() {
        while (true) {
            SSLSocket s;
            try {
                s = (SSLSocket)s2.accept();
                new ServerThread(dW, s).start();
            } catch (IOException ex) {
                Logger.getLogger(HomeServerHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
