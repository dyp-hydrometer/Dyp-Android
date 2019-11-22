package com.test.framer.model;

public class TimeElapsed {
        private static final int SECOND_MILLIS = 1000;
        public static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;  //< public because it is used in UnitFrament
       // public static final double millis_min = (1 / MINUTE_MILLIS) ;
        public static final double millis_min = 0.0000166666 ;
        private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
        private static final int DAY_MILLIS = 24 * HOUR_MILLIS;

    /**
     *
     * @param time time Elapsed in millisecond
     * @return time ago
     */
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

    /**
     *  Convert the time in H:M:S format
     * @param interval API request time interval
     * @return the time in H:M:S
     */
        public static String minToHMS(int interval){
            StringBuffer temp = new StringBuffer(); int H=0,M=0,S=0;
            System.out.println("interval " + interval + " HOUR_MILLIS "+ HOUR_MILLIS +" div "+interval / HOUR_MILLIS);
            if(interval >=  HOUR_MILLIS){
                H=interval / HOUR_MILLIS;
                H= H>24? 23 : H;
                interval = interval - (H * HOUR_MILLIS);
                temp.append(H+":");
                //temp.append(H<=9 ? "0"+H+":": H+":");
                System.out.println("Hour " + H );
            }
            else{
                temp.append("0:");
            }

            if(interval >=  MINUTE_MILLIS){
                M=interval / MINUTE_MILLIS;
                M = M > 60? 59 : M;
                interval = interval - (M * MINUTE_MILLIS);

                temp.append(M<= 9 ? "0"+M+":": M+":");
                System.out.println("Min " + M );
            }
            else{
                temp.append("00:");
            }

            if(interval >=  SECOND_MILLIS){
                S=interval / SECOND_MILLIS;
                S = S > 60? 59 : S;
                interval = interval - (S * SECOND_MILLIS);
                temp.append(S<=9 ? "0"+S: S);
                System.out.println("Second " + S + S);
            }
            else{
                temp.append("00");
            }
            return temp.toString().trim();
        }
    }