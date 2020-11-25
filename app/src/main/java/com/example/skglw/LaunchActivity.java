package com.example.skglw;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputLayout;
import org.json.JSONObject;
import java.util.concurrent.FutureTask;

public class LaunchActivity extends AppCompatActivity {
    EditText etUN, etPW;
    TextInputLayout tilUN, tilPW;
    TextWatcher textWatcher;
    String login, password;

    static SharedPreferences  mSettings;
    public static final String  APP_PREFERENCES = "app";
    public static final String APP_PREFERENCES_TOKEN = "token";
    public static final String APP_PREFERENCES_LOGIN = "login";
    public static final String APP_PREFERENCES_PASSWORD = "password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.launch);
        tilPW = findViewById(R.id.tilPassword);
        etPW = findViewById(R.id.password);
        tilUN = findViewById(R.id.tilUsername);
        etUN = findViewById(R.id.username);

        textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tilPW.setErrorEnabled(false);
                tilUN.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        };

        etUN.addTextChangedListener(textWatcher);
        etPW.addTextChangedListener(textWatcher);

        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        if(mSettings.contains(APP_PREFERENCES_TOKEN)) {
            User.setToken(mSettings.getString(APP_PREFERENCES_TOKEN, ""));
            if(getUser()!=null){
                User.setLogin(mSettings.getString(APP_PREFERENCES_LOGIN, ""));
                User.setPassword(mSettings.getString(APP_PREFERENCES_PASSWORD, ""));
                Intent intent = new Intent(LaunchActivity.this, MainActivity.class);
                intent.putExtra("token", User.getToken());
                LaunchActivity.this.startActivity(intent);
            }
            else {
                User.dropToken();
                etUN.setText(mSettings.getString(APP_PREFERENCES_LOGIN, ""));
                etPW.setText(mSettings.getString(APP_PREFERENCES_PASSWORD, ""));
            }
        }

        Button btnAuth = findViewById(R.id.auth);
        btnAuth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                login = etUN.getText().toString();
                password = etPW.getText().toString();

                    if (login.length() == 0) {
                        etUN.requestFocus();
                        tilUN.setError("Введите имя пользователя");
                    }
                    if (password.length() == 0) {
                        etPW.requestFocus();
                        tilPW.setError("Введите пароль");
                    }
                    else{
                          User.setLogin(login);
                          User.setPassword(password);
                          if(auth()){
                              SharedPreferences.Editor editor = mSettings.edit();
                              editor.putString(APP_PREFERENCES_LOGIN, User.getLogin());
                              editor.putString(APP_PREFERENCES_PASSWORD, User.getPassword());
                              editor.putString(APP_PREFERENCES_TOKEN, User.getToken());
                              editor.apply();
                              getUser();
                              Intent intent = new Intent(LaunchActivity.this, MainActivity.class);
                              //intent.putExtra("token", User.getToken());
                            LaunchActivity.this.startActivity(intent);
                          }
                          else {
                              etPW.setText("");
                              tilPW.setError(" ");
                              tilUN.setError(" ");

                          }
                    }
            }
        });

        Button btnMap = findViewById(R.id.map);
        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LaunchActivity.this, MapActivity.class);
                startActivity(intent);
            }
        });

    }

    private Boolean auth() {
        JSONObject json = new JSONObject();
        try {
            json.put("username", User.getLogin());
            json.put("password", User.getPassword());
        } catch (Exception e) {
        }
        String response = "";
        Log.e("json", json.toString());
        RequestCallable callable = new RequestCallable("POST", "http://mobile-api.fxnode.ru:18888/login", json);
        FutureTask task = new FutureTask(callable);
        Thread request = new Thread(task);
        request.start();
        try {
            request.join();
            response = String.valueOf(task.get());
        } catch (Exception e) {
            e.printStackTrace();
        }
        JSONObject obj;
        try {
            obj = new JSONObject(response);
            User.setToken(obj.get("token").toString());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    return false;
    }

    private String getUser() {
        JSONObject json = new JSONObject();
        try {
            json.put("token", User.getToken());
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestCallable callable = new RequestCallable("POST", "http://mobile-api.fxnode.ru:18888/getuser", json);
        FutureTask task = new FutureTask(callable);
        Thread request = new Thread(task);
        request.start();

        try {
            request.join();
            String jsonString = String.valueOf(task.get());
            json = new JSONObject(jsonString);
            String firstname = " " + json.get("firstname").toString();
            String lastname = json.get("lastname").toString();
            String middlename = " " + json.get("middlename").toString();
            String name = firstname + middlename + lastname;
            User.name = name;
            return name;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
