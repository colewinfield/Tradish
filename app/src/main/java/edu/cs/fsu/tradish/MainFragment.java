package edu.cs.fsu.tradish;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.firebase.geofire.GeoLocation;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.io.IOException;
import java.util.List;

import static android.content.ContentValues.TAG;

public class MainFragment extends Fragment {
    private FloatingActionButton mSearchFAB;
    private FloatingActionButton mNewLocationFAB;
    private OnDashboardListener mListener;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private double mLongitude, mLatitude;
    //private Toolbar toolbar;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    // ##########################################################################################
    // # onCreateView: used to initialize the widgets within the UI. Using init(rootView) to    #
    // # aid with this (just to declutter the method).                                          #
    // ##########################################################################################

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        init(rootView);

        final LocationManager lm = (LocationManager) getContext().getSystemService(
                Context.LOCATION_SERVICE);
        final LocationListener ll = new LocationListener() {

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onProviderDisabled(String provider) {
                Toast.makeText(getContext(), "Connection Lost",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLocationChanged(Location location) {
                mLatitude = location.getLatitude();
                mLongitude = location.getLongitude();

                Log.d(TAG, "Latitude: " + mLatitude + ", Longitude: " + mLongitude);
                lm.removeUpdates(this);
                mListener.onStartSearch(mLatitude, mLongitude);
            }

        };

        mSearchFAB.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View view) {
                lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, ll);
            }
        });

        return rootView;
    }

    // ##########################################################################################
    // # init(View view) is used to initialize the views. It takes in the view that is inflated #
    // # from onCreateView. Uses this to findViewById.                                          #
    // # ***********Not yet completed, but it will have more event listeners. ******************#
    // ##########################################################################################

    private void init(View view) {
        mSearchFAB = view.findViewById(R.id.fab_search_main);
        mNewLocationFAB = view.findViewById(R.id.fab_new_location_main);
//        drawerLayout = view.findViewById(R.id.drawer_layout);
//        navigationView = view.findViewById(R.id.nav_view);
        //toolbar = view.findViewById(R.id.toolbar);
        // setSupportActionBar(toolbar);
        // ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open_nav, R.string.close_nav);
        //drawerLayout.addDrawerListener(toggle);
        //toggle.syncState();
        mNewLocationFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onStartNewLocation();
            }
        });


    }

    // ##########################################################################################
    // # Used anywhere a listener is needed. A listener is used to communicate with the Main-   #
    // # -Activity. mListener tells it to call "OnSomething" to start a new fragment.           #
    // ##########################################################################################

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnDashboardListener) {
            mListener = (OnDashboardListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnDashboardListener");
        }
    }

    // ##########################################################################################
    // # Detach the listener to avoid any memory leaks.                                         #
    // ##########################################################################################

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    // ##########################################################################################
    // # Interface for the Listener. MainActivity implements this interface, so it must have    #
    // # these methods that create different fragments.                                         #
    // ##########################################################################################

    public interface OnDashboardListener {
        void onStartNewLocation();
        void onStartSearch(double latitude, double longitude);
    }


}
