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

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.List;

import pro.miri.models.WeatherInfo;

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
        String API_URL = "http://api.openweathermap.org/data/2.5/weather?q={city_name}&units=metric&appid={API_key}";
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
                                JsonObject coord = result.get("coord").getAsJsonObject();
                                double lon = coord.get("lon").getAsDouble();
                                double lat = coord.get("lat").getAsDouble();


                                // set to view
                                // F->??C : ??C= Math.round((F-273.15))
                                mainTemperator.setText(String.valueOf(temp + " ??C"));
                                mainLocation.setText(cityName+", "+country);
                                mainDate.setText(description);

                                loadDailyForecast(lon, lat);
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
    private void loadDailyForecast(double lon, double lat){
        String URL = "https://api.openweathermap.org/data/2.5/onecall?lat={lat}&lon={lon}&exclude=hourly,minutely,current&units=metric&appid={APP_KEY}";
        String COMPLETE_URL = URL.replace("{lon}", String.valueOf(lon))
                .replace("{lat}", String.valueOf(lat))
                .replace("{APP_KEY}", API_KEY);
        Log.d("URL", COMPLETE_URL);
        Ion.with(this)
                .load(COMPLETE_URL)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if (e != null){ //if there is an error
                            e.printStackTrace();
                            Toast.makeText(MainActivity.this, "Error during retrieve daily forecast data, please try again!", Toast.LENGTH_SHORT).show();

                        }else{
                            try {
                                // retrieve data and add it to listView
                                //Log.d("Res: ", result.toString());
                                // Load data to the ListView
                                List<WeatherInfo> weatherInfoList = new ArrayList<>();
                                String timeZone = result.get("timezone").getAsString();
                                JsonArray dailyWeatherInfo = result.get("daily").getAsJsonArray();

                                for (int i=1; i< dailyWeatherInfo.size(); i++){
                                    JsonObject weatherInfoItem = dailyWeatherInfo.get(i).getAsJsonObject();
                                    long dt = weatherInfoItem.get("dt").getAsLong();
                                    double temp = (weatherInfoItem.get("temp").getAsJsonObject()).get("day").getAsDouble();
                                    String icon = ((weatherInfoItem.get("weather").getAsJsonArray()).get(0).getAsJsonObject()).get("icon").getAsString();
                                    weatherInfoList.add(new WeatherInfo(dt, timeZone, temp, icon));
                                }
                                // attach adapter to listview
                                DailyForecastAdapter adapter = new DailyForecastAdapter(MainActivity.this, weatherInfoList);
                                weekWeatherListView.setAdapter(adapter);

                            }
                            catch (Exception ex){
                                ex.printStackTrace();
                                Toast.makeText(MainActivity.this, "Error city not found", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });


    }
}