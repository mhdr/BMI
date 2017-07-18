package ir.mhdr.bmi;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.multidex.MultiDexApplication;

import net.danlew.android.joda.JodaTimeAndroid;

import org.greenrobot.greendao.database.Database;

import java.util.Locale;

import ir.mhdr.bmi.blDao.Import;
import ir.mhdr.bmi.blDao.PrivateSettingBL;
import ir.mhdr.bmi.dao.DaoMaster;
import ir.mhdr.bmi.lib.Statics;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;


public class MyApplication extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/BYekan.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());

        JodaTimeAndroid.init(this); // joda

        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "bmi2");
        Database db = helper.getWritableDb();
        Statics.daoSession = new DaoMaster(db).newSession();

        PrivateSettingBL privateSettingBL = new PrivateSettingBL();
        privateSettingBL.createSettings();

        Import importDb=new Import(getApplicationContext());
        importDb.fromV1ToV2();
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


