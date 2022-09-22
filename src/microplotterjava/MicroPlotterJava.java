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
        
        try {
            // Setting theme
        UIManager.setLookAndFeel(
            UIManager.getSystemLookAndFeelClassName());
        } 
        catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
           // Booo
        }
        
        MicroPlotterJava();
    }
    
    public static void MicroPlotterJava(){
        Layout LYT = new Layout(990, 730);
    }
}