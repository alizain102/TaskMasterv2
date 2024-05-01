package com.alihassan.taskmasterv2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class HomeFragment extends Fragment {

    private Button addTaskButton, viewTasksButton, viewPTasksButton, signOutButton, settingsButton;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        addTaskButton = rootView.findViewById(R.id.addTaskButton);
        viewTasksButton = rootView.findViewById(R.id.viewTasksButton);
        viewPTasksButton = rootView.findViewById(R.id.viewPTasksButton);
        signOutButton = rootView.findViewById(R.id.logout);
        settingsButton = rootView.findViewById(R.id.settingsButton);

        addTaskButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ((MainActivity) requireActivity()).replaceFragment(new TaskCreationFragment(), true);
            }
        });

        viewTasksButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ((MainActivity) requireActivity()).replaceFragment(new TaskListFragment(), true);
            }
        });

        viewPTasksButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ((MainActivity) requireActivity()).replaceFragment(new PriorityTaskListFragment(), true);
            }
        });

        signOutButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                signOut();
            }
        });

        settingsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openSettingsFragment();
            }
        });

        return rootView;
    }

    private void signOut() {
        // Implement sign-out logic here
        AuthManager authManager = new AuthManager(requireContext());
        authManager.logOut();

        ((MainActivity) requireActivity()).replaceFragment(new LoginFragment(), false);
    }

    private void openSettingsFragment() {
        ((MainActivity) requireActivity()).replaceFragment(new SettingsFragment(), true);
    }
}