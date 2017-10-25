// the setup routine runs once when you press reset:
void setup() {
  Serial.begin(115200);
}
 
// the loop routine runs over and over again forever:
void loop() {

  int max_weight = 3500;
  int min_weight = 200;
  // read the input on analog pin 0:
  int weight_sensor = analogRead(A0);
  float weight = 0;

  weight = ((weight_sensor - min_weight) / (max_weight - min_weight)) * 100;


  // print out the value you read:


  Serial.println(weight);
  delay(1);        // delay in between reads for stability
}