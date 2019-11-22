package com.test.framer.model;

public class temperature {
    /**
     *  convert temoerature into the preferred unit
     * @param tempF temperature in Fahrenheit
     * @param T  unit
     * @return  temperature in preferred unit
     */

    public static double prefTemprature(double tempF, String T ){
        if(T.equalsIgnoreCase("C")){
            return (( tempF - 32) * 0.555);
        }
        else{
            return tempF;
        }
    }
}
