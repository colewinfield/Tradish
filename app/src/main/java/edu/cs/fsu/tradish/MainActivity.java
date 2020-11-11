package edu.cs.fsu.tradish;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity implements RegisterFragment.OnRegisterListener {

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

        ResultsFragment fragment = new ResultsFragment();
        String tag = ResultsFragment.class.getCanonicalName();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_frame, fragment, tag).commit();
    }


    @Override
    public void onStartDashboard() {

    }
}