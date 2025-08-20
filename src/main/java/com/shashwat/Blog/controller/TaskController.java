package com.shashwat.Blog.controller;

import com.shashwat.Blog.model.Task;
import com.shashwat.Blog.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class TaskController {


    @Autowired
    public TaskService taskService;

    @PostMapping("/user/{userId}/addTask")
    public ResponseEntity<Task> addTask(@PathVariable int userId, @RequestBody Task task) {
       return taskService.addTaskForUser(userId, task);

    }

    @GetMapping("/user/{userId}/getAllTasks")
    public ResponseEntity<List<Task>> getAllTasksForUser(@PathVariable int userId) {
        return taskService.getTasksForUser(userId);
    }

    @DeleteMapping("/tasks/{taskId}")
    public ResponseEntity<String> deleteTask(@PathVariable int taskId) {
        taskService.deleteTaskById(taskId);
        return ResponseEntity.ok("Task deleted successfully");
    }
    @GetMapping("/tasks/{taskId}")
    public ResponseEntity<Task> getTaskById(@PathVariable int taskId) {
        Task task = taskService.getTaskById(taskId);
        return ResponseEntity.ok(task);
    }

    @PatchMapping("/tasks/{taskId}/status")
    public ResponseEntity<String> updateTaskStatus(
            @PathVariable int taskId,
            @RequestBody Map<String, String> body) {

        String newStatus = body.get("newStatus");
        System.out.println(newStatus);
        if (newStatus == null || newStatus.isEmpty()) {
            return ResponseEntity.badRequest().body("Missing newStatus in request body");
        }

        taskService.updateStatus(taskId, newStatus);
        System.out.println("here");
        return ResponseEntity.ok("Status updated successfully");
    }

}
