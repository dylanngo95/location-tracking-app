package com.jundat95.locationtracking.View.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jundat95.locationtracking.Common.ProgressDialogLoading;
import com.jundat95.locationtracking.Common.TiSharedPreferences;
import com.jundat95.locationtracking.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private static final int REQUEST_REGISTER = 0;
    private static final int ACCESS_FINE_LOCATION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        checkPermission();
        checkLogin();
        //dataTest();
    }

    @BindView(R.id.input_user_name) EditText inputUserName;
    @BindView(R.id.input_password) EditText inputPassword;
    @BindView(R.id.btn_login) Button btnLogin;

    private ProgressDialogLoading loading;
    private String userName;
    private String password;

    @OnClick(R.id.btn_login)
    public void loginClick(){
        if(!validate()){
            return;
        }
        btnLogin.setEnabled(false);
        showProgressDialogLoading();



    }

    private void loginSuccess() {
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.link_register)
    public void linkRegisterClick(){
        Intent intent = new Intent(getApplicationContext(),RegisterActivity.class);
        startActivityForResult(intent,REQUEST_REGISTER);
        overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_REGISTER){
            if(resultCode == RESULT_OK){
                //this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    private void showProgressDialogLoading(){
        loading = new ProgressDialogLoading(LoginActivity.this," Waiting...");
        loading.showProgressDialog();
    }

    private void dismissProgressDialogLoading(){
        loading.dismissProgressDialog();
    }

    private boolean validate(){
        boolean valid = true;
        userName = inputUserName.getText().toString();
        password = inputPassword.getText().toString();

        if(userName.isEmpty() || userName.length() <= 5){
            inputUserName.setError("at least 5 characters");
            valid = false;
        }else {
            inputUserName.setError(null);
        }

        if(password.isEmpty() || password.length() <= 4){
           inputPassword.setError("at least 4 characters");
            valid = false;
        }else {
            inputPassword.setError(null);
        }

        return valid;
    }

    private void checkLogin(){

    }

    private void dataTest(){
        inputUserName.setText("jundat95");
        inputPassword.setText("123456");
    }

    private void checkPermission(){

        // Check Permission
        if (ContextCompat.checkSelfPermission(LoginActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(LoginActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Every open app
                ActivityCompat.requestPermissions(LoginActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        ACCESS_FINE_LOCATION);

            } else {

                // One open app
                ActivityCompat.requestPermissions(LoginActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        ACCESS_FINE_LOCATION);
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(
                            LoginActivity.this,
                            "Granted permission",
                            Toast.LENGTH_SHORT
                    ).show();

                } else {

                    Toast.makeText(
                            LoginActivity.this,
                            "Reopen App, Please Allow permission",
                            Toast.LENGTH_SHORT
                    ).show();
                }
                return;
            }

        }
    }
}
