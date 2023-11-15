package br.edu.ifsp.dmo.listatarefas.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.edu.ifsp.dmo.listatarefas.R;
import br.edu.ifsp.dmo.listatarefas.model.Task;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {

    private Context context;
    private List<Task> dataset;
    private TaskItemClickListener listener;

    public TaskAdapter(Context context, List<Task> dataset, TaskItemClickListener listener) {
        this.context = context;
        this.dataset = dataset;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TaskAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskAdapter.ViewHolder holder, int position) {
        Task t = dataset.get(position);
        holder.titleTextView.setText(t.getTitle());
        holder.itemView.setOnClickListener(
                view -> listener.clickTaskItem(position)
        );
        holder.doneImageView.setOnClickListener(
                view -> listener.clickTaskDone(position)
        );
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView doneImageView;
        public TextView titleTextView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            doneImageView = itemView.findViewById(R.id.image_task_item);
            titleTextView = itemView.findViewById(R.id.textview_task_item);
        }
    }
}
