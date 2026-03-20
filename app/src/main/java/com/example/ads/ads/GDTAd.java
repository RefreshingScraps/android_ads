package com.example.ads.ads;

import android.content.Context;
import static com.example.ads.DevUtil.*;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.media3.exoplayer.ExoPlayer;

import com.example.ads.AdPlatform;
import com.example.ads.Init;
import com.example.ads.SplashActivity;
import com.qq.e.ads.nativ.ADSize;
import com.qq.e.ads.nativ.NativeExpressAD;
import com.qq.e.ads.nativ.NativeExpressADView;
import com.qq.e.ads.nativ.NativeExpressMediaListener;
import com.qq.e.ads.splash.SplashAD;
import com.qq.e.ads.splash.SplashADListener;
import com.qq.e.comm.constants.AdPatternType;
import com.qq.e.comm.constants.LoadAdParams;
import com.qq.e.comm.managers.GDTAdSdk;
import com.qq.e.comm.util.AdError;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class GDTAd {
    private static final String TAG = "广点通广告 SDK";
    public static void InitGDTSDK(Context context, String appId){
        GDTAdSdk.initWithoutStart(context,appId);  // 该接口不会采集用户信息
        // 调用initWithoutStart后请尽快调用start，否则可能影响广告填充，造成收入下降
        GDTAdSdk.start(new GDTAdSdk.OnStartListener() {
            @Override
            public void onStartSuccess() {
                // 推荐开发者在onStartSuccess回调后开始拉广告
                i(TAG, "SDK初始化成功");
                Init.adSDKisLoaded.put(AdPlatform.GDT, true);
            }

            @Override
            public void onStartFailed(Exception e) {
                e(TAG, "SDK初始化失败，错误信息："+e.getMessage());
            }
        });
    }
    public static void GDTSplashAd(Context context,String posId,ViewGroup container){
        // 创建开屏广告实例
        SplashAD splashAD = new SplashAD(context, posId,  new SplashADListener() {
            @Override
            public void onADDismissed() {
                // 广告关闭，进入主界面
                i(TAG, "广告关闭");
                SplashActivity.goToMainActivity(context);
            }
            @Override
            public void onNoAD(com.qq.e.comm.util.AdError adError) {
                // 广告加载失败，也进入主界面
                e(TAG, "广告加载失败，错误信息：" + adError.getErrorMsg() + "(" + adError.getErrorCode() + ")");
                SplashActivity.goToMainActivity(context);
            }
            @Override
            public void onADPresent() {
                // 广告成功展示
                i(TAG, "广告成功展示");
            }
            @Override
            public void onADClicked() {
                // 广告被点击
                i(TAG, "用户点击了广告");
            }
            @Override
            public void onADTick(long millisUntilFinished) {

            }
            @Override
            public void onADExposure() {
                // 广告曝光
                i(TAG, "广告曝光");
                SplashActivity.onSplashAdLoaded(context, container);
            }
            @Override
            public void onADLoaded(long l) {
                // 广告加载成功（开屏广告加载成功后会立即展示，此回调可能不会触发）
                i(TAG, "广告加载成功");
            }
        });
        // 拉取并展示广告，传入广告容器和自定义的跳过按钮（可选）
        splashAD.fetchAdOnly();
        splashAD.showAd(container);
        // 如果不需要自定义跳过按钮，可以使用下面这个方法
        // splashAD.fetchAndShowIn(container);
    }
    public static void GDTPreRollAd(Context context, String posID, @NonNull ViewGroup adContainer, ExoPlayer player){
        adContainer.removeAllViews();
        NativeExpressAD nativeExpressAD = new NativeExpressAD(context, new ADSize(ADSize.FULL_WIDTH, ADSize.AUTO_HEIGHT), posID, new NativeExpressAD.NativeExpressADListener() {
            @Override
            public void onADLoaded(List<NativeExpressADView> list) {
                NativeExpressADView nativeExpressADView = list.get(0);
                nativeExpressADView.setMediaListener(new NativeExpressMediaListener() {
                    @Override
                    public void onVideoInit(NativeExpressADView nativeExpressADView) {
                        i(TAG,"视频初始化");
                    }

                    @Override
                    public void onVideoLoading(NativeExpressADView nativeExpressADView) {
                        i(TAG,"视频正在加载");
                    }

                    @Override
                    public void onVideoCached(NativeExpressADView nativeExpressADView) {
                        i(TAG,"视频已缓存");
                    }

                    @Override
                    public void onVideoReady(NativeExpressADView nativeExpressADView, long l) {
                        i(TAG,"视频已准备好");
                        nativeExpressADView.render();
                        adContainer.setVisibility(ViewGroup.VISIBLE);
                        adContainer.addView(nativeExpressADView);
                    }

                    @Override
                    public void onVideoStart(NativeExpressADView nativeExpressADView) {
                        i(TAG,"视频开始播放");
                    }

                    @Override
                    public void onVideoPause(NativeExpressADView nativeExpressADView) {
                        i(TAG,"视频暂停");
                    }

                    @Override
                    public void onVideoComplete(NativeExpressADView nativeExpressADView) {
                        i(TAG,"视频播放完毕");
                        nativeExpressADView.destroy();
                        player.play();
                    }

                    @Override
                    public void onVideoError(NativeExpressADView nativeExpressADView, AdError adError) {
                        e(TAG,"视频播放失败，错误信息："+adError.getErrorMsg()+"("+adError.getErrorCode()+")");
                        nativeExpressADView.destroy();
                        player.play();
                    }

                    @Override
                    public void onVideoPageOpen(NativeExpressADView nativeExpressADView) {
                        i(TAG,"视频详情页打开");
                    }

                    @Override
                    public void onVideoPageClose(NativeExpressADView nativeExpressADView) {
                        i(TAG,"视频详情页关闭");
                    }
                });
                nativeExpressADView.preloadVideo();
            }

            @Override
            public void onRenderFail(NativeExpressADView nativeExpressADView) {
                e(TAG,"广告染失败");
                nativeExpressADView.destroy();
                player.play();
            }

            @Override
            public void onRenderSuccess(NativeExpressADView nativeExpressADView) {
                i(TAG,"广告渲染成功");
            }

            @Override
            public void onADExposure(NativeExpressADView nativeExpressADView) {
                i(TAG,"广告曝光");
            }

            @Override
            public void onADClicked(NativeExpressADView nativeExpressADView) {
                i(TAG,"广告被点击");
            }

            @Override
            public void onADClosed(NativeExpressADView nativeExpressADView) {
                i(TAG,"广告关闭");
                nativeExpressADView.destroy();
                player.play();
            }

            @Override
            public void onADLeftApplication(NativeExpressADView nativeExpressADView) {

            }

            @Override
            public void onNoAD(AdError adError) {
                i(TAG,"广告加载失败，错误信息："+adError.getErrorMsg()+"("+adError.getErrorCode()+")");
                player.play();
            }
        });
        LoadAdParams loadAdParams = new LoadAdParams();
        Map<String, String> info = new HashMap<>();
        info.put("custom_key", "native_express");
        info.put("staIn", "com.qq.e.demo");
        info.put("thrmei", "aaaa_bbbb_cccc_dddd");
        loadAdParams.setDevExtra(info);
        nativeExpressAD.loadAD(1,loadAdParams);
    }
    public static void GDTFeedAd(Context context, String posID, @NonNull ViewGroup adContainer){
        adContainer.removeAllViews();
        NativeExpressAD nativeExpressAD = new NativeExpressAD(context, new ADSize(ADSize.FULL_WIDTH,ADSize.AUTO_HEIGHT), posID, new NativeExpressAD.NativeExpressADListener() {
            @Override
            public void onADLoaded(List<NativeExpressADView> list) {
                i(TAG, "onADLoaded: " + list.size());
                NativeExpressADView nativeExpressADView = list.get(0);
                nativeExpressADView.render();
                // 3.返回数据后，SDK 会返回可以用于展示 NativeExpressADView 列表
                if (nativeExpressADView.getBoundData().getAdPatternType() == AdPatternType.NATIVE_VIDEO) {
                    nativeExpressADView.setMediaListener(new NativeExpressMediaListener() {
                        @Override
                        public void onVideoInit(NativeExpressADView nativeExpressADView) {
                            i(TAG, "视频初始化");
                        }

                        @Override
                        public void onVideoLoading(NativeExpressADView nativeExpressADView) {
                            i(TAG, "视频加载中");
                        }

                        @Override
                        public void onVideoCached(NativeExpressADView nativeExpressADView) {
                            i(TAG, "视频已缓存");
                        }

                        @Override
                        public void onVideoReady(NativeExpressADView nativeExpressADView, long l) {
                            i(TAG, "视频已准备好");
                        }

                        @Override
                        public void onVideoStart(NativeExpressADView nativeExpressADView) {
                            i(TAG, "视频开始播放");
                        }

                        @Override
                        public void onVideoPause(NativeExpressADView nativeExpressADView) {
                            i(TAG, "视频暂停");
                        }

                        @Override
                        public void onVideoComplete(NativeExpressADView nativeExpressADView) {
                            i(TAG, "视频完成");
                        }

                        @Override
                        public void onVideoError(NativeExpressADView nativeExpressADView, AdError adError) {
                            e(TAG, "视频播放错误，错误信息："+adError.getErrorMsg()+"("+adError.getErrorCode()+")");
                        }

                        @Override
                        public void onVideoPageOpen(NativeExpressADView nativeExpressADView) {

                        }

                        @Override
                        public void onVideoPageClose(NativeExpressADView nativeExpressADView) {

                        }
                    });
                }
                nativeExpressADView.render();
                if (adContainer.getChildCount() > 0) {
                    adContainer.removeAllViews();
                }

                // 需要保证 View 被绘制的时候是可见的，否则将无法产生曝光和收益。
                adContainer.addView(nativeExpressADView);
            }

            @Override
            public void onRenderFail(NativeExpressADView nativeExpressADView) {
                nativeExpressADView.destroy();
            }

            @Override
            public void onRenderSuccess(NativeExpressADView nativeExpressADView) {
                i(TAG, "广告渲染成功");
            }

            @Override
            public void onADExposure(NativeExpressADView nativeExpressADView) {
                i(TAG, "广告曝光");
            }

            @Override
            public void onADClicked(NativeExpressADView nativeExpressADView) {
                i(TAG, "广告被点击");
            }

            @Override
            public void onADClosed(NativeExpressADView nativeExpressADView) {
                i(TAG, "广告关闭");
                nativeExpressADView.destroy();
            }

            @Override
            public void onADLeftApplication(NativeExpressADView nativeExpressADView) {
                nativeExpressADView.destroy();
            }

            @Override
            public void onNoAD(AdError adError) {
                e(TAG, "广告加载失败，错误信息："+adError.getErrorMsg()+"("+adError.getErrorCode()+")");
            }
        });
        nativeExpressAD.loadAD(1);
    }
}
