package com.example.tthtt.utils;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by book4 on 2018/4/6.
 */

public class InstallHelper {
    private static InstallHelper mInstance;
    private Process process;
    private String mPath;

    public static InstallHelper getInstance(){
        if(mInstance == null){
            synchronized (InstallHelper.class){
                mInstance = new InstallHelper();
            }
        }
        return mInstance;
    }

    /**
     * 结束进程,执行操作调用即可
     */
    public void install(String path) {
        mPath = path;
        initProcess();
        LogHelper.d("应用安装" + path);
        killProcess(path);
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
        String cmd = "pm install -r " + packageName + " \n";
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

    public void deleteInstallApk(){
        if(StringUtil.isEmpty(mPath)) return;
        File file = new File(mPath);
        if(file.exists()){
            LogHelper.d("删除安装包");
            file.delete();
        }
    }
}
