public class Treasure
{
  private String treasureitem="";
  private int count;
  
  public Treasure()
  {
    count = (int)(Math.random()*3+1);
    if(count==1)
    {
      treasureitem="crown";
    }
    if (count==2)
    {
      treasureitem= "diamonds";
    }
    if (count==3)
    {
      treasureitem = "trophy";
    }
    else
    {
      treasureitem = "nothing";
    }
  }

  public String getTreasure()
  {
    return treasureitem;
  }
    
  public int getCount()
  {
    return count;
  }

}

 