package com.zzg.robotium.lib;

import android.content.Context;
import android.util.Log;

public class InputDataStore {

    private static final String TAG = "ROBOT";

    private String Input_TargetPakage;

    private String ExternalStorageDirectory;

    private String Input_LogPath;

    private String Input_LogImagePath;

    private String Input_LogCrash;

    private String Input_TargetActivity;

    private int Input_Retrytime=1;

    private InputDataStore() {
    }

    private static InputDataStore inputDataStore = null;

    public static InputDataStore getInstance() {
        if (inputDataStore == null) {
            inputDataStore = new InputDataStore();
        }
        return inputDataStore;
    }

    public void init(Context context) {
        if (context != null) {
            setInput_TargetPakage(context.getPackageName());
            setExternalStorageDirectory(context.getApplicationContext().getFilesDir().getAbsolutePath());
            setInput_LogPath(getExternalStorageDirectory() + getInput_TargetPakage() + "/Result/");
            setInput_LogImagePath(getInput_LogPath() + "images/");
            setInput_LogCrash(getInput_LogPath() + "crash/");
            Log.e(TAG, "Input_TargetPakage" + Input_TargetPakage);
            Log.e(TAG, "ExternalStorageDirectory" + ExternalStorageDirectory);
            Log.e(TAG, "Input_LogPath" + Input_LogPath);
            Log.e(TAG, "Input_LogImagePath" + Input_LogImagePath);
            Log.e(TAG, "Input_LogCrash" + Input_LogCrash);
        } else {
            Log.e(TAG,"初始化失败："+"无法获取目标apk");
        }
    }

    public int getInput_Retrytime() {
        return Input_Retrytime;
    }

    public void setInput_Retrytime(int input_Retrytime) {
        Input_Retrytime = input_Retrytime;
    }

    public void setInput_TargetActivity(String action) {
        Log.e(TAG, "launcher activity=====" + action);
        Input_TargetActivity = action;
    }

    public String getInput_TargetPakage() {
        return Input_TargetPakage;
    }

    public void setInput_TargetPakage(String input_TargetPakage) {
        Input_TargetPakage = input_TargetPakage;
    }

    public String getExternalStorageDirectory() {
        return ExternalStorageDirectory;
    }

    public void setExternalStorageDirectory(String externalStorageDirectory) {
        ExternalStorageDirectory = externalStorageDirectory + "/";
    }

    public String getInput_LogPath() {
        return Input_LogPath;
    }

    public void setInput_LogPath(String input_LogPath) {
        Input_LogPath = input_LogPath;
    }

    public String getInput_LogImagePath() {
        return Input_LogImagePath;
    }

    public void setInput_LogImagePath(String input_LogImagePath) {
        Input_LogImagePath = input_LogImagePath;
    }

    public String getInput_LogCrash() {
        return Input_LogCrash;
    }

    public void setInput_LogCrash(String input_LogCrash) {
        Input_LogCrash = input_LogCrash;
    }

    public String getInput_TargetActivty() {
        return Input_TargetActivity;
    }
}
