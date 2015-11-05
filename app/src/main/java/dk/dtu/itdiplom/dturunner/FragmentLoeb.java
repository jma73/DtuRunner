package dk.dtu.itdiplom.dturunner;


import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
//import android.app.Fragment;
import android.support.v4.app.Fragment;
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

import dk.dtu.itdiplom.dturunner.Utils.LocationUtils;

/**
 * Dette fragment skal indeholde selve løbsaktiviteten
 */
public class FragmentLoeb extends Fragment implements
        View.OnClickListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener

{

    protected static final String TAG = "JJ-location-updates";

    /**
     * The desired interval for location updates. Inexact. Updates may be more or less frequent.
     */
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;

    /**
     * The fastest rate for active location updates. Exact. Updates will never be more frequent
     * than this value.
     */
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;

    /**
     * Provides the entry point to Google Play services.
     */
    protected GoogleApiClient mGoogleApiClient;


    /**
     * Stores parameters for requests to the FusedLocationProviderApi.
     */
    protected LocationRequest mLocationRequest;

    /**
     * Time when the location was updated represented as a String.
     */
    protected String mLastUpdateTime;

    /**
     * Represents a geographical location.
     */
    protected Location mCurrentLocation;


    protected TextView mLastUpdateTimeTextView;
    protected TextView mLatitudeTextView;
    protected TextView mLongitudeTextView;

    // Labels.
    protected String mLatitudeLabel;
    protected String mLongitudeLabel;
    protected String mLastUpdateTimeLabel;

    protected Boolean mRequestingLocationUpdates;



    private Button buttonStartAktivitet;
    private Button mStopUpdatesButton;
    private ArrayList<Location> locationList;
    private TextView textViewLocations;
    private Button buttonShow;

    public FragmentLoeb() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rod = inflater.inflate(R.layout.fragment_loeb, container, false);


        buttonStartAktivitet = (Button) rod.findViewById(R.id.buttonStartAktivitet);
        textViewLocations = (TextView) rod.findViewById(R.id.textViewLocations);
        buttonShow = (Button) rod.findViewById(R.id.buttonShow);
        buttonStartAktivitet.setOnClickListener(this);
        mStopUpdatesButton = (Button) rod.findViewById(R.id.buttonStop);
        mStopUpdatesButton.setOnClickListener(this);
        buttonShow.setOnClickListener(this);

        mLatitudeTextView = (TextView) rod.findViewById(R.id.latitude_text);
        mLongitudeTextView = (TextView) rod.findViewById(R.id.longitude_text);
        mLastUpdateTimeTextView = (TextView) rod.findViewById(R.id.last_update_time_text);

        locationList = new ArrayList<Location>();

        // Set labels.
        mLatitudeLabel = ("latitude_label");
        mLongitudeLabel = ("longitude_label");
        mLastUpdateTimeLabel = "last_update_time_label";

        mRequestingLocationUpdates = false;
        mLastUpdateTime = "";


        // Kick off the process of building a GoogleApiClient and requesting the LocationServices
        // API.
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
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())    // todo jan - måtte ændre fra this til getContext() eller getActivity(). brug getActivity() siger Jakob
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)               // todo jan - her vælges Location...
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
        mLocationRequest = new LocationRequest();

        // Sets the desired interval for active location updates. This interval is
        // inexact. You may not receive updates at all if no location sources are available, or
        // you may receive them slower than requested. You may also receive updates faster than
        // requested if other applications are requesting location at a faster interval.
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);

        // Sets the fastest rate for active location updates. This interval is exact, and your
        // application will never receive updates faster than this value.
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);

        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

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
        if(v==buttonShow)
        {
            showAllLocations();
        }
    }

    /**
     * Handles the Stop Updates button, and requests removal of location updates. Does nothing if
     * updates were not previously requested.
     */
    public void stopUpdatesButtonHandler(View view) {
        if (mRequestingLocationUpdates) {
            mRequestingLocationUpdates = false;
            Toast.makeText(getActivity(), "Location updates stoppet!", Toast.LENGTH_LONG);
            setButtonsEnabledState();
            stopLocationUpdates();
        }
    }

    // todo jan old name: startUpdatesButtonHandler. rename til noget mere sigende!!!
    //      public void startUpdatesButtonHandler(View view)
    public void startLoebsAktivitetOgLocationUpdates() {
        if (!mRequestingLocationUpdates)
        {
            mRequestingLocationUpdates = true;
            Toast.makeText(getActivity(), "Location updates startet!", Toast.LENGTH_LONG);
            setButtonsEnabledState();
            startLocationUpdates();
        }
    }

    /**
     * Requests location updates from the FusedLocationApi.
     */
    protected void startLocationUpdates() {

        // tjek for googleAPI:


        // The final argument to {@code requestLocationUpdates()} is a LocationListener
        // (http://developer.android.com/reference/com/google/android/gms/location/LocationListener.html).
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
    }

    /**
     * Removes location updates from the FusedLocationApi.
     */
    protected void stopLocationUpdates() {
        // It is a good practice to remove location requests when the activity is in a paused or
        // stopped state. Doing so helps battery performance and is especially
        // recommended in applications that request frequent location updates.

        // The final argument to {@code requestLocationUpdates()} is a LocationListener
        // (http://developer.android.com/reference/com/google/android/gms/location/LocationListener.html).
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

    /**
     * Ensures that only one button is enabled at any time. The Start Updates button is enabled
     * if the user is not requesting location updates. The Stop Updates button is enabled if the
     * user is requesting location updates.
     */
    private void setButtonsEnabledState() {
        if (mRequestingLocationUpdates) {
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
        mGoogleApiClient.connect();
    }


    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "onLocationChanged " + location.getLatitude());
        // todo jan - her skal bla gemmes placeringen. og laves beregninger...

        mCurrentLocation = location;
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        updateUI();
        saveLocation();  // todo
        Toast.makeText(getActivity(), "Location updated", Toast.LENGTH_SHORT).show();
        updateUI();
    }

    private void saveLocation()
    {
        locationList.add(mCurrentLocation);
        showAllLocations();
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

            double distance = LocationUtils.distFromDouble(locationList.get(0).getLatitude(), locationList.get(0).getLongitude(), locationList.get(i).getLatitude(), locationList.get(i).getLongitude());
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
            mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
            updateUI();
        }

        // If the user presses the Start Updates button before GoogleApiClient connects, we set
        // mRequestingLocationUpdates to true (see startUpdatesButtonHandler()). Here, we check
        // the value of mRequestingLocationUpdates and if it is true, we start location updates.
        if (mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    /**
     * Updates the latitude, the longitude, and the last location time in the UI.
     */
    private void updateUI() {

        Log.d(TAG, String.format("%s: %f", mLatitudeLabel,
                mCurrentLocation.getLatitude()));


        mLatitudeTextView.setText(String.format("%s: %f", mLatitudeLabel,
                mCurrentLocation.getLatitude()));
        mLongitudeTextView.setText(String.format("%s: %f", mLongitudeLabel,
                mCurrentLocation.getLongitude()));
        mLastUpdateTimeTextView.setText(String.format("%s: %s", mLastUpdateTimeLabel,
                mLastUpdateTime));
    }

    @Override
    public void onConnectionSuspended(int cause) {
        // The connection to Google Play services was lost for some reason. We call connect() to
        // attempt to re-establish the connection.
        Log.i(TAG, "Connection suspended " + cause);
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // Refer to the javadoc for ConnectionResult to see what error codes might be returned in
        // onConnectionFailed.
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + connectionResult.getErrorCode());
    }
    //endregion Connection Callbacks
}
