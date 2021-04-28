package com.shaybrow.taskmaster1.models;

public class Task {
    String title;
    String body;
    String state;

    public Task(String title, String body) {
        this.title = title;
        this.body = body;
        this.state = "new";
    }

    public Task(String title) {
        this.title = title;
        this.state = "new";
    }

    public String getState() {
        return state;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Task (){
        this.state = "new";
    }

    public void assignTask () {
        this.state = "assigned";

    }
    public void inProgress () {
        this.state = "in progress";
    }

    public void completeTask (){
        this.state = "complete";
    }
}
