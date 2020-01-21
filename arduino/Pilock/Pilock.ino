#include "LowPower.h"
#include <SPI.h>
#include "PN532_SPI.h"
#include "PN532.h"
#include "credentials.h"
//================================ nfc controll
PN532_SPI pn532spi(SPI, 10);
PN532 nfc(pn532spi);

int communicateNfc(){
  
return 0;
}

//================================ power save
const int wakeUpPin = 3;
void wakeUp(){}

void goToSleep(){
  attachInterrupt(0, wakeUp, LOW);
  LowPower.powerDown(SLEEP_FOREVER, ADC_OFF, BOD_OFF); 
  detachInterrupt(0); 
}

//================================ state machine
#define STATE_IDLE 0
#define STATE_NUMINCOME 1
#define STATE_OPENIDLE 2
#define STATE_RESET 3
#define STATE_OPTION1 4
#define STATE_OPTION2 5
int machineState = 0;
void setState(int state){
  machineState = state;
}

//================================ wifi data
String WIFI_NAME = "";//need to change here
String WIFI_PASSWORD = "";//need to change here
String SERVER_ADDR = "http://000.000.000.000";
String SERVER_GETURL = "";//try not to use
String SERVER_POSTURL = "";

void setWifiData(String wifi, String password){
  WIFI_NAME = wifi;
  WIFI_PASSWORD = password;
}

void setServerData(String addr, String path){
   SERVER_ADDR = addr;
   SERVER_POSTURL = path;
}

int connectWifi(){

return 0;//1 if success
}

int sendREST(){

 return 0;//1 if success
}
//================================ door controll
int openDoor(){

  return 0;
}
int closeDoor(){
  
  return 0;
}
int checkDoor(){
  
  return 0;
}

//================================ keypad controll

int readBtn(){
  
}
int confirmPwd(int pwd[]){
 
}

//================================ states as functions
void doStateIdle(){
  //check for kepad, nfc ...
  goToSleep();//goto sleep when needed
  //nfc
  while(1){
          bool success;
        
          uint8_t responseLength = 32;
        
          Serial.println("Waiting for an ISO14443A card");
          // set shield to inListPassiveTarget
          success = nfc.inListPassiveTarget();
          if (success){
            Serial.println("Found something!");
            uint8_t selectApdu[] = {0x00,                                     /* CLA */
                                    0xA4,                                     /* INS */
                                    0x04,                                     /* P1  */
                                    0x00,                                     /* P2  */
                                    0x07,                                     /* Length of AID  */
                                    0xF0, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, /* AID defined on Android App0xF0, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, */
                                    0x00 /* Le  */};
        
            uint8_t response[32];
            success = nfc.inDataExchange(selectApdu, sizeof(selectApdu), response, &responseLength);
            if (success){
        
              Serial.print("responseLength: ");
              Serial.println(responseLength);
        
              nfc.PrintHexChar(response, responseLength);
        
              do {
                uint8_t apdu[] = "Hello from Arduino";
                uint8_t back[32];
                uint8_t length = 32;
        
                success = nfc.inDataExchange(apdu, sizeof(apdu), back, &length);
        
                if (success){
                  Serial.print("responseLength: ");
                  Serial.println(length);
                  nfc.PrintHexChar(back, length);
                }else{
                  Serial.println("Broken connection?");
                }
              } while (success);
            }
            else {
              Serial.println("Failed sending SELECT AID");
            }
          }else{
            Serial.println("Didn't find anything!");
          }
          delay(1000);

  }
  
}
void doStateNumIncome(){
  //when keyapd , check password
}
void doStateOpenIdle(){
  //check doorstate for closed
}
void doStateReset(){
  //
}
void doStateOption1(){
  
}
void doStateOption2(){
  
}
//================================ main setup and loop
void setup() {
  machineState = STATE_IDLE;
  pinMode(wakeUpPin, INPUT);  

  uint32_t versiondata = nfc.getFirmwareVersion();
  if (!versiondata){
    Serial.print("Didn't find PN53x board");
    while (1); // halt
  }
  // Got ok data, print it out!
  Serial.print("Found chip PN5");
  Serial.println((versiondata >> 24) & 0xFF, HEX);
  Serial.print("Firmware ver. ");
  Serial.print((versiondata >> 16) & 0xFF, DEC);
  Serial.print('.');
  Serial.println((versiondata >> 8) & 0xFF, DEC);

  nfc.SAMConfig();
}

void loop() {
  switch(machineState){
    case STATE_IDLE:
        doStateIdle();
      break;
    case STATE_NUMINCOME:
        doStateNumIncome();
      break;
    case STATE_OPENIDLE:
       doStateOpenIdle();
      break;
    case STATE_RESET:
        doStateReset();
      break;
    case STATE_OPTION1:
        doStateOption1();
      break;
    case STATE_OPTION2:
        doStateOption2();
      break;
    //default: 
      //state Error need beeping
      //then turn to idle
  }
}
