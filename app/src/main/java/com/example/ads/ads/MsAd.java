package com.example.ads.ads;
import android.app.Activity;
import static com.example.ads.DevUtil.*;
import android.content.Context;
import android.view.ViewGroup;

import com.example.ads.AdPlatform;
import com.example.ads.Init;
import com.meishu.sdk.BuildConfig;
import com.meishu.sdk.core.AdSdk;
import com.meishu.sdk.core.MSAdConfig;
import com.meishu.sdk.core.ad.MsAdSlot;
import com.meishu.sdk.core.ad.banner.BannerAdEventLoader;
import com.meishu.sdk.core.ad.banner.BannerAdLoadListener;
import com.meishu.sdk.core.ad.banner.IBannerAd;
import com.meishu.sdk.core.ad.draw.DrawAd;
import com.meishu.sdk.core.ad.draw.DrawAdEventLoader;
import com.meishu.sdk.core.ad.draw.DrawAdLoadListener;
import com.meishu.sdk.core.ad.draw.IDrawAd;
import com.meishu.sdk.core.ad.fullscreenvideo.FullScreenAdLoadListener;
import com.meishu.sdk.core.ad.fullscreenvideo.IFullScreenMediaListener;
import com.meishu.sdk.core.ad.fullscreenvideo.IFullScreenVideoAd;
import com.meishu.sdk.core.ad.interstitial.InterstitialAd;
import com.meishu.sdk.core.ad.interstitial.InterstitialAdEventLoader;
import com.meishu.sdk.core.ad.interstitial.InterstitialAdLoadListener;
import com.meishu.sdk.core.ad.paster.PasterAd;
import com.meishu.sdk.core.ad.paster.PasterAdLoadListener;
import com.meishu.sdk.core.ad.recycler.FeedExpressAdInteractionListener;
import com.meishu.sdk.core.ad.recycler.RecyclerAdData;
import com.meishu.sdk.core.ad.recycler.RecyclerAdLoadListener;
import com.meishu.sdk.core.ad.reward.RewardAdLoadListener;
import com.meishu.sdk.core.ad.reward.RewardAdMediaListener;
import com.meishu.sdk.core.ad.reward.RewardInteractionListener;
import com.meishu.sdk.core.ad.reward.RewardVideoAd;
import com.meishu.sdk.core.ad.reward.RewardVideoEventLoader;
import com.meishu.sdk.core.ad.splash.ISplashAd;
import com.meishu.sdk.core.ad.splash.SplashAdEventLoader;
import com.meishu.sdk.core.ad.splash.SplashAdLoadListener;
import com.meishu.sdk.core.loader.InteractionListener;
import com.meishu.sdk.core.utils.AdError;
import androidx.annotation.NonNull;
import androidx.media3.exoplayer.ExoPlayer;

import com.example.ads.SplashActivity;

import java.util.List;
import java.util.Map;

import com.meishu.sdk.core.ad.recycler.RecyclerMixAdEventLoader;
import com.meishu.sdk.core.ad.fullscreenvideo.FullScreenVideoEventAdLoader;

import com.meishu.sdk.core.ad.paster.PasterAdEventLoader;
public class MsAd {
    private static final String TAG = "美数广告 SDK";
    public static void initMsAdSDK(Context context,String appId){
        MSAdConfig sdkConfig = new MSAdConfig.Builder()
                .appId(appId)
                .enableDebug(isDebug())  //开启DEBUG模式，打印内部LOG
                .build();
        AdSdk.init(context, sdkConfig);
        Init.adSDKisLoaded.put(AdPlatform.MS, true);
    }
    public static void MsSplashAd(Context context,String pid,ViewGroup adContainer){
        //只加载广告
        new SplashAdEventLoader(context,
                new MsAdSlot.Builder()
                        .setPid(pid)
                        .setFetchCount(1)
                        .setIsHideSkipBtn(false)
                        .build(),
                new SplashAdLoadListener() {
            @Override
            public void onLoadSuccess(@NonNull ISplashAd iSplashAd) {
                i(TAG,"广告加载成功");
                iSplashAd.setInteractionListener(new InteractionListener() {
                    @Override
                    public void onAdClicked() {
                        i(TAG, "广告被点击");
                    }

                    @Override
                    public void onAdExposure() {
                        SplashActivity.onSplashAdLoaded(context, adContainer);
                    }

                    @Override
                    public void onAdClosed() {
                        i(TAG, "广告关闭");
                        SplashActivity.goToMainActivity(context);
                    }
                });
                iSplashAd.showAd(adContainer);
            }

            @Override
            public void onLoadFail(AdError adError) {
                e(TAG,"广告加载失败，错误信息："+adError.getMessage()+"("+adError.getCode()+")");
                SplashActivity.goToMainActivity(context);
            }

            @Override
            public void onRenderSuccess(ISplashAd iSplashAd) {
                i(TAG,"广告渲染成功");
            }

            @Override
            public void onAdFail(ISplashAd iSplashAd, AdError adError, int i) {
                e(TAG,"广告渲染失败，错误信息："+adError.getMessage()+"("+adError.getCode()+")");
                SplashActivity.goToMainActivity(context);
            }
        },5000).loadAd();

    }
    public static void MsRecyclerMixAdAd(Activity activity, String Pid, @NonNull ViewGroup adContainer){
        adContainer.removeAllViews();
        new RecyclerMixAdEventLoader(adContainer.getContext(),
                new MsAdSlot(new MsAdSlot.Builder()
                        .setFetchCount(1)
                        .setPid(Pid)
                        .setIsVideoAutoPlay(true)
                        .setIsHideSkipBtn(false)
                ), new RecyclerAdLoadListener() {
            @Override
            public void onLoadedSuccess(List<RecyclerAdData> list) {
                i("美数广告","广告加载成功");
                RecyclerAdData recyclerAdData = list.get(0);
                recyclerAdData.setExpressAdInteractionListener(new FeedExpressAdInteractionListener() {
                    @Override
                    public void onAdClicked() {

                    }

                    @Override
                    public void onAdExposure() {

                    }

                    @Override
                    public void onAdClosed() {

                    }

                    @Override
                    public void onRenderError(int i, String s) {
                        e("美数广告","广告渲染失败，错误信息："+s+"("+i+")");
                    }

                    @Override
                    public void onRenderSuccess() {
                        i("美数广告","广告渲染成功");
                        adContainer.addView(recyclerAdData.getExpressView());
                    }
                });
                recyclerAdData.render(activity);
            }

            @Override
            public void onLoadFail(AdError adError) {
                i("美数广告","广告加载失败，错误信息："+adError.getMessage()+"("+adError.getCode()+")");
            }
        }).loadAd();
    }
    public static void MsBannerAd(Activity activity, String pid, @NonNull ViewGroup bannerContainer){
        bannerContainer.removeAllViews();
        new BannerAdEventLoader(activity,
                new MsAdSlot.Builder()
                        .setPid(pid)
                        .setAutoRender(true)
                        .setCloseButtonVisible(true)
                        .setWidth(bannerContainer.getMeasuredWidth())
                        .setHeight(bannerContainer.getMeasuredHeight())
                        .build(),
                new BannerAdLoadListener() {
            @Override
            public void onLoadSuccess(IBannerAd ad) {
                d(TAG, "DEMO ADEVENT " + (new Throwable().getStackTrace()[0].getMethodName()));
                ad.render();
                ad.showAd(bannerContainer);
            }

            @Override
            public void onLoadFail(AdError adError) {
                d(TAG, "DEMO ADEVENT " + (new Throwable().getStackTrace()[0].getMethodName()));
            }

            @Override
            public void onRenderSuccess(IBannerAd ad) {
                d(TAG, "DEMO ADEVENT " + (new Throwable().getStackTrace()[0].getMethodName()));
                //Toast.makeText(activity.getApplicationContext(),"渲染成功",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdFail(IBannerAd ad, AdError adError, int type) {
                d(TAG, "DEMO ADEVENT " + (new Throwable().getStackTrace()[0].getMethodName()));
            }
        }).loadAd();
    }
    public static void MsFullScreenVideoAd(Activity activity, String pid){
        new FullScreenVideoEventAdLoader(activity, new MsAdSlot.Builder().setPid(pid).build(), new FullScreenAdLoadListener() {
            @Override
            public void onLoadSuccess(IFullScreenVideoAd ad) {
                d(TAG, "DEMO ADEVENT " + (new Throwable().getStackTrace()[0].getMethodName()));
            }

            @Override
            public void onLoadFail(AdError adError) {
                d(TAG, "DEMO ADEVENT " + (new Throwable().getStackTrace()[0].getMethodName()));
            }

            @Override
            public void onRenderSuccess(IFullScreenVideoAd fullScreenVideoAd) {
                d(TAG, "DEMO ADEVENT " + (new Throwable().getStackTrace()[0].getMethodName()));
                fullScreenVideoAd.setInteractionListener(new InteractionListener() {
                    @Override
                    public void onAdClicked() {
                        d(TAG, "DEMO ADEVENT " + (new Throwable().getStackTrace()[0].getMethodName()));
                    }

                    @Override
                    public void onAdExposure() {
                        e(TAG,"ecpm="+fullScreenVideoAd.getData().getEcpm());
                        d(TAG, "DEMO ADEVENT " + (new Throwable().getStackTrace()[0].getMethodName()));
                    }

                    @Override
                    public void onAdClosed() {
                        d(TAG, "DEMO ADEVENT " + (new Throwable().getStackTrace()[0].getMethodName()));
                    }

                });
                fullScreenVideoAd.setMediaListener(new IFullScreenMediaListener() {
                    @Override
                    public void onVideoLoaded() {
                        d(TAG, "DEMO ADEVENT " + (new Throwable().getStackTrace()[0].getMethodName()));
                    }

                    @Override
                    public void onVideoStart() {
                        d(TAG, "DEMO ADEVENT " + (new Throwable().getStackTrace()[0].getMethodName()));
                    }

                    @Override
                    public void onVideoPause() {
                        d(TAG, "DEMO ADEVENT " + (new Throwable().getStackTrace()[0].getMethodName()));
                    }

                    @Override
                    public void onVideoResume() {
                        d(TAG, "DEMO ADEVENT " + (new Throwable().getStackTrace()[0].getMethodName()));
                    }

                    @Override
                    public void onVideoCompleted() {
                        d(TAG, "DEMO ADEVENT " + (new Throwable().getStackTrace()[0].getMethodName()));
                    }

                    @Override
                    public void onVideoError() {
                        d(TAG, "DEMO ADEVENT " + (new Throwable().getStackTrace()[0].getMethodName()));
                    }

                    @Override
                    public void onSkippedVideo() {
                        d(TAG, "DEMO ADEVENT " + (new Throwable().getStackTrace()[0].getMethodName()));
                    }
                });
            }

            @Override
            public void onAdFail(IFullScreenVideoAd ad, AdError adError, int type) {
                d(TAG, "DEMO ADEVENT " + (new Throwable().getStackTrace()[0].getMethodName()));
            }
        }).loadAd();
    }
    public static void MsPasterAd(Context context, String pid, @NonNull ViewGroup videoContainer, ExoPlayer player){
        videoContainer.removeAllViews();
        MsAdSlot msAdSlot = new MsAdSlot.Builder()
                .setPid(pid)
                .build();
        PasterAdEventLoader pasterAdLoader = new PasterAdEventLoader(context, videoContainer, msAdSlot, new PasterAdLoadListener() {
            @Override
            public void onVideoLoaded() {

            }

            @Override
            public void onVideoComplete() {
                //player.play();
            }

            @Override
            public void onLoadSuccess(PasterAd pasterAd) {
                pasterAd.setInteractionListener(new InteractionListener() {
                    @Override
                    public void onAdClicked() {
                        // 点击时可以把广告关掉
                        d(TAG, "DEMO ADEVENT " + (new Throwable().getStackTrace()[0].getMethodName()));
                    }

                    @Override
                    public void onAdExposure() {
                        d(TAG, "DEMO ADEVENT " + (new Throwable().getStackTrace()[0].getMethodName()));
                    }

                    @Override
                    public void onAdClosed() {
                        d(TAG, "DEMO ADEVENT " + (new Throwable().getStackTrace()[0].getMethodName()));
                        player.play();
                    }

                });
            }

            @Override
            public void onLoadFail(AdError adError) {
                player.play();
            }

            @Override
            public void onRenderSuccess(PasterAd pasterAd) {

            }

            @Override
            public void onAdFail(PasterAd pasterAd, AdError adError, int i) {
                player.play();
            }
        });
        pasterAdLoader.loadAd();
    }
    public static void MsRewardVideoAd(Context context, String pid){
        new RewardVideoEventLoader(context, new MsAdSlot.Builder().setPid(pid).build(), new RewardAdLoadListener() {
            @Override
            public void onLoadSuccess(RewardVideoAd ad) {
                d(TAG, "DEMO ADEVENT " + (new Throwable().getStackTrace()[0].getMethodName()));
                ad.setInteractionListener(new RewardInteractionListener() {
                    @Override
                    public void onReward(Map<String, Object> map) {
                        d(TAG, "DEMO ADEVENT " + (new Throwable().getStackTrace()[0].getMethodName()));
                    }

                    @Override
                    public void onAdClicked() {
                        d(TAG, "DEMO ADEVENT " + (new Throwable().getStackTrace()[0].getMethodName()));
                    }

                    @Override
                    public void onAdExposure() {
                        e(TAG,"ecpm="+ad.getData().getEcpm());
                        d(TAG, "DEMO ADEVENT " + (new Throwable().getStackTrace()[0].getMethodName()));
                    }

                    @Override
                    public void onAdClosed() {
                        d(TAG, "DEMO ADEVENT " + (new Throwable().getStackTrace()[0].getMethodName()));
                    }

                });
                ad.setMediaListener(new RewardAdMediaListener() {
                    @Override
                    public void onVideoLoaded() {
                        d(TAG, "DEMO ADEVENT " + (new Throwable().getStackTrace()[0].getMethodName()));
                    }

                    @Override
                    public void onVideoStart() {
                        d(TAG, "DEMO ADEVENT " + (new Throwable().getStackTrace()[0].getMethodName()));
                    }

                    @Override
                    public void onVideoPause() {
                        d(TAG, "DEMO ADEVENT " + (new Throwable().getStackTrace()[0].getMethodName()));
                    }

                    @Override
                    public void onVideoResume() {
                        d(TAG, "DEMO ADEVENT " + (new Throwable().getStackTrace()[0].getMethodName()));
                    }

                    @Override
                    public void onVideoCompleted() {
                        d(TAG, "DEMO ADEVENT " + (new Throwable().getStackTrace()[0].getMethodName()));
                    }

                    @Override
                    public void onVideoError() {
                        d(TAG, "DEMO ADEVENT " + (new Throwable().getStackTrace()[0].getMethodName()));
                    }

                    @Override
                    public void onSkippedVideo() {
                        d(TAG, "DEMO ADEVENT " + (new Throwable().getStackTrace()[0].getMethodName()));
                    }
                });
            }

            @Override
            public void onLoadFail(AdError adError) {
                d(TAG, "DEMO ADEVENT " + (new Throwable().getStackTrace()[0].getMethodName()));
            }

            @Override
            public void onRenderSuccess(RewardVideoAd ad) {
                d(TAG, "DEMO ADEVENT " + (new Throwable().getStackTrace()[0].getMethodName()));
            }

            @Override
            public void onAdFail(RewardVideoAd ad, AdError adError, int type) {
                d(TAG, "DEMO ADEVENT " + (new Throwable().getStackTrace()[0].getMethodName()));
            }
        }).loadAd();
    }
    public static void MsInterstitialAd(Activity activity, String pid){
        new InterstitialAdEventLoader(activity,
                new MsAdSlot.Builder()
                        .setPid(pid)
                        .setIsClickToClose(true)
                        .build(),
                new InterstitialAdLoadListener() {
                    @Override
                    public void onLoadSuccess(InterstitialAd ad) {
                        d(TAG, "DEMO ADEVENT " + (new Throwable().getStackTrace()[0].getMethodName()));
                        ad.setInteractionListener(new InteractionListener() {
                            @Override
                            public void onAdClicked() {
                                d(TAG, "DEMO ADEVENT " + (new Throwable().getStackTrace()[0].getMethodName()));
                            }

                            @Override
                            public void onAdExposure() {
                                d(TAG, "DEMO ADEVENT " + (new Throwable().getStackTrace()[0].getMethodName()) + " " + "ecpm=" + ad.getData().getEcpm());
                            }

                            @Override
                            public void onAdClosed() {
                                d(TAG, "DEMO ADEVENT " + (new Throwable().getStackTrace()[0].getMethodName()));
                            }
                        });
                    }

                    @Override
                    public void onLoadFail(AdError adError) {
                        d(TAG, "DEMO ADEVENT " + (new Throwable().getStackTrace()[0].getMethodName()));
                    }

                    @Override
                    public void onRenderSuccess(InterstitialAd ad) {
                        d(TAG, "DEMO ADEVENT " + (new Throwable().getStackTrace()[0].getMethodName()));
                    }

                    @Override
                    public void onAdFail(InterstitialAd ad, AdError adError, int type) {
                        d(TAG, "DEMO ADEVENT " + (new Throwable().getStackTrace()[0].getMethodName()));
                    }
                });
    }
    public static void MsDrawAd(Context context, String pid, ViewGroup adContainer){
        new DrawAdEventLoader(context,
                new MsAdSlot.Builder()
                        .setPid(pid)
                        .setAutoRender(true)
                        .build(),
                new DrawAdLoadListener() {
                    @Override
                    public void onLoadSuccess(IDrawAd ad) {
                        d(TAG, "DEMO ADEVENT " + (new Throwable().getStackTrace()[0].getMethodName()));
                        adContainer.addView(ad.getAdView());
                    }

                    @Override
                    public void onLoadFail(AdError adError) {
                        d(TAG, "DEMO ADEVENT " + (new Throwable().getStackTrace()[0].getMethodName()));
                    }

                    @Override
                    public void onRenderSuccess(IDrawAd iDrawAd) {
                        d(TAG, "DEMO ADEVENT " + (new Throwable().getStackTrace()[0].getMethodName()));
                        //Toast.makeText(VideoFeedActivity.this, "渲染成功", Toast.LENGTH_SHORT).show();

                        iDrawAd.setInteractionListener(new InteractionListener() {
                            @Override
                            public void onAdClicked() {
                                d(TAG, "DEMO ADEVENT " + (new Throwable().getStackTrace()[0].getMethodName()));
                            }

                            @Override
                            public void onAdExposure() {
                                d(TAG, "DEMO ADEVENT " + (new Throwable().getStackTrace()[0].getMethodName()));
                            }

                            @Override
                            public void onAdClosed() {
                                d(TAG, "DEMO ADEVENT " + (new Throwable().getStackTrace()[0].getMethodName()));
                            }
                        });

                        iDrawAd.setOnDrawVideoListener(new DrawAd.IDrawVideoListener() {
                            @Override
                            public void playRenderingStart() {
                                e(TAG, "playRenderingStart: " );
                            }

                            @Override
                            public void playPause() {
                                e(TAG, "playPause: " );
                            }

                            @Override
                            public void playResume() {
                                e(TAG, "playResume: " );
                            }

                            @Override
                            public void playCompletion() {
                                e(TAG, "playCompletion: " );
                            }

                            @Override
                            public void playError() {
                                e(TAG, "playError: " );
                            }

                            @Override
                            public void pauseBtnClick() {
                                e(TAG, "pauseBtnClick: " );
                            }

                            @Override
                            public void onProgressUpdate(long current, long duration) {
                                e(TAG, "onProgressUpdate: "+current);
                            }

                            @Override
                            public void onClickRetry() {
                                e(TAG, "onClickRetry: " );
                            }

                            @Override
                            public void onVideoLoad() {
                                e(TAG, "onVideoLoad: " );
                            }

                            @Override
                            public void onVideoError(int errorCode, String errorMsg) {
                                e(TAG, "onVideoError: "+errorCode );
                            }
                        });
                    }

                    @Override
                    public void onAdFail(IDrawAd ad, AdError adError, int type) {
                        d(TAG, "DEMO ADEVENT " + (new Throwable().getStackTrace()[0].getMethodName()));
                    }

        }).loadAd();
    }
}