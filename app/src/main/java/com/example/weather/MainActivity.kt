package com.example.weather

import android.annotation.SuppressLint
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import com.example.weather.databinding.ActivityMainBinding
import org.json.JSONObject
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    var CITY:String = "dhaka"
    val API:String = "9359de0daeeeb9edfbef5708cc26da6d"
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.searchButton.setOnClickListener {
            if (binding.searchCity.text.toString().isNotEmpty())
                CITY = binding.searchCity.text.toString().trim()
            else
                CITY = "dhaka"

            weatherTask().execute()
        }
        weatherTask().execute()
    }
    inner class weatherTask(): AsyncTask<String, Void, String>()
    {
        override fun onPreExecute() {
            super.onPreExecute()
            binding.progressBar.visibility = VISIBLE
            binding.mainContainer.visibility = GONE
            binding.errorMassage.visibility = GONE

        }

        override fun doInBackground(vararg p0: String?): String? {
            var response:String?

            try{
                response = URL("https://api.openweathermap.org/data/2.5/weather?q=$CITY&appid=9359de0daeeeb9edfbef5708cc26da6d").readText(Charsets.UTF_8)

            }catch (e: Exception){
                response = null
            }
            return response
        }

        @SuppressLint("SetTextI18n")
        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            try {
                val jsonObjects = JSONObject(result)
                val main = jsonObjects.getJSONObject("main")
                val sys = jsonObjects.getJSONObject("sys")
                val wind = jsonObjects.getJSONObject("wind")
                val weather = jsonObjects.getJSONArray("weather").getJSONObject(0)
                val updateAt:Long = jsonObjects.getLong("dt")
                val updateText = SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH).format(Date(updateAt*1000))
                val temp = main.getDouble("temp") - 273.15
                val minTemp = (main.getDouble("temp_min") - 273.15)
                val maxTemp = (main.getDouble("temp_max") - 273.15)
                val realFeel = (main.getDouble("feels_like") - 273.15)
                val pressure = main.getString("pressure")
                val humidity = main.getString("humidity")
                val visibility = jsonObjects.getString("visibility")
                val country = sys.getString("country")
                val sunrise = sys.getLong("sunrise")
                val sunset = sys.getLong("sunset")
                val windSpeed = wind.getString("speed")
                val weatherDescription = weather.getString("description")
                val address = jsonObjects.getString("name")+","+country


                binding.address.text = address
                binding.updateAt.text = updateText
                binding.status.text = weatherDescription
                binding.temp.text = String.format("%.1f",temp)+"°C "
                binding.minTemp.text = "Min "+String.format("%.1f",minTemp)+"°C "
                binding.maxTemp.text = "Max "+String.format("%.1f",maxTemp)+"°C "
                binding.feeLike.text = "Feels "+String.format("%.1f",realFeel)+"°C "
                binding.sunriseTime.text = SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(sunrise*1000))
                binding.sunsetTime.text = SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(sunset*1000))
                binding.wind.text = windSpeed
                binding.pressure.text = pressure
                binding.humidity.text = humidity
                binding.visibility.text = visibility


                binding.mainContainer.visibility = VISIBLE
                binding.progressBar.visibility = GONE

            }catch (e: Exception)
            {
                binding.progressBar.visibility = GONE
                binding.errorMassage.visibility = VISIBLE
            }


        }
    }
}