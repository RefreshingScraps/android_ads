package com.example.ads;
import static com.example.ads.SettingsUtils.getSetting;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;

import com.example.ads.ads.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Init {
    public static Map<AdPlatform, Boolean> adSDKisLoaded = new HashMap<>();
    public static List<Boolean> getSettings(Context context, List<String> settingKeys){
        List<Boolean> settingsEnabled = new ArrayList<>();
        for(String item : settingKeys){
            settingsEnabled.add(getSetting(context, item, false));
        }
        return settingsEnabled;
    }
}
