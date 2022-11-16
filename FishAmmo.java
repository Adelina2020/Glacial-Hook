import java.awt.Image;
import java.awt.Graphics;

public class FishAmmo extends Ammo
{
  private Image image;
  
  public FishAmmo(int x, int y, int s)
  {
    super(x, y, s);
    image = ImageManager.loadImage("Assets/bubble.png");
  }
  
  @Override
  public void move(String direction){
    super.move("UP");
  } 

  @Override
  public void draw(Graphics window) {
    window.drawImage(image,getX(),getY(),getWidth(),getHeight(),null);
  }
}