package pro.miri;

import androidx.appcompat.app.AlertDialog;
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

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

public class MainActivity extends AppCompatActivity {
    final String API_KEY = "3a213010f97dde28c42f410369f59136";

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
        Ion.with(this)
                .load(API_URL.replace("{city_name}", location).replace("{API_key}", API_KEY))
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        // do stuff with the result or error
                        //Log.d("Result: ", result.toString());
                        if(e != null){
                            e.printStackTrace();
                            Toast.makeText(MainActivity.this, "Error during retrieve data, please try again!", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            try {
                                //convert json response to java
                                JsonObject main = result.get("main").getAsJsonObject();
                                double temp = main.get("temp").getAsDouble();
                                JsonObject sys = result.get("sys").getAsJsonObject();
                                String country = sys.get("country").getAsString();
                                String cityName = result.get("name").getAsString();
                                JsonArray weather = result.get("weather").getAsJsonArray();
                                JsonObject weatherObj = weather.get(0).getAsJsonObject();
                                String description = weatherObj.get("description").getAsString();
                                String icon = weatherObj.get("icon").getAsString();



                                // set to view
                                mainTemperator.setText(String.valueOf(Math.round((temp-273.15))+ " °C"));
                                mainLocation.setText(cityName+", "+country);
                                mainDate.setText(description);

                                loadIconToImageView(icon);
                            }
                            catch (Exception ex){
                                ex.printStackTrace();
                                Toast.makeText(MainActivity.this, "Error: city not found.", Toast.LENGTH_SHORT).show();
                            }

                        }
                    }
                });
    }
    private void loadIconToImageView(String iconName){
        // other Ion alternatives : GLIDE, PECASSO
        String ICON_URL = "http://openweathermap.org/img/wn/{iconName}@2x.png";
        Ion.with(this).load(ICON_URL.replace("{iconName}", iconName)).intoImageView(iconImageView);

    }
}