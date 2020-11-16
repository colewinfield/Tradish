package edu.cs.fsu.tradish;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterFragment extends Fragment {
    private FirebaseAuth mFirebaseAuth;
    private EditText mLogin;
    private EditText mPassword;
    private Button mRegisterButton;
    private TextView mLoginInstead;
    private OnRegisterListener mListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_register_user, container, false);
        init(rootView);

        return rootView;
    }

    private void init(View view) {
        mLogin = view.findViewById(R.id.editTextEmailRegister);
        mPassword = view.findViewById(R.id.editTextPasswordRegister);
        mRegisterButton = view.findViewById(R.id.signupButtonRegister);
        mLoginInstead = view.findViewById(R.id.loginInsteadText);

        mLoginInstead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onStartLoginFragment();
            }
        });

        mFirebaseAuth = FirebaseAuth.getInstance();

        mRegisterButton.setOnClickListener(new View.OnClickListener() {
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
                    mFirebaseAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (!task.isSuccessful()) {
                                        Toast.makeText(getContext(), "Signup failed",
                                                Toast.LENGTH_SHORT).show();
                                    } else {
                                        mListener.onStartLoginFragment();
                                    }
                                }
                            });
                }
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnRegisterListener) {
            mListener = (OnRegisterListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnRegisterFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnRegisterListener {
        void onStartLoginFragment();
    }

}
