/**
 *
 * Copyright (c) 2014 CoderKiss
 *
 * CoderKiss[AT]gmail.com
 *
 */

package com.example.tthtt.utils;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.admin.DevicePolicyManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.ConfigurationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.IBinder;
import android.os.Looper;
import android.os.StrictMode;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.MotionEvent;

import com.example.tthtt.common.MyAppContext;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.TimeoutException;

import dalvik.system.DexFile;

public class SystemUtil {
	public static final String TAG = "SystemUtil";

	public static final int MAX_BRIGHTNESS = 255;
	public static final int MIN_BRIGHTNESS = 0;

	public final static boolean isMainThread() {
		return Looper.getMainLooper() == Looper.myLooper();
	}

	public static boolean installedApp(String packageName) {
		Context context = MyAppContext.getInstance();
		PackageInfo packageInfo = null;
		if (TextUtils.isEmpty(packageName)) {
			return false;
		}
		final PackageManager packageManager = context.getPackageManager();
		List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
		if (packageInfos == null) {
			return false;
		}
		for (int index = 0; index < packageInfos.size(); index++) {
			packageInfo = packageInfos.get(index);
			final String name = packageInfo.packageName;
			if (packageName.equals(name)) {
				return true;
			}
		}
		return false;
	}

	public static int getStatusBarHeight(Context context) {
		int height = 0;
		if (context == null) {
			return height;
		}
		Resources resources = context.getResources();
		int resId = resources.getIdentifier("status_bar_height", "dimen",
				"android");
		if (resId > 0) {
			height = resources.getDimensionPixelSize(resId);
		}
		return height;
	}

	public static void uninstallApp(String packageName) {
		Context context = MyAppContext.getInstance();
		boolean installed = installedApp(packageName);
		if (!installed) {
			return;
		}

		boolean isRooted = isRooted();
		if (isRooted) {
			runRootCmd("pm uninstall " + packageName);
		} else {
			Uri uri = Uri.parse("package:" + packageName);
			Intent intent = new Intent(Intent.ACTION_DELETE, uri);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(intent);
		}
	}

	public static int getSysScreenBrightness() {
		Context context = MyAppContext.getInstance();
		int screenBrightness = 255;
		try {
			screenBrightness = Settings.System.getInt(
					context.getContentResolver(),
					Settings.System.SCREEN_BRIGHTNESS);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return screenBrightness;
	}

	public static void setSysScreenBrightness(int brightness) {
		Context context = MyAppContext.getInstance();
		try {
			if (brightness < MIN_BRIGHTNESS) {
				brightness = MIN_BRIGHTNESS;
			}
			if (brightness > MAX_BRIGHTNESS) {
				brightness = MAX_BRIGHTNESS;
			}
			ContentResolver resolver = context.getContentResolver();
			Uri uri = Settings.System
					.getUriFor(Settings.System.SCREEN_BRIGHTNESS);
			Settings.System.putInt(resolver, Settings.System.SCREEN_BRIGHTNESS,
					brightness);
			resolver.notifyChange(uri, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static int getBrightnessMode() {
		Context context = MyAppContext.getInstance();
		int brightnessMode = Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL;
		try {
			brightnessMode = Settings.System.getInt(
					context.getContentResolver(),
					Settings.System.SCREEN_BRIGHTNESS_MODE);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return brightnessMode;
	}

	// 1 auto, 0 manual
	public static void setBrightnessMode(int brightnessMode) {
		Context context = MyAppContext.getInstance();
		try {
			Settings.System.putInt(context.getContentResolver(),
					Settings.System.SCREEN_BRIGHTNESS_MODE, brightnessMode);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void setWifiEnabled(boolean enable) {
		Context context = MyAppContext.getInstance();
		try {
			WifiManager wifiManager = (WifiManager) context.getApplicationContext()
					.getSystemService(Context.WIFI_SERVICE);
			wifiManager.setWifiEnabled(enable);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static boolean isWifiEnabled() {
		Context context = MyAppContext.getInstance();
		boolean enabled = false;
		try {
			WifiManager wifiManager = (WifiManager) context.getApplicationContext()
					.getSystemService(Context.WIFI_SERVICE);
			enabled = wifiManager.isWifiEnabled();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return enabled;
	}

	public static boolean isRooted() {
		String binaryName = "su";
		boolean rooted = false;
		String[] places = {"/sbin/", "/system/bin/", "/system/xbin/",
				"/data/local/xbin/", "/data/local/bin/", "/system/sd/xbin/",
				"/system/bin/failsafe/", "/data/local/"};
		for (String where : places) {
			if (new File(where + binaryName).exists()) {
				rooted = true;
				break;
			}
		}
		return rooted;
	}

	public static void lockScreen() {
		Context context = MyAppContext.getInstance();
		DevicePolicyManager deviceManager = (DevicePolicyManager) context
				.getSystemService(Context.DEVICE_POLICY_SERVICE);
		deviceManager.lockNow();
	}

	// <uses-permission android:name="android.permission.INJECT_EVENTS" />
	public final static void inputKeyEvent(int keyCode) {
		try {
			runRootCmd("input keyevent " + keyCode);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String runCmd(String cmd) {
		if (TextUtils.isEmpty(cmd)) {
			return null;
		}
		Process process = null;
		String result = null;

		String[] commands = {"/system/bin/sh", "-c", cmd};

		try {
			process = Runtime.getRuntime().exec(commands);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			int read = -1;
			InputStream errIs = process.getErrorStream();
			while ((read = errIs.read()) != -1) {
				baos.write(read);
			}
			baos.write('\n');

			InputStream inIs = process.getInputStream();
			while ((read = inIs.read()) != -1) {
				baos.write(read);
			}

			byte[] data = baos.toByteArray();
			result = new String(data);

		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	private static void runRootCmd(String cmd) {
		if (TextUtils.isEmpty(cmd)) {
			return;
		}
		Process process;
		try {
			process = Runtime.getRuntime().exec("su");
			DataOutputStream os = new DataOutputStream(
					process.getOutputStream());
			os.writeBytes(cmd + " ;\n");
			os.flush();

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			int read = -1;
			InputStream errIs = process.getErrorStream();
			while ((read = errIs.read()) != -1) {
				baos.write(read);
			}
			baos.write('\n');

			InputStream inIs = process.getInputStream();
			while ((read = inIs.read()) != -1) {
				baos.write(read);
			}

			byte[] data = baos.toByteArray();
			String result = new String(data);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static int getDistance(MotionEvent e1, MotionEvent e2) {
		float x = e1.getX() - e2.getX();
		float y = e1.getY() - e2.getY();
		return (int) Math.sqrt(x * x + y * y);
	}

	public static long getMaxMemory() {
		Runtime runtime = Runtime.getRuntime();
		long maxMemory = runtime.maxMemory();
		return maxMemory;
	}

	public static boolean isDebuggable(Context context) {
		boolean debuggable = ((context.getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0);
		return debuggable;
	}

	public static String getApplicaitonDir() {
		Context context = MyAppContext.getInstance();
		PackageManager packageManager = context.getPackageManager();
		String packageName = context.getPackageName();
		String applicationDir = null;
		try {
			PackageInfo p = packageManager.getPackageInfo(packageName, 0);
			applicationDir = p.applicationInfo.dataDir;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return applicationDir;
	}

	public static void restartApplication(Class<?> clazz) {
		Context context = MyAppContext.getInstance();
		Intent intent = new Intent(context, clazz);
		int requestCode = 198964;
		PendingIntent pendingIntent = PendingIntent.getActivity(context,
				requestCode, intent, PendingIntent.FLAG_CANCEL_CURRENT);
		AlarmManager am = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		am.set(AlarmManager.RTC, System.currentTimeMillis() + 500,
				pendingIntent);
		System.exit(0);
	}


	public static String getVersionName() {
		try {
			PackageInfo info = MyAppContext.getInstance().getPackageManager().getPackageInfo(MyAppContext.getInstance().getPackageName(), 0);
			return info.versionName;
		} catch (NameNotFoundException e) {
			return "";
		}
	}


	public static int getVersionCode() {
		try {
			Context context = MyAppContext.getInstance();
			PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			return info.versionCode;
		} catch (NameNotFoundException e) {
			return -1;
		}
	}

	public static int getVersionCode(String packageName) {
		try {
			Context context = MyAppContext.getInstance();
			PackageInfo info = context.getPackageManager().getPackageInfo(packageName, 0);
			return info.versionCode;
		} catch (NameNotFoundException e) {
			return -1;
		}
	}


	public static boolean isSupportedOpenGlEs2() {
		ActivityManager activityManager = (ActivityManager) MyAppContext.getInstance().getSystemService(Context.ACTIVITY_SERVICE);
		ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
		boolean supportedOpenGlEs2 = configurationInfo.reqGlEsVersion >= 0x20000;
		return supportedOpenGlEs2;
	}


	/**
	 * Compares two version strings, using Semantic Versioning convention. See <a href="http://semver.org/">http://semver.org/</a>.
	 *
	 * @param v1 first version string.
	 * @param v2 second version string.
	 * @return 0 if the versions are equal, 1 if version v1 is before version v2, -1 if version v1 is after version v2, -2 if version format is invalid.
	 */
	public static int compareVersions(String v1, String v2) {
		if (v1 == null || v2 == null || v1.trim().equals("") || v2.trim().equals("")) return -2;
		else if (v1.equals(v2)) return 0;
		else {
			boolean valid1 = v1.matches("\\d+\\.\\d+\\.\\d+");
			boolean valid2 = v2.matches("\\d+\\.\\d+\\.\\d+");

			if (valid1 && valid2) {
				int[] nums1;
				int[] nums2;

				try {
					nums1 = convertStringArrayToIntArray(v1.split("\\."));
					nums2 = convertStringArrayToIntArray(v2.split("\\."));
				} catch (NumberFormatException e) {
					return -2;
				}

				if (nums1.length == 3 && nums2.length == 3) {
					if (nums1[0] < nums2[0]) return 1;
					else if (nums1[0] > nums2[0]) return -1;
					else {
						if (nums1[1] < nums2[1]) return 1;
						else if (nums1[1] > nums2[1]) return -1;
						else {
							if (nums1[2] < nums2[2]) return 1;
							else if (nums1[2] > nums2[2]) return -1;
							else {
								return 0;
							}
						}
					}
				} else {
					return -2;
				}
			} else {
				return -2;
			}
		}
	}


	private static int[] convertStringArrayToIntArray(String[] stringArray) throws NumberFormatException {
		if (stringArray != null) {
			int intArray[] = new int[stringArray.length];
			for (int i = 0; i < stringArray.length; i++) {
				intArray[i] = Integer.parseInt(stringArray[i]);
			}
			return intArray;
		}
		return null;
	}

	@SuppressLint("NewApi")
	public static void enableStrictMode() {
		Context context = MyAppContext.getInstance();
		int appFlags = context.getApplicationInfo().flags;
		if ((appFlags & ApplicationInfo.FLAG_DEBUGGABLE) != 0 && Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
					.detectAll()
					.permitDiskReads()
					.permitDiskWrites()
					.penaltyLog()
					.penaltyDialog()
					.build());
			StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
					.detectLeakedClosableObjects()
					.detectLeakedRegistrationObjects()
					.detectLeakedSqlLiteObjects()
					.penaltyLog()
					.penaltyDeath()
					.build());
		}
	}

	public static boolean isPkgInstalled(String pkgName) {
		PackageInfo packageInfo = null;
		try {
			packageInfo = MyAppContext.getInstance().getPackageManager().getPackageInfo(pkgName, 0);
		} catch (NameNotFoundException e) {
			packageInfo = null;
		}
		if (packageInfo == null) {
			return false;
		} else {
			return true;
		}
	}


	public  static boolean serviceIsRunning(Context context,
								   String serviceClassName) {
		ActivityManager manager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningServiceInfo> running = manager.getRunningServices(Integer.MAX_VALUE);

		for (int i = 0; i < running.size(); i++) {
			if (serviceClassName.equals(running.get(i).service.getClassName())) {
				return true;
			}
		}
		return false;
	}

	public static boolean isAppOnForeground(Context context) {
		ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		String packageName =context.getPackageName();

		List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
				.getRunningAppProcesses();
		if (appProcesses == null)
			return false;

		for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
			// The name of the process that this object is associated with.
			if (appProcess.processName.equals(packageName)
					&& appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
				return true;
			}
		}
		return false;
	}

	@SuppressWarnings("rawtypes")
	public static void setProcessLimit(int limit) {
		DexFile df;
		try {
			df = new DexFile(new File("/system/priv-app/Settings.apk"));
		} catch (IOException e) {
			try {
				df = new DexFile(new File("/system/app/Settings.apk"));
			} catch (IOException e2) {
				e.printStackTrace();
				return;
			}
		}
		try {
			Class ActivityManagerNative = Class.forName("android.app.ActivityManagerNative");
			Class IActivityManager = Class.forName("android.app.IActivityManager");

			Method getDefault =  ActivityManagerNative.getMethod("getDefault", new Class[0]);
			Object am = IActivityManager.cast(getDefault.invoke(ActivityManagerNative, new Object[0]));

			Class[] args = new Class[1];
			args[0] = int.class;
			Method setProcessLimit = am.getClass().getMethod("setProcessLimit", args);
			setProcessLimit.invoke(am,limit);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	public static void makeServiceForeground(IBinder binder) {
		DexFile df;
		try {
			df = new DexFile(new File("/system/priv-app/Settings.apk"));
		} catch (IOException e) {
			try {
				df = new DexFile(new File("/system/app/Settings.apk"));
			} catch (IOException e2) {
				e.printStackTrace();
				return;
			}
		}
		try {
			Class ActivityManagerNative = Class.forName("android.app.ActivityManagerNative");
			Class IActivityManager = Class.forName("android.app.IActivityManager");

			Method getDefault =  ActivityManagerNative.getMethod("getDefault", new Class[0]);
			Object am = IActivityManager.cast(getDefault.invoke(ActivityManagerNative, new Object[0]));
			Class[] args = new Class[3];
			args[0] = IBinder.class;
			args[1] = int.class;
			args[2] = boolean.class;
			Method setProcessForeground = am.getClass().getMethod("setProcessForeground", args);
			setProcessForeground.invoke(am,binder, android.os.Process.myPid(), true);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}


	static String[] SU_OPTIONS = {
			"/data/bin/su",
			"/system/bin/su",
			"/system/xbin/su", };

	public static String execP(List<String> commands)
	{
		Process process = null;
		DataOutputStream os = null;
		String result = "";
		try {
			process = Runtime.getRuntime().exec(new String[] {"sh"});
			os = new DataOutputStream(process.getOutputStream());
			for (String command : commands) {
				os.writeBytes(command + "\n");
			}
			os.writeBytes("echo \"rc:\" $?\n");
			os.writeBytes("exit\n");
			os.flush();
            final long timeout=1500;
            Worker worker = new Worker(process);
            worker.start();
            try {
                worker.join(timeout);
                if (worker.exit != null){
                    if (process.exitValue() == 0) {
                        result = "success";
                    }else {
                        result = "failed";
                    }
                } else{
                    throw new TimeoutException();
                }
            } catch (InterruptedException ex) {
                worker.interrupt();
            }
			return result;
		} catch (Exception e) {
			return result;
		} finally {
			if (os != null)
				try {
					os.close();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			if (process != null) {
				try {
					process.exitValue();
				} catch (IllegalThreadStateException e) {
					process.destroy();
				}
			}
		}
	}

    private static class Worker extends Thread {
        private final Process process;
        private Integer exit;

        private Worker(Process process) {
            this.process = process;
        }

        public void run() {
            try {
                exit = process.waitFor();
            } catch (InterruptedException ignore) {
                return;
            }
        }
    }

	public static String exec(String command)
	{
		Process process = null;
		DataOutputStream os = null;
		String result = "";
		try {
			process = Runtime.getRuntime().exec(new String[]{"sh", "-c", command});
			os = new DataOutputStream(process.getOutputStream());
			os.writeBytes("echo \"rc:\" $?\n");
			os.writeBytes("exit\n");
			os.flush();
			process.waitFor();
			if (process.exitValue() == 0) {
				result = "success";
			}else {
				result = "failed";
			}
			return result;
		} catch (Exception e) {
			return result;
		} finally {
			if (os != null)
				try {
					os.close();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			if (process != null) {
				try {
					process.exitValue();
				} catch (IllegalThreadStateException e) {
					process.destroy();
				}
			}
		}
	}

	public static boolean isProcessAlive(Process p) {
		try {
			p.exitValue();
			return false;
		} catch (IllegalThreadStateException e) {
			return true;
		}
	}


	public static String getSuPath() {
		for (String p : SU_OPTIONS) {
			File su = new File(p);
			if (su.exists()) {
				return p;
			}
		}
		return null;
	}

	public static String getShPath() {
		String path = System.getenv("PATH");
		if ((path != null) && (path.length() > 0)) {
			String[] line = path.split(":");
			int i = line.length;
			for (int j = 0; j < i; j++) {
				File file = new File(line[j], "sh");
				if (file.exists())
					return file.getPath();
			}
		}
		return null;
	}

	public static boolean hasSuCmd() {
		try {
			String str = getSuPath();
			if (str != null)
				return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static void checkPermission(String output) {
		if (TextUtils.isEmpty(output))
			throw new SecurityException("Permission denied");
		if (-1 == output.indexOf("root"))
			throw new SecurityException("Permission denied");
	}


	public static IBinder getSystemService(String serviceName)
	{
		try {
			Class ServiceManager  = SystemUtil.class.getClassLoader().loadClass("android.os.ServiceManager");
			Method getServiceMethod = ServiceManager.getMethod("getService", String.class);
			IBinder binder =(IBinder) getServiceMethod.invoke(null,serviceName);
			return  binder;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}catch (NoSuchMethodException e) {
			e.printStackTrace();
		}catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void addSystemService(String serviceName, IBinder service)
	{
		try {
			Class ServiceManager  = SystemUtil.class.getClassLoader().loadClass("android.os.ServiceManager");
			Method addServiceMethod = ServiceManager.getMethod("addService", String.class, IBinder.class);
			addServiceMethod.invoke(null,serviceName,service);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}catch (NoSuchMethodException e) {
			e.printStackTrace();
		}catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	public static PackageInfo getPackageInfo(Context context){
		PackageManager pm = context.getPackageManager();
		try {
			return pm.getPackageInfo(context.getPackageName(), 0);
		} catch (NameNotFoundException e) {
		}
		return  new PackageInfo();
	}
}
