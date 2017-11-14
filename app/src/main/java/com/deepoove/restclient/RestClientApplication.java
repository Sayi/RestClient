package com.deepoove.restclient;

import android.support.multidex.MultiDexApplication;

import com.deepoove.restclient.conf.Settings;
import com.deepoove.restclient.conf.Template;
import com.deepoove.restclient.request.VolleySingleton;
import com.umeng.analytics.MobclickAgent;

/**
 * @author Sayi
 */

public class RestClientApplication extends MultiDexApplication {


    public static final String PREFS_NAME = "conf_prefs";

    private static Settings settings;


    @Override
    public void onCreate() {
        super.onCreate();
        settings = new Settings(this, PREFS_NAME);
        Template.getInstance(getResources());
        VolleySingleton.getInstance(getApplicationContext());

        MobclickAgent.setScenarioType(getApplicationContext(), MobclickAgent.EScenarioType.E_UM_NORMAL);
        MobclickAgent.setDebugMode(false);
    }

    public static Settings getSettings() {
        return settings;
    }
}
