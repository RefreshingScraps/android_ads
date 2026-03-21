package com.example.ads.ads;

import static com.example.ads.DevUtil.*;
import android.app.Application;
import android.app.Activity;

import android.view.ViewGroup;

import com.example.ads.AdPlatform;
import com.example.ads.Init;
import com.kc.openset.config.OSETSDK;
import com.kc.openset.listener.OSETInitListener;
import com.kc.openset.ad.listener.OSETSplashAdLoadListener;
import com.kc.openset.ad.listener.OSETSplashListener;
import com.kc.openset.ad.splash.OSETSplash;
import com.kc.openset.ad.splash.OSETSplashAd;
import com.example.ads.SplashActivity;
public class OSETAd {
    private static final String TAG = "OSET广告 SDK";
    public static void initOSETSDK(Application application, String APPKEY){
        OSETSDK.getInstance()
//                .setCustomController(new OSETCustomController(){})
                .init(application, APPKEY, new OSETInitListener(){
                    @Override
                    public void onError(String s) {
                        e(TAG,"SDK初始化失败，错误信息："+s);
                    }

                    @Override
                    public void onSuccess() {
                        i(TAG,"SDK初始化成功");
                        Init.adSDKisLoaded.put(AdPlatform.OSET, true);
                    }
                });
    }
    
    public static void OSETSplashAd(Activity activity,String PosId,ViewGroup adContainer) {
        OSETSplashAdLoadListener osetSplashAdLoadListener ;
        osetSplashAdLoadListener = new OSETSplashAdLoadListener() {

            @Override
            public void onLoadFail(String s, String s1) {
                e(TAG,"广告加载失败，错误信息："+s1+"("+s+")");
                SplashActivity.goToMainActivity(activity.getApplicationContext());
            }

            @Override
            public void onLoadSuccess(OSETSplashAd osetSplashAd) {
                osetSplashAd.showAd(activity, adContainer, new OSETSplashListener(){

                    @Override
                    public void onError(String s, String s1) {
                        e(TAG,"广告加载失败，错误信息："+s1+"("+s+")");
                        SplashActivity.goToMainActivity(adContainer.getContext());
                    }

                    @Override
                    public void onAdDetailViewClosed() {
                        SplashActivity.goToMainActivity(adContainer.getContext());
                    }

                    @Override
                    public void onClick() {
                        i(TAG,"广告被点击");
                    }

                    @Override
                    public void onClose() {
                        i(TAG,"广告关闭");
                        SplashActivity.goToMainActivity(adContainer.getContext());
                    }

                    @Override
                    public void onShow() {
                        i(TAG,"广告展示");
                        SplashActivity.onSplashAdLoaded(activity.getApplicationContext(), adContainer);
                    }
                });
            }

        };
        OSETSplash.getInstance()
                .setContext(activity)
                .setPosId(PosId)
                .loadAd(osetSplashAdLoadListener);

    }
}
