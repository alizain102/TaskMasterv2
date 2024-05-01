package com.alihassan.taskmasterv2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class EditTaskFragment extends Fragment {

    private EditText titleEditText, descriptionEditText, dueDateEditText, priorityEditText;
    private Button saveButton;
    private Task selectedTask;

    public void setSelectedTask(Task task) {
        selectedTask = task;
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_edit_task, container, false);

        titleEditText = rootView.findViewById(R.id.titleEditText);
        descriptionEditText = rootView.findViewById(R.id.descriptionEditText);
        dueDateEditText = rootView.findViewById(R.id.dueDateEditText);
        priorityEditText = rootView.findViewById(R.id.priorityEditText);
        saveButton = rootView.findViewById(R.id.saveButton);

        if (selectedTask != null) {
            titleEditText.setText(selectedTask.getTitle());
            descriptionEditText.setText(selectedTask.getDescription());
            dueDateEditText.setText(selectedTask.getDueDate());
            priorityEditText.setText(selectedTask.getPriority());
        }

        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                saveTaskChanges();
            }
        });

        return rootView;
    }

    private void saveTaskChanges() {
        String newTitle = titleEditText.getText().toString().trim();
        String newDescription = descriptionEditText.getText().toString().trim();
        String newDueDate = dueDateEditText.getText().toString().trim();
        String newPriority = priorityEditText.getText().toString().trim();

        selectedTask.setTitle(newTitle);
        selectedTask.setDescription(newDescription);
        selectedTask.setDueDate(newDueDate);
        selectedTask.setPriority(newPriority);

        Toast.makeText(requireContext(), "Changes saved successfully", Toast.LENGTH_SHORT).show();

        TaskDetailFragment taskDetailFragment = new TaskDetailFragment();
        taskDetailFragment.setSelectedTask(selectedTask);
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, taskDetailFragment)
                .addToBackStack(null)
                .commit();
    }
}