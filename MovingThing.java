import java.awt.Graphics;

public abstract class MovingThing implements Moveable, Collideable<MovingThing>
{
  private int xPos;
  private int yPos;
  private int width;
  private int height;
  private int speed;

  public MovingThing(int x, int y, int w, int h, int s)
  {
    xPos = x;
    yPos = y;
    width = w;
    height = h;
    speed = s;
  }
  

  public void setPos( int x, int y)
  {
    xPos = x;
    yPos = y;
  }

  public void setX(int x)
  {
    xPos = x;
  }

  public void setY(int y)
  {
    yPos = y;
  }


  public int getX()
  {
    return xPos;
  }

  public int getY()
  {
    return yPos;
  }

  public void setWidth(int w)
  {
    width = w;
  }

  public void setHeight(int h)
  {
    height = h;
  }

  public int getWidth()
  {
    return width;
  }

  public int getHeight()
  {
    return height;
  }

  
  public void setSpeed(int s) {
    speed = s;
  } 

  public int getSpeed() {
    return speed;
  }

  public boolean didCollide(MovingThing other) {
    int thisX1 = this.getX();
    int thisX2 = this.getX() + this.getWidth();
    int thisY1 = this.getY();
    int thisY2 = this.getY() + this.getHeight();
    int otherX1 = other.getX();
    int otherX2 = other.getX() + other.getWidth();
    int otherY1 = other.getY();
    int otherY2 = other.getY() + other.getHeight();
    
    return ((thisX1 < otherX1 && otherX1 < thisX2) ||
            (thisX1 < otherX2 && otherX2 < thisX2)) &&
            ((thisY1 < otherY1 && otherY1 < thisY2) ||
            (thisY1 < otherY2 && otherY2 < thisY2));
  }

  public abstract void move(String direction);
  public abstract void draw(Graphics window);

  public String toString()
  {
    return getX() + " " + getY() + " " + getWidth() + " " + getHeight();
  }
}
