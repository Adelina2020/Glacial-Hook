import java.awt.Color;
import java.awt.Graphics;

public class Ammo extends MovingThing
{
  private int speed;

  public Ammo()
  {
    this(0,0,0);
  }

  public Ammo(int x, int y)
  {
    super(x, y, 20, 20, 0);
  }

  public Ammo(int x, int y, int s)
  {
    super(x, y, 20, 20, s);
  }

  public Ammo(int x, int y, int w, int h, int s) {
    super(x, y, w, h, s);
  }

  public void move(String direction) {
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
  
  public void draw(Graphics window)
  {
    window.setColor(Color.WHITE);
    window.fillRect(getX(), getY(), getWidth(), getHeight());
    window.setColor(Color.BLACK);
  }

  public String toString()
  {
    return super.toString() + " " + speed;
  }
}
