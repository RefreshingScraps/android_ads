package com.example.ads.ads;

import android.text.TextUtils;
import static com.example.ads.DevUtil.*;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mobads.sdk.api.BDAdConfig;
import com.baidu.mobads.sdk.api.BDDialogParams;
import com.baidu.mobads.sdk.api.BaiduNativeManager;
import com.baidu.mobads.sdk.api.ExpressInterstitialAd;
import com.baidu.mobads.sdk.api.ExpressInterstitialListener;
import com.baidu.mobads.sdk.api.MobadsPermissionSettings;
import com.baidu.mobads.sdk.api.NativeResponse;
import com.baidu.mobads.sdk.api.RequestParameters;
import com.baidu.mobads.sdk.api.RewardVideoAd;
import com.baidu.mobads.sdk.api.SplashAd;
import com.baidu.mobads.sdk.api.SplashInteractionListener;
import com.bumptech.glide.Glide;
import com.example.ads.AdPlatform;
import com.example.ads.Init;
import com.example.ads.R;
import com.example.ads.SplashActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BaiduAd {
    public static String TAG = "白青藤广告 SDK";
    public static void InitBaiduSDK(Context context, String Appsid) {
        BDAdConfig bdAdConfig = new BDAdConfig.Builder()
                // 1、设置app名称，可选
                // 2、应用在mssp平台申请到的appsid，和包名一一对应，此处设置等同于在AndroidManifest.xml里面设置
                .setAppsid(Appsid)
                // 3、设置下载弹窗的类型和按钮动效样式，可选
                .setDialogParams(new BDDialogParams.Builder()
                        .setDlDialogType(BDDialogParams.TYPE_BOTTOM_POPUP)
                        .setDlDialogAnimStyle(BDDialogParams.ANIM_STYLE_NONE)
                        .build())
                // 4、控制台debug日志调试开关 接入调试阶段可以打开，上线前需关闭
                .setDebug(isDebug())
                // 6、如需获知SDK初始化结果，可选择性注册监听
                .setBDAdInitListener(new BDAdConfig.BDAdInitListener() {
                    @Override
                    public void success() {
                        i(TAG, "SDK初始化成功");
                        Init.adSDKisLoaded.put(AdPlatform.BAIDU, true);
                    }

                    @Override
                    public void fail() {
                        e(TAG, "SDK初始化失败");
                    }
                })
                .build(context);
        bdAdConfig.init();
        // 设置SDK可以使用的权限，包含：设备信息、定位、存储
        // 注意：建议授权SDK读取设备信息，SDK会在应用获得系统权限后自行获取IMEI等设备信息
        // 授权SDK获取设备信息会有助于提升ECPM
        MobadsPermissionSettings.setPermissionReadDeviceID(true);
        MobadsPermissionSettings.setPermissionLocation(true);
        MobadsPermissionSettings.setPermissionStorage(true);
    }
    public static void BaiduSplashAd(Context context, String adPlaceId, ViewGroup viewGroup){
        SplashAd mSplashAd = new SplashAd(context, adPlaceId, new SplashInteractionListener() {
            @Override
            public void onLpClosed() {

            }

            @Override
            public void onAdPresent() {
                i(TAG, "广告成功展示");
            }

            @Override
            public void onAdExposed() {
                SplashActivity.onSplashAdLoaded(context, viewGroup);
            }

            @Override
            public void onAdClick() {
                i(TAG, "用户点击了广告");
            }

            @Override
            public void onAdCacheSuccess() {

            }

            @Override
            public void onAdCacheFailed() {

            }

            @Override
            public void onAdDismissed() {
                // 广告关闭，跳转到主页面
                i(TAG, "广告关闭");
                SplashActivity.goToMainActivity(context);
            }
            @Override
            public void onAdSkip(){
                i(TAG, "广告被跳过");
                SplashActivity.goToMainActivity(context);
            }
            @Override
            public void onADLoaded() {
                i(TAG, "广告加载成功");
            }
            @Override
            public void onAdFailed(String errorMessage) {
                e(TAG, "广告加载失败，错误信息：" + errorMessage);
                SplashActivity.goToMainActivity(context);
            }
        });
        mSplashAd.loadAndShow(viewGroup);
    }
    public static void BaiduFeedAd(Context context,String adPlaceId,ViewGroup adContainer){
        adContainer.removeAllViews();

        View unifiedView = LayoutInflater.from(context).inflate(R.layout.ad_list,null);
        ImageView adImage = unifiedView.findViewById(R.id.ad_image);
        ImageView adIcon = unifiedView.findViewById(R.id.ad_icon);
        TextView adTitle = unifiedView.findViewById(R.id.ad_title);
        TextView adDesc = unifiedView.findViewById(R.id.ad_desc);
        Button adButton = unifiedView.findViewById(R.id.ad_button);
        ImageView adLogo = unifiedView.findViewById(R.id.ad_logo);
        ImageView adLogoText = unifiedView.findViewById(R.id.ad_logo_text);
        ImageView adClose = unifiedView.findViewById(R.id.ad_close);

        BaiduNativeManager baiduNativeManager = new BaiduNativeManager(context,adPlaceId);
        RequestParameters requestParameters = new RequestParameters.Builder().build();

        baiduNativeManager.loadFeedAd(requestParameters, new BaiduNativeManager.FeedAdListener() {
            @Override
            public void onNativeLoad(List<NativeResponse> list) {
                i("百青藤广告","广告加载成功");
                NativeResponse nativeResponse = list.get(0);

                // 返回广告的标题
                String title = nativeResponse.getTitle();
                if (!TextUtils.isEmpty(title)) {
                    adTitle.setVisibility(View.VISIBLE);
                    adTitle.setText(title);
                }

                // 返回广告的AppIcon的图片URL
                String iconUrl = nativeResponse.getIconUrl();
                if (!TextUtils.isEmpty(iconUrl)) {
                    adIcon.setVisibility(View.VISIBLE);
                    // 需要开发者自己处理图片URL，可使用图片加载框架去处理，本示例使用glide加载仅供参考
                    Glide.with(context).load(iconUrl).into(adIcon);
                }

                // 返回广告的AppIcon的图片URL
                String imageUrl = nativeResponse.getImageUrl();
                if (!TextUtils.isEmpty(imageUrl)) {
                    adImage.setVisibility(View.VISIBLE);
                    // 需要开发者自己处理图片URL，可使用图片加载框架去处理，本示例使用glide加载仅供参考
                    Glide.with(context).load(imageUrl).into(adImage);
                }

                // 请开发者务必自己实现加入sdk的logo及广告字样
                // 返回广告的logoURL
                String logoUrl = nativeResponse.getBaiduLogoUrl();
                if (!TextUtils.isEmpty(logoUrl)) {
                    adLogo.setVisibility(View.VISIBLE);
                    Glide.with(context).load(logoUrl).into(adLogo);
                }

                String description = nativeResponse.getDesc();
                if (!TextUtils.isEmpty(description)) {
                    adDesc.setVisibility(View.VISIBLE);
                    adDesc.setText(description);
                }

                // 返回广告的文字logoURL
                String textLogoUrl = nativeResponse.getAdLogoUrl();
                if (!TextUtils.isEmpty(textLogoUrl)) {
                    adLogoText.setVisibility(View.VISIBLE);
                    Glide.with(context).load(textLogoUrl).into(adLogoText);
                }

                String C = nativeResponse.getActButtonString();
                if(!TextUtils.isEmpty(C)) {
                    adButton.setVisibility(View.VISIBLE);
                    // 返回广告的描述文本信息
                    adButton.setText(C);
                }
                adClose.setOnClickListener(v -> adContainer.removeAllViews());

                // 把允许点击的View添加到集合里面
                ArrayList<View> clickViews = new ArrayList<>();
                clickViews.add(adImage);
                clickViews.add(adTitle);
                clickViews.add(adLogo);
                clickViews.add(adIcon);
                clickViews.add(adButton);

                nativeResponse.registerViewForInteraction(adContainer, clickViews, clickViews, new NativeResponse.AdInteractionListener() {
                    @Override
                    public void onAdClick() {
                        i("百青藤广告","广告被点击");
                    }

                    @Override
                    public void onADExposed() {
                        i("百青藤广告 SDK","广告曝光");
                        //SplashActivity.onAdLoaded(clickViews);
                    }

                    @Override
                    public void onADExposureFailed(int i) {
                        e("百青藤广告 SDK","广告曝光失败，错误码："+i);
                        adContainer.removeAllViews();
                    }

                    @Override
                    public void onADStatusChanged() {

                    }

                    @Override
                    public void onAdUnionClick() {

                    }
                });
                // 把自定义View添加到广告容器里面
                adContainer.removeAllViews();
                adContainer.addView(unifiedView);
            }

            @Override
            public void onNativeFail(int i, String s, NativeResponse nativeResponse) {
                e("百青藤广告 SDK","广告加载失败，错误信息："+s+"("+i+")");
                adContainer.removeAllViews();
            }

            @Override
            public void onNoAd(int i, String s, NativeResponse nativeResponse) {
                e("百青藤广告 SDK","没有广告返回，错误信息："+s+"("+i+")");
                adContainer.removeAllViews();
            }

            @Override
            public void onVideoDownloadSuccess() {
                i("百青藤广告 SDK","视频物料下载成功");
            }

            @Override
            public void onVideoDownloadFailed() {
                e("百青藤广告 SDK","视频物料下载失败");
                adContainer.removeAllViews();
            }

            @Override
            public void onLpClosed() {
                i("百青藤广告 SDK","lp页面被关闭" );
            }
        });
    }
    public static void BaiduExpressInterstitialAd(Context context, String adPlaceId){
        ExpressInterstitialAd expressInterstitialAd = new ExpressInterstitialAd(context,adPlaceId){};
        expressInterstitialAd.setLoadListener(new ExpressInterstitialListener() {
            @Override
            public void onADLoaded() {

            }

            @Override
            public void onAdClick() {

            }

            @Override
            public void onAdClose() {

            }

            @Override
            public void onAdFailed(int i, String s) {

            }

            @Override
            public void onNoAd(int i, String s) {

            }

            @Override
            public void onADExposed() {
            }

            @Override
            public void onADExposureFailed() {

            }

            @Override
            public void onAdCacheSuccess() {

            }

            @Override
            public void onAdCacheFailed() {

            }

            @Override
            public void onLpClosed() {

            }
        });
        expressInterstitialAd.load();
        expressInterstitialAd.show();
    }
    public static void BaiduRewardVideoAd(Context context, String adPlaceId){
        RewardVideoAd rewardVideoAd = new RewardVideoAd(context, adPlaceId, new RewardVideoAd.RewardVideoAdListener() {
            @Override
            public void onAdShow() {

            }

            @Override
            public void onAdClick() {

            }

            @Override
            public void onAdClose(float v) {

            }

            @Override
            public void onAdFailed(String s) {

            }

            @Override
            public void onVideoDownloadSuccess() {

            }

            @Override
            public void onVideoDownloadFailed() {

            }

            @Override
            public void playCompletion() {

            }

            @Override
            public void onAdLoaded() {

            }

            @Override
            public void onAdSkip(float v) {

            }

            @Override
            public void onRewardVerify(boolean b) {

            }

        });
        rewardVideoAd.load();
        rewardVideoAd.show();
    }

}