package com.example.tthtt.utils;

/**
 * Created by book4 on 2018/3/20.
 */

public class PowerHelper {

    public static void shutDown(){
        try {
                Process proc = Runtime.getRuntime().exec(new String[]{"su","-c", "reboot -p"});
                proc.waitFor();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
