package com.test.framer.model;
import java.lang.Math;

public class Gravity {
//    private float gravity;
//
//    public Gravity(float gravity) {
//        this.gravity = gravity;
//    }
//
//    public void setGravity(float gravity) {
//        this.gravity = gravity;
//    }

    // arguments: gravity in specific gravity(not unt)
    // return the prefer unit in brix, plato
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
//ABV = alcohol by volume, OG = original gravity, and FG =
// gravity. So, using this formula with a beer having an OG
// of 1.055 and a FG of 1.015, your ABV would be 5.25%.
    public static float ABV(float OG, float FG){
        return (float) ((OG-FG)*131.25);
    }
}
