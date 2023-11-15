package br.edu.ifsp.dmo.listatarefas.model;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;


public class User implements Serializable {

    private final String name;
    private final int password;
    private final List<Task> taskList;

    public User(String name, int password) {
        this.name = name;
        this.password = password;
        this.taskList = new LinkedList<>();
    }

    public String getName() {
        return name;
    }

    public int getPassword() {
        return password;
    }

    public List<Task> getTaskList() {
        return taskList;
    }

    public boolean loginTest(int password){
        return this.password == password;
    }

    public void addTask(Task task){
        if (task != null){
            taskList.add(task);
        }
    }
    public boolean removeTask(int position){
        return taskList.remove(position) != null;
    }

    public boolean removeTask(Task task){
        return taskList.remove(task);
    }

    public Task getTask(int position){
        return taskList.get(position);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return name.equals(user.name);
    }

}
