package com.fydp2.cbdnlp;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;


public class MainActivity extends AppCompatActivity {

    EditText inp;
    TextView cate, proba;
    Button buttonSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_main);

        inp = findViewById(R.id.input);
        cate = findViewById(R.id.category);
        proba = findViewById(R.id.probability);
        buttonSubmit = findViewById(R.id.detect);

        buttonSubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String comm = inp.getText().toString();

                volleyPost(comm);
                inp.requestFocus();
            }
        });
    }

    public void volleyPost(String comm) {
        String postUrl = "https://www.cbdnlp.xyz/predict";
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JSONObject postData = new JSONObject();
        try {
            postData.put("content", comm);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, postUrl, postData, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JsonParser parser = new JsonParser();
                JsonObject jsonObject = parser.parse(String.valueOf(response)).getAsJsonObject();
                JsonObject root = jsonObject.getAsJsonObject();
                for (Map.Entry<String, JsonElement> e : root.entrySet()) {
                    String ct = e.getKey();
                    cate.setText(ct);
                    String pr = String.valueOf(jsonObject.get(ct));
                    pr = pr.replaceAll("^\"+|\"+$", "");
                    proba.setText(pr + "%");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        requestQueue.add(jsonObjectRequest);

    }

}