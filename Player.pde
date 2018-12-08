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