package dk.dtu.itdiplom.dturunner.Views;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
//import android.app.Fragment;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

// Disse imports er til for at kunne anvende fused location:
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import dk.dtu.itdiplom.dturunner.Database.DatabaseHelper;
import dk.dtu.itdiplom.dturunner.Model.Entities.LoebsAktivitet;
import dk.dtu.itdiplom.dturunner.Model.LocationHelper;
import dk.dtu.itdiplom.dturunner.Model.PointInfo;
import dk.dtu.itdiplom.dturunner.R;
import dk.dtu.itdiplom.dturunner.SingletonDtuRunner;
import dk.dtu.itdiplom.dturunner.Utils.LocationUtils;

/**
 * Dette fragment indeholder selve løbsaktiviteten
 */
public class FragmentLoeb extends Fragment implements
        View.OnClickListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener
{
    protected static final String TAG = "JJ-location-updates";

    /** * Det ønskede interval for location opdateringer. Upræcis, opdateringer vil forekomme mere eller mindre præcist.  */
    //public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 6000;    // todo jan - bør ligge i settings...
    /** * Hurtigste rate for lokationsopdateringer. Præcis. Opdateringer vil aldrig være oftere end denne værdi.          */
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = UPDATE_INTERVAL_IN_MILLISECONDS / 2;

    // SingletonDtuRunner singletonDtuRunner;
//    protected GoogleApiClient googleApiClient;
//    protected Boolean requestingLocationUpdates;
//    /** * requests til FusedLocationProviderApi.      */
//    protected LocationRequest locationRequest;

    protected String mLastUpdateTime;
    protected Location mCurrentLocation;
    private UUID loebsAktivitetUUID;
    protected double mDistanceAccumulated;

    private ArrayList<Location> locationList;
    private LoebsAktivitet loebsAktivitet;          // denne introduceres, og skal erstatte flere variabler

    // Labels.
    protected String mLatitudeLabel;
    protected String mLongitudeLabel;
    protected String mLastUpdateTimeLabel;

    // Views:
    protected TextView mLastUpdateTimeTextView;
    protected TextView mLatitudeTextView;
    protected TextView mLongitudeTextView;
    private TextView textViewLocations;
    private TextView textViewDistance;
    private TextView textViewSpeed;
    private TextView textViewSpeed2;
    private TextView textViewTimer;

    private Button buttonStartAktivitet;
    private Button mStopUpdatesButton;
    private Button buttonAfslut;
    private Button buttonShow;

    public FragmentLoeb() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(savedInstanceState == null)
        {
            Log.d(TAG, "FragmentLoeb: savedInstanceState is null");
        }

        View rod = inflater.inflate(R.layout.fragment_loeb, container, false);

        if(SingletonDtuRunner.loebsStatus.isLoebsAktivitetStartet)
        {
            // todo jan ???
        }

        buttonStartAktivitet = (Button) rod.findViewById(R.id.buttonStartAktivitet);
        buttonAfslut = (Button) rod.findViewById(R.id.buttonAfslut);
        buttonShow = (Button) rod.findViewById(R.id.buttonShow);
        buttonStartAktivitet.setOnClickListener(this);
        buttonAfslut.setOnClickListener(this);
        buttonShow.setOnClickListener(this);

        textViewLocations = (TextView) rod.findViewById(R.id.textViewLocations);
        textViewDistance = (TextView) rod.findViewById(R.id.textViewDistance);
        textViewSpeed = (TextView) rod.findViewById(R.id.textViewSpeed);
        textViewSpeed2 = (TextView) rod.findViewById(R.id.textViewSpeed2);
        textViewTimer = (TextView) rod.findViewById(R.id.textViewTimer);

        mStopUpdatesButton = (Button) rod.findViewById(R.id.buttonStop);
        mStopUpdatesButton.setOnClickListener(this);

        mLatitudeTextView = (TextView) rod.findViewById(R.id.latitude_text);
        mLongitudeTextView = (TextView) rod.findViewById(R.id.longitude_text);
        mLastUpdateTimeTextView = (TextView) rod.findViewById(R.id.last_update_time_text);

        locationList = new ArrayList<Location>();
        loebsAktivitet = new LoebsAktivitet();

        // Set labels.
        mLatitudeLabel = "latitude";
        mLongitudeLabel = "longitude";
        mLastUpdateTimeLabel = "opdateringstidspunkt";

        SingletonDtuRunner.loebsStatus.locationGoogleApi.requestingLocationUpdates = false;
        mLastUpdateTime = "";
        mDistanceAccumulated = 0;

        // Setup databaseContract:
        // databaseContract = new DatabaseContract(getActivity());

        // todo jan - tester pop-up til aktivering af gps
        boolean isGpsEnabled = LocationHelper.checkForUserEnabledGpsSettings(getContext());
//        if(!isGpsEnabled)
//        {
//            askToEnableGps();
//        }



        // todo jan - skal ligge et andet sted...
        // Kick off the process of building a GoogleApiClient and requesting the LocationServices API.
        buildGoogleApiClient();

        return rod;
    }


    //region overførte metoder
    /**
     * Builds a GoogleApiClient. Uses the {@code #addApi} method to request the
     * LocationServices API.
     *
     */
    protected synchronized void buildGoogleApiClient() {

        Log.i(TAG, "Building GoogleApiClient");
        SingletonDtuRunner.loebsStatus.locationGoogleApi.googleApiClient = new GoogleApiClient.Builder(getActivity())    // todo jan - måtte ændre fra this til getContext() eller getActivity(). brug getActivity() siger Jakob
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)               // todo jan - her vælges Location... Der er også en FusedLocationApi...
                .build();
        createLocationRequest();
    }

    /**
     * Sets up the location request. Android has two location request settings:
     * {@code ACCESS_COARSE_LOCATION} and {@code ACCESS_FINE_LOCATION}. These settings control
     * the accuracy of the current location. This sample uses ACCESS_FINE_LOCATION, as defined in
     * the AndroidManifest.xml.
     * <p/>
     * When the ACCESS_FINE_LOCATION setting is specified, combined with a fast update
     * interval (5 seconds), the Fused Location Provider API returns location updates that are
     * accurate to within a few feet.
     * <p/>
     * These settings are appropriate for mapping applications that show real-time location
     * updates.
     */
    protected void createLocationRequest() {

        // check for gps and ask user: flyttet til onCreate...
        // boolean isGpsEnabled = checkForUserEnabledGpsSettings();
//    if(!isGpsEnabled)
//    {
//        askToEnableGps();
//    }

        // dette ser ud til at det virker :-) så resten her kan slettes.
        LocationHelper.createLocationRequest();

//        SingletonDtuRunner.loebsStatus.locationGoogleApi.locationRequest = new LocationRequest();
//
//        // Sets the desired interval for active location updates. This interval is
//        // inexact. You may not receive updates at all if no location sources are available, or
//        // you may receive them slower than requested. You may also receive updates faster than
//        // requested if other applications are requesting location at a faster interval.
//        SingletonDtuRunner.loebsStatus.locationGoogleApi.locationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
//
//        // Sets the fastest rate for active location updates. This interval is exact, and your
//        // application will never receive updates faster than this value.
//        SingletonDtuRunner.loebsStatus.locationGoogleApi.locationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
//
//        SingletonDtuRunner.loebsStatus.locationGoogleApi.locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

//    private boolean checkForUserEnabledGpsSettings() {
//
//        Log.d(TAG, "Tester om GPS settings er aktiveret!");
//
//        // Get Location Manager and check for GPS & Network location services
//        LocationManager lm = (LocationManager) getContext().getSystemService(getContext().LOCATION_SERVICE);
//        logGpsSettingsStatus(lm);
//
//        // todo jan - tag stilling til om GPS skal være aktiveret...
//        if(!lm.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
//                !lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
//        {
//            Log.d(TAG, "GPS settings is not enabled!");
//            LocationHelper.askToEnableGps2(getContext());
//            return false;
//        }
//
//        Log.d(TAG, "GPS settings is already enabled!");
//
//        return true;
//    }
//
//    private void logGpsSettingsStatus(LocationManager lm) {
//        // note jan - ekstra logning for at se hvad der ikke er aktiveret
//        if(!lm.isProviderEnabled(LocationManager.GPS_PROVIDER))
//        {
//            Log.d(TAG, "GPS (GPS_PROVIDER) settings is not enabled!");
//        }
//        if(!lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
//        {
//            Log.d(TAG, "GPS (NETWORK_PROVIDER) settings is not enabled!");
//        }
//    }

//    private void askToEnableGps2() {
//        // Build the alert dialog
//        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
//        builder.setTitle("Placerings tjenester er ikke aktiveret");
//        builder.setMessage("Aktiverer venligst placeringstjenester (gps) for at kunne anvende appen!");
//        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialogInterface, int i) {
//                // Show location settings when the user acknowledges the alert dialog
//                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                startActivity(intent);
//       }
//        });
//
//        builder.setNegativeButton("Annuller", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.cancel();
//            }
//        });
//        Dialog alertDialog = builder.create();
//        alertDialog.setCanceledOnTouchOutside(false);
//        alertDialog.show();
//    }
//
//    private void askToEnableGps() {
//        Intent myIntent = new Intent( Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//        this.startActivity(myIntent);
//    }

    //endregion



    @Override
    public void onClick(View v) {
        if(v== buttonStartAktivitet)
        {
            // 1) tjek for location enabled
            startLoebsAktivitetOgLocationUpdates();
        }
        if(v==mStopUpdatesButton)
        {
            stopUpdatesButtonHandler(v);    // todo jan - refactor ect.
        }
        if(v==buttonAfslut)
        {
            Log.d(TAG, "todo jan - buttonAfslut. Sørg for at lukke løbsaktivitet...");
        }
        if(v==buttonShow)
        {
            showAllLocations();
        }
    }

    /**
     *
     * Handles the Stop Updates button, and requests removal of location updates. Does nothing if
     * updates were not previously requested.
     */
    public void stopUpdatesButtonHandler(View view) {
        if(getActivity() == null) return;   // hvis activity context er null er vi allerede ude af fragment.

        if (SingletonDtuRunner.loebsStatus.locationGoogleApi.requestingLocationUpdates) {
            SingletonDtuRunner.loebsStatus.locationGoogleApi.requestingLocationUpdates = false;
            Toast.makeText(getActivity(), "Location updates stoppet!", Toast.LENGTH_LONG);
            setButtonsEnabledState();
            stopLocationUpdates();
            SingletonDtuRunner.loebsStatus.isLoebsAktivitetStartet = false;
        }
    }

    // todo jan old name: startUpdatesButtonHandler. rename til noget mere sigende!!!
    //      public void startUpdatesButtonHandler(View view)
    public void startLoebsAktivitetOgLocationUpdates() {
        if(getActivity() == null)   // hvis activity context er null er vi allerede ude af fragment.
            return;

        if(SingletonDtuRunner.loebsStatus.isLoebsAktivitetStartet)
        {
            Log.d(TAG, "løbsaktivitet er allerede startet! (SingletonDtuRunner.loebsStatus.isLoebsAktivitetStartet) ");
            // todo jan - aktiver knapper? mm
        }


//        if(SingletonDtuRunner.isLoebsAktivitetStartet)
//        {
//            Log.d(TAG, "løbsaktivitet er allerede startet! (SingletonDtuRunner.isLoebsAktivitetStartet) ");
//            // todo jan - aktiver knapper? mm
//        }

//        SingletonDtuRunner.isLoebsAktivitetStartet = true;
        SingletonDtuRunner.loebsStatus.isLoebsAktivitetStartet = true;
        if (!SingletonDtuRunner.loebsStatus.locationGoogleApi.requestingLocationUpdates)
        {
            boolean googleApiStatus = startLocationUpdates();
            opretLoebsAktivitet();
            nulstilLoebsdata();


            if(googleApiStatus)
            {
                SingletonDtuRunner.loebsStatus.locationGoogleApi.requestingLocationUpdates = true;
                Toast.makeText(getActivity(), "Location updates startet!", Toast.LENGTH_LONG);
                setButtonsEnabledState();
            }
            else
            {
                textViewTimer.setText("Du har ikke google play installeret. Så kan du desværre ikke anvende denne app.");
            }
        }
    }

    private void nulstilLoebsdata() {
        // nulstil data
        locationList.clear();
    }

    private void opretLoebsAktivitet() {
        // opret løb:
        LoebsAktivitet loebsAktivitet = new LoebsAktivitet();
        loebsAktivitet.setNavnAlias("TestNavn");
        loebsAktivitet.setEmail("Test email");
        loebsAktivitet.setLoebsNote("Dette er et test løb! skal have input fra bruger...");
        loebsAktivitet.setPersonId("todo");
        //loebsAktivitet.setStarttidspunkt();

        // todo jan kan det gøre pænere? dvs. uden new hver gang... eller static...
        DatabaseHelper databaseHelper = new DatabaseHelper();
        loebsAktivitetUUID = databaseHelper.insertLoebsAktivitet(loebsAktivitet, getActivity());
    }

    /**
     * Requests location updates from the FusedLocationApi.
     */
    protected boolean startLocationUpdates() {

        // tjek for googleAPI:


        // The final argument to {@code requestLocationUpdates()} is a LocationListener
        // (http://developer.android.com/reference/com/google/android/gms/location/LocationListener.html).

        try
        {
            LocationServices.FusedLocationApi.requestLocationUpdates(
//                    singletonDtuRunner.googleApiClient, locationRequest, this);
                    SingletonDtuRunner.loebsStatus.locationGoogleApi.googleApiClient, SingletonDtuRunner.loebsStatus.locationGoogleApi.locationRequest, this);
            return true;
        }
        catch (Exception exception)
        {
            // java.lang.IllegalStateException: GoogleApiClient is not connected yet.
            Log.d(TAG, "Der opstod en fejl. Måske GoogleApiClient ikke er tilgængelig? " + exception + " " + exception.getStackTrace() );
        }
        return false;
    }

    protected void stopLocationUpdates() {

//        LocationServices.FusedLocationApi.removeLocationUpdates(singletonDtuRunner.googleApiClient, this);
        LocationServices.FusedLocationApi.removeLocationUpdates(SingletonDtuRunner.loebsStatus.locationGoogleApi.googleApiClient, this);
    }

    private void setButtonsEnabledState() {
        if (SingletonDtuRunner.loebsStatus.locationGoogleApi.requestingLocationUpdates) {
            buttonStartAktivitet.setEnabled(false);
            mStopUpdatesButton.setEnabled(true);
        } else {
            buttonStartAktivitet.setEnabled(true);
            mStopUpdatesButton.setEnabled(false);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        SingletonDtuRunner.loebsStatus.locationGoogleApi.googleApiClient.connect();
//        singletonDtuRunner.googleApiClient.connect();
    }


    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "onLocationChanged " + location.getLatitude() + ", " + location.getLongitude());
        // todo jan - her skal bla gemmes placeringen. og laves beregninger...

        if(getActivity() == null)   // hvis activity context er null er vi allerede ude af fragment.
            return;

        Toast.makeText(getActivity(), "Location updated", Toast.LENGTH_SHORT).show();

        mCurrentLocation = location;
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        updateUI();
        saveLocation();  // todo
        //updateUI();
    }

    private void saveLocation()
    {


        locationList.add(mCurrentLocation);
        int size = locationList.size();
        double distance = 0;
        double speedSinceLast = 0;



        if(size > 1)
        {
            //mDistanceAccumulated += LocationUtils.distFromDouble()

            distance = LocationUtils.getDistanceBetweenPoints(locationList.get(size - 2), mCurrentLocation);
            speedSinceLast = LocationUtils.getSpeedBetweenPoints(locationList.get(size - 2), mCurrentLocation);
            double speedSinceStartAverage = LocationUtils.getSpeedBetweenPoints(locationList.get(0), mCurrentLocation);

            mDistanceAccumulated +=distance;
            //double distanceInMeters = mDistanceAccumulated / 1000;
            textViewDistance.setText(( mDistanceAccumulated + " meter"));

            String speedSinceLastWithDecimals = String.format("%.5f", speedSinceLast);
            textViewSpeed.setText((String.format("%s m/s", speedSinceLastWithDecimals)));
            String speedSinceStartAverageWithDecimals = String.format("%.5f", speedSinceStartAverage);
            textViewSpeed2.setText((String.format("%s m/s avg", speedSinceStartAverageWithDecimals)));

            double timeSinceStart = LocationUtils.getTimeSinceStart(locationList.get(0), locationList.get(size - 1)) / 1000;
            textViewTimer.setText(timeSinceStart + "  sekunder");
        }

        // todo jan - working here... 9/11-15
        PointInfo pointInfo = new PointInfo(mCurrentLocation.getTime(), mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude(), speedSinceLast, distance, 1);
        loebsAktivitet.pointInfoList.add(pointInfo);

        savePointToDatabase(pointInfo);

        showAllLocations();
    }

    private void savePointToDatabase(PointInfo pointInfo) {

        DatabaseHelper databaseHelper = new DatabaseHelper();
        databaseHelper.insertPointData(pointInfo, loebsAktivitetUUID, getActivity());
    }

    private void showAllLocations() {
        textViewLocations.setText("All positions:\n");
        textViewLocations.setTextColor(Color.BLUE);

        //PrintLocation(locationArrayList);
        final int size = locationList.size();

        textViewLocations.append("\n");
        textViewLocations.append("size: " + size);
        textViewLocations.append("\n");

        for (int i = 0; i < size; i++)
        {

            Date date = new Date(locationList.get(i).getTime());
            DateFormat formatter = new SimpleDateFormat("HH:mm:ss:SSS");
            String dateFormatted = formatter.format(date);

            // textViewAppend.append("" + locationArrayList.get(i) + "\n");
            //textViewLocations.append("" + locationList.get(i).getSpeed() + ", " + dateFormatted);
            textViewLocations.append("Acc: " + locationList.get(i).getAccuracy() + ", Alt:" + locationList.get(i).getAltitude() + ", " + dateFormatted);
            // textViewLocations.append(" ::: " + TryGetLocationAddress(locationList.get(i)));
            textViewLocations.append(" -->" + locationList.get(i).getLatitude() + ", " + locationList.get(i).getLongitude() + "\n");
            // textViewLocations.append("\n");

            double distance = LocationUtils.getDistanceBetweenPoints(locationList.get(0).getLatitude(), locationList.get(0).getLongitude(), locationList.get(i).getLatitude(), locationList.get(i).getLongitude());
            //double distance = LocationUtils.distFromDouble(locationList.get(0).getLatitude(), locationList.get(0).getLongitude(), locationList.get(i).getLatitude(), locationList.get(i).getLongitude());
            long secondsPassed = (locationList.get(i).getTime() - locationList.get(0).getTime()) / 1000;
            //LocationUtils.distFrom(locationList.get(i).getLatitude(), locationList.get(i).getLongitude(), locationList.get(i).getLatitude(), locationList.get(i).getLongitude() );
            textViewLocations.append("Distance: " + distance + ", Total seconds: " + secondsPassed);

            double speed = LocationUtils.getSpeed(distance, secondsPassed);
            String speedWithDecimals = String.format("Value of a: %.9f", speed);
            if(i > 5)
            {

                textViewLocations.append(", Speed (5): " + LocationUtils.getSpeedBetweenPoints(locationList.get(i-5), locationList.get(i)));
                textViewLocations.append(", Speed2: " + speedWithDecimals + " m/s. ");
                textViewLocations.append("\n");
            }
            else if(i > 1)
            {
                textViewLocations.append("Distance: " + distance);
                textViewLocations.append(", Speed: " + LocationUtils.getSpeedBetweenPoints(locationList.get(i-1), locationList.get(i)));
                textViewLocations.append(", Speed2: " + speedWithDecimals + " m/s. ");
                // textViewLocations.append(", Speed2: " + speed);
                textViewLocations.append("\n");
            }

            textViewLocations.append(" - - - - - - - - - - - ");
            textViewLocations.append("\n");

        }
    }

    //region Connection Callbacks
    @Override
    public void onConnected(Bundle bundle) {
        Log.i(TAG, " * * * Connected to GoogleApiClient * * * ");

        // If the initial location was never previously requested, we use
        // FusedLocationApi.getLastLocation() to get it. If it was previously requested, we store
        // its value in the Bundle and check for it in onCreate(). We
        // do not request it again unless the user specifically requests location updates by pressing
        // the Start Updates button.
        //
        // Because we cache the value of the initial location in the Bundle, it means that if the
        // user launches the activity,
        // moves to a new location, and then changes the device orientation, the original location
        // is displayed as the activity is re-created.
        if (mCurrentLocation == null) {
            mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(SingletonDtuRunner.loebsStatus.locationGoogleApi.googleApiClient);
//            mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(singletonDtuRunner.googleApiClient);
            mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
            updateUI();
        }

        // If the user presses the Start Updates button before GoogleApiClient connects, we set
        // requestingLocationUpdates to true (see startUpdatesButtonHandler()). Here, we check
        // the value of requestingLocationUpdates and if it is true, we start location updates.
        if (SingletonDtuRunner.loebsStatus.locationGoogleApi.requestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    // todo jan - denne er ikke nødvendig. overvej formål!
    private void updateUI() {

        if(mCurrentLocation == null)
        {
            Log.d(TAG, "mCurrentLocation er null. Der er nok ikke adgang til placeringsdata...");
            return;
        }

        Log.d(TAG, String.format("%s: %f", mLatitudeLabel,
                mCurrentLocation.getLatitude()));

        mLatitudeTextView.setText(String.format("%s: %f", mLatitudeLabel, mCurrentLocation.getLatitude()));
        mLongitudeTextView.setText(String.format("%s: %f", mLongitudeLabel, mCurrentLocation.getLongitude()));
        mLastUpdateTimeTextView.setText(String.format("%s: %s", mLastUpdateTimeLabel, mLastUpdateTime));
    }

    @Override
    public void onConnectionSuspended(int cause) {
        // The connection to Google Play services was lost for some reason. We call connect() to
        // attempt to re-establish the connection.
        Log.i(TAG, "Connection suspended " + cause);
        SingletonDtuRunner.loebsStatus.locationGoogleApi.googleApiClient.connect();
//        singletonDtuRunner.googleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // Refer to the javadoc for ConnectionResult to see what error codes might be returned in
        // onConnectionFailed.
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + connectionResult.getErrorCode());
    }
    //endregion Connection Callbacks
}
