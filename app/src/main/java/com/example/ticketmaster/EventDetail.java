package com.example.ticketmaster;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class EventDetail extends AppCompatActivity {

    //Intialize variables
    AlertDialog.Builder builder;

    DatabaseHelperForFavoriteEvent myDB;

    TextView nameEvent, nameURL, namePrice, nameImageURL;

    Button addFav;

    String eventDetail, eventURL , priceRange, imageURL, eventName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        builder = new AlertDialog.Builder(this);

        myDB = new DatabaseHelperForFavoriteEvent(this);

        nameEvent = findViewById(R.id.name_event);
        nameURL = findViewById(R.id.name_url);
        namePrice = findViewById(R.id.name_price);
        nameImageURL = findViewById(R.id.name_image);
        addFav = findViewById(R.id.add_fav);

        Intent intent = getIntent();
        String eventSaveName = intent.getStringExtra("eventList");
        eventDetail = intent.getStringExtra("eventList");
        eventURL = intent.getStringExtra("eventURL");
        priceRange = intent.getStringExtra("eventPrice");
        imageURL = intent.getStringExtra("eventImage");
        eventName = intent.getStringExtra("eventName");


        nameEvent.setText(eventDetail);
        nameURL.setText(eventURL);
        namePrice.setText(priceRange);
        nameImageURL.setText(imageURL);

        //click on add to favorite button and send event to the fevorite activity
        addFav.setOnClickListener(v -> {
            AddData(eventName);
            Intent i = new Intent(EventDetail.this,FevoriteEvents.class);
            i.putExtra("eventName", eventSaveName);
            startActivity(i);

        });
    }

    //This method is Used for add data
    public void AddData(String eventName) {
        boolean inseartData = myDB.addData(eventName);

        if (inseartData == true) {
            Toast.makeText(this, "Event Saved", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.event_detail_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.event_detail_home:
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                return true;
            case R.id.event_detail_help:
                builder.setMessage(R.string.help_event_detail)
                        .setCancelable(true)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                AlertDialog alert = builder.create();
                //Setting the title manually
                alert.setTitle(R.string.help_title);
                alert.show();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

}