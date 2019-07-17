package com.example.werp_india;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


/**
 * A simple {@link Fragment} subclass.
 */
public class SignUpFragment extends Fragment {


    public SignUpFragment() {
        // Required empty public constructor
    }

    private TextView alreadyHaveAnAccount;
    private FrameLayout parentFrameLayout;

    private EditText email;
    private EditText fullname;
    private EditText password;
    private EditText confirmpassword;

    private ImageButton closebtn;
    private Button signup_btn;

    private ProgressBar progressBar;

    private FirebaseAuth firebaseAuth;

    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        email = view.findViewById(R.id.sign_up_email);
        fullname = view.findViewById(R.id.sign_up_fullname);
        password = view.findViewById(R.id.sign_up_password);
        confirmpassword = view.findViewById(R.id.sign_up_confirm_password);

        closebtn = view.findViewById(R.id.sign_up_close_btn);
        signup_btn = view.findViewById(R.id.sign_up_btn);

        progressBar = view.findViewById(R.id.sign_up_progressbar);

        firebaseAuth = FirebaseAuth.getInstance();

        alreadyHaveAnAccount = view.findViewById(R.id.tv_already_have_an_account);
        parentFrameLayout = getActivity().findViewById(R.id.register_framelayout);

        return view;

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        alreadyHaveAnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(new SignInFragment());

            }
        });

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        fullname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        confirmpassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkEmailAndPassword();
            }
        });
    }


    private void setFragment(Fragment fragment) {

        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_from_left, R.anim.slideout_from_right);
        fragmentTransaction.replace(parentFrameLayout.getId(), fragment);
        fragmentTransaction.commit();

    }

    @SuppressLint("ResourceAsColor")
    private void checkInputs(){

        if(!TextUtils.isEmpty(email.getText())){

            if(!TextUtils.isEmpty(fullname.getText())){

                if (! TextUtils.isEmpty(password.getText()) && password.length() >= 8){

                    if(! TextUtils.isEmpty(confirmpassword.getText())){

                        signup_btn.setEnabled(true);
                        signup_btn.setTextColor(Color.rgb(255,255,255));

                    }else {
                        signup_btn.setEnabled(false);
                        signup_btn.setTextColor(Color.argb(50,255,255,255));
                    }

                }else {
                    signup_btn.setEnabled(false);
                    signup_btn.setTextColor(Color.argb(50,255,255,255));
                }
            }else{
                signup_btn.setEnabled(false);
                signup_btn.setTextColor(Color.argb(50,255,255,255));
            }
        } else {
            signup_btn.setEnabled(false);
            signup_btn.setTextColor(Color.argb(50,255,255,255));
        }
    }
    private void checkEmailAndPassword(){

        if(email.getText().toString().matches(emailPattern)){

            if(password.getText().equals(confirmpassword.getText())){

                progressBar.setVisibility(View.VISIBLE);
                signup_btn.setEnabled(false);
                signup_btn.setTextColor(Color.argb(50,255,255,255));

                firebaseAuth.createUserWithEmailAndPassword(email.getText().toString(),password.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete( Task<AuthResult> task) {

                                if(task.isSuccessful()){

                                    Intent mainIntent = new Intent(getActivity(), MainActivity.class);
                                    startActivity(mainIntent);
                                    getActivity().finish();

                                }else {
                                    signup_btn.setEnabled(true);
                                    signup_btn.setTextColor(Color.rgb(255,255,255));
                                    progressBar.setVisibility(View.INVISIBLE);
                                    String error = task.getException().getMessage();
                                    Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }else {

                confirmpassword.setError("Password doesn't match!");
            }

        }else {

            email.setError("Invalid Email");

        }


    }
}