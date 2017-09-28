package com.example.zarea.firebase;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {


    @BindView(R.id.signin_email_ET)
    EditText signinEmailET;
    @BindView(R.id.signin_password_ET)
    EditText signinPasswordET;
    @BindView(R.id.sign_in_btn)
    Button signInBtn;
    @BindView(R.id.sign_out_btn)
    Button signOutBtn;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            startActivity(new Intent(MainActivity.this, SecondActivity.class));
            finish();
        }
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

    }

    @OnClick(R.id.sign_in_btn)
    public void login() {
        if (isValidData(signinEmailET, signinPasswordET)) {
            progressBar.setVisibility(View.VISIBLE);
            String email = signinEmailET.getText().toString().trim();
            String password = signinPasswordET.getText().toString();

            FirebaseAuth.getInstance()
                    .signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            progressBar.setVisibility(View.GONE);
                            if (task.isSuccessful()) {

                                getUserData();
                            } else {
                                Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
        }

    }

    private void getUserData() {
        FirebaseDatabase.getInstance().getReference()
                .child("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);

                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("uri", user.getUrl());
                        editor.putString("name", user.getfName() + " " + user.getlName());
                        editor.apply();

                        startActivity(new Intent(MainActivity.this, SecondActivity.class));
                        finishAffinity();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(MainActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
    @OnClick(R.id.sign_out_btn)
    public void signUp(){
        startActivity(new Intent(MainActivity.this, SignOutActivity.class));
        finishAffinity();

    }

}
