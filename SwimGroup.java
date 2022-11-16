import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

public class SwimGroup
{
  private List<Swimmer> swimmers;
  private List<Swimmer> hitSwimmers;

  public SwimGroup()
  {
    swimmers = new ArrayList<Swimmer>();
    hitSwimmers = new ArrayList<Swimmer>();
  }

  public void add(Swimmer s)
  {
    swimmers.add(s);
  }
  
  public void clearSwimmers()
  {
    swimmers.clear();
  }

  public void draw(Graphics window)
  {
    for (Swimmer s : swimmers) {
      s.draw(window);
    }
  }


  public void move()
  {
    for (int i = 0; i < swimmers.size(); i++) {
      Swimmer s = swimmers.get(i);
      if (s.getX() + s.getWidth() > 600 || s.getX() < 0) {
        
        s.switchDirection();
        s.setY(s.getY() - 20);
      }
      if (s.isGoingLeft()) {
        s.move("LEFT");
      }
      else {
        s.move("RIGHT");
      }
    }

    for (int i = 0; i < swimmers.size(); i++) {
      if (swimmers.size() == 0) {
        return;
      }
      if (swimmers.get(i).getY() < 120 ||
         ((swimmers.get(i).getX() + swimmers.get(i).getWidth() > 600 ||
         swimmers.get(i).getX() < 0))
         && swimmers.get(i).isHit()) {
        swimmers.remove(i);
        i--;
        if (i < 0) {
          i = 0;
        }
      }
    }
  }

  // calulate if swimmers are hit by shots, remove the swimmer and the shot
  public int calcHits(List<Ammo> shots)
  {
    hitSwimmers.clear();
    int count = 0;
    
    for (int i = 0; i < swimmers.size(); i++) {
      for (int j = 0; j < shots.size(); j++) {
        
        if (swimmers.size() == 0 || shots.size() == 0) {
          return count;
        }
        
        if (swimmers.get(i).didCollide(shots.get(j))) {
          swimmers.get(i).hit();
          hitSwimmers.add(swimmers.get(i));
          swimmers.remove(i);
          shots.remove(j);
          i--;
          if (i < 0) {
            i = 0;
          }
          j--;
          count++;
        }
      }
    }
    return count;
  }

  public List<Swimmer> getList() {
    return swimmers;
  }

  public List<Swimmer> getHitList() {
    return hitSwimmers;
  }

  public String toString()
  {
    return "";
  }
}