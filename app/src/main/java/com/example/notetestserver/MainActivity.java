package com.example.notetestserver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import app.app;
import app.spref;
import fragments.NoteFragment;
import fragments.ProfileFragment;
import fragments.SettingsFragment;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    BottomNavigationView bottomnavigation;
    NoteFragment noteFragment = new NoteFragment();
    ProfileFragment profileFragment = new ProfileFragment();
    SettingsFragment settingsFragment = new SettingsFragment();

    public static Typeface font;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.profile: {
                openFragment(profileFragment);
                break;
            }
            case R.id.note: {
                openFragment(noteFragment);
                break;
            }
            case R.id.setting: {
                openFragment(settingsFragment);
                break;
            }
        }
        return true;
    }

    private void init() {
        bottomnavigation = findViewById(R.id.bottomnavigation);
        bottomnavigation.setOnNavigationItemSelectedListener(this);

        bottomnavigation.setSelectedItemId(R.id.note);
        openFragment(noteFragment);
        try {
            font = Typeface.createFromAsset(getAssets(), "font/" + spref.get().getString("font", "roboto.ttf"));
            app.l(spref.get().getString("font", "khande.ttf"));
        } catch (Exception e) {

        }
    }


    private void openFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.cl, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        if (bottomnavigation.getSelectedItemId() != R.id.note) {
            bottomnavigation.setSelectedItemId(R.id.note);
        } else super.onBackPressed();

    }

}