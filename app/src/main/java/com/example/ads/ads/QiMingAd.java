package com.example.ads.ads;

import android.content.Context;
import android.view.ViewGroup;
import android.view.View;

import com.example.ads.AdPlatform;
import com.example.ads.Init;
import com.ja.adx.qiming.ad.bean.SplashAdInfo;
import com.ja.adx.qiming.ad.error.Error;
import com.ja.adx.qiming.QiMingADXSDK;
import com.ja.adx.qiming.config.InitConfig;
import static com.example.ads.DevUtil.*;
import com.example.ads.SplashActivity;
public class QiMingAd {
    private static final String TAG = "QiMingADX SDK";
    public static void InitQiMingSDK(Context context,String appId){
        // 初始化QiMingADX广告SDK
        QiMingADXSDK.getInstance().init(context, new InitConfig.Builder()
                // 设置AppId，必须的
                .appId(appId)
                // 是否开启Debug，开启会有详细的日志信息打印
                // 注意上线后请置为false
                .debug(isDebug())
                .build());
        Init.adSDKisLoaded.put(AdPlatform.QIMING, true);
    }
    public static void QiMingSplashAd(Context context,String posId,ViewGroup adContainer){
        com.ja.adx.qiming.ad.SplashAd splashAd = new com.ja.adx.qiming.ad.SplashAd(context);
        splashAd.setListener(new com.ja.adx.qiming.ad.listener.SplashAdListener() {
            @Override
            public void onAdTick(long millisUntilFinished) {
                // 倒计时剩余时长（单位：秒）
            }

            @Override
            public void onAdReceive(SplashAdInfo splashAdInfo) {
                // 广告获取成功回调，在此回调中展示广告
                // 获取开屏广告视图
                View view = splashAdInfo.getSplashAdView();
                // 将广告视图添加到容器中，注意容器高度要大于屏幕75%
                adContainer.addView(view);
                // 渲染广告，一定要最后调用
                splashAdInfo.render();
            }

            @Override
            public void onAdExpose(SplashAdInfo splashAdInfo) {
                // 广告展示回调
                SplashActivity.onSplashAdLoaded(context, adContainer);
            }

            @Override
            public void onAdClick(SplashAdInfo splashAdInfo) {
                // 广告点击回调
            }

            @Override
            public void onAdSkip(SplashAdInfo splashAdInfo) {
                // 广告跳过回调
                SplashActivity.goToMainActivity(context);
            }

            @Override
            public void onAdClose(SplashAdInfo splashAdInfo) {
                // 广告关闭回调，可在此处进入应用首页
                SplashActivity.goToMainActivity(context);
            }

            @Override
            public void onAdFailed(Error error) {
                e(TAG,"广告加载失败，错误信息："+error.getError()+"("+error.getCode()+")");
                SplashActivity.goToMainActivity(context);
            }
        });
        splashAd.loadAd(posId);
    }
}
