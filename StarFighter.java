import javax.swing.JFrame;
import java.awt.*;

public class StarFighter extends JFrame
{
  private static final int WIDTH = 610;
  private static final int HEIGHT = 430;
  
  public StarFighter()
  {
    super("Fishing Game");
    setSize(WIDTH,HEIGHT);

    Ocean theGame = new Ocean();
    ((Component)theGame).setFocusable(true);

    getContentPane().add(theGame);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setVisible(true);
  }

  public static void main( String args[] )
  {
    
    StarFighter run = new StarFighter();
  }
}
