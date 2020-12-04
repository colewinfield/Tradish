package edu.cs.fsu.tradish;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
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
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;


public class ResultsFragment extends Fragment {

    public static int DISTANCE_RADIUS_DEFAULT = 10;

    private SearchView mSearchView;
    private ScrollView mScrollView;
    private RecyclerView mRecyclerView;
    private RestaurantAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private DatabaseReference mFirebaseReference;
    private ArrayList<Restaurant> mRestaurants;
    private GeoLocation mGeoLocation;
    private OnResultsAdapterListener mListener;

    private Dialog mDialog;
    private TextView mDialogName;
    private TextView mDialogCategory;
    private Button mDialogNavigate;

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

        initDialog();

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
                                                    mListener.onSetAdapter();

                                                    startActivity(intent);

                                                    mDialog.cancel();

                                                }
                                            });
                                            mDialog.show();
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

    public void initDialog() {
        mDialog = new Dialog(getContext());
        mDialog.setContentView(R.layout.dialog_restaurant);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        mDialogName = mDialog.findViewById(R.id.dialog_restaurant_name);
        mDialogCategory = mDialog.findViewById(R.id.dialog_restaurant_category);
        mDialogNavigate = mDialog.findViewById(R.id.dialog_navigation_button);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnResultsAdapterListener) {
            mListener = (OnResultsAdapterListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnResultsAdapterListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnResultsAdapterListener {
        void onSetAdapter();
    }

}
