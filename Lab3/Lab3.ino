// the setup function runs once when you press reset or power the board
void setup() {
  // initialize serial communications at 9600 bps:
  Serial.begin(9600);

  // initialize digital pins
  pinMode(9, OUTPUT); // Yellow
  pinMode(8, OUTPUT); // Red
  pinMode(10, OUTPUT); // Green
  pinMode(11, OUTPUT); // White

  pinMode(4, INPUT); // Switch 1
  pinMode(3, INPUT); // Switch 2
  pinMode(2, INPUT); // Switch 3

  pinMode(12, OUTPUT); // BUZZER

  pinMode(A0, OUTPUT); // Temp Analog
  pinMode(A1, OUTPUT); // LDR Analog

  pinMode(5, INPUT); // Photointerrupter

}

void loop(){
  photoInterrupter();


}

void photoInterrupter(){
  if(digitalRead(5) == HIGH){
    digitalWrite(12, HIGH);
    delay(1000);
    digitalWrite(12, LOW);
    delay(1000);
  }
}

void readLDR(){
  int lightVal = analogRead(A1);
  Serial.print("LDR Value: ");
  Serial.println(lightVal);

  if(lightVal < 15){
    digitalWrite(11, HIGH);
  }else {
    digitalWrite(11, LOW);
  }
  delay(1000);
}


void readTemp() { 
  int temp = analogRead(A0);
  float voltage = temp * (5.0 / 1023.0);
  float tempInC = (voltage) * 100.0;

  Serial.print("Temperature: ");
  Serial.print(tempInC);
  Serial.println("C");
  delay(1000);

}

void ledControl(){
  digitalWrite(11, HIGH); delay(1000);
  digitalWrite(11, LOW); delay(1000);

  digitalWrite(10, HIGH);

  digitalWrite(9, HIGH);
  delay(5000);
  digitalWrite(9, LOW);

  for(int i=0; i<10; i++){
    digitalWrite(8, HIGH);
    delay(1000);
    digitalWrite(8, LOW);
    delay(1000);
  }
}

void switchControl(){
  if(digitalRead(4) == HIGH){
    digitalWrite(11, HIGH);
    digitalWrite(8, HIGH);
  } else {
    digitalWrite(11, LOW);
    digitalWrite(8, LOW);

  }


  if(digitalRead(3) == HIGH){
    digitalWrite(10, HIGH);
    digitalWrite(9, HIGH);
    delay(300);
    digitalWrite(10, LOW);
    digitalWrite(9, LOW);
    delay(300);

  }

  if(digitalRead(2) == HIGH){
    digitalWrite(12, HIGH);
    delay(1000);
    digitalWrite(12, LOW);
    delay(1000);

  }
}

