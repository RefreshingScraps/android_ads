package com.example.ads.ads;

import static com.example.ads.DevUtil.*;
import android.content.Context;

import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.beizi.fusion.AdListener;
import com.beizi.fusion.SplashAd;
import com.beizi.fusion.BeiZis;
import com.example.ads.AdPlatform;
import com.example.ads.Init;
import com.example.ads.SplashActivity;
public class BeiZiAd {
    public static void InitBeiZiSDK(Context context,String appId){
        //建议在application里面调用；假如App有功能引导，也可点击"立即体验"按钮中调用
        BeiZis.init(context, appId);
        Init.adSDKisLoaded.put(AdPlatform.BEIZI, true);
    }
    private static SplashAd splashAd;
    public static void BeiziSplashAd(Context context, String GroupId, @NonNull ViewGroup adContainer){
        //跳过按钮传null
        splashAd = new SplashAd(context, null, GroupId, new AdListener() {

            /**
             * 广告加载成功
             */
            @Override
            public void onAdLoaded() {
                i("BeiZisDemo", "onAdLoaded");
                if (splashAd != null) {
                    splashAd.show(adContainer);
                }
            }

            @Override
            public void onAdShown() {
                SplashActivity.onSplashAdLoaded(context, adContainer);
                i("BeiZisDemo", "onAdShown");
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                i("BeiZisDemo", "onAdFailedToLoad:" + errorCode);
                SplashActivity.goToMainActivity(context);
            }

            /**
             * 广告关闭
             */
            @Override
            public void onAdClosed() {
                i("BeiZisDemo", "onAdClosed");
                SplashActivity.goToMainActivity(context);
            }

            /**
             * 倒计时回调，返回广告还将被展示的剩余时间。
             * @param millisUnitFinished 单位是毫秒，转换成秒需除以1000（如：Math.round(millisUnitFinished / 1000f)）
             */
            @Override
            public void onAdTick(long millisUnitFinished) {
            }

            /**
             * 广告点击
             */
            @Override
            public void onAdClicked() {
                i("BeiZisDemo", "onAdClick");
            }
        }, 5000);//广告请求超时时长，建议5000毫秒,该参数单位为ms
        //第一个参数是广告宽度，第二个参数是广告高度，单位是dp，
        //按照实际的容器宽度和高度传递，默认屏幕的宽度和高度，
        //如需添加底部logo view ，则传递的高度为屏幕高度减去logo view的高度
        splashAd.loadAd(adContainer.getWidth(),adContainer.getHeight());
    }
}
