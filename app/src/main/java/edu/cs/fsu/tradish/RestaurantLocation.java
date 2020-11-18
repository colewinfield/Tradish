package edu.cs.fsu.tradish;

import android.location.Location;

public class RestaurantLocation {
    private static double mLatitude;
    private static double mLongitude;

    public RestaurantLocation() {}

    public static Location getLocation() {
        Location newLocation = new Location("restaurant location");
        newLocation.setLatitude(mLatitude);
        newLocation.setLongitude(mLongitude);

        return newLocation;
    }

    public static double getLatitude() {
        return mLatitude;
    }

    public static double getLongitude() {
        return mLongitude;
    }

    public static void setLatitude(double mLatitude) {
        RestaurantLocation.mLatitude = mLatitude;
    }

    public static void setLongitude(double mLongitude) {
        RestaurantLocation.mLongitude = mLongitude;
    }

    
}
