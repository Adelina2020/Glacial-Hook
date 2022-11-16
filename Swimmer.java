import java.awt.Graphics;
import java.awt.Image;

public abstract class Swimmer extends MovingThing
{
  private Image image;
  private boolean isGoingLeft;
  private boolean isHit;

  public Swimmer(int x, int y, int w, int h, int s)
  {
    super(x, y, w, h, s);
    isGoingLeft = true;
    isHit = false;
    updateImage();
  }

  public void hit() {
    isHit = true;
  }

  public boolean isHit() {
    return isHit;
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

  public void switchDirection() {
    if (isGoingLeft) {
      isGoingLeft = false;
    }
    else {
      isGoingLeft = true;
    }
    updateImage();
  }

  public boolean isGoingLeft() {
    return isGoingLeft;
  }

  public void updateImage() {
    image = getImage();
  }

  public abstract Image getImage();

  public String toString()
  {
    return super.toString() + " " + getSpeed();
  }
}
