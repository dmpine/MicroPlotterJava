package microplotter;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
*
* @author danielpineda
*/
public class Main {
    public static void main(String[] args) {
        System.out.println("Starting MicroPlotter...");
        
        try {
            // Setting theme
        UIManager.setLookAndFeel(
            UIManager.getSystemLookAndFeelClassName());
        } 
        catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
           // Booo
        }
        
        MicroPlotter();
        
    }
    
    public static void MicroPlotter(){
        Layout LYT = new Layout(990, 730);
    }
}