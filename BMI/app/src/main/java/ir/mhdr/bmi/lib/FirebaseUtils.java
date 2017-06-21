package ir.mhdr.bmi.lib;

import android.app.Activity;
import android.content.Context;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.analytics.FirebaseAnalytics;

/**
 * Created by mahmood on 6/19/17.
 */

public class FirebaseUtils {


    public final static String Bazaar = "Bazaar";
    public final static String GooglePlayStore = "Bazaar";
    public final static String IranApps = "Bazaar";
    public final static String AvalMarket = "Bazaar";

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
