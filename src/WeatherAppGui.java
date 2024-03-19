import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

import org.json.simple.JSONObject;

import assets.WeatherApp;

public class WeatherAppGui extends JFrame{

private JSONObject weatherData;

public  WeatherAppGui(){
    // setup my app gui 
    super( "WeatherApp" );

    // set logo
    ImageIcon logo = new ImageIcon("src/assets/cloudy.png");
    setIconImage(logo.getImage());
    
    // set size off out gui
    setSize(450,650);

    // open the gui at the center of the screen
    setLocationRelativeTo(null);

    // end's program when gui close
    setDefaultCloseOperation(EXIT_ON_CLOSE);

    // manually set ui
    setLayout(null);

    setResizable(false);

    addGuiComponents();

}

private void addGuiComponents(){

    // search bar
    JTextField searchBar = new JTextField();

    // location and size of searchbar
    searchBar.setBounds(15,15,350,45);

    //  searchbar font style 
    searchBar.setFont(new Font("Dialog",Font.PLAIN,24));
 
   add(searchBar);

    

    //  weather image
    JLabel weatherImage = new JLabel(loadImage(("src/assets/cloudy.png")));
    weatherImage.setBounds(0, 125, 450, 200);
    add(weatherImage);

    // temperature text
    JLabel temperature = new JLabel("10 C");
    temperature.setBounds(0,350,450,54);
    temperature.setFont(new Font("Dialog",Font.BOLD,48));
    temperature.setHorizontalAlignment(SwingConstants.CENTER);
    add(temperature);

    // weather condition description
    JLabel weatherCondition = new JLabel("cloudly");
    weatherCondition.setBounds(0, 400, 450, 45);
    weatherCondition.setFont(new Font("Dialog",Font.PLAIN,32));
    weatherCondition.setHorizontalAlignment(SwingConstants.CENTER);
    add(weatherCondition);

    // humidity section
    JLabel humidity = new JLabel(loadImage("src/assets/humidity.png"));
    humidity.setBounds(15,500,74,66);
    add(humidity);
    JLabel humidityText = new JLabel("<html><b>Humidity:</b> <p>100%</p> </html>");
    humidityText.setBounds(90,500,85,55);
    humidityText.setFont(new Font("Dialog", Font.PLAIN,16));
    add(humidityText);

    // windspeed section
    JLabel windSpeed = new JLabel(loadImage("src/assets/windspeed.png"));
    windSpeed.setBounds(220,500,75,65);
    add(windSpeed);
    JLabel windSpeedText = new JLabel("<html><b>Windspeed:</b> <p>15km</p>/h</html>");
    windSpeedText.setBounds(310,500,85,55);
    windSpeed.setFont(new Font("Dialog", Font.PLAIN,16));
    add(windSpeedText);

    //search button
     JButton searchButton = new JButton(loadImage("src/assets/search.png"))  ;  

    // change coursor on hovering the search button
    searchButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    searchButton.setBounds(375,13,47,45);
    searchButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e){
            // take the location from the user
            String userInput = searchBar.getText();
         
            // validate input
            if(userInput.replaceAll("\\s", "").length()<=0){
                return;
            }

            // retrieve the weatherdata
            weatherData = WeatherApp.getWeatherData(userInput);

            // update the gui 

            // update the weather image
            String weatherConditions = (String) weatherData.get("weather_condition");
            switch (weatherConditions) {
                case "Clear":
                    weatherImage.setIcon(loadImage("src/assets/clear.png"));
                    break;
                
                case "Cloudy":
                    weatherImage.setIcon(loadImage("src/assets/cloudy.png"));
                    break;
                
                case "Rain":
                    weatherImage.setIcon(loadImage("src/assets/rain.png"));
                    break;
               
                case "Snow":
                    weatherImage.setIcon(loadImage("src/assets/snow.png"));
                    break;
            }

            // update temperature text
            double temperaturex = (double) weatherData.get("temperature");
            temperature.setText(temperaturex + "C");
            temperature.setBounds(0,350,450,54);

            // update weathercondition text
            weatherCondition.setText(weatherConditions);

            long humidityx = (long) weatherData.get("humidity");
            humidityText.setText("<html><b>Humidity:</b>"+ humidityx + "%</html>");

            double windSpeedx = (double) weatherData.get("windspeed");
            windSpeedText.setText("<html><b>Windspeed:</b>" + windSpeedx + "<p>Km/h</p></html>");
        }
    });
    add(searchButton);
}

 private ImageIcon loadImage(String resourcePath){
    try{
        // read the image from a given path
        BufferedImage image = ImageIO.read(new File(resourcePath));
        // now return the image to render
        return new ImageIcon(image); 
    }
    catch (IOException e){
        e.printStackTrace();
    }
    System.out.println("Couldn't find the image");
    return null;

 }
}