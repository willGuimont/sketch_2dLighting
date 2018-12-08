class NoCooldownPowerUp extends PowerUp
{
  public NoCooldownPowerUp(int x, int y)
  {
    super(x, y);
    previousCool = cooldownP;
    this.D = color(0, 127, 255);
    duration = 300;
  }
  
  void power()
  {
    super.power();
    cooldownMain = 1;
    player.C = color(0, 127, 255);
  }
    
  void undoPower()
  {
    super.undoPower();
    cooldownMain = previousCool;
    player.C = player.pC;
  }
  
  int previousCool;
}