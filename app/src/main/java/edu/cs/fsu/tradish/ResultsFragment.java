package edu.cs.fsu.tradish;

import android.content.Context;
import android.os.Bundle;
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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


public class ResultsFragment extends Fragment {
    private SearchView mSearchView;
    private RecyclerView mRecyclerView;
    private RestaurantAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private DatabaseReference mFirebaseReference;
    private ArrayList<Restaurant> mRestaurants;

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

        mRestaurants = new ArrayList<>();

        mFirebaseReference = FirebaseDatabase.getInstance().getReference("Restaurants");

        mFirebaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot item : snapshot.getChildren()) {
                    Restaurant restaurant = item.getValue(Restaurant.class);
                    mRestaurants.add(restaurant);
                }

                mAdapter = new RestaurantAdapter(mRestaurants);
                mRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

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
