package com.gammaray.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.Log;
import android.view.ViewConfiguration;

import com.blankj.utilcode.util.FileUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * Created by Govind on 02/05/2018.
 */
public class FileHelper {
    private static final int BUFFER_SIZE = 8192;//2048;
    private static String TAG = FileHelper.class.getName().toString();
    private static String parentPath = "";

    public static boolean zip(String sourcePath, String destinationPath, String destinationFileName, Boolean includeParentFolder) {
        new File(destinationPath).mkdirs();
        FileOutputStream fileOutputStream;
        ZipOutputStream zipOutputStream = null;
        try {
            if (!destinationPath.endsWith("/")) destinationPath += "/";
            String destination = destinationPath + destinationFileName;
            File file = new File(destination);
            if (!file.exists()) file.createNewFile();

            fileOutputStream = new FileOutputStream(file);
            zipOutputStream = new ZipOutputStream(new BufferedOutputStream(fileOutputStream));
            if (includeParentFolder)
                parentPath = new File(sourcePath).getParent() + "/";
            else
                parentPath = sourcePath;

            zipFile(zipOutputStream, sourcePath);
        } catch (IOException ioe) {
            Log.d(TAG, ioe.getMessage());
            return false;
        } finally {
            if (zipOutputStream != null)
                try {
                    zipOutputStream.close();
                } catch (IOException e) {
                }
        }
        return true;
    }

    private static void zipFile(ZipOutputStream zipOutputStream, String sourcePath) throws IOException {
        java.io.File files = new java.io.File(sourcePath);
        java.io.File[] fileList = files.listFiles();

        String entryPath = "";
        BufferedInputStream input;
        for (java.io.File file : fileList) {
            if (file.isDirectory()) {
                zipFile(zipOutputStream, file.getPath());
            } else {
                byte data[] = new byte[BUFFER_SIZE];
                FileInputStream fileInputStream = new FileInputStream(file.getPath());
                input = new BufferedInputStream(fileInputStream, BUFFER_SIZE);
                entryPath = file.getAbsolutePath().replace(parentPath, "");

                ZipEntry entry = new ZipEntry(entryPath);
                zipOutputStream.putNextEntry(entry);
                int count;
                while ((count = input.read(data, 0, BUFFER_SIZE)) != -1) {
                    zipOutputStream.write(data, 0, count);
                }
                input.close();
            }
        }
    }

    public static Boolean unzip(String sourceFile, String destinationFolder) {
        ZipInputStream zis = null;

        try {
            zis = new ZipInputStream(new BufferedInputStream(new FileInputStream(sourceFile)));
            ZipEntry ze;
            int count;
            byte[] buffer = new byte[BUFFER_SIZE];
            while ((ze = zis.getNextEntry()) != null) {
                String fileName = ze.getName();
                fileName = fileName.substring(fileName.indexOf("/") + 1);
                File file = new File(destinationFolder, fileName);
                File dir = ze.isDirectory() ? file : file.getParentFile();

                if (!dir.isDirectory() && !dir.mkdirs())
                    throw new FileNotFoundException("Invalid path: " + dir.getAbsolutePath());
                if (ze.isDirectory()) continue;
                FileOutputStream fout = new FileOutputStream(file);
                try {
                    while ((count = zis.read(buffer)) != -1)
                        fout.write(buffer, 0, count);
                } finally {
                    fout.close();
                }

            }
        } catch (IOException ioe) {
            Log.d(TAG, ioe.getMessage());
            return false;
        } finally {
            if (zis != null)
                try {
                    zis.close();
                } catch (IOException e) {

                }
        }
        return true;
    }

    public static void saveToFile(String destinationPath, String data, String fileName) {
        try {
            new File(destinationPath).mkdirs();
            File file = new File(destinationPath + fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fileOutputStream = new FileOutputStream(file, true);
            fileOutputStream.write((data + System.getProperty("line.separator")).getBytes());

        } catch (FileNotFoundException ex) {
            Log.d(TAG, ex.getMessage());
        } catch (IOException ex) {
            Log.d(TAG, ex.getMessage());
        }
    }

    public static String saveBitmap(String name, Bitmap bitmap, Context context) {
        String targetPath = context.getFilesDir() + "/images/";
        File saveFile = new File(targetPath, name);
        if (!FileUtils.isFileExists(targetPath)) {
            try {

                FileOutputStream saveImgOs = new FileOutputStream(saveFile);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 88, saveImgOs);
                saveImgOs.flush();
                saveImgOs.close();

            } catch (IOException e) {

            }
        }
        return saveFile.getAbsolutePath();
    }

    /**
     * 检查是否存在虚拟按键栏
     * @param context
     * @return
     */
    public static boolean hasNavBar(Context context) {
        Resources res = context.getResources();
        int resourceId = res.getIdentifier("config_showNavigationBar", "bool", "android");
        if (resourceId != 0) {
            boolean hasNav = res.getBoolean(resourceId);
            // check override flag
            String sNavBarOverride = getNavBarOverride();
            if ("1".equals(sNavBarOverride)) {
                hasNav = false;
            } else if ("0".equals(sNavBarOverride)) {
                hasNav = true;
            }
            return hasNav;
        } else { // fallback
            return !ViewConfiguration.get(context).hasPermanentMenuKey();
        }
    }
    /**
     * 判断虚拟按键栏是否重写
     * @return
     */
    private static String getNavBarOverride() {
        String sNavBarOverride = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                Class c = Class.forName("android.os.SystemProperties");
                Method m = c.getDeclaredMethod("get", String.class);
                m.setAccessible(true);
                sNavBarOverride = (String) m.invoke(null, "qemu.hw.mainkeys");
            } catch (Throwable e) {
            }
        }
        return sNavBarOverride;
    }
}