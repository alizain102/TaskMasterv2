package com.alihassan.taskmasterv2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import java.util.List;

public class PriorityTaskListFragment extends Fragment {

    private Spinner filterSpinner;
    private ListView taskListView;
    private ArrayAdapter<Task> taskAdapter;
    private TaskManager taskManager;
    private TextView noTasksTextView; // TextView to display "No Tasks Found" message

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_priority_task_list, container, false);

        filterSpinner = rootView.findViewById(R.id.filterSpinner);
        taskListView = rootView.findViewById(R.id.taskListView);
        noTasksTextView = rootView.findViewById(R.id.noTasksTextView); // Initialize TextView
        taskAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1);
        taskListView.setAdapter(taskAdapter);

        taskManager = TaskManager.getInstance(new AuthManager(requireContext()));

        ArrayAdapter<CharSequence> filterAdapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.filter_options, android.R.layout.simple_spinner_item);
        filterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        filterSpinner.setAdapter(filterAdapter);
        filterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateTaskList(position);
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        taskListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Task selectedTask = taskAdapter.getItem(position);
                openTaskDetailFragment(selectedTask);
            }
        });

        return rootView;
    }

    private void updateTaskList(int position) {
        List<Task> tasks = taskManager.getTasksForCurrentUser();

        switch (position) {
            case 0: // Priority Level
                tasks = taskManager.sortTasksByPriority(tasks);
                break;
            case 1: // Due Date
                tasks = taskManager.sortTasksByDueDate(tasks);
                break;
            case 2: // Completion Status
                tasks = taskManager.filterTasksByCompletion(tasks, true);
                break;
            default:
                break;
        }

        taskAdapter.clear();
        taskAdapter.addAll(tasks);

        if (tasks.isEmpty()) {
            noTasksTextView.setVisibility(View.VISIBLE);
            taskListView.setVisibility(View.GONE);
        } else {
            noTasksTextView.setVisibility(View.GONE);
            taskListView.setVisibility(View.VISIBLE);
        }
    }

    private void openTaskDetailFragment(Task selectedTask) {
        TaskDetailFragment taskDetailFragment = new TaskDetailFragment();
        taskDetailFragment.setSelectedTask(selectedTask);
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, taskDetailFragment)
                .addToBackStack(null)
                .commit();
    }
}