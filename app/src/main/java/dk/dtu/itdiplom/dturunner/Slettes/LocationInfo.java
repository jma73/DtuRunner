package dk.dtu.itdiplom.dturunner.Slettes;

import java.util.Date;

/**
 * Created by JanMøller on 31-10-2015.
 */
public class LocationInfo {

    private Date mTimestamp;
    private double mLatitude;
    private double mLongitude;

    // distance? speed? heart

    // methods todo jan:
    //   - get printable / tostring
    //   -

    public void LocationInfo() {
    }

    public double getLatitude(){
        return mLatitude;
    }

    public void setLatitude(double latitude){
        mLatitude = latitude;
    }

    public double getLongitude(){
        return mLongitude;
    }

    public void setLongitude(double longitude){
        mLongitude = longitude;
    }


}
