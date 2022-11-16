public class Wave extends Display
{
  private int wave;

  public Wave(int x, int y, int s) {
    super(x, y, s);
    wave = 0;
  }

  public void setWave(int w) {
    wave = w;
  }

  public int getWave() {
    return wave;
  }

  public void incWave() {
    wave++;
  }

  @Override
  public String getText() {
    return "Wave: " + wave;
  }
}