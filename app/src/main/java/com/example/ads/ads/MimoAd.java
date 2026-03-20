package com.example.ads.ads;
import android.content.Context;
import android.os.Build;
import android.view.ViewGroup;

import com.example.ads.AdPlatform;
import com.example.ads.Init;
import com.miui.zeus.mimo.sdk.MimoSdk;
import com.miui.zeus.mimo.sdk.ADParams;
import static com.example.ads.DevUtil.*;
import com.miui.zeus.mimo.sdk.SplashAd;
import com.miui.zeus.mimo.sdk.SplashAd.SplashAdLoadListener;
import com.miui.zeus.mimo.sdk.SplashAd.SplashAdInteractionListener;
import com.example.ads.SplashActivity;
public class MimoAd {
    private static final String TAG = "米盟广告 SDK";
    private static boolean isXiaomiDevice = false;
    public static void initMimoSDK(Context context){
        //这个字符串可以自己定义,例如判断华为就填写huawei,魅族就填写meizu
        if ("xiaomi".equalsIgnoreCase(Build.MANUFACTURER)) {
            isXiaomiDevice = true;
            MimoSdk.init(context, new MimoSdk.InitCallback() {

                @Override
                public void success() {
                    i(TAG,"SDK初始化成功");
                    Init.adSDKisLoaded.put(AdPlatform.MIMO, true);
                }

                @Override
                public void fail(int code, String msg) {
                    e(TAG,"SDK初始化失败，错误信息："+msg+"("+code+")");
                }
            });
        } else {
            i(TAG, "非小米设备，程序结束。");
        }
    }
    
    public static void MimoSplashAd(Context context,String upId,ViewGroup container){
        if(isXiaomiDevice) {
            SplashAd splashAd = new SplashAd();
            ADParams params = new ADParams.Builder().setUpId(upId).build();
            splashAd.loadAd(params, new SplashAdLoadListener() {

                @Override
                public void onAdRequestSuccess() {
                    i(TAG, "广告请求成功");
                    // 广告请求成功
                }

                @Override
                public void onAdLoaded() {
                    // 广告加载成功，在需要的时候在此处展示广告

                    splashAd.showAd(container, new SplashAdInteractionListener() {

                        @Override
                        public void onAdShow() {
                            // 广告展示
                            i(TAG, "广告曝光");
                            SplashActivity.onSplashAdLoaded(context, container);
                        }

                        @Override
                        public void onAdClick() {
                            // 广告被点击
                            i(TAG, "广告被点击");
                        }

                        @Override
                        public void onAdDismissed() {

                            i(TAG, "广告被关闭");
                            // 点击关闭按钮广告消失回调
                            SplashActivity.goToMainActivity(context);
                        }

                        @Override
                        public void onAdRenderFailed(int errorCode, String errorMsg) {
                            //广告渲染失败
                            //container.setVisibility(View.GONE);
                            e(TAG, "广告渲染失败，错误信息：" + errorMsg + "(" + errorCode + ")");
                            SplashActivity.goToMainActivity(context);
                        }
                    });
                }

                @Override
                public void onAdLoadFailed(int errorCode, String errorMsg) {
                    // 广告加载失败
                    e(TAG, "广告加载失败，错误信息：" + errorMsg + "(" + errorCode + ")");
                    SplashActivity.goToMainActivity(context);
                }
            });
        } else {
            i(TAG, "非小米设备，程序结束。");
            SplashActivity.goToMainActivity(context);
        }
    }
}
