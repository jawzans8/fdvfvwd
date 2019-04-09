package com.project.lockerapp;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.util.ArrayList;

import br.com.moip.validators.CreditCard;
import cn.pedant.SweetAlert.SweetAlertDialog;
import cz.msebera.android.httpclient.Header;

public class UpdateActivity extends BaseActivity {

    private EditText mLockerET, mUsernameET, mPasswordET, mEmailET, mPhoneET, mCreditCardET;
    private String mUsername, mPassword, mEmail, mPhone, mCreditCard;

    ArrayList<String> listOfPattern=new ArrayList<String>();

    private SweetAlertDialog pDialog;

    private SessionManager mSessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        mLockerET     = (EditText) findViewById(R.id.locker_et);
        mUsernameET   = (EditText) findViewById(R.id.username_et);
        mPasswordET   = (EditText) findViewById(R.id.password_et);
        mEmailET      = (EditText) findViewById(R.id.email_et);
        mPhoneET      = (EditText) findViewById(R.id.phone_et);
        mCreditCardET = (EditText) findViewById(R.id.credit_number_et);

        String ptVisa = "^4[0-9]{6,}$";
        listOfPattern.add(ptVisa);
        String ptMasterCard = "^5[1-5][0-9]{5,}$";
        listOfPattern.add(ptMasterCard);
        String ptAmeExp = "^3[47][0-9]{5,}$";
        listOfPattern.add(ptAmeExp);
        String ptDinClb = "^3(?:0[0-5]|[68][0-9])[0-9]{4,}$";
        listOfPattern.add(ptDinClb);
        String ptDiscover = "^6(?:011|5[0-9]{2})[0-9]{3,}$";
        listOfPattern.add(ptDiscover);
        String ptJcb = "^(?:2131|1800|35[0-9]{3})[0-9]{3,}$";
        listOfPattern.add(ptJcb);

        pDialog = new SweetAlertDialog(UpdateActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);

        mSessionManager = new SessionManager(getApplicationContext());

        mLockerET.setText("Number " + mSessionManager.getKeyLocker() + "");
        mUsernameET.setText(mSessionManager.getKeyUsername());
        mPasswordET.setText(mSessionManager.getKeyPassword());
        mEmailET.setText(mSessionManager.getKeyEmail());
        mPhoneET.setText(mSessionManager.getKeyPhone() + "");
        mCreditCardET.setText(mSessionManager.getKeyCredit() + "");
    }

    public void attempSave(View view)
    {
        mUsername   = mUsernameET.getText().toString();
        mPassword   = mPasswordET.getText().toString();
        mEmail      = mEmailET.getText().toString();
        mPhone      = mPhoneET.getText().toString();
        mCreditCard = mCreditCardET.getText().toString();

        if (TextUtils.isEmpty(mUsername) || TextUtils.isEmpty(mPassword)
                || TextUtils.isEmpty(mEmail) || TextUtils.isEmpty(mPhone) || TextUtils.isEmpty(mCreditCard))
        {
            Toast.makeText(getApplicationContext(), "All Fields are Required!", Toast.LENGTH_LONG).show();
            return;
        }



        if (!mEmail.matches("[a-zA-Z0-9._-]+@[a-z]+.[a-z]+"))
        {
            Toast.makeText(getApplicationContext(), "Invalid Email Address!", Toast.LENGTH_LONG).show();
            return;
        }

        if (!android.util.Patterns.PHONE.matcher(mPhone).matches())
        {
            Toast.makeText(getApplicationContext(), "Invalid Phone Number!", Toast.LENGTH_LONG).show();
            return;
        }

        if (!new CreditCard(mCreditCard).isValid())
        {
            Toast.makeText(getApplicationContext(), "Invalid Credit Number!", Toast.LENGTH_LONG).show();
            return;
        }

        RequestParams mRequestParams = new RequestParams();
        mRequestParams.put("id", mSessionManager.getKeyID() + "");
        mRequestParams.put("email", mEmail);
        mRequestParams.put("phone", mPhone);
        mRequestParams.put("password", mPassword);
        mRequestParams.put("credit", mCreditCard);

        pDialog.show();

        LockerRestClints.post("updateInfo.php", mRequestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                pDialog.dismiss();

                try
                {
                    String res = new String(responseBody);

                    JSONObject resObj = new JSONObject(res);

                    if (resObj.getBoolean("success"))
                    {
                        mSessionManager.setKeyUsername(mUsername);
                        mSessionManager.setKeyPassword(mPassword);
                        mSessionManager.setKeyEmail(mEmail);
                        mSessionManager.setKeyPhone(Integer.parseInt(mPhone));
                        mSessionManager.setKeyCredit(mCreditCard);

                        Toast.makeText(getApplicationContext(), "Done!",
                                Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), resObj.getString("message"),
                                Toast.LENGTH_LONG).show();
                    }
                }
                catch (Exception ex)
                {
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

    public void attempDelete(View view)
    {
        RequestParams mRequestParams = new RequestParams();
        mRequestParams.put("id", mSessionManager.getKeyID() + "");

        pDialog.show();

        LockerRestClints.post("delete.php", mRequestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                pDialog.dismiss();

                try
                {
                    String res = new String(responseBody);

                    JSONObject resObj = new JSONObject(res);

                    if (resObj.getBoolean("success"))
                    {
                        mSessionManager.setKeyID(0);

                        Toast.makeText(getApplicationContext(), "Done!", Toast.LENGTH_LONG).show();

                        Intent mIntent = new Intent(UpdateActivity.this,
                                MainActivity.class);
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
}
