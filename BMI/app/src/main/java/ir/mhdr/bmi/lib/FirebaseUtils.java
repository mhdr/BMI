package ir.mhdr.bmi.lib;

import android.app.Activity;
import android.content.Context;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.analytics.FirebaseAnalytics;

public class FirebaseUtils {

    public class AppStoreSource {
        public final static String Dev = "Dev";
        public final static String Bazaar = "Bazaar";
        public final static String GooglePlayStore = "GooglePlayStore";
        public final static String IranApps = "IranApps";
        public final static String AvalMarket = "AvalMarket";
        public final static String MyKet = "Myket";
        public final static String ParsHub = "ParsHub";
        public final static String Cando = "Cando";
        public final static String Pupli = "Pupli";
    }

    public class UserProperty {
        public final static String InstallSource = "InstallSource";
        public final static String NumberOfProfiles = "NumberOfProfiles";
        public final static String NumberOfWeightRecords = "NumberOfWeightRecords";
        public final static String UsageInterval = "UsageInterval";
        public final static String UserAge = "UserAge";
        public final static String UserGender = "UserGender";
        public final static String UUID = "UUID";
    }


    /**
     * check google play service availability
     *
     * @param context
     * @return
     */
    public static boolean checkPlayServices(Context context) {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(context);
        if (result != ConnectionResult.SUCCESS) {
            return false;
        }

        return true;
    }

    /**
     * check google play service availability and make it available
     *
     * @param context
     * @param activity
     */
    public static void makePlayServicesAvailable(Context context, Activity activity) {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(context);
        if (result != ConnectionResult.SUCCESS) {
            if (googleAPI.isUserResolvableError(result)) {
                //googleAPI.getErrorDialog(this, result, PLAY_SERVICES_RESOLUTION_REQUEST).show();
                googleAPI.makeGooglePlayServicesAvailable(activity);
            }
        }
    }
}
