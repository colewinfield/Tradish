package edu.cs.fsu.tradish;

import android.location.Location;

public class Restaurant {
    private String mName;
    private String mDescription;
    private String mCategory;
    private String mAddress;
    private int mReferenceId;
    private Location mLocation;

    public Restaurant() {
        // TODO: get a referenceID method, random number
    }

    public String getName() {
        return mName;
    }

    public String getDescription() {
        return mDescription;
    }

    public int getReferenceId() {
        return mReferenceId;
    }

    public Location getLocation() {
        return mLocation;
    }

    public String getCategory() {
        return mCategory;
    }

    public void setName(String name) {
        mName = name;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public void setCategory(String category) {
        mCategory = category;
    }

    public void setAddress(String address) {
        mAddress = address;
    }

    public void setLocation(Location location) {
        mLocation = location;
    }
}
