package com.example.ads.ads;

import android.app.Activity;
import android.content.Context;
import static com.example.ads.DevUtil.*;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.example.ads.AdPlatform;
import com.example.ads.Init;
import com.example.ads.SplashActivity;
import com.jd.ad.sdk.bl.initsdk.JADInitCallback;
import com.jd.ad.sdk.bl.initsdk.JADPrivateController;
import com.jd.ad.sdk.bl.initsdk.JADYunSdk;
import com.jd.ad.sdk.bl.initsdk.JADYunSdkConfig;
import com.jd.ad.sdk.dl.addata.JADMaterialData;
import com.jd.ad.sdk.dl.model.JADSlot;
import com.jd.ad.sdk.fdt.utils.ScreenUtils;
import com.jd.ad.sdk.multi.BuildConfig;
import com.jd.ad.sdk.nativead.JADNative;
import com.jd.ad.sdk.nativead.JADNativeLoadListener;
import com.jd.ad.sdk.nativead.JADNativeSplashInteractionListener;
import com.jd.ad.sdk.nativead.JADNativeWidget;

import java.util.ArrayList;
import java.util.List;

public class JdAd {
    private static final String TAG = "京媒广告 SDK";
    public static int getWidth(Context context){
        return ScreenUtils.getPhoneWidth(context);
    }
    public static int getHeight(Context context){
        return ScreenUtils.getPhoneHeight(context);
    }
    public static void initJadSDK(Context context,String APP_ID){
        //初始化SDK配置
        JADYunSdkConfig config = new JADYunSdkConfig
                .Builder()
                .setAppId(APP_ID) //媒体在平台申请的 APP ID
                .setEnableLog(isDebug()) //测试阶段打开，可以通过日志排查问题，上线时去除该调用
                .setPrivateController(new JADPrivateController() {
                    @Override
                    public String getOaid() {
                        return "";
                    }
                }) //隐私信息控制设置，此项必须设置！！
                .setSupportMultiProcess(true)//是否支持多进程，true表示支持，默认不支持,若不支持多进程场景，无需设置该配置
                .build();
    
        //初始化SDK
        //JADYunSdk.init(context, config);
        //同步初始化SDK
        final boolean[] initstatus = new boolean[2];
        JADYunSdk.syncInit(context, config, new JADInitCallback() {
            /**
             * 初始化成功
             */
            @Override
            public void onInitSuccess() {
                i(TAG, "SDK同步初始化成功");
                initstatus[0] = true;
                if(initstatus[1]){
                    Init.adSDKisLoaded.put(AdPlatform.JD, true);
                }
            }
            /**
             * 初始化失败
             */
            @Override
            public void onInitFailure(int code, String msg) {
                e(TAG, "SDK同步初始化失败，错误信息："+msg+"("+code+")");
            }
        });
    
        //异步初始化SDK
        JADYunSdk.asyncInit(context, config, new JADInitCallback() {
            /**
             * 初始化成功
             */
            @Override
            public void onInitSuccess() {
                i(TAG, "SDK异步初始化成功");
                initstatus[1] = true;
                if(initstatus[0]){
                    Init.adSDKisLoaded.put(AdPlatform.JD, true);
                }
            }
            /**
             * 初始化失败
             */
            @Override
            public void onInitFailure(int code, String msg) {
                e(TAG, "SDK异步初始化失败，错误信息："+msg+"("+code+")");
            }
        });
    }

    public static void JadSplashAd(Context context,String slotID,float expressImageWidth,float expressImageHeight,ViewGroup adView){
        JADSlot slot = new JADSlot.Builder()
                .setSlotID(slotID)
                .setImageSize(expressImageWidth, expressImageHeight)
                .setAdType(JADSlot.AdType.SPLASH)
                .build();
        JADNative mJADNative = getJadNative(context, slot);
        // 摇一摇组件,返回大小为（100dp,100dp）的View，当View attachedToWindow时，动画start，detachedFromWindow时，动画end
        View shakeAnimationView = JADNativeWidget.getShakeAnimationView(context);

          // 滑动组件，返回大小为（matchParent，120dp）的View，当View attachedToWindow时，动画start，detachedFromWindow时，动画end
        View swipeAnimationView = JADNativeWidget.getSwipeAnimationView(context);
        //可点击View列表
        List<View> clickList = new ArrayList<>();
          //        clickList.add(imageView);
        clickList.add(shakeAnimationView);
        clickList.add(swipeAnimationView);

      // 关闭View列表
  //        View skipBtn = adView.findViewById(R.id.jad_splash_skip_btn);
        List<View> closeList = new ArrayList<>();
  //        closeList.add(skipBtn);

  // 注册需要监听的视图，包括整体的广告View、点击视图列表、关闭视图列表
        mJADNative.registerNativeView((Activity) context, adView, clickList, closeList,
                new JADNativeSplashInteractionListener() {

                    /**
                     * 广告曝光
                     */
                    @Override
                    public void onExposure() {
                        // TODO 广告曝光上报
                        i(TAG, "广告曝光");
                        SplashActivity.onSplashAdLoaded(context, adView);
                    }

                    /**
                     * 广告倒计时,
                     *
                     * @param time 倒计时当前数字
                     */
                    @Override
                    public void onCountdown(int time) {
                        // TODO：关于倒计时视图刷新可在这个回调中进行操作

                    }
                    /**
                     * 广告点击
                     */
                    @Override
                    public void onClick(View view) {
                        // TODO 广告点击上报

                    }

                    /**
                     * 广告关闭
                     */
                    @Override
                    public void onClose(View view) {
                        SplashActivity.goToMainActivity(context);
                    }
                });
        mJADNative.destroy();
    }

    @NonNull
    private static JADNative getJadNative(Context context, JADSlot slot) {
        JADNative mJADNative = new JADNative(slot);
        mJADNative.loadAd(new JADNativeLoadListener() {

            /**
             * 广告数据加载成功
             */
            @Override
            public void onLoadSuccess() {
                // TODO：广告数据返回上报
            }

            /**
             * 广告数据加载失败
             *
             * @param code  错误码
             * @param error 错误描述信息
             */
            @Override
            public void onLoadFailure(int code, String error) {
                SplashActivity.goToMainActivity(context);
            }
        });
        List<JADMaterialData> adList = mJADNative.getDataList();
        return mJADNative;
    }
}
