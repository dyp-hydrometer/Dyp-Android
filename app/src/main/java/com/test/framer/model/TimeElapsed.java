package com.test.framer.model;

// the methods will return the time elep
public class TimeElapsed {
        private static final int SECOND_MILLIS = 1000;
        public static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;  //< public because it is used in UnitFrament
       // public static final double millis_min = (1 / MINUTE_MILLIS) ;
        public static final double millis_min = 0.0000166666 ;
        private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
        private static final int DAY_MILLIS = 24 * HOUR_MILLIS;
        public static String getTimeAgo(long time) {
            if (time < 1000000000000L) {
                time *= 1000;
            }
            long now = System.currentTimeMillis();
            System.out.println(now);
            if (time > now || time <= 0) {
                return null;
            }

            final long diff = now - time;
            if (diff < MINUTE_MILLIS) {
                return "just now";
            } else if (diff < 2 * MINUTE_MILLIS) {
                return "a minute ago";
            } else if (diff < 50 * MINUTE_MILLIS) {
                return diff / MINUTE_MILLIS + " minutes ago";
            } else if (diff < 90 * MINUTE_MILLIS) {
                return "an hour ago";
            } else if (diff < 24 * HOUR_MILLIS) {
                return diff / HOUR_MILLIS + " hours ago";
            } else if (diff < 48 * HOUR_MILLIS) {
                return "yesterday";
            } else {
                return diff / DAY_MILLIS + " days ago";
            }
        }

//        public static void main(String[] args) {
//            System.out.println("Hello World");
//            String timeAgo = TimeElapsed.getTimeAgo(1573096170);
//            System.out.println(timeAgo);
//        }

    }
