// return weather data from an API, the GUI will display it to the user.

package assets;

import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class WeatherApp {
    // fetch weather data from the searched location
    @SuppressWarnings("unchecked")
    public static JSONObject getWeatherData(String locationName){

        // get location coordinates
        JSONArray locationData = getLocationData(locationName);
        
        // take latitude and longitude of our location
        JSONObject location = (JSONObject) locationData.get(0);
        double latitude = (double) location.get("latitude");
        double longitude = (double) location.get("longitude");

        // build api url request with location coordinates
        String urlString = "https://api.open-meteo.com/v1/forecast?" +
        "latitude=" + latitude + "&longitude=" + longitude +
        "&hourly=temperature_2m,relative_humidity_2m,weather_code,wind_speed_10m";

        try {
            // call API
            HttpURLConnection connect = fetchApiResponse(urlString);

            if( connect.getResponseCode() != 200){
                System.out.println("Failed to connect!");
            }

            StringBuilder resultsJson = new StringBuilder();
            Scanner scanner = new Scanner(connect.getInputStream());

            while (scanner.hasNext()) {
                resultsJson.append(scanner.nextLine());
            }

            scanner.close();
            connect.disconnect();

            JSONParser parser = new JSONParser();
            JSONObject resluJsonObject = (JSONObject) parser.parse(String.valueOf(resultsJson));


            JSONObject hourly = (JSONObject) resluJsonObject.get("hourly");

            // get index of our current hour
            JSONArray time = (JSONArray) hourly.get("time");
            int index = findIndexOfCurrenTime(time);

            // get temperature
            JSONArray temperatureArray = (JSONArray) hourly.get("temperature_2m");
            double temperature = (double) temperatureArray.get(index);
            // weather code
            
            JSONArray weatherCode = (JSONArray) hourly.get("weather_code");
            String weatherCondition = convertWeatherCode((long) weatherCode.get(index));
            
            // get humidity
            JSONArray relativeHumidity = (JSONArray) hourly.get("relative_humidity_2m");
            long humidity = (long) relativeHumidity.get(index);

            // windspeed
            JSONArray windSpeedData = (JSONArray) hourly.get("wind_speed_10m");
            double windspeed = (double) windSpeedData.get(index);

            // create the json object that we use on the frontend
            JSONObject weatherData = new JSONObject();
            weatherData.put("temperature", temperature);
            weatherData.put("weather_condition", weatherCondition);
            weatherData.put("humidity", humidity);
            weatherData.put("windspeed", windspeed);
            
            return weatherData;

        } catch (Exception e) {
            e.printStackTrace();
        }


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

    private static int findIndexOfCurrenTime(JSONArray timeList){
        
        String currentTime = getCurrentTime();

        for(int i=0 ; i < timeList.size(); i++ ){
            String time = (String) timeList.get(i);
            if(time.equalsIgnoreCase(currentTime));
            return i ;
        }
        return 0;

    }

    public static String getCurrentTime(){
    
        LocalDateTime currentDateTime = LocalDateTime.now();
        
        // format date so the API can read it properly
        DateTimeFormatter dateFormated = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH':00'");
        String formatedDateAndTime = currentDateTime.format(dateFormated);

        return formatedDateAndTime;
    }

    private static String convertWeatherCode (long weatherCode){
        
        String weatherCondition = "";

        if (weatherCode == 0L) {
            weatherCondition = "Clear";
        }
        else if(weatherCode > 0L && weatherCode <= 3L){
            weatherCondition = "Cloudy";
        }
        else if((weatherCode >= 51L && weatherCode <= 67L) || (weatherCode >= 80L && weatherCode <= 99L)) {
            weatherCondition = "Rain";
        }
        else if(weatherCode >= 71L && weatherCode <= 77L){
            weatherCondition = "Snow";
        }
        return weatherCondition;
    }


}
