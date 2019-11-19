package com.test.framer.model;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
//< Validate the IP address

public class regexIP {
    public static boolean isValidIP(String ipAddr){
        Pattern ptn = Pattern.compile("^(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})$");
        Matcher mtch = ptn.matcher(ipAddr);
        return mtch.find();
    }
}
