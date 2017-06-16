package ir.mhdr.bmi;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.multidex.MultiDexApplication;

import net.danlew.android.joda.JodaTimeAndroid;
import net.time4j.android.ApplicationStarter;

import java.util.Locale;

import ir.mhdr.bmi.lib.Update;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;


public class MyApplication extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/BYekan.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());

        ApplicationStarter.initialize(this, true); // time4j
        JodaTimeAndroid.init(this); // joda
    }

    @Override
    protected void attachBaseContext(Context base) {
        Resources res = base.getResources();

        Locale locale = new Locale("en");
        Locale.setDefault(locale);

        Configuration config = new Configuration();
        config.locale = locale;

        res.updateConfiguration(config, res.getDisplayMetrics());

        super.attachBaseContext(base);
    }
}


