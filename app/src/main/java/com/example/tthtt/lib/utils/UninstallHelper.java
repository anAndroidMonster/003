package com.example.tthtt.lib.utils;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * Created by book4 on 2018/4/6.
 */

public class UninstallHelper {
    private static UninstallHelper mInstance;
    private Process process;

    public static UninstallHelper getInstance(){
        if(mInstance == null){
            synchronized (UninstallHelper.class){
                mInstance = new UninstallHelper();
            }
        }
        return mInstance;
    }

    /**
     * 结束进程,执行操作调用即可
     */
    public void uninstall(List<String> appList) {
        initProcess();
        for(String app: appList) {
            LogHelper.d("应用卸载" + app);
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
        String cmd = "pm uninstall " + packageName + " \n";
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
