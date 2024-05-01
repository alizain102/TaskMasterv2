package com.alihassan.taskmasterv2;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TaskManager {

    private static TaskManager instance;
    private Map<String, List<Task>> userTasksMap;
    private AuthManager authManager;

    private TaskManager(AuthManager authManager) {
        userTasksMap = new HashMap<>();
        this.authManager = authManager;
    }

    public static TaskManager getInstance(AuthManager authManager) {
        if (instance == null) {
            instance = new TaskManager(authManager);
        }
        return instance;
    }

    public void addTask(Task task) {
        String currentUser = authManager.getCurrentUser();
        if (!userTasksMap.containsKey(currentUser)) {
            userTasksMap.put(currentUser, new ArrayList<>());
        }
        userTasksMap.get(currentUser).add(task);
    }

    public List<Task> getTasksForCurrentUser() {
        String currentUser = authManager.getCurrentUser();
        return userTasksMap.getOrDefault(currentUser, new ArrayList<>());
    }

    public List<Task> sortTasksByPriority(List<Task> tasks) {
        return tasks.stream()
                .sorted(Comparator.comparing(Task::getPriority))
                .collect(Collectors.toList());
    }
    public List<Task> sortTasksByDueDate(List<Task> tasks) {
        return tasks.stream()
                .sorted(Comparator.comparing(Task::getDueDate))
                .collect(Collectors.toList());
    }
    public List<Task> filterTasksByCompletion(List<Task> tasks, boolean completed) {
        return tasks.stream()
                .filter(task -> task.isStatus() == completed)
                .collect(Collectors.toList());
    }

    public void deleteTask(Task task) {
        String currentUser = authManager.getCurrentUser();
        List<Task> userTasks = userTasksMap.get(currentUser);
        if (userTasks != null) {
            userTasks.remove(task);
        }
    }

    public void updateTask(Task task) {
        String currentUser = authManager.getCurrentUser();
        List<Task> userTasks = userTasksMap.get(currentUser);
        if (userTasks != null && userTasks.contains(task)) {
            int index = userTasks.indexOf(task);
            userTasks.set(index, task);
        }
    }

}