package com.jundat95.locationtracking.View.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;

import com.jundat95.locationtracking.Common.ProgressDialogLoading;
import com.jundat95.locationtracking.Model.RegisterUserModel;
import com.jundat95.locationtracking.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        //dataTest();
    }

    @BindView(R.id.input_full_name) EditText inputFullName;
    @BindView(R.id.input_email) EditText inputEmail;
    @BindView(R.id.input_user_name) EditText inputUserName;
    @BindView(R.id.input_password) EditText inputPassword;
    @BindView(R.id.input_re_password) EditText inputRepassword;
    @BindView(R.id.btn_register) Button btnRegister;

    private ProgressDialogLoading loading;
    private String fullName,email,userName,password,rePassword;

    @OnClick(R.id.btn_register)
    public void register() {
        if (!validate()) {
            return;
        }
        btnRegister.setEnabled(false);
        showProgressDialog();
        // Create json object
        RegisterUserModel user = new RegisterUserModel(
                userName,
                password,
                fullName,
                email
        );


    }

    private void registerSuccess() {
        setResult(RESULT_OK,null);
        finish();
    }

    @OnClick(R.id.link_login)
    public void linkLoginClick(){
        Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
    }

    private void showProgressDialog(){
        loading = new ProgressDialogLoading(RegisterActivity.this," Waiting...");
        loading.showProgressDialog();
    }

    private void dismissProgressDialog(){
        loading.dismissProgressDialog();
    }

    private boolean validate() {
        boolean valid = true;

        fullName = inputFullName.getText().toString();
        email = inputEmail.getText().toString();
        userName = inputUserName.getText().toString();
        password = inputPassword.getText().toString();
        rePassword = inputRepassword.getText().toString();

        if (fullName.isEmpty() || fullName.length() <= 5) {
            inputFullName.setError("at least 5 characters");
            valid = false;
        } else {
            inputFullName.setError(null);
        }

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            inputEmail.setError("enter a valid email address");
            valid = false;
        } else {
            inputEmail.setError(null);
        }

        if (userName.isEmpty() || userName.length() <= 5) {
            inputUserName.setError("at least 5 characters");
            valid = false;
        } else {
            inputUserName.setError(null);
        }

        if (password.isEmpty() || password.length() <= 4) {
            inputPassword.setError("at least 4 characters");
            valid = false;
        } else {
            inputPassword.setError(null);
        }

        if (rePassword.isEmpty() || rePassword.length() <= 4) {
            inputRepassword.setError("at least 4 characters");
            valid = false;
        } else {
            if(!password.equals(rePassword)){
                inputRepassword.setError("Re password don't match password");
                valid = false;
            }else {
                inputRepassword.setError(null);
            }
        }

        return valid;

    }

    private void dataTest(){
        inputFullName.setText("Tinh Ngo");
        inputUserName.setText("jundat95");
        inputPassword.setText("123456");
        inputRepassword.setText("123456");
        inputEmail.setText("jundat95@gmail.com");
    }

}
