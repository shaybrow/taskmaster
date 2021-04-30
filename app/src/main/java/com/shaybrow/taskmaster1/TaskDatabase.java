package com.shaybrow.taskmaster1;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.shaybrow.taskmaster1.daos.TaskDao;
import com.shaybrow.taskmaster1.models.Task;
//update version to destroy and create new DB
@Database(entities = {Task.class}, version = 1)
public abstract class TaskDatabase extends RoomDatabase {
    public abstract TaskDao taskDao();
}
