package br.edu.ifsp.dmo.listatarefas.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import br.edu.ifsp.dmo.listatarefas.Constants;
import br.edu.ifsp.dmo.listatarefas.R;
import br.edu.ifsp.dmo.listatarefas.view.adapter.TaskItemClickListener;

public class TaskDetailsActivity extends AppCompatActivity  {
    private TextInputEditText titleInputText;
    private TextInputEditText descriptionInputText;
    private TextInputLayout titleLayout;
    private Button button;
    private boolean isUpdate;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details);

        ActionBar bar = getSupportActionBar();
        if(bar != null){
            bar.setDisplayHomeAsUpEnabled(true);
        }

        titleInputText = findViewById(R.id.input_task_title);
        titleLayout = findViewById(R.id.textInputLayout4);
        descriptionInputText = findViewById(R.id.input_task_description);
        button = findViewById(R.id.button_save_task);
        button.setOnClickListener(view -> save());

        Intent intent = getIntent();
        if(intent.hasExtra(Constants.KEY_TASK_POSITION)){
            isUpdate = true;
            titleInputText.setText(intent.getStringExtra(Constants.ATTR_TASK_TITLE));
            descriptionInputText.setText(intent.getStringExtra(Constants.ATTR_TASK_DESCRIPTION));
            position = intent.getIntExtra(Constants.KEY_TASK_POSITION,-1);

            titleInputText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    titleLayout.setErrorEnabled(false);
                    titleLayout.setError("");
                }
            });
        }
    }

