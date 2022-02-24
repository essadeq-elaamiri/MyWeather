package pro.miri;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

public class MainActivity extends AppCompatActivity {
    Button searchBtn;
    EditText searchEditText;
    ImageView iconImageView;
    TextView mainTemperator;
    TextView mainLocation;
    TextView mainDate;
    ListView weekWeatherListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //hidding action bar
        //getSupportActionBar().hide();

        //get views
        searchBtn = findViewById(R.id.searchBtn);
        searchEditText = findViewById(R.id.searchEditText);
        iconImageView = findViewById(R.id.iconImageView);
        mainTemperator = findViewById(R.id.mainTemperator);
        mainLocation = findViewById(R.id.mainLocation);
        mainDate = findViewById(R.id.mainDate);
        weekWeatherListView = findViewById(R.id.weekWeatherListView);

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get city or location name
                String location = searchEditText.getText().toString().trim();
                if(location.isEmpty()){
                    Toast.makeText(MainActivity.this, "Please enter a valid city name !", Toast.LENGTH_SHORT).show();

                }else{
                    // TODO: loading weather data by city name
                    loadWeatherDataByCity(location);

                }

            }
        });


    }

    private void loadWeatherDataByCity(String location) {
        String API_URL = "http://api.openweathermap.org/data/2.5/weather?q={city_name}&appid={API_key}";
        String API_KEY = "3a213010f97dde28c42f410369f59136";
        Ion.with(this)
                .load(API_URL.replace("{city_name}", location).replace("{API_key}", API_KEY))
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        // do stuff with the result or error
                        Log.d("Result: ", result.toString());

                    }
                });
    }
}