package com.example.tthtt.utils;

import android.content.Context;

import com.example.tthtt.db.helper.VpnDbHelper;
import com.example.tthtt.model.VpnModel;
import com.google.gson.Gson;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by book4 on 2018/4/14.
 */

public class ChangePhoneHelper {

    private static ChangePhoneHelper mInstance;
    private Object mManager;
    private Method mChange;
    private Method mSetDeviceMap;
    private Method mDisconnect;
    private Method mConnect;

    private Object mConfig;
    private Method mGetInstance;
    private Method mGetBoolean;
    private Method mSetBoolean;

    private Object mAES;
    private Method mDecodeHex;
    private Method mEncodeHex;

    public static ChangePhoneHelper getInstance(){
        if(mInstance == null){
            synchronized (ChangePhoneHelper.class){
                mInstance = new ChangePhoneHelper();
            }
        }
        return mInstance;
    }

    public void init(Context context){
        try {
            Context mmsCtx = context.createPackageContext("com.android.settings", Context.CONTEXT_INCLUDE_CODE | Context.CONTEXT_IGNORE_SECURITY);
            Class<?> clazz = Class.forName("com.android.settings.changephone.ChangePhoneManager", true, mmsCtx.getClassLoader());
            Class<?> clazz2 = Class.forName("com.oinux.android.ConfigLoader", true, mmsCtx.getClassLoader());
            Class<?> class3 = Class.forName("com.oinux.android.AES", true, mmsCtx.getClassLoader());
            Constructor<?>[] constructors = clazz.getDeclaredConstructors();
            Constructor<?> constructor = constructors[0];
            constructor.setAccessible(true);

            mManager = constructor.newInstance(context);

            Method[] methodss = clazz.getDeclaredMethods();
            Method[] methodss2 = clazz2.getDeclaredMethods();
            Method[] methodss3 = class3.getDeclaredMethods();
            for (Method meth : methodss) {
                if (meth.getName().equals("doChangePhone")) {
                    mChange = meth;
                    mChange.setAccessible(true);
                }else if(meth.getName().equals("-set1")){
                    mSetDeviceMap = meth;
                    mSetDeviceMap.setAccessible(true);
                }else if(meth.getName().equals("disconnect")){
                    mDisconnect = meth;
                    mDisconnect.setAccessible(true);
                }else if(meth.getName().equals("connect")){
                    mConnect = meth;
                    mConnect.setAccessible(true);
                }
            }
            for (Method meth : methodss2) {
                if (meth.getName().equals("getInstance")) {
                    mGetInstance = meth;
                    mGetInstance.setAccessible(true);
                    mConfig = mGetInstance.invoke(null);
                }else if(meth.getName().equals("getBoolean")){
                    mGetBoolean = meth;
                    mGetBoolean.setAccessible(true);
                }else if(meth.getName().equals("set")){
                    Class<?>[] types = meth.getParameterTypes();
                    if(types.length == 2 && types[0].getSimpleName().equals("String") && types[1].getSimpleName().equals("Object")){
                        mSetBoolean = meth;
                        mSetBoolean.setAccessible(true);
                    }
                }
            }

            for (Method meth : methodss3) {
                if (meth.getName().equals("decodeHex")) {
                    mDecodeHex = meth;
                    mDecodeHex.setAccessible(true);
                }else if(meth.getName().equals("encodeHex")){
                    mEncodeHex = meth;
                    mEncodeHex.setAccessible(true);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            LogHelper.d("change phone init error:" + e.getMessage());
        }
    }

    public VpnModel connectVpnRandom(){
        List<VpnModel> vpnList = VpnDbHelper.getInstance().getListInUse();
        if(vpnList == null || vpnList.size() <= 0){
            return null;
        }
        Random random = new Random();
        int index = random.nextInt(vpnList.size());
        VpnModel result = vpnList.get(index);
        LogHelper.d("连接vpn：" + result.getServer());
        HashMap<String, String> map = ConfigFileHelper.ReadConfig();
        if(map == null || map.size() <= 0) return null;
        String code = ChangePhoneHelper.getInstance().getVpnCode(result);
        map.put("VPN_PROFILE", code);
        connectVpn(code);
        return result;
    }

    public void connectVpn(String code){
        try {
            mDisconnect.invoke(mManager);
            mSetBoolean.invoke(mConfig, new Object[] {"VPN_PROFILE", code});
            mConnect.invoke(mManager, false);
        }catch (Exception ex){
            ex.printStackTrace();
            LogHelper.d("change phone vpn error:" + ex.getMessage());
        }
    }

    public void doChange(Map map){
        try {
            mSetBoolean.invoke(mConfig, new Object[] {"connect_vpn", false});
            mSetDeviceMap.invoke(null, new Object[]{mManager, map});
            LogHelper.d("set map");
            mChange.invoke(mManager);
            LogHelper.d("do change");
        }catch (Exception ex){
            ex.printStackTrace();
            LogHelper.d("change phone change error:" + ex.getMessage());
        }
    }

    public VpnModel getVpn(String code){
        VpnModel result = null;
        if(StringUtil.isEmpty(code)) return result;
        try {
            byte[] codes = (byte[]) mDecodeHex.invoke(null, code);
            VpnProfile profile = VpnProfile.decode("VPN_PROFILE", codes);
            Gson gson = new Gson();
            String jsonStr = gson.toJson(profile);
            result = gson.fromJson(jsonStr, VpnModel.class);
            result.setCodeStr(code);
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    public String getVpnCode(VpnModel model){
        String result = null;
        if(model == null) return result;
        try {
            Gson gson = new Gson();
            String jsonStr = gson.toJson(model);
            VpnProfile profile = gson.fromJson(jsonStr, VpnProfile.class);
            byte[] code2 = profile.encode();
            result = (String) mEncodeHex.invoke(null, code2);
            model.setCodeStr(result);
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }
}
