public class Score extends Display
{
  private int score;

  public Score(int x, int y, int s) {
    super(x, y, s);
    score = 0;
  }

  public void setScore(int s) {
    score = s;
  }

  public int getScore() {
    return score;
  }

  public void addPoints(int points) {
    score += points;
  }

  public void removePoints(int points) {
    score -= points;
  }

  @Override
  public String getText() {
    return "Score: " + score;
  }
}