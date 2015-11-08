package dk.dtu.itdiplom.dturunner.Utils;

import android.location.Location;
import android.util.Log;

/**
 * Created by JanMøller on 05-11-2015.
 */
public class LocationUtils {

    /*
        Med float.
     */
//    public static float distFrom(float lat1, float lng1, float lat2, float lng2) {
//        double earthRadius = 6371000; //meters
//        double dLat = Math.toRadians(lat2-lat1);
//        double dLng = Math.toRadians(lng2-lng1);
//        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
//                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
//                        Math.sin(dLng/2) * Math.sin(dLng/2);
//        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
//        float dist = (float) (earthRadius * c);
//
//        return dist;
//    }

//    public static double distFromDouble(double latitude1, double longitude1, double latitude2, double longitude2) {
//        float distance = distFrom((float)latitude1, (float)longitude1, (float)latitude2, (float)longitude2  );
//
//        Log.d("jj", "distance: "+distance);
//
//        return (double) distance;
//    }

    /*
        Med double.
     */
    public static double getDistanceBetweenPoints(double lat1, double lng1, double lat2, double lng2 ){
        double earthRadius = 6371000; //meters
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lng2 - lng1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double distanceInMeters = Math.round(earthRadius * c);
        return distanceInMeters;
    }


    /*
    min egen test. skal nok bare slettes...
      http://stackoverflow.com/questions/21410698/calculating-speed-for-a-navigation-app-without-getspeed-method
     */
    long getSpeed(Location location, long distanceInMeters)
    {
        long lastTimeStamp = location.getTime();
        long timeDelta = (location.getTime() - lastTimeStamp)/1000;

        long speed = 0;
        if(timeDelta > 0){
            speed = (distanceInMeters/timeDelta);
        }
        return speed;
    }

    /*
        Det er vist blot dette der skal til...
     */
    public static double getSpeed(double distanceInMeters, long timeDelta)
    {
        double speed = (distanceInMeters/timeDelta);
        return speed;
    }


    public static double getDistanceBetweenPoints(Location location1, Location location2){

        double lat1 = location1.getLatitude();
        double lng1 = location1.getLongitude();
        double lat2 = location2.getLatitude();
        double lng2 = location2.getLongitude();


//        return (long) distFromDouble(lat1, lng1, lat2, lng2);
        return getDistanceBetweenPoints(lat1, lng1, lat2, lng2);

//        double dLat = Math.toRadians(lat2 - lat1);
//        double dLon = Math.toRadians(lng2 - lng1);
//        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
//                + Math.cos(Math.toRadians(lat1))
//                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
//                * Math.sin(dLon / 2);
//        double c = 2 * Math.asin(Math.sqrt(a));
//        long distanceInMeters = Math.round(6371000 * c);
//        return distanceInMeters;
    }

    public static double getSpeedBetweenPoints(Location location1, Location location2){

        // todo jan - 8/11-15: Stadig under test!!!
        double distanceInMeters = getDistanceBetweenPoints(location1, location2);

        long time1 = location1.getTime();
        long time2 = location2.getTime();

        long secondsPassed = (location2.getTime() - location1.getTime()) / 1000;

        Log.d("jj", "distanceInMeters: " + distanceInMeters);
        Log.d("jj", "secondsPassed: " + secondsPassed);
        Log.d("jj", "time1: " + time1);
        Log.d("jj", "time2: " + time2);

        if(distanceInMeters == 0)
            return 0;

        return distanceInMeters / secondsPassed;
    }

}
