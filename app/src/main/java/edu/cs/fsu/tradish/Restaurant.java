package edu.cs.fsu.tradish;

import android.location.Location;

public class Restaurant {
    private String mName;
    private String mDescription;
    private String mCategory;
    private int mReferenceId;
    private Location mLocation;

    public Restaurant(String name, String description, String category, int referenceId) {
        mName = name;
        mDescription = description;
        mReferenceId = referenceId;
        mCategory = category;
//        mLocation = location;
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
}
