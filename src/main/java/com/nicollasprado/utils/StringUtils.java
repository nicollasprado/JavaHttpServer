package com.nicollasprado.utils;

public class StringUtils {
    public static String toTitleCase(String originalStr){
        StringBuilder finalStr = new StringBuilder(originalStr.toLowerCase());

        char previous = ' ';
        for(int i = 0; i < finalStr.length(); i++){
            char iter = finalStr.charAt(i);

            if(i == 0 || previous == ' ') {
                char iterUpper = (char) (iter - 32);
                finalStr.setCharAt(i, iterUpper);
            }

            previous = iter;
        }

        return finalStr.toString();
    }

}
