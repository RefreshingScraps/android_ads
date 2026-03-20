package com.example.ads;

import static com.example.ads.SplashActivity.goToMainActivity;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.example.ads.ads.*;

public class loadAdByType {
    public static AdPlatform getAdPlatform(@NonNull String adPlatform){
        switch (adPlatform){
            case "ADMOB":
                return AdPlatform.ADMOB;
            default:
                throw new IllegalArgumentException("非法参数：" + adPlatform);
        }
    }
    public static void initSDKByAdPlatform(Context context, @NonNull AdPlatform adPlatform){
        switch (adPlatform) {
            case ADMOB:
                AdMob.initAdMobSDK(context);
                break;
        }
    }
    public static void loadSplashAd(Context context, @NonNull AdPlatform selectedPlatform, ViewGroup SplashAdContainer){
        if(!Boolean.TRUE.equals(Init.adSDKisLoaded.get(selectedPlatform))) {
            initSDKByAdPlatform(context, selectedPlatform);
        }
        switch (ScreenOrientation.getScreenOrientation(context)){
            case Configuration.ORIENTATION_PORTRAIT:
                switch (selectedPlatform) {
                    case ADMOB:
                        AdMob.AdMobSplashAd((Activity) context, AdId.AdMobId.SPLASH_ID);
                        break;
                    default:
                        goToMainActivity(context);
                        break;
                }
                break;
            case Configuration.ORIENTATION_LANDSCAPE:
                switch (selectedPlatform){
                    default:
                        goToMainActivity(context);
                        break;
                }
                break;
            default:
                goToMainActivity(context);
                break;
        }
    }
}