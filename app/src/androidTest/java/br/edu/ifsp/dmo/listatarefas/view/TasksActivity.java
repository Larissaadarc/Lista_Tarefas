package br.edu.ifsp.dmo.listatarefas.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import br.edu.ifsp.dmo.listatarefas.Constants;
import br.edu.ifsp.dmo.listatarefas.R;
import br.edu.ifsp.dmo.listatarefas.dao.TaskDao;
import br.edu.ifsp.dmo.listatarefas.model.Task;
import br.edu.ifsp.dmo.listatarefas.model.User;
import br.edu.ifsp.dmo.listatarefas.view.adapter.TaskAdapter;
import br.edu.ifsp.dmo.listatarefas.view.adapter.TaskItemClickListener;

public class TasksActivity extends AppCompatActivity implements TaskItemClickListener {

    private User user;
    private TaskDao dao;
    private FloatingActionButton button;
    private RecyclerView recyclerView;
    private ActivityResultLauncher<Intent> launcherNew;
    private ActivityResultLauncher<Intent> launcherUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);

        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra(Constants.KEY_USER);
        dao = new TaskDao(this, user);

        ActionBar bar = getSupportActionBar();
        if (bar != null){
            bar.setDisplayHomeAsUpEnabled(true);
        }

        launcherNew = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult o) {
                        if (o.getResultCode() == RESULT_OK){
                            String title = o.getData().getStringExtra(Constants.ATTR_TASK_TITLE);
                            String desc = o.getData().getStringExtra(Constants.ATTR_TASK_DESCRIPTION);
                            saveTask(title, desc);
                        }
                    }
                }
        );

        launcherUpdate = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult o) {
                        if (o.getResultCode() == RESULT_OK){
                            String title = o.getData().getStringExtra(Constants.ATTR_TASK_TITLE);
                            String desc = o.getData().getStringExtra(Constants.ATTR_TASK_DESCRIPTION);
                            int position = o.getData().getIntExtra(Constants.KEY_TASK_POSITION, -1);
                            updatedTask(title, desc, position);
                        }
                    }
                }
        );

        button = findViewById(R.id.fab_add_task);
        button.setOnClickListener(view -> new_task());

        TaskAdapter adapter = new TaskAdapter(this, user.getTaskList(), this);
        recyclerView = findViewById(R.id.recycler);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStop(){
        super.onStop();
        dao.updateTasks();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        if (item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void new_task() {
        Intent intent = new Intent(this, TaskDetailsActivity.class);
        launcherNew.launch(intent);
    }

    private void updatedTask(String title, String desc, int position) {
        if (position != -1){
            user.getTask(position).setTitle(title);
            user.getTask(position).setDescription(desc);
            recyclerView.getAdapter().notifyDataSetChanged();
        }
    }

    private void saveTask(String title, String desc) {
        user.addTask(new Task(title, desc));
        recyclerView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void clickTaskItem(int position) {
        Intent intent = new Intent(this, TaskDetailsActivity.class);
        intent.putExtra(Constants.ATTR_TASK_TITLE, user.getTask(position).getTitle());
        intent.putExtra(Constants.ATTR_TASK_DESCRIPTION, user.getTask(position).getDescription());
        intent.putExtra(Constants.KEY_TASK_POSITION, position);
        launcherUpdate.launch(intent);
    }

    @Override
    public void clickTaskDone(int position) {
        user.removeTask(position);
        recyclerView.getAdapter().notifyDataSetChanged();
    }
}