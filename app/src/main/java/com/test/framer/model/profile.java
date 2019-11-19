package com.test.framer.model;


import java.util.ArrayList;

public class profile {
        private int id;
        private String PName;
        private double Gravity;
        private double Temperature;
        private String Description;

    public int getId() {
        return id;
    }

    public String getDescription() {
        return Description;
    }
    // private boolean mOnline;

        public profile(int Id,String name,double grav,double temp, String description ) {
            id = Id;
            PName = name;
            Gravity = grav;
            Temperature = temp;
            Description = description;
        }

        public String getName() {
            return PName;
        }

        public double getGravity() {
            return Gravity;
        }

        public double getTemperature() {
            return Temperature;
        }


        public static int getLastContactId() {
            return lastContactId;
        }

         private static int lastContactId = 0;

        public static ArrayList<profile> createProfileList(int numProfile) {
            ArrayList<profile> profiles = new ArrayList<profile>();

//            for (int i = 1; i <= numProfile; i++) {
//                profiles.add(new profile("Beer " + ++lastContactId,"2","80"));
//            }
            return profiles;
        }
        @Override
        public String toString() {
            return PName;
        }

    }
