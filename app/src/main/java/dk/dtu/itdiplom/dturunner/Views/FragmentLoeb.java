package dk.dtu.itdiplom.dturunner.Views;

import android.content.Intent;
import android.content.SharedPreferences;
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
import java.util.Timer;
import java.util.TimerTask;

import dk.dtu.itdiplom.dturunner.Database.DatabaseHelper;
import dk.dtu.itdiplom.dturunner.LoebsAktivitetService;
import dk.dtu.itdiplom.dturunner.Model.Entities.GlobaleKonstanter;
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
    final String TAG = getClass().getName();

    // Labels.
    private String latitudeLabel;  // kan udgå
    private String longitudeLabel;  // kan udgå
    private String lastUpdateTimeLabel;  // kan udgå

    // Views:
    private TextView lastUpdateTimeTextView;  // kan udgå
    private TextView latitudeTextView;  // kan udgå
    private TextView longitudeTextView;  // kan udgå

    private TextView textViewLocations;
    private TextView textViewDistance;
    private TextView textViewSpeed;
    private TextView textViewSpeed2;
    private TextView textViewTimer;

    private Button buttonStartLoebsAktivitet;
    private Button buttonStopLoebsAktivitet;
    private Button buttonAfslut;

    public FragmentLoeb() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(savedInstanceState == null)
        {
            Log.d(TAG, "FragmentLoeb: savedInstanceState is null");
            setRetainInstance(true);
        }
        else
        {
            setButtonsEnabledState();
        }
        if(SingletonDtuRunner.loebsStatus.isLoebsAktivitetStartet)
        {
            Log.d(TAG, "FragmentLoeb: Der er allerede et løb igang - indlæs data...???");
        }





        View rod = inflater.inflate(R.layout.fragment_loeb, container, false);

        buttonStartLoebsAktivitet = (Button) rod.findViewById(R.id.buttonStartAktivitet);
        buttonAfslut = (Button) rod.findViewById(R.id.buttonAfslut);
        buttonStartLoebsAktivitet.setOnClickListener(this);
        buttonAfslut.setOnClickListener(this);
        buttonStopLoebsAktivitet = (Button) rod.findViewById(R.id.buttonStop);
        buttonStopLoebsAktivitet.setOnClickListener(this);

        textViewLocations = (TextView) rod.findViewById(R.id.textViewLocations);
        textViewDistance = (TextView) rod.findViewById(R.id.textViewDistance);
        textViewSpeed = (TextView) rod.findViewById(R.id.textViewSpeed);
        textViewSpeed2 = (TextView) rod.findViewById(R.id.textViewSpeed2);
        textViewTimer = (TextView) rod.findViewById(R.id.textViewTimer);

        latitudeTextView = (TextView) rod.findViewById(R.id.latitude_text);
        longitudeTextView = (TextView) rod.findViewById(R.id.longitude_text);
        lastUpdateTimeTextView = (TextView) rod.findViewById(R.id.last_update_time_text);

        // vis kun dette View hvis udvikling er slået til:
        if(SingletonDtuRunner.erUnderUdviklingFlag)
        {
            View linearLayoutLoebShowPositions = rod.findViewById(R.id.linearLayoutLoebShowPositions);
            linearLayoutLoebShowPositions.setVisibility(View.VISIBLE);
            rod.findViewById(R.id.linearlayoutCurrentLatLon).setVisibility(View.VISIBLE);
        }

        // todo jan - statig under test:
        if(!SingletonDtuRunner.loebsStatus.isLoebsAktivitetStartet) {

            Log.d(TAG, "isLoebsAktivitetStartet: false. Så initialiser løbs værdier...");

            // Set labels.
            latitudeLabel = "latitude";
            longitudeLabel = "longitude";
            lastUpdateTimeLabel = "opdateringstidspunkt";

            SingletonDtuRunner.loebsStatus.locationList = new ArrayList<Location>();
            SingletonDtuRunner.loebsStatus.loebsAktivitet = new LoebsAktivitet();

            SingletonDtuRunner.loebsStatus.locationGoogleApi.requestingLocationUpdates = false;
            SingletonDtuRunner.loebsStatus.mLastUpdateTime = "";
            // SingletonDtuRunner.loebsStatus.mDistanceAccumulated = 0;

            // todo jan - tester pop-up til aktivering af gps
            boolean isGpsEnabled = LocationHelper.checkForUserEnabledGpsSettings(getContext());

            // todo jan - skal ligge et andet sted...
            // Kick off the process of building a GoogleApiClient and requesting the LocationServices API.
            LocationHelper.buildGoogleApiClient(getActivity(), this);
        }
        else {
            // reload values...
            setButtonsEnabledState();

            updateUIMedLatLonKoordinater();
            int size = SingletonDtuRunner.loebsStatus.locationList.size();
            Log.d(TAG, "- SingletonDtuRunner.loebsStatus.locationList.size() " + size);
        }

        return rod;
    }

    @Override
    public void onClick(View v) {
        if(v== buttonStartLoebsAktivitet)
        {
            // 1) tjek for location enabled
            startLoebsAktivitetOgLocationUpdates();
        }
        if(v== buttonStopLoebsAktivitet)
        {
            stopUpdatesButtonHandler(v);    // todo jan - refactor ect.
        }
        if(v==buttonAfslut)
        {
            // todo jan udskfit stopUpdatesButtonHandler. den er der blot for at kunne stoppe et løb, hvis stop knappen ikke er aktiv.
            stopUpdatesButtonHandler(v);
            Log.d(TAG, "todo jan - buttonAfslut. Sørg for at lukke løbsaktivitet...");
        }
//        if(v==buttonShow)
//        {
//            showAllLocations();
//        }
    }

    private void stopUpdatesButtonHandler(View view) {
        if(getActivity() == null) return;   // hvis activity context er null er vi allerede ude af fragment.

        SingletonDtuRunner.loebsStatus.isLoebsAktivitetStartet = false;

        if (SingletonDtuRunner.loebsStatus.locationGoogleApi.requestingLocationUpdates) {
            SingletonDtuRunner.loebsStatus.locationGoogleApi.requestingLocationUpdates = false;
            Toast.makeText(getActivity(), "Location updates stoppet!", Toast.LENGTH_LONG);
            setButtonsEnabledState();
            stopLocationUpdates();
            stopService();
        }
    }

    private void startLoebsAktivitetOgLocationUpdates() {
        if(getActivity() == null)   // hvis activity context er null er vi allerede ude af fragment.
            return;

        if(SingletonDtuRunner.loebsStatus.isLoebsAktivitetStartet)
        {
            Log.d(TAG, "løbsaktivitet er allerede startet! (SingletonDtuRunner.loebsStatus.isLoebsAktivitetStartet) ");
            // todo jan - aktiver knapper? mm
        }
        else
        {
            nulstilLoebsdata();
        }

        SingletonDtuRunner.loebsStatus.isLoebsAktivitetStartet = true;
        Log.d(TAG, "SingletonDtuRunner.loebsStatus.isLoebsAktivitetStartet er nu sat til true!");

        if (!SingletonDtuRunner.loebsStatus.locationGoogleApi.requestingLocationUpdates)
        {
            boolean googleApiStatus = startLocationUpdates();

            if(googleApiStatus)
            {
                opretLoebsAktivitet();
                nulstilLoebsdata();

                SingletonDtuRunner.loebsStatus.isLoebsAktivitetStartet = true;
                SingletonDtuRunner.loebsStatus.locationGoogleApi.requestingLocationUpdates = true;
                Toast.makeText(getActivity(), "Location updates startet!", Toast.LENGTH_LONG);
                setButtonsEnabledState();

                // todo jan 1/12-15: ikke færdig med dette:
                Timer timer = new Timer();
                TimerTask timertask = new TimerTask() {
                    @Override
                    public void run() {
                        // todo jan: lav metode. til at opdatere tiden... og evt. andre views.
                        //      og task skal først startes når start knappen trykkes.

                        opdaterTimer();
                        Log.d(TAG, "timertask");
                    }
                };
                int period = 1000;
                //timer.schedule(timertask, 1000, period);
                timertask.run();  //    ikke nødvendigt??

                startService();
            }
            else
            {
                SingletonDtuRunner.loebsStatus.isLoebsAktivitetStartet = false;
                setButtonsEnabledState();
                textViewTimer.setText("Du har ikke google play installeret. Så kan du desværre ikke anvende denne app.");
            }
        }
    }

    private void stopService() {
        getActivity().stopService(new Intent(getActivity(), LoebsAktivitetService.class));
    }

    private void startService() {
        getActivity().startService(new Intent(getActivity(), LoebsAktivitetService.class));
    }

    private void nulstilLoebsdata() {
        // nulstil data
        SingletonDtuRunner.loebsStatus.locationList.clear();
        SingletonDtuRunner.loebsStatus.loebsAktivitet.nulstil();
    }

    private void opretLoebsAktivitet() {

        LoebsAktivitet loebsAktivitet = new LoebsAktivitet();

        SharedPreferences pref = getActivity().getPreferences(0);

        String navn = pref.getString(GlobaleKonstanter.PREF_PERSONNAVN, "");
        String email = pref.getString(GlobaleKonstanter.PREF_EMAIL, "");
        String studienummer = pref.getString(GlobaleKonstanter.PREF_STUDIENUMMER, "");

        loebsAktivitet.pointInfoList.clear();
        loebsAktivitet.setNavnAlias(navn + " " + studienummer);
        loebsAktivitet.setEmail(email);
        loebsAktivitet.setLoebsNote("Dette er et test løb! skal have input fra bruger...");
        loebsAktivitet.setPersonId(studienummer);
        //loebsAktivitet.setStarttidspunkt();

        DatabaseHelper databaseHelper = new DatabaseHelper();
        SingletonDtuRunner.loebsStatus.loebsAktivitetUUID = databaseHelper.insertLoebsAktivitet(loebsAktivitet, getActivity());
    }

    /**
     * Requests location updates from the FusedLocationApi.
     */
    protected boolean startLocationUpdates() {

        // tjek for googleAPI:

        try
        {
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    SingletonDtuRunner.loebsStatus.locationGoogleApi.googleApiClient,
                    SingletonDtuRunner.loebsStatus.locationGoogleApi.locationRequest,
                    this);
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

        LocationServices.FusedLocationApi.removeLocationUpdates(
                SingletonDtuRunner.loebsStatus.locationGoogleApi.googleApiClient,
                this);
    }

    private void setButtonsEnabledState() {
        if (SingletonDtuRunner.loebsStatus.isLoebsAktivitetStartet) {
            buttonStartLoebsAktivitet.setEnabled(false);
            buttonStopLoebsAktivitet.setEnabled(true);
        } else {
            buttonStartLoebsAktivitet.setEnabled(true);
            buttonStopLoebsAktivitet.setEnabled(false);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        SingletonDtuRunner.loebsStatus.locationGoogleApi.googleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "onLocationChanged " + location.getLatitude() + ", " + location.getLongitude());

        if(getActivity() == null)   // hvis activity context er null er vi allerede ude af fragment.
        {
            Log.d(TAG, "onLocationChanged,  getActivity() == null  !!! så kan vi ikke gøre mere her!!!");

            return;
        }

        Toast.makeText(getActivity(), "Location updated", Toast.LENGTH_SHORT).show();

        SingletonDtuRunner.loebsStatus.mCurrentLocation = location;
        SingletonDtuRunner.loebsStatus.mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        updateUIMedLatLonKoordinater();
        saveLocation();  // todo
    }

    private void saveLocation()
    {
        SingletonDtuRunner.loebsStatus.locationList.add(SingletonDtuRunner.loebsStatus.mCurrentLocation);
        int size = SingletonDtuRunner.loebsStatus.locationList.size();
        double distanceOld = 0;
        double speedSinceLast = 0;

        showLoebsParametre();

//
        if(size > 1)
        {
        // todo jan - er desværre pt. afhængig af disse værdier: De kunne nok beregnes et andet sted...
            distanceOld = LocationUtils.getDistanceBetweenPoints(SingletonDtuRunner.loebsStatus.locationList.get(size - 2), SingletonDtuRunner.loebsStatus.mCurrentLocation);
            speedSinceLast = LocationUtils.getSpeedBetweenPoints(SingletonDtuRunner.loebsStatus.locationList.get(size - 2), SingletonDtuRunner.loebsStatus.mCurrentLocation);
        }

        // todo jan - working here... 9/11-15


        PointInfo pointInfo = new PointInfo(SingletonDtuRunner.loebsStatus.mCurrentLocation.getTime(), SingletonDtuRunner.loebsStatus.mCurrentLocation.getLatitude(), SingletonDtuRunner.loebsStatus.mCurrentLocation.getLongitude(), speedSinceLast, distanceOld, 1);
        SingletonDtuRunner.loebsStatus.loebsAktivitet.pointInfoList.add(pointInfo);

        savePointToDatabase(pointInfo);

        // note jan - vises kun under udvikling
        if(SingletonDtuRunner.erUnderUdviklingFlag)
        {
            // todo jan 22/11-15: Dette kan kan nok godt udgå senere...
            showAllLocations();
        }
    }

    private void showLoebsParametre() {


        int size = SingletonDtuRunner.loebsStatus.locationList.size();

        if(size > 0)
        {
            opdaterTimer();

            double speedSinceLast = 0;

            // todo jan - dette er sådan skal være hvis det virker...
            // todo jaman - distanceOld skal beregnes på en anden måde end før...

            speedSinceLast = SingletonDtuRunner.loebsStatus.loebsAktivitet.getCurrentSpeedSinceLastPoint();
            double speedLatestPoints = SingletonDtuRunner.loebsStatus.loebsAktivitet.getAverageSpeedLatestPoints();
            double speedSinceStartAverage = SingletonDtuRunner.loebsStatus.loebsAktivitet.getAverageSpeedFromStart();

            //textViewDistance.setText(( SingletonDtuRunner.loebsStatus.mDistanceAccumulated + " meter"));
            final double totalDistance = SingletonDtuRunner.loebsStatus.loebsAktivitet.getTotalDistanceMeters();
            textViewDistance.setText(( String.format("%.1f meter", totalDistance )));

            String speedSinceLastWithDecimals = String.format("%.2f", speedSinceLast);
            textViewSpeed.setText((String.format("%s m/s", speedSinceLastWithDecimals)));
            String speedLatestPointsAverageWithDecimals = String.format("%.2f", speedLatestPoints);
            String speedSinceStartAverageWithDecimals = String.format("%.2f", speedSinceStartAverage);
            textViewSpeed2.setText((String.format("%s m/s avg (%s)", speedSinceStartAverageWithDecimals, speedLatestPointsAverageWithDecimals)));
        }
        else
        {
            textViewTimer.setText(LocationUtils.displayTimerFormat(0));
        }
    }

    private void opdaterTimer() {
        int size = SingletonDtuRunner.loebsStatus.locationList.size();
        if(size == 0)
        {
            textViewTimer.setText(LocationUtils.displayTimerFormat(0));
            return;
        }

        int previousIndex = size - 1;
        if(previousIndex < 0)
            previousIndex = 0;

        long timeSinceStartMilliSeconds = LocationUtils.getTimeMillisecondsSinceStart(SingletonDtuRunner.loebsStatus.locationList.get(0),
                SingletonDtuRunner.loebsStatus.locationList.get(previousIndex));
        textViewTimer.setText(LocationUtils.displayTimerFormat(timeSinceStartMilliSeconds));
    }

    private void savePointToDatabase(PointInfo pointInfo) {

        DatabaseHelper databaseHelper = new DatabaseHelper();
        databaseHelper.insertPointData(pointInfo, SingletonDtuRunner.loebsStatus.loebsAktivitetUUID, getActivity());
    }

    private void showCurrentValues()
    {
        // todo jan 22/11-15: skal opdatere fra værdier på løbsAktivitet...
        // SingletonDtuRunner.loebsStatus.loebsAktivitet.
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
            //textViewLocations.append("" + locationList.get(i).getSpeedMetersPerSecond() + ", " + dateFormatted);
            textViewLocations.append("Acc: " + SingletonDtuRunner.loebsStatus.locationList.get(i).getAccuracy() + ", Alt:" + SingletonDtuRunner.loebsStatus.locationList.get(i).getAltitude() + ", " + dateFormatted);
            // textViewLocations.append(" ::: " + TryGetLocationAddress(locationList.get(i)));
            textViewLocations.append(" -->" + SingletonDtuRunner.loebsStatus.locationList.get(i).getLatitude() + ", " + SingletonDtuRunner.loebsStatus.locationList.get(i).getLongitude() + "\n");
            // textViewLocations.append("\n");

            double distance = LocationUtils.getDistanceBetweenPoints(SingletonDtuRunner.loebsStatus.locationList.get(0).getLatitude(), SingletonDtuRunner.loebsStatus.locationList.get(0).getLongitude(), SingletonDtuRunner.loebsStatus.locationList.get(i).getLatitude(), SingletonDtuRunner.loebsStatus.locationList.get(i).getLongitude());
            //double distance = LocationUtils.distFromDouble(locationList.get(0).getLatitude(), locationList.get(0).getLongitude(), locationList.get(i).getLatitude(), locationList.get(i).getLongitude());
            long milliSecondsPassed = (SingletonDtuRunner.loebsStatus.locationList.get(i).getTime() - SingletonDtuRunner.loebsStatus.locationList.get(0).getTime());
            //LocationUtils.distFrom(locationList.get(i).getLatitude(), locationList.get(i).getLongitude(), locationList.get(i).getLatitude(), locationList.get(i).getLongitude() );
            textViewLocations.append("Distance: " + distance + ", Total seconds: " + milliSecondsPassed / 1000);

            double speed = LocationUtils.getSpeedMetersPerSecond(distance, milliSecondsPassed);
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

        if (SingletonDtuRunner.loebsStatus.mCurrentLocation == null) {
            SingletonDtuRunner.loebsStatus.mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(SingletonDtuRunner.loebsStatus.locationGoogleApi.googleApiClient);
            SingletonDtuRunner.loebsStatus.mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
            updateUIMedLatLonKoordinater();
        }

        if (SingletonDtuRunner.loebsStatus.locationGoogleApi.requestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    // todo jan - denne er ikke nødvendig. overvej formål!
    private void updateUIMedLatLonKoordinater() {

        if(SingletonDtuRunner.loebsStatus.mCurrentLocation == null)
        {
            Log.d(TAG, "mCurrentLocation er null. Der er nok ikke adgang til placeringsdata...");
            return;
        }

        Log.d(TAG, String.format("%s: %f", latitudeLabel,
                SingletonDtuRunner.loebsStatus.mCurrentLocation.getLatitude()));

        latitudeTextView.setText(String.format("%s: %f", latitudeLabel, SingletonDtuRunner.loebsStatus.mCurrentLocation.getLatitude()));
        longitudeTextView.setText(String.format("%s: %f", longitudeLabel, SingletonDtuRunner.loebsStatus.mCurrentLocation.getLongitude()));
        lastUpdateTimeTextView.setText(String.format("%s: %s", lastUpdateTimeLabel, SingletonDtuRunner.loebsStatus.mLastUpdateTime));
    }

    @Override
    public void onConnectionSuspended(int cause) {
        // Hvis forbindelsen til Google Play services er mistet, kalder vi connect()
        // for at forsøge at genetablere forbindelsen.
        Log.i(TAG, "Connection suspended " + cause);
        SingletonDtuRunner.loebsStatus.locationGoogleApi.googleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + connectionResult.getErrorCode());
    }
    //endregion Connection Callbacks
}