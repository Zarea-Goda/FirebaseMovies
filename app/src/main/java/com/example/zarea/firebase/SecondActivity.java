package com.example.zarea.firebase;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeInfoDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.interfaces.Closure;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SecondActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.text)
    TextView text;
    @BindView(R.id.img)
    ImageView img;
    @BindView(R.id.TopRelated)
    FloatingActionButton TopRelated;
    @BindView(R.id.Popular)
    FloatingActionButton Popular;
    @BindView(R.id.andApi)
    FloatingActionButton andApi;
    @BindView(R.id.poster_RV)
    RecyclerView posterRV;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.action_text)
    TextView actionText;
    private String uri;
    private String name;
    RecyclerView.LayoutManager zLayoutManger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        ButterKnife.bind(this);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        uri = preferences.getString("url", "");
        name = preferences.getString("name", "");

        Picasso.with(this)
                .load(uri)
                .placeholder(R.drawable.progress_image)
                .error(R.drawable.error)
                .transform(new CircleTransform())
                .into(img);
        text.setText(name);

        Popular.setOnClickListener(this);
        TopRelated.setOnClickListener(this);
        andApi.setOnClickListener(this);

        // mLayoutManger = new GridLayoutManager(this, 2);
        zLayoutManger = new LinearLayoutManager(this);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.Popular:
                final RecyclerView.LayoutManager mLayoutManger;
                mLayoutManger = new GridLayoutManager(this, 2);
                actionText.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                AndroidNetworking.get("https://api.themoviedb.org/3/movie/popular")
                        .addQueryParameter("api_key", "0f167dcd1f0e20a46b7d6d95658fc544")
                        .build()
                        .getAsObject(Movie.class, new ParsedRequestListener() {
                            @Override
                            public void onResponse(Object response) {
                                progressBar.setVisibility(View.GONE);
                                posterRV.setLayoutManager(mLayoutManger);
                                RecyclerAdapter adapter = new RecyclerAdapter((Movie) response, SecondActivity.this);
                                posterRV.setAdapter(adapter);
                            }

                            @Override
                            public void onError(ANError anError) {
                            }
                        });

                break;

            case R.id.TopRelated:
                final RecyclerView.LayoutManager mLayoutManger0;
                mLayoutManger0 = new GridLayoutManager(this, 2);
                actionText.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                AndroidNetworking.get("https://api.themoviedb.org/3/movie/top_rated")
                        .addQueryParameter("api_key", "0f167dcd1f0e20a46b7d6d95658fc544")
                        .build()
                        .getAsObject(Movie.class, new ParsedRequestListener() {
                            @Override
                            public void onResponse(Object response) {
                                progressBar.setVisibility(View.GONE);
                                posterRV.setLayoutManager(mLayoutManger0);
                                RecyclerAdapter adapter = new RecyclerAdapter((Movie) response, SecondActivity.this);
                                posterRV.setAdapter(adapter);
                            }

                            @Override
                            public void onError(ANError anError) {
                                Toast.makeText(SecondActivity.this, anError.getErrorDetail(), Toast.LENGTH_LONG).show();
                            }
                        });
                break;

            case R.id.andApi:
//                final RecyclerView.LayoutManager mLayoutManagerz;
//                mLayoutManagerz = new LinearLayoutManager(this);
                actionText.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                AndroidNetworking.put("http://androidblog.esy.es/ImageJsonData.php")
                        .build()
                        .getAsObjectList(Mobile.class, new ParsedRequestListener<List<Mobile>>() {
                            @Override
                            public void onResponse(List<Mobile> response) {
                                progressBar.setVisibility(View.GONE);
                                MobileAdapter adapter = new MobileAdapter(SecondActivity.this, response);
                                posterRV.setLayoutManager(zLayoutManger);
                                posterRV.setAdapter(adapter);

                            }

                            @Override
                            public void onError(ANError anError) {

                            }
                        });
                break;

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_sign_out, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                new AwesomeInfoDialog(this)
                        .setTitle("log out...!")
                        .setMessage("are you sure...?!!!")
                        .setColoredCircle(R.color.dialogErrorBackgroundColor)
                        .setDialogIconAndColor(R.drawable.ic_dialog_info, R.color.white)
                        .setCancelable(true)
                        .setPositiveButtonText(getString(R.string.dialog_yes_button))
                        .setPositiveButtonbackgroundColor(R.color.dialogInfoBackgroundColor)
                        .setPositiveButtonTextColor(R.color.white)
                        .setNegativeButtonText(getString(R.string.dialog_no_button))
                        .setNegativeButtonbackgroundColor(R.color.dialogErrorBackgroundColor)
                        .setNegativeButtonTextColor(R.color.white)
                        .setPositiveButtonClick(new Closure() {
                            @Override
                            public void exec() {
                                FirebaseAuth.getInstance().signOut();
                                startActivity(new Intent(SecondActivity.this, MainActivity.class));
                                finish();
                            }

                        })
                        .setNegativeButtonClick(new Closure() {
                            @Override
                            public void exec() {
                                Toast.makeText(SecondActivity.this, "stay here...", Toast.LENGTH_LONG).show();
                            }
                        })
                        .show();

                return true;
        }

        return false;
    }
}
