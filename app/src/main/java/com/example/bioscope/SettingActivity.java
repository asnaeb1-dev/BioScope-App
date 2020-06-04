package com.example.bioscope;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.example.bioscope.DialogBox.GenreDialog;
import com.example.bioscope.DialogBox.RemoveAccountDialog;
import com.gc.materialdesign.widgets.ColorSelector;

public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings_layout_linear, new SettingsFragment())
                .commit();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {

        private Preference colorChanger, profilePictureChanger, genreSelector, deleteAccount;

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);

            colorChanger = (Preference) findPreference("colorChanger");
            profilePictureChanger = (Preference) findPreference("profilePicture");
            genreSelector = findPreference("genre");
            deleteAccount = findPreference("deletepermanent");

            startFunctioning();
        }

        private void startFunctioning(){

            colorChanger.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    new ColorSelector(((Activity)getContext()), getResources().getColor(R.color.primary), new ColorSelector.OnColorSelectedListener() {
                        @Override
                        public void onColorSelected(int color) {
                            Toast.makeText(getContext(), String.valueOf(color), Toast.LENGTH_SHORT).show();
                        }
                    }) .show();
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
        }

    }
}