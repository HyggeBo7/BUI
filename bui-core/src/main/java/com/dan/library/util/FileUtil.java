/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.dan.library.util;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {
    private static final String TAG = "FileUtil";

    public static File getSaveFile(Context context) {
        File file = new File(context.getFilesDir(), "pic.jpg");
        return file;
    }

    public static File getSaveFileSd(Context context) {
        String fileUrl = Environment.getExternalStorageDirectory().getPath() + "/iformula/file";
        File dir = new File(fileUrl);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return new File(fileUrl, "pic.jpg");
    }

    /**
     * 读取某个文件夹下的所有文件名称,可以根据正则匹配(reg==null:获取所有)
     */
    public static List<String> readFileAndPathReg(String filePath, String reg) {
        return readFileAndPath(filePath, false, reg);
    }

    /**
     * 读取某个文件夹下的所有文件,或根据正则匹配
     */
    public static List<String> readFileAndPath(String filePath, Boolean filePathFlag, String reg) {
        List<String> returnFileList = new ArrayList<>();
        //如果路径为null
        if (StringUtils.isBlank(filePath)) {
            return returnFileList;
        }
        File file = new File(filePath);
        //文件不存在直接返回
        if (!file.exists()) {
            return returnFileList;
        }
        try {
            //如果是文件
            if (!file.isDirectory()) {
                //设置值
                String value = getFilePathType(file, filePathFlag, reg);
                if (value != null) {
                    returnFileList.add(value);
                }
            } else if (file.isDirectory()) {
                //文件夹
                String[] fileList = file.list();
                if (null != fileList && fileList.length > 0) {
                    for (String path : fileList) {
                        File readFile = new File(filePath + "/" + path);
                        if (!readFile.isDirectory()) {
                            String value = getFilePathType(readFile, filePathFlag, reg);
                            if (value != null) {
                                returnFileList.add(value);
                            }
                        } else if (readFile.isDirectory()) {
                            //添加子目录获取到的名称
                            returnFileList.addAll(readFileAndPath(filePath + "/" + path, filePathFlag, reg));
                        }
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "readfile()   Exception:", e);
        }
        return returnFileList;
    }

    private static String getFilePathType(File file, Boolean filePathFlag, String reg) {
        String returnValue = null;
        if (null == filePathFlag) {
            //相对路径
            returnValue = file.getPath();
        } else if (filePathFlag) {
            //绝对路径
            returnValue = file.getAbsolutePath();
        } else {
            //文件名称
            returnValue = file.getName();
        }
        if (StringUtils.isNotBlank(reg)) {
            return returnValue.matches(reg) ? returnValue : null;
        }
        return returnValue;
    }

    /**
     * 删除文件
     *
     * @param path
     * @return
     */
    public static boolean removeFile(String path) {
        if (StringUtils.isBlank(path)) {
            return false;
        }
        return removeFile(new File(path));
    }

    public static boolean removeFile(File file) {
        if (file != null && file.exists() && file.isFile()) {

            return file.delete();
        }
        return false;
    }
}
