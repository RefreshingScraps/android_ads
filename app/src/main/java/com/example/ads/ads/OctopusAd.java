package com.example.ads.ads;
import com.bumptech.glide.Glide;
import com.example.ads.AdPlatform;
import com.example.ads.Init;
import com.example.ads.R;
import com.example.ads.SplashActivity;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.octopus.ad.NativeAd;
import com.octopus.ad.NativeAdListener;
import com.octopus.ad.NativeAdResponse;
import com.octopus.ad.SplashAd;
import static com.example.ads.DevUtil.*;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.octopus.ad.SplashAdListener;
import com.octopus.ad.Octopus;
import com.octopus.ad.internal.nativead.NativeAdEventListener;
import com.octopus.ad.model.ComplianceInfo;

import java.util.ArrayList;

public class OctopusAd {
    private static final String TAG = "章鱼移动 SDK";
    public static void InitOctopusSDK(Context applicationContext,String appId){
        Octopus.init(applicationContext, appId);
        Octopus.setIsDownloadDirect(true);
        Init.adSDKisLoaded.put(AdPlatform.OCTOPUS, true);
    }
    private static SplashAd mSplashAd;
    public static void OctopusSplashAd(Context context,String slotId,ViewGroup container){
        SplashAdListener listener = new SplashAdListener() {
            @Override
            public void onAdLoaded() {
                i(TAG,"广告加载成功");
                mSplashAd.showAd();
//                i(TAG, "onAdLoaded");
                // 新版本支持开启广告预缓存，减少请求响应时间，需要的话联系运营开启。
                // 开启预缓存后，展示之前要先判断广告是否过期，避免展示过期广告影响收益。
//                if (mSplashAd.isValid()) {

//                } else {
//                    jump();
//                }
                // 广告在此竞价 mSplashAd.getPrice(); 单位分
                // 注意：竞价结束后记得调用竞胜或竞败接口
                // 竟胜时候调用(提高ECPM, 提升填充率)
                // mSplashAd.sendWinNotice(第二高价格);
                // 竟败时候调用(提高ECPM, 提升填充率)
                // mSplashAd.sendLossNotice(最高价, ADBidEvent.PRICE_LOW_FILTER,ADBidEvent.OTHER);
            }

            @Override
            public void onAdCacheLoaded(boolean isSuccess) {
//                i(TAG, "onAdCacheLoaded: " + isSuccess);
                i(TAG,"广告加载状态"+isSuccess);
                // 图片素材和视频资源缓存到本地的回调，在此展示效果最好。
                // 注意：视频等资源缓存失败会回调onAdFailedToLoad
                // 广告是否加载成功并且在有效时间内
                // isSuccess可以用来判断是否缓存成功，没缓存成功也可以播放在线视频
            }

            @Override
            public void onAdShown() {
                i(TAG,"广告成功展示");
                SplashActivity.onSplashAdLoaded(context, container);
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                e(TAG,"广告加载失败，错误码："+errorCode);
                SplashActivity.goToMainActivity(context);
            }

            @Override
            public void onAdClosed() {

                i(TAG,"广告关闭");
                SplashActivity.goToMainActivity(context);
            }

            @Override
            public void onAdClicked() {
                i(TAG,"广告被点击");
            }

            @Override
            public void onAdTick(long millisUnitFinished) {
            }
        };
        mSplashAd = new com.octopus.ad.SplashAd(context, slotId, container,listener);
        mSplashAd.openAdInNativeBrowser(true);
    }
    private static NativeAd mNativeAd;
    public static void OctopusNativeAd(Context context,String slotId,ViewGroup adContainer) {
        mNativeAd = new NativeAd(context, slotId, new NativeAdListener() {

            @Override
            public void onAdFailed(int errorCode) {
                d(TAG, "onAdFailed:" + errorCode);
            }

            @Override
            public void onAdLoaded(NativeAdResponse response) {
                d(TAG, "onAdLoaded");
                // 广告是否加载成功并且在有效时间内
                if (mNativeAd != null && mNativeAd.isValid()) {

                    if (response == null) return;
                    View unifiedView = LayoutInflater.from(context).inflate(R.layout.ad_list,null);
                    RelativeLayout rlContainer = unifiedView.findViewById(R.id.rl_container);
                    FrameLayout adVideo = unifiedView.findViewById(R.id.ad_video);
                    ImageView adImage = unifiedView.findViewById(R.id.ad_image);
                    ImageView adIcon = unifiedView.findViewById(R.id.ad_icon);
                    TextView adTitle = unifiedView.findViewById(R.id.ad_title);
                    TextView adDesc = unifiedView.findViewById(R.id.ad_desc);
                    Button adButton = unifiedView.findViewById(R.id.ad_button);
                    ImageView adLogo = unifiedView.findViewById(R.id.ad_logo);
                    ImageView adLogoText = unifiedView.findViewById(R.id.ad_logo_text);
                    ImageView adClose = unifiedView.findViewById(R.id.ad_close);
                    FrameLayout adCompliance = unifiedView.findViewById(R.id.ad_compliance);

                    i("OctopusDemo", TAG
                            + "response imageUrl:" + response.getImageUrl()
                            + "\n;IconUrl:" + response.getIconUrl()
                            + "\n;getTitle:" + response.getTitle()
                            + "\n;getDescription:" + response.getDescription()
                            + "\n;getButtonText:" + response.getButtonText()
                            + "\n;getLogoUrl:" + response.getLogoUrl()
                            + "\n;getTextLogoUrl:" + response.getTextLogoUrl()
                    );

                    // 返回广告的标题
                    String title = response.getTitle();
                    if (!TextUtils.isEmpty(title)) {
                        adTitle.setVisibility(View.VISIBLE);
                        adTitle.setText(title);
                    }

                    // 返回广告的描述文本信息
                    String description = response.getDescription();
                    if (!TextUtils.isEmpty(description)) {
                        adDesc.setVisibility(View.VISIBLE);
                        adDesc.setText(description);
                    }

                    // 返回广告的AppIcon的图片URL
                    String iconUrl = response.getIconUrl();
                    if (!TextUtils.isEmpty(iconUrl)) {
                        // 需要开发者自己处理图片URL，可使用图片加载框架去处理，本示例使用glide加载仅供参考
                        Glide.with(context).load(iconUrl).into(adIcon);
                    }

                    // 判断是否是视频有两种方式，二选一就可以
                    if (response.isVideo()) {
                        // 获取视频组件
                        View view = response.getVideoView(context);
                        if (view != null) {
                            // 视频容器背景默认设置为黑色
                            adVideo.setBackgroundColor(Color.BLACK);
                            // 视频居中展示
                            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, Gravity.CENTER);
                            adVideo.addView(view, layoutParams);
                        }
                    } else {
                        // 图片渲染有两种，一种是直接拿渲染好的高斯模糊组件，防止图片变形
                        // 方法一：获取高斯模糊组件
                        View view = response.getBlurView(context);
                        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, Gravity.CENTER);
                        adVideo.addView(view, layoutParams);
                    }

                    // 返回点击按钮的文本信息
                    adButton.setText(response.getButtonText());

                    // 请开发者务必自己实现加入sdk的logo及广告字样
                    // 返回广告的logoURL
                    String logoUrl = response.getLogoUrl();
                    if (!TextUtils.isEmpty(logoUrl)) {
                        Glide.with(context).load(logoUrl).into(adLogo);
                    }

                    // 返回广告的文字logoURL
                    String textLogoUrl = response.getTextLogoUrl();
                    if (!TextUtils.isEmpty(textLogoUrl)) {
                        Glide.with(context).load(textLogoUrl).into(adLogoText);
                    }

                    // 获取交互类型，判断是否是下载广告
                    if (response.getInteractionType() == NativeAdResponse.INTERACTION_TYPE_DOWNLOAD) {
                        // 获取广告下载六要素
                        ComplianceInfo complianceInfo = response.getComplianceInfo();
                        // 可以选择自己添加下载六要素
                        // 也可以直接添加下载六要素控件
                        if (complianceInfo != null) {
                            // 下载六要素默认为屏幕宽度
                            // View complianceView = response.getComplianceView(this);
                            // 下载六要素指定宽度，单位dp，用于缩放文字比例
                            View complianceView = response.getComplianceView(context, 360);
                            FrameLayout.LayoutParams complianceParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
                            adCompliance.addView(complianceView, complianceParams);
                        }
                    }

                    // 添加摇一摇 shakeViewSize：摇一摇控件的宽高，单位dp  shakeTextSize 摇一摇文案字体大小，单位sp
                    response.addShakeView(rlContainer, 100, 10);

                    // 把自定义View添加到广告容器里面
                    adContainer.removeAllViews();
                    adContainer.addView(unifiedView);

                    // 把允许点击的View添加到集合里面
                    ArrayList<View> clickViews = new ArrayList<>();
                    clickViews.add(adImage);
                    clickViews.add(adButton);
                    // 把允许关闭的View添加到集合里面
                    ArrayList<View> closeViews = new ArrayList<>();
                    closeViews.add(adClose);
                    // 注册原生自渲染广告的曝光点击事件，必须调用
                    response.bindUnifiedView(adContainer, clickViews, closeViews, new NativeAdEventListener() {
                        @Override
                        public void onAdClick() {
                            i(TAG, "onAdClick");
                        }

                        @Override
                        public void onADExposed() {
                            i(TAG, "onADExposed");
                        }

                        @Override
                        public void onAdRenderFailed(int errorCode) {
                            i(TAG, "onAdRenderFailed");
//                            Toast.makeText(NativeUnifiedActivity.this, "onAdRenderFailed reason: " + errorCode, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onAdClose() {
                            i(TAG, "onAdClose");
                            adContainer.removeAllViews();
                        }
                    });
                }
                // 注意：竞价结束后记得调用竞胜或竞败接口
                // 竟胜时候调用(提高ECPM, 提升填充率)
                // mNativeAd.sendWinNotice(第二高价格);
                // 竟败时候调用(提高ECPM, 提升填充率)
                // mNativeAd.sendLossNotice(最高价, ADBidEvent.PRICE_LOW_FILTER,ADBidEvent.OTHER);
            }
        });
//使用SDK内部浏览器打开落地页（默认为false，设为false的情况下会使用本地浏览器打开）
        mNativeAd.openAdInNativeBrowser(true);
        mNativeAd.loadAd();
    }

}
