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
import com.google.firebase.auth.FirebaseUser;

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
        mFirebaseAuth = FirebaseAuth.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);
        init(rootView);

        return rootView;
    }

    private void init(View view) {
        mLogin = view.findViewById(R.id.editTextEmailLogin);
        mPassword = view.findViewById(R.id.editTextPasswordLogin);
        mLoginButton = view.findViewById(R.id.loginButtonLogin);
        mRegisterButton = view.findViewById(R.id.signupButtonLogin);

        mFirebaseAuth = FirebaseAuth.getInstance();

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
                if( mFirebaseUser != null ){
                    mListener.onStartDashBoard();
                }
            }
        };

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
                            if (!task.isSuccessful()){
                                Toast.makeText(getContext(), "Login error, try again",
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                mListener.onStartDashBoard();
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

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

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnLoginListener {
        void onStartRegisterFragment();
        void onStartDashBoard();
    }

}
