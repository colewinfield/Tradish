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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.io.IOException;
import java.util.List;

public class NewLocationFragment extends Fragment {
    private EditText mRestaurantName;
    private EditText mCategory;
    private EditText mDescription;
    private Button mShareButton;
    private RatingBar mAuthenticityLevel;
    private Location mLocation;
    private String mAddress;
    private Restaurant mRestaurant;
    private OnNewLocationListener mListener;



    @SuppressLint("MissingPermission")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_new_location, container, false);

        int apiVersion = Build.VERSION.SDK_INT;
        if (apiVersion > Build.VERSION_CODES.LOLLIPOP_MR1) {
            if (!checkIfPermitted()) {
                requestPermission();
            }
        }

        init(rootView);

        LocationManager mLocationManager = (LocationManager)
                getContext().getSystemService(Context.LOCATION_SERVICE);

        mLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        final LocationListener mLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                Geocoder geo = new Geocoder(getContext());
                List<Address> addresses = null;

                try {
                    addresses = geo.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (addresses != null) {
                    mRestaurant.setAddress(addresses.get(0).getAddressLine(0));
                }

                mRestaurant.setLocation(location);
            }
        };

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

        mShareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRestaurant.setName(mRestaurantName.getText().toString());
                mRestaurant.setCategory(mCategory.getText().toString());
                mRestaurant.setDescription(mDescription.getText().toString());
                //TODO: send to Firebase database

                mListener.onStartDashboardFromNL();
            }
        });
    }


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
