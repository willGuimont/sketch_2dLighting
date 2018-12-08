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
        
      PVector rectCenter = new PVector(ob.x + 0.5*ob.w, ob.y + 0.5*ob.h);
        
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