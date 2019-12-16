package com.dan.html.plugin;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.dan.common.entity.AjaxResult;
import com.dan.html.util.JSUtils;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.dcloud.common.DHInterface.IApp;
import io.dcloud.common.DHInterface.ISysEventListener;
import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.DHInterface.StandardFeature;

/**
 * @version 1.0
 * @author: Bo
 * @fileName: BluePlugin
 * @createDate: 2019-12-11 16:26.
 * @description: 打开蓝牙, 获取已配对列表
 */
public class BluePlugin extends StandardFeature {

    private final static String TAG = "BluePlugin";

    private final static int REQUEST_CODE_OPEN = 777;

    private BluetoothAdapter myBluetoothAdapter = null;

    @Override
    public void onStart(Context context, Bundle bundle, String[] strings) {
        super.onStart(context, bundle, strings);
        this.myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    //是否支持蓝牙
    public String isAvailable(IWebview iWebview, JSONArray jsonArray) {

        return JSUtils.wrapJsVar(isAvailable());
    }

    //打开蓝牙-(异步回调)
    public void openBluetooth(final IWebview iWebview, JSONArray jsonArray) {
        final String callbackId = jsonArray.optString(0);
        if (!isAvailable()) {
            JSUtils.execCallbackOk(iWebview, callbackId, new AjaxResult(0, "该设备不支持蓝牙!", jsonArray));
            return;
        }
        if (!isOpen()) {
            final IApp iApp = iWebview.obtainFrameView().obtainApp();
            iApp.registerSysEventListener(new ISysEventListener() {
                @Override
                public boolean onExecute(SysEventType pEventType, Object pArgs) {
                    Object[] _args = (Object[]) pArgs;
                    int requestCode = (Integer) _args[0];
                    int resultCode = (Integer) _args[1];
                    //Intent data = (Intent) _args[2];
                    if (pEventType == SysEventType.onActivityResult) {
                        iApp.unregisterSysEventListener(this, SysEventType.onActivityResult);
                        if (requestCode == REQUEST_CODE_OPEN) {
                            //resultCode==0为拒绝,-1是允许
                            //System.out.println("registerSysEventListener=====>requestCode:" + requestCode + ",resultCode:" + resultCode);
                            JSUtils.execCallbackOkData(iWebview, callbackId, resultCode != 0);
                        }
                    }
                    return false;
                }
            }, SysEventType.onActivityResult);
            //判断蓝牙是否开启
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            iWebview.getActivity().startActivityForResult(intent, REQUEST_CODE_OPEN);
        } else {
            JSUtils.execCallbackOk(iWebview, callbackId, new AjaxResult(true), false);
        }
    }

    /**
     * 获取蓝牙设备配对列表
     *
     * @return json
     */
    public String getPairDeviceList(IWebview iWebview, JSONArray jsonArray) {
        return JSUtils.wrapJsVar(getPairDeviceList());
    }

    //界面插件判断蓝牙是否已经开启
    public String isOpen(IWebview iWebview, JSONArray jsonArray) {
        return JSUtils.wrapJsVar(isOpen());
    }

    /////////////////////////私有方法(private)///////////////////////////////

    //是否支持蓝牙
    private boolean isAvailable() {

        return myBluetoothAdapter != null;
    }

    //判断蓝牙是否已经开启
    private boolean isOpen() {
        if (myBluetoothAdapter != null) {
            return myBluetoothAdapter.isEnabled();
        }
        return false;
    }

    //获取蓝牙设备配对列表
    private List<Map<String, Object>> getPairDeviceList() {
        List<Map<String, Object>> bluetoothEntityList = new ArrayList<>();
        if (isOpen()) {
            Set<BluetoothDevice> bondedDevices = myBluetoothAdapter.getBondedDevices();
            if (bondedDevices.size() < 1) {
                return bluetoothEntityList;
            }
            Map<String, Object> bluetoothMap;
            for (BluetoothDevice device : bondedDevices) {
                bluetoothMap = new HashMap<>();
                bluetoothMap.put("name", device.getName());
                bluetoothMap.put("address", device.getAddress());
                bluetoothMap.put("type", device.getType());
                bluetoothEntityList.add(bluetoothMap);
            }
            return bluetoothEntityList;
        }
        return bluetoothEntityList;
    }

}
