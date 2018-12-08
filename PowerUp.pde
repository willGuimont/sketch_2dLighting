class PowerUp
{
  public PowerUp(int x, int y)
    {
      pos = new PVector(x, y);
    }
    
    void power()
    {}
    
    void undoPower()
    {}
    
    void apply()
    {
      taken = true;
      this.power();
    }
    
    void unapply()
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
    private int size = 10;
    protected int D = color(255, 0, 0);
    boolean deathFlag = false;
    boolean taken = false;
    int duration = 50;
    int t = 0;
}