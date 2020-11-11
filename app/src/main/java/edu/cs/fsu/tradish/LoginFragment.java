package edu.cs.fsu.tradish;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


public class LoginFragment extends Fragment {
    private EditText mLogin;
    private EditText mPassword;
    private Button mLoginButton;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
    }

}
