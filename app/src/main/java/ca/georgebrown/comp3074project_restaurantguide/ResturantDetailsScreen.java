package ca.georgebrown.comp3074project_restaurantguide;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import ca.georgebrown.comp3074project_restaurantguide.database.AppDatabase;
import ca.georgebrown.comp3074project_restaurantguide.model.Restaurant;

public class ResturantDetailsScreen extends AppCompatActivity {
    private AppDatabase db;
    private EditText rname,raddress,rdescription,rtag,rphone;
    private RatingBar rratingbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resturant_details_screen);

        SharedPreferences selectedTemp = getSharedPreferences("selected", Context.MODE_PRIVATE);
        final String name = selectedTemp.getString("resturantSelected", "");
        db = AppDatabase.getInstance(this);
        final Restaurant restaurant = db.restaurantDao().findByName(name);

        ImageButton email = findViewById(R.id.btnEmailShare);
        ImageButton facebook = findViewById(R.id.btnFacebookShare);
        ImageButton twitter = findViewById(R.id.btnTwitterShare);
        ImageButton direction = findViewById(R.id.btnDirections);
        ImageButton fullmapview = findViewById(R.id.btnFullMapView);
        rname = findViewById(R.id.txtResturantName);
        raddress = findViewById(R.id.txtRestuantAddress);
        rdescription = findViewById(R.id.txtResturantDescription);
        rtag = findViewById(R.id.txtResturantTag);
        rphone = findViewById(R.id.txtResturantPhone);
        rratingbar = findViewById(R.id.rbDetailsRatingBar);
        Button delete = findViewById(R.id.btnDelete);

        Button saveEdit = findViewById(R.id.btnSaveEdits);

        rname.setText(restaurant.getName());
        raddress.setText(restaurant.getAddress());
        rphone.setText(restaurant.getPhone());
        rdescription.setText(restaurant.getDescription());
        rtag.setText(restaurant.getTags());
        rratingbar.setRating(restaurant.getRating());
        email.setOnClickListener(v -> {
            String message = restaurant.toString();
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("text/plain");
            share.putExtra(Intent.EXTRA_TEXT, message);

            startActivity(Intent.createChooser(share, "Share Via"));
            Toast.makeText(ResturantDetailsScreen.this,"Email clicked!", Toast.LENGTH_LONG).show();
        });

        facebook.setOnClickListener(v -> {
            Intent tweet = new Intent(Intent.ACTION_SEND);
            tweet.putExtra(Intent.EXTRA_TEXT, "Sample test for Twitter.");
            startActivity(Intent.createChooser(tweet, "Share this via"));
            Toast.makeText(ResturantDetailsScreen.this,"Facebook clicked!", Toast.LENGTH_LONG).show();
        });

        twitter.setOnClickListener(v -> {
            String message = restaurant.toString();
            Intent tweet = new Intent(Intent.ACTION_VIEW);
            tweet.setData(Uri.parse("http://twitter.com/?status=" + Uri.encode(message)));
            startActivity(tweet);
            Toast.makeText(ResturantDetailsScreen.this,"Twitter clicked!", Toast.LENGTH_LONG).show();
        });

        direction.setOnClickListener(v -> {
            Uri intentUri = Uri.parse("https://www.google.com/maps/dir/?api=1&destination=" + Uri.encode(""+restaurant.getAddress()));
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, intentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            startActivity(mapIntent);

        });

        fullmapview.setOnClickListener(v -> {
            Uri intentUri = Uri.parse("https://www.google.com/maps/search/?api=1&query=" + Uri.encode(""+restaurant.getAddress()));
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, intentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            startActivity(mapIntent);
        });

        saveEdit.setOnClickListener(v -> {
            db = AppDatabase.getInstance(ResturantDetailsScreen.this);
            SharedPreferences selectedTemp12 = getSharedPreferences("selected", Context.MODE_PRIVATE);
            final String namee = selectedTemp12.getString("resturantSelected", "");
            db.restaurantDao().updateResturant(
                    rname.getText().toString(),
                    raddress.getText().toString(),
                    rdescription.getText().toString(),
                    rtag.getText().toString(),
                    rphone.getText().toString(),
                    rratingbar.getRating(),
                    namee

            );

            Toast.makeText(ResturantDetailsScreen.this, "Changes Saved!", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(ResturantDetailsScreen.this, ResturantList.class);
            startActivity(intent);
            finish();

        });
        delete.setOnClickListener(v -> {
            db = AppDatabase.getInstance(ResturantDetailsScreen.this);
            SharedPreferences selectedTemp1 = getSharedPreferences("selected", Context.MODE_PRIVATE);
            final String namee = selectedTemp1.getString("resturantSelected", "");

            db.restaurantDao().deleteResturant(namee);
            Toast.makeText(ResturantDetailsScreen.this, "Restuarant Deleted!", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(ResturantDetailsScreen.this, ResturantList.class);
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
