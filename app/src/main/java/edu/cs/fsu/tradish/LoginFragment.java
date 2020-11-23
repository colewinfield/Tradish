package edu.cs.fsu.tradish;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import static android.content.ContentValues.TAG;


public class LoginFragment extends Fragment {
    private EditText mLogin;
    private EditText mPassword;
    private Button mLoginButton;
    private Button mRegisterButton;
    private OnLoginListener mListener;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;


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
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);
        init(rootView);

        return rootView;
    }

    // ##########################################################################################
    // # init(View view) is used to initialize the views. It takes in the view that is inflated #
    // # from onCreateView. Uses this to findViewById.                                          #
    // # Also initializes the mRegisterButton and mLoginButton. The registerButton goes to a    #
    // # register fragment, in case the user has not yet registered. THe loginButton uses       #
    // # Firebase to check if the user is registered. Also checks if the form is filled out.    #
    // ##########################################################################################

    private void init(View view) {
        mLogin = view.findViewById(R.id.editTextEmailLogin);
        mPassword = view.findViewById(R.id.editTextPasswordLogin);
        mLoginButton = view.findViewById(R.id.loginButtonLogin);
        mRegisterButton = view.findViewById(R.id.signupButtonLogin);
        mFirebaseAuth = FirebaseAuth.getInstance();

        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onStartRegisterFragment();
            }
        });

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mLogin.getText().toString();
                String password = mPassword.getText().toString();

                if (email.isEmpty() && password.isEmpty()) {
                    Toast.makeText(getContext(), "Fields are empty", Toast.LENGTH_SHORT)
                            .show();
                } else if (password.isEmpty()) {
                    mPassword.setError("Password required");
                    mPassword.requestFocus();
                } else if (email.isEmpty()) {
                    mLogin.setError("Email required");
                    mLogin.requestFocus();
                } else {
                    mFirebaseAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(getActivity(),
                                    new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (!task.isSuccessful()) {
                                                Toast.makeText(getContext(), "Login error, try again",
                                                        Toast.LENGTH_SHORT).show();
                                            } else {
                                                Log.d(TAG, "onComplete: USERNAME: " + mFirebaseAuth.getUid());
                                                MainActivity.sCurrentUser.setUsername(mFirebaseAuth.getUid());
                                                mListener.onStartDashBoard();
                                            }
                                        }
                                    });
                }
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

        if (context instanceof OnLoginListener) {
            mListener = (OnLoginListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnLoginListener");
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

    public interface OnLoginListener {
        void onStartRegisterFragment();

        void onStartDashBoard();
    }

}
