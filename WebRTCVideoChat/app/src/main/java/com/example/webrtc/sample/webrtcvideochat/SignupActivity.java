package com.example.webrtc.sample.webrtcvideochat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.webrtc.sample.webrtcvideochat.commons.Constants;
import com.example.webrtc.sample.webrtcvideochat.models.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";

    @BindView(R.id.input_username) EditText edtUsername;
    @BindView(R.id.input_fname) EditText edtFname;
    @BindView(R.id.input_lname) EditText edtLname;
    @BindView(R.id.input_email) EditText edtEmail;
    @BindView(R.id.input_age) EditText edtAge;
    @BindView(R.id.input_password) EditText edtPassword;
    @BindView(R.id.input_reEnterPassword) EditText edtReEnterPasswordText;
    @BindView(R.id.btn_signup) Button btnSignupButton;
    @BindView(R.id.link_login) TextView _loginLink;
    private DatabaseReference mDatabase;


    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);
        mDatabase = FirebaseDatabase.getInstance().getReference("users");
        btnSignupButton.setOnClickListener(view->signup());

        _loginLink.setOnClickListener(view->{
            Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        });
    }

    public void signup() {
        Log.d(TAG, "Signup");

        Log.e(TAG, "signup:"+validate() );
        if (!validate()) {
            onSignupFailed();
            return;
        }

        btnSignupButton.setEnabled(false);
        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();
        String id=mDatabase.push().getKey();
        String username = edtUsername.getText().toString();
        String fname = edtFname.getText().toString();
        String lname = edtLname.getText().toString();
        String email = edtEmail.getText().toString();
        int age = Integer.parseInt(edtAge.getText().toString());
        String password = edtPassword.getText().toString();
        User user=new User(username,password,fname,lname,age,email,false);
        mDatabase.child(username).setValue(user);
        progressDialog.dismiss();
        startActivity(new Intent(this,LoginActivity.class));
        finish();

    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "signup failed", Toast.LENGTH_LONG).show();
        btnSignupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;
        String username = edtUsername.getText().toString();
        String firstname = edtFname.getText().toString();
        String lastname = edtLname.getText().toString();
        String email = edtEmail.getText().toString();
        String age = edtAge.getText().toString();
        String password = edtPassword.getText().toString();
        String reEnterPassword = edtReEnterPasswordText.getText().toString();
        if (username.isEmpty() || username.length() < 3) {
            edtUsername.setError("at least 3 characters");
            valid = false;
        } else {
            edtUsername.setError(null);
        }

        if (firstname.isEmpty()) {
            edtFname.setError("Enter first name");
            valid = false;
        } else {
            edtFname.setError(null);
        }

        if (lastname.isEmpty()) {
            edtLname.setError("Enter last name");
            valid = false;
        } else {
            edtLname.setError(null);
        }


        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edtEmail.setError("enter a valid email address");
            valid = false;
        } else {
            edtEmail.setError(null);
        }

        if (age.isEmpty() || age.length()<=0) {
            edtAge.setError("Enter Valid age");
            valid = false;
        } else {
            edtAge.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            edtPassword.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            edtPassword.setError(null);
        }

        if (reEnterPassword.isEmpty() || reEnterPassword.length() < 4 || reEnterPassword.length() > 10 || !(reEnterPassword.equals(password))) {
            edtReEnterPasswordText.setError("Password Do not match");
            valid = false;
        } else {
            edtReEnterPasswordText.setError(null);
        }

        return valid;
    }


}