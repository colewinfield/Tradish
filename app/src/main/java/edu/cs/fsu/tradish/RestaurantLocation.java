package edu.cs.fsu.tradish;

import android.location.Location;

public class RestaurantLocation {
    private double mLatitude;
    private double mLongitude;

    public RestaurantLocation() {}

    public Location getLocation() {
        Location newLocation = new Location("restaurant location");
        newLocation.setLatitude(mLatitude);
        newLocation.setLongitude(mLongitude);

        return newLocation;
    }

    public double getLatitude() {
        return mLatitude;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public void setLatitude(double latitude) {
        mLatitude = latitude;
    }

    public void setLongitude(double longitude) {
        mLongitude = longitude;
    }


}
