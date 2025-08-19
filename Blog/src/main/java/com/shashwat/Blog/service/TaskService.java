package com.shashwat.Blog.service;


import com.shashwat.Blog.model.Task;
import com.shashwat.Blog.model.User;
import com.shashwat.Blog.repo.TaskRepo;
import com.shashwat.Blog.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {
    @Autowired
    private TaskRepo taskRepo;

    @Autowired
    private UserRepo userRepo;
    public ResponseEntity<Task> addTaskForUser(int userId, Task task) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Set relationship
        task.setUser(user);
       Task task1= taskRepo.save(task);

        return new ResponseEntity<>(task1,HttpStatus.CREATED);
    }

    public ResponseEntity<List<Task>> getTasksForUser(int userId) {
        return new ResponseEntity<>(taskRepo.findTasksByUser_Id(userId),HttpStatus.OK);
    }

    public void deleteTaskById(int taskId) {
        if (!taskRepo.existsById(taskId)) {
            throw new RuntimeException("Task not found");
        }
        taskRepo.deleteById(taskId);
    }

    public Task getTaskById(int taskId) {
        return taskRepo.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));
    }

    public void updateStatus(int taskId, String newStatus) {
        Task task = taskRepo.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        task.setState(newStatus);
        taskRepo.save(task);
    }


}
