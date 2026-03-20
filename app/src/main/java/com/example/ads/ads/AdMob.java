package com.example.ads.ads;

import android.app.Activity;
import android.content.Context;
import static com.example.ads.DevUtil.*;

import androidx.annotation.NonNull;

import com.example.ads.SplashActivity;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.appopen.AppOpenAd;

public class AdMob {
    private static final String TAG = "AdMob Ad";
    public static void initAdMobSDK(Context context){
        new Thread(
                () -> {
                    // Initialize the Google Mobile Ads SDK on a background thread.
                    MobileAds.initialize(context, initializationStatus -> {});
                })
                .start();
    }
    public static void AdMobSplashAd(@NonNull final Activity activity, String adUnitId) {
        AppOpenAd.load(
                activity.getApplicationContext(),
                adUnitId,
                new AdRequest.Builder().build(),
                new AppOpenAd.AppOpenAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull AppOpenAd ad) {
                        // Called when an app open ad has loaded.
                        d(TAG, "App open ad loaded.");
                        //SplashActivity.onAdLoaded(context);
                        ad.setFullScreenContentCallback(
                                new FullScreenContentCallback() {
                                    @Override
                                    public void onAdDismissedFullScreenContent() {
                                        // Called when full screen content is dismissed.
                                        d(TAG, "Ad dismissed fullscreen content.");
                                        SplashActivity.goToMainActivity(activity.getApplicationContext());
                                    }

                                    @Override
                                    public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                                        // Called when full screen content failed to show.
                                        d(TAG, adError.getMessage());
                                        SplashActivity.goToMainActivity(activity.getApplicationContext());
                                    }

                                    @Override
                                    public void onAdShowedFullScreenContent() {
                                        d(TAG, "Ad showed fullscreen content.");
                                    }

                                    @Override
                                    public void onAdImpression() {
                                        // Called when an impression is recorded for an ad.
                                        d(TAG, "The ad recorded an impression.");
                                    }

                                    @Override
                                    public void onAdClicked() {
                                        // Called when ad is clicked.
                                        d(TAG, "The ad was clicked.");
                                    }
                                });
                        ad.show(activity);
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Called when an app open ad has failed to load.
                        d(TAG, "App open ad failed to load with error: " + loadAdError.getMessage());
                        SplashActivity.goToMainActivity(activity.getApplicationContext());
                    }
                });
    }
}
