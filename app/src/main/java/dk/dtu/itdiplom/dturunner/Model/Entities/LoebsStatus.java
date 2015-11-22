package dk.dtu.itdiplom.dturunner.Model.Entities;

import android.location.Location;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by jan on 19-11-2015.
 * Denne klasse er til diverse variabler som skal gemmes på tværs af aktiviteter og fragmenter.
 *  dvs. de kan tilgåes via singleton klassen.
 */
public class LoebsStatus {


    public static boolean isLoebsAktivitetStartet;
    public static UUID loebsAktivitetUUID;

    public static LocationGoogleApi locationGoogleApi;

    public static String mLastUpdateTime;
    public static Location mCurrentLocation;
    public static double mDistanceAccumulated;
    public static ArrayList<Location> locationList;     // obs: denne skal kun være her under udvikling. Brug PointInfo under LoebsAktivitet.
    public static LoebsAktivitet loebsAktivitet;          // denne introduceres, og skal erstatte flere variabler

}
