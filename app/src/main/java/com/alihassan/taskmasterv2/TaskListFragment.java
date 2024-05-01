package com.alihassan.taskmasterv2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import java.util.List;

public class TaskListFragment extends Fragment {

    private ListView taskListView;
    private ArrayAdapter<Task> taskAdapter;
    private List<Task> tasks;
    private AuthManager authManager;
    private TextView noTasksTextView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_task_list, container, false);

        taskListView = rootView.findViewById(R.id.taskListView);
        noTasksTextView = rootView.findViewById(R.id.noTasksTextView);

        authManager = new AuthManager(requireContext());

        TaskManager taskManager = TaskManager.getInstance(authManager);

        tasks = taskManager.getTasksForCurrentUser();

        taskAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, tasks);
        taskListView.setAdapter(taskAdapter);

        // Check if tasks list is empty
        if (tasks.isEmpty()) {
            // Show "No Tasks Found" message
            noTasksTextView.setVisibility(View.VISIBLE);
            taskListView.setVisibility(View.GONE);
        } else {
            // Hide "No Tasks Found" message
            noTasksTextView.setVisibility(View.GONE);
            taskListView.setVisibility(View.VISIBLE);

            taskListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Task selectedTask = tasks.get(position);
                    openTaskDetailFragment(selectedTask);
                }
            });
        }

        return rootView;
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