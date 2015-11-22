package dk.dtu.itdiplom.dturunner.Views;

import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
    final String fragmentLoebTag = "FragmentLoeb";


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
    //private Button buttonShow;

    public FragmentLoeb() {
        // Required empty public constructor
    }

//    @Override
//    public void onBackPressed() {
//
//        super.getActivity().getSupportFragmentManager();
//
//        Log.d(TAG, "- onBackPressed.... " + getActivity().getSupportFragmentManager().getBackStackEntryCount());
//
//        //getSupportFragmentManager().findFragmentById(R.id.)
//
//        if (getActivity().getSupportFragmentManager().findFragmentByTag(fragmentLoebTag) != null) {
//            Log.d(TAG, "- onBackPressed - fragment found!!!");
//            getActivity().finish();
//        }
//        else if (getActivity().getSupportFragmentManager().getBackStackEntryCount() == 0) {
//            Log.d(TAG, "- onBackPressed - getBackStackEntryCount == 0...");
//            getActivity().finish();
//        } else {
//            getActivity().getSupportFragmentManager().popBackStack();
//        }
//    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(savedInstanceState == null)
        {
            Log.d(TAG, "FragmentLoeb: savedInstanceState is null");
        }

        View rod = inflater.inflate(R.layout.fragment_loeb, container, false);


        buttonStartAktivitet = (Button) rod.findViewById(R.id.buttonStartAktivitet);
        buttonAfslut = (Button) rod.findViewById(R.id.buttonAfslut);
//        buttonShow = (Button) rod.findViewById(R.id.buttonShow);
        buttonStartAktivitet.setOnClickListener(this);
        buttonAfslut.setOnClickListener(this);
//        buttonShow.setOnClickListener(this);

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





        if(!SingletonDtuRunner.loebsStatus.isLoebsAktivitetStartet) {
            //return rod;
            // todo jan ???

            Log.d(TAG, "isLoebsAktivitetStartet: false. Så initialiser løbs værdier...");

            // Set labels.
            mLatitudeLabel = "latitude";
            mLongitudeLabel = "longitude";
            mLastUpdateTimeLabel = "opdateringstidspunkt";

            SingletonDtuRunner.loebsStatus.locationList = new ArrayList<Location>();
            SingletonDtuRunner.loebsStatus.loebsAktivitet = new LoebsAktivitet();

            SingletonDtuRunner.loebsStatus.locationGoogleApi.requestingLocationUpdates = false;
            SingletonDtuRunner.loebsStatus.mLastUpdateTime = "";
            SingletonDtuRunner.loebsStatus.mDistanceAccumulated = 0;

            // todo jan - tester pop-up til aktivering af gps
            boolean isGpsEnabled = LocationHelper.checkForUserEnabledGpsSettings(getContext());
//        if(!isGpsEnabled)
//        {
//            askToEnableGps();
//        }

            // todo jan - skal ligge et andet sted...
            // Kick off the process of building a GoogleApiClient and requesting the LocationServices API.
            LocationHelper.buildGoogleApiClient(getActivity(), this);
        }
        else {
            // reload values...
            updateUI();
            int size = SingletonDtuRunner.loebsStatus.locationList.size();
            Log.d(TAG, "- SingletonDtuRunner.loebsStatus.locationList.size() " + size);
        }

        return rod;
    }


    //region overførte metoder
    /**
     * Builds a GoogleApiClient. Uses the {@code #addApi} method to request the
     * LocationServices API.
     *
     */
//    protected synchronized void buildGoogleApiClient() {
//
//        Log.i(TAG, "Building GoogleApiClient");
//        SingletonDtuRunner.loebsStatus.locationGoogleApi.googleApiClient = new GoogleApiClient.Builder(getActivity())    // todo jan - måtte ændre fra this til getContext() eller getActivity(). brug getActivity() siger Jakob
//                .addConnectionCallbacks(this)
//                .addOnConnectionFailedListener(this)
//                .addApi(LocationServices.API)               // todo jan - her vælges Location... Der er også en FusedLocationApi...
//                .build();
//        LocationHelper.createLocationRequest();
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
//        if(v==buttonShow)
//        {
//            showAllLocations();
//        }
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
        SingletonDtuRunner.loebsStatus.locationList.clear();
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
        SingletonDtuRunner.loebsStatus.loebsAktivitetUUID = databaseHelper.insertLoebsAktivitet(loebsAktivitet, getActivity());
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
        {
            Log.d(TAG, "onLocationChanged,  getActivity() == null  !!! så kan vi ikke gøre mere her!!!");

            return;
        }

        Toast.makeText(getActivity(), "Location updated", Toast.LENGTH_SHORT).show();

        SingletonDtuRunner.loebsStatus.mCurrentLocation = location;
        SingletonDtuRunner.loebsStatus.mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        updateUI();
        saveLocation();  // todo
        //updateUI();
    }

    private void saveLocation()
    {


        SingletonDtuRunner.loebsStatus.locationList.add(SingletonDtuRunner.loebsStatus.mCurrentLocation);
        int size = SingletonDtuRunner.loebsStatus.locationList.size();
        double distance = 0;
        double speedSinceLast = 0;

        double timeSinceStart = LocationUtils.getTimeSinceStart(SingletonDtuRunner.loebsStatus.locationList.get(0), SingletonDtuRunner.loebsStatus.locationList.get(size - 1)) / 1000;
        textViewTimer.setText(timeSinceStart + "  sekunder");

        if(size > 1)
        {
            //mDistanceAccumulated += LocationUtils.distFromDouble()

            distance = LocationUtils.getDistanceBetweenPoints(SingletonDtuRunner.loebsStatus.locationList.get(size - 2), SingletonDtuRunner.loebsStatus.mCurrentLocation);
            speedSinceLast = LocationUtils.getSpeedBetweenPoints(SingletonDtuRunner.loebsStatus.locationList.get(size - 2), SingletonDtuRunner.loebsStatus.mCurrentLocation);
            double speedSinceStartAverage = LocationUtils.getSpeedBetweenPoints(SingletonDtuRunner.loebsStatus.locationList.get(0), SingletonDtuRunner.loebsStatus.mCurrentLocation);

            SingletonDtuRunner.loebsStatus.mDistanceAccumulated +=distance;
            //double distanceInMeters = mDistanceAccumulated / 1000;
            textViewDistance.setText(( SingletonDtuRunner.loebsStatus.mDistanceAccumulated + " meter"));

            String speedSinceLastWithDecimals = String.format("%.5f", speedSinceLast);
            textViewSpeed.setText((String.format("%s m/s", speedSinceLastWithDecimals)));
            String speedSinceStartAverageWithDecimals = String.format("%.5f", speedSinceStartAverage);
            textViewSpeed2.setText((String.format("%s m/s avg", speedSinceStartAverageWithDecimals)));


        }

        // todo jan - working here... 9/11-15
        PointInfo pointInfo = new PointInfo(SingletonDtuRunner.loebsStatus.mCurrentLocation.getTime(), SingletonDtuRunner.loebsStatus.mCurrentLocation.getLatitude(), SingletonDtuRunner.loebsStatus.mCurrentLocation.getLongitude(), speedSinceLast, distance, 1);
        SingletonDtuRunner.loebsStatus.loebsAktivitet.pointInfoList.add(pointInfo);

        savePointToDatabase(pointInfo);

        showAllLocations();
    }

    private void savePointToDatabase(PointInfo pointInfo) {

        DatabaseHelper databaseHelper = new DatabaseHelper();
        databaseHelper.insertPointData(pointInfo, SingletonDtuRunner.loebsStatus.loebsAktivitetUUID, getActivity());
    }

    private void showAllLocations() {
        textViewLocations.setText("All positions:\n");
        textViewLocations.setTextColor(Color.BLUE);

        //PrintLocation(locationArrayList);
        final int size = SingletonDtuRunner.loebsStatus.locationList.size();

        textViewLocations.append("\n");
        textViewLocations.append("size: " + size);
        textViewLocations.append("\n");

        for (int i = 0; i < size; i++)
        {

            Date date = new Date(SingletonDtuRunner.loebsStatus.locationList.get(i).getTime());
            DateFormat formatter = new SimpleDateFormat("HH:mm:ss:SSS");
            String dateFormatted = formatter.format(date);

            // textViewAppend.append("" + locationArrayList.get(i) + "\n");
            //textViewLocations.append("" + locationList.get(i).getSpeed() + ", " + dateFormatted);
            textViewLocations.append("Acc: " + SingletonDtuRunner.loebsStatus.locationList.get(i).getAccuracy() + ", Alt:" + SingletonDtuRunner.loebsStatus.locationList.get(i).getAltitude() + ", " + dateFormatted);
            // textViewLocations.append(" ::: " + TryGetLocationAddress(locationList.get(i)));
            textViewLocations.append(" -->" + SingletonDtuRunner.loebsStatus.locationList.get(i).getLatitude() + ", " + SingletonDtuRunner.loebsStatus.locationList.get(i).getLongitude() + "\n");
            // textViewLocations.append("\n");

            double distance = LocationUtils.getDistanceBetweenPoints(SingletonDtuRunner.loebsStatus.locationList.get(0).getLatitude(), SingletonDtuRunner.loebsStatus.locationList.get(0).getLongitude(), SingletonDtuRunner.loebsStatus.locationList.get(i).getLatitude(), SingletonDtuRunner.loebsStatus.locationList.get(i).getLongitude());
            //double distance = LocationUtils.distFromDouble(locationList.get(0).getLatitude(), locationList.get(0).getLongitude(), locationList.get(i).getLatitude(), locationList.get(i).getLongitude());
            long secondsPassed = (SingletonDtuRunner.loebsStatus.locationList.get(i).getTime() - SingletonDtuRunner.loebsStatus.locationList.get(0).getTime()) / 1000;
            //LocationUtils.distFrom(locationList.get(i).getLatitude(), locationList.get(i).getLongitude(), locationList.get(i).getLatitude(), locationList.get(i).getLongitude() );
            textViewLocations.append("Distance: " + distance + ", Total seconds: " + secondsPassed);

            double speed = LocationUtils.getSpeed(distance, secondsPassed);
            String speedWithDecimals = String.format("Value of a: %.9f", speed);
            if(i > 5)
            {

                textViewLocations.append(", Speed (5): " + LocationUtils.getSpeedBetweenPoints(SingletonDtuRunner.loebsStatus.locationList.get(i-5), SingletonDtuRunner.loebsStatus.locationList.get(i)));
                textViewLocations.append(", Speed2: " + speedWithDecimals + " m/s. ");
                textViewLocations.append("\n");
            }
            else if(i > 1)
            {
                textViewLocations.append("Distance: " + distance);
                textViewLocations.append(", Speed: " + LocationUtils.getSpeedBetweenPoints(SingletonDtuRunner.loebsStatus.locationList.get(i-1), SingletonDtuRunner.loebsStatus.locationList.get(i)));
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
        if (SingletonDtuRunner.loebsStatus.mCurrentLocation == null) {
            SingletonDtuRunner.loebsStatus.mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(SingletonDtuRunner.loebsStatus.locationGoogleApi.googleApiClient);
//            mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(singletonDtuRunner.googleApiClient);
            SingletonDtuRunner.loebsStatus.mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
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

        if(SingletonDtuRunner.loebsStatus.mCurrentLocation == null)
        {
            Log.d(TAG, "mCurrentLocation er null. Der er nok ikke adgang til placeringsdata...");
            return;
        }

        Log.d(TAG, String.format("%s: %f", mLatitudeLabel,
                SingletonDtuRunner.loebsStatus.mCurrentLocation.getLatitude()));

        mLatitudeTextView.setText(String.format("%s: %f", mLatitudeLabel, SingletonDtuRunner.loebsStatus.mCurrentLocation.getLatitude()));
        mLongitudeTextView.setText(String.format("%s: %f", mLongitudeLabel, SingletonDtuRunner.loebsStatus.mCurrentLocation.getLongitude()));
        mLastUpdateTimeTextView.setText(String.format("%s: %s", mLastUpdateTimeLabel, SingletonDtuRunner.loebsStatus.mLastUpdateTime));
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