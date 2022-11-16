import java.awt.Image;

public class SwimPenguin extends Swimmer
{
  
  
  public SwimPenguin(int x, int y, int w, int h, int s)
  {
    super(x, y, w, h, s);
  }

  @Override
  public void hit() {
    super.hit();
    setSpeed(3);
    updateImage();
  }

  @Override
  public Image getImage() {
    if (isGoingLeft()) {
      if (isHit()) {
        return ImageManager.loadImage("Assets/swimPenguinHitL.png");
      }
      return ImageManager.loadImage("Assets/swimPenguinL.png");
    }
    else {
      if (isHit()) {
        return ImageManager.loadImage("Assets/swimPenguinHitR.png");
      }
      return ImageManager.loadImage("Assets/swimPenguinR.png");
    }
  }

  public String toString()
  {
    return super.toString() + " " + getSpeed();
  }
}
