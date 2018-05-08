package com.example.tthtt.utils;

import java.net.NetworkInterface;
import java.util.Collections;
import java.util.Enumeration;

/**
 * Created by book4 on 2018/3/21.
 */

public class VpnHelper {
    private static VpnHelper mInstance;

    public static VpnHelper getInstance(){
        if(mInstance == null){
            synchronized (VpnHelper.class){
                if(mInstance == null){
                    mInstance = new VpnHelper();
                }
            }
        }
        return mInstance;
    }



    public static boolean isVpnConnected() {
        try {
            Enumeration<NetworkInterface> niList = NetworkInterface.getNetworkInterfaces();
            if(niList != null) {
                for (NetworkInterface intf : Collections.list(niList)) {
                    if(!intf.isUp() || intf.getInterfaceAddresses().size() == 0) {
                        continue;
                    }
                    if ("tun0".equals(intf.getName()) || "ppp0".equals(intf.getName())){
                        return true;
                    }
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return false;
    }
}
