package dk.dtu.itdiplom.dturunner.Model.Entities;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;

/**
 *
 * Created by JanMøller on 21-11-2015.
 */
public class LocationGoogleApi {



    public static GoogleApiClient googleApiClient;
    public static Boolean requestingLocationUpdates;
    /** * requests til FusedLocationProviderApi.      */
    public static LocationRequest locationRequest;


//    public void testtestcreateLocationRequest()
//    {
//        this.locationRequest = new LocationRequest();
//        locationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
//        locationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
//        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//    }
}
