package com.test.framer.model;

public class temperature {
    float temperature;

    public temperature(float temperature) {
        this.temperature = temperature;
    }

//    public static void setTemperature(float temperature) {
//        this.temperature = temperature;
//    }

    // Argument temperature in Fahrenheit , the preferred temperature

    public static double prefTemprature(double tempF, String T ){
        if(T=="C"){
            return ( tempF - 32) * (5/9);
        }
        else{
            return tempF;
        }

    }
}
