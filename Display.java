import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;

public abstract class Display 
{
  private int xPos;
  private int yPos;
  private int fontSize;

  public Display(int x, int y, int s) {
    xPos = x;
    yPos = y;
    fontSize = s;
  }

  public void setPos(int x, int y){
    xPos = x;
    yPos = y;
  }

  public void draw(Graphics window) {
    window.setColor(Color.BLACK);
    window.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, fontSize));
    window.drawString(getText(), xPos, yPos);
  }

  public String getText() {
    return "";
  }
  
}