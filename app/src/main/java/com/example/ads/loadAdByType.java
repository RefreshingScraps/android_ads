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
            case "BAIDU":
                return AdPlatform.BAIDU;
            case "BEIZI":
                return AdPlatform.BEIZI;
            case "CSJ":
                return AdPlatform.CSJ;
            case "GDT":
                return AdPlatform.GDT;
            case "HW":
                return AdPlatform.HW;
            case "KS":
                return AdPlatform.KS;
            default:
                throw new IllegalArgumentException("非法参数：" + adPlatform);
        }
    }
    public static void initSDKByAdPlatform(Context context, @NonNull AdPlatform adPlatform){
        switch (adPlatform) {
            case ADMOB:
                AdMob.initAdMobSDK(context);
                break;
            case BAIDU:
                BaiduAd.InitBaiduSDK(context, AdId.BaiduId.APP_ID);
                break;
            case BEIZI:
                BeiZiAd.InitBeiZiSDK(context, "");
                break;
            case CSJ:
                CsjAd.InitCsjSDK(context, AdId.CsjId.APP_ID, context.getString(R.string.app_name));
                break;
            case GDT:
                GDTAd.InitGDTSDK(context, AdId.GDTId.APP_ID);
                break;
            case HW:
                HwAd.InitHwSDK(context);
                break;
            case KS:
                KsAd.initKSSDK(context, AdId.KsId.APP_ID, context.getString(R.string.app_name));
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
                    case BAIDU:
                        BaiduAd.BaiduSplashAd(context, AdId.BaiduId.SPLASH_ID, SplashAdContainer);
                        break;
                    case BEIZI:
                        BeiZiAd.BeiziSplashAd(context, AdId.BeiziId.SPLASH_ID , SplashAdContainer);
                        break;
                    case CSJ:
                        CsjAd.CsjSplashAd(context, AdId.CsjId.SPLASH_ID, SplashAdContainer);
                        break;
                    case GDT:
                        GDTAd.GDTSplashAd(context, AdId.GDTId.SPLASH_ID, SplashAdContainer);
                        break;
                    case HW:
                        HwAd.HwSplashAd(context, AdId.HwId.SPLASH_ID_PORTRAIT, SplashAdContainer.findViewById(R.id.splash_ad_view));
                        break;
                    case KS:
                        KsAd.KSSplashAd(context, AdId.KsId.SPLASH_ID, SplashAdContainer);
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