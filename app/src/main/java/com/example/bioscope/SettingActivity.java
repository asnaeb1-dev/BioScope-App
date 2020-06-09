package com.example.bioscope;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.example.bioscope.DialogBox.ChangePasswordDialog;
import com.example.bioscope.DialogBox.ColorSelectorDialog;
import com.example.bioscope.DialogBox.GenreDialog;
import com.example.bioscope.DialogBox.RemoveAccountDialog;
import com.gc.materialdesign.widgets.ColorSelector;

public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        ImageView imageView = findViewById(R.id.settings_backButton);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        getSupportFragmentManager().beginTransaction().replace(R.id.settings_layout_linear, new SettingsFragment()).commit();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {

        private Preference genreSelector, deleteAccount, aboutDevs, security;

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);

            genreSelector = findPreference("genre");
            deleteAccount = findPreference("deletepermanent");
            aboutDevs = findPreference("about_developers");
            security = findPreference("security");

            startFunctioning();
        }

        private void startFunctioning(){

            aboutDevs.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    startActivity(new Intent(getContext(), AboutActivity.class));
                    return true;
                }
            });


            //display the genre change dialog so that user can change his genre specs again
            genreSelector.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    DisplayMetrics displayMetrics = new DisplayMetrics();
                    ((Activity)getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                    GenreDialog gd = new GenreDialog(getContext());
                    gd.generateGenreDialog(displayMetrics.widthPixels,displayMetrics.heightPixels);
                    return true;
                }
            });

            deleteAccount.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    RemoveAccountDialog removeAccount = new RemoveAccountDialog(getContext());
                    removeAccount.generateConfirmationDialog();
                    return true;
                }
            });

            security.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    new ChangePasswordDialog(getContext()).showChangePasswordDialog();
                    return false;
                }
            });
        }
    }
}