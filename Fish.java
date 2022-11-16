import java.awt.Image;

public class Fish extends Swimmer
{
  public Fish(int x, int y, int w, int h, int s)
  {
    super(x, y, w, h, s);
  }

  @Override
  public Image getImage() {
    if (super.isGoingLeft()) {
      return ImageManager.loadImage("Assets/fishL.png");
    }
    else {
      return ImageManager.loadImage("Assets/fishR.png");
    }
  }

  public String toString()
  {
    return super.toString() + " " + getSpeed();
  }
}
