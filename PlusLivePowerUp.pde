class PlusLivePowerUp extends PowerUp
{
  public PlusLivePowerUp(int x, int y)
  {
    super(x, y);
    this.D = color(0, 255, 0);
    duration = 10;
  }
  
  void power()
  {
    super.power();
    player.lives++;
    player.C = color(0, 255, 0);
  }
    
  void undoPower()
  {
    super.undoPower();
    player.C = player.pC;
  }
}