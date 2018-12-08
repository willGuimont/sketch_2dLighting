import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class sketch_2dLighting extends PApplet {

boolean left = false;
boolean right = false;
boolean up = false;
boolean down = false;

public void keyPressed()
{
  switch(key)
  {
    case 'a':
      left = true;
      break;
    case 'd':
      right = true;
      break;
    case 'w':
      up = true;
      break;
    case 's':      
      down = true;
      break;
  }
}

public void keyReleased()
{
  switch(key)
  {
    case 'a':
      left = false;
      break;
    case 'd':
      right = false;
      break;
    case 'w':
      up = false;
      break;
    case 's':      
      down = false;
      break;
  }
}

Player player;
ArrayList<Obstacle> obstacles;
ArrayList<Bullet> bullets;
ArrayList<Enemy> enemies;
ArrayList<PowerUp> powerUp;

boolean hit = false;
boolean started = false;

int cooldownMain = 20;
int cooldownP = 20;
int t = 0;

int enemySpawn = 250;
int e = 0;

int powerSpawn = 300;
int p = 50;

int score = 0;
int ob = 20;

public void setup()
{
  
  player = new Player(width/2, height/2);
  
  obstacles = new ArrayList<Obstacle>();
  bullets = new ArrayList<Bullet>();
  enemies = new ArrayList<Enemy>();
  powerUp = new ArrayList<PowerUp>();
}

public void draw()
{
  if (hit)
  {
    background(255, 0, 0);
    hit = false;
    return;
  }
  
  if (!started)
  {
    background(0);
    textSize(30);
    text("Hold space\nand click to play...", width/2-150, height/2-150);
    return;
  }
  
  if (player.lives <= 0)
  {
    background(0);
    textSize(30);
    text("Game Over\nYour score: " + score + "\nHold space\nand click to respawn...", width/2-150, height/2-150);
    enemies.clear();
    return;
  }
  
  t++;
  e++;
  p++;
  if (e > enemySpawn && enemies.size() <= 10)
  {
    e = 0;
    Enemy enemy = spawnEnemy(player);
    if (enemy != null)
      enemies.add(enemy);
  }
  
  if (p > powerSpawn && powerUp.size() <= 50)
  {
    p = 0;
    float a = random(0, 1);
    PowerUp pu;
    if (a < 0.50f)
    {
      pu = new NoCooldownPowerUp((int)random(0, width), (int)random(0, height));
    }
    else if (a < 0.80f)
    {
      pu  = new InvinciblePowerUp((int)random(0, width), (int)random(0, height));
    }
    else
    {
      pu = new PlusLivePowerUp((int)random(0, width), (int)random(0, height));
    }
    powerUp.add(pu);
  }
  background(127);
  player.update();
  
  for (int i = 0; i < obstacles.size(); i++)
  {
    Obstacle current = obstacles.get(i);
    current.update();
    player.collision(current);
  }
  
  for (int i = 0; i < enemies.size(); i++)
  {
    Enemy current = enemies.get(i);
    current.update(bullets);
    for (int j = 0; j < obstacles.size(); j++)
    {
      current.collision(obstacles.get(j));
    }
    if (current.deathFlag)
    {
      enemies.remove(i);
      i--;
      score += 100;
    }
  }
  
  for (int i = 0; i < bullets.size(); i++)
  {
    Bullet current = bullets.get(i);
    current.update();
    current.collision(player);
    for (int j = 0; j < obstacles.size(); j++)
    {
      current.collision(obstacles.get(j));
    }
    for (int j = 0; j < enemies.size(); j++)
    {
      current.collision(enemies.get(j));
    }
    if (current.deathFlag)
    {
      bullets.remove(i);
      i--;
    }
  }
  
  for (int i = 0; i < powerUp.size(); i++)
  {
    PowerUp current = powerUp.get(i);
    current.update();
    current.collision(player);
    for (int j = 0; j < obstacles.size(); j++)
    {
      current.collision(obstacles.get(j));
    }
    if (current.deathFlag)
    {
      powerUp.remove(i);
      i--;
    }
  }
 
  
  for (int i = 0; i < bullets.size(); i++)
  {
    Bullet current = bullets.get(i); 
    current.draw();
  }
  
  for (int i = 0; i < enemies.size(); i++)
  {
    Enemy current = enemies.get(i); 
    current.draw();
  }
  
  for (int i = 0; i < powerUp.size(); i++)
  {
    PowerUp current = powerUp.get(i);
    current.draw();
  }
  
  player.draw();
  
  for (int i = 0; i < obstacles.size(); i++)
  {
    Obstacle current = obstacles.get(i);
    player.sight(current);
  }
 
  
 
  for (int i = 0; i < obstacles.size(); i++)
  {
    Obstacle current = obstacles.get(i); 
    current.draw();
  }  
 
  fill(255);
  stroke(255);
  textSize(30);
  text("Lives: " + player.lives, 10, 30);
  text("Score: " + score, 10, 60);
}

public void mousePressed()
{
  if ((player.lives <= 0 && key == ' ') || !started)
  {
    started = true;
    player.lives = 3;
    score = 0;
    obstacles.clear();
    
    obstacles.add(new Obstacle(0, 0, 10, height, color(0)));
    obstacles.add(new Obstacle(width-10, 0, 10, height, color(0)));
   
    obstacles.add(new Obstacle(0, 0, width, 10, color(0)));
    obstacles.add(new Obstacle(0, height - 10, width, 10, color(0)));
    for (int i = 0; i < ob; i++)
    {
      obstacles.add(new Obstacle((int)random(0, width), (int)random(0, height), (int)random(50, 150), (int)random(50, 150), color(random(0, 255), random(0, 255), random(0, 255))));
    }
  }
  if (t > cooldownMain)
  {
    t = 0;
    bullets.add(new Bullet(player.pos, player.lookDirection, true));
  }
}
class Bullet
{
  public Bullet(PVector pos, PVector dir, boolean fromPlayer)
  {
    this.pos = pos.copy();
    this.dir = dir.copy();
    this.dir = dir.normalize();
    this.dir.mult(speed);
    
    this.fromPlayer = fromPlayer;
  }
  
  public void update()
  {
    pos.add(dir);
  }
  
  public void draw()
  {
    blendMode(BLEND);
    fill(C);
    stroke(C);
    ellipse(pos.x, pos.y, size, size);
  }
  
  public void collision(Obstacle ob)
  {
    if (isCollidingCircleRectangle(
      pos.x,
      pos.y,
      size/2,
      ob.x,
      ob.y,
      ob.w,
      ob.h))
      {
        deathFlag = true;
      }
  }
  
  public void collision(Enemy en)
  {
    if (circleCircleCollision(pos.x, pos.y, size/2, en.pos.x, en.pos.y, en.size/2) && fromPlayer)
    {
      deathFlag = true;
      en.deathFlag = true;
    }
  }
  
  public void collision(Player pl)
  {
    if (circleCircleCollision(pos.x, pos.y, size/2, pl.pos.x, pl.pos.y, pl.size/2) && !fromPlayer)
    {
      deathFlag = true;
      if (!player.isInvincible)
        player.lives--;
      fill(255, 0, 0);
      stroke(255, 0, 0);
      hit = true;
    }
  }
  
  private PVector pos;
  private PVector dir;
  
  boolean deathFlag = false;
  int size = 10;
  int C = color(255, 0, 255);
  boolean fromPlayer;
  float speed = 10;
}
public boolean isCollidingCircleRectangle(
      float circleX,
      float circleY,
      float radius,
      float rectangleX,
      float rectangleY,
      float rectangleWidth,
      float rectangleHeight)
{
    float circleDistanceX = abs(circleX - rectangleX - rectangleWidth/2);
    float circleDistanceY = abs(circleY - rectangleY - rectangleHeight/2);
 
    if (circleDistanceX > (rectangleWidth/2 + radius)) { return false; }
    if (circleDistanceY > (rectangleHeight/2 + radius)) { return false; }
 
    if (circleDistanceX <= (rectangleWidth/2)) { return true; }
    if (circleDistanceY <= (rectangleHeight/2)) { return true; }
 
    float cornerDistance_sq = pow(circleDistanceX - rectangleWidth/2, 2) +
                         pow(circleDistanceY - rectangleHeight/2, 2);
 
    return (cornerDistance_sq <= pow(radius,2));
}

public boolean lineRectangleIntersect(float x1, float y1, float x2, float y2,
                               float rx, float ry, float rw, float rh) {
                                  
  float topIntersection;
  float bottomIntersection;
  float topPoint;
  float bottomPoint;
 
  // Calculate m and c for the equation for the line (y = mx+c)
  float m = (y2-y1) / (x2-x1);
  float c = y1 -(m*x1);
 
  // If the line is going up from right to left then the top intersect point is on the left
  if(m > 0) {
    topIntersection = (m*rx  + c);
    bottomIntersection = (m*(rx+rw)  + c);
  }
  // Otherwise it's on the right
  else {
    topIntersection = (m*(rx+rw)  + c);
    bottomIntersection = (m*rx  + c);
  }
 
  // Work out the top and bottom extents for the triangle
  if(y1 < y2) {
    topPoint = y1;
    bottomPoint = y2;
  } else {
    topPoint = y2;
    bottomPoint = y1;
  }
 
  float topOverlap;
  float botOverlap;
 
  // Calculate the overlap between those two bounds
  topOverlap = topIntersection > topPoint ? topIntersection : topPoint;
  botOverlap = bottomIntersection < bottomPoint ? bottomIntersection : bottomPoint;
 
  return (topOverlap<botOverlap) && (!((botOverlap<ry) || (topOverlap>ry+rh)));
 
}

public boolean circleCircleCollision(float x1, float y1, float radius1, float x2, float y2, float radius2)
{
  float dist = sqrt(pow(x2 - x1, 2) + pow(y2 - y1, 2));
  return dist < (radius1 + radius2);
}
public Enemy spawnEnemy(Player pl)
  {
    int watch_dog = 0;
    while(watch_dog < 100)
    {
      PVector pos = new PVector(random(0, width), random(0, height));
      if (sqrt(pow(pos.x - pl.pos.x, 2) + pow(pos.y - pl.pos.y, 2)) > 300)
      {
        return new Enemy((int)pos.x, (int)pos.y, pl);
      }
      watch_dog++;
    }
    return null;
  }

class Enemy
{ 
  public Enemy(int x, int y, Player pl)
  {
    toPlayer = new PVector();
    pos = new PVector(x, y);
    player = pl;
  }
  
  public void update(ArrayList<Bullet> list)
  {
    t++;
    PVector playerPos = player.pos.copy();
    toPlayer = playerPos.sub(pos).copy();
    toPlayer = toPlayer.normalize();
    if (sqrt(pow(pos.x - player.pos.x, 2) + pow(pos.y - player.pos.y, 2)) > 40)
      pos.add(toPlayer);
    if (t > cooldown)
    {
      t = 0;
      list.add(new Bullet(pos, toPlayer, false));
    }
  }
  
  public void draw()
  {
    blendMode(BLEND);
    fill(D);
    stroke(D);
    ellipse(pos.x, pos.y, size, size);
  }
  
  public boolean collision(Obstacle ob)
  {
    boolean state = isCollidingCircleRectangle(
      pos.x,
      pos.y,
      size,
      ob.x,
      ob.y,
      ob.w,
      ob.h);
      if (!state)
        return false;
        
      PVector rectCenter = new PVector(ob.x + 0.5f*ob.w, ob.y + 0.5f*ob.h);
        
      while (isCollidingCircleRectangle(
      pos.x,
      pos.y,
      size/2,
      ob.x,
      ob.y,
      ob.w,
      ob.h))
      {
        PVector dir = pos.copy();
        dir.sub(rectCenter);
        
        if (abs(pos.x - rectCenter.x) > ob.w/2)
          dir.y = 0;
        else 
          dir.x = 0;
        
        dir = dir.normalize();
        
        pos.add(dir);
      }
        
      return true;
  }
  
  private PVector pos;
  private int size = 20;
  private PVector toPlayer;
  private Player player;
  private int D = color(255, 0, 0);
  boolean deathFlag = false;
  int t = 0;
  int cooldown = 90;
}
class InvinciblePowerUp extends PowerUp
{
  public InvinciblePowerUp(int x, int y)
  {
    super(x, y);
    this.D = color(255, 255, 255);
    duration = 250;
  }
  
  public void power()
  {
    super.power();
    player.isInvincible = true;
    player.C = color(255, 255, 255);
  }
    
  public void undoPower()
  {
    super.undoPower();
    player.isInvincible = false;
    player.C = player.pC;
  }
}
public float cartesian2polarAngle (float x, float y){ 
  float angle = (atan2(y,x) * 180/PI); 
  return angle; 
} 
class NoCooldownPowerUp extends PowerUp
{
  public NoCooldownPowerUp(int x, int y)
  {
    super(x, y);
    previousCool = cooldownP;
    this.D = color(0, 127, 255);
    duration = 300;
  }
  
  public void power()
  {
    super.power();
    cooldownMain = 1;
    player.C = color(0, 127, 255);
  }
    
  public void undoPower()
  {
    super.undoPower();
    cooldownMain = previousCool;
    player.C = player.pC;
  }
  
  int previousCool;
}
class Obstacle
{
  public Obstacle(int x, int y, int w, int h, int c)
  {
    this.x = x;
    this.y = y;
    this.w = w;
    this.h = h;
    this.c = c;
  }
  
  public void update()
  {
  
  }
  
  public void draw()
  {
    blendMode(BLEND);
    fill(c);
    stroke(c);
    
    rect(x, y, w, h);
  }
  
  private int x;
  private int y;
  private int w;
  private int h;
  private int c;
}
class Player
{
  public Player(int x, int y)
  {
    pos = new PVector(x, y);
    lookDirection = new PVector();
  }
  
  public void update()
  {
    if (left)
      pos.x -= speed;
    else if (right)
      pos.x += speed;
      
    if (up)
      pos.y -= speed;
    else if (down)
      pos.y += speed;
    
    lookDirection = pos.copy();
    if (pos.x != mouseX || pos.y != mouseY)
    {
      lookDirection = new PVector(mouseX, mouseY);
      lookDirection.sub(pos);
      lookDirection = lookDirection.normalize();
    }
    
  }
  
  public void draw()
  {
    blendMode(BLEND);
    
    PVector[] polyPoint = new PVector[5];
    PVector left = (lookDirection.copy()).rotate(-HALF_PI/2);
    PVector right = (lookDirection.copy()).rotate(HALF_PI/2);
    
    polyPoint[0] = pos.copy();
    polyPoint[1] = new PVector(pos.x + 10000*left.x, pos.y + 10000*left.y);
    polyPoint[2] = new PVector(pos.x - 10000*right.x, pos.y - 10000*right.y);
    polyPoint[3] = new PVector(pos.x - 10000*left.x, pos.y - 10000*left.y);
    polyPoint[4] = new PVector(pos.x + 10000*right.x, pos.y + 10000*right.y);
    
    blendMode(REPLACE);
    fill(50, 127);
    noStroke();
    
    beginShape();
    
    for (int i = 0; i < 5; i++)
    {
      if (polyPoint[i] != null)
        vertex(polyPoint[i].x, polyPoint[i].y);
    }
    
    endShape();
    
    fill(C);
    stroke(C);
    ellipse(pos.x, pos.y, size, size);
    fill(255);
    stroke(255);
    line(pos.x, pos.y, pos.x + 15*lookDirection.x, pos.y + 15*lookDirection.y);
    
    fill(255, 0, 0);
    stroke(255, 0, 0);
    line(pos.x, pos.y, pos.x + 10000*lookDirection.x, pos.y + 10000*lookDirection.y);
    
    
  }
  
  public boolean collision(Obstacle ob)
  {
    boolean state = isCollidingCircleRectangle(
      pos.x,
      pos.y,
      size,
      ob.x,
      ob.y,
      ob.w,
      ob.h);
      if (!state)
        return false;
        
      PVector rectCenter = new PVector(ob.x + 0.5f*ob.w, ob.y + 0.5f*ob.h);
        
      while (isCollidingCircleRectangle(
      pos.x,
      pos.y,
      size/2,
      ob.x,
      ob.y,
      ob.w,
      ob.h))
      {
        PVector dir = pos.copy();
        dir.sub(rectCenter);
        
        if (abs(pos.x - rectCenter.x) > ob.w/2)
          dir.y = 0;
        else 
          dir.x = 0;
        
        dir = dir.normalize();
        
        pos.add(dir);
      }
        
      return true;
  }
  
  public void sight(Obstacle ob)
  {
    PVector[] corners = new PVector[4];
    PVector[] directorVector = new PVector[4];
    PVector[] polyPoint = new PVector[8];
    PVector point = pos.copy();
    boolean[] lineCollision = new boolean[4];
    
    PVector TL = new PVector(ob.x, ob.y);
    PVector TR = new PVector(ob.x + ob.w, ob.y);
    PVector BL = new PVector(ob.x, ob.y + ob.h);
    PVector BR = new PVector(ob.x + ob.w, ob.y + ob.h);
    
    corners[0] = TL;
    corners[1] = TR;
    corners[2] = BL;
    corners[3] = BR;
    int acc = 0;
    for (int i = 0; i < 4; i++)
    {
      directorVector[i] = corners[i].copy();
      directorVector[i].sub(point);
      
      if (pos.y == ob.y || pos.y == ob.y + ob.h)
        point.y++;
        
      if (pos.x == ob.x || pos.x == ob.x + ob.w)
        point.x++;
      
      directorVector[i] = corners[i].copy();
      directorVector[i].sub(point);
      
      float x1 = point.x;
      float y1 = point.y;
      
      float x2 = point.x + 10000*directorVector[i].x;
      float y2 = point.y + 10000*directorVector[i].y;
      
      fill(200, 50);
      stroke(200, 50);
      if (!lineRectangleIntersect(x1, y1, x2, y2, ob.x+1, ob.y+1, ob.w-2, ob.h-2))
      {
        lineCollision[i] = true;
        //line(x1, y1, x2, y2);
        if (!(i == 3 && lineCollision[0]) || !(i == 1 && lineCollision[2]))
        {
          polyPoint[acc] = corners[i].copy();
          acc++;
          polyPoint[acc] = new PVector(x2, y2);
          acc++;
        }
      }
    }
    
    blendMode(REPLACE);
    fill(50, 127);
    noStroke();
    
    beginShape(QUAD_STRIP);
    
    for (int i = 0; i < acc; i++)
    {
      if (polyPoint[i] != null)
        vertex(polyPoint[i].x, polyPoint[i].y);
    }
    
    endShape();
    
    //boolean lineRectangleIntersect(float x1, float y1, float x2, float y2, float rx, float ry, float rw, float rh)
    
  }
  
  private PVector pos;
  private int size = 20;
  private int speed = 2;
  private PVector lookDirection;
  int pC = color(255, 0, 255);
  private int C = pC;
  int lives = 0;
  boolean isInvincible = false;
}
class PlusLivePowerUp extends PowerUp
{
  public PlusLivePowerUp(int x, int y)
  {
    super(x, y);
    this.D = color(0, 255, 0);
    duration = 10;
  }
  
  public void power()
  {
    super.power();
    player.lives++;
    player.C = color(0, 255, 0);
  }
    
  public void undoPower()
  {
    super.undoPower();
    player.C = player.pC;
  }
}
class PowerUp
{
  public PowerUp(int x, int y)
    {
      pos = new PVector(x, y);
    }
    
    public void power()
    {}
    
    public void undoPower()
    {}
    
    public void apply()
    {
      taken = true;
      this.power();
    }
    
    public void unapply()
    {
      deathFlag = true;
      this.undoPower();
    }
    
    public void update()
    {
      if (taken)
        t++;
      if (t >= duration)
        unapply();
    }
    
    public void draw()
    {
      if (taken)
        return;
      blendMode(BLEND);
      fill(D);
      stroke(D);
      ellipse(pos.x, pos.y, size, size);
    }
    
    public boolean collision(Player pl)
    {
      if (taken)
        return false;
      if (circleCircleCollision(pos.x, pos.y, size/2, pl.pos.x, pl.pos.y, pl.size/2))
      {
        this.apply();
        return true;
      }
      return false;
    }
    
    public boolean collision(Obstacle ob)
    {
      boolean state = isCollidingCircleRectangle(
        pos.x,
        pos.y,
        size,
        ob.x,
        ob.y,
        ob.w,
        ob.h);
        if (!state)
          return false;
          
        PVector rectCenter = new PVector(ob.x + 0.5f*ob.w, ob.y + 0.5f*ob.h);
          
        while (isCollidingCircleRectangle(
        pos.x,
        pos.y,
        size/2,
        ob.x,
        ob.y,
        ob.w,
        ob.h))
        {
          PVector dir = pos.copy();
          dir.sub(rectCenter);
          
          if (abs(pos.x - rectCenter.x) > ob.w/2)
            dir.y = 0;
          else 
            dir.x = 0;
          
          dir = dir.normalize();
          
          pos.add(dir);
        }
          
        return true;
    }
    
    private PVector pos;
    private int size = 10;
    protected int D = color(255, 0, 0);
    boolean deathFlag = false;
    boolean taken = false;
    int duration = 50;
    int t = 0;
}
  public void settings() {  size(800, 800); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "--present", "--window-color=#666666", "--stop-color=#cccccc", "sketch_2dLighting" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
