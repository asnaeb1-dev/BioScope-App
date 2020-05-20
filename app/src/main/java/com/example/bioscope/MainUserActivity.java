package com.example.bioscope;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.bioscope.API.AdminRoutes;
import com.example.bioscope.API.UserRoutes;
import com.example.bioscope.POJO.LogoutUser;
import com.example.bioscope.Utility.Config;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainUserActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout drawer;
    private NavigationView navigationView;
    private ProgressBar mainScreenPB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_user);
        Toolbar toolbar = findViewById(R.id.toolbar);
        mainScreenPB = findViewById(R.id.mainScreenPB);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_user, menu);
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        switch (id){
            case R.id.nav_gallery:
                Toast.makeText(this, "Gallery", Toast.LENGTH_SHORT).show();
                break;

            case R.id.nav_home:
                Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show();
                break;

            case R.id.nav_slideshow:
                Toast.makeText(this, "Slideshow", Toast.LENGTH_SHORT).show();
                break;

            case R.id.nav_logout:
                new AlertDialog.Builder(MainUserActivity.this)
                        .setTitle("Logout user")
                        .setMessage("Are you sure you want to logout?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mainScreenPB.setVisibility(View.VISIBLE);
                                logoutUser();
                            }
                        }).setNegativeButton("No", null).show();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logoutUser(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.getBaseUrl())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        UserRoutes user = retrofit.create(UserRoutes.class);
        Call<LogoutUser> call = user.logoutUser(getSharedPreferences("MY_PREFS", MODE_PRIVATE).getString("USER_TOKEN", null));
        call.enqueue(new Callback<LogoutUser>() {
            @Override
            public void onResponse(Call<LogoutUser> call, Response<LogoutUser> response) {
                if(response.isSuccessful()){
                    assert response.body()!=null;
                    if(response.body().getMessage().equals("logged_out")){
                        SharedPreferences sharedPreferences = getSharedPreferences("MY_PREFS", MODE_PRIVATE);
                        SharedPreferences.Editor sf = sharedPreferences.edit();
                        sf.remove("USER_TOKEN");
                        sf.apply();
                        mainScreenPB.setVisibility(View.GONE);
                        startActivity(new Intent(MainUserActivity.this, MainActivity.class));
                        finish();
                    }
                }
            }

            @Override
            public void onFailure(Call<LogoutUser> call, Throwable t) {
                mainScreenPB.setVisibility(View.GONE);
                Log.e("ERROR", t.getMessage());
            }
        });

    }

}
