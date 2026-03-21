package com.example.ads;

import static com.example.ads.SplashActivity.goToMainActivity;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.util.Log;
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
            case "SIGMOB":
                return AdPlatform.SIGMOB;
            case "MIMO":
                return AdPlatform.MIMO;
            case "MS":
                return AdPlatform.MS;
            case "OCTOPUS":
                return AdPlatform.OCTOPUS;
            case "TAPTAP":
                return AdPlatform.TAPTAP;
            case "OSET":
                return AdPlatform.OSET;
            case "QIMING":
                return AdPlatform.QIMING;
            case "JD":
                return AdPlatform.JD;
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
            case SIGMOB:
                SigmobAd.initSigmobSDK(context, AdId.SigmobId.APP_ID, AdId.SigmobId.APP_KEY);
                break;
            case MIMO:
                MimoAd.initMimoSDK(context);
                break;
            case MS:
                MsAd.initMsAdSDK(context, AdId.MSId.APP_ID);
                break;
            case OCTOPUS:
                OctopusAd.InitOctopusSDK(context, AdId.OctopusId.APP_ID);
                break;
            case TAPTAP:
                TaptapAd.InitTaptapSDK(context, AdId.TaptapId.MEDIA_ID, AdId.TaptapId.MEDIA_NAME, AdId.TaptapId.MEDIA_KEY);
                break;
            case QIMING:
                QiMingAd.InitQiMingSDK(context, "100002");//包名不对
                break;
            case OSET:
                OSETAd.initOSETSDK((Application) context.getApplicationContext(), AdId.OpenSetId.APP_KEY);
                break;
            case JD:
                JdAd.initJadSDK(context, AdId.JdId.APP_ID);
                break;
            case TANX:
                TanxAd.initTanxSDK((Activity) context, AdId.TanxId.APP_ID, AdId.TanxId.APP_KEY);
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
                    case SIGMOB:
                        // Sigmob ID提供：https://github.com/gstory0404/sigmobad
                        SigmobAd.SigmobSplashAd(context, AdId.SigmobId.SPLASH_ID, null, null, SplashAdContainer);
                        break;
                    case MIMO:
                        MimoAd.MimoSplashAd(context, AdId.MimoId.SPLASH_ID, SplashAdContainer);
                        break;
                    case MS:
                        MsAd.MsSplashAd(context, AdId.MSId.SPLASH_ID, SplashAdContainer);
                        break;
                    case TAPTAP:
                        TaptapAd.TaptapSplashAd(context, AdId.TaptapId.horizontal.SPLASH_ID, (Activity) context, SplashAdContainer);
                        break;
                    case OCTOPUS:
                        OctopusAd.OctopusSplashAd(context, AdId.OctopusId.SPLASH_ID, SplashAdContainer);
                        break;
                    case OSET:
                        OSETAd.OSETSplashAd((Activity) context, AdId.OpenSetId.SPLASH_ID, SplashAdContainer);
                        break;
                    case JD:
                        JdAd.JadSplashAd(context, AdId.JdId.ESPLASH_ID, JdAd.getWidth(context), JdAd.getHeight(context), SplashAdContainer);
                        break;
                    //case TANX:
                    //    TanxAd.TanxSplashAd(context, "TODO tanx广告位", SplashAdContainer);
                    //    break;
                    //case QIMING:
                    //    QiMingAd.QiMingSplashAd(context, "TODO QiMing广告位", SplashAdContainer);
                    //    break;
                    default:
                        goToMainActivity(context);
                        break;
                }
                break;
            case Configuration.ORIENTATION_LANDSCAPE:
                switch (selectedPlatform){
                    case MIMO:
                        MimoAd.MimoSplashAd(context, AdId.MimoId.SPLASH_ID, SplashAdContainer);
                        break;
                    case TAPTAP:
                        TaptapAd.TaptapSplashAd(context, AdId.TaptapId.vertical.SPLASH_ID, (Activity) context, SplashAdContainer);
                        break;
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
    public static void loadFeedAd(@NonNull Activity activity,@NonNull ViewGroup feedAdContainer,@NonNull AdPlatform adPlatform){
        //feedAdContainer.setBackgroundColor(getResources().getColor(R.color.gray));
        if(Boolean.FALSE.equals(Init.adSDKisLoaded.get(adPlatform))) {
            initSDKByAdPlatform(activity, adPlatform);
        }
        switch (adPlatform) {
            case CSJ:
                CsjAd.CsjFeedAd(feedAdContainer.getContext(), AdId.CsjId.NATIVE_RECYCLERVIEW_ID, feedAdContainer);
                break;
            case BAIDU:
                BaiduAd.BaiduFeedAd(feedAdContainer.getContext(), AdId.BaiduId.NATIVE_SIMPLE_ID, feedAdContainer);
                break;
            case GDT:
                GDTAd.GDTFeedAd(feedAdContainer.getContext(), AdId.GDTId.NATIVE_EXPRESS_ID_PICTURE_VIDEO, feedAdContainer);
                break;
            case TAPTAP:
                TaptapAd.TaptapNativeAd(activity, AdId.TaptapId.FEED.PICTURE_ID, feedAdContainer);
                break;
            case SIGMOB:
                SigmobAd.SigmobNativeAd(feedAdContainer.getContext(), AdId.SigmobId.FEED_ID, null, feedAdContainer);
                break;
            case MS:
                MsAd.MsRecyclerMixAdAd(activity, AdId.MSId.FEED_ID, feedAdContainer);
                break;
            case KS:
                KsAd.KsFeedAd(AdId.KsId.FEED_ID, feedAdContainer);
                break;
            case OCTOPUS:
                OctopusAd.OctopusNativeAd(feedAdContainer.getContext(), AdId.OctopusId.NATIVE_RECYCLERVIEW_ID, feedAdContainer);
                break;
            default:
                Log.i("广告", "没有选中广告");
                break;
            //        FeedAds.HwAd(feedAdContainer.getContext(), AppAdId.HwId.NATIVE_ID_SMALL,adItem.HwNativeAdContainer);
        }
    }
}