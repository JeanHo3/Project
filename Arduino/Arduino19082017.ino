#include "DHT.h"
#include <TimeLib.h>
#include <Wire.h>

#define DHTPIN A0     // what pin we're connected to
#define moisensPin A1
#define DHTTYPE DHT22   // DHT 22  (AM2302)
DHT dht(DHTPIN, DHTTYPE);

int motorPin = 3;
int watertime = 5;
int waittime = 5;
int watered = false;
String output;

void setup()
{
    Serial.begin(9600);
    dht.begin();
    Wire.begin(8);                // join i2c bus with address #8
    Wire.onReceive(receiveEvent);
    pinMode(motorPin, OUTPUT);
}

void loop()
{
    float h = dht.readHumidity();
    float t = dht.readTemperature();
    int moisensValue = analogRead(moisensPin);

    // check if returns are valid, if they are NaN (not a number) then something went wrong!
    if (isnan(t) || isnan(h))
    {
        Serial.println("Failed to read from DHT");
    }
    else
    {
        output = h;
        output.concat(";");
        output.concat(String(t));
        output.concat(";");
        output.concat(String(moisensValue));
        output.concat(";");
        output.concat(String(watered));
        if(t>20 && moisensValue>100 && watered == false)
        {
          digitalWrite(motorPin, HIGH);
          delay(watertime*1000);
          digitalWrite(motorPin, LOW);
          watered = true;
        }
        delay(10000);
    }
}

void receiveEvent(int howMany){
  int x = Wire.read();
  if(x==1)
  {
    float h = dht.readHumidity();
    float t = dht.readTemperature();
    int moisensValue = analogRead(moisensPin);
    output = h;
        output.concat(";");
        output.concat(String(t));
        output.concat(";");
        output.concat(String(moisensValue));
        output.concat(";");
        output.concat(String(watered));
        char newOut[output.length()+1];
  output.toCharArray(newOut, output.length()+1);
  Wire.write(newOut); // respond with message
  }
  else if (x==2)
  {
    digitalWrite(motorPin, HIGH);
    delay(watertime*1000);
    digitalWrite(motorPin, LOW);
    watered = true;
  }
}
