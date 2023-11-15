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

public class NewUserActivity extends AppCompatActivity {

    private TextInputEditText nameInput;
    private TextInputEditText passwordInput;
    private TextInputEditText confirmInput;
    private TextInputLayout textInputLayout1;
    private TextInputLayout textInputLayout3;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayShowHomeEnabled(true);
        }

        nameInput = findViewById(R.id.input_new_username);
        passwordInput = findViewById(R.id.input_new_password);
        confirmInput = findViewById(R.id.input_new_confirm_password);
        textInputLayout1 = findViewById(R.id.textInputLayout);
        textInputLayout3 = findViewById(R.id.textInputLayout3);
        button = findViewById(R.id.button_save_new_user);

        button.setOnClickListener(
                view -> process()
        );

        nameInput.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        textInputLayout1.setError("");
                        textInputLayout1.setErrorEnabled(false);
                    }
                }

        );

        confirmInput.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        textInputLayout3.setError("");
                        textInputLayout3.setErrorEnabled(false);
                    }
                }
        );
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        if (item.getItemId() == android.R.id.home){
            setResult(RESULT_CANCELED);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void process() {
        String name = nameInput.getText().toString();
        String pwd = passwordInput.getText().toString();
        String confirm = confirmInput.getText().toString();

        if (!pwd.equals(confirm)){
            textInputLayout3.setError(getString(R.string.password_not_equal));
            textInputLayout3.setErrorEnabled(true);
        }else {
            if (name.isEmpty()){
                textInputLayout1.setErrorEnabled(true);
                textInputLayout1.setError(getString(R.string.write_username));
            }else {
                int password = Integer.parseInt(pwd);
                Intent intent = new Intent();
                intent.putExtra(Constants.ATTR_USERNAME, name);
                intent.putExtra(Constants.ATTR_PASSWORD, password);
                setResult(RESULT_OK, intent);
                finish();
            }
        }
    }
}