package com.example.ticketmaster;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    //Initialize Variables
    DataBaseHelperForSearchBar myDB;

    ListView eventList;
    private TextView searchCity, searchRadius;
    private Button serchBtn;

    //For Creating a Progress bar
    ProgressBar progressBar;
    AlertDialog.Builder builder;


    //For create items in menu
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.language:
                changeLanguage();
                return true;

            case R.id.help:
                builder.setMessage(R.string.help_home).setCancelable(true).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
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

    //This method is created for changinf a language in application
    private void changeLanguage() {

        final String[] listItems = {"English-Us", "English-UK", "French"};

        //this alertdialog is used for choosing a language
        AlertDialog.Builder mbuilder = new AlertDialog.Builder(this);
        mbuilder.setTitle("Choose language..");
        mbuilder.setSingleChoiceItems(listItems, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                if(i == 0) {
                    //English Us
                    setLocale("us");
                    recreate();
                }
                else if(i == 1) {
                    //English Uk
                    setLocale("uk");
                    recreate();
                }
                else if(i == 2) {
                    //French
                    setLocale("fr");
                    recreate();
                }

                //Dismiss aleart dialog when language selected
                dialog.dismiss();
            }
        });

        AlertDialog mDialog =  mbuilder.create();
        //show Alert Dialog
        mDialog.show();

    }

    //A Locale object logically consists of the fields like languages, script, country, variant, extensions.
    private void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getBaseContext().getResources().updateConfiguration(configuration, getBaseContext().getResources().getDisplayMetrics());
        //Save data to shared praferences
        SharedPreferences.Editor editor = getSharedPreferences("Settings", MODE_PRIVATE).edit();
        editor.putString("My_Lang", lang);
        editor.apply();
    }

    //load language saved in shared preference
    public void loadLocale() {
        SharedPreferences preferences = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language = preferences.getString("My_Lang", "");
        setLocale(language);
    }

    //for crating a menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);
        return true;
    }

    //When Application run this metthod is first called
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale();
        setContentView(R.layout.activity_main);

        //For initialize databse using database halper
        myDB = new DataBaseHelperForSearchBar(this);

        Cursor data = myDB.getSearchListContents();

        progressBar = findViewById(R.id.progressbar);
        progressBar.setVisibility(View.GONE);
        builder = new AlertDialog.Builder(this);

        final ArrayList<String> cities = new ArrayList<>();

        eventList = findViewById(R.id.citiesList);
        searchCity = findViewById(R.id.search_city);
        searchRadius = findViewById(R.id.search_radius);
        serchBtn = findViewById(R.id.search_btn);

        serchBtn.setOnClickListener(v -> {
            progressBar.setVisibility(View.VISIBLE);
            Snackbar snackBar = Snackbar .make(v, "An Error Occurred!", Snackbar.LENGTH_LONG) .setAction("RETRY", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                }
            });
            snackBar.setActionTextColor(Color.BLUE);

            snackBar.show();
            String city = searchCity.getText().toString();
            String radius = searchRadius.getText().toString();
            AddData(city);

            Intent i = new Intent(getApplicationContext(), EventListAPI.class);
            i.putExtra("city", city);
            i.putExtra("radius",radius);
            startActivity(i);

        });

        if(data.getCount() == 0) {
            Toast.makeText(this, "Serach City...", Toast.LENGTH_SHORT).show();
        } else {
            while (data.moveToNext()) {
                cities.add(data.getString(1));
                ListAdapter listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, cities);
                eventList.setAdapter(listAdapter);
            }
        }

    }

    //AddData method id used for storig a search cities in database
    public void AddData(String city) {
        boolean inseartData = myDB.addSearchData(city);

        if (inseartData == true) {
            Toast.makeText(this, "searching...", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
        }
    }
}