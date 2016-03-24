package com.ibrahim.volley;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.List;

import model.Flower;
import parsers.FlowerJSONParser;

public class MainActivity extends AppCompatActivity {
    public static final String PHOTOS_BASE_URL =
            "http://services.hanselandpetal.com/photos/";
    TextView output;
    ProgressBar pb;
    List<Flower> flowerList;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.list);
//		Initialize the TextView for vertical scrolling
        // output = (TextView) findViewById(R.id.textView);
        //output.setMovementMethod(new ScrollingMovementMethod());

        pb = (ProgressBar) findViewById(R.id.progressBar);
        pb.setVisibility(View.INVISIBLE);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_do_task) {
            if (isOnline()) {
                requestData("http://services.hanselandpetal.com/feeds/flowers.json");
               // requestData("http://api.themoviedb.org/3/movie/popular?api_key=9be7f08976068ddd6a18566f3a565cc1");
            } else {
                Toast.makeText(this, "Network isn't available", Toast.LENGTH_LONG).show();
            }
        }
        return false;
    }

    private void requestData(String uri) {
        StringRequest request = new StringRequest(uri,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        flowerList = FlowerJSONParser.parseFeed(response);
                        updateDisplay();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        );
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }

    protected void updateDisplay() {
        FlowerAdapter adapter = new FlowerAdapter(this, R.layout.item_flower, flowerList);
        listView.setAdapter(adapter);
    }

    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

}