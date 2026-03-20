package com.example.ads.ads;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import static com.example.ads.DevUtil.*;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.ads.AdPlatform;
import com.example.ads.Init;
import com.example.ads.SplashActivity;
import com.huawei.hms.ads.AdListener;
import com.huawei.hms.ads.AdParam;
import com.huawei.hms.ads.AudioFocusType;
import com.huawei.hms.ads.BannerAdSize;
import com.huawei.hms.ads.BiddingParam;
import com.huawei.hms.ads.HwAds;
import com.huawei.hms.ads.InterstitialAd;
import com.huawei.hms.ads.MediaMuteListener;
import com.huawei.hms.ads.banner.BannerView;
import com.huawei.hms.ads.instreamad.InstreamAd;
import com.huawei.hms.ads.instreamad.InstreamAdLoadListener;
import com.huawei.hms.ads.instreamad.InstreamAdLoader;
import com.huawei.hms.ads.instreamad.InstreamMediaStateListener;
import com.huawei.hms.ads.instreamad.InstreamView;
import com.huawei.hms.ads.nativead.NativeAdLoader;
import com.huawei.hms.ads.nativead.NativeView;
import com.huawei.hms.ads.splash.SplashAdDisplayListener;
import com.huawei.hms.ads.splash.SplashView;

import java.util.List;

public class HwAd {
    private static final String TAG = "华为广告 SDK";
    public static void InitHwSDK(Context context) {
        HwAds.init(context);
        Init.adSDKisLoaded.put(AdPlatform.HW, true);
    }
    public static void HwSplashAd(Context context, String slotId, @NonNull SplashView splashView) {
        // "testq6zq98hecj"为测试专用的广告位ID, App正式发布时需要改为正式的广告位ID
        AdParam adParam = new AdParam.Builder().build();

        SplashView.SplashAdLoadListener splashAdLoadListener = new SplashView.SplashAdLoadListener() {
            @Override
            public void onAdLoaded() {
                // 广告加载成功时调用
                i(TAG, "广告加载成功");
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // 广告加载失败时调用, 跳转至App主界面
                e(TAG, "广告加载失败，错误码：" + errorCode);
                SplashActivity.goToMainActivity(context);
            }

            @Override
            public void onAdDismissed() {
                i(TAG, "广告关闭");
                // 广告展示完毕时调用, 跳转至App主界面
                SplashActivity.goToMainActivity(context);
            }
        };

        int orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;

        // 设置视频类开屏广告的音频焦点类型
        splashView.setAudioFocusType(AudioFocusType.NOT_GAIN_AUDIO_FOCUS_WHEN_MUTE);
        // 加载广告
        splashView.load(slotId, orientation, adParam, splashAdLoadListener);

        SplashAdDisplayListener adDisplayListener = new SplashAdDisplayListener() {
            @Override
            public void onAdShowed() {
                SplashActivity.onSplashAdLoaded(context, splashView);
                i(TAG, "广告曝光");
                // 广告显示时调用
            }

            @Override
            public void onAdClick() {
                // 广告被点击时调用
                i(TAG, "广告被点击");
            }
        };
        splashView.setAdDisplayListener(adDisplayListener);
    }
    public static boolean HwExSplashAd(Context context){
        Hw_ExSplashServiceConnection serviceConnection = new Hw_ExSplashServiceConnection(context);
        Intent intent = new Intent("com.huawei.hms.ads.EXSPLASH_SERVICE");
        intent.setPackage("com.huawei.hwid");
        boolean result = context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        i(TAG, "bindService result: " + result);
        return result;
    }
    public static void HwBannerAd(String adId, BannerView bannerView, FrameLayout adFrameLayout) {
        /*常用的标准横幅广告尺寸如下表所示：
            类型	尺寸（宽*高，以dp为单位）	说明
            BANNER_SIZE_320_50	320x50	普通横幅广告，适用于手机设备。
            BANNER_SIZE_320_100	320x100	大型横幅广告，适用于手机设备。
            BANNER_SIZE_300_250	300x250	中矩形横幅广告，适用于手机设备。
            BANNER_SIZE_360_57	360x57	普通横幅广告，适用于1080*170px的广告素材。
            BANNER_SIZE_360_144	360x144	大型横幅广告，适用于1080*432px的广告素材。
            BANNER_SIZE_468_60	468x60	普通横幅广告，适用于手机设备。
            BANNER_SIZE_728_90	728x90	普通横幅广告，适用于横屏平板和竖屏手机。
            BANNER_SIZE_SMART	屏幕宽度 x 32|50|90	智能横幅广告，根据设备的宽高比自动调整广告尺寸，适用于手机设备。
            BANNER_SIZE_ADVANCED	屏幕宽度 x 最优高度	自适应横幅广告，根据设备的尺寸和横竖屏状态计算出合适的尺寸。
        说明
            在中国大陆区域暂只支持BANNER_SIZE_360_57和BANNER_SIZE_360_144。
            在非中国大陆区域建议使用BANNER_SIZE_320_50和BANNER_SIZE_300_250。
            更多广告尺寸请参见API文档中的BannerAdSize类。*/

        // 空指针防护（Kotlin的!!在Java中需要显式判断）
        if (bannerView == null || adFrameLayout == null) {
            e(TAG, "BannerView或AdFrameLayout为空");
            return;
        }

        // "testw6vs28auh3"为测试专用的广告位ID，App正式发布时需要改为正式的广告位ID
        // 设置广告位ID和广告尺寸
        bannerView.setAdId(adId);
        bannerView.setBannerAdSize(BannerAdSize.BANNER_SIZE_360_57);
        adFrameLayout.addView(bannerView);

        // 设置轮播时间间隔为60秒
        bannerView.setBannerRefresh(60);

        // 创建广告请求，加载广告
        AdParam adParam = new AdParam.Builder().build();
        bannerView.loadAd(adParam);

        bannerView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // 广告加载成功时调用
            }

            @Override
            public void onAdFailed(int errorCode) {
                // 广告加载失败时调用
                bannerView.destroy();
            }

            @Override
            public void onAdOpened() {
                // 广告打开时调用
            }

            @Override
            public void onAdClicked() {
                // 广告点击时调用
            }

            @Override
            public void onAdLeave() {
                // 广告离开应用时调用
            }

            @Override
            public void onAdClosed() {
                // 广告关闭时调用
                bannerView.destroy();
            }
        });
    }
    public static void HwNativeAd(Context context, String adId) {
        // "testy63txaom86"为测试专用的广告位ID，App正式发布时需要改为正式的广告位ID
        NativeAdLoader.Builder builder = new NativeAdLoader.Builder(context, adId);
        builder.setNativeAdLoadedListener(nativeAd -> {
            // 广告加载成功后调用
        }).setAdListener(new AdListener() {
            @Override
            public void onAdFailed(int errorCode) {
                // 广告加载失败时调用
            }
        });
        //NativeAdLoader nativeAdLoader = builder.build();
    }
    public static void HwSDKrenderedAd(Context context, String AdId, NativeView adContainer) {
        // 实例化NativeAdLoader,传入广告位id列表
        NativeAdLoader.Builder builder = new NativeAdLoader.Builder(context, AdId);
        NativeAdLoader nativeAdLoader = builder.setNativeAdLoadedListener(nativeAd -> {
            if (adContainer != null) {
                adContainer.setNativeAd(nativeAd);
            }
        }).setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                i(TAG, "load ad, success:");
            }

            @Override
            public void onAdFailed(int errorCode) {
                e(TAG, "fail to load ad, errorCode is:" + errorCode);
            }
        }).build();

        // setSupportTemplate:当开发者完成适配后，需求设置为true才会返回SDK自渲染广告
        // 从云端获取广告
        nativeAdLoader.loadAd(new AdParam.Builder().setSupportTemplate(true).build());
    }
    public static void HwInterstitialAd(Context context, Activity activity, String slotId){
        InterstitialAd interstitialAd;
        interstitialAd = new InterstitialAd(context);
        // "testb4znbuh3n2"为测试专用的广告位ID，App正式发布时需要改为正式的广告位ID
        interstitialAd.setAdId(slotId);
        // 加载插屏广告
        AdParam.Builder builder = new AdParam.Builder();
        // 可选 设置实时bidding广告位参数
        BiddingParam biddingParam = new BiddingParam();
        builder.addBiddingParamMap(slotId, biddingParam);
        builder.setTMax(500);
        interstitialAd.setAdListener(new AdListener(){
                                         @Override
                                         public void onAdLoaded() {
                                             // 广告加载成功时调用
                                             // 显示广告
                                             if (interstitialAd.isLoaded()) {
                                                 interstitialAd.show(activity);
                                             } else {
                                                 Toast.makeText(context, "Ad did not load", Toast.LENGTH_SHORT).show();
                                             }
                                         }
                                         @Override
                                         public void onAdFailed(int errorCode) {
                                             // 广告加载失败时调用
                                         }
                                         @Override
                                         public void onAdClosed() {
                                             // 广告关闭时调用
                                         }
                                         @Override
                                         public void onAdClicked() {
                                             // 广告点击时调用
                                         }
                                         @Override
                                         public void onAdLeave() {
                                             // 广告离开时调用
                                         }
                                         @Override
                                         public void onAdOpened() {
                                             // 广告打开时调用
                                         }
                                     }
        );
        //builder.setCur("币种字符列表"); //？
        interstitialAd.loadAd(new AdParam.Builder().build());
    }
    public static void HwInstreamAd(Context context, String slotId, @NonNull InstreamView instreamView){
        // "testy3cglm3pj0"为测试专用的广告位ID，App正式发布时需要改为正式的广告位ID
        InstreamAdLoader.Builder builder = new InstreamAdLoader.Builder(context, slotId);
        // 设置贴片最大时长
        InstreamAdLoader adLoader = builder.setTotalDuration(15)
                // 设置贴片返回的最大数量
                .setMaxCount(1)
                .setInstreamAdLoadListener(new InstreamAdLoadListener() {
                    @Override
                    public void onAdLoaded(List<InstreamAd> ads) {
                        // 广告加载成功后调用
                    }

                    @Override
                    public void onAdFailed(int errorCode) {
                        // 广告加载失败后调用
                    }
                }).build();
        AdParam.Builder paramBuilder = new AdParam.Builder();
        // 可选 设置实时bidding广告位参数
        BiddingParam biddingParam = new BiddingParam();
        paramBuilder.addBiddingParamMap(slotId, biddingParam);
        paramBuilder.setTMax(500);
//        builder.setCur();
        instreamView.setInstreamMediaChangeListener(ad -> {
            // 广告媒体切换
        });

        instreamView.setInstreamMediaStateListener(new InstreamMediaStateListener() {
            @Override
            public void onMediaProgress(int percent, int playTime) {
                // 播放过程
            }

            @Override
            public void onMediaStart(int playTime) {
                // 播放开始
            }

            @Override
            public void onMediaPause(int playTime) {
                // 播放暂停
            }

            @Override
            public void onMediaStop(int playTime) {
                // 播放停止
            }

            @Override
            public void onMediaCompletion(int playTime) {
                // 播放完成
            }

            @Override
            public void onMediaError(int playTime, int errorCode, int extra) {
                // 播放错误
            }
        });

        instreamView.setMediaMuteListener(new MediaMuteListener() {
            @Override
            public void onMute() {
                // 贴片广告静音
            }

            @Override
            public void onUnmute() {
                // 贴片广告取消静音
            }
        });
        adLoader.loadAd(new AdParam.Builder().build());
    }
}