package com.shaybrow.taskmaster1.models;

public class Task {
    String title;
    String body;

    private TaskState state = new NewState();

    public Task (){}

    public void previousState(){
        state.prev(this);
    }
    public void nextState(){
        state.next(this);
    }

    public interface TaskState{
        void next(Task task);
        void prev(Task task);

    }

    public class NewState implements TaskState{


        @Override
        public void next(Task task) {

        }

        @Override
        public void prev(Task task) {

        }
    }

}
