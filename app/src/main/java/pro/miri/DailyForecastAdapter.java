package pro.miri;

import android.content.Context;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.koushikdutta.ion.Ion;

import java.sql.Date;

import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import pro.miri.models.WeatherInfo;

public class DailyForecastAdapter extends ArrayAdapter<WeatherInfo> {
    private Context context;
    private List<WeatherInfo> weatherInfoList;

    public DailyForecastAdapter(@NonNull Context context, @NonNull List<WeatherInfo> weatherInfoList) {
        super(context, 0, weatherInfoList);
        this.context = context;
        this.weatherInfoList = weatherInfoList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // we will convert our layout xml to a view
        convertView = LayoutInflater.from(context).inflate(R.layout.daily_weather_item, parent, false);
        // now we have access to all our components
        TextView date = convertView.findViewById(R.id.dailyForecastDate);
        TextView temp = convertView.findViewById(R.id.dailyForecastTemp);
        ImageView icon = convertView.findViewById(R.id.dailyForecastIcon);

        WeatherInfo current = this.weatherInfoList.get(position);

        Date dateFormatted = new Date(current.getDate()*1000);
        /*
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, MMM yy", Locale.ENGLISH);
        dateFormat.setTimeZone(TimeZone.getTimeZone(current.getTimeZone()));

         */
        date.setText(dateFormatted.toString());

        temp.setText(String.valueOf(current.getTemperature()).concat(" °C"));
        loadIconToImageView(icon, current.getIcon());

        //String ICON_URL = "http://openweathermap.org/img/wn/{iconName}@2x.png";
        //Ion.with(getContext()).load(ICON_URL.replace("{iconName}", current.getIcon())).intoImageView(icon);

        return convertView;

    }

    private void loadIconToImageView(ImageView iconImageView, String iconName){
        // other Ion alternatives : GLIDE, PECASSO
        String ICON_URL = "http://openweathermap.org/img/wn/{iconName}@2x.png";
        Ion.with(getContext()).load(ICON_URL.replace("{iconName}", iconName)).intoImageView(iconImageView);

    }
}
