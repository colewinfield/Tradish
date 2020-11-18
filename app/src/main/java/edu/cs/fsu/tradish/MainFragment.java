package edu.cs.fsu.tradish;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

public class MainFragment extends Fragment {
    private FloatingActionButton mSearchFAB;
    private FloatingActionButton mNewLocationFAB;
    private OnDashboardListener mListener;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
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
        drawerLayout = view.findViewById(R.id.drawer_layout);
        navigationView = view.findViewById(R.id.nav_view);
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

        mSearchFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onStartSearch();
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
        void onStartSearch();
    }


}
