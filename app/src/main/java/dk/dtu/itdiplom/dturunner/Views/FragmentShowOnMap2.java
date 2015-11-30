package dk.dtu.itdiplom.dturunner.Views;


import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import dk.dtu.itdiplom.dturunner.R;

/*
    Eksempel på
 */
public class FragmentShowOnMap2 extends Fragment
        implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener
{

    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    LatLng latLng;
    GoogleMap mGoogleMap;
    SupportMapFragment mFragment;


    private SupportMapFragment mMapFragment;
    private GoogleMap mMapGoogleMap;

    public FragmentShowOnMap2() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rod = inflater.inflate(R.layout.fragment_show_on_map2, container, false);

        Log.d("jjTest", "onCreateView in FragmentShowOnMap2");

        // pga. man ikke kan skabe et SupportFragment i onCreateView, løses det på denne måde:
        mMapFragment = new SupportMapFragment() {
            @Override
            public void onActivityCreated(Bundle savedInstanceState) {
                super.onActivityCreated(savedInstanceState);
                mMapGoogleMap = mMapFragment.getMap();
                if (mMapGoogleMap != null) {
                    setupMap();
                }
            }
        };
        getChildFragmentManager().beginTransaction().add(R.id.map2, mMapFragment).commit();


//        mFragment = (SupportMapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.map2);
//        if(mFragment == null)
//        {
//            Log.d("jjTest", "fragment er null.");
//        }
//        mGoogleMap = mFragment.getMapAsync();
//
//        MarkerOptions markerOptions = new MarkerOptions();
//        markerOptions.position(latLng);
//        markerOptions.title("Current Position");
//
//        mGoogleMap.addMarker(markerOptions);


        return rod;

    }

    /*
        For at illustrere at der kan plottes flere punkter.
        Dvs. så kan jeg også plotte en rute ind.
     */
    private void setupMap() {
        //mGoogleMap = mFragment.getMap();
        LatLng dtuCampus = new LatLng(55.731345, 12.395785);
        LatLng lautrupVang4 = new LatLng(55.731405, 12.392751);
        LatLng lautrupVang1 = new LatLng(55.732011, 12.389959);

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(dtuCampus);
        markerOptions.title("Current Position");
        mMapGoogleMap.addMarker(markerOptions);

        MarkerOptions markerOptions2 = new MarkerOptions();
        markerOptions2.position(lautrupVang1);
        markerOptions2.title("Position lautrupVang1");
        // markerOptions2.title("Current Position");
        mMapGoogleMap.addMarker(markerOptions2);

        Marker kiel = mMapGoogleMap.addMarker(new MarkerOptions()
                .position(lautrupVang4)
                .title("lautrupVang4")
                .snippet("LautrupVang 4")
                .flat(true)
//                .icon(BitmapDescriptorFactory                       .fromResource(R.drawable.mr_ic_play_dark)
//                )
        );

        // Zoom - 1 ud, 15 meget tæt på.
        mMapGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(dtuCampus, 15));

    }


    @Override
    public void onConnected(Bundle bundle) {
        Log.d("jjTest", "onConnected");

    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d("jjTest", "onConnectionSuspended");

    }

    @Override
    public void onLocationChanged(Location location) {

        Log.d("jjTest", "onLocationChanged");

//place marker at current position
        mGoogleMap.clear();
        latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        Marker m = mGoogleMap.addMarker(markerOptions);

        Toast.makeText(getActivity(), "Location Changed", Toast.LENGTH_SHORT).show();

        //If you only need one location, unregister the listener
        //LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d("jjTest", "onConnectionFailed");

    }
}
