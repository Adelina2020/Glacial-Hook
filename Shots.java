import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

public class Shots
{
  private List<Ammo> ammo;

  public Shots()
  {
    ammo = new ArrayList<Ammo>();
  }

  public void add(Ammo al)
  {
    ammo.add(al);
  }

  //post - draw each Ammo
  public void draw(Graphics window)
  {
    for (Ammo a: ammo){
      a.draw(window);
    }
  }

  public void move(String direction)
  {
    for (Ammo a : ammo) {
      a.move("UP");
    }
  }
  
  // remove any Ammo which has reached the edge of the screen
  public void cleanUpEdges()
  {
    for (int i=0; i<ammo.size(); i++){
      if (ammo.get(i).getY() <= 0 || ammo.get(i).getY() >= 400){
        ammo.remove(i);
        i--;
      }
    }
  }

  public List<Ammo> getList()
  {
    return ammo;
  }

  public void clearList(){
    ammo.clear();
  }

  public String toString()
  {
    return "";
  }
}
 