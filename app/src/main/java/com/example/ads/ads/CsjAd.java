package com.example.ads.ads;

import static com.bytedance.sdk.openadsdk.TTAdLoadType.PRELOAD;

import static com.example.ads.DevUtil.*;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.bytedance.sdk.openadsdk.TTAdConfig;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdSdk;
import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.CSJAdError;
import com.bytedance.sdk.openadsdk.CSJSplashAd;
import com.bytedance.sdk.openadsdk.TTAdNative;

import com.bytedance.sdk.openadsdk.TTNativeExpressAd;
import com.example.ads.AdPlatform;
import com.example.ads.Init;
import com.example.ads.SplashActivity;

import java.util.List;

public class CsjAd {
    private static final String TAG = "穿山甲广告 SDK";
    public static void InitCsjSDK(Context context, String appId, String appName) {
//强烈建议在应用对应的Application#onCreate()方法中调用，避免出现content为null的异常
        TTAdSdk.init(context,new TTAdConfig.Builder()
                    .appId(appId)//xxxxxxx为穿山甲媒体平台注册的应用ID
                    .appName(appName)
                    .titleBarTheme(TTAdConstant.TITLE_BAR_THEME_DARK)//落地页主题
                    .allowShowNotify(true) //是否允许sdk展示通知栏提示,若设置为false则会导致通知栏不显示下载进度
                    .debug(isDebug()) //测试阶段打开，可以通过日志排查问题，上线时去除该调用
                    .directDownloadNetworkType(TTAdConstant.NETWORK_STATE_WIFI) //允许直接下载的网络状态集合,没有设置的网络下点击下载apk会有二次确认弹窗，弹窗中会披露应用信息
                    .supportMultiProcess(false) //是否支持多进程，true支持
                    .build()
            //如果明确某个进程不会使用到广告SDK，可以只针对特定进程初始化广告SDK的content
        );
        TTAdSdk.start(new TTAdSdk.Callback() {
            @Override
            public void success() {
                if(TTAdSdk.isSdkReady()) {
                    i(TAG, "SDK初始化成功");
                    Init.adSDKisLoaded.put(AdPlatform.CSJ, true);
                }
                else{
                    e(TAG, "SDK初始化失败");
                }
            }
            @Override
            public void fail(int code, String msg) {
                e(TAG, "广告加载失败，错误信息：" + msg + "(" + code + ")");
            }
        });
    }
    public static void CsjSplashAd(Context context,String mCodeId,ViewGroup mSplashContainer){
        // sdk初始化完成，可以进行广告加载等后续操作
        //创建TTAdNative对象，createAdNative(Context context) context需要传入Activity对象
//保证每次请求的广告对象为新的广告对象，避免重复使用广告同一个对象进行广告请求
        TTAdNative mTTAdNative = TTAdSdk.getAdManager().createAdNative(context);
        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(mCodeId) //平台创建的代码位ID 以8开头9位数字
                //不区分渲染方式，要求开发者同时设置setImageAcceptedSize（单位：px）和setExpressViewAcceptedSize（单位：dp ）接口，不同时设置可能会导致展示异常。
//                    .setImageAcceptedSize(imageViewWidth, imageViewHeight)
//                    .setExpressViewAcceptedSize(expressViewWidth, expressViewHeight)
                .setAdLoadType(PRELOAD)//推荐使用，用于标注此次的广告请求用途为预加载（当做缓存）还是实时加载，方便后续为开发者优化相关策略
                .build();
        mTTAdNative.loadSplashAd(adSlot, new TTAdNative.CSJSplashAdListener() {
            //5700及以上新增，开屏素材加载成功
            @Override
            public void onSplashLoadSuccess(CSJSplashAd ad) {
                i(TAG, "广告加载成功");
            }

            //加载开屏素材失败
            @Override
            public void onSplashLoadFail(CSJAdError error) {
                //开发者处理跳转到APP主页面逻辑
                e(TAG, "广告加载失败，错误信息：" + error.getMsg() + "(" + error.getCode() + ")");
                SplashActivity.goToMainActivity(context);
            }

            //开屏渲染成功，可以展示开屏
            @Override
            public void onSplashRenderSuccess(CSJSplashAd ad) {
                if (ad == null) {
                    e(TAG, "未获取到广告");
                    SplashActivity.goToMainActivity(context);
                    return;
                }
                if (mSplashContainer != null) {
                    mSplashContainer.removeAllViews();
                    //把SplashView 添加到ViewGroup中,注意开屏广告view：width =屏幕宽；height >=75%屏幕高

                    SplashActivity.onSplashAdLoaded(context, (ViewGroup) ad.getSplashView());
                    ad.setSplashAdListener(new CSJSplashAd.SplashAdListener() {
                        @Override
                        public void onSplashAdShow(CSJSplashAd csjSplashAd) {

                        }

                        @Override
                        public void onSplashAdClick(CSJSplashAd csjSplashAd) {

                        }

                        @Override
                        public void onSplashAdClose(CSJSplashAd csjSplashAd, int i) {
                            SplashActivity.goToMainActivity(context);
                        }
                    });
                    ad.showSplashView(mSplashContainer);

                }else {
                    SplashActivity.goToMainActivity(context);
                    //开发者处理跳转到APP主页面逻辑
                }
            }

            @Override
            public void onSplashRenderFail(CSJSplashAd ad, CSJAdError csjAdError) {
                e(TAG, "广告渲染失败，错误信息：" + csjAdError.getMsg() + "(" + csjAdError.getCode() + ")");
                SplashActivity.goToMainActivity(context);
                //开发者处理跳转到APP主页面逻辑
            }
        }, 5000);
    }
    public static void CsjFeedAd(Context context,String codeId,ViewGroup convertView){
//创建TTAdNative对象，createAdNative(Context context) context需要传入Activity对象
//保证每次请求的广告对象为新的广告对象，避免重复使用广告同一个对象进行广告请求
        convertView.removeAllViews();
        TTAdNative mTTAdNative = TTAdSdk.getAdManager().createAdNative(context);
        com.bytedance.sdk.openadsdk.AdSlot adSlot = new com.bytedance.sdk.openadsdk.AdSlot.Builder()
                .setCodeId(codeId) //广告位id
                .setSupportDeepLink(true)
                .setAdCount(1) //请求广告数量为1到3条
                .setExpressViewAcceptedSize(375,0) //期望模板广告view的size,宽度最低为375，单位dp
                .setAdLoadType(com.bytedance.sdk.openadsdk.TTAdLoadType.PRELOAD)//推荐使用，用于标注此次的广告请求用途为预加载（当做缓存）还是实时加载，方便后续为开发者优化相关策略
                .build();//请求广告
        mTTAdNative.loadNativeExpressAd(adSlot, new TTAdNative.NativeExpressAdListener() {

            //广告请求失败
            @Override
            public void onError(int code, String message) {

            }

            //广告请求成功
            @Override
            public void onNativeExpressAdLoad(List<TTNativeExpressAd> ads) {
                TTNativeExpressAd mTTAd = ads.get(0);
                mTTAd.setExpressInteractionListener(new TTNativeExpressAd.ExpressAdInteractionListener() {

                    //广告点击回调
                    @Override
                    public void onAdClicked(View view, int type) {

                    }

                    //广告展示回调
                    @Override
                    public void onAdShow(View view, int type) {
                    }

                    //广告渲染失败回调
                    @Override
                    public void onRenderFail(View view, String msg, int code) {

                    }

                    //广告渲染成功回调
                    @Override
                    public void onRenderSuccess(View view, float width, float height) {
                        // 当前不建议直接使用 float width, float height
                        convertView.addView(view);
                    }
                });
                mTTAd.render();
            }
        });
    }
}
