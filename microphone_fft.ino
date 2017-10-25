
#include <stdint.h>
#include <ffft.h>
 
#define  IR_AUDIO  0 // ADC channel to capture


volatile  byte  position = 0;
volatile  long  zero = 0;

int16_t capture[FFT_N];			/* Wave captureing buffer */
complex_t bfly_buff[FFT_N];		/* FFT buffer */
uint16_t spektrum[FFT_N/2];		/* Spectrum output buffer */

 
void setup() {
    Serial.begin(57600);
    adcInit();
    adcCalb();
    establishContact();
}
 
 
void loop() {
    if (position == FFT_N)
    {
        fft_input(capture, bfly_buff);
        fft_execute(bfly_buff);
        fft_output(bfly_buff, spektrum);

        for (byte i = 0; i < 64; i++){
            Serial.write(spektrum[i]);
        }
        position = 0;
    } 
}

void establishContact() {
    while (Serial.available() <= 0) {
        Serial.write('A');   // send a capital A
        delay(300);
    }
}

ISR(ADC_vect)
{
    if (position >= FFT_N)
        return;

    capture[position] = ADC + zero;
    if (capture[position] == -1 || capture[position] == 1)
        capture[position] = 0;

    position++;
}

void adcInit(){
    ADMUX = _BV(REFS0) | IR_AUDIO; // | _BV(ADLAR); 
    ADCSRA = _BV(ADSC) | _BV(ADEN) | _BV(ADATE) | _BV(ADIE) | _BV(ADPS2) | _BV(ADPS1) | _BV(ADPS0); 
    sei();
}

void adcCalb(){
    Serial.println("Start to calc zero");
    long midl = 0;
    // get 2 meashurment at 2 sec
    // on ADC input must be NO SIGNAL!!!
    for (byte i = 0; i < 2; i++) {
        position = 0;
        delay(100);
        midl += capture[0];
        delay(900);
    }
    zero = -midl/2;
    Serial.println("Done.");
}
