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