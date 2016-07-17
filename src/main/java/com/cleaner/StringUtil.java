package com.cleaner;

public class StringUtil {

    public static boolean contains(String[] args, String s)
    {
        for(String arg : args){
            if(arg.contains(s))
                return true;
        }

        return false;
    }

    public static String extract(String[] args, String searchString){
        for(String arg : args){
            if(arg.contains(searchString)){
                return arg;
            }
        }

        return null;
    }
}
