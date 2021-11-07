package br.edu.ifsp.arq.dmos5_2021s1.minhastarefas.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Task {

    private String name;
    private String description;
    private byte priority;
    private String dueDate;
    private boolean done;

    public Task(String name, String description, byte priority, String dueDate, boolean done) {
        this.setName(name);
        this.setDescription(description);
        this.setPriority(priority);
        this.setDueDate(dueDate);
        this.setDone(done);
    }

    @Override
    public boolean equals(Object task) {
        boolean isEquals = false;
        if(task != null) {
            if(this.getName().equalsIgnoreCase(((Task) task).getName()) &&
                this.getPriority() == ((Task) task).getPriority() &&
                this.getDueDate().equals(((Task) task).getDueDate()) &&
                this.getDescription().equalsIgnoreCase(((Task) task).getDescription())) {
                isEquals = true;
            }
        }
        return isEquals;
    }

    @Override
    public String toString() {
        return String.format("\n Name: %s | Description : %s | Priority : %d | Due date: %s | Done : %s", getName(), getDescription(), getPriority(), getDueDate(), isDone());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte getPriority() {
        return priority;
    }

    public void setPriority(byte priority) {
        if(priority == 1 || priority == 2 || priority == 3 ) {
            this.priority = priority;
        }
        else {
            this.priority = 0;
        }
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        if(dueDate != null && !dueDate.isEmpty()) {
            this.dueDate = dueDate;
        }
        else {
            this.dueDate = getCurrentDate();
        }
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    private String getCurrentDate() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }
}
