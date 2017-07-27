package fujitsu.vidhayak.vidhayakjee;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import fujitsu.vidhayak.vidhayakjee.AppIntro.Config;
import fujitsu.vidhayak.vidhayakjee.AppIntro.DefaultIntro;

public class MainActivity extends AbsRuntimePermission implements View.OnClickListener {

    Button msignupbtn, mloginbtn;
    EditText mnumbertxt;
    String sendotptxt;

    private static final int REQUEST_PERMISSION = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {

                SharedPreferences sharedPreferences = getSharedPreferences(Config.FLAG, Context.MODE_PRIVATE);

                if (sharedPreferences.getBoolean(Config.FLAG, true)) {

                    startActivity(new Intent(MainActivity.this, DefaultIntro.class));
                    SharedPreferences.Editor e = sharedPreferences.edit();
                    e.putBoolean(Config.FLAG, false);
                    e.apply();
                }
            }
        });
        t.start();

        mnumbertxt = (EditText) findViewById(R.id.number_txt);
        msignupbtn = (Button) findViewById(R.id.signup_btn);
        mloginbtn = (Button) findViewById(R.id.login_btn);


        msignupbtn.setOnClickListener(this);
        mloginbtn.setOnClickListener(this);

        requestAppPermissions(new String[]{
                        Manifest.permission.READ_SMS,
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE},
                R.string.msg, REQUEST_PERMISSION);

    }

    @Override
    public void onPermissionsGranted(int requestCode) {

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.login_btn:

                SendNumber();

                break;

            case R.id.signup_btn:

                Intent intent = new Intent(MainActivity.this,RegisterClass.class);
                startActivity(intent);

                break;

        }
    }

    public void SendNumber(){

        final String KEY_mobile = "mobile";
        final String KEY_token = "token";

        sendotptxt = mnumbertxt.getText().toString().trim();
        //final String token = SaveUserId.getInstance(this).getDeviceToken();

        if (TextUtils.isEmpty(sendotptxt)) {
            mnumbertxt.requestFocus();
            mnumbertxt.setError("This Field Is Mandatory");
        }
        else{

            String url = null;

            String REGISTER_URL = Urls.login;

            REGISTER_URL = REGISTER_URL.replaceAll(" ", "%20");
            try {
                URL sourceUrl = new URL(REGISTER_URL);
                url = sourceUrl.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //   Log.d("jaba", usernsme);
                            try {
                                JSONObject jsonresponse = new JSONObject(response);
                                boolean success = jsonresponse.getBoolean("success");

                                if (success) {


                                    Intent registerintent = new Intent(MainActivity.this, Verifyotp.class);
                                    startActivity(registerintent);
                                    finish();


                                } else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                    builder.setMessage("Login Failed....")
                                            .setNegativeButton("Retry", null)
                                            .create()
                                            .show();

                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            Toast.makeText(MainActivity.this, response.toString(), Toast.LENGTH_LONG).show();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // Log.d("jabadi", usernsme);
                            Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_LONG).show();

                        }
                    }) {


                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    //Adding parameters to request

                    params.put(KEY_mobile, sendotptxt);
                    return params;

                }

            };
            RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
            requestQueue.add(stringRequest);
        }
    }

}