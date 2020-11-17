package edu.cs.fsu.tradish;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements RegisterFragment.OnRegisterListener,
    LoginFragment.OnLoginListener, MainFragment.OnDashboardListener,
    NewLocationFragment.OnNewLocationListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        main();
    }

    private void main() {

        // ######################################################################
        // # I DON'T HAVE A MAIN DASHBOARD YET. MACAYLA MIGHT WANT              #
        // # TO DESIGN THAT PART. FOR NOW, I HAVE A "FAUX" RESULTS              #
        // # PAGE SHOWING, JUST SO I CAN GET A LOOK AT HOW THE RESULTS PAGE     #
        // # WILL LOOK.                                                         #
        // ######################################################################

        if (isUserLoggedIn()) {
            onStartDashBoard();
        } else {
            onStartLogin();
        }

    }


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

    public boolean isUserLoggedIn() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        return (auth.getCurrentUser() != null);
    }

    @Override
    public void onStartNewLocation() {
        onStartNewLocationFragment();
    }

    @Override
    public void onStartDashboardFromNL() {
        onStartDashBoard();
    }
}