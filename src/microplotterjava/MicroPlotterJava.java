/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package microplotterjava;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;




/**
 *
 * @author danielpineda
 */
public class MicroPlotterJava {

    public static void main(String[] args) {
        //UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        try {
            // Set System L&F
        UIManager.setLookAndFeel(
            UIManager.getSystemLookAndFeelClassName());
        } 
        catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
           // handle exception
        }
        // handle exception
        // handle exception
        // handle exception
        
        MicroPlotterJava();
    }
    
    public static void MicroPlotterJava(){
        Layout LYT = new Layout(870, 725);
    }
}