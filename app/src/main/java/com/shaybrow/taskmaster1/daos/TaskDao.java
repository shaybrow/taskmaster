package com.shaybrow.taskmaster1.daos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.shaybrow.taskmaster1.models.Task;

import java.util.List;

@Dao
public interface TaskDao {

    @Insert
    public void insert (Task task);

    @Query("SELECT * FROM Task")
    public List<Task> findAll();



    @Query("SELECT * FROM Task WHERE Title LIKE :title LIMIT 1")
    public Task findByTitle(String title);

//    @Query("SELECT * FROM Task")
//    public List<Task> findAll();


}
