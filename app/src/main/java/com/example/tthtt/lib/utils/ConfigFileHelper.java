package com.example.tthtt.lib.utils;

import android.os.Environment;

import com.example.tthtt.logic.syncPhoneDb.model.DeviceModel;
import com.example.tthtt.logic.syncPhoneDb.model.PhoneModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Scanner;
public class ConfigFileHelper {

    public static void WriteDeviceConfig(PhoneModel config){
        if(!checkSd()) return;
        if(config == null || config.getDevice_info() == null) return;
        String content = config.getDevice_info();
        HashMap<String, String> map = new Gson().fromJson(content, new TypeToken<HashMap<String, String>>(){}.getType());
        content = "";
        for (HashMap.Entry<String, String> entry : map.entrySet()) {
            content += entry.getKey() + "=" + entry.getValue() + "\r\n";
        }

        String sDir = Environment.getExternalStorageDirectory().getPath() + File.separator + "device.ini";
        FileUtil.writeFile(sDir, content, false);
    }

    private static boolean checkSd(){
        if(Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)){
            return true;
        }else{
            return false;
        }
    }

    public static HashMap<String, String> ReadConfig(){
        try {
            if(!checkSd()) return null;
            String sDir = Environment.getExternalStorageDirectory().getPath() + File.separator + "config.ini";
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
                int index = str.indexOf("downloaded_open_rate");
                HashMap<String, String> map = new HashMap<>();
                if(index >= 0){
                    String time = str.substring(0, index);
                    map.put("time", time);
                    str = str.substring(index);
                }
                while (str.indexOf("\n") > 0){
                    index = str.indexOf("\n");
                    String data = str.substring(0, index);
                    str = str.substring(index + 1);

                    index = data.indexOf("=");
                    String key = data.substring(0, index);
                    String value = data.substring(index + 1);
                    map.put(key, value);
                }
                return map;
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void WriteConfig(HashMap<String, String> map){
        if(!checkSd()) return;
        if(map == null || map.size() <= 0) return;
        String content = "";
        if(map.get("time") != null){
            content += map.get("time") + "\n";
            map.remove("time");
        }
        for (HashMap.Entry<String, String> entry : map.entrySet()) {
            content += entry.getKey() + "=" + entry.getValue() + "\r\n";
        }

        String sDir = Environment.getExternalStorageDirectory().getPath() + File.separator + "config.ini";
        FileUtil.writeFile(sDir, content, false);
    }
}
