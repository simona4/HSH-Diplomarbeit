/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import java.sql.*;
import org.mindrot.jbcrypt.BCrypt;


/*

in der datawrapper klasse sind eben alle variablen .z.b licht und setter/getter methoden
dazu brauchst du auch hilfsmethoden. 
z.b. 
- eine boolean isRegisteredUser(String username, String password), in der du 
true zur�ckgibst, wenn der benutzer in der Datenbank ist, und false wenn nicht.
da drinnen machst du dann eine statement variable und ein select...
- eine methode, die alle werte von den ger�ten in die datenbank schreibt. 
- etc etc

damit du an der richtigen stelle in den anderen klassen nur mehr die methode isRegisteredUser (etc) aufrufen musst und true/false bekommst.

*/


/**
 *
 * @author Simona
 */
public class DataWrapper {
    int led1;
    int led2;
    int led3;
    int led4;
    int led5; 
    int posF1;
    int posF2;
    int posG;
    int posT;
    int hg;
    int ka;
    int vnt;
    
    Connection con;
    Statement stmt;
    ResultSet rs;
  
    
    public DataWrapper() {
    try{  
        Class.forName("com.mysql.jdbc.Driver");  
        con = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/hsh","root","");  
        //hier HSH ist die Datenbank und root ist Username
        
        System.out.println("Mit der Datenbank verbunden...");
    }
    catch(Exception e){ System.out.println(e);
    }
    
    initializeTestConfig();
}
 
    public void initializeTestConfig() {
        led1=0;
        led2=0;
        led3=1;
        led4=1;
        led5=0;
        posF1=1;
        posF2=0;
        posG=1;
        posT=0;
        hg=1;
        ka=0;
        vnt=1;
    }
    
    
    /**
     * 
     * @param led1, das Lichh1 einschalten oder ausschalten 
     * @return den Wert von dem Licht (on/off)
     */
    public synchronized int setLed1(int led1){
     return this.led1 = led1;
   }
    
     /**
     * 
     * @param led1, nimmt das Wert von dem Licht
     * @return den Wert von dem Licht
     */ 
   public synchronized int getLed1(){
     return led1;
   }
   
    /**
     * 
     * @param led2, das Licht2 einschalten oder ausschalten 
     * @return den Wert von dem Licht (on/off)
     */
    public synchronized int setLed2(int led2){
     return this.led2 = led2;
   }
    
     /**
     * 
     * @param led2, nimmt das Wert von dem Licht
     * @return den Wert von dem Licht
     */ 
   public synchronized int getLed2(){
     return led2;
   }
   
    /**
     * 
     * @param led3, das Licht3 einschalten oder ausschalten 
     * @return den Wert von dem Licht (on/off)
     */
    public synchronized int setLed3(int led3){
     return this.led3 = led3;
   }
    
     /**
     * 
     * @param led3, nimmt das Wert von dem Licht
     * @return den Wert von dem Licht
     */ 
   public synchronized int getLed3(){
     return led3;
   }
   
    /**
     * 
     * @param led4, das Licht4 einschalten oder ausschalten 
     * @return den Wert von dem Licht (on/off)
     */
    public synchronized int setLed4(int led4){
     return this.led4 = led4;
   }
    
     /**
     * 
     * @param led4, nimmt das Wert von dem Licht
     * @return den Wert von dem Licht
     */ 
   public synchronized int getLed4(){
     return led4;
   }
   
    /**
     * 
     * @param led5, das Licht5 einschalten oder ausschalten 
     * @return den Wert von dem Licht (on/off)
     */
    public synchronized int setLed5(int led5){
     return this.led5 = led5;
   }
    
     /**
     * 
     * @param led5, nimmt das Wert von dem Licht
     * @return den Wert von dem Licht
     */ 
   public synchronized int getLed5(){
     return led5;
   }
   
   /**
    * 
    * @param posF1, das Fensterrollo1 oeffnen oder schliessen
    * @return den Wert von dem Fensterrollo 
    */
   public synchronized int setPosF1(int posF1){
     return this.posF1 = posF1;
   }
   
     /**
      * 
      * @param posF1, nimmt den Wert von dem Fensterrollo
      * @return das Wert von dem Fensterrollo
      */
   public synchronized int getPosF1(){
        return posF1;
    }
   
   /**
    * 
    * @param posF2, das Fensterrollo2 oeffnen oder schliessen
    * @return den Wert von dem Fensterrollo 
    */
   public synchronized int setPosF2(int posF2){
     return this.posF2 = posF2;
   }
   
     /**
      * 
      * @param posF2, nimmt den Wert von dem Fensterrollo
      * @return das Wert von dem Fensterrollo
      */
   public synchronized int getPosF2(){
        return posF2;
    }
   
   /**
    * 
    * @param posG, den Garagentor oeffnen oder schliessen
    * @return den Wert von dem Garagentor 
    */
   public synchronized int setPosG(int posG){
     return this.posG = posG;
   }
   
     /**
      * 
      * @param posG, nimmt den Wert von dem Garagentor
      * @return das Wert von dem Garagentor
      */
   public synchronized int getPosG(){
        return posG;
    }
   
   /**
    * 
    * @param posT, den Tuer oeffnen oder schliessen
    * @return den Wert von dem Tuer
    */
   public synchronized int setPosT(int posT){
     return this.posT = posT;
   }
   
     /**
      * 
      * @param posT, nimmt den Wert von dem Tuer
      * @return das Wert von dem Tuer
      */
   public synchronized int getPosT(){
        return posT;
    }

   /**
    * 
    * @param hg, das Heizungsgeraet einschalten oder ausschalten
    * @return den Wert von dem Heizungsgeraet
    */
   public synchronized int setHg(int hg){
       return this.hg = hg;
   }
   
   /**
    * 
    * @param hg, nimmt den Wert von dem Heizungsgeraet
    * @return den Wert von dem Heizungsgeraet
    */
   
   public synchronized int getHg(){
       return hg;
   }
   
   /**
    * 
    * @param ka, die Klimaanlage einschalten
    * @return den Wert von der Klimaanlage 
    */
   public synchronized int setKa(int ka){
     return this.ka = ka;
   }
   
     /**
      * 
      * @param ka, nimmt den Wert von der Klimaanlage
      * @return das Wert von der Klimaanlage
      */
   public synchronized int getKa(){
        return ka;
    }
   
   /**
    * 
    * @param vnt, den Ventilator einschalten oder ausschalten
    * @return den Wert von dem Ventilator (on/off)
    */
    public synchronized int setVentilator(int vnt){
     return this.vnt = vnt;
   }
   
   /**
    * @param vnt, nimmt den Wert von dem Ventilator
    * @return den Wert von dem Ventilator
    */
   public synchronized int getVentilator(){
       return vnt;
   }
   
   public String hashPassword(String password){
       return BCrypt.hashpw(password, BCrypt.gensalt());
   }
   
   public void checkPass(String password, String hashPassword){
       if(BCrypt.checkpw(password, hashPassword)){
           System.out.println("Die Passwörter sind gleich. HOORAY!");
       }
       else {
           System.out.println("GEFÄHRLICH!");
       }
   }
   
   public synchronized boolean isRegisteredUser(String name, String password) throws SQLException {
      try {
       System.out.println("Username: " + name);
       System.out.println("Password: " + password);
       String sql = "SELECT id_user FROM user WHERE username = '"+ name + "' and passwort = '"+ password +"';";
       Statement stmt = (Statement) con.createStatement();
       ResultSet rs = stmt.executeQuery(sql);
       
       if (rs.next()) {
           System.out.println("Login erfolgreich");
           return true;
       }
       else {
           System.out.println("Error");
           return false; 
       }
      }
      catch (NullPointerException e) {
          System.out.println("Exception thrown");
          return false; 
      }
   }
   
   public boolean updateState(String m) throws SQLException {
       System.out.println(m);
       String[] s = m.split(":");
       
       int led1 = Integer.parseInt(s[1]);
       int led2 = Integer.parseInt(s[3]);
       int led3 = Integer.parseInt(s[5]);
       int led4 = Integer.parseInt(s[7]);
       int led5 = Integer.parseInt(s[9]);
       int posF1 = Integer.parseInt(s[11]);
       int posF2 = Integer.parseInt(s[13]);
       int posG = Integer.parseInt(s[15]);
       int posT = Integer.parseInt(s[17]);
       int hg = Integer.parseInt(s[19]);
       int ka = Integer.parseInt(s[21]);
       int vnt = Integer.parseInt(s[23]);
       
       setLed1(led1);
       setLed2(led2);
       setLed3(led3);
       setLed4(led4);
       setLed5(led5);
       setPosF1(posF1);
       setPosF2(posF2);
       setPosG(posG);
       setPosT(posT);
       setHg(hg);
       setKa(ka);
       setVentilator(vnt);
    try {
        String sql = "INSERT INTO geraete (Geraetenname, Status) " + "VALUES(?, ?)";
        stmt = (Statement) con.createStatement();
        int result = stmt.executeUpdate(sql);
    }  
    catch (NullPointerException e) {
        System.out.println("Error");
        return false; 
    }

       return true;
   }
   
   public boolean getState(String m) throws SQLException {
       String[] s1 = m.split(":");
       stmt = (Statement) con.createStatement();
       stmt.executeQuery("SELECT * FROM geraete ");
       return true;
   }
   
   public boolean logoutUser(String a) throws SQLException {
       stmt = (Statement) con.createStatement();
       stmt.executeQuery("SELECT username, password FROM user");
       return true;
   }
}