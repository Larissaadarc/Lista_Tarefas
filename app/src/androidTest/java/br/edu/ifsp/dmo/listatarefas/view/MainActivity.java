package br.edu.ifsp.dmo.listatarefas.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import br.edu.ifsp.dmo.listatarefas.Constants;
import br.edu.ifsp.dmo.listatarefas.R;
import br.edu.ifsp.dmo.listatarefas.dao.UserDao;
import br.edu.ifsp.dmo.listatarefas.model.User;

public class MainActivity extends AppCompatActivity {

    private UserDao userDao;
    private FloatingActionButton addUserButton;
    private TextInputEditText usernameInput;
    private TextInputEditText passwordInput;
    private CheckBox saveDataCheckBox;
    private Button loginButton;
    private ActivityResultLauncher<Intent> resultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userDao = new UserDao(this);
        findIds();
        setClicks();
        checkPrefs();

        resultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult o) {
                        if (o.getResultCode() == RESULT_OK){
                            String name = o.getData().getStringExtra(Constants.ATTR_USERNAME);
                            int password = o.getData().getIntExtra(Constants.ATTR_PASSWORD, -1);
                            save_new_user(name, password);
                        }
                    }
                }
        );
    }

    private void save_new_user(String name, int password) {
        if (userDao.create(new User(name, password))){
            Toast.makeText(this, "Usuário cadastrado com sucesso.", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this, "Erro ao cadastrar Usuário.", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkPrefs() {
        boolean saved;
        String name;
        int password;
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        saved = preferences.getBoolean(Constants.ATTR_SAVE_LOGIN, false);
        if (saved){
            name = preferences.getString(Constants.ATTR_USERNAME, "");
            password = preferences.getInt(Constants.ATTR_PASSWORD, -1);
            updateUI(name, password, saved);
        }
    }

    private void login(){
        String name;
        int password;

        name = usernameInput.getText().toString();
        password = Integer.parseInt(passwordInput.getText().toString());

        if (name.isEmpty() || passwordInput.getText().toString().isEmpty()){
            Toast.makeText(this, "Informe os dados do login.", Toast.LENGTH_SHORT).show();
        }else {
            User user = userDao.recuperate(name);
            if (user == null){
                Toast.makeText(this, "Usuário não cadastrado", Toast.LENGTH_SHORT).show();
            }else {
                if (user.loginTest(password)){
                    updateprefs(user);
                    Intent intent = new Intent(this, TasksActivity.class);
                    intent.putExtra(Constants.KEY_USER, user);
                    startActivity(intent);
                }
            }
        }
    }

    private void updateprefs(User user) {
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        if (saveDataCheckBox.isChecked()){
            editor.putString(Constants.ATTR_USERNAME, user.getName());
            editor.putInt(Constants.ATTR_PASSWORD, -1);
            editor.putBoolean(Constants.ATTR_SAVE_LOGIN, false);
        }
        editor.commit();
    }

    private void updateUI(String name, int password, boolean saved) {
        usernameInput.setText(name);
        passwordInput.setText(String.valueOf(password));
        saveDataCheckBox.setChecked(saved);
    }

    private void setClicks() {
        addUserButton.setOnClickListener(view -> startNewUser());
        loginButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        login();
                    }
                }
        );
    }

    private void startNewUser() {
        Intent intent = new Intent(this, NewUserActivity.class);
        resultLauncher.launch(intent);
    }

    private void findIds() {
        addUserButton = findViewById(R.id.fab_add_user);
        usernameInput = findViewById(R.id.input_username);
        passwordInput = findViewById(R.id.input_password);
        saveDataCheckBox = findViewById(R.id.check_save_data);
        loginButton = findViewById(R.id.button_login);
    }
}