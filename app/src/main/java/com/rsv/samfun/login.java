package com.rsv.samfun;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.util.HashMap;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class login extends AppCompatActivity {

    private EditText uname;
    private EditText pword;
    private CheckBox Rmbme;
    private String username;
    private String password;
    private Button alrt;
    private Button login;
    private SharedPreferences mPrefs;
    private static final String PREFS_NAME = "PrefsFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (!isTaskRoot()) {
            finish();
            return;
        }

        bind();
        mPrefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        alrt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View alertView) {
                ShowInfo();
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginReq();
            }
        });
        getPrefsData();
    }

    private void bind() {
        uname = findViewById(R.id.user);
        pword = findViewById(R.id.pass);
        Rmbme = findViewById(R.id.Rmbme);
        alrt = findViewById(R.id.forgotpass);
        login = findViewById(R.id.loginbtn);
    }

    private void getPrefsData() {
        SharedPreferences sp = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        if (sp.contains("pref_uname")){
            String u = sp.getString("pref_uname", "not_found");
            uname.setText(u.toString());
        }
        if (sp.contains("pref_pass")){
            String p = sp.getString("pref_pass", "not_found");
            pword.setText(p.toString());
        }
        if (sp.contains("pref_check")){
            Boolean b = sp.getBoolean("pref_check", false);
            Rmbme.setChecked(b);
        }
    }

    private void makeToast(final Context ctx, final String msg, final int dur) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast toast = Toast.makeText(ctx, msg, dur);
                View view = toast.getView();
                view.setBackgroundColor(Color.TRANSPARENT);

                TextView text = view.findViewById(android.R.id.message);
                text.setShadowLayer((float) 0.75, 2, 4, Color.BLACK);
                text.setTextColor(Color.WHITE);
                text.setBackgroundResource(R.drawable.btn_bg);
                toast.show();
            }
        });
    }

    private void ShowInfo() {
        AlertDialog alertDialog = new AlertDialog.Builder(login.this).create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setTitle(getString(R.string.Info));
        alertDialog.setMessage(getString(R.string.contact));
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    private void loginReq() {
        username = uname.getText().toString();
        password = pword.getText().toString();
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password))
        {
            AlertDialog fill = new AlertDialog.Builder(login.this).create();
            fill.setCanceledOnTouchOutside(false);
            fill.setTitle(getString(R.string.note));
            fill.setMessage(getString(R.string.fill_both));
            fill.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            fill.show();
        }
        else{
            new login2().execute();
        }
    }

    class login2 extends AsyncTask<Void, Void, Void> {
        ProgressDialog pdialog = new ProgressDialog(login.this);
        final String USER_AGENT = getString(R.string.user_agent);
        final String tzone = getString(R.string.tzone);
        final String loginFormUrl = getString(R.string.form_url);
        final String loginActionUrl = getString(R.string.action_url);
        final String nameurl = getString(R.string.nameurl);
        public HashMap<String, String> cookies = new HashMap<>();
        HashMap<String, String> formData = new HashMap<>();

        @Override
        protected void onPreExecute() {
            pdialog.setMessage("Loading...");
            pdialog.setCanceledOnTouchOutside(false);
            pdialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Connection.Response loginForm = Jsoup.connect(loginFormUrl)
                        .followRedirects(true)
                        .method(Connection.Method.GET)
                        .userAgent(USER_AGENT).execute();
                cookies.putAll(loginForm.cookies());
                formData.put("userid", username);
                formData.put("pwd", password);
                formData.put("timezoneOffset", tzone);
                Connection.Response login = Jsoup.connect(loginActionUrl)
                        .cookies(cookies)
                        .data(formData)
                        .method(Connection.Method.POST)
                        .userAgent(USER_AGENT)
                        .followRedirects(true)
                        .execute();

                cookies.clear();
                cookies.putAll(login.cookies());
                Document checklogin = login.parse();
                Element log = checklogin.select(getString(R.string.check_lgn)).first();
                if(log != null)
                {
                    if (Rmbme.isChecked()){
                        Boolean boolIsChecked = Rmbme.isChecked();
                        SharedPreferences.Editor editor = mPrefs.edit();
                        editor.putString("pref_uname", username);
                        editor.putString("pref_pass", password);
                        editor.putBoolean("pref_check", boolIsChecked);
                        editor.apply();
                    }else{
                        mPrefs.edit().clear().apply();
                    }

                    Document getname = Jsoup.connect(nameurl)
                            .userAgent(USER_AGENT)
                            .cookies(login.cookies())
                            .followRedirects(true)
                            .get();
                    String name = getname.select("a[id='pthdr2uoghelp']").first().text();
                    name = name.replace("(Campus ID:GUR/", "");
                    name = name.substring(0, name.indexOf(", SIS"));
                    Intent myIntent = new Intent( login.this, MainActivity.class);
                    myIntent.putExtra("header_name",name);
                    myIntent.putExtra("hashMap", cookies);
                    myIntent.putExtra("uname", username);
                    myIntent.putExtra("pword", password);
                    myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(myIntent);
                }
                else{
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog failed = new AlertDialog.Builder(login.this).create();
                            failed.setCanceledOnTouchOutside(false);
                            failed.setTitle(getString(R.string.error));
                            failed.setMessage(getString(R.string.try_again));
                            failed.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                            failed.show();
                        }
                    });
                }
            } catch (Exception e) {
                String Msg = e.toString();
                if (Msg.contains("ENETUNREACH")) {
                    Msg = "Network is Unreachable";
                }else if (Msg.contains("EHOSTUNREACH")) {
                    Msg = "No Route to Host, Connect to UoG Campus Network";
                }
                final String finalMsg = Msg;
                makeToast(getApplicationContext(),Msg,2500);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if(pdialog.isShowing()){
                pdialog.dismiss();
            }
        }
    }
}