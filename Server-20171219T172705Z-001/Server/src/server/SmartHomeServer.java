/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Diese Klasse ist für die Verbindung mit dem Arduino verantwortlich. Sie ist
 * ein Thread
 *
 * @author Simona
 */
public class SmartHomeServer extends Thread {

    DataWrapper dW; //deklariert eine neue Variale mit dem Namen Socket
    Socket socket1; //deklariert eine neue Instanzvariable mit dem Namen DataWrapper

    PrintWriter pW; //Instantzvariable mit dem Namen PrintWriter
    BufferedReader bR; //Instanzvariable mit dem Namen BufferedReader
    String username; //deklariert eine neue Variable mit dem Namen username und dem Datentyp String
    String passwort; //deklariert eine neue Variable mit dem Namen passwort und dem Datentyp String

    /**
     * Ein Konstruktor mit dem gleichen Namen wie der Name der Klasse erstellt
     * Das ist ein parametrisierter Konstruktor und enthält zwei Parameter
     *
     * @param socket1, gibt das Socket
     * @param dW, hier steht die Instanzvariable für die DataWrapper-Klasse
     */
    SmartHomeServer(DataWrapper dW, Socket socket1) throws IOException {
        this.dW = dW;
        this.socket1 = socket1;
    }

    public void run() { //Methode run(): der Thread ist jetzt in einem lauffähigen Zustand

        try {
            System.out.println("hsh instance started"); //der Thread ist gestartet
            boolean login = false; //login Variable auf false initialisieren
            boolean doWork = true; //doWork Varibale auf true initialisieren
            System.out.println(socket1.toString()); //wandelt den Socket in einer String um
            pW = new PrintWriter(socket1.getOutputStream(), true); //gibt den Socket in Bytes aus
            bR = new BufferedReader(new InputStreamReader(socket1.getInputStream())); //speichert den Socket in einem Puffer zuerst

            while (doWork) {
                String m1 = bR.readLine(); //der Server kriegt eine Nachricht vom Arduino und speichert es in dem Puffer
                String[] split1 = m1.split(":"); //die Nachricht wird in einem Array gespeichert
                System.out.println("Das Haus schickt jetzt eine Nachricht: " + m1);

                /**
                 * Der Arduino möchte sich einloggen. Er schickt zuerst eine
                 * Login-Anfrage. Der Server entscheidet, ob er diese Anfrage
                 * akzeptieren kann oder nicht. Falls der Server die Anfrage
                 * akzeptiert, schickt er login_successful an deb Arduino zurück
                 * Falls die Anfrage nicht erfolgreicht ist, sendet der Server
                 * login_unsuccessful Der Ardunino kann den Status des Hauses
                 * nicht ändern, deshalb braucht er keine Update-State Methode
                 */
                if (split1[0].matches(".*login.*")) {
                    if (login == true) {
                        pW.println("already_logged_in");
                        pW.flush();
                        doWork = false;
                    } else {
                        if (split1[1].equals("Client") && split1[2].equals("1234")) {
                            System.out.println("login test ausgabe: successful");
                            pW.println("login_successful");
                            pW.flush();
                            login = true;
                        } else {
                            pW.println("login_unsuccessful");
                            pW.flush();
                            doWork = false;
                        }
                    }
                } /**
                 * Der Arduino möchte den aktuellen Status des Hauses wissen.
                 * Sie schiken eine Get-Anfrage. Falls diese Anfrage eine
                 * Zusagung bekommt, schickt der Server get_accepted zurück und
                 * danach werden die Werte der Bauteile an den Arduino
                 * geschickt. Der Thread macht eine Pause für 1 Sekunde und
                 * startet wieder. Wenn die Get-Anfrage nicht erfolgreich ist,
                 * sendet der Server get_denied an die Clients zurück
                 */
                else if (m1.matches(".*Get.*")) {
                    if (login) {
                        pW.println("get_accepted");
                        pW.flush();
                        Thread.sleep(100);

                        pW.println(dW.getLed1());
                        pW.flush();
                        Thread.sleep(100);

                        pW.println(dW.getLed2());
                        pW.flush();
                        Thread.sleep(100);

                        pW.println(dW.getLed3());
                        pW.flush();
                        Thread.sleep(100);

                        pW.println(dW.getLed4());
                        pW.flush();
                        Thread.sleep(100);

                        pW.println(dW.getLed5());
                        pW.flush();
                        Thread.sleep(100);

                        pW.println(dW.getPosF1());
                        pW.flush();
                        Thread.sleep(100);

                        pW.println(dW.getPosF2());
                        pW.flush();
                        Thread.sleep(100);

                        pW.println(dW.getPosG());
                        pW.flush();
                        Thread.sleep(100);

                        pW.println(dW.getPosT());
                        pW.flush();
                        Thread.sleep(100);

                        pW.println(dW.getHg());
                        pW.flush();
                        Thread.sleep(100);

                        pW.println(dW.getKa());
                        pW.flush();
                        Thread.sleep(100);

                        pW.println(dW.getVentilator());
                        pW.flush();
                        Thread.sleep(100);
                    } else {
                        pW.println("get_denied");
                        pW.flush();
                    }
                } /**
                 * Der Arduino möchten am Ende abmelden. Um diesen Aktion
                 * erfolgreich zu machen, schickt er "logout". Dieser Gesuch
                 * kann entweder in Ordnung oder nicht sein, Der Server stellt
                 * fest, ob er das annehmen kann oder nicht
                 */
                else if (split1[0].matches(".*logout.*")) {
                    if (login) {
                        pW.println("logout_successful");
                        System.out.println("Der Client wurde erfolgreich ausgeloggt");
                        login = false;
                    } else {
                        pW.println("logout_unsuccessful");
                    }
                    pW.flush();
                    doWork = false;
                } /**
                 * Falls der Arduino irgendwas hineinschreibt oder eine sinnlose
                 * Anfrage an dem Server macht, antwortet der Server mit einer
                 * invalid_request Antwort
                 */
                else {
                    pW.println("invalid_request");
                    pW.flush();
                }

            }
            /**
             * Der PrintWriter, der BufferedReader und der Socket werden hier
             * geschlossen. Das Prozess ist fertig
             */
            pW.close();
            bR.close();
            socket1.close();

        }/**
         * Hier werden ein Input/Output Exception und ein SQL-Exception
         * abgefangen
         */
        catch (IOException e) {
            System.out.println(e);
        } catch (InterruptedException ex) {
            Logger.getLogger(SmartHomeServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
