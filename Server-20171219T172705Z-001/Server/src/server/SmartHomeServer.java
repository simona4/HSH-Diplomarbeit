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
 *
 * @author Simona
 */
public class SmartHomeServer extends Thread {

    DataWrapper dW;
    Socket socket1;

    PrintWriter pW;
    BufferedReader bR;
    String username;
    String passwort;

    /**
     *
     * @param socket1, gibt das Socket
     */
    SmartHomeServer(DataWrapper dW, Socket socket1) throws IOException {
        this.dW = dW;
        this.socket1 = socket1;
    }

    public void run() {
        
        try {
            System.out.println("hsh instance started");
            boolean login = false;
            boolean doWork = true;
            System.out.println(socket1.toString());
            pW = new PrintWriter(socket1.getOutputStream(), true);
            bR = new BufferedReader(new InputStreamReader(socket1.getInputStream()));

            while (doWork) {
                String m1 = bR.readLine();
                String[] split1 = m1.split(":");
                System.out.println("Das Haus schickt jetzt eine Nachricht: " + m1);

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
                } 
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
                    }
                    else {
                        pW.println("get_denied");
                        pW.flush();
                    }
                } else if (split1[0].matches(".*logout.*")) {
                    if (login) {
                        pW.println("logout_successful");
                        System.out.println("Der Client wurde erfolgreich ausgeloggt");
                        login = false;
                    } else {
                        pW.println("logout_unsuccessful");
                    }
                    pW.flush();
                    doWork = false;
                } else {
                    pW.println("invalid_request");
                    pW.flush();
                }

            } 
            pW.close();
            bR.close();
            socket1.close();

        } catch (IOException e) {
            System.out.println(e);
        } catch (InterruptedException ex) {
            Logger.getLogger(SmartHomeServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
