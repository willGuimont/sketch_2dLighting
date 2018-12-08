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