package com.example.ticketmaster;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class EventListAPI extends AppCompatActivity {

    //intialize variables

    private ListView eventNameList;
    String name_city, city_radius;

    //ProgressDialog progressDialog;

    private ProgressBar progressBar;

    AlertDialog.Builder eventBuilder;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);

        //initialize Builder for AlertDialog
        eventBuilder = new AlertDialog.Builder(this);

        eventNameList = findViewById(R.id.list_event_name);

        final ArrayList<String> events = new ArrayList<>();

        //Using Intent for gat data from the another page
        Intent intent = getIntent();
        name_city = intent.getStringExtra("city");
        city_radius = intent.getStringExtra("radius");


        //Using Volly sent the Api Requast
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="https://app.ticketmaster.com/discovery/v2/events.json?apikey=V5a7L7l9CS9F1RGRBbgr1UtBa2Czay9H&city="+name_city+"&radius="+city_radius+"";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        try{
                            //JSON object for get the data from the API
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject obj = jsonObject.getJSONObject("_embedded");
                            JSONArray results = obj.getJSONArray("events");

                            //Using for loop we can retrive multiple data from API
                            for (int i = 0; i < results.length(); i++) {
                                JSONObject article = results.getJSONObject(i);
                                String sectionName = article.getString("name");
                                String sectionURL = article.getString("url");
                                String sectionPrice = article.getString("priceRanges");
                                String sectionImage = article.getString("images");

                                //for add data in arraylist
                                events.add(sectionName.toString());

                                //Using arrayadapter arraylist data show in listview
                                final ArrayAdapter adapter = new ArrayAdapter<String>(getApplication(),
                                        android.R.layout.simple_list_item_1,
                                        events);
                                eventNameList.setAdapter(adapter);

                                eventNameList.setOnItemClickListener((parent, view, position, id) -> {

                                    //Usind Intent we can send json data to another page
                                    String selectedItem = (String) parent.getItemAtPosition(position);
                                    Intent passData = new Intent(getApplicationContext(), EventDetail.class);
                                    passData.putExtra("eventList", selectedItem);
                                    passData.putExtra("eventName", sectionName);
                                    passData.putExtra("eventURL", sectionURL);
                                    passData.putExtra("eventPrice", sectionPrice);
                                    passData.putExtra("eventImage", sectionImage);
                                    startActivity(passData);
                                    Snackbar snackbar = Snackbar
                                            .make(view, "www.journaldev.com", Snackbar.LENGTH_LONG);
                                    snackbar.show();

                                });


                            }


                        }catch (Exception e) {e.printStackTrace();}
                    }
                }, new Response.ErrorListener() {
            @Override
            //When erro Occurs this method calles
            public void onErrorResponse(VolleyError error) {
                System.out.println("That didn't work!");
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);


    }

    //This method is call for Creating menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.event_menu, menu);
        return true;
    }

    //this method is created for add item in menu
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.event_home:
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                return true;
            case R.id.event_help:

                //AlertDialog using for showing a help tag
                eventBuilder.setMessage(R.string.help_event).setCancelable(true).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog alert = eventBuilder.create();
                //Setting the title manually
                alert.setTitle(R.string.help_title);
                alert.show();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }
}