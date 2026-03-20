package com.example.ads.ads;

import static com.example.ads.DevUtil.*;
import android.view.View;
import android.view.ViewGroup;
import android.content.Context;

import com.example.ads.AdPlatform;
import com.example.ads.Init;
import com.kwad.sdk.api.KsAdSDK;
import com.kwad.sdk.api.KsFeedAd;
import com.kwad.sdk.api.SdkConfig;
import com.kwad.sdk.api.KsLoadManager;
import com.kwad.sdk.api.KsScene;
import com.kwad.sdk.api.KsSplashScreenAd;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.ads.SplashActivity;

import java.util.List;

public class KsAd {
    private static final String TAG = "快手广告 SDK";
    public static void initKSSDK(Context context,String appId,String appName) {
        KsAdSDK.init(context, new SdkConfig.Builder()
                .appId(appId) // 测试aapId，请联系快⼿平台申请正式AppId，必填
                .appName(appName)// 测试appName，请填写您应⽤的名称，⾮必填
                .showNotification(true) // 是否展示下载通知栏
                .debug(isDebug()) // 是否开启sdk 调试⽇志  可选
                .build());
        KsAdSDK.start();
        Init.adSDKisLoaded.put(AdPlatform.KS, true);
    }
    public static void KSSplashAd(Context context,long posId,ViewGroup container){
        KsLoadManager adRequestManager = KsAdSDK.getLoadManager();
        KsScene scene = new KsScene
                //是否需要开屏⼩窗展示，默认为false, 设置false后将不会回调 onShowMiniWindow
                // .needShowMiniWindow(true)
                .Builder(posId).build(); // 此为测试posId，请联系快⼿平台申请正式posId
        if (adRequestManager != null) {
            adRequestManager.loadSplashScreenAd(scene, new
                    KsLoadManager.SplashScreenAdListener() {
                        @Override
                        public void onError(int code, String msg) {
                            e(TAG ,"开屏⼴告请求失败，错误信息："+ code +"("+ msg+")");
                            SplashActivity.goToMainActivity(context);
                        }
                        
                        @Override
                        public void onRequestResult(int adNumber) {
                            i(TAG, "开屏⼴告⼴告请求填充 " + adNumber);
                        }

                        @Override
                        public void onSplashScreenAdLoad(@Nullable KsSplashScreenAd splashScreenAd) {
                            //SplashAd.ksSplashScreenAd 为静态变量， 保存splashScreenAd⽤户⼩窗模式 SplashAd.ksSplashScreenAd = splashScreenAd;
                            //你可以选择View接⼊或者Frament接⼊ addFragment(KsSplashScreenAd splashScreenAd)
                            if(splashScreenAd != null) {
                                View view = splashScreenAd.getView(context, new KsSplashScreenAd.SplashScreenAdInteractionListener() {
                                            @Override
                                            public void onAdClicked() {
                                                i(TAG, "开屏⼴告点击");
                                                //onAdClick 会吊起h5或者应⽤商店。 不直接跳转，等返回后再跳转。 mGotoMainActivity = true;
                                                //点击不出发显示miniWindow SplashAd.ksSplashScreenAd = null;
                                            }

                                            @Override
                                            public void onAdShowError(int code, String extra) {
                                                i(TAG, "开屏⼴告显示错误 " + code + " extra " + extra);
                                                SplashActivity.goToMainActivity(context);
                                                //出错不出发显示miniWindown SplashAd.ksSplashScreenAd = null;
                                            }

                                            @Override
                                            public void onAdShowEnd() {
                                                i(TAG, "开屏⼴告展示完毕");
                                                SplashActivity.goToMainActivity(context);
                                            }

                                            @Override
                                            public void onAdShowStart() {
                                                i(TAG, "开屏⼴告开始展示");
                                                SplashActivity.onSplashAdLoaded(context, container);
                                            }

                                            @Override
                                            public void onSkippedAd() {
                                                i(TAG, "⽤户跳过开屏⼴告");
                                                SplashActivity.goToMainActivity(context);
                                            }

                                            @Override
                                            public void onDownloadTipsDialogShow() {

                                            }

                                            @Override
                                            public void onDownloadTipsDialogDismiss() {

                                            }

                                            @Override
                                            public void onDownloadTipsDialogCancel() {

                                            }
                                        });
                                container.removeAllViews();
                                view.setLayoutParams(new
                                        ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                        ViewGroup.LayoutParams.MATCH_PARENT));
                                container.addView(view);
                            }
                        }
                    });
        }
    }
    public static void KsFeedAd(long posId, @NonNull ViewGroup adContainer){
        adContainer.removeAllViews();
        KsScene scene = new KsScene.Builder(posId)
                .adNum(1).build(); // 此为测试posId，请联系快⼿平台申请正式posId
        KsAdSDK.getLoadManager().loadConfigFeedAd(scene, new
                KsLoadManager.FeedAdListener() {
                    @Override
                    public void onError(int i, String s) {
                        e("快手广告","广告加载失败，错误信息："+s+"("+i+")");
                    }

                    @Override
                    public void onFeedAdLoad(@Nullable List<KsFeedAd> list) {
                        if(list != null) {
                            KsFeedAd ksFeedAd = list.get(0);
                            ksFeedAd.setAdInteractionListener(new KsFeedAd.AdInteractionListener() {
                                @Override
                                public void onAdClicked() {

                                }

                                @Override
                                public void onAdShow() {

                                }

                                @Override
                                public void onDislikeClicked() {

                                }

                                @Override
                                public void onDownloadTipsDialogShow() {

                                }

                                @Override
                                public void onDownloadTipsDialogDismiss() {

                                }
                            });
                            ksFeedAd.render(new KsFeedAd.AdRenderListener() {
                                @Override
                                public void onAdRenderSuccess(View view) {
                                    adContainer.addView(view);
                                }

                                @Override
                                public void onAdRenderFailed(int i, String s) {
                                    e("快手广告","广告渲染失败，错误信息："+s+"("+i+")");
                                }
                            });
                        }
                    }
                });
    }
}
