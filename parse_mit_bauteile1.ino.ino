#include "WiFiEsp.h" //library von ESP8266
#include <Servo.h>
#include <RCSwitch.h>

// Emulate Serial1 on pins 6/7 if not present
#ifndef HAVE_HWSERIAL1
#include "SoftwareSerial.h"
SoftwareSerial Serial1(6, 7); // RX, TX
#endif

char ssid[] = "HTL-Schueler";            // your network SSID (name)
char pass[] = "1Schueler";        // your network password
int status = WL_IDLE_STATUS;     // the Wifi radio's status

char server[] = "192.168.27.15";  // server ip; 

// Initialize the Ethernet client object
WiFiEspClient client;

char message [255]; //globale Variable

int led1_pin = 22;    // Digital Pin für LED 1 auswählen
int led1;             // Wert wird von Server gelesen
int led2_pin = 23;    // Digital Pin für LED 2 auswählen
int led2;             // Wert wird von Server gelesen
int led3_pin = 24;    // Digital Pin für LED 3 auswählen
int led3;             // Wert wird von Server gelesen
int led4_pin = 25;    // Digital Pin für LED 4 auswählen
int led4;             // Wert wird von Server gelesen
int led5_pin = 26;    // Digital Pin für LED  5 auswählen
int led5;             // Wert wird von Server gelesen

int vnt_pin = 13;     // PMW Pin für Ventilator auswählen
int vnt;              // Wert wird von Server gelesen
        
int pw_pin = A0;      // Analog Pin für Photowiderstand auswählen
int pwStatus;         // Variable, um den Photowiderstand Wert hier zu speichern
int ledPW = 27;       // Pin für Photowiderstand LED auswählen 

int rs_pin = A1;      // Analog Pin für Regensensor auswählen
int rsStatus;         // Variable um den Regensensor Wert hier zu speichern
int buzzRS = 28;      // Pin für Buzzer auswählen 
        
Servo srvF1;          // Servo Fenster Objekt, um den Servo kontrollieren
int posF1;            // Wert wird von Server gelesen
Servo srvF2;          // Servo Fenster Objekt, um den Servo kontrollieren
int posF2;            // Wert wird von Server gelesen
Servo srvG;           // Servo Garage Objekt, um den Servo kontrollieren
int posG;             // Variable, um die Position von Garage Servo Motor speichern
Servo srvT;           // Servo Tür Objekt, um den Servo kontrollieren
int posT;             // Variable, um die Position von Tür Servo Motor speichern

RCSwitch mySwitch = RCSwitch(); // RC Switch Objekt machen
int hg;                         // Wert für Heizungerät wird von Server gelesen
int ka;                         // Wert für Klimaanlage wird von Server gelesen
        
int t1_pin = A2;     // Analog Input Pin für Temperatur Sensor auswählen
int t1Status;        // Variable, um den Wert von Temperatur Sensor zu speichen

int pir = 7;        // Digital Input Pin für PIR Sensor auswählen
int pirStatus;      // Variable um den PIR-Sensor Wert hier zu speichern 
int buzzPIR = 29;   // Pin für Buzzer auswählen

void nextLine(char term) {        // nextLine function
    int pos = 0;
    char c; 
    do {
      
      while(!client.available()) delay(1);
      
      c = client.read();
     // Serial.println(c);
      
      if(c == term) 
        message[pos] = '\0';
      
      else        message[pos] = c;
      
      pos++;
    } while(c!=term);
}
    void initializeLEDs() {
   // LED initialisierung für void setup: 
   
   pinMode(led1_pin,OUTPUT); // LED 1 wird als Output deklariert
   pinMode(led2_pin,OUTPUT); // LED 2 wird als Output deklariert
   pinMode(led3_pin,OUTPUT); // LED 3 wird als Output deklariert
   pinMode(led4_pin,OUTPUT); // LED 4 wird als Output deklariert
   pinMode(led5_pin,OUTPUT); // LED 5 wird als Output deklariert 
}

void initializeVNT (){
  // VNT initialisierung für void setup:
   
  pinMode(vnt_pin, OUTPUT); // Ventilator wird als Output deklariert
}

void initializePW(){
  // Photowiderstand initialisierung für void setup:
  
  pinMode(pw_pin, INPUT); // Photowiderstand Sensor als Input deklariert
  pinMode(ledPW, OUTPUT); // LED für Photowiderstand als Output deklariert
}

void initializeRS(){
  // Rainsensor initialisierung für void setup:
  
  pinMode(rs_pin, INPUT);   // Regensensor als Input deklariert
  pinMode(buzzRS, OUTPUT);  // Buzzer für Regensensor als Output deklariert
}

void initializeServo(){
  // Servo PIN initialisierung für void setup: 
  
  srvF1.attach(12); // Servo an pin 12 mit Servo objekt verbinden
  srvF2.attach(11); // Servo an pin 11 mit Servo objekt verbinden
  srvG.attach(10);  // Servo an pin 10 mit Servo objekt verbinden
  srvT.attach(9);   // Servo an pin 9 mit Servo objekt verbinden
  
}

void initializeRF(){
  // RF Sender PIN initialisierung für void setup:
  
  mySwitch.enableTransmit(31); // Transmitter ist mit Pin #31 verbundet
}

void initializeT(){
  // Temperatur initialisierun für void setup:
  
  pinMode(t1_pin, INPUT); // Temperatur Sensor wird als Input deklariert
}

void initializePIR(){
  //  Passive infrared sensor initialisierung für void setup:
  
  pinMode(pir, INPUT);       // PIR Sensor wird als Input deklariert
  pinMode(buzzPIR, OUTPUT);  // Buzzer für PIR-Sensor wird als Output deklariert
}



void setup(){
  // initialize serial for debugging
  Serial.begin(9600);
  initializeLEDs(); 
initializeVNT();
initializePW();
initializeServo();
initializeRS();
initializeRF();
initializeT();
initializePIR();

  Serial1.begin(115200);
  // initialize ESP module
  WiFi.init(&Serial1);

  // check for the presence of the shield
  if (WiFi.status() == WL_NO_SHIELD) {
    Serial.println("WiFi shield not present");
    // don't continue
    while (true);
  }

  // attempt to connect to WiFi network
  while ( status != WL_CONNECTED) {
    Serial.print("Attempting to connect to WPA SSID: ");
    Serial.println(ssid);
    // Connect to WPA/WPA2 network
    status = WiFi.begin(ssid, pass);
  }

  // you're connected now, so print out the data
  Serial.println("You're connected to the network");

  printWifiStatus();
   Serial.println();
  Serial.println("Starting connection to server...");
  // if you get a connection, report back via serial
  if (client.connect(server, 50004)) {
  Serial.println("Connected to server");
  
  }
  
  client.println("login:Client:1234"); //login with Username and Password
  client.flush();

  nextLine('\n');
  Serial.println(message);
}

void setLEDs(){
  /*
   * Auf diesem Void werden LEDs für den Haus gesteuert
   */
    if (led1 == 1){                 // Wenn Wert "1" kommt
      digitalWrite(led1_pin, HIGH); // LED einschalten
    }if (led1 == 0){                // Wenn Wert "0" kommt
      digitalWrite(led1_pin, LOW);  // LED ausschalten
    }
    if (led2 == 1){                // Wenn Wert "1" kommt
      digitalWrite(led2_pin, HIGH); // LED einschalten
    }if (led2 == 0){                // Wenn Wert "0" kommt
      digitalWrite(led2_pin, LOW);  // LED ausschalten
    }
    if (led3 == 1){                // Wenn Wert "1" kommt
      digitalWrite(led3_pin, HIGH); // LED einschalten
    }if (led3 == 0){                // Wenn Wert "0" kommt
      digitalWrite(led3_pin, LOW);  // LED ausschalten
    }
    if (led4 == 1){                // Wenn Wert "1" kommt
      digitalWrite(led4_pin, HIGH); // LED einschalten
    }if (led4 == 0){                // Wenn Wert "0" kommt
      digitalWrite(led4_pin, LOW);  // LED ausschalten
    }
    if (led5 == 1){                // Wenn Wert "1" kommt
      digitalWrite(led5_pin, HIGH); // LED einschalten
    }if (led5 == 0){                // Wenn Wert "0" kommt
      digitalWrite(led5_pin, LOW);  // LED ausschalten
    }
}

void setVNT(){
  /*
   * Hier steht die Code, um den Ventilator zu steuern
   */
   analogWrite(vnt_pin, vnt);       // Hier wird die Geschwindigkeit der Ventilator mit PWM gesteuert 
}


void sensorPW(){
  /*
   * Code für Photowiderstand 
   */
    pwStatus = analogRead(pw_pin);     // Photowiderstand Wert  von 0 bis 1023 lesen und es im "pwStatus" speichern
    delay(250);                        // Stopt den Programm für 250 milisekunden
    if(pwStatus >= 400){               // Wenn Photowiderstand Wert größer als 400 ist, es ist Nacht
       digitalWrite(ledPW, HIGH);      // Dann LED einschalten
       Serial.print("Es ist Nacht: "); // Nachricht im Serial Monitor anzeigen
       Serial.println(pwStatus);       // Photowiderstand Wert im Serial Monitor anzeigen
    }else{                             // Wenn Photowiderstand Wert kleiner als 400 ist, es ist Hell
       digitalWrite(ledPW, LOW);       // LED ausschalten
       Serial.print("Es ist Hell: " ); // Nachricht im Serial Monitor anzeigen
       Serial.println(pwStatus);       // Photowiderstand Wert im Serial Monitor anzeige
    }
}

void sensorRS(){
  /*
   * Code für Regensensor
   */
    rsStatus = analogRead(rs_pin);        // Regensensor Wer von 0 bis 1023 lesen und es im "rsStatus" speichern
    delay(250);                           // Stopt den Prgramm für 250 milisekunden
    if(rsStatus < 450){                   // Wenn Regensensor Wert kleiner als 450 ist, es regnet
      digitalWrite(buzzRS, HIGH);         // Dann wird der Buzzer eingeschaltet
      Serial.print("Es Regnet: ");        // Nachricht im Serial Monitor anzeigen
      Serial.println(rsStatus);           // Regensensor Wert auch im Serial Monitor zeigen
    }else{                                // Wenn Regensensor Wert größer als 450 ist, es regenet
      digitalWrite(buzzRS, LOW);          // Dann wird der Buzzer ausgeschlatet 
      Serial.print("Es regnet nicht: ");  // Nachricht im Serial Monitor anzeigen
      Serial.println(rsStatus);           // Regensensor Wert im Serial Monitor anzeigen
    }
}

// Servo steuern Code 
void setServo(){
    srvF1.write(posF1);          // Position von Rollo1 wird mit "posF1" Grad verändert
    srvF2.write(posF2);          // Position von Rollo2 wird mit "posF2" Grad verändert
    srvG.write(posG);            // Position von Garage wird mit "posG" Grad verändert
    srvT.write(posT);            // Position von Tür wird mit "posT" Grad verändert
}

// RF Sender für Klimaanlage und Heizunggerät
void setRF(){
    if  (ka == 1){                          // Wenn im "ka" wert "1" kommt
      mySwitch.send(16762196);              // Der RF Sender übertragt diesem Wert
      Serial.println("Steckdose A ist ON");   // Nachricht im Serial Monitor zeigen
    }if (ka == 0){                          // Wenn "ka" wert "0" hat
      mySwitch.send(16762193);              // Übertragt der RF Sender diesem Wert
      Serial.println("Steckdose A ist OFf");  // Steckdose ist ausgeschaltet und zeigt den Nachricht im Bildschirm
    }
    delay(200);
    if (hg == 1){                          // Wenn im "hg" wert "1" kommt
      mySwitch.send(1283748);               // Der RF Sender übertragt diesem Wert
      Serial.println("Steckdose B ist ON");   // Nachricht im Serial Monitor zeigen
    }if (hg == 0){                          // Wenn "hg" wert "0" hat
      mySwitch.send(123948);                // Übertragt der RF Sender diesem Wert
      Serial.println("Steckdose B ist OFf");  // Steckdose B ist ausgeschaltet und zeigt den Nachricht im Bildschirm
    }
}

// Temepratur Sensor Code 
void sensorT(){
   t1Status = analogRead(t1_pin);                    // Temperatur Sensor Wert wird gelesen
    float t1Millivolts = (t1Status / 1024.0) * 5000; // Statische Formel von Internet
    float t1Celsius = t1Millivolts / 10;             // Sensor output is 10mV für degree Celsius
    Serial.print(t1Celsius);                         // Temperatur im Serial Monitor anzeigen
    Serial.println(" degree Celsius");               
  //  delay(1000);                                     // wait for one second
}


void loop()
{
  
  Serial.println("beginning of loop");
  client.println("Get"); //get_state in Server senden
  client.flush();

  nextLine('\n');
  Serial.println(message);    //get_state_accepted receive
  delay(100);
  
  nextLine('\n');
  Serial.println(atoi(message));
  led1 = atoi(message);    //Status of Led1   received
     
  nextLine('\n');
  Serial.println(atoi(message));
  led2 = atoi(message);

  nextLine('\n');
  Serial.println(atoi(message));
  led3 = atoi(message);

  nextLine('\n');
  Serial.println(atoi(message));
  led4 = atoi(message);

  nextLine('\n');
  Serial.println(atoi(message));
  led5 = atoi(message);


  nextLine('\n');
  Serial.println(atoi(message));
  posF1 = atoi(message);

  nextLine('\n');
  Serial.println(atoi(message));
  posF2 = atoi(message);

   nextLine('\n');
  Serial.println(atoi(message));
  posG = atoi(message);

   nextLine('\n');
  Serial.println(atoi(message));
  posT = atoi(message);

   nextLine('\n');
  Serial.println(atoi(message));
  hg = atoi(message);

   nextLine('\n');
  Serial.println(atoi(message));
  ka = atoi(message);

    nextLine('\n');
  Serial.println(atoi(message));
  vnt = atoi(message);
  
  // if there are incoming bytes available
  // from the server, read them and print them
   
  Serial.println("before set");
      setLEDs();
  Serial.println("afterleds");
      
      setVNT();
  Serial.println("aftervnt");
      setServo();
  Serial.println("afterservo");
      setRF();

  Serial.println("afterrf");
      sensorPW();
  Serial.println("afterpw");
      sensorRS();
  Serial.println("afterrs");
      sensorT();  

  Serial.println("after set");
  // if the server's disconnected, stop the client
  if (!client.connected()) {
    Serial.println();
    Serial.println("Disconnecting from server...");
    client.stop();

 
  }
  Serial.println("end of loop");
}

void printWifiStatus()
{
  // print the SSID of the network you're attached to
  Serial.print("SSID: ");
  Serial.println(WiFi.SSID());

  // print your WiFi shield's IP address
  IPAddress ip = WiFi.localIP();
  Serial.print("IP Address: ");
  Serial.println(ip);

  // print the received signal strength
  long rssi = WiFi.RSSI();
  Serial.print("Signal strength (RSSI):");
  Serial.print(rssi);
  Serial.println(" dBm");
}
