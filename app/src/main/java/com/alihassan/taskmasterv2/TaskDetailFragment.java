package com.alihassan.taskmasterv2;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class TaskDetailFragment extends Fragment {

    private TextView titleTextView, descriptionTextView, dueDateTextView, priorityTextView, statusTextView;
    private Button markCompletedButton, editButton, deleteButton, setReminderButton;
    private Task selectedTask;
    private boolean reminderEnabled = false;

    public void setSelectedTask(Task task) {
        selectedTask = task;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_task_detail, container, false);

        titleTextView = rootView.findViewById(R.id.titleTextView);
        descriptionTextView = rootView.findViewById(R.id.descriptionTextView);
        dueDateTextView = rootView.findViewById(R.id.dueDateTextView);
        priorityTextView = rootView.findViewById(R.id.priorityTextView);
        statusTextView = rootView.findViewById(R.id.statusTextView);
        markCompletedButton = rootView.findViewById(R.id.markCompletedButton);
        editButton = rootView.findViewById(R.id.editButton);
        deleteButton = rootView.findViewById(R.id.deleteButton);
        setReminderButton = rootView.findViewById(R.id.setReminderButton);

        // Display task details
        if (selectedTask != null) {
            titleTextView.setText(selectedTask.getTitle());
            descriptionTextView.setText(selectedTask.getDescription());
            dueDateTextView.setText(selectedTask.getDueDate());
            priorityTextView.setText(selectedTask.getPriority());
            statusTextView.setText(selectedTask.isStatus() ? "Completed" : "Incomplete");

            if (!selectedTask.isStatus()) {
                markCompletedButton.setVisibility(View.VISIBLE);
                markCompletedButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Update task status to completed
                        selectedTask.setStatus(true);
                        statusTextView.setText("Completed");
                        markCompletedButton.setVisibility(View.GONE);
                    }
                });
            } else {
                markCompletedButton.setVisibility(View.GONE);
            }

            editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Navigate to edit task fragment
                    openEditTaskFragment(selectedTask);
                }
            });

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDeleteConfirmationDialog();
                }
            });

            setReminderButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showSetReminderConfirmation();
                }
            });
        }

        return rootView;
    }

    private void openEditTaskFragment(Task task) {
        EditTaskFragment editTaskFragment = new EditTaskFragment();
        editTaskFragment.setSelectedTask(task);
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, editTaskFragment)
                .addToBackStack(null)
                .commit();
    }

    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Confirm Deletion");
        builder.setMessage("Are you sure you want to delete this task?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Delete the task
                deleteTask(selectedTask);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void deleteTask(Task task) {
        TaskManager taskManager = TaskManager.getInstance(new AuthManager(requireContext()));
        taskManager.deleteTask(task);
        Toast.makeText(requireContext(), "Task deleted successfully", Toast.LENGTH_SHORT).show();

        TaskListFragment taskListFragment = new TaskListFragment();
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, taskListFragment)
                .addToBackStack(null)
                .commit();
    }

    private void showSetReminderConfirmation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Set Reminder");
        builder.setMessage("Do you want to set a reminder for this task?");
        builder.setPositiveButton("Set Reminder", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                toggleReminder();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void toggleReminder() {
        reminderEnabled = !reminderEnabled;
        if (reminderEnabled) {
            showReminderTimePicker();
        } else {
            cancelReminder();
        }
    }

    private void showReminderTimePicker() {

    }

    private void setReminder(long reminderTimeMillis) {
        // Set a reminder for the task
        Intent intent = new Intent(getContext(), ReminderService.class);
        intent.putExtra("taskTitle", selectedTask.getTitle());

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                getContext(),
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE // Add FLAG_IMMUTABLE flag
        );

        AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.set(AlarmManager.RTC_WAKEUP, reminderTimeMillis, pendingIntent);
        }
    }

    private void cancelReminder() {
        // Cancel the reminder for the task
        Intent intent = new Intent(getContext(), ReminderService.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), 0, intent, PendingIntent.FLAG_NO_CREATE | PendingIntent.FLAG_IMMUTABLE);
        if (pendingIntent != null) {
            AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
            alarmManager.cancel(pendingIntent);
            pendingIntent.cancel();
        }
    }
}