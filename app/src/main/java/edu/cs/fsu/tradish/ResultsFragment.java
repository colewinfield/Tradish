package edu.cs.fsu.tradish;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ResultsFragment extends Fragment {
    // Example
    private ArrayList<Restaurant> exampleList;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        exampleList = new ArrayList<Restaurant>();
        exampleList.add(new Restaurant("Name 1","Mexican", getText(R.string.lipsum).toString(), 1));
        exampleList.add(new Restaurant("Name 2", "Chinese", getText(R.string.lipsum).toString(), 2));
        exampleList.add(new Restaurant("Name 3", "Indian", getText(R.string.lipsum).toString(), 3));
        exampleList.add(new Restaurant("Name 4","Korean", getText(R.string.lipsum).toString(), 1));
        exampleList.add(new Restaurant("Name 5", "Irish", getText(R.string.lipsum).toString(), 2));
        exampleList.add(new Restaurant("Name 6", "Canadian", getText(R.string.lipsum).toString(), 3));
        exampleList.add(new Restaurant("Name 7","Mexican", getText(R.string.lipsum).toString(), 1));
        exampleList.add(new Restaurant("Name 8", "Jamaican", getText(R.string.lipsum).toString(), 2));
        exampleList.add(new Restaurant("Name 9", "Indian", getText(R.string.lipsum).toString(), 3));
        exampleList.add(new Restaurant("Name 10","Thai", getText(R.string.lipsum).toString(), 1));
        exampleList.add(new Restaurant("Name 11", "Chinese", getText(R.string.lipsum).toString(), 2));
        exampleList.add(new Restaurant("Name 12", "Vietnamese", getText(R.string.lipsum).toString(), 3));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search_results, container, false);

        mRecyclerView = rootView.findViewById(R.id.resultsRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mAdapter = new RestaurantAdapter(exampleList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        return rootView;
    }
}
