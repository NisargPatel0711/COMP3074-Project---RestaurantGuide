package ca.georgebrown.comp3074project_restaurantguide;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import ca.georgebrown.comp3074project_restaurantguide.database.AppDatabase;
import ca.georgebrown.comp3074project_restaurantguide.model.Restaurant;

public class TagResturantList extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_resturant_list);

        AppDatabase db = AppDatabase.getInstance(this);
        ArrayList<String> restaurantNames = new ArrayList<>();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String tag = preferences.getString("Tag", "");

        //Get all restaurants
        List<Restaurant> restaurantList = db.restaurantDao().tagFilter(tag);


        for(Restaurant restaurant :restaurantList){
            restaurantNames.add(restaurant.getName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, restaurantNames);

        ListView listView = findViewById(R.id.listRestuarantTagList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            String selectionItem = (String)parent.getItemAtPosition(position);

            //Add selected resturant to Preferences
            SharedPreferences selectedTemp = getSharedPreferences("selected", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = selectedTemp.edit();
            editor.putString("resturantSelected", selectionItem);
            editor.apply();

            Intent intent = new Intent(view.getContext(), ResturantDetailsScreen.class);
            startActivity(intent);
            finish();
        });
    }
}
