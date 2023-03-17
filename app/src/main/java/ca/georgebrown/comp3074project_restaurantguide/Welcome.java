package ca.georgebrown.comp3074project_restaurantguide;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import ca.georgebrown.comp3074project_restaurantguide.database.AppDatabase;
import ca.georgebrown.comp3074project_restaurantguide.model.Restaurant;

public class Welcome extends AppCompatActivity {

    private AppDatabase db;
    private EditText aName,aAddress,aPhone,aDescription;
    private RatingBar aRating;
    private Spinner spinnerTags;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        db = AppDatabase.getInstance(this);
        Button gotolist = findViewById(R.id.btnLoadData);
        Button btnAdd = findViewById(R.id.btnAdd);
        aName = findViewById(R.id.txtAddName);
        aAddress = findViewById(R.id.txtRestuantAddress);
        aPhone = findViewById(R.id.txtAddPhone);
        aRating = findViewById(R.id.rbResturant);
        aDescription = findViewById(R.id.txtAddDescription);
        spinnerTags = findViewById(R.id.spinnerResturantTags);

        SharedPreferences preferences = getSharedPreferences("myPreferences", MODE_PRIVATE);
        String userName = preferences.getString("name", null);
        TextView name = findViewById(R.id.txtWelcome);

        if(userName != null) {
            name.setText("Welcome " + userName);
        }

        Toast.makeText(this,"Welcome "+ userName, Toast.LENGTH_LONG).show();

        Spinner spinner = findViewById(R.id.spinnerResturantTags);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(Welcome.this,R.array.resturant_tags, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


        //First ask if there are already data in the database, if not, import data
        int restaurantCount = db.restaurantDao().countRestaurants();
        if (restaurantCount == 0){
            //insert into the database
            db.restaurantDao().insertAll(new Restaurant(
                    "Kothur Indian Cuisine",
                    "2403 Lake Shore Blvd W, Etobicoke, ON M8V 1C5",
                    "Best indian food with so many varieties if indian food.",
                    "indian",
                    "+14162535047",
                    4.5f));
            db.restaurantDao().insertAll(new Restaurant(
                    "Tandoori Flame Brampton",
                    "8150 Dixie Rd, Brampton, ON L6T 5N9",
                    "If you crave for indian food, then this is the best restaurant in the town, with all cuisines of indian food.",
                    "indian",
                    "+19054511686",
                    4.0f));
            db.restaurantDao().insertAll(new Restaurant(
                    "Lahore Grill",
                    "1274 Gerrard St E, Toronto, ON M4L 1Y6",
                    "Authentic and tasty Pakistani food.",
                    "asian",
                    "+14167789785",
                    5.0f));
            db.restaurantDao().insertAll(new Restaurant(
                    "Chipotle Mexican Grill",
                    "300 Borough Dr, Scarborough, ON M1P 4P5",
                    "If you want fresh mexican food, then this place is for you.",
                    "mexican",
                    "+16473484750",
                    3.0f));

            Log.i("DATABASE-TEST","OnCreate: data inserted");
            Toast.makeText(this, "onCreate: inserted data", Toast.LENGTH_LONG).show();

        } else {
            Log.i("DATABASE-TEST","onCreate: data already exists");
            Toast.makeText(this, "onCreate: data already exists", Toast.LENGTH_LONG).show();
        }

        btnAdd.setOnClickListener(v -> {
            String addName,addAddress,addDescription,addTag,addPhone;
            float addRating;

            addName = aName.getText().toString();
            addAddress = aAddress.getText().toString();
            addDescription = aDescription.getText().toString();
            addTag = spinnerTags.getSelectedItem().toString();
            addPhone = aPhone.getText().toString();
            addRating = aRating.getRating();

            db.restaurantDao().insertAll(new Restaurant(
                    addName,
                    addAddress,
                    addDescription,
                    addTag,
                    addPhone,
                    addRating
            ));
            Toast.makeText(Welcome.this, "Added to database!", Toast.LENGTH_LONG).show();

            aName.setText("");
            aAddress.setText("");
            aDescription.setText("");
            aPhone.setText("");

        });

        gotolist.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), ResturantList.class);
            startActivity(intent);
            finish();
        });
    }

    protected void onDestroy() {
        AppDatabase.destroyInstance();
        super.onDestroy();
    }

    @Override
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
                return true;

            case R.id.itemAboutUs:
                startActivity(new Intent(this, AboutUs.class));
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }

    }

}
