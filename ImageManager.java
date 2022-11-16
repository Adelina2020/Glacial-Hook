import java.net.URL;
import java.awt.Image;
import javax.imageio.ImageIO;

public class ImageManager
  {
    // Returns the image specified by the fileName
    public static Image loadImage(String fileName) {
      Image img = null;
      try {
          URL url = ImageManager.class.getResource(fileName);
          img = ImageIO.read(url);
        }
        catch(Exception e) {
          System.out.println(fileName + " not found");
        }
      return img;
    }
  }