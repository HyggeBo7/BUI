package top.dearbo.common.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import top.dearbo.common.log.Logger;

/**
 * Created by Bo on 2017/7/25.
 * 网络判断工具类
 */

public class NetworkUtil {

    /**
     * 打开网络设置界面
     */
    public static void openWirelessSettings(Activity activity) {
        activity.startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    //网络判断
    public static boolean isNetworkAvailable(Activity activity) {

        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        ConnectivityManager connectivityManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager == null) {
            return false;
        } else {
            // 获取NetworkInfo对象
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();

            if (networkInfo != null && networkInfo.length > 0) {
                for (int i = 0; i < networkInfo.length; i++) {
                    Logger.dTag(i + "===状态===", String.valueOf(networkInfo[i].getState()));
                    Logger.dTag(i + "===类型===", networkInfo[i].getTypeName());
                    // 判断当前网络状态是否为连接状态
                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

}
