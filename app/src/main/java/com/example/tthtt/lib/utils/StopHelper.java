package com.example.tthtt.lib.utils;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by book4 on 2018/4/6.
 */

public class StopHelper {
    private static StopHelper mInstance;
    private Process process;
    private final String[] mAppArray = {"com.lsf.xmchqq.android"};

    public static StopHelper getInstance(){
        if(mInstance == null){
            synchronized (StopHelper.class){
                mInstance = new StopHelper();
            }
        }
        return mInstance;
    }

    /**
     * 结束进程,执行操作调用即可
     */
    public void kill() {
        initProcess();
        for(String app: mAppArray) {
            killProcess(app);
        }
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
    private void killProcess(String packageName) {
        OutputStream out = process.getOutputStream();
        String cmd = "am force-stop " + packageName + " \n";
        String cmd2 = "pm clear " + packageName + "\n";
        try {
            out.write(cmd.getBytes());
            out.write(cmd2.getBytes());
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
