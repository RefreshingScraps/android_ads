package com.example.ads.ads;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import static com.example.ads.DevUtil.*;
import android.view.ViewGroup;

import com.alimm.tanx.core.TanxInitListener;
import com.alimm.tanx.core.ad.ITanxAd;
import com.alimm.tanx.core.ad.ad.template.rendering.splash.ITanxSplashExpressAd;
import com.alimm.tanx.core.ad.listener.ITanxAdLoader;
import com.alimm.tanx.core.config.TanxConfig;
import com.alimm.tanx.core.request.TanxAdLoadType;
import com.alimm.tanx.core.request.TanxAdSlot;
import com.alimm.tanx.core.request.TanxError;
import com.alimm.tanx.ui.TanxSdk;
import com.example.ads.AdPlatform;
import com.example.ads.Init;

import java.util.List;

public class TanxAd {
    private static final String TAG = "";
    public static void initTanxSDK(Activity activity, String appId, String appKey) {
        TanxConfig config = new TanxConfig.Builder()
                .appId(appId)
                .appKey(appKey)
                //.oaid("new oaid")
                .oaidSwitch(true)
                //.logStatus(LogStatus.LOG_ALL_CLOSE)
                .netDebug(isDebug())
                .saveRequestJson(isDebug()) //针对广告无填充问额，用于debug保存请求json来进行问题排查
                //.imageLoader(new MyImageLoader())
                .setEnableSensor(true)
                .build();


        TanxSdk.init(activity.getApplication(), config, new TanxInitListener() {
            @Override
            public void succ() {
                e(TAG, "InitListener succ");
                Init.adSDKisLoaded.put(AdPlatform.TANX, true);
            }

            @Override
            public void error(int code, String msg) {
                e(TAG, "InitListener error:" + msg);
            }

        });

    }
    public static void TanxSplashAd(Context context, String pid, ViewGroup splash_container){
        //>2.3.6版本使用
        TanxAdSlot adSlot = new TanxAdSlot.Builder()
                .adCount(1)
                .pid(pid)
                //2.9.2版本新增，主要用于控制反馈弹窗的开启和关闭，true开启，false关闭，默认开启
                .setFeedBackDialog(true)
                //3.1.1版本新增，支持开屏广告自动缓存，建议使用TanxAdLoadType.PRELOAD
                .setLoadType(TanxAdLoadType.PRELOAD)
                .build();
        ITanxAdLoader iTanxAdLoader = TanxSdk.getSDKManager().createAdLoader(context );
        iTanxAdLoader.loadSplashAd(adSlot, new ITanxAdLoader.OnAdLoadListener<>() {
            @Override
            public void onLoaded(List<ITanxSplashExpressAd> adList) {
                ITanxSplashExpressAd iTanxSplashExpressAd = adList.get(0);
                splash_container.addView(iTanxSplashExpressAd.getAdView(), new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                iTanxSplashExpressAd.setOnSplashAdListener(new ITanxSplashExpressAd.OnSplashAdListener() {
                    @Override
                    public void onAdRender(ITanxSplashExpressAd splashExpressAd) {
                        //3.2.0版本生效，如有性能要求，可在此方法回调后进行广告addview
                    }

                    @Override
                    public void onAdClicked() {
                        //isAdClicked = true;
                    }

                    @Override
                    public void onAdShake() {

                    }

                    @Override
                    public void onAdShow() {

                    }

                    @Override
                    public void onAdClosed() {
                        //goToHomePage();
                    }

                    @Override
                    public void onAdFinish() {
                        //isAdFinished = true;
                        //goToHomePage();
                    }

                    @Override
                    public void onShowError(TanxError error) {
                        //goToHomePage();

                    }

                    @Override
                    public void onExposureCommitSuccess(ITanxAd iTanxAd) {

                    }

                    @Override
                    public void onClickCommitSuccess(ITanxAd iTanxAd) {

                    }
                });

            }

            @Override
            public void onTimeOut() {
                //goToHomePage();
            }

            @Override
            public void onError(TanxError error) {
                //goToHomePage();
            }
        }, 5000);

    }

}
