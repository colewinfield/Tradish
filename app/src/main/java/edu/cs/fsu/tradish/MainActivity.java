package edu.cs.fsu.tradish;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.preference.PreferenceManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity implements RegisterFragment.OnRegisterListener,
    LoginFragment.OnLoginListener, MainFragment.OnDashboardListener,
    NewLocationFragment.OnNewLocationListener, ResultsFragment.OnResultsAdapterListener {

    public static final String EXTRA_LATITUDE = "latitude";
    public static final String EXTRA_LONGITUDE = "LONGITUDE";
    public static User sCurrentUser = new User();
    public static ArrayList<Restaurant> sRecentList;
    public static RestaurantAdapter sRestaurantAdapter;


    // ##########################################################################################
    // # When the activity is created, main() is called. main() does the start-up logic.        #
    // ##########################################################################################

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int apiVersion = Build.VERSION.SDK_INT;
        if (apiVersion > Build.VERSION_CODES.LOLLIPOP_MR1) {
            if (!checkIfPermitted()) {
                requestPermission();
            }
        }

        main();
    }

    // ##########################################################################################
    // # Checks if the user has previously logged in or not. If they have, it goes right to     #
    // # to the dashboard. Else, to the login screen.                                           #
    // ##########################################################################################

    private void main() {

        if (isUserLoggedIn()) {
            FirebaseAuth auth = FirebaseAuth.getInstance();
            sCurrentUser.setUsername(auth.getUid());
            Log.d(TAG, "main: USERNAME: " + auth.getUid());
            onStartDashBoard();
        } else {
            onStartLogin();
        }

        sRecentList = getRecentList();

        if (sRecentList == null) {
            sRecentList = new ArrayList<>();
        }

        sRestaurantAdapter = new RestaurantAdapter(sRecentList);
    }


    // ##########################################################################################
    // # The below methods are just methods used to create a fragment of some type and then     #
    // # use the FragmentManager to replace the current fragment with another (when switching   #
    // # to different screens).                                                                 #
    // ##########################################################################################

    private void onStartMain() {
        MainFragment fragment = new MainFragment();
        String tag = MainFragment.class.getCanonicalName();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_frame, fragment, tag).commit();
    }

    private void onStartLogin() {
        LoginFragment fragment = new LoginFragment();
        String tag = LoginFragment.class.getCanonicalName();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_frame, fragment, tag).commit();
    }

    private void onStartRegister() {
        RegisterFragment fragment = new RegisterFragment();
        String tag = RegisterFragment.class.getCanonicalName();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_frame, fragment, tag).commit();
    }

    private void onStartNewLocationFragment() {
        NewLocationFragment fragment = new NewLocationFragment();
        String tag = NewLocationFragment.class.getCanonicalName();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_frame, fragment, tag)
                .addToBackStack(null)
                .commit();
    }

    private void onStartSearchFragment(double latitude, double longitude) {
        ResultsFragment fragment = new ResultsFragment();

        Log.d(TAG, "MainActivity Latitude: " + latitude + ", Longitude: " + longitude);


        Bundle bundle = new Bundle();
        bundle.putDouble(EXTRA_LATITUDE, latitude);
        bundle.putDouble(EXTRA_LONGITUDE, longitude);
        fragment.setArguments(bundle);

        String tag = ResultsFragment.class.getCanonicalName();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_frame, fragment, tag)
                .addToBackStack(null)
                .commit();
    }

    // ##########################################################################################
    // # These are the required overrides for the implemented interfaces. They're used as call- #
    // # -backs from fragments to start a new fragment when a button or some event occurs.      #
    // # Essentially used to communicate with the MainActivity from a child fragment.           #
    // ##########################################################################################

    @Override
    public void onStartLoginFragment() {
        onStartLogin();
    }

    @Override
    public void onStartRegisterFragment() {
        onStartRegister();
    }

    @Override
    public void onStartDashBoard() {
        onStartMain();
    }

    @Override
    public void onStartNewLocation() {
        onStartNewLocationFragment();
    }

    @Override
    public void onStartSearch(double latitude, double longitude) {
        onStartSearchFragment(latitude, longitude);
    }

    @Override
    public void onStartDashboardFromNL() {
        onStartDashBoard();
    }

    // ##########################################################################################
    // # Uses Firebase to identify if the user has previously logged in or not.                 #
    // ##########################################################################################

    public boolean isUserLoggedIn() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        return (auth.getCurrentUser() != null);
    }
    // ##########################################################################################
    // # Uses Firebase to grab the current instance and sign out the user. Returns true if the  #
    // # user has succesfully signed out.                                                       #
    // ##########################################################################################
    public boolean Logout() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signOut();
        saveRecents();
        return(auth.getCurrentUser() == null);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == 101) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT)
                        .show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private boolean checkIfPermitted() {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.GET_ACCOUNTS);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_NETWORK_STATE,
                        Manifest.permission.ACCESS_COARSE_LOCATION},
                101);
    }

    @Override
    public void onSetAdapter() {
        FragmentManager fm = getSupportFragmentManager();
        MainFragment fragment = (MainFragment) fm.findFragmentByTag(MainFragment.class.getCanonicalName());
        fragment.setAdapter();
    }

    private void saveRecents() {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        Gson gson = new Gson();

        String json = gson.toJson(sRecentList);

        editor.putString(sCurrentUser.getUsername(), json);
        editor.commit();
    }

    private ArrayList<Restaurant> getRecentList() {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        Gson gson = new Gson();
        String json = sharedPrefs.getString(sCurrentUser.getUsername(), "");
        Type type = new TypeToken<List<Restaurant>>() {}.getType();

        return gson.fromJson(json, type);
    }

    @Override
    protected void onStop() {
        super.onStop();
        saveRecents();
    }
}