#include <string.h>
#include "LowPower.h"
#include <SPI.h>
#include "PN532_SPI.h"
#include "PN532.h"
#include "credentials.h"
#include <SoftwareSerial.h>
#include "WiFiEsp.h"


#define PHOTO2 2
#define PHOTO1 3
#define WIFI_TX_PIN 4//46
#define WIFI_RX_PIN 5//57
#define SCL_PIN 6//64
#define SDO_PIN 7//75
#define RELAY 8  
#define RELAY2 9  

//================================ gotosleep
static unsigned long last_activation_time = 0;
void doorsleep(){
  attachInterrupt(0, wakeInterr, LOW);
  attachInterrupt(1, photoInterr,FALLING);//pin 3 interrupt
  unsigned long now_time = millis();
  if(now_time-last_activation_time>30000){
    
    Serial.println("  ");
    Serial.println("Sleep Start");
    delay(1000);
    LowPower.powerDown(SLEEP_FOREVER, ADC_OFF, BOD_OFF);
  }else{
    Serial.println("not sleep yet");
  }
   detachInterrupt(0);
   detachInterrupt(1);
}

void countActivation(){
    last_activation_time   = millis();
}

static unsigned long last_door_interrupt_time = 0;
void wakeInterr(){
  unsigned long interrupt_time = millis();//debounce
  if (interrupt_time - last_door_interrupt_time > 1000){
     last_activation_time   = millis();
    last_door_interrupt_time = interrupt_time;
    Serial.println("Interr End");
  }
    
}
void photoInterr(){
  unsigned long interrupt_time = millis();//debounce
  if (interrupt_time - last_door_interrupt_time > 1000){
     last_door_interrupt_time = interrupt_time;
    Serial.println("Interr End");
  }
    
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
  countActivation();
}
int checkState(int state){
    return machineState == state;
}
//================================ wifi data
String DEVICEID = "drlk0001";//const?
char ssid[] = "AndroidHotspot8D_91_42";            // your network SSID (name)
char pass[] = "wazxde135";        // your network password
const char* server = "35.208.214.3";
const String open_url = "/drlk0001?msg=Door%20was%20Opened";
const String close_url = "/drlk0001?msg=Door%20was%20Closed";
const String error_url = "/drlk0001?msg=Door%20was%20Not%20Closed";

int wifiStatus = WL_IDLE_STATUS;
SoftwareSerial WifiSerial(WIFI_RX_PIN, WIFI_TX_PIN); // RX, TX
WiFiEspClient client;
/*void setWifiData(String wifi, String password){
  WIFI_NAME = wifi;
  WIFI_PASSWORD = password;
}*/
void printWifiStatus(){
  Serial.print("SSID: ");
  Serial.println(WiFi.SSID());
  IPAddress ip = WiFi.localIP();
  Serial.print("IP Address: ");
  Serial.println(ip);
  long rssi = WiFi.RSSI();
  Serial.print("Signal strength (RSSI):");
  Serial.print(rssi);
  Serial.println(" dBm");

}

int connectToWifi(){
  
  WiFi.init(&WifiSerial);
  if (WiFi.status() == WL_NO_SHIELD) {
    Serial.println("WiFi shield not present");
    return 0;
  }
  int wtry =0;
  while ( wifiStatus != WL_CONNECTED) {
    Serial.print("Attempting to connect to WPA SSID: ");
    Serial.println(ssid);
    wifiStatus = WiFi.begin(ssid, pass);
    if(wtry >1)
      break;
    else
      wtry++;
  }
  
  Serial.println("You're connected to the network");
  printWifiStatus();
  if(WiFi.status() == WL_CONNECTED){
    return 1;
  }
  return 0;
}

void postRequest(const String urlType){

  digitalWrite(RELAY2, HIGH);
 // delay(1000);
  
  connectToWifi();
  
  
  if (client.connect(server, 8080)){
    
    Serial.println("Connected to server"); 
    client.print("POST ");
    client.print(urlType); 
    client.print(" HTTP/1.1\r\n"); 
    client.print("Host: "); 
    client.print(server); 
    client.print("\r\n"); 
    client.print("Connection: close\r\n\r\n");
    //client.close();
    client.stop();
  }

  
  wifiStatus=WL_IDLE_STATUS;
  WiFi.disconnect();
  //WifiSerial.end();
  digitalWrite(RELAY2, LOW);
}



//================================ door controll
int checkDoor(){
      //delay(500);
      int val = digitalRead(3); // 
      if (val == OUTPUT){ // 초기 센서 값 OFF 으로 설정
        return 1;//Door Locked
      }
  return 0; //Door opened
}
int openDoor(){//need count?
  int tries = 0;
  while(checkDoor()){
    digitalWrite(RELAY, HIGH);
    delay(100);
    Serial.print("open\n");
    digitalWrite(RELAY, LOW);
    //delay(1000);
    //notify
    //
    if(tries>3){
      postRequest(error_url);
      return 0;
    }else{
      tries++;
      delay(5000);
    }
  }
  
  postRequest(open_url);
  return 1;
}
int closeDoor(){
  int tries = 0;
  while(!checkDoor()){
    digitalWrite(RELAY, HIGH);
    delay(100);
    Serial.print("close\n");
    digitalWrite(RELAY, LOW);
    if(tries>3){
      postRequest(error_url);
      return 0;
    }else{
      tries++;
      delay(3000);
    }
  }
  postRequest(close_url);
  return 1;
}

//bool manul_close = false;




//================================ nfc controll
PN532_SPI pn532spi(SPI, 10);
PN532 nfc(pn532spi);

#define HEADER_FING 1
#define HEADER_PWD 2
#define HEADER_INIT 3

int nfcMessageOrder = 0;
int nfcHeader = 0;//0 is null for header or hardware btn
char nfcData[4][32];
char androidId[32]="12292c43045aef13";

void setAndroidId(const char *data){
  for(int i=0;i<32;i++){
      androidId[i]=data[i];
  }
}
int checkAndroidId(const char *data){
  for(int i=0;i<32;i++){
      if(androidId[i]!=data[i])
        return 0;
  }
  return 1;
}
int checkEmpty(const char *data){
  if(data[0]=='f'){
    return 1;  
  }
  else 
    return 0;
}
void initNfcData(){
  for(int c=0;c<4;c++){
    for(int r=0;r<32;r++)
      nfcData[c][r]=0;
  }  
}

void seeNfcData(){
  for(int c=0;c<4;c++){
    for(int r=0;r<32;r++)
      Serial.print(nfcData[c][r]);
      Serial.println("  ");
  }  
  Serial.println("");
}

bool checkHeader(const char *data,const char *data2){

    for(int i=0;i<4;i++){
      if(data[i]==data2[i]){}
      else{ return 0; }
    }
  return 1;
}

void readNFC(){
    //char newNfcData[4][32];//reset each time
    //nfcHeader = 0;// init each nfc,,, no need to wirte each time 
    nfcMessageOrder = 0;

    bool success;
    uint8_t responseLength = 32;
   //===================Waiting for an ISO14443A card=================
   // set shield to inListPassiveTarget
   //Serial.println("exechange str");
    success = nfc.inListPassiveTarget();
    //Serial.println("exechange end");
    if (success){//Found something!
      uint8_t selectApdu[] = {0x00,0xA4,0x04,0x00,0x07,    /* CLA *//* INS *//* P1  *//* P2  *//* Length of AID  */                                 
                              0xF0, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06,  /* AID defined on Android App0xF0, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, */
                              0x00 };/* Le  */
  
      uint8_t response[32];
      success = nfc.inDataExchange(selectApdu, sizeof(selectApdu), response, &responseLength);//header here
      if (success){
        nfc.PrintHexChar(response, responseLength);//responseLength//--------------------------------------Log
            if(checkHeader("init", response )){//string compare to check header
              nfcHeader=HEADER_INIT; 
            }else if(checkHeader("fing", response )){
              nfcHeader=HEADER_FING;
            }else if(checkHeader("pswd", response )){
              nfcHeader=HEADER_PWD;
            }else{} 
        do {
          uint8_t apdu[] = "start";//return data to arduino
          uint8_t back[32]; // return data from android
          uint8_t length = 32; // return length
          success = nfc.inDataExchange(apdu, sizeof(apdu), back, &length);
          
          if (success){                
           nfc.PrintHexChar(back, length);//--------------------------------------Log
            if(nfcMessageOrder<4){//limit message index to avoid over flow
              
              for(int r=0;r<length;r++){
                  if( back[r] > 0x1f){
                     nfcData[nfcMessageOrder][r] = ((char)back[r]);//save msg out side of loop
                  }else{
                    nfcData[nfcMessageOrder][r]= 0x20;
                  }
              }
              nfcMessageOrder++;//next message;
            }else if(nfcMessageOrder >=4){
              break;
            }
            //=================if(strcmp())
            
          }else{}//connection broken?
        }while (success);
      }
      else {}//failed selecting AID
    }else{}//did not find anything

}


//================================ keypad & pwd controll


int prevKey =0;
int readKeypad(void){
  byte Count;
  byte Key_State = 0;
  for(Count = 1; Count <= 16; Count++){
    digitalWrite(SCL_PIN, LOW); 
    if (!digitalRead(SDO_PIN))
      Key_State = Count; 
    digitalWrite(SCL_PIN, HIGH);
  }
  
  if (Key_State>12){
    return 0;
  }
  return Key_State; 
}

int readKeyIdle(){
  int key = readKeypad();
  prevKey = key;
  return key;
}

int getKey(){
  int key = readKeypad();
  //while( readKeypad() ){//delay(30);
   //}
  if(prevKey == key){
    return 0;
  }
  prevKey = key;
  
//Serial.print("getKey : ");
//Serial.println(key);
  return key;
}


int pwdIndex =0;
int savedPwd[16]= {1,2,3,4,
                  0,0,0,0,
                  0,0,0,0,
                  0,0,0,0};
int checkPwd[16];

void setPwd(const int *data){
  for(int i =0; i<16;i++){
    savedPwd[i] = 0;
    savedPwd[i] = data[i];
  }
}
void resetCPwd(){
  pwdIndex =0;
  for(int i =0; i<16;i++){
    checkPwd[i] = 0;
  }
}
int confirmPwdNFC(const char *data){
  int temp =0;
  for(int i=0;i<16;i++){
      switch(data[i]){
      case '1':
        temp =1;
        break;
      case '2':
        temp =2;
        break;
      case '3':
        temp =3;
        break;
      case '4':
        temp =4;
        break;
      case '5':
        temp =5;
        break;
      case '6':
        temp =6;
        break;
      case '7':
        temp =7;
        break;
      case '8':
        temp =8;
        break;
      case '9':
        temp =9;
        break;
      case '0':
        temp =10;
        break;   
      default:
        temp=0;
        break;
    }
      if(savedPwd[i] == temp){
        Serial.print(temp);
      }else{ 
        Serial.print("failed : ");
        Serial.println(temp);
        Serial.println(savedPwd[i]);
        resetCPwd();
        return 0; 
      }
  }
  Serial.println("");
  return 1;
}
int confirmPwd(){
  Serial.println("Checking");
  for(int i=0;i<16;i++){
      if(savedPwd[i] == checkPwd[i]){
        Serial.print(checkPwd[i]);
      }else{ 
        Serial.print("failed : ");
        Serial.print(savedPwd[i]);
        Serial.println(checkPwd[i]);
        resetCPwd();
        return 0; 
      }
  }
  Serial.println("");
  resetCPwd();
  return 1;
}
int insertPwd(){
  int key = getKey();
  
  if(key>0){
    Serial.println(key);
    if(key == 11 && confirmPwd()){//check
      return 2;
    }else if(key == 12){//cancel
      return 0;
    }else{
      checkPwd[pwdIndex] = key;
      pwdIndex++;
    }
  }
  return 1;
}


//================================ states as functions


void doStateIdle(){ //check for kepad, nfc ...
  //wait for interrupt???
  while(checkState(STATE_IDLE)){
    doorsleep();
    
    readNFC();
     if(readKeyIdle()){
        setState(STATE_NUMINCOME);
      }
  // do header job
    if(!checkDoor()){
      setState(STATE_OPENIDLE);
    }
    switch(nfcHeader){
      case HEADER_FING:
          if( checkAndroidId(nfcData[0]) ){
            Serial.print("recived Data: ");
            Serial.println(nfcData[0]);
            
            setState(STATE_OPENIDLE);
          }
        break;
      case HEADER_PWD:
        if(confirmPwdNFC(nfcData[0])
            && checkAndroidId(nfcData[1]) ){
          setState(STATE_OPENIDLE);
          
        }
        /*Serial.println(nfcData[0]);
        Serial.println(nfcData[1]);
        Serial.println(nfcData[2]);*/
        break;
      //default:
        //do hardware pdn here no data compare
      }
    if(nfcHeader!=0){
      nfcHeader=0;
    }
  }

 

}

void doStateNumIncome(){
  while(checkState(STATE_NUMINCOME)){

    int res = insertPwd();
     if(res==2)
      setState(STATE_OPENIDLE);
     else if(res){}
     else
       setState(STATE_IDLE);
     
    /*switch(insertPwd()){
      case 0:
        setState(STATE_IDLE);
        break;
      case 2:
        setState(STATE_OPENIDLE);
        break;  
    }*/
  }
  //readKeypad
  //when keyapd , check password
}

static unsigned long open_time = 0;
void doStateOpenIdle(){
  //check doorstate for closed
  Serial.println("door opened");
  openDoor();
  open_time = millis();
  while(checkState(STATE_OPENIDLE)){//sensor ==true
     if((millis()-open_time>10000)||checkDoor() || readKeypad()){
      closeDoor();
     setState(STATE_IDLE);
     }
     
  }
}
void doStateReset(){//init here?
  while(checkState(STATE_RESET) ){
    readNFC();
    if( nfcHeader == HEADER_INIT){
        //setWifiData(nfcData[1],nfcData[2]);
        setState(STATE_IDLE);

    }
  }
  
}
void doStateOption1(){//init??
   //nfc here too
}
void doStateOption2(){
  
}



//================================ main setup and loop

void setup() {
  machineState = STATE_IDLE;//STATE_RESET;
 
  
  Serial.begin(9600);
  nfc.begin();
  pinMode(RELAY, OUTPUT);
  pinMode(RELAY2, OUTPUT);
  pinMode(SCL_PIN, OUTPUT);  
  pinMode(SDO_PIN, INPUT); 
  
  pinMode(PHOTO1, INPUT); // 포토인터럽터 입력으로 설정;  
  //
  
  
  uint32_t versiondata = nfc.getFirmwareVersion();
  if (!versiondata){
    Serial.print("Didn't find PN53x board");
    while (1); // halt
  }else{
   Serial.println("Found PN53x board"); 
   }
  nfc.SAMConfig();
  initNfcData();
  WifiSerial.begin(9600);
  resetCPwd();
  closeDoor();
  Serial.println("drlk start");
  pinMode(2, INPUT_PULLUP);
  delay(1000);
}

void loop() {
  

  Serial.print("Machine state : ");//--------------------------------------Log
  Serial.println(machineState);
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
    /*case STATE_OPTION1:
        doStateOption1();
      break;
    case STATE_OPTION2:
        doStateOption2();
      break;*/
    default: 
      setState(STATE_IDLE);
      break;
  }
  delay(100);
  
}
