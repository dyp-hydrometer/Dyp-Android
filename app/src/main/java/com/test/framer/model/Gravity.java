package com.test.framer.model;
import java.lang.Math;

public class Gravity {
    /**
     *  Convert the specific gravity in the preferred unit
     * @param SG Specific gravity
     * @param prefGravity preferred unit
     * @return Double Specific gravity in the preferred unit unit in brix, plato
     */
    public static double prefGravUnit(double SG,String prefGravity ){
        double brix,plato;
        if(prefGravity=="Brix"){
            brix= ((((182.4601 * SG) -775.6821) * SG +1262.7794) * SG -669.5622);
            return brix ;
        }
        else if(prefGravity=="Plato"){
            plato = (-1 * 616.868) + (1111.14 * SG) - (630.272 * Math.pow(SG,2)) + (135.997 * Math.pow(SG,3));
            return plato;
        }
        else{
            return SG;
        }
    }

    /**
     *  estimate the alcohol by volume base on the initial specific gravity and the final one
     * @param OG Origin Gravity
     * @param FG Final Gravity
     * @return Alcohol by volume
     */
    public static Double ABV(Double OG, Double FG){
        return  ((OG-FG)*131.25);
    }
}
