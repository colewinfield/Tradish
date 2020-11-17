package edu.cs.fsu.tradish;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

// ##########################################################################################
// # This is a RecyclerView adapter that aids in the use of a RecyclerView. It's the        #
// # replacement for a ListView. ListViews cause a lot of performance degradation with long #
// # lists. Use this instead when gathering the search results.                             #
// ##########################################################################################

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.RestaurantViewHolder> {
    private ArrayList<Restaurant> mRestaurants;

    public static class RestaurantViewHolder extends RecyclerView.ViewHolder {
        public TextView mNameView;
        public TextView mDescriptionView;
        public TextView mDistanceView;
        public TextView mFoodCategoryView;

        public RestaurantViewHolder(@NonNull View itemView) {
            super(itemView);
            mNameView = itemView.findViewById(R.id.nameOfRestaurantResult);
            mDescriptionView = itemView.findViewById(R.id.descriptionOfRestaurantResult);
            mFoodCategoryView = itemView.findViewById(R.id.categoryOfRestaurantResult);
        }
    }

    public RestaurantAdapter(ArrayList<Restaurant> restaurantArrayList) {
        mRestaurants = restaurantArrayList;
    }

    @NonNull
    @Override
    public RestaurantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.result_item, parent, false);
        RestaurantViewHolder viewHolder = new RestaurantViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantViewHolder holder, int position) {
        Restaurant currentRestaurant = mRestaurants.get(position);

        holder.mNameView.setText(currentRestaurant.getName());
        holder.mFoodCategoryView.setText(currentRestaurant.getCategory());
        holder.mDescriptionView.setText(currentRestaurant.getDescription());
    }

    @Override
    public int getItemCount() {
        return mRestaurants.size();
    }
}
