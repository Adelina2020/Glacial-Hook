import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Canvas;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.awt.Image;

public class Ocean extends Canvas implements KeyListener, Runnable
{
  private final int WIDTH = 600;
  private final int HEIGHT = 400;

  // Declare game state trackers
  private boolean isPaused = true;
  private boolean isTitleScreen = true;
  private boolean isRuleScreen = false;
  private boolean isGameOver = false;

  // Declare game objects
  private Penguin penguin;
  private SwimGroup fishSchool;
  private SwimGroup pengGroup;
  private SwimGroup hitPengGroup;
  private Score score;
  private Lives lives;
  private Wave wave;
  private Shots penguinShots;
  private Shots fishShots;
  private EffectGroup effects;

  // Declare game variables
  private int difficulty;
  private int penguinCoolDown;
  private int pengSize = 60;
  private int startX = WIDTH/2 - pengSize/2;
  private int startY = 10;
  private int fishWidth = 40;
  private int fishHeight = 24;
  private int fishCD;
  private long lastPengShot;
  private long lastFishShot;
  private long lastLostLife;
  private int fishSpeed;
  private int bubbleFreq;
  private int swimPengSpeed;
  private int effectLength;

  // Declare others
  private boolean[] keys;
  private BufferedImage back;
  
  // Declare game images
  private Image oceanImage;
  private Image titleImage;
  private Image gameOverImage;
  private Image gainPointImage;
  private Image losePointImage;
  private Image loseLifeImage;
  private Image nextWaveImage;
  private Image rulesImage;
  
  public Ocean()
  {
    setBackground(Color.black);

    keys = new boolean[8];

    // Instantiate game objects
    penguin = new Penguin(startX, startY, pengSize, pengSize, 2);
    fishSchool = new SwimGroup();
    pengGroup = new SwimGroup();
    hitPengGroup = new SwimGroup();
    penguinShots = new Shots();
    fishShots = new Shots();
    effects = new EffectGroup();
    score = new Score(10, 30, 20);
    lives = new Lives(10, 60, 20);
    wave = new Wave(10, 90, 20);

    // Set game variables
    fishCD = getRandomCD();
    lastPengShot = 0;
    lastFishShot = 0;
    lastLostLife = 0;
    effectLength = 100;

    // Load all game images
    oceanImage = ImageManager.loadImage("Assets/ocean.png");
    titleImage = ImageManager.loadImage("Assets/titleScreen.png");
    rulesImage = ImageManager.loadImage("Assets/rulesScreen.png");
    gameOverImage = ImageManager.loadImage("Assets/gameOver.png");

    gainPointImage = ImageManager.loadImage("Assets/plusOne.png");
    losePointImage = ImageManager.loadImage("Assets/minusFive.png");
    loseLifeImage = ImageManager.loadImage("Assets/loseLife.png");
    nextWaveImage = ImageManager.loadImage("Assets/nextWaveScreen.png");

    this.addKeyListener(this);
    new Thread(this).start();

    setVisible(true);
  }

  public void update(Graphics window)
  {
    paint(window);
  }

  public void paint( Graphics window )
  {
    //set up the double buffering to make the game animation nice and smooth
    Graphics2D twoDGraph = (Graphics2D)window;

    //take a snap shop of the current screen and same it as an image
    //that is the exact same width and height as the current screen
    if (back==null)
      back = (BufferedImage)(createImage(getWidth(),getHeight()));

    //create a graphics reference to the back ground image
    //we will draw all changes on the background image
    Graphics graphToBack = back.createGraphics();

    // Checks if the game is on one of the initial screens
    if (isTitleScreen){
      drawTitleScreen(graphToBack);
    }
    if (isRuleScreen){
      drawRulesScreen(graphToBack);
    }

    // Runs if the main game is currently playing
    if (!isPaused && !isTitleScreen && !isGameOver) {
      // Start a new wave, spawn new fish and swimming penguins
      if (fishSchool.getList().size() == 0) {
        startWave(twoDGraph, back, graphToBack);
      }

      // Move all swim groups
      fishSchool.move();
      pengGroup.move();
      hitPengGroup.move();

      // Update shots and effects
      fishShoot();
      updateShots(penguinShots, graphToBack);
      updateShots(fishShots, graphToBack);
      effects.updateEffects();

      // Check player movement
      movePeng();
    }
    
    
    calcHits();

    // Ends the game if lives are 0
    if (lives.getLives() == 0) {
      drawAll(graphToBack);      
      drawGameOver(graphToBack);
      isGameOver = true;
    }

    // Resets the game after it ends
    if (isGameOver){
      if (keys[5]){
        resetGame();
      }
    }

    // When the main game is running, draw all game elements
    if (!isPaused) {
      drawAll(graphToBack);
    }
    
    toScreen(twoDGraph, back);
  }

  // Draws all items on the buffer image
  public void drawAll(Graphics window) {
    window.drawImage(oceanImage, 0, 0, WIDTH, HEIGHT, null);
    hitPengGroup.draw(window);
    penguinShots.draw(window);
    fishShots.draw(window);
    fishSchool.draw(window);
    pengGroup.draw(window);
    penguin.draw(window);
    score.draw(window);
    lives.draw(window);
    wave.draw(window);
    effects.draw(window);
  }

  // Draws the image to the screen
  public void toScreen(Graphics2D graph, BufferedImage b) {
    graph.drawImage(b, null, 0, 0);
  }

  // Updates the player's actions like moving and shooting
  public void movePeng() {
    if (keys[0] && penguin.getX()>0) {
      penguin.move("LEFT");
    }
    if (keys[1] && penguin.getX()<600-penguin.getWidth()-2) {
      penguin.move("RIGHT");
    }
    if (keys[2] && penguin.getY()>0) {
      penguin.move("UP");
    }
    if (keys[3] && penguin.getY()<120-penguin.getHeight()-30) {
      penguin.move("DOWN");
    }

    if (keys[4]) {
      pengShoot();
    }
  }

  // Makes the player shoot with a cooldown
  public void pengShoot() {
    long currTime = System.currentTimeMillis();
    if (currTime - lastPengShot > penguinCoolDown) {
      penguinShots.add(new PenguinAmmo(penguin.getX() + penguin.getWidth()/2 - 2, penguin.getY()+pengSize, 2));
      lastPengShot = currTime;
    }
  }

  // Makes the fish shoot at random intervals
  public void fishShoot() {
    if (fishSchool.getList().size() == 0) {
      return;
    }
    long currTime = System.currentTimeMillis();
    if (currTime - lastFishShot > fishCD*bubbleFreq) {
      int r = (int) (Math.random() * fishSchool.getList().size());
      Swimmer fish = fishSchool.getList().get(r);
      fishShots.add(new FishAmmo(fish.getX(), fish.getY(), 1));
      fishCD = getRandomCD();
      lastFishShot = currTime;
    }
  }

  // Returns a random cooldown for the fish shots
  public int getRandomCD() {
    return (int) (Math.random() * 5) + 1;
  }

  // Moves all shots on the screen
  public void updateShots(Shots shots, Graphics window) {
    shots.move("");
    shots.cleanUpEdges();
  }

  // Calculate all hits and update score and lives
  public void calcHits() {
    
    // Check if the player's shots hit fish
    int shotsHitFish =  fishSchool.calcHits(penguinShots.getList());
    if (shotsHitFish > 0) {
      score.addPoints(shotsHitFish);
      for (Swimmer sw : fishSchool.getHitList()) {
        effects.add(new Effect(sw.getX(), sw.getY(), gainPointImage, effectLength));
      }
    }
    
    // Check if the fish shots hit the player
    int shotsHitPeng = penguin.calcHits(fishShots.getList());
    if (shotsHitPeng > 0) {
      lives.loseLives(shotsHitPeng);
      effects.add(new Effect(penguin.getX() + 10, penguin.getY() + 10, loseLifeImage, effectLength));
    }

    // Check if the player touches a fish and reset position
    for (Swimmer s : fishSchool.getList()) {
      long currentTime = System.currentTimeMillis();
      if (penguin.didCollide(s) && currentTime - lastLostLife > 1000) {
        lastLostLife = currentTime;
        lives.loseLives(1);
        penguin.setPos(startX, startY);
        //isPaused = true;
      }
    }

    // Check if the player's shots hit a penguin
    int shotsHitPengs = pengGroup.calcHits(penguinShots.getList());
    if (shotsHitPengs > 0) {
      score.removePoints(shotsHitPengs * 5);
      for (Swimmer sw : pengGroup.getHitList()) {
        effects.add(new Effect(sw.getX(), sw.getY(), losePointImage, effectLength));
        hitPengGroup.add(sw);
        
      }
    }

    // Sets score and lives minimum to 0
    if (score.getScore() < 0) {
      score.setScore(0);
    }
    if (lives.getLives() < 0) {
      lives.setLives(0);
    }
  }

  // Starts a new wave
  public void startWave(Graphics2D graph, BufferedImage b, Graphics window) {
    wave.incWave();
    int numRows;
    int fishPerRow;
    int numPengs;

    // Calculate number of rows
    if (wave.getWave() <= 3) {
      numRows = 1;
    }
    else {
      numRows = 2;
    }

    // Calculate number of fish per row
    if (wave.getWave() == 1) {
      fishPerRow = 6;
    }
    else if (wave.getWave() == 2) {
      fishPerRow = 8;
    }
    else {
      fishPerRow = 10;
    }

    // Calculate number of penguins
    if (wave.getWave() == 1) {
      numPengs = 0;
    }
    else if (wave.getWave() == 2 || wave.getWave() == 3) {
      numPengs = 1;
    }
    else if (wave.getWave() == 4 || wave.getWave() == 5) {
      numPengs = 2;
    }
    else {
      numPengs = 3;
    }
    if (difficulty == 2){
      numPengs++;
    }

    // Pauses the game and displays a next wave display
    if (wave.getWave() > 1) {
      drawAll(window);
      window.drawImage(nextWaveImage, 75, 50, null);
      toScreen(graph, b);
      try {
        while(true) {
          Thread.currentThread().sleep(3000);
          break;
        }
      }
      catch (Exception e) {
      }
    }
    penguinShots.clearList();
    fishShots.clearList();
    effects.clear();
    makeFish(fishPerRow, numRows);
    makeSwimPengs(numPengs);
  }

  // Draws the title screen
  public void drawTitleScreen(Graphics window) {
    window.drawImage(titleImage,getX(),getY(),getWidth(),getHeight(),null);
    if (isTitleScreen){
      if (keys[6]){
        difficulty = 1;
        fishSpeed = 1;
        bubbleFreq = 500;
        penguinCoolDown = 350;
        isTitleScreen = false;
        isRuleScreen = true;
        drawRulesScreen(window);
      } else if (keys[7]){
        difficulty = 2;
        fishSpeed = 2;
        bubbleFreq = 300;
        penguinCoolDown = 550;
        isTitleScreen = false;
        isRuleScreen = true;
        drawRulesScreen(window);
      }
    }
  }

  // Draws the rules screen
  public void drawRulesScreen(Graphics window){
    window.drawImage(rulesImage,0,0,null);
    if (isRuleScreen){
      if (keys[5]) {
        isPaused = false;
        isRuleScreen = false;
      }
    }
  }

  // Draws the game over screen
  public void drawGameOver(Graphics window) {
    window.drawImage(gameOverImage,75,50,null);
    score.setPos(100, 230);
    score.draw(window);
    lives.setPos(100, 230);
    //lives.draw(window);
    wave.setPos(415, 230);
    wave.draw(window);
    isPaused = true;
  }

  // Resets the game
  public void resetGame() {
    score.setPos(10, 20);
    lives.setPos(10, 60);
    wave.setPos(10, 100);
    score.setScore(0);
    lives.setLives(3);
    penguinShots.clearList();
    fishShots.clearList();
    penguin.setPos(300-penguin.getWidth()/2, 10);
    fishSchool.clearSwimmers();
    wave.setWave(0);
    isTitleScreen = true;
    isPaused = true;
    isGameOver = false;
  }

  // Spawn a wave of fish 
  public void makeFish(int fishPerRow, int numRows) {
    int horBuff = (WIDTH - fishPerRow*fishWidth)/(fishPerRow+1);
    for (int i = 0; i < numRows; i++) {
      int yPos = HEIGHT - (30*(i+1) + fishHeight*i);
      for (int j = 0; j < fishPerRow; j++) {
        int xPos = horBuff*(j+1) + fishWidth*j;
        Fish fish = new Fish(xPos, yPos, fishWidth, fishHeight, fishSpeed);
        fishSchool.add(fish);
      }
    }
  } 

  // Spawn swimming penguins
  public void makeSwimPengs(int num) {
    for (int i = 0; i < num; i++) {
      int randY = (int) (Math.random() * 270) + 100;
      int randSpeed = (int) (Math.random() * 2) + 1;
      SwimPenguin swimPeng = new SwimPenguin(WIDTH-pengSize, randY, pengSize, pengSize, randSpeed);
      pengGroup.add(swimPeng);
    }
  }

  // Checks if a key is pressed and updates the array
  public void keyPressed(KeyEvent e)
  {
    if (e.getKeyCode() == KeyEvent.VK_LEFT)
    {
      keys[0] = true;
    }
    if (e.getKeyCode() == KeyEvent.VK_RIGHT)
    {
      keys[1] = true;
    }
    if (e.getKeyCode() == KeyEvent.VK_UP)
    {
      keys[2] = true;
    }
    if (e.getKeyCode() == KeyEvent.VK_DOWN)
    {
      keys[3] = true;
    }
    if (e.getKeyCode() == KeyEvent.VK_SPACE)
    {
      keys[4] = true;
    }
    if (e.getKeyCode() == KeyEvent.VK_R)
    {
      keys[5] = true;
    }
    if (e.getKeyCode() == KeyEvent.VK_1)
    {
      keys[6] = true;
    }
    if (e.getKeyCode() == KeyEvent.VK_2)
    {
      keys[7] = true;
    }
    repaint();
  }

  // Checks if a key is released and updates the array
  public void keyReleased(KeyEvent e)
  {
    if (e.getKeyCode() == KeyEvent.VK_LEFT)
    {
      keys[0] = false;
    }
    if (e.getKeyCode() == KeyEvent.VK_RIGHT)
    {
      keys[1] = false;
    }
    if (e.getKeyCode() == KeyEvent.VK_UP)
    {
      keys[2] = false;
    }
    if (e.getKeyCode() == KeyEvent.VK_DOWN)
    {
      keys[3] = false;
    }
    if (e.getKeyCode() == KeyEvent.VK_SPACE)
    {
      keys[4] = false;
    }
    if (e.getKeyCode() == KeyEvent.VK_R)
    {
      keys[5] = false;
    }
    if (e.getKeyCode() == KeyEvent.VK_1)
    {
      keys[6] = false;
    }
    if (e.getKeyCode() == KeyEvent.VK_2)
    {
      keys[7] = false;
    }
    repaint();
  }

  // I have no idea what this does
  public void keyTyped(KeyEvent e)
  {
    //no code needed here
  }

  public void run()
  {
    try
    {
      while(true)
      {
        Thread.currentThread().sleep(5);
        repaint();
      }
    }catch(Exception e)
    {
    }
  }
}

