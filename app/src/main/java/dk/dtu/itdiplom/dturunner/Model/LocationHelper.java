package dk.dtu.itdiplom.dturunner.Model;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import dk.dtu.itdiplom.dturunner.SingletonDtuRunner;
import dk.dtu.itdiplom.dturunner.Views.FragmentLoeb;

/**
 * Created by JanMøller on 21-11-2015.
 */
public class LocationHelper {

    /** * Det ønskede interval for location opdateringer. Upræcis, opdateringer vil forekomme mere eller mindre præcist.  */
    //public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 6000;    // todo jan - bør ligge i settings...
    /** * Hurtigste rate for lokationsopdateringer. Præcis. Opdateringer vil aldrig være oftere end denne værdi.          */
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = UPDATE_INTERVAL_IN_MILLISECONDS / 2;

    protected static final String TAG = "JJ-LocationHelper";

    public static void createLocationRequest()
    {
        SingletonDtuRunner.loebsStatus.locationGoogleApi.locationRequest = new LocationRequest();

        // Sets the desired interval for active location updates. This interval is
        // inexact. You may not receive updates at all if no location sources are available, or
        // you may receive them slower than requested. You may also receive updates faster than
        // requested if other applications are requesting location at a faster interval.
        SingletonDtuRunner.loebsStatus.locationGoogleApi.locationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);

        // Sets the fastest rate for active location updates. This interval is exact, and your
        // application will never receive updates faster than this value.
        SingletonDtuRunner.loebsStatus.locationGoogleApi.locationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        SingletonDtuRunner.loebsStatus.locationGoogleApi.locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    /**
     * Builds a GoogleApiClient. Uses the {@code #addApi} method to request the
     * LocationServices API.
     *
     */
    public static synchronized void buildGoogleApiClient(Context context, FragmentLoeb fragmentLoeb) {

        Log.i(TAG, "Building GoogleApiClient");
        SingletonDtuRunner.loebsStatus.locationGoogleApi.googleApiClient = new GoogleApiClient.Builder(context)    // todo jan - måtte ændre fra this til getContext() eller getActivity(). brug getActivity() siger Jakob
                .addConnectionCallbacks(fragmentLoeb)
                .addOnConnectionFailedListener(fragmentLoeb)
                .addApi(LocationServices.API)               // note jan - her vælges Location API... Der er også en FusedLocationApi... Dette er OK!
                .build();
        LocationHelper.createLocationRequest();
    }

    public static void askToEnableGps2(final Context context) {
        // Build the alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Placerings tjenester er ikke aktiveret");
        builder.setMessage("Aktiverer venligst placeringstjenester (gps) for at kunne anvende appen!");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                // Show location settings when the user acknowledges the alert dialog
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                context.startActivity(intent);

            }
        });

        builder.setNegativeButton("Annuller", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        Dialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

    private void askToEnableGps(Context context) {
        Intent myIntent = new Intent( Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        context.startActivity(myIntent);
    }

    public static boolean checkForUserEnabledGpsSettings(Context context) {

        Log.d("TAG todo jan", "Tester om GPS settings er aktiveret!");

        // Get Location Manager and check for GPS & Network location services
        LocationManager lm = (LocationManager) context.getSystemService(context.LOCATION_SERVICE);
        logGpsSettingsStatus(lm);

        // todo jan - tag stilling til om GPS skal være aktiveret...
        if(!lm.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                !lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
        {
            Log.d(TAG, "GPS settings is not enabled!");
            LocationHelper.askToEnableGps2(context);
            return false;
        }

        Log.d(TAG, "GPS settings is already enabled!");

        return true;
    }

    private static void logGpsSettingsStatus(LocationManager lm) {
        // note jan - ekstra logning for at se hvad der ikke er aktiveret
        if(!lm.isProviderEnabled(LocationManager.GPS_PROVIDER))
        {
            Log.d(TAG, "GPS (GPS_PROVIDER) settings is not enabled!");
        }
        if(!lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
        {
            Log.d(TAG, "GPS (NETWORK_PROVIDER) settings is not enabled!");
        }
    }
}
