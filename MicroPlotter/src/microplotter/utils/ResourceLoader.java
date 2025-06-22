package microplotter.utils;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;

public class ResourceLoader {
    
    public static Image loadImage(String imageName) {
        try {
            InputStream stream = ResourceLoader.class.getClassLoader()
                .getResourceAsStream("" + imageName);
            if (stream != null) {
                return ImageIO.read(stream);
            }
        } catch (IOException e) {
            System.err.println("Could not load image: " + imageName);
        }
        return null;
    }
    
    public static ImageIcon loadIcon(String imageName) {
        try {
            InputStream stream = ResourceLoader.class.getClassLoader()
                .getResourceAsStream("" + imageName);
            if (stream != null) {
                return new ImageIcon(ImageIO.read(stream));
            }
        } catch (IOException e) {
            System.err.println("Could not load icon: " + imageName);
        }
        return null;
    }
}
