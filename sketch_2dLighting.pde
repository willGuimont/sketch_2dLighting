boolean left = false;
boolean right = false;
boolean up = false;
boolean down = false;

void keyPressed()
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

void keyReleased()
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

int enemySpawn = 150;
int e = 0;

int powerSpawn = 300;
int p = 50;

int score = 0;
int ob = 20;

void setup()
{
  size(800, 800);
  player = new Player(width/2, height/2);
  
  obstacles = new ArrayList<Obstacle>();
  bullets = new ArrayList<Bullet>();
  enemies = new ArrayList<Enemy>();
  powerUp = new ArrayList<PowerUp>();
}

void draw()
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
    if (a < 0.50)
    {
      pu = new NoCooldownPowerUp((int)random(0, width), (int)random(0, height));
    }
    else if (a < 0.80)
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

void mousePressed()
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
