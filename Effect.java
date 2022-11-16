import java.awt.Graphics;
import java.awt.Image;

public class Effect
  {
    private int xPos;
    private int yPos;
    private Image image;
    private int length;
    private int frame;

    public Effect(int x, int y, Image img, int len) {
      xPos = x;
      yPos = y;
      image = img;
      length = len;
      frame = 1;
    }

    public void draw(Graphics window) {
      window.drawImage(image, xPos, yPos, image.getWidth(null), image.getHeight(null), null);
    }

    public void updateFrame() {
      frame++;
    }

    public boolean isOver() {
      return frame >= length;
    }
  }