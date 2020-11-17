package edu.cs.fsu.tradish;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.io.IOException;
import java.util.List;

import static android.content.ContentValues.TAG;

public class NewLocationFragment extends Fragment {
    private EditText mRestaurantName;
    private EditText mCategory;
    private EditText mDescription;
    private Button mShareButton;
    private RatingBar mAuthenticityLevel;
    private String mAddress;
    private Restaurant mRestaurant;
    private OnNewLocationListener mListener;

    // ##########################################################################################
    // # onCreateView: used to initialize the widgets within the UI. Using init(rootView) to    #
    // # aid with this (just to declutter the method).                                          #
    // # Checks whether or not the user has granted location permissions for application.       #
    // # Those using Marshmallow (API 26) or higher need to manually allow it. When this frag   #
    // # -ment is created, it requests permissions. LocationManager and a LocationListener      #
    // # are added below to get the location of the person after the submit button is clicked.  #
    // # Uses the EditTexts to fill out the rest of a Restaurant object that'll be used to      #
    // # store within the Firebase database. When button is pressed, listener is activated and  #
    // # then released (to save battery).                                                       #
    // # TODO: Need to setup Firebase database, perform form completeness check, make the user  #
    // # TODO: -select a category that is predetermined (Chinese, Italian, Mexican, etc.)..     #
    // ##########################################################################################


    @SuppressLint("MissingPermission")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_new_location, container, false);

        init(rootView);

        int apiVersion = Build.VERSION.SDK_INT;
        if (apiVersion > Build.VERSION_CODES.LOLLIPOP_MR1) {
            if (!checkIfPermitted()) {
                requestPermission();
            }
        }


            final LocationManager lm = (LocationManager) getContext().getSystemService(
                    Context.LOCATION_SERVICE);
            final LocationListener ll = new LocationListener() {
                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {
                }

                @Override
                public void onProviderEnabled(String provider) {
                    Toast.makeText(getContext(), "Waiting for location",
                            Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onProviderDisabled(String provider) {
                    Toast.makeText(getContext(), "Connection Lost",
                            Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onLocationChanged(Location location) {
                    Geocoder geo = new Geocoder(getContext());
                    List<Address> addresses = null;

                    try {
                        addresses = geo.getFromLocation(location.getLatitude(),
                                location.getLongitude(), 10);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (addresses.size() > 0 &&
                            addresses.get(0).getMaxAddressLineIndex() >= 0) {
                        mRestaurant.setAddress(addresses.get(0).getAddressLine(0));
                        mRestaurant.setLocation(location);
                    }

                    Log.d(TAG, "Restaurant: " + mRestaurant.toString());
                    lm.removeUpdates(this);
                    mListener.onStartDashboardFromNL();
                }

            };



            mShareButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mRestaurant.setName(mRestaurantName.getText().toString());
                    mRestaurant.setCategory(mCategory.getText().toString());
                    mRestaurant.setDescription(mDescription.getText().toString());

                    Toast.makeText(getContext(), "Waiting for location ...",
                            Toast.LENGTH_SHORT).show();
                    lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, ll);
                }
            });

        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void init(View view) {
        mRestaurant = new Restaurant();
        mRestaurantName = view.findViewById(R.id.editTextRestaurantName);
        mCategory = view.findViewById(R.id.editTextCategoryRestaurant);
        mDescription = view.findViewById(R.id.descriptionOfRestaurant);
        mAuthenticityLevel = view.findViewById(R.id.ratingBar);
        mShareButton = view.findViewById(R.id.shareButton);


    }

    // ##########################################################################################
    // # Below are methods sed in checking the permissions of the application. It checks if     #
    // # the user has granted permissions yet, and if they haven't, requests that the user      #
    // # grants them with a dialog. This is needed for API 26+ (Marshmallow).                   #
    // ##########################################################################################

    private boolean checkIfPermitted() {
        int result = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.GET_ACCOUNTS);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_NETWORK_STATE,
                        Manifest.permission.ACCESS_COARSE_LOCATION},
                101);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == 101) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            } else {
                Toast.makeText(getContext(), "Permission denied", Toast.LENGTH_SHORT)
                        .show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    // ##########################################################################################
    // # Routine listener methods to set up an interaction between MainActivity and fragment.   #
    // ##########################################################################################

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof OnNewLocationListener) {
            mListener = (OnNewLocationListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnNewLocationListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnNewLocationListener {
        void onStartDashboardFromNL();
    }

}
