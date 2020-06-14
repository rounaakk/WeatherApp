package com.rounak.weather;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androdocs.httprequest.HttpRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private Button mButton;

    String CITY = "kolkata,in";
    String API = "8118ed6ee68db2debfaaa5a44c832918";

    TextView addressTxt, updated_atTxt, statusTxt, tempTxt, temp_minTxt, temp_maxTxt, sunriseTxt,
            sunsetTxt, windTxt, pressureTxt, humidityTxt;
    int count;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mButton = findViewById(R.id.button_send);
        count=0;


        addressTxt = findViewById(R.id.address);
        tempTxt = findViewById(R.id.temp);
        temp_minTxt = findViewById(R.id.temp_min);
        temp_maxTxt = findViewById(R.id.temp_max);
        statusTxt = findViewById(R.id.status);
        updated_atTxt = findViewById(R.id.updated_at);
        sunriseTxt = findViewById(R.id.sunrise);
        sunsetTxt = findViewById(R.id.sunset);
        windTxt = findViewById(R.id.wind);
        pressureTxt = findViewById(R.id.pressure);
        humidityTxt = findViewById(R.id.humidity);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
//                TextView tv = (TextView)findViewById(R.id.address);
                EditText city_edit = (EditText)findViewById(R.id.city);
                CITY = city_edit.getText().toString();

                new weatherTask().execute();
            }
        });



    }

    class weatherTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        protected String doInBackground(String... args) {
            if(count==1){
                String response = HttpRequest.excuteGet("https://api.openweathermap.org/data/2.5/forecast/daily?q=" + CITY + "&cnt="+"2"+"&appid"+API);
                return response;

            }

            String response = HttpRequest.excuteGet("https://api.openweathermap.org/data/2.5/weather?q=" + CITY + "&units=metric&appid=" + API);
            return response;
        }

        @Override
        protected void onPostExecute(String result) {

            if(count==1){

                try {
                    JSONObject jsonObj = new JSONObject(result);
                    JSONObject list = jsonObj.getJSONArray("list").getJSONObject(1);
                    Long sunrise = list.getLong("sunrise");
                    Long sunset = list.getLong("sunset");
                    String windSpeed = list.getString("speed");
                    String pressure = jsonObj.getString("pressure");
                    String humidity = jsonObj.getString("humidity");

                    JSONObject tempObj = list.getJSONObject("temp");
                    String tempMin = "Min Temp: " + String.valueOf(tempObj.getLong("min")-273.15) + "°C";
                    String tempMax = "Max Temp: " + String.valueOf(tempObj.getLong("max")-273.15) + "°C";
                    String temp = String.valueOf(tempObj.getLong("temp")-273.15) + "°C";

                    Long updatedAt = list.getLong("dt");
                    String updatedAtText = "Updated at: " + new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH).format(new Date(updatedAt * 1000));

                    JSONObject weather = list.getJSONObject("weather");
                    String weatherDescription = weather.getString("description");

                    JSONObject city = jsonObj.getJSONObject("city");
                    String address = city.getString("name");


                    /* Populating extracted data into our views */
                    addressTxt.setText(address);
                    updated_atTxt.setText(updatedAtText);
                    statusTxt.setText(weatherDescription.toUpperCase());
                    tempTxt.setText(temp);
                    temp_minTxt.setText(tempMin);
                    temp_maxTxt.setText(tempMax);
                    sunriseTxt.setText(new SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(new Date(sunrise * 1000)));
                    sunsetTxt.setText(new SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(new Date(sunset * 1000)));
                    windTxt.setText(windSpeed);
                    pressureTxt.setText(pressure);
                    humidityTxt.setText(humidity);

                    /* Views populated, Hiding the loader, Showing the main design */
//                findViewById(R.id.loader).setVisibility(View.GONE);
//                findViewById(R.id.mainContainer).setVisibility(View.VISIBLE);


                } catch (JSONException e) {
//                findViewById(R.id.loader).setVisibility(View.GONE);
//                findViewById(R.id.errorText).setVisibility(View.VISIBLE);
                    Toast.makeText(MainActivity.this, "City Not Found tommorow", Toast.LENGTH_SHORT).show();
                }
                count=0;
            }

            else{try {
                JSONObject jsonObj = new JSONObject(result);
                JSONObject main = jsonObj.getJSONObject("main");
                JSONObject sys = jsonObj.getJSONObject("sys");
                JSONObject wind = jsonObj.getJSONObject("wind");
                JSONObject weather = jsonObj.getJSONArray("weather").getJSONObject(0);
//
                Long updatedAt = jsonObj.getLong("dt");
                String updatedAtText = "Updated at: " + new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH).format(new Date(updatedAt * 1000));
                String temp = main.getString("temp") + "°C";
                String tempMin = "Min Temp: " + main.getString("temp_min") + "°C";
                String tempMax = "Max Temp: " + main.getString("temp_max") + "°C";
                String pressure = main.getString("pressure");
                String humidity = main.getString("humidity");
//
                Long sunrise = sys.getLong("sunrise");
                Long sunset = sys.getLong("sunset");
                String windSpeed = wind.getString("speed");
                String weatherDescription = weather.getString("description");
//
                String address = jsonObj.getString("name");


                /* Populating extracted data into our views */
                addressTxt.setText(address);
                updated_atTxt.setText(updatedAtText);
                statusTxt.setText(weatherDescription.toUpperCase());
                tempTxt.setText(temp);
                temp_minTxt.setText(tempMin);
                temp_maxTxt.setText(tempMax);
                sunriseTxt.setText(new SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(new Date(sunrise * 1000)));
                sunsetTxt.setText(new SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(new Date(sunset * 1000)));
                windTxt.setText(windSpeed);
                pressureTxt.setText(pressure);
                humidityTxt.setText(humidity);

                /* Views populated, Hiding the loader, Showing the main design */
//                findViewById(R.id.loader).setVisibility(View.GONE);
//                findViewById(R.id.mainContainer).setVisibility(View.VISIBLE);


            } catch (JSONException e) {
//                findViewById(R.id.loader).setVisibility(View.GONE);
//                findViewById(R.id.errorText).setVisibility(View.VISIBLE);
                Toast.makeText(MainActivity.this, "City Not Found", Toast.LENGTH_SHORT).show();
            }}

        }
    }
}
