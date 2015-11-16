//package dk.dtu.itdiplom.dturunner;
//
//import android.app.FragmentManager;
//import android.app.FragmentTransaction;
//import android.content.Intent;
//import android.location.Location;
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.TextView;
//
//// Disse imports er til for at kunne anvende fused location:
//import com.google.android.gms.common.ConnectionResult;
//import com.google.android.gms.common.api.GoogleApiClient;
//import com.google.android.gms.location.LocationListener;
//import com.google.android.gms.location.LocationRequest;
//import com.google.android.gms.location.LocationServices;
//
//import java.text.DateFormat;
//import java.util.Date;
//
//public class MainActivity extends AppCompatActivity
//        implements
//        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener
//{
//
//    protected static final String TAG = "JJ-location-updates";
//
//    /**
//     * Det ønskede interval for location opdateringer. Opdateringer kan ske mere eller mindre frekvent.
//     * Upræcis.
//     */
//    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
//
//    /**
//     * Hurtigste interval for lokationsopdateringer. Der vil aldrig opdateres oftere end denne værdi.
//     * Præcis
//     */
//    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
//            UPDATE_INTERVAL_IN_MILLISECONDS / 2;
//
//    /**
//     * Adgang til Google Play services.
//     */
//    protected GoogleApiClient mGoogleApiClient;
//
//    /**
//     * Stores parameters for requests to the FusedLocationProviderApi.
//     */
//    protected LocationRequest mLocationRequest;
//
//    /**
//     * Tidspunktet for hvornår lokationen blev opdateret.
//     */
//    protected String mLastUpdateTime;
//
//    /**
//     * Dette er den modtagne position
//     */
//    protected Location mCurrentLocation;
//
//    protected Button mStartUpdatesButton;
//    protected Button mStopUpdatesButton;
//    protected TextView mLastUpdateTimeTextView;
//    protected TextView mLatitudeTextView;
//    protected TextView mLongitudeTextView;
//
//    // Labels.
//    protected String mLatitudeLabel;
//    protected String mLongitudeLabel;
//    protected String mLastUpdateTimeLabel;
//
//    /**
//     * Holder styr på om lokationsopdateringer er slået til.
//     */
//    protected Boolean mRequestingLocationUpdates;
//
//    // jans tilføjelser:
//    private Button buttonShow;      // todo jan
////    private TextView textViewLocations;
////    ArrayList<Location> locationList;// = new ArrayList<Location>();
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        // Locate the UI widgets.
//        mStartUpdatesButton = (Button) findViewById(R.id.buttonStart);
//        mStopUpdatesButton = (Button) findViewById(R.id.buttonStop);
//        buttonShow = (Button) findViewById(R.id.buttonShow);
//        mLatitudeTextView = (TextView) findViewById(R.id.latitude_text);
//        mLongitudeTextView = (TextView) findViewById(R.id.longitude_text);
//        mLastUpdateTimeTextView = (TextView) findViewById(R.id.last_update_time_text);
//
//        // Set labels.
//        mLatitudeLabel = ("latitude_label");
//        mLongitudeLabel = ("longitude_label");
//        mLastUpdateTimeLabel = "last_update_time_label";
//
//        mRequestingLocationUpdates = false;
//        mLastUpdateTime = "";
//
//
//        addFragmentForsideMenu();
//
//
//        // Update values using data stored in the Bundle.
//        // updateValuesFromBundle(savedInstanceState);
//
//        // opret googleApi og LocationServices
//        buildGoogleApiClient();
//
//
//
//    }
//
//
//    //region overførte metoder
//    /**
//     * Builds a GoogleApiClient. Uses the {@code #addApi} method to request the
//     * LocationServices API.
//     *
//     */
//    protected synchronized void buildGoogleApiClient() {
//        Log.i(TAG, "Building GoogleApiClient");
//        mGoogleApiClient = new GoogleApiClient.Builder(this)
//                .addConnectionCallbacks(this)
//                .addOnConnectionFailedListener(this)
//                .addApi(LocationServices.API)               // todo jan - her vælges Location...
//                .build();
//        createLocationRequest();
//    }
//
//    /**
//     * Sets up the location request. Android has two location request settings:
//     * {@code ACCESS_COARSE_LOCATION} and {@code ACCESS_FINE_LOCATION}. These settings control
//     * the accuracy of the current location. This sample uses ACCESS_FINE_LOCATION, as defined in
//     * the AndroidManifest.xml.
//     * <p/>
//     * When the ACCESS_FINE_LOCATION setting is specified, combined with a fast update
//     * interval (5 seconds), the Fused Location Provider API returns location updates that are
//     * accurate to within a few feet.
//     * <p/>
//     * These settings are appropriate for mapping applications that show real-time location
//     * updates.
//     */
//    protected void createLocationRequest() {
//        mLocationRequest = new LocationRequest();
//
//        // Sets the desired interval for active location updates. This interval is
//        // inexact. You may not receive updates at all if no location sources are available, or
//        // you may receive them slower than requested. You may also receive updates faster than
//        // requested if other applications are requesting location at a faster interval.
//        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
//
//        // Sets the fastest rate for active location updates. This interval is exact, and your
//        // application will never receive updates faster than this value.
//        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
//
//        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//    }
//
//    //endregion
//
//
//
//    //region Implemented methods from google api.
//    @Override
//    public void onLocationChanged(Location location) {
//
//        // todo jan: ikke helt klar over hvornår den kaldes...
//        mLatitudeTextView.setText("Hellllllooooo");
//
//
//    }
//
//    @Override
//    public void onConnected(Bundle bundle) {
//        Log.i(TAG, "Connected to GoogleApiClient");
//
//        // If the initial location was never previously requested, we use
//        // FusedLocationApi.getLastLocation() to get it. If it was previously requested, we store
//        // its value in the Bundle and check for it in onCreate(). We
//        // do not request it again unless the user specifically requests location updates by pressing
//        // the Start Updates button.
//        //
//        // Because we cache the value of the initial location in the Bundle, it means that if the
//        // user launches the activity,
//        // moves to a new location, and then changes the device orientation, the original location
//        // is displayed as the activity is re-created.
//        if (mCurrentLocation == null) {
//            mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
//            mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
//            updateUI();
//        }
//
//        // If the user presses the Start Updates button before GoogleApiClient connects, we set
//        // mRequestingLocationUpdates to true (see startUpdatesButtonHandler()). Here, we check
//        // the value of mRequestingLocationUpdates and if it is true, we start location updates.
//        if (mRequestingLocationUpdates) {
//            startLocationUpdates();
//        }
//    }
//
//    @Override
//    public void onConnectionSuspended(int i) {
//
//    }
//
//    @Override
//    public void onConnectionFailed(ConnectionResult connectionResult) {
//
//    }
//
//    //endregion
//
//    /**
//     * Start knappen
//     */
//    public void startUpdatesButtonHandler(View view) {
//        // kun hvis opdateringer ikke allerede er slået til.
//        if (!mRequestingLocationUpdates)
//        {
//            mRequestingLocationUpdates = true;
//            setButtonsEnabledState();
//            startLocationUpdates();
//        }
//    }
//
//    /**
//     * Stop knappen
//     */
//    public void stopUpdatesButtonHandler(View view) {
//        if (mRequestingLocationUpdates) {
//            mRequestingLocationUpdates = false;
//            setButtonsEnabledState();
//            stopLocationUpdates();
//        }
//    }
//
//
//    private void updateUI() {
//        mLatitudeTextView.setText(String.format("%s: %f", mLatitudeLabel,
//                mCurrentLocation.getLatitude()));
//        mLongitudeTextView.setText(String.format("%s: %f", mLongitudeLabel,
//                mCurrentLocation.getLongitude()));
//        mLastUpdateTimeTextView.setText(String.format("%s: %s", mLastUpdateTimeLabel,
//                mLastUpdateTime));
//    }
//
//    /**
//     * Requests location updates from the FusedLocationApi.
//     */
//    protected void startLocationUpdates() {
//        // (http://developer.android.com/reference/com/google/android/gms/location/LocationListener.html).
//        LocationServices.FusedLocationApi.requestLocationUpdates(
//                mGoogleApiClient, mLocationRequest, this);
//    }
//
//    protected void stopLocationUpdates() {
//        // fjerner location requests når activity er paused eller i stop state...
//        //  dette hjælper til at spare batteri.
//        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
//    }
//
//    private void setButtonsEnabledState() {
//        if (mRequestingLocationUpdates) {
//            mStartUpdatesButton.setEnabled(false);
//            mStopUpdatesButton.setEnabled(true);
//        } else {
//            mStartUpdatesButton.setEnabled(true);
//            mStopUpdatesButton.setEnabled(false);
//        }
//    }
//
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        mGoogleApiClient.connect();
//    }
//
//    public void showLocationsButtonHandler(View view) {
//
//        ShowAllLocations();
//    }
//
//    private void ShowAllLocations() {
////        textViewLocations.setText("All positions:\n");
////        textViewLocations.setTextColor(Color.BLUE);
////
////        //PrintLocation(locationArrayList);
////        final int size = locationList.size();
////
////        textViewLocations.append("\n");
////        textViewLocations.append("size: " + size);
////        textViewLocations.append("\n");
//    }
//
//    public void buttonHandlerTestLocation(View view) {
//        // open fragment!
//
//        FragmentShowOnMap fragment = new FragmentShowOnMap();
//        FragmentShowOnMap2 frag = new FragmentShowOnMap2();
//        //Fragment fragment = new FragmentShowOnMap();
//
//        FragmentManager fragmentManager = getFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
////fragmentTransaction.add(R.id.testFragment, fragment);
//fragmentTransaction.add(R.id.testFragment, frag);
//
//        fragmentTransaction.replace(R.id.frameLayout1, new FragmentShowOnMap2());
//        fragmentTransaction.addToBackStack(null);
//        fragmentTransaction.commit();
//
//
//        //fragmentTransaction.hide()
//
//
////        if (savedInstanceState == null) {
////            Fragment fragment = new FragmentShowOnMap();
////            getSupportFragmentManager().beginTransaction()
////                    .add(R.id.fragmentindhold, fragment)  // tom container i layout
////                    .commit();
////        }
//
//    }
//
//    public void buttonHandlerLoeb(View view) {
//        Intent i = new Intent(getApplicationContext(), MapActivity.class);
//        startActivity(i);
//    }
//
//    public void buttonHandlerHistory(View view) {
//        addFragmentForsideMenu();
//
//
//    }
//
//    private void addFragmentForsideMenu() {
//        // todo jan - test med indsæt af Fragment menu!
//
//        // todo jan - udkommenteret pga. start med at indføre app.compat. support. 5/11-15.
//
////        FragmentManager fragmentManager = getFragmentManager();
////        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
////
////        FragmentForside fragmentForside = new FragmentForside();
////        fragmentTransaction.add(R.id.frameLayout1, fragmentForside);
////        fragmentTransaction.addToBackStack("ssstttjan");
////        fragmentTransaction.commit();
//    }
//
//
//    public void buttonHandlerLoeb2(View view) {
//        // todo jan - udkommenteret pga. start med at indføre app.compat. support. 5/11-15.
//
////        FragmentManager fragmentManager = getFragmentManager();
////        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
////
////        fragmentTransaction.replace(R.id.frameLayout1, new FragmentLoeb());
////        fragmentTransaction.addToBackStack(null);
////        fragmentTransaction.commit();
//    }
//}