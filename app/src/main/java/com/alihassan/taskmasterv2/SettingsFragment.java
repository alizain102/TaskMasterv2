package com.alihassan.taskmasterv2;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

public class SettingsFragment extends Fragment {

    private SharedPreferences sharedPreferences;
    private static final String THEME_PREF_KEY = "theme_pref";
    private Switch reminderSwitch;
    private static final String REMINDER_PREF_KEY = "reminder_pref";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);

        Button changeToDarkMode = rootView.findViewById(R.id.changeToDarkMode);
        Button changeToLightMode = rootView.findViewById(R.id.changeToLightMode);
        reminderSwitch = rootView.findViewById(R.id.reminderSwitch);

        sharedPreferences = requireContext().getSharedPreferences("MyPrefs", requireContext().MODE_PRIVATE);

        int currentTheme = sharedPreferences.getInt(THEME_PREF_KEY, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        if (currentTheme == AppCompatDelegate.MODE_NIGHT_YES) {
            changeToLightMode.setVisibility(View.VISIBLE);
            changeToDarkMode.setVisibility(View.GONE);
        } else {
            changeToLightMode.setVisibility(View.GONE);
            changeToDarkMode.setVisibility(View.VISIBLE);
        }

        changeToDarkMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleTheme();
            }
        });

        changeToLightMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleTheme();
            }
        });

        boolean isReminderEnabled = sharedPreferences.getBoolean(REMINDER_PREF_KEY, false);
        reminderSwitch.setChecked(isReminderEnabled);
        reminderSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                sharedPreferences.edit().putBoolean(REMINDER_PREF_KEY, isChecked).apply();
            }
        });

        return rootView;
    }

    private void toggleTheme() {
        int currentTheme = sharedPreferences.getInt(THEME_PREF_KEY, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        int newTheme = currentTheme == AppCompatDelegate.MODE_NIGHT_YES ?
                AppCompatDelegate.MODE_NIGHT_NO : AppCompatDelegate.MODE_NIGHT_YES;
        sharedPreferences.edit().putInt(THEME_PREF_KEY, newTheme).apply();
        AppCompatDelegate.setDefaultNightMode(newTheme);
    }
}
