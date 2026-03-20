package com.example.ads.ads;

import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import static com.example.ads.DevUtil.*;

import com.huawei.hms.ads.ExSplashService;

public final class Hw_ExSplashServiceConnection implements ServiceConnection {

    private static final String TAG = "ExSplashServiceConnection";

    private final Context context;

    public Hw_ExSplashServiceConnection(Context context) {
        this.context = context;
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        i(TAG, "onServiceConnected");
        ExSplashService exSplashService = ExSplashService.Stub.asInterface(service);
        if (exSplashService != null) {
            try {
                // 是否同意用户协议，同意为true，不同意为false
                exSplashService.enableUserInfo(true);
            } catch (RemoteException e) {
                i(TAG, "enableUserInfo error");
            } finally {
                context.unbindService(this);
            }
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        i(TAG, "onServiceDisconnected");
    }
}