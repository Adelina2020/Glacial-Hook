import java.awt.Image;
import java.awt.Graphics;

public class PenguinAmmo extends Ammo
{
  private Image image;
  
  public PenguinAmmo(int x, int y, int s) {
    super(x, y, 12, 34, s);
    image = ImageManager.loadImage("Assets/hook.png");
  }

  @Override
  public void move(String direction) {
    super.move("DOWN");
  }

  @Override
  public void draw(Graphics window) {
    window.drawImage(image,getX(),getY(),getWidth(),getHeight(),null);
  }
}