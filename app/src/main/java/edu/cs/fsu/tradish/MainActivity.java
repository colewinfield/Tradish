package edu.cs.fsu.tradish;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;

import javax.xml.transform.Result;

public class MainActivity extends AppCompatActivity implements RegisterFragment.OnRegisterListener,
    LoginFragment.OnLoginListener, MainFragment.OnDashboardListener,
    NewLocationFragment.OnNewLocationListener {

    // ##########################################################################################
    // # When the activity is created, main() is called. main() does the start-up logic.        #
    // ##########################################################################################

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        main();
    }

    // ##########################################################################################
    // # Checks if the user has previously logged in or not. If they have, it goes right to     #
    // # to the dashboard. Else, to the login screen.                                           #
    // ##########################################################################################

    private void main() {

        if (isUserLoggedIn()) {
            onStartDashBoard();
        } else {
            onStartLogin();
        }

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
                .replace(R.id.fragment_frame, fragment, tag).commit();
    }

    private void onStartSearchFragment() {
        ResultsFragment fragment = new ResultsFragment();
        String tag = ResultsFragment.class.getCanonicalName();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_frame, fragment, tag).commit();
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
    public void onStartSearch() {
        onStartSearchFragment();
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
}