package com.shashwat.Blog.repo;

import com.shashwat.Blog.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepo extends JpaRepository<Task,Integer> {

     List<Task> findTasksByUser_Id(int userId);

}
