package microplotter.utils;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;

/**
 * @brief A static utility class for loading application resources.
 * @details This class provides methods to load resources, such as images and icons,
 * from the application's classpath. This approach ensures that resources are found
 * regardless of the application's current working directory, making the deployment
 * more robust.
 */
public class ResourceLoader {

    /**
     * @brief Loads an image from the classpath.
     * @details This method finds a resource by its name, opens it as an input stream,
     * and reads it into an `Image` object. This is suitable for setting frame icons.
     * @param imageName The name of the image file located in the resources path (e.g., "my_icon.png").
     * @return The loaded `Image` object, or `null` if the resource cannot be found or an error occurs.
     */
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

    /**
     * @brief Loads an image from the classpath as a Swing ImageIcon.
     * @details This method finds a resource by its name, opens it as an input stream,
     * and reads it into an `ImageIcon` object. This is suitable for use directly
     * in Swing components like JLabels or JButtons.
     * @param imageName The name of the image file located in the resources path (e.g., "my_icon.png").
     * @return The loaded `ImageIcon` object, or `null` if the resource cannot be found or an error occurs.
     */
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