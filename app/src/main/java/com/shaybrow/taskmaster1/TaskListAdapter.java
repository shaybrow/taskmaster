package com.shaybrow.taskmaster1;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.amplifyframework.datastore.generated.model.Task;

import java.util.ArrayList;
import java.util.List;

import javax.security.auth.login.LoginException;

// Make sure type of viewholder conforms to This recyclerciew's viewholder as listed at the bottom
public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.TaskViewHolder> {

//  save the a
public static List<com.amplifyframework.datastore.generated.model.Task> taskList;

    public ClickOnTaskAble clickOnTaskAble;
    public TaskListAdapter (ClickOnTaskAble clickOnTaskAble, List<com.amplifyframework.datastore.generated.model.Task> taskList){
        this.clickOnTaskAble = clickOnTaskAble;
        this.taskList = taskList;
    }

    //    build and bind the data
        @NonNull
        @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        choose the fragment and render or 'inflate'

//        boiler plate code
        View fragment = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_task, parent, false);

//        pair the fragment with the data
//        attach frag to viewholder
        TaskViewHolder taskViewHolder = new TaskViewHolder(fragment);

        return taskViewHolder;
    }
//
    public static class TaskViewHolder extends RecyclerView.ViewHolder{
        //        public Task design = taskList.get();
        public Task task;

        public TaskViewHolder (@NonNull View itemView){
            super(itemView);


        }
    }
//  changes the data when fragment cycles
    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {

        ((TextView)holder.itemView.findViewById(R.id.fragTask)).setText(taskList.get(position).getTitle());
        holder.task = taskList.get(position);

        holder.itemView.setOnClickListener( v ->{
            Log.i("onClick", "Someone clicked on a task");
            clickOnTaskAble.handleClickOnTask(holder);

        });
    }

//    how many should be built
    @Override
    public int getItemCount() {
        return taskList.size();
    }


    public interface ClickOnTaskAble {
        public void handleClickOnTask (TaskViewHolder taskViewHolder);
    }
}
