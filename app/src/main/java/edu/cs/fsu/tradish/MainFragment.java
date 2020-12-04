package edu.cs.fsu.tradish;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import androidx.transition.TransitionInflater;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.geofire.GeoLocation;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class MainFragment extends Fragment {
    private FloatingActionButton mSearchFAB;
    private FloatingActionButton mNewLocationFAB;
    private FloatingActionButton mLogout;
    private OnDashboardListener mListener;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private double mLongitude, mLatitude;

    private RecyclerView mRecyclerView;
    private RestaurantAdapter mAdapter;
    private ArrayList<Restaurant> mRestaurants;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.LayoutManager mLayoutManagerRecents;

    private RecyclerView mRecentSpots;
    private RestaurantAdapter mRecentsAdapter;

    private Dialog mDialog;
    private TextView mDialogName;
    private TextView mDialogCategory;
    private Button mDialogNavigate;


    //private Toolbar toolbar;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TransitionInflater inflater = TransitionInflater.from(requireContext());
        setEnterTransition(inflater.inflateTransition(R.transition.slide_left));
        setExitTransition(inflater.inflateTransition(R.transition.slide_right));
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
        mLogout = view.findViewById(R.id.fab_logout);

        mRecyclerView = view.findViewById(R.id.dashboardRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecentSpots = view.findViewById(R.id.recentSpotsRecycler);
        mRecentSpots.setHasFixedSize(true);
        mLayoutManagerRecents = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        mRecentSpots.setLayoutManager(mLayoutManagerRecents);


        initDialog();

        mRestaurants = new ArrayList<>();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        Query query = reference.child("Restaurants").
                orderByChild("username").equalTo(MainActivity.sCurrentUser.getUsername());

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    Log.d(TAG, "onDataChange: dataSnapshot exists");

                    for (DataSnapshot item : dataSnapshot.getChildren()) {
                        Restaurant restaurant = item.getValue(Restaurant.class);
                        mRestaurants.add(restaurant);
                        Log.d(TAG, "onDataChange: MainFragment: " + restaurant.toString());
                    }

                    mAdapter = new RestaurantAdapter(mRestaurants);
                    mRecyclerView.setAdapter(mAdapter);

                    mAdapter.setOnItemClickListener(new RestaurantAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(int position) {
                            Log.d(TAG, "Test Click: " + mRestaurants.get(position));
                            final Restaurant restaurant = mRestaurants.get(position);
                            final RestaurantLocation location = restaurant.getLocation();

                            mDialogName.setText(restaurant.getName());
                            mDialogCategory.setText(restaurant.getCategory());
                            mDialogNavigate.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(Intent.ACTION_VIEW,
                                            Uri.parse("google.navigation:q="
                                                    + location.getLatitude()
                                                    + ","
                                                    + location.getLongitude())
                                    );
                                    intent.setPackage("com.google.android.apps.maps");
                                    MainActivity.sRecentList.add(restaurant);
                                    setAdapter();

                                    startActivity(intent);

                                }
                            });
                            mDialog.show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        mNewLocationFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onStartNewLocation();
            }
        });
        mLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mListener.Logout())
                {
                    mListener.onStartLoginFragment();
                }
            }
        });

        mRecentSpots.setAdapter(MainActivity.sRestaurantAdapter);
    }

    public void initDialog() {
        mDialog = new Dialog(getContext());
        mDialog.setContentView(R.layout.dialog_restaurant);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        mDialogName = mDialog.findViewById(R.id.dialog_restaurant_name);
        mDialogCategory = mDialog.findViewById(R.id.dialog_restaurant_category);
        mDialogNavigate = mDialog.findViewById(R.id.dialog_navigation_button);
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
        boolean Logout();
        void onStartLoginFragment();
    }

    public void setAdapter(){
        mRecentSpots.setAdapter(MainActivity.sRestaurantAdapter);
    }

}
