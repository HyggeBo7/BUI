package top.dearbo.common.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;

/**
 * Created by Bo on 2019/1/16 18:07
 */
public class SharedUtil {

    private static final String FileKey = "system_shared_key";

    private SharedPreferences.Editor edit;

    private SharedPreferences sharedPreferences;

    public SharedUtil(Context context) {
        this(context, FileKey);
    }

    @SuppressLint("CommitPrefEdits")
    public SharedUtil(Context context, String fileKey) {
        this.sharedPreferences = context.getSharedPreferences(fileKey, Context.MODE_PRIVATE);
        this.edit = this.sharedPreferences.edit();
    }

    public SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

    public Boolean getBooleanValue(String key) {
        return getBooleanValue(key, false);
    }

    public Boolean getBooleanValue(String key, boolean defValue) {
        return sharedPreferences.getBoolean(key, defValue);
    }

    public String getStringValue(String key) {
        return getStringValue(key, null);
    }

    public String getStringValue(String key, String defValue) {
        return sharedPreferences.getString(key, defValue);
    }

    public int getIntValue(String key, Integer defValue) {
        return sharedPreferences.getInt(key, defValue);
    }

    public float getFloatValue(String key, Float defValue) {
        return sharedPreferences.getFloat(key, defValue);
    }

    public float getLongValue(String key, Long defValue) {
        return sharedPreferences.getLong(key, defValue);
    }

    public Map<String, ?> getAllValue() {
        return sharedPreferences.getAll();
    }

    public boolean save(String key, Object param) {
        if (param instanceof Integer) {
            return edit.putInt(key, Integer.parseInt(String.valueOf(param))).commit();
        } else if (param instanceof Double) {
            return edit.putFloat(key, Float.parseFloat(String.valueOf(param))).commit();
        } else if (param instanceof Float) {
            return edit.putFloat(key, Float.parseFloat(String.valueOf(param))).commit();
        } else if (param instanceof Long) {
            return edit.putLong(key, Long.parseLong(String.valueOf(param))).commit();
        } else if (param instanceof Boolean) {
            return edit.putBoolean(key, Boolean.parseBoolean(String.valueOf(param))).commit();
        }
        return edit.putString(key, String.valueOf(param)).commit();
    }

    public boolean delete(String key) {
        return edit.remove(key).commit();
    }

    public boolean clear() {

        return edit.clear().commit();
    }
}
