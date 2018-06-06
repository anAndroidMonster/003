package com.example.tthtt.utils;

import com.example.tthtt.common.Constant;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by book4 on 2018/4/6.
 */

public class KeyHelper {
    private static KeyHelper mInstance;
    private Process process;
    public static final int KEYCODE_VOLUME_UP = 24;
    public static final int KEYCODE_VOLUME_DOWN = 25;

    public static KeyHelper getInstance(){
        if(mInstance == null){
            synchronized (KeyHelper.class){
                mInstance = new KeyHelper();
            }
        }
        return mInstance;
    }

    /**
     * 结束进程,执行操作调用即可
     */
    public void inputKey(int keyCode) {
        initProcess();
        doProgress(keyCode);
        close();
    }

    /**
     * 初始化进程
     */
    private void initProcess() {
        if (process == null)
            try {
                process = Runtime.getRuntime().exec("su");
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    /**
     * 结束进程
     */
    private void doProgress(int keyCode) {
        OutputStream out = process.getOutputStream();
        String cmd = "input keyevent " + keyCode + " \n";
        try {
            out.write(cmd.getBytes());
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭输出流
     */
    private void close() {
        if (process != null)
            try {
                process.getOutputStream().close();
                process = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
    }
}
