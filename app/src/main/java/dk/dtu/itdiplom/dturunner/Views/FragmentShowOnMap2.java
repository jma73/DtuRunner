package dk.dtu.itdiplom.dturunner.Views;


import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import dk.dtu.itdiplom.dturunner.R;

/*
    Eksempel på brug af maps. som det kunne se ud når man optegner en rute på kortet.
 */
public class FragmentShowOnMap2 extends Fragment
        implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener
{
    LatLng latLng;
    private SupportMapFragment mapFragment;
    private GoogleMap mapGoogleMap;

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
        //  http://stackoverflow.com/questions/14047257/how-do-i-know-the-map-is-ready-to-get-used-when-using-the-supportmapfragment/14956903#14956903
        //          ref.: http://stackoverflow.com/questions/25051246/how-to-use-supportmapfragment-inside-a-fragment

        mapFragment = new SupportMapFragment() {
            @Override
            public void onActivityCreated(Bundle savedInstanceState) {
                super.onActivityCreated(savedInstanceState);
                mapFragment.setEnterTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                mapGoogleMap = mapFragment.getMap();
                if (mapGoogleMap != null) {
                    setupMap(); // her indlæses markers mm.
                }
            }
        };
        getChildFragmentManager().beginTransaction().add(R.id.map2, mapFragment).commit();

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
        mapGoogleMap.addMarker(markerOptions);

        MarkerOptions markerOptions2 = new MarkerOptions();
        markerOptions2.position(lautrupVang1);
        markerOptions2.title("Position lautrupVang1");
        // markerOptions2.title("Current Position");
        mapGoogleMap.addMarker(markerOptions2);

        Marker marker = mapGoogleMap.addMarker(new MarkerOptions()
                .position(lautrupVang4)
                .title("lautrupVang4")
                .snippet("LautrupVang 4")
//                .icon(BitmapDescriptorFactory                       .fromResource(R.drawable.mr_ic_play_dark)
//                )
        );

        PolylineOptions rectOptions = new PolylineOptions()
                .width(3)
                .color(Color.BLUE)
                .add(dtuCampus,
                        lautrupVang4,
                        lautrupVang1
                );
        mapGoogleMap.addPolyline(rectOptions);

        // Zoom - 1 = langt ude, 15 tæt på.
        mapGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(dtuCampus, 15));

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
        mapGoogleMap.clear();
        latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        Marker m = mapGoogleMap.addMarker(markerOptions);

        Toast.makeText(getActivity(), "Location Changed", Toast.LENGTH_SHORT).show();

        //If you only need one location, unregister the listener
        //LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d("jjTest", "onConnectionFailed");

    }
}
