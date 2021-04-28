package com.shaybrow.taskmaster1;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import javax.security.auth.login.LoginException;

// Make sure type of viewholder conforms to This recyclerciew's viewholder as listed at the bottom
public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.TaskViewHolder> {

//  save the a


    public ClickOnTaskAble clickOnTaskAble;
    public TaskListAdapter (ClickOnTaskAble clickOnTaskAble){
        this.clickOnTaskAble = clickOnTaskAble;
    }

    public static List<String> tasks;

    static {
        tasks =new ArrayList<>();
        tasks.add("Sleep");
        tasks.add("potato");
        tasks.add("Sleep");
        tasks.add("potato");
        tasks.add("Sleep");
        tasks.add("potato");
        tasks.add("Sleep");
        tasks.add("potato");
        tasks.add("Sleep");
        tasks.add("potato");
        tasks.add("Sleep");
        tasks.add("potato");
        tasks.add("Sleep");
        tasks.add("potato");
        tasks.add("Sleep");
        tasks.add("potato");
        tasks.add("Sleep");
        tasks.add("potato");
        tasks.add("Sleep");
        tasks.add("potato");


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

//  changes the data when fragment cycles
    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        String thisTask = tasks.get(position);
//      this will be needed for clicking
        holder.design = thisTask;
        ((TextView)holder.itemView.findViewById(R.id.fragTask)).setText(thisTask);

        holder.itemView.setOnClickListener( v ->{
            Log.i("onClick", "Someone clicked on a task");
            clickOnTaskAble.handleClickOnTask(holder);

        });
    }

//    how many should be built
    @Override
    public int getItemCount() {
        return tasks.size();
    }
    public static class TaskViewHolder extends RecyclerView.ViewHolder{
        public String design;
        public TaskViewHolder (@NonNull View itemView){
            super(itemView);


        }
    }
    public interface ClickOnTaskAble {
        public void handleClickOnTask (TaskViewHolder taskViewHolder);
    }
}
