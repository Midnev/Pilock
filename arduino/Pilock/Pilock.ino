#include "LowPower.h"
#include <SPI.h>
#include "PN532_SPI.h"
#include "PN532.h"
#include "credentials.h"
//================================ nfc controll
PN532_SPI pn532spi(SPI, 10);
PN532 nfc(pn532spi);

#define HEADER_FING 1
#define HEADER_PWD 2
#define HEADER_INIT 3

int nfcMessageOrder = 0;
int nfcHeader = 0;//0 is null for header or hardware btn
uint8_t nfcData[4][32];

void readNFC(){
    uint8_t newNfcData[4][32];//reset each time
    nfcHeader = 0;// init each nfc set
    nfcMessageOrder = 0
  
  
         bool success;
        
          uint8_t responseLength = 32;
        
         //===================Waiting for an ISO14443A card=================
         // set shield to inListPassiveTarget
          success = nfc.inListPassiveTarget();
          if (success){//Found something!
            uint8_t selectApdu[] = {0x00,0xA4,0x04,0x00,0x07,    /* CLA *//* INS *//* P1  *//* P2  *//* Length of AID  */                                 
                                    0xF0, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06,  /* AID defined on Android App0xF0, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, */
                                    0x00 };/* Le  */
        
            uint8_t response[32];
            success = nfc.inDataExchange(selectApdu, sizeof(selectApdu), response, &responseLength);
            if (success){
              nfc.PrintHexChar(response, responseLength);//responseLength
        
              do {
                uint8_t apdu[] = "start";//return data to arduino
                uint8_t back[32]; // return data from android
                uint8_t length = 32; // return length
                success = nfc.inDataExchange(apdu, sizeof(apdu), back, &length);
                
                if (success){                
                  nfc.PrintHexChar(back, length);  
                  for(int i = 0;i<32;i++){ // insert into 
                     newNfcData[nfcMessageOrder][i] = back[i];//save msg
                  }
                  if(nfcMessageOrder<4)//limit message index to avoid over flow
                     nfcMessageOrder++;//next message;
                }else{}//connection broken?
              }while (success);
              if(success){
                nfcData = newNfcData;//move to refresh
              }
            }
            else {}//failed selecting AID
          }else{}//did not find anything

}

void identifyHeaderType(){
   //string compare to check header
    nfcHeader= 1;
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
    //relay activate;
  return 0;
}
int closeDoor(){
  
  return 0;
}
int checkDoor(){
  
  return 0;
}

//================================ keypad controll
int savedPwd[32];
int readBtn(){
   //reading 7 line btns
}
int confirmPwd(int pwd[]){
  //password compare alg
}

//================================ states as functions


void doStateIdle(){
  //check for kepad, nfc ...


  //wait for interrupt???
  while(1){
    goToSleep();//goto sleep when needed
  
    readNFC();

    //identify header
  // do header job
    switch(nfcHeader){
      case HEADER_FING:
          //
        break;
      case HEADER_PWD:
        //
        break;
      case HEADER_INIT:
         // need all data
        break;
      default:
  
        //do hardware pdn here no data compare
      
      }
     // other idle state
    
  }
  
   
  
  
}

void doStateNumIncome(){
  //when keyapd , check password
}
void doStateOpenIdle(){
  //check doorstate for closed
}
void doStateReset(){//init here?
  
}
void doStateOption1(){//init??
   //nfc here too
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
