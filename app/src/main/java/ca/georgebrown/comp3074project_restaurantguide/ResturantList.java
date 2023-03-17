package ca.georgebrown.comp3074project_restaurantguide;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import ca.georgebrown.comp3074project_restaurantguide.database.AppDatabase;
import ca.georgebrown.comp3074project_restaurantguide.model.Restaurant;

public class ResturantList extends AppCompatActivity {

    private ArrayAdapter<String> adapter;
    private EditText tagSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resturant_list);

        EditText search = findViewById(R.id.txtSearch);
        Button btnSearchTag = findViewById(R.id.btnSearchTag);

        tagSearch = findViewById(R.id.txtSearchTag);

        AppDatabase db = AppDatabase.getInstance(this);
        ArrayList<String> restaurantNames = new ArrayList<>();

        //Get all restaurants
        List<Restaurant> restaurantList = db.restaurantDao().getAll();

        for(Restaurant restaurant :restaurantList){
            restaurantNames.add(restaurant.getName());
        }

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, restaurantNames);

        ListView listView = findViewById(R.id.listResturants);
        listView.setAdapter(adapter);

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                (ResturantList.this).adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

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

        btnSearchTag.setOnClickListener(v -> {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ResturantList.this);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("Tag",""+tagSearch.getText().toString());
            editor.apply();
            Intent intent = new Intent(v.getContext(), TagResturantList.class);
            startActivity(intent);
            finish();
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_layout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.itemAddResturant:
                startActivity(new Intent(this, Welcome.class));
                finish();
                return true;

            case R.id.itemResturantList:
                startActivity(new Intent(this, ResturantList.class));
                finish();
                return true;

            case R.id.itemAboutUs:
                startActivity(new Intent(this, AboutUs.class));
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }
}
