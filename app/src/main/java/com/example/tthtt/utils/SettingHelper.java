package com.example.tthtt.utils;

import android.os.Environment;

import com.example.tthtt.model.SettingModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileInputStream;
import java.util.Scanner;

public class SettingHelper {

    public static SettingModel ReadHardwareConfig(){
        try {
            if(!checkSd()) return null;
            String sDir = Environment.getExternalStorageDirectory().getPath() + File.separator + "settings.txt";
            File fileName = new File(sDir);
            if (fileName.exists()) {
                FileInputStream input = new FileInputStream(sDir);
                Scanner scan = new Scanner(input);
                String str="";
                while (scan.hasNext()) {
                    str = new StringBuilder(String.valueOf(str)).append(scan.next()).append
                            ("\n").toString();
                }
                input.close();
                scan.close();
                SettingModel config = (SettingModel) new Gson().fromJson(str, new TypeToken<SettingModel>(){}.getType());
                return config;
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void WriteConfig(SettingModel config){
        if(!checkSd()) return;
        if(config == null ) return;
        String content = new Gson().toJson(config);
        String sDir = Environment.getExternalStorageDirectory().getPath() + File.separator + "settings.txt";
        FileUtil.writeFile(sDir, content, false);
    }

    private static boolean checkSd(){
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            return true;
        }else{
            return false;
        }
    }

}
