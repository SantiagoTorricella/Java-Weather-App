import javax.swing.*;

public class WeatherAppGui extends JFrame{

    public static void main(String[] args) {
        
    }

   public  WeatherAppGui(){
    // setup my app gui 
    super( "WeatherApp" );
    
    // set size off out gui
    setSize(450,650);

    // open the gui at the center of the screen
    setLocationRelativeTo(null);

    // end's program when gui close
    setDefaultCloseOperation(EXIT_ON_CLOSE);

    
}


}