import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;

public class Penguin extends MovingThing
{
  private Image image;

  public Penguin()
  {
    this(0,0,30,30,0);
  }

  public Penguin(int x, int y, int w, int h, int s)
  {
    super(x, y, w, h, s);
    image = ImageManager.loadImage("Assets/penguin.png");
  }

  public void move(String direction)
  {
    if (direction.equals("UP")){
      setY(getY()-getSpeed());
    } else if (direction.equals("DOWN")){
      setY(getY()+getSpeed());
    } else if (direction.equals("LEFT")){
      setX(getX()-getSpeed());
    } else if (direction.equals("RIGHT")){
      setX(getX()+getSpeed());
    }
  }

  public void draw( Graphics window )
  { window.drawImage(image,getX(),getY(),getWidth(),getHeight(),null);
  }

  public int calcHits(List<Ammo> shots) {
    int count = 0;
    for (int i = 0; i < shots.size(); i++) {
      if (shots.size() == 0) {
        return count;
      }
      if (this.didCollide(shots.get(i))) {
        shots.remove(i);
        i--;
        if (i < 0) {
          i = 0;
        }
        count++;
      }
    }
    return count;
  }

  public String toString()
  {
    return super.toString() + " " + getSpeed();
  }

  public void moveAndDraw(Graphics window) {
    
  }
}
