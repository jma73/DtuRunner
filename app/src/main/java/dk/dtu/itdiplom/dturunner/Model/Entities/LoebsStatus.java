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

    public boolean isLoebsAktivitetStartet;
    public UUID loebsAktivitetUUID;
    public LocationGoogleApi locationGoogleApi;

    public String mLastUpdateTime;
    public Location mCurrentLocation;
    //public double mDistanceAccumulated;          // denne kan måske godt udgå

    /*
        Liste med typen Location. Gemmes for at have alle de muligheder der er i Lokation typen.
     */
    public ArrayList<Location> locationList;     // obs: denne skal kun være her under udvikling. Brug PointInfo under LoebsAktivitet.
    public LoebsAktivitet loebsAktivitet;          // denne introduceres, og skal erstatte flere variabler

}
