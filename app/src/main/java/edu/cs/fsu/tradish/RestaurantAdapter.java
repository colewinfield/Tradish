package edu.cs.fsu.tradish;

import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
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
    private Dialog mDialog;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public static class RestaurantViewHolder extends RecyclerView.ViewHolder {
        public TextView mNameView;
        public TextView mDescriptionView;
        public TextView mAuthenticityView;
        public TextView mFoodCategoryView;
        public RatingBar ratingBar;

        public RestaurantViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            mNameView = itemView.findViewById(R.id.nameOfRestaurantResult);
            mDescriptionView = itemView.findViewById(R.id.descriptionOfRestaurantResult);
            mFoodCategoryView = itemView.findViewById(R.id.categoryOfRestaurantResult);
            mAuthenticityView = itemView.findViewById(R.id.authenticityLevel);
            ratingBar = itemView.findViewById(R.id.ResultsRatingBar);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

    public RestaurantAdapter(ArrayList<Restaurant> restaurantArrayList) {
        mRestaurants = restaurantArrayList;
    }

    @NonNull
    @Override
    public RestaurantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.result_item, parent, false);

        RestaurantViewHolder viewHolder = new RestaurantViewHolder(view, mListener);


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantViewHolder holder, int position) {
        Restaurant currentRestaurant = mRestaurants.get(position);

        holder.mNameView.setText(currentRestaurant.getName());
        holder.mFoodCategoryView.setText(currentRestaurant.getCategory());
        holder.mDescriptionView.setText(currentRestaurant.getDescription());
        holder.mAuthenticityView.setText("Authenticity of " + currentRestaurant.getAuthenticityRating());
        holder.ratingBar.setMax(5);
        holder.ratingBar.setRating((int) currentRestaurant.getAuthenticityRating());
    }

    @Override
    public int getItemCount() {
        return mRestaurants.size();
    }
}
