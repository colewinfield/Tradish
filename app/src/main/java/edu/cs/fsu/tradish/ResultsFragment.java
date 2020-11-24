package edu.cs.fsu.tradish;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.sql.Struct;
import java.util.ArrayList;

import static android.content.ContentValues.TAG;


public class ResultsFragment extends Fragment {

    public static int DISTANCE_RADIUS_DEFAULT = 10;

    private SearchView mSearchView;
    private RecyclerView mRecyclerView;
    private RestaurantAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private DatabaseReference mFirebaseReference;
    private ArrayList<Restaurant> mRestaurants;
    private GeoLocation mGeoLocation;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);

        mRecyclerView = rootView.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mSearchView = rootView.findViewById(R.id.searchView);

        Bundle extras = getArguments();

        if (extras != null) {
            double latitude = extras.getDouble(MainActivity.EXTRA_LATITUDE);
            double longitude = extras.getDouble(MainActivity.EXTRA_LONGITUDE);

            mGeoLocation = new GeoLocation(latitude, longitude);

            Log.d(TAG, "Checking GeoLocation of user in ResultsFragment: " +
                    "Latitude: " + latitude + ", Longitude: " + longitude);
        }

        mRestaurants = new ArrayList<>();

        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("GeoFire");
        GeoFire geoFire = new GeoFire(ref);

        if (mGeoLocation != null) {
            GeoQuery geoQuery = geoFire.queryAtLocation(mGeoLocation, DISTANCE_RADIUS_DEFAULT);
            geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
                @Override
                public void onKeyEntered(String key, GeoLocation location) {

                    FirebaseDatabase.getInstance().getReference("Restaurants").child(key)
                            .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Log.d(TAG, "onDataChange: Checking if entered.");


                            String name = snapshot.child("name").getValue(String.class);
                            Log.d(TAG, "onDataChange name: " + name);
                            Restaurant restaurant = snapshot.getValue(Restaurant.class);
                            mRestaurants.add(restaurant);
                            mAdapter = new RestaurantAdapter(mRestaurants);
                            mRecyclerView.setAdapter(mAdapter);

                            mAdapter.setOnItemClickListener(new RestaurantAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(int position) {
                                    mRestaurants.get(position);

                                    // TODO: Finish after mainFragment
                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onKeyExited(String key) {

                }

                @Override
                public void onKeyMoved(String key, GeoLocation location) {

                }

                @Override
                public void onGeoQueryReady() {

                }

                @Override
                public void onGeoQueryError(DatabaseError error) {

                }
            });


        }

        if (mSearchView != null) {
            mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    search(newText);
                    return true;
                }
            });
        }


        return rootView;
    }

    private void search(String text) {
        ArrayList<Restaurant> restaurants = new ArrayList<>();

        for (Restaurant restaurant : mRestaurants) {
            if (restaurant.getCategory().toLowerCase().contains(text.toLowerCase())) {
                restaurants.add(restaurant);
            } else if (restaurant.getDescription().toLowerCase().contains(text.toLowerCase())) {
                restaurants.add(restaurant);
            } else if (restaurant.getName().toLowerCase().contains(text.toLowerCase())) {
                restaurants.add(restaurant);
            }
        }

        RestaurantAdapter adapter = new RestaurantAdapter(restaurants);
        mRecyclerView.setAdapter(adapter);
    }

}
