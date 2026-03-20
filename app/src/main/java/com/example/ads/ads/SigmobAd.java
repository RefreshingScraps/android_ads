package com.example.ads.ads;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import static com.example.ads.DevUtil.*;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.ads.AdPlatform;
import com.example.ads.Init;
import com.example.ads.R;
import com.example.ads.SplashActivity;
import com.sigmob.windad.OnInitializationListener;
import com.sigmob.windad.OnStartListener;
import com.sigmob.windad.Splash.WindSplashAD;
import com.sigmob.windad.Splash.WindSplashADListener;
import com.sigmob.windad.Splash.WindSplashAdRequest;
import com.sigmob.windad.WindAdError;
import com.sigmob.windad.WindAdOptions;
import com.sigmob.windad.WindAds;
import com.sigmob.windad.natives.NativeADEventListener;
import com.sigmob.windad.natives.NativeAdPatternType;
import com.sigmob.windad.natives.WindNativeAdData;
import com.sigmob.windad.natives.WindNativeAdRequest;
import com.sigmob.windad.natives.WindNativeUnifiedAd;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SigmobAd {
    private static final String TAG = "Sigmob广告 SDK";
    public static void initSigmobSDK(Context context, String appId, String appKey){
        WindAds ads = WindAds.sharedAds();
        WindAdOptions options = new WindAdOptions(appId, appKey);
// 设置自定义设备信息控制器（可选）
        ads.init(context,options, new OnInitializationListener() {

            @Override
            public void onInitializationSuccess() {
                e(TAG, "SDK初始化成功");
                Init.adSDKisLoaded.put(AdPlatform.SIGMOB, true);
            }

            @Override
            public void onInitializationFail(String error) {
                e(TAG, "SDK初始化失败，错误信息：" + error);
            }
        });
        ads.start(new OnStartListener() {
            @Override
            public void onStartSuccess() {
                i(TAG, "SDK开始成功");
            }

            @Override
            public void onStartFail(String error) {
                i(TAG, "SDK开始失败，错误信息："+error);
            }
        });
    }
    
    public static void SigmobSplashAd(Context context, String PLACEMENT_ID, String USER_ID, Map<String, Object> OPTIONS, ViewGroup adContainer){
        // PLACEMENT_ID 必填
        WindSplashAdRequest splashAdRequest = new WindSplashAdRequest(PLACEMENT_ID, USER_ID,OPTIONS);
        splashAdRequest.setDisableAutoHideAd(true);
        // 广告允许最大等待返回时间
        //splashAdRequest.setFetchDelay(5);
        WindSplashAD mWindSplashAD = new WindSplashAD(splashAdRequest, new WindSplashADListener() {
            @Override
            public void onSplashAdShow(String placementId) {
                i(TAG, "广告曝光");
                SplashActivity.onSplashAdLoaded(context, adContainer);
            }

            @Override
            public void onSplashAdLoadSuccess(String placementId) {

                i(TAG, "广告加载成功");
            }

            @Override
            public void onSplashAdLoadFail(WindAdError error, String placementId) {

                i(TAG, "广告加载失败，错误信息："+error.getMessage()+"("+error.getErrorCode()+")");
                SplashActivity.goToMainActivity(context);
            }

            @Override
            public void onSplashAdShowError(WindAdError error, String placementId) {
                i(TAG, "广告曝光失败，错误信息："+error.getMessage()+"("+error.getErrorCode()+")");
                SplashActivity.goToMainActivity(context);

            }

            @Override
            public void onSplashAdClick(String placementId) {

                i(TAG, "用户点击了广告");
            }

            @Override
            public void onSplashAdClose(String placementId) {

                i(TAG, "广告关闭");
                SplashActivity.goToMainActivity(context);
            }

            @Override
            public void onSplashAdSkip(String placementId) {

                i(TAG, "广告被跳过");
                SplashActivity.goToMainActivity(context);
            }
        });
        mWindSplashAD.loadAndShow(adContainer); // 不需要再调用 mWindSplashAD.showAd();
    }
    public static void SigmobNativeAd(Context context, String placementId, String userId, @NonNull ViewGroup adContainer){
        adContainer.removeAllViews();

        View unifiedView = LayoutInflater.from(adContainer.getContext()).inflate(R.layout.ad_list,null);
        ImageView adImage = unifiedView.findViewById(R.id.ad_image);
        Button adButton = unifiedView.findViewById(R.id.ad_button);
        ViewGroup adVideo = unifiedView.findViewById(R.id.ad_video);
        ImageView adIcon = unifiedView.findViewById(R.id.ad_icon);
        TextView adTitle = unifiedView.findViewById(R.id.ad_title);
        ImageView adClose = unifiedView.findViewById(R.id.ad_close);

        WindNativeUnifiedAd windNativeUnifiedAd = new WindNativeUnifiedAd(new WindNativeAdRequest(placementId, userId, null));
        windNativeUnifiedAd.setNativeAdLoadListener(new WindNativeUnifiedAd.WindNativeAdLoadListener() {
            @Override
            public void onAdError(WindAdError error, String placementId) {
                e("Sigmob广告","广告加载失败，错误信息："+error.getMessage()+"("+error.getErrorCode()+")");
                adContainer.removeAllViews();
            }
            @Override
            public void onAdLoad(List<WindNativeAdData> adDataList, String placementId) {
                d("Sigmob广告", "onAdLoaded");
                WindNativeAdData windNativeAdData = adDataList.get(0);
                // 返回广告的标题
                String title = windNativeAdData.getTitle();
                if (!TextUtils.isEmpty(title)) {
                    adTitle.setVisibility(View.VISIBLE);
                    adTitle.setText(title);
                }

                // 返回广告的描述文本信息
                String CTA = windNativeAdData.getCTAText();
                if (!TextUtils.isEmpty(CTA)) {
                    adButton.setVisibility(View.VISIBLE);
                    adButton.setText(CTA);
                }

                // 返回广告的AppIcon的图片URL
                String iconUrl = windNativeAdData.getIconUrl();
                if (!TextUtils.isEmpty(iconUrl)) {
                    // 需要开发者自己处理图片URL，可使用图片加载框架去处理，本示例使用glide加载仅供参考
                    Glide.with(context).load(iconUrl).into(adIcon);
                }

                if (windNativeAdData.getAdPatternType() == NativeAdPatternType.NATIVE_VIDEO_AD) {
                    // 视频容器背景默认设置为黑色
                    adVideo.setBackgroundColor(Color.BLACK);
                    windNativeAdData.bindMediaView(adVideo, new WindNativeAdData.NativeADMediaListener() {
                        @Override
                        public void onVideoStart() {
                            d("Sigmob广告", "onVideoStart: ");
                        }

                        @Override
                        public void onVideoPause() {
                            d("Sigmob广告", "onVideoPause: ");
                        }

                        @Override
                        public void onVideoResume() {
                            d("Sigmob广告", "onVideoResume: ");
                        }

                        @Override
                        public void onVideoCompleted() {
                            windNativeAdData.startVideo();
                            d("Sigmob广告", "onVideoCompleted: ");
                        }

                        @Override
                        public void onVideoError(WindAdError windAdError) {
                            d("Sigmob广告", "onVideoError: " + windAdError.toString());
                            adContainer.removeAllViews();
                        }

                        @Override
                        public void onVideoLoad() {
                            d("Sigmob广告", "onVideoLoad: ");
                        }
                    });
                } else if (windNativeAdData.getAdPatternType() == NativeAdPatternType.NATIVE_BIG_IMAGE_AD) {
                    List<ImageView> imageViews=new ArrayList<>();
                    imageViews.add(adImage);
                    windNativeAdData.bindImageViews(imageViews, 0);
                }
                // 把允许点击的View添加到集合里面
                ArrayList<View> clickViews = new ArrayList<>();
                clickViews.add(adImage);
                clickViews.add(adVideo);
                clickViews.add(adButton);
                windNativeAdData.bindViewForInteraction(adContainer, clickViews, clickViews, null, new NativeADEventListener() {
                    @Override
                    public void onAdExposed() {

                    }

                    @Override
                    public void onAdClicked() {

                    }

                    @Override
                    public void onAdDetailShow() {

                    }

                    @Override
                    public void onAdDetailDismiss() {

                    }

                    @Override
                    public void onAdError(WindAdError error) {
                        adContainer.removeAllViews();
                    }
                });
                adClose.setOnClickListener(v -> adContainer.removeAllViews());
            }
        });
        windNativeUnifiedAd.loadAd(1);
        // 把自定义View添加到广告容器里面
        adContainer.removeAllViews();
        adContainer.addView(unifiedView);
    }
}
