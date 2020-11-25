package com.example.skglw;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;
import org.json.JSONObject;
import java.util.concurrent.FutureTask;

public class Fragment2 extends androidx.fragment.app.Fragment {

    public static Fragment2 newInstance() {
        Fragment2 fragmentFirst = new Fragment2();
        return fragmentFirst;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    LinearLayout loginLl, logoutLl,passLl;
    Button passBtn, logBtn, logoutBtn,cancelBtn;
   EditText oldPassEt,newPassEt,oldLogEt,newLogEt;
    TextInputLayout oldPassTil,newPassTil,oldLogTil,newLogTil;
    TextView changePass,changeLog, userTv, logoutTv, mesTv, unblockTv;
    TextWatcher textWatcher;

    static SharedPreferences  mSettings;
    public static final String  APP_PREFERENCES = "app";
    public static final String APP_PREFERENCES_TOKEN = "token";
    public static final String APP_PREFERENCES_LOGIN = "login";
    public static final String APP_PREFERENCES_PASSWORD = "password";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment2, container, false);
        loginLl = view.findViewById(R.id.llLogin);
        logoutLl = view.findViewById(R.id.llLogout);
        passLl = view.findViewById(R.id.llPass);

        oldPassEt=view.findViewById(R.id.etOldPass);
        oldPassTil=view.findViewById(R.id.tilOldPass);
        oldLogEt=view.findViewById(R.id.etOldLog);
        oldLogTil=view.findViewById(R.id.tilOldLog);
        newPassEt=view.findViewById(R.id.etNewPass);
        newPassTil=view.findViewById(R.id.tilNewPass);
        newLogEt=view.findViewById(R.id.etNewLog);
        newLogTil=view.findViewById(R.id.tilNewLog);
        passBtn=view.findViewById(R.id.btnPass);
        logBtn=view.findViewById(R.id.btnLog);
        logoutTv=view.findViewById(R.id.tvLogout);
        userTv = view.findViewById(R.id.tvUser);
        userTv.setText("Вы вошли как "+User.name);
        logoutBtn=view.findViewById(R.id.btnLogout);
        cancelBtn=view.findViewById(R.id.btnCancel);
        mesTv=view.findViewById(R.id.tvMes);

        changeLog=view.findViewById(R.id.tvLog);
        changeLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(loginLl.getVisibility()==View.VISIBLE) {
                    changeLog.setTextColor(getResources().getColor(R.color.white));
                    loginLl.setVisibility(View.GONE);
                }
                else{
                    changeLog.setTextColor(getResources().getColor(R.color.colorAccent));
                    changePass.setTextColor(getResources().getColor(R.color.white));
                    logoutTv.setTextColor(getResources().getColor(R.color.white));
                    loginLl.setVisibility(View.VISIBLE);
                    logoutLl.setVisibility(View.GONE);
                    passLl.setVisibility(View.GONE);
                }
            }
        });

        changePass=view.findViewById(R.id.tvPass);
        changePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(passLl.getVisibility()==View.VISIBLE) {
                    changePass.setTextColor(getResources().getColor(R.color.white));
                    passLl.setVisibility(View.GONE);
                }
                else{
                changePass.setTextColor(getResources().getColor(R.color.colorAccent));
                changeLog.setTextColor(getResources().getColor(R.color.white));
                logoutTv.setTextColor(getResources().getColor(R.color.white));
                passLl.setVisibility(View.VISIBLE);
                loginLl.setVisibility(View.GONE);
                logoutLl.setVisibility(View.GONE);
            }
            }
        });

        logoutTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                if(logoutLl.getVisibility()==View.VISIBLE) {
                    logoutTv.setTextColor(getResources().getColor(R.color.white));
                    logoutLl.setVisibility(View.GONE);
                }
                else{
            logoutTv.setTextColor(getResources().getColor(R.color.colorAccent));
            changePass.setTextColor(getResources().getColor(R.color.white));
            changeLog.setTextColor(getResources().getColor(R.color.white));
            logoutLl.setVisibility(View.VISIBLE);
            loginLl.setVisibility(View.GONE);
            passLl.setVisibility(View.GONE);

        }
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutTv.setTextColor(getResources().getColor(R.color.white));
                loginLl.setVisibility(View.GONE);
                logoutLl.setVisibility(View.GONE);
                passLl.setVisibility(View.GONE);

            }
        });

        logBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldL, newL;
                oldL = oldLogEt.getText().toString();
                newL = newLogEt.getText().toString();
                oldLogEt.addTextChangedListener(textWatcher);
                newLogEt.addTextChangedListener(textWatcher);
                if (oldL.length() == 0) {
                    oldLogTil.requestFocus();
                    oldLogTil.setError("Введите старый логин");
                }
                if (newL.length() == 0) {
                    newLogTil.requestFocus();
                    newLogTil.setError("Введите новый логин");
                }
                else if (!(oldL.equals(User.getLogin()))) {
                    oldLogTil.requestFocus();
                    oldLogTil.setError("Неправильный логин");
                }
                else {

                    changeLog.setTextColor(getResources().getColor(R.color.white));
                    oldLogEt.setText("");
                    newLogEt.setText("");
                    loginLl.setVisibility(View.GONE);
                    JSONObject json = new JSONObject();
                    try {
                        json.put("token", User.getToken());
                        json.put("username",newL);
                        User.setLogin(newL);
                        mSettings = getActivity().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = mSettings.edit();
                        editor.putString(APP_PREFERENCES_LOGIN, newL);
                        editor.apply();
                        RequestCallable callable = new RequestCallable("PUT", "http://mobile-api.fxnode.ru:18888/editeusername", json, getActivity());
                        FutureTask task = new FutureTask(callable);
                        Thread request = new Thread(task);
                        request.start();
                        request.join();
                    }catch(Exception e){}
                }

            }
        });

        passBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldP, newP;
                oldP = oldPassEt.getText().toString();
                newP = newPassEt.getText().toString();
                oldPassEt.addTextChangedListener(textWatcher);
                newPassEt.addTextChangedListener(textWatcher);
                if (oldP.length() == 0) {
                    oldPassTil.requestFocus();
                    oldPassTil.setError("Введите старый пароль");
                }
                if (newP.length() == 0) {
                    newPassTil.requestFocus();
                    newPassTil.setError("Введите новый пароль");
                }
                else if (!(oldP.equals(User.getPassword()))) {
                    oldPassTil.requestFocus();
                    oldPassTil.setError("Неправильный пароль");
                }
                else {

                    changePass.setTextColor(getResources().getColor(R.color.white));
                    oldPassEt.setText("");
                    newPassEt.setText("");
                    passLl.setVisibility(View.GONE);
                    JSONObject json = new JSONObject();
                    try {
                        json.put("token", User.getToken());
                        json.put("password",newP);
                        User.setPassword(newP);

                        mSettings = getActivity().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = mSettings.edit();
                        editor.putString(APP_PREFERENCES_PASSWORD, newP);
                        editor.apply();
                        RequestCallable callable = new RequestCallable("PUT", "http://mobile-api.fxnode.ru:18888/editepassword", json, getActivity());
                        FutureTask task = new FutureTask(callable);
                        Thread request = new Thread(task);
                        request.start();
                        request.join();
                    }catch(Exception e){}
                }

            }
        });
        textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                oldPassTil.setErrorEnabled(false);
                newPassTil.setErrorEnabled(false);
                oldLogTil.setErrorEnabled(false);
                newLogTil.setErrorEnabled(false);
            }
            @Override
            public void afterTextChanged(Editable s) {}
        };
        unblockTv=view.findViewById(R.id.tvUnblock);
        unblockTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialAlertDialogBuilder dialog = new MaterialAlertDialogBuilder(getContext());
                dialog.setTitle("Разблокировать карты");
                dialog.setMessage("Вы уверены, что хотите разблокировать все карты?");
                dialog.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                dialog.setPositiveButton("Разблокировать", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try{
                            RequestCallable callable = new RequestCallable( "http://mobile-api.fxnode.ru:18888/unblock-all", getActivity());
                            FutureTask task = new FutureTask(callable);
                            Thread request = new Thread(task);
                            request.start();
                            request.join();
                            unblockTv.setEnabled(false);
                        }catch(Exception e){}
                    }
                });
                dialog.show();
            }
        });

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject json = new JSONObject();
                try {
                    json.put("token", User.getToken());
                    RequestCallable callable = new RequestCallable("DELETE", "http://mobile-api.fxnode.ru:18888/logout", json, getActivity());
                    FutureTask task = new FutureTask(callable);
                    Thread request = new Thread(task);
                    request.start();
                    request.join();
                    mSettings = getActivity().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = mSettings.edit();
                    editor.remove(APP_PREFERENCES_TOKEN);
                    editor.apply();                    Intent intent = new Intent(getActivity(),LaunchActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }catch(Exception e){}
            }
        });
        return view;
    }
}
