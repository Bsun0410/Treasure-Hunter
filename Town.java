public class Town
{
  //instance variables
    private Hunter hunter;
    private Shop shop;
    private Terrain terrain;
    private String printMessage;
    private boolean toughTown;
    private Treasure treasure;
    private boolean searched;
    public static boolean crownsearch = false;
    public static boolean diamondssearch = false;
    public static boolean trophysearch = false;
    public boolean lostMostRecentFight;
    private boolean hardMode;
    private boolean easyMode;
    private boolean lostitem;
    //Constructor
    /**
    * The Town Constructor takes in a shop and the surrounding terrain, but leaves the hunter as null until one arrives.
    * @param s The town's shoppe.
    * @param t The surrounding terrain.
    */
    public Town(Shop shop, double toughness, boolean hardMode, boolean easyMode)
    {
        this.shop = shop;
        this.terrain = getNewTerrain();
        this.hardMode = hardMode;
        this.easyMode = easyMode;
        // the hunter gets set using the hunterArrives method, which
        // gets called from a client class
        hunter = null;
        
        printMessage = "";
        
        // higher toughness = more likely to be a tough town
        toughTown = (Math.random() < toughness);
            treasure = null;
            searched = false;
        if (easyMode){
            if(Math.random() <.15){
                lostitem = true;
            }
        }
    }
    
    public String getLatestNews()
    {
        return printMessage;
    }
    
    /**
    * Assigns an object to the Hunter in town.
    * @param h The arriving Hunter.
    */
    public void hunterArrives(Hunter hunter)
    {
        this.hunter = hunter;
        printMessage = "Welcome to town, " + hunter.getHunterName() + ".";
        
        if (toughTown)
        {
        printMessage += "\nIt's pretty rough around here, so watch yourself.";
        }
        else
        {
        printMessage += "\nWe're just a sleepy little town with mild mannered folk.";
        }
    }
   
    /**
    * Handles the action of the Hunter leaving the town.
    * @return true if the Hunter was able to leave town.
    */
    public boolean leaveTown()
    {
        boolean canLeaveTown = terrain.canCrossTerrain(hunter);
        if (canLeaveTown && !lostitem)
        {
            String item = terrain.getNeededItem();
            printMessage = "You used your " + item + " to cross the " + terrain.getTerrainName() + ".";
            if (broken())
            {
                hunter.removeItemFromKit(item);
                printMessage += "\nUnfortunately, your " + item + " broke.";
            }   
            if (!easyMode) {
                int treasureCount =  (diamondssearch ? 1 : 0) + (crownsearch ? 1 : 0) + (trophysearch ? 1 : 0);
                double odds;
                if (treasureCount == 3) {
                    odds = .30;
                }
                else if (treasureCount == 2) {
                    odds = .15;
                }
                else {
                    odds = .05;
                }
                odds += hardMode ? .05 : 0;
                if (treasureCount >= 1 && Math.random() <= odds) {
                    if (trophysearch) {
                        trophysearch = false;
                        System.out.println("\nYou lost the trophy due to the rough terrain.\n");
                    }
                    else if (crownsearch) {
                        crownsearch = false;
                        System.out.println("\nYou lost the crown due to the rough terrain.\n");
                    }
                    if (diamondssearch) {
                        diamondssearch = false;
                        System.out.println("\nYou lost the diamonds due to the rough terrain.\n");
                    }
                }
                else if (odds <= odds) {
                    int goldLost = (int) (Math.random() * 5) + 1;
                    System.out.println("You lost " + goldLost + " gold due to the rough terrain.");
                    //lose gold command **insert here**
                }
            }
            return true;
        }
        if (lostitem){
            System.out.println("It took a lot to leave this place...");
            return true;
        }
        printMessage = "You can't leave town, " + hunter.getHunterName() + ". You don't have a " + terrain.getNeededItem() + ".";
        return false;
    }
    
    public void enterShop(String choice)
    {
        shop.enter(hunter, choice);
    }
  
    /**
    * Gives the hunter a chance to fight for some gold.<p>
    * The chances of finding a fight and winning the gold are based on the toughness of the town.<p>
    * The tougher the town, the easier it is to find a fight, and the harder it is to win one.
    */
    public void lookForTrouble()
    {
        double noTroubleChance;
        if (toughTown)
        {
        noTroubleChance = 0.66;
        }
        else
        {
        noTroubleChance = 0.33;
        }
        
        if (Math.random() > noTroubleChance)
        {
        printMessage = "You couldn't find any trouble";
        lostMostRecentFight = false;
        }
        else
        {
        printMessage = "You want trouble, stranger! Oof! Umph! Ow!\n";
        int goldDiff = (int)(Math.random() * 10) + 1;
        noTroubleChance = .5;

        if (hardMode) {
            noTroubleChance += .1;
        }
        if (easyMode) {
            goldDiff = (int)(Math.random() * 15) + 3;
            noTroubleChance -= .1;
        }
        if (Math.random() > noTroubleChance)
        {
            printMessage += "Take my gold.";
            printMessage += "\nYou won the brawl and received " +  goldDiff + " gold.";
            hunter.changeGold(goldDiff);
            lostMostRecentFight = false;
        }
        else
        {
            if (easyMode){
                goldDiff = (int)(Math.random() * 8) + 1;
            }
            printMessage += "Ouch!!\nYou lost the brawl and paid " +  goldDiff + " gold.";
            hunter.changeGold(-1 * goldDiff);
            lostMostRecentFight = true;
        }
        }
    }
    
    public String toString()
    {
        return "This nice little town is surrounded by " + terrain.getTerrainName() + ".";
    }

    public boolean getMostRecentFight(){
        return lostMostRecentFight;
    }
  
    /**
    * Determines the surrounding terrain for a town, and the item needed in order to cross that terrain.
    * 
    * @return A Terrain object.
    */
    private Terrain getNewTerrain()
    {
        double rnd = Math.random();
        if (rnd < .15){
            return new Terrain("Glacier", "Icee");
        }
        if (rnd < .3)
        {
        return new Terrain("Mountains", "Rope");
        }
        else if (rnd < .45)
        {
        return new Terrain("Ocean", "Boat");
        }
        else if (rnd < .60)
        {
        return new Terrain("Plains", "Horse");
        }
        else if (rnd < .75)
        {
        return new Terrain("Desert", "Water");
        }
        else
        {
        return new Terrain("Jungle", "Machete");
        }
    }
  
    /**
    * Determines whether or not a used item has broken.
    * @return true if the item broke.
    */
    private boolean broken()
    {
        double rand = Math.random();
        return (rand < 0.5);
    }

    public void lookForTreasure(){
        if (searched == false){
            Treasure treasure = new Treasure();
            if (treasure.getCount() == 1 && diamondssearch == true){
                System.out.println("You found diamonds again.");
            }
            else if (treasure.getCount() == 2 && trophysearch == true){
                System.out.println("You found trophy again.");    
            }
            else if (treasure.getCount() == 3 && crownsearch == true){
                System.out.println("You found crown again."); 
            }
            else{
                System.out.println("You have found " + treasure.getTreasure() + ".");
                searched = true;
                if (treasure.getCount() == 1){
                    diamondssearch = true;
                }
                if (treasure.getCount() == 2){
                    trophysearch = true;
                }
                if (treasure.getCount() == 3){
                    crownsearch = true;
                }
            }
        }
        else{
            System.out.println("You have already searched this town...");
        }
    }

}