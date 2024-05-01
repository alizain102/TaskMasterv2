package com.alihassan.taskmasterv2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import java.util.ArrayList;
import java.util.List;

public class TaskCreationFragment extends Fragment {

    private EditText titleEditText, descriptionEditText, dueDateEditText, notesEditText;
    private Spinner prioritySpinner;
    private Button createTaskButton;
    private AuthManager authManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_task_creation, container, false);

        titleEditText = rootView.findViewById(R.id.titleEditText);
        descriptionEditText = rootView.findViewById(R.id.descriptionEditText);
        dueDateEditText = rootView.findViewById(R.id.dueDateEditText);
        notesEditText = rootView.findViewById(R.id.notesEditText);
        prioritySpinner = rootView.findViewById(R.id.prioritySpinner);
        createTaskButton = rootView.findViewById(R.id.createTaskButton);
        authManager = new AuthManager(requireContext());

        List<String> priorities = new ArrayList<>();
        priorities.add("High");
        priorities.add("Medium");
        priorities.add("Low");
        ArrayAdapter<String> priorityAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, priorities);
        priorityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        prioritySpinner.setAdapter(priorityAdapter);

        createTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createTask();
            }
        });

        return rootView;
    }

    private void createTask() {
        String title = titleEditText.getText().toString().trim();
        String description = descriptionEditText.getText().toString().trim();
        String dueDate = dueDateEditText.getText().toString().trim();
        String notes = notesEditText.getText().toString().trim();
        String priority = prioritySpinner.getSelectedItem().toString();
        String currentUser = authManager.getCurrentUser();

        if (title.isEmpty() || description.isEmpty() || dueDate.isEmpty() || notes.isEmpty()) {
            Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        AuthManager authManager = new AuthManager(requireContext());

        Task task = new Task(title, description, dueDate, notes, priority, currentUser, false);
        TaskManager taskManager = TaskManager.getInstance(authManager);
        taskManager.addTask(task);

        titleEditText.setText("");
        descriptionEditText.setText("");
        dueDateEditText.setText("");
        notesEditText.setText("");

        Toast.makeText(requireContext(), "Task created successfully", Toast.LENGTH_SHORT).show();
    }
}