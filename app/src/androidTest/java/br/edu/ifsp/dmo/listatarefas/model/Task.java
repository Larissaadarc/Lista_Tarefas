package br.edu.ifsp.dmo.listatarefas.model;
import androidx.annotation.Nullable;
public class Task {
    private String title;
    private String description;
    public Task(String title, String description) {
        this.title = title;
        this.description = description;
    }
    public String getTitle(){
        return title;
    }
    public void setTitle(String title){
        this.title = title;
    }
    public String getDescription(){
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof Task) {
            Task other = (Task) obj;
            if (title == other.title && description == other.description) {
                return true;
            }
        }
        return false;
    }
}

