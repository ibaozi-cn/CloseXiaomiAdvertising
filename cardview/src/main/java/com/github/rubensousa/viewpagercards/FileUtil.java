package com.github.rubensousa.viewpagercards;


import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class FileUtil {

    public static String readLogByString(String path, String defaultValue) {
        StringBuffer sb = new StringBuffer();
        String tempstr = null;
        try {
            File file = new File(path);
            File dir = file.getParentFile();
            if (!dir.exists()) {
                dir.mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
                writeLog(path, defaultValue, false, "utf-8");
                return defaultValue;
            }
            FileInputStream fis = new FileInputStream(file);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis, "utf-8"));
            while ((tempstr = br.readLine()) != null) {
                sb.append(tempstr);
            }
        } catch (IOException ex) {
            return defaultValue;
        }
        return sb.toString();
    }

    /**
     * 写入文件,末尾自动添加\r\n
     *
     * @param path
     * @param str
     */
    public static void writeLog(String path, String str, boolean is_append, String encode) {
        try {
            File file = new File(path);
            File dir = file.getParentFile();
            if (!dir.exists()) {
                dir.mkdirs();
            }
            if (!file.exists())
                file.createNewFile();
            FileOutputStream out = new FileOutputStream(file, is_append); //true表示追加
            StringBuffer sb = new StringBuffer();
            sb.append(str + "\r\n");
            out.write(sb.toString().getBytes(encode));//
            out.close();
            Log.d("AccessibilityNodeInfo", "writeLog: $path " + path + "$content " + str);
        } catch (IOException ex) {
            ex.printStackTrace();
            Log.e("AccessibilityNodeInfo", "writeLog: StackTrace " + ex.getStackTrace());
        }
    }

    public static void copyAssets(Context context) {
        AssetManager assetManager = context.getAssets();
        String[] files = null;
        try {
            files = assetManager.list("");
        } catch (IOException e) {
            Log.e("tag", "Failed to get asset file list.", e);
        }
        for(String filename : files) {
            InputStream in = null;
            OutputStream out = null;
            try {
                in = assetManager.open(filename);
                File outFile = new File(context.getExternalFilesDir(null), filename);
                out = new FileOutputStream(outFile);
                copyFile(in, out);
                in.close();
                in = null;
                out.flush();
                out.close();
                out = null;
            } catch(IOException e) {
                Log.e("tag", "Failed to copy asset file: " + filename, e);
            }
        }
    }
    private static void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while((read = in.read(buffer)) != -1){
            out.write(buffer, 0, read);
        }
    }


}
