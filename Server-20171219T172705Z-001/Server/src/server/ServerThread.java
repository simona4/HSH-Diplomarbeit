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
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Diese Klasse ist für die Verbindung mit den SWING- und Web Client
 * verantwortlich
 *
 * @author Simona
 */
public class ServerThread extends Thread {

    Socket socket; //deklariert eine neue Variale mit dem Namen Socket
    DataWrapper dW; //deklariert eine neue Instanzvariable mit dem Namen DataWrapper

    PrintWriter pW; //Instantzvariable mit dem Namen PrintWriter
    BufferedReader bR; //Instanzvariable mit dem Namen BufferedReader
    String username; //deklariert eine neue Variable mit dem Namen username und dem Datentyp String
    String passwort; //deklariert eine neue Variable mit dem Namen passwort und dem Datentyp String

    /**
     * Ein Konstruktor mit dem gleichen Namen wie der Name der Klasse erstellt
     * Das ist ein parametrisierter Konstruktor und enthält zwei Parameter
     *
     * @param socket, gibt das Socket
     * @param dW, hier steht die Instanzvariable für die DataWrapper-Klasse
     */
    public ServerThread(DataWrapper dW, Socket socket) {
        this.dW = dW;
        this.socket = socket;
    }

    @Override
    public void run() { //Methode run(): der Thread ist jetzt in einem lauffähigen Zustand 

        try {
            boolean login = false; //login Variable auf false initialisieren
            boolean doWork = true; //doWork Varibale auf true initialisieren
            System.out.println(socket.toString()); //wandelt den Socket in einer String um
            pW = new PrintWriter(socket.getOutputStream()); //gibt den Socket in Bytes aus
            bR = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            while (doWork) {
                String m = bR.readLine(); //der Server kriegt eine Nachricht von den Clients
                String[] split = m.split(":"); // der Server speichert die Nachrichten der Clients in einer Array und teilt 
                //diese Nachrichten in Positionen in der Array ein
                System.out.println("Der Client schickt jetzt eine Nachricht: " + m);

                /**
                 * Die Clients möchten sich einloggen. Sie schicken zuerst eine
                 * Login-Anfrage. Der Server entscheidet, ob er diese Anfrage
                 * akzeptieren kann oder nicht. Falls der Server die Anfrage
                 * akzeptiert, schickt er login_successful an die Clients zurück
                 * Falls die Anfrage nicht erfolgreicht ist, sendet der Server
                 * login_unsuccessful an die Clients
                 */
                if (split[0].matches(".*login.*")) {
                    if (login == true) {
                        pW.println("already_logged_in");
                        pW.flush();
                    } else {
                        if (dW.isRegisteredUser(split[1], split[2]) == true) {
                            System.out.println("login test ausgabe: successful");
                            pW.println("login_successful");
                            pW.flush();
                            login = true;
                        } else {
                            login = false;
                            pW.println("login_unsuccessful");
                            pW.flush();
                        }
                    }

                } /**
                 * Die Clients wollen den Status des Hauses ändern und Sie
                 * schicken eine Update-Nachricht Diese Nachricht wird nittels
                 * der Methode readLine() gelesen Der Server entscheidet, ob er
                 * diese Anfrage annehmen kann oder nicht Im Fall der Annahme
                 * schickt er update_accepted Im Fall der Ablehnung sendet der
                 * Server update_denied
                 */
                else if (split[0].matches(".*Update.*")) {
                    if (login) {
                        pW.println("update_accepted");
                        pW.flush();

                        dW.updateState(bR.readLine());
                    } else {
                        pW.println("update_denied");
                        pW.flush();
                        doWork = false;
                    }
                } /**
                 * Die Clients wollen den aktuellen Status des Hauses wissen.
                 * Sie schiken eine Get-Anfrage. Falls diese Anfrage eine
                 * Zusagung bekommt, schickt der Server get_accepted zurück und
                 * danach werden Variablen für jedes Gerät des Hauses erstellt
                 * Diese Geräte haben verschiedene Werte: 1 ist für Einschalten,
                 * 0 ist für Ausschalten Die Geräte, die mehr als zwei Werte
                 * haben, haben einen Option mehr Alle diese Werte werden zu den
                 * Clients geschikt und im Bidschirm ausgegeben. Wenn die
                 * Get-Anfrage nicht erfolgreich ist, sendet der Server
                 * get_denied an die Clients zurück
                 */
                else if (m.matches(".*Get.*")) {
                    if (login) {
                        pW.println("get_accepted");
                        pW.flush();

                        String strled1;
                        String strled2;
                        String strled3;
                        String strled4;
                        String strled5;
                        String strposF1;
                        String strposF2;
                        String strposG;
                        String strposT;
                        String strhg;
                        String strka;
                        String strvnt;

                        if (dW.getLed1() == 1) {
                            strled1 = "led1:1";
                        } else {
                            strled1 = "led1:0";
                        }
                        if (dW.getLed2() == 1) {
                            strled2 = "led2:1";
                        } else {
                            strled2 = "led2:0";
                        }
                        if (dW.getLed3() == 1) {
                            strled3 = "led3:1";
                        } else {
                            strled3 = "led3:0";
                        }
                        if (dW.getLed4() == 1) {
                            strled4 = "led4:1";
                        } else {
                            strled4 = "led4:0";
                        }
                        if (dW.getLed5() == 1) {
                            strled5 = "led5:1";
                        } else {
                            strled5 = "led5:0";
                        }
                        if (dW.getPosF1() == 90) {
                            strposF1 = "posF1:90";
                        } else if (dW.getPosF1() == 180) {
                            strposF1 = "posF1:180";
                        } else {
                            strposF1 = "posF1:0";
                        }
                        if (dW.getPosF2() == 90) {
                            strposF2 = "posF2:90";
                        } else if (dW.getPosF2() == 180) {
                            strposF2 = "posF2:180";
                        } else {
                            strposF2 = "posF2:0";
                        }
                        if (dW.getPosG() == 180) {
                            strposG = "posG:180";
                        } else {
                            strposG = "posG:0";
                        }
                        if (dW.getPosT() == 90) {
                            strposT = "posT:90";
                        } else {
                            strposT = "posT:0";
                        }
                        if (dW.getHg() == 1) {
                            strhg = "hg:1";
                        } else {
                            strhg = "hg:0";
                        }
                        if (dW.getKa() == 1) {
                            strka = "ka:1";
                        } else {
                            strka = "ka:0";
                        }
                        if (dW.getVentilator() == 180) {
                            strvnt = "vnt:180";
                        } else if (dW.getVentilator() == 255) {
                            strvnt = "vnt:255";
                        } else {
                            strvnt = "vnt:0";
                        }
                        String r = strled1 + ":" + strled2 + ":" + strled3 + ":" + strled4 + ":" + strled5 + ":" + strposF1 + ":" + strposF2 + ":" + strposG + ":" + strposT + ":" + strhg + ":" + strka + ":" + strvnt;
                        System.out.println(r);
                        pW.println(r);
                        pW.flush();

                    } else {
                        pW.println("get_denied");
                        pW.flush();
                    }
                } /**
                 * Die Clients möchten am Ende abmelden. Um diesen Aktion
                 * erfolgreich zu machen, schicken sie "logout". Dieser Gesuch
                 * kann entweder in Ordnung oder nicht sein. Der Server stellt
                 * fest, ob er das annehmen kann oder nicht
                 */
                else if (split[0].matches(".*logout.*")) {
                    if (login) {
                        pW.println("logout_successful");
                        System.out.println("client wurde ausgeloggt");
                        login = false;
                    } else {
                        pW.println("logout_unsuccessful");
                    }
                    pW.flush();
                    doWork = false; //der Prozess ist beendet
                } /**
                 * Falls die Clients irgendwas hineinschreiben oder eine
                 * sinnlose Anfrage an dem Server machen, antwortet der Server
                 * mit einer invalid_request Antwort
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
            socket.close();

        } /**
         * Hier werden ein Input/Output Exception und ein SQL-Exception
         * abgefangen
         */
        catch (IOException ex) {
            Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
