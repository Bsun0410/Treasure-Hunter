import java.util.Scanner;

public class TreasureHunter
{
    //Instance variables
    private Town currentTown;
    private Hunter hunter;
    private boolean hardMode;
    private boolean gameOver;
    private boolean easyMode;
    
        //Constructor
    /**
    * Constructs the Treasure Hunter game.
    */
    public TreasureHunter()
    {
        // these will be initialized in the play method
        currentTown = null;
        hunter = null;
        hardMode = false;
    }
  
    // starts the game; this is the only public method
    public void play()
    {
        welcomePlayer();
        enterTown();
        showMenu();
    }
  
    /**
    * Creates a hunter object at the beginning of the game and populates the class member variable with it.
    */
    private void welcomePlayer()
    {
        Scanner scan = new Scanner(System.in);
    
        System.out.println("Welcome to TREASURE HUNTER!");
        System.out.println("Going hunting for the big treasure, eh?");
        System.out.print("What's your name, Hunter? ");
        String name = scan.nextLine();
        
        // set hunter instance variable
        hunter = new Hunter(name, 10);
        
        System.out.print("Select level of difficulty: (E)asy, (N)ormal, (H)ard ");
        String hard = scan.nextLine();
        if (hard.equals("H") || hard.equals("h"))
        {
        hardMode = true;
        }
        if (hard.equals("E") || hard.equals("e")){
        easyMode = true;
        }
    }
    
    /**
    * Creates a new town and adds the Hunter to it.
    */
    private void enterTown()
    {
        double markdown = 0.5;
        double toughness = 0.4;
        if (hardMode)
        {
        // in hard mode, you get less money back when you sell items
        markdown = 0.25;
        
        // and the town is "tougher"
        toughness = 0.75;
        }
        if (easyMode){
            markdown = .5;
            toughness = .2;
        }
        
        // note that we don't need to access the Shop object
        // outside of this method, so it isn't necessary to store it as an instance
        // variable; we can leave it as a local variable
        Shop shop = new Shop(markdown, easyMode);
        // creating the new Town -- which we need to store as an instance
        // variable in this class, since we need to access the Town
        // object in other methods of this class
        currentTown = new Town(shop, toughness, hardMode, easyMode);
        
        // calling the hunterArrives method, which takes the Hunter
        // as a parameter; note this also could have been done in the
        // constructor for Town, but this illustrates another way to associate
        // an object with an object of a different class
        currentTown.hunterArrives(hunter);
    }

    public boolean getEasyMode(){
        return easyMode;
    }
   
    /**
    * Displays the menu and receives the choice from the user.<p>
    * The choice is sent to the processChoice() method for parsing.<p>
    * This method will loop until the user chooses to exit.
    */
    private void showMenu()
    {
        Scanner scan = new Scanner(System.in);
        String choice = "";
        
        while (!(choice.equals("X") || choice.equals("x")))
        {
        System.out.println();
        System.out.println(currentTown.getLatestNews());
        System.out.println("***");
        System.out.println(hunter);
        System.out.println(currentTown);
        System.out.println("(B)uy something at the shop.");
        System.out.println("(S)ell something at the shop.");
        System.out.println("(M)ove on to a different town.");
        System.out.println("(L)ook for trouble!");
                System.out.println("(H)unt for treasure!");
        System.out.println("Give up the hunt and e(X)it.");
        System.out.println();
        System.out.print("What's your next move? ");
        choice = scan.nextLine();
        processChoice(choice);
        if (win()) {
            System.out.println("\n Congratulations! You have won the Treasure Hunter!");
            break;
        }
        if (gameOver){
            System.out.println("\n\nYou've lost all your gold. Game Over.");
            break;
        }
    }
}
    
    private boolean win() {
        if (Town.crownsearch && Town.diamondssearch && Town.trophysearch) {
            return true;
        }
        return false;
    }
    /**
    * Takes the choice received from the menu and calls the appropriate method to carry out the instructions.
    * @param choice The action to process.
    */
    private void processChoice(String choice)
    {
        if (choice.equals("B") || choice.equals("b") || choice.equals("S") || choice.equals("s"))
        {
        currentTown.enterShop(choice);
        }
        else if (choice.equals("M") || choice.equals("m"))
        {
        if (currentTown.leaveTown())
        {
        //This town is going away so print its news ahead of time.
            System.out.println(currentTown.getLatestNews());
            enterTown();
        }
        }
        else if (choice.equals("L") || choice.equals("l"))
        {
        currentTown.lookForTrouble();
        if (hunter.getGold() == 0 && currentTown.getMostRecentFight() == true){
                gameOver = true;
            }       
        }
        else if (choice.equals("X") || choice.equals("x"))
        {
        System.out.println("Fare thee well, " + hunter.getHunterName() + "!");
        }
            else if (choice.equals("h") || choice.equals("H"))
        {
        currentTown.lookForTreasure();
        }
        else
        {
        System.out.println("Invalid option! Try again.");
        }
    }
}