package com.project.lockerapp;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cn.pedant.SweetAlert.SweetAlertDialog;
import cz.msebera.android.httpclient.Header;

public class MainActivity extends BaseActivity {

    private EditText mUsernameET, mPasswordET;
    private String mUsername, mPassword;

    private SweetAlertDialog pDialog;

    private SessionManager mSessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mUsernameET = (EditText) findViewById(R.id.username_et);
        mPasswordET = (EditText) findViewById(R.id.password_et);

        pDialog = new SweetAlertDialog(MainActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);

        mSessionManager = new SessionManager(getApplicationContext());

        if (mSessionManager.getKeyID() != 0)
        {
            Intent mIntent = new Intent(MainActivity.this,
                    UpdateActivity.class);
            startActivity(mIntent);
            finish();
        }
    }

    public void HandleClickButton(View view)
    {
        Intent mIntent;

        Button mClickedButton = (Button) view;

        int mClickedButtonID = mClickedButton.getId();

        switch (mClickedButtonID)
        {
            case R.id.login_btn:
                attempLogin();
                break;

            case R.id.register_btn:
                mIntent = new Intent(MainActivity.this, ResgiterActivity.class);
                startActivityForResult(mIntent, 1);
                break;
        }
    }

    private void attempLogin()
    {
        mUsername = mUsernameET.getText().toString();
        mPassword = mPasswordET.getText().toString();

        if (TextUtils.isEmpty(mUsername) || TextUtils.isEmpty(mPassword))
        {
            Toast.makeText(getApplicationContext(), "All Fields are Required!", Toast.LENGTH_LONG).show();
            return;
        }

        RequestParams mRequestParams = new RequestParams();
        mRequestParams.put("username", mUsername);
        mRequestParams.put("password", mPassword);

        pDialog.show();

        LockerRestClints.post("login.php", mRequestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                pDialog.dismiss();

                try
                {
                    String res = new String(responseBody);

                    JSONObject resObj = new JSONObject(res);

                    if (resObj.getBoolean("success"))
                    {
                        mSessionManager.setKeyID(resObj.getInt("id"));
                        mSessionManager.setKeyUsername(mUsername);
                        mSessionManager.setKeyPassword(mPassword);
                        mSessionManager.setKeyEmail(resObj.getString("email"));
                        mSessionManager.setKeyPhone(resObj.getInt("phone"));
                        mSessionManager.setKeyCredit(resObj.getString("credit_number"));
                        mSessionManager.setKeyLocker(resObj.getInt("locker_number"));

                        Intent mIntent = new Intent(MainActivity.this,
                                UpdateActivity.class);
                        startActivity(mIntent);
                        finish();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), resObj.getString("message"),
                                Toast.LENGTH_LONG).show();
                    }
                }
                catch (Exception ex)
                {
                    Log.e("error111", ex + "");
                    Toast.makeText(getApplicationContext(), "Unexpected wrong! ", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                pDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Unexpected wrong! ", Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1)
        {
            if (resultCode == RESULT_OK)
            {
                Intent mIntent = new Intent(MainActivity.this, UpdateActivity.class);
                startActivity(mIntent);
                finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        new SweetAlertDialog(MainActivity.this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Alert")
                .setContentText("Are you sure you want to exit?")
                .setConfirmText("Exit!")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        finish();
                    }
                })
                .show();
    }
}
