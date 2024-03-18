// return weather data from an API, the GUI will dusplay it to the user.

package assets;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class WeatherApp {
    // fetch weather data from the searched location
    public static JSONObject getWeatherData(String locationName){

        // get location coordinates
        JSONArray locationData = getLocationData(locationName);

        // take latitude and longitude of our location
        JSONObject location = (JSONObject) locationData.get(0);
        double latitude = (double) location.get("latitude");
        double longitude = (double) location.get("longitude");

        return null;
    } 

    public static JSONArray getLocationData(String locationName){
        // replace withesapces to adhere to api's request format
        locationName.replaceAll(" ", "+");

        // creating API url with location
        String urlString = "https://geocoding-api.open-meteo.com/v1/search?name="
       + locationName + "&count=10&language=en&format=json";

        try {
            // call API
        HttpURLConnection connect = fetchApiResponse(urlString);

            // check response status
            if( connect.getResponseCode() != 200){
                System.out.println("Failed to connect!");
                return null;
            }

            else{
                // mutable string
                StringBuilder  resultsJson = new StringBuilder();
                Scanner scanner = new Scanner(connect.getInputStream());
                
                while (scanner.hasNext()) {
                    resultsJson.append(scanner.nextLine());                    
                }

                scanner.close();

                connect.disconnect();

                JSONParser parser = new JSONParser();

                JSONObject resultsJsonObj = (JSONObject) parser.parse(String.valueOf(resultsJson));

                JSONArray locationData = (JSONArray) resultsJsonObj.get("results");

                return locationData ;
            }

            
        } catch (Exception e) {
            e.printStackTrace();
        } 
        return null;
        

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
