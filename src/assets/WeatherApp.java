// return weather data from an API, the GUI will dusplay it to the user.

package assets;

import java.net.HttpURLConnection;
import java.net.URL;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class WeatherApp {
    // fetch weather data from the searched location
    public static JSONObject getWeatherData(String locationName){

        // get location coordinates
        JSONArray locationData = getLocationData(locationName);
    } 

    private static JSONArray getLocationData(String locationName){
        // replace withesapces to adhere to api's request format
        locationName.replaceAll(" ", "+");

        // creating API url with location
        String urlString = "https://geocoding-api.open-meteo.com/v1/search?name="
       + locationName + "&count=10&language=en&format=json";

        try {
            // call API
        HttpURLConnection connect = fetchApiResponse(urlString);
            
        } catch (Exception e) {
            e.printStackTrace();
        } 


    }
    private static HttpURLConnection fetchApiResponse(String urlString){
    try {

        // connecto to the API
        @SuppressWarnings("deprecation")
        URL url = new URL(urlString);
        HttpURLConnection connect = (HttpURLConnection) url.openConnection();

        // set request method
        connect.setRequestMethod("GET");
        connect.connect();
        return connect;

    } catch (Exception e) {
        e.printStackTrace();
    }
    return null;
    }
}
