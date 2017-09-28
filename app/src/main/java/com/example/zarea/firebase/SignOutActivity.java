package com.example.zarea.firebase;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignOutActivity extends BaseActivity {

    @BindView(R.id.sign_out_image)
    ImageView signOutImage;
    @BindView(R.id.first_name_ET)
    EditText firstNameET;
    @BindView(R.id.last_name_ET)
    EditText lastNameET;
    @BindView(R.id.signout_email_ET)
    EditText signoutEmailET;
    @BindView(R.id.signout_password_ET)
    EditText signoutPasswordET;
    @BindView(R.id.register)
    Button register;
    @BindView(R.id.sign_up_progress_bar)
    ProgressBar signUpProgressBar;

    Integer Request_Camera = 1, Select_File = 0;
    private Uri uri;
    private String url = "https://firebasestorage.googleapis.com/v0/b/alert-impulse-160200.appspot.com/o/photos%2F20150705_133055.jpg?alt=media&token=847ec2ee-eb8c-4b1d-9354-1c79d89cce8e";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_out);
        ButterKnife.bind(this);
    }
    @OnClick(R.id.sign_out_image)
    public void selectImage(){
        final CharSequence[] items = {"Camera", "Gallary", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(SignOutActivity.this);
        builder.setTitle("Add Image");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                if (items[i].equals("Camera")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, Request_Camera);



                } else if (items[i].equals("Gallary")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(intent.createChooser(intent, "Select File"), Select_File);

                } else if (items[i].equals("Cancal")) {
                    finish();
                }

            }
        });
        builder.show();

    }
        @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if (resultCode == RESULT_OK) {
                if (requestCode == Request_Camera) {

//                    Bundle bundle = data.getExtras();
//                    final Bitmap bitmap = (Bitmap) bundle.get("data");
//                    signOutImage.setImageBitmap(bitmap);
                    Uri uri = data.getData();
                    signOutImage.setImageURI(uri);
                    FirebaseStorage.getInstance().getReference()
                            .child("photos")
                            .child(uri.getLastPathSegment())
                            .putFile(uri)
                            .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        url = task.getResult().getDownloadUrl().toString();
                                        Toast.makeText(SignOutActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(SignOutActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                    Picasso.with(this)
                            .load(uri)
                            .placeholder(R.drawable.progress_image)
                            .error(R.drawable.error)
                            .transform(new CircleTransform())
                            .into(signOutImage);
                    Toast.makeText(SignOutActivity.this, "wait when uploaded", Toast.LENGTH_SHORT).show();

                } else if (requestCode == Select_File) {
                    Uri uri = data.getData();
                    signOutImage.setImageURI(uri);
                    FirebaseStorage.getInstance().getReference()
                            .child("photos")
                            .child(uri.getLastPathSegment())
                            .putFile(uri)
                            .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        url = task.getResult().getDownloadUrl().toString();
                                        Toast.makeText(SignOutActivity.this, "Uploaded", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(SignOutActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                    Picasso.with(this)
                            .load(uri)
                            .placeholder(R.drawable.progress_image)
                            .error(R.drawable.error)
                            .transform(new CircleTransform())
                            .into(signOutImage);

                    Toast.makeText(SignOutActivity.this, "wait when uploaded", Toast.LENGTH_LONG).show();
                }
            }
        }

    @OnClick(R.id.register)
    public void signOut() {
        if (isValidData(firstNameET, lastNameET, signoutEmailET, signoutPasswordET)) {
        signUpProgressBar.setVisibility(View.VISIBLE);

            final String fName = firstNameET.getText().toString().trim();
            final String lName = lastNameET.getText().toString().trim();
            final String email = signoutEmailET.getText().toString().trim();
            String password = signoutPasswordET.getText().toString();

            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                User user = new User();
                                user.setEmail(email);
                                user.setfName(fName);
                                user.setlName(lName);
                                user.setUrl(url);
                                uploadUserInfo(user);
                            } else {
                                Toast.makeText(SignOutActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }

    }
    private void uploadUserInfo(final User user) {

        FirebaseDatabase.getInstance()
                .getReference().child("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())//id
                .setValue(user)//email , pass . first name . lastname
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        signUpProgressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {

                            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(SignOutActivity.this);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("url", user.getUrl());
                            editor.putString("name", user.getfName() + " " + user.getlName());
                            editor.apply();

                            startActivity(new Intent(SignOutActivity.this, SecondActivity.class));
                            finishAffinity();

                        } else {
                            Toast.makeText(SignOutActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }


}
