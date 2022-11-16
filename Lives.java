public class Lives extends Display
{
  private int lives;

  public Lives(int x, int y, int l) {
    super(x, y, l);
    lives = 3;
  }

  public void setLives(int l) {
    lives = l;
  }

  public int getLives() {
    return lives;
  }

  public void loseLives(int lostLives) {
    lives -= lostLives;
  }

  @Override
  public String getText() {
    return "Lives: " + lives;
  }
}