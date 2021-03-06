package edu.cs.fsu.tradish;

import android.location.Location;

import java.util.UUID;

// ##########################################################################################
// # Object that'll be used to house the information of the newly created food location.    #
// # It will then be sent to the Firebase database.                                         #
// ##########################################################################################

public class Restaurant {
    private String mName;
    private String mDescription;
    private String mCategory;
    private String mAddress;
    private String mReferenceId;
    private String mUsername;
    private User mCreator;
    private RestaurantLocation mLocation;
    private double mAuthenticityRating;


    public Restaurant() {
        // TODO: get a referenceID method, random number
        mReferenceId = UUID.randomUUID().toString();
        mCreator = MainActivity.sCurrentUser;
    }

    public String getName() {
        return mName;
    }

    public String getDescription() {
        return mDescription;
    }

    public String getReferenceId() {
        return mReferenceId;
    }

    public RestaurantLocation getLocation() {
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

    public void setLocation(RestaurantLocation location) {
        mLocation = location;
    }

    public double getAuthenticityRating() {
        return mAuthenticityRating;
    }

    public void setAuthenticityRating(double authenticityRating) {
        mAuthenticityRating = authenticityRating;
    }

    public User getCreator() {
        return mCreator;
    }

    public void setCreator(User creator) {
        mCreator = creator;
    }

    public String getUsername() {
        return mUsername;
    }

    public void setUsername(String username) {
        mUsername = username;
    }

    @Override
    public String toString() {
        return "Restaurant{" +
                "mName='" + mName + '\'' +
                ", mDescription='" + mDescription + '\'' +
                ", mCategory='" + mCategory + '\'' +
                ", mAddress='" + mAddress + '\'' +
                ", mReferenceId=" + mReferenceId +
                ", mLocation=" + mLocation +
                '}';
    }
}
