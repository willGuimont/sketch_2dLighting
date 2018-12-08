class InvinciblePowerUp extends PowerUp
{
  public InvinciblePowerUp(int x, int y)
  {
    super(x, y);
    this.D = color(255, 255, 255);
    duration = 250;
  }
  
  void power()
  {
    super.power();
    player.isInvincible = true;
    player.C = color(255, 255, 255);
  }
    
  void undoPower()
  {
    super.undoPower();
    player.isInvincible = false;
    player.C = player.pC;
  }
}