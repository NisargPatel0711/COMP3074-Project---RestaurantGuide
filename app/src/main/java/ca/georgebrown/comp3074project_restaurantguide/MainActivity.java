package ca.georgebrown.comp3074project_restaurantguide;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = findViewById(R.id.btn_login);
        button.setOnClickListener(v -> {
            SharedPreferences.Editor editor = getSharedPreferences("myPreferences", MODE_PRIVATE).edit();
            EditText name = findViewById(R.id.txtUserName);
            editor.putString("name", name.getText().toString());
            editor.apply();
            Intent intent = new Intent(MainActivity.this, Welcome.class);
            startActivity(intent);
            finish();
        });
    }
}
