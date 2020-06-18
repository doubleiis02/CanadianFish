import java.util.*;

public class Game
{
  private static Player player;
  private static Bot partner;
  private static Bot opponent1;
  private static Bot opponent2;
  private static Deck deck;
  private static int turn;
  private static int[] scores = new int[2];
  private static Bot[] botList;
   
  
  
  public static void main(String[] args)
  {
    System.out.println("Welcome to Canadian Fish!\n------------------------------------------------------------------------\n");
    Scanner input = new Scanner(System.in);
    String status = statusQuestion();
    while (!status.equalsIgnoreCase("q") && !status.equalsIgnoreCase("r") && !status.equalsIgnoreCase("s"))
    {
      System.out.println("You entered an invalid argument. Please try again.");
      status = input.nextLine();
    }
    while (!status.equalsIgnoreCase("q"))
    {
      if (status.equalsIgnoreCase("r"))
      {
        String rules = "- There are 9 sets: 2-7 of hearts, spades, clubs, and diamonds, 9-Ace of hearts,spades, clubs, and diamonds, and lastly all of the 8s plus the two jokers.\n";
        rules += "- In order to win, your team must collect 5 sets. You can ask a person on the opposing team for a card.\n";
        rules += "- If they have it, you take it from them. If they don’t, it becomes their turn to ask cards.\n";
        rules += "- You can only ask for a card if you have a card in that set.\n";
        rules += "- You can call for a set once you are sure that you and your partner have all the cards in that set.\n";
        rules += "- However, if your team does not have all the cards, the opposing team gains the set instead.\n";
        rules += "- Team members cannot communicate with each other, so you must try to determine who has what card based on the cards that people ask for and take away.\n";
        System.out.println(rules);
        status = statusQuestion();
      }
      else
      {
        // finding names
        System.out.println("What is your name?");
        String playerName = input.nextLine();
        System.out.println("Hi " + playerName + ". What would you like your partner's name to be?");
        String partnerName = input.nextLine();
        System.out.println("What is your first opponent's name?");
        String opponent1Name = input.nextLine();
        System.out.println("What is your second opponent's name? (Must be different from first opponent's name)");
        String opponent2Name = input.nextLine();
        
        // setting up players
        deck = new Deck();
        deck.shuffle();
        
        ArrayList<String> tempCards = new ArrayList();
        for (int i = 0; i <= 13; i++)
          tempCards.add(deck.whole_deck.get(i));
        player = new Player(playerName, tempCards);
        
        tempCards = new ArrayList<String>();
        for (int i = 14; i <= 26; i++)
          tempCards.add(deck.whole_deck.get(i));
        partner = new Bot(partnerName, tempCards, 13, 1);
        
        tempCards = new ArrayList<String>();
        for (int i = 27; i <= 40; i++)
          tempCards.add(deck.whole_deck.get(i));
        opponent1 = new Bot(opponent1Name, tempCards, 14, 2);
        
        tempCards = new ArrayList<String>();
        for (int i = 41; i <= 53; i++)
          tempCards.add(deck.whole_deck.get(i));
        opponent2 = new Bot(opponent2Name, tempCards, 13, 3);
        
        player.sortCards();
        System.out.print(player.checkCards());
        boolean gameover = false;
        turn = 0;// Player:0, Partner:1, Opponent1:2, Opponent2:3
        scores[0] = 0; // teamscore
        scores[1] = 0; // oppscore
        botList = new Bot[3];
        botList[0] = partner;
        botList[1] = opponent1;
        botList[2] = opponent2;
        System.out.println("All preparations done. Let the game begin!");
        
        String option = optionQuestion();
        while (!option.equalsIgnoreCase("a") && !option.equalsIgnoreCase("c") && !option.equalsIgnoreCase("q"))
        {
          System.out.println("You entered an invalid argument. Please try again.");
          option = input.nextLine();
        }
        if (option.equalsIgnoreCase("q"))
        {
          gameover = true;
          break;
        }
            
        while(!option.equalsIgnoreCase("q"))
        {
          if (option.equalsIgnoreCase("a"))
          {
            System.out.println("Who would you like to ask, " + opponent1.getName() + " or " + opponent2.getName() +"?");
            String who_to_ask = input.nextLine();
            while (!who_to_ask.equalsIgnoreCase(opponent1.getName()) && !who_to_ask.equalsIgnoreCase(opponent2.getName()))
            {
              System.out.println("Please try again. Type '" + opponent1.getName() + "' or '" + opponent2.getName() + "'.");
              who_to_ask = input.nextLine();
            }
            if (who_to_ask.equalsIgnoreCase(opponent1.getName()))
              who_to_ask = opponent1.getName();
            else
              who_to_ask = opponent2.getName();
            System.out.println("What card would you like to ask?");
            System.out.println("Make sure you type in lowercase and in the format '2 of spades' or 'ace of diamonds' or 'black joker'.");
            String card = input.nextLine();
            while (!deck.whole_deck.contains(card))
            {
              System.out.println("Please try again.");
              card = input.nextLine();
            }
            turn = ask(card, who_to_ask);
            break;
          }
          else // if option is "c"
          {
            player.sortCards();
            System.out.println(player.checkCards());
                
            option = optionQuestion();
            while (!option.equalsIgnoreCase("a") && !option.equalsIgnoreCase("c") && !option.equalsIgnoreCase("q"))
            {
              System.out.println("You entered an invalid argument. Please try again.");
              option = input.nextLine();
            }
            if (option.equalsIgnoreCase("q"))
              gameover = true;
          }
        }
        
        while (gameover == false)
        {
          System.out.println("Type c to call, type q to quit, or type any other key to continue.");
          String choice = input.nextLine();
          if (choice.equalsIgnoreCase("c"))
          {
            scores = call(scores);
            if (scores[0] == 5 || scores[1] == 5)
            {
              gameover = true;
              break;
            }
          }
          if (choice.equalsIgnoreCase("q"))
          {
            gameover = true;
            break;
          }
          
          // player has decided to continue
          
          // at the start of every turn, every bot checks if they should call
          for (int i = 1; i <= 3; i++)
          {
            ArrayList<String> setToCall = check_callstatus(i);
            if (setToCall != null)
            {
              call_bot(setToCall, i);
              if (scores[0] == 5 || scores[1] == 5)
              {
                gameover = true;
                break;
              }
            }
          }
          if (gameover)
            break;
          
          if (player.cards.isEmpty())
            System.out.println("You are out of cards, but the game still continues.");
            
          if (turn == 0)
          {
            option = optionQuestion();
            while (!option.equalsIgnoreCase("a") && !option.equalsIgnoreCase("c") && !option.equalsIgnoreCase("q"))
            {
              System.out.println("You entered an invalid argument. Please try again.");
              option = input.nextLine();
            }
            
            if (option.equalsIgnoreCase("q"))
            {
              gameover = true;
              break;
            }
            
            while(!option.equalsIgnoreCase("q"))
            {
              if (option.equalsIgnoreCase("a"))
              {
                System.out.println("Who would you like to ask, " + opponent1.getName() + " or " + opponent2.getName() +"?");
                String who_to_ask = input.nextLine();
                while (!who_to_ask.equalsIgnoreCase(opponent1.getName()) && !who_to_ask.equalsIgnoreCase(opponent2.getName()))
                {
                  System.out.println("Please try again. Type '" + opponent1.getName() + "' or '" + opponent2.getName() + "'.");
                  who_to_ask = input.nextLine();
                }
                if (who_to_ask.equalsIgnoreCase(opponent1.getName()))
                  who_to_ask = opponent1.getName();
                else
                  who_to_ask = opponent2.getName();
                System.out.println("What card would you like to ask?");
                System.out.println("Make sure you type in lowercase and in the format '____of___' or black/red/ joker.");
                String card = input.nextLine();
                while (!deck.whole_deck.contains(card))
                {
                  System.out.println("Please try again.");
                  card = input.nextLine();
                }
                turn = ask(card, who_to_ask);
                break;
              }
              else // if option is "c"
              {
                player.sortCards();
                System.out.println(player.checkCards());
                
                option = optionQuestion();
                while (!option.equalsIgnoreCase("a") && !option.equalsIgnoreCase("c") && !option.equalsIgnoreCase("q"))
                {
                  System.out.println("You entered an invalid argument. Please try again.");
                  option = input.nextLine();
                }
              }
            }
          }
          else if (turn == 1)
          {
            System.out.println("It is your partner " + partner.getName() + "'s turn.");
            turn = ask_bot(turn);
          }
          else if (turn == 2)
          {
            System.out.println("It is your opponent " + opponent1.getName() + "'s turn.");
            turn = ask_bot(turn);
          }
          else // if turn == 3
          {
            System.out.println("It is your opponent " + opponent2.getName() + "'s turn.");
            turn = ask_bot(turn);
          }
        }
        
        System.out.println("The game is over.");
        if (scores[0] == 5)
          System.out.println("Congratulations! Your team has won.");
        else if (scores[1] == 5)
          System.out.println("Your team lost.");
        
        // end of game
        status = statusQuestion();
      }
    }
  }
  
  
  
  
  // HELPER METHOD FOR STATUS LOOP BEFORE GAME STARTS
  public static String statusQuestion()
  {
    Scanner input = new Scanner(System.in);
    System.out.println("\nWhat would you like to do?\n"
                        + "  Type 'r' to read the rules\n" 
                        + "  Type 's' to start a new game\n"
                        + "  Type 'q' to quit\n");
    return input.nextLine();
  }
  
  
  
  // HELPER METHOD FOR OPTION LOOP DURING PLAYER'S TURN
  public static String optionQuestion()
  {
    Scanner input = new Scanner(System.in);
    System.out.println("\nIt is your turn. What would you like to do?\n"
                        + "  Type 'a' to ask\n"
                        + "  Type 'c' to check your cards\n"
                        + "  Type 'q' to quit\n");
    return input.nextLine();
  }
  
  
  // FINDING THE SET THAT A CARD BELONGS TO
  public static ArrayList<String> whichSet(String card)
  {
    if (deck.low_d_set.contains(card))
          return deck.low_d_set;
      else if (deck.high_d_set.contains(card))
          return deck.high_d_set;
      else if (deck.low_c_set.contains(card))
          return deck.low_c_set;
      else if (deck.high_c_set.contains(card))
          return deck.high_c_set;
      else if (deck.low_s_set.contains(card))
          return deck.low_s_set;
      else if (deck.high_s_set.contains(card))
          return deck.high_s_set;
      else if (deck.low_h_set.contains(card))
          return deck.low_h_set;
      else if (deck.high_h_set.contains(card))
          return deck.high_h_set;
      else
          return deck.middle_set;
  }
  
  
  
  
  // DELETING ALL CARDS OF A SET AFTER A TEAM HAS DECLARED IT
  public static void deleteSet(ArrayList<String> set)
  {
    for (String c : set)
    {
      //updating probs and removing cards
      int index = deck.order.indexOf(c);
      for (Bot bot : botList)
      {
        bot.player_prob.set(index, 0.0);
        bot.partner_prob.set(index, 0.0);
        bot.opp1_prob.set(index, 0.0);
        bot.opp2_prob.set(index, 0.0);
        if (bot.cards.contains(c))
          bot.cards.remove(c);
      }
      if (player.cards.contains(c))
        player.cards.remove(c);
    }
  }
  
  
  
  
  // ASKING SOMEONE FOR A CARD
  public static int ask(String card, String who_to_ask)
  {
    Scanner input = new Scanner(System.in);
    
    // converting who_to_ask from a String to Bot
    
    Bot personAsked;
    if (who_to_ask.equalsIgnoreCase(opponent1.getName()) && opponent1.cards.isEmpty() == false)
      personAsked = opponent1;
    else if (who_to_ask.equalsIgnoreCase(opponent2.getName()) && opponent2.cards.isEmpty() == false)
      personAsked = opponent2;
    // if who_to_ask is invalid, ask one more time
    else
    {
      System.out.println("You cannot ask this person. Try again.");
      who_to_ask = input.nextLine();
      if (who_to_ask.equalsIgnoreCase(opponent1.getName()) && opponent1.cards.isEmpty() == false)
        personAsked = opponent1;
      else if (who_to_ask.equalsIgnoreCase(opponent2.getName()) && opponent2.cards.isEmpty() == false)
        personAsked = opponent2;
      else
      {
        System.out.println("You're stupid. You lose your turn.");
        return (int)(Math.random()*2) + 2;
      }
    }
    
    // checking if player has asked for a card that they have
    if (player.cards.contains(card))
    {
      System.out.println("You have that card and therefore cannot ask someone else for it. Try again.");
      int index = deck.order.indexOf(card);
      for (Bot b : botList)
      {
        b.player_prob.set(index, 1.0);
        b.partner_prob.set(index, 0.0);
        b.opp1_prob.set(index, 0.0);
        b.opp2_prob.set(index, 0.0);
      }
      System.out.println("Which card will you ask for?");
      card = input.nextLine();
      if (player.cards.contains(card))
      {
        index = deck.order.indexOf(card);
        for (Bot b : botList)
        {
          b.player_prob.set(index, 1.0);
          b.partner_prob.set(index, 0.0);
          b.opp1_prob.set(index, 0.0);
          b.opp2_prob.set(index, 0.0);
        }
        System.out.println("You're stupid. You lose your turn.");
        return (int)(Math.random()*2) + 2;
      }
    }
    
    
    ArrayList<String> set = whichSet(card);
    
    
    // "doing" the ask
    if (personHasSet(turn, set)) // checking if player has a card in the set that they are asking for
    {
      // now everyone knows that the person asked won't have card
      int index = deck.order.indexOf(card);
      if (personAsked == opponent1)
      {
        partner.opp1_prob.set(index, 0.0);
        opponent2.opp1_prob.set(index, 0.0);
      }
      else // personAsked == opponent2
      {
        partner.opp2_prob.set(index, 0.0);
        opponent1.opp2_prob.set(index, 0.0);
      }
        
      // if personAsked has the card
      if (personAsked.cards.contains(card))
      {
        player.cards.add(card);
        personAsked.cards.remove(card);
        System.out.println("You took the " + card + " from " + personAsked.getName());
        
        // adjusting probs
        for (Bot b : botList)
        {
          b.player_prob.set(index, 1.0);
          b.opp1_prob.set(index, 0.0);
          b.opp2_prob.set(index, 0.0);
        }
        
        // adjusting partner's player prob for other cards in set that player might have
        adjustProbsPlayer(set);
        
        return player.turn;
      }
      else // if personAsked doesn't have the card
      {
        System.out.println(personAsked.getName() + " does not have that card.");
        
        // now everyone knows player doesn't have that card
        for (Bot b : botList)
          b.player_prob.set(index, 0.0);
        
        // adjusting partner's player prob for other cards in set that player might have
        adjustProbsPlayer(set);
        
        return personAsked.turn;
      }
    }
    
    // if player can't ask for the card!
    System.out.println("You have chosen to ask for a card in a set you do not have. Your turn is over.");
    for (String c : set)
    {
      int index = deck.order.indexOf(c);
      for (Bot b : botList)
        b.player_prob.set(index, 0.0);
    }
    return personAsked.turn;
  }
  
  
  
  
  // HELPER METHOD IN GENERAL - CHECK IF PERSON HAS A CARD IN SET WHEN ASKING A CARD
  public static boolean personHasSet(int turn, ArrayList<String> set)
  {
    switch(turn)
    {
      case 0:
        for (String c : player.cards)
        {
           if (set.contains(c))
            return true;
        }
        break;
      case 1:
        for (String c : partner.cards)
        {
           if (set.contains(c))
            return true;
        }
        break;
      case 2:
        for (String c : opponent1.cards)
        {
           if (set.contains(c))
            return true;
        }
        break;
      case 3:
        for (String c : opponent2.cards)
        {
           if (set.contains(c))
            return true;
        }
        break;
    }
    return false;
  }
  
  
  
  
  // HELPER METHOD FOR ASK - ADJUSTING PROBS FOR OTHER CARDS THAT PLAYER MIGHT HAVE
  public static void adjustProbsPlayer(ArrayList<String> set)
  {
    double prob = 0.0;
    int index;
    for (Bot b : botList)
    {
      for (String c : set)
      {
        index = deck.order.indexOf(c);
        if (b.player_prob.get(index) != 0.0 && b.player_prob.get(index) < 1.0)
          prob++;
      }
      if (prob > 0.0)
      {
        prob = 1/prob;
        for (String c : set)
        {
          index = deck.order.indexOf(c);
          if (b.player_prob.get(index) != 0.0 && b.player_prob.get(index) < 1.0)
            b.player_prob.set(index, b.player_prob.get(index) + prob);
        }
      }
    }
  }
  
  
  
  
  // WHEN PLAYER DECIDES TO CALL A SET
  public static int[] call(int[] scores)
  {
    // choosing which set
    Scanner input = new Scanner(System.in);
    ArrayList<Integer> possibleSets = new ArrayList<Integer>();
    System.out.println("Which value would you like to call?");
    if (deck.low_h_ingame)
    {
        System.out.println("Type 1 for low hearts");
        possibleSets.add(1);
    }
    if (deck.low_c_ingame)
    {
        System.out.println("Type 2 for low clubs");
        possibleSets.add(2);
    }
    if (deck.low_s_ingame)
    {
        System.out.println("Type 3 for low spades");
        possibleSets.add(3);
    }
    if (deck.low_d_ingame)
    {
        System.out.println("Type 4 for low diamonds");
        possibleSets.add(4);
    }
    if (deck.high_h_ingame)
    {
        System.out.println("Type 5 for high hearts");
        possibleSets.add(5);
    }
    if (deck.high_c_ingame)
    {
        System.out.println("Type 6 for high clubs");
        possibleSets.add(6);
    }
    if (deck.high_s_ingame)
    {
        System.out.println("Type 7 for high spades");
        possibleSets.add(7);
    }
    if (deck.high_d_ingame)
    {
        System.out.println("Type 8 for high diamonds");
        possibleSets.add(8);
    }
    if (deck.middle_ingame)
    {
        System.out.println("Type 9 for 8s and Jokers\n");
        possibleSets.add(9);
    }
    int inp = input.nextInt();
    
    while(!possibleSets.contains(inp))
    {
      System.out.println("Try again. Type a valid number.");
      inp = input.nextInt();
    }
    
    // turn int into the corresponding set
    ArrayList<String> set;
    switch(inp)
    {
        case 1:
            set = deck.low_h_set;
            deck.low_h_ingame = false;
            break;
        case 2:
            set = deck.low_c_set;
            deck.low_c_ingame = false;
            break;
        case 3:
            set = deck.low_s_set;
            deck.low_s_ingame = false;
            break;
        case 4:
            set = deck.low_d_set;
            deck.low_d_ingame = false;
            break;
        case 5:
            set = deck.high_h_set;
            deck.high_h_ingame = false;
            break;
        case 6:
            set = deck.high_c_set;
            deck.high_c_ingame = false;
            break;
        case 7:
            set = deck.high_s_set;
            deck.high_s_ingame = false;
            break;
        case 8:
            set = deck.high_d_set;
            deck.high_d_ingame = false;
            break;
        default: // case 9
            set = deck.middle_set;
            deck.middle_ingame = false;
    }
    
    // upon calling...
    
    for (String c : set)
    {
      if (!player.cards.contains(c) && !partner.cards.contains(c))
      {
        scores[1]++; // add 1 point to opposing team
        System.out.println("Oh no :( Your team did not have the whole set. Opposing team gets the set.");
        System.out.println("Your score: " + scores[0] + " Opposing team score: " + scores[1]);
        deleteSet(set);
        return scores;
      }
    }
    scores[0]++; // add 1 point to player's team
    System.out.println("Success! Your team had the whole set."); 
    System.out.println("Your score: " + scores[0] + " Opposing team score: " + scores[1]);
    deleteSet(set);
    return scores;
  }
  
  
  
  
  // HELPER METHOD FOR ASK_BOT - WHEN A BOT NEEDS TO CHOOSE WHICH CARD TO ASK FOR
  public static String[] choose_card(int turn) // return the person to ask & the card....as a String[]
  {
    ArrayList<Integer> playersToAsk = new ArrayList<Integer>();
    if (turn == 1)
    {
      if (!opponent1.cards.isEmpty())
        playersToAsk.add(2);
      if (!opponent2.cards.isEmpty())
        playersToAsk.add(3);
    }
    else
    {
      if (!player.cards.isEmpty())
        playersToAsk.add(0);
      if (!partner.cards.isEmpty())
        playersToAsk.add(1);
    }
    
    int search = playersToAsk.get((int)(Math.random() * playersToAsk.size()));
    String name = "";
    String card = "";
    double max = 0.0;
    double prob;
    String c;
    Bot b;
    switch(turn)
    {
      case 1:
        b = partner;
        break;
      case 2:
        b = opponent1;
        break;
      default: // case 3
        b = opponent2;
    }
    
    // looking for greatest prob values
    if (search == 0)
    {
      // search player first
      for (int i = 0; i < deck.order.size(); i++)
      {
        prob = b.player_prob.get(i);
        c = deck.order.get(i);
        if (prob == 1.0 && personHasSet(turn, whichSet(c)))
        {
          String[] list = {player.getName(), c};
          return list;
        }
        if (prob > max && personHasSet(turn, whichSet(c)))
        {
          max = prob;
          card = c;
          name = player.getName();
        }
      }
      // search partner second if not empty
      if (!partner.cards.isEmpty())
      {
        for (int i = 0; i < deck.order.size(); i++)
        {
          prob = b.partner_prob.get(i);
          c = deck.order.get(i);
          if (prob == 1.0 && personHasSet(turn, whichSet(c)))
          {
            String[] list = {partner.getName(), c};
            return list;
          }
          if (prob > max && personHasSet(turn, whichSet(c)))
          {
            max = prob;
            card = c;
            name = partner.getName();
          }
        }
      }
      // return name and card w max prob, if not done already
      String[] list = {name, card};
      return list;
    }
    else if (search == 1)
    {
      // search partner first
      for (int i = 0; i < deck.order.size(); i++)
      {
        prob = b.partner_prob.get(i);
        c = deck.order.get(i);
        if (prob == 1.0 && personHasSet(turn, whichSet(c)))
        {
          String[] list = {partner.getName(), c};
          return list;
        }
        if (prob > max && personHasSet(turn, whichSet(c)))
        {
          max = prob;
          card = c;
          name = partner.getName();
        }
      }
      // search player second if not empty
      if (!player.cards.isEmpty())
      {
        for (int i = 0; i < deck.order.size(); i++)
        {
          prob = b.player_prob.get(i);
          c = deck.order.get(i);
          if (prob == 1.0 && personHasSet(turn, whichSet(c)))
          {
            String[] list = {player.getName(), c};
            return list;
          }
          if (prob > max && personHasSet(turn, whichSet(c)))
          {
            max = prob;
            card = c;
            name = player.getName();
          }
        }
      }
      // return name and card w max prob, if not done already
      String[] list = {name, card};
      return list;
    }
    else if (search == 2)
    {
      // search opponent1 first
      for (int i = 0; i < deck.order.size(); i++)
      {
        prob = b.opp1_prob.get(i);
        c = deck.order.get(i);
        if (prob == 1.0 && personHasSet(turn, whichSet(c)))
        {
          String[] list = {opponent1.getName(), c};
          return list;
        }
        if (prob > max && personHasSet(turn, whichSet(c)))
        {
          max = prob;
          card = c;
          name = opponent1.getName();
        }
      }
      // search opponent2 second if not empty
      if (!opponent2.cards.isEmpty())
      {
        for (int i = 0; i < deck.order.size(); i++)
        {
          prob = b.opp2_prob.get(i);
          c = deck.order.get(i);
          if (prob == 1.0 && personHasSet(turn, whichSet(c)))
          {
            String[] list = {opponent2.getName(), c};
            return list;
          }
          if (prob > max && personHasSet(turn, whichSet(c)))
          {
            max = prob;
            card = c;
            name = opponent2.getName();
          }
        }
      }
      // return name and card w max prob, if not done already
      String[] list = {name, card};
      return list;
    }
    else // if search == 3
    {
      // search opponent2 first
      for (int i = 0; i < deck.order.size(); i++)
      {
        prob = b.opp2_prob.get(i);
        c = deck.order.get(i);
        if (prob == 1.0 && personHasSet(turn, whichSet(c)))
        {
          String[] list = {opponent2.getName(), c};
          return list;
        }
        if (prob > max && personHasSet(turn, whichSet(c)))
        {
          max = prob;
          card = c;
          name = opponent2.getName();
        }
      }
      // search opponent1 second if not empty
      if (!opponent1.cards.isEmpty())
      {
        for (int i = 0; i < deck.order.size(); i++)
        {
          prob = b.opp1_prob.get(i);
          c = deck.order.get(i);
          if (prob == 1.0 && personHasSet(turn, whichSet(c)))
          {
            String[] list = {opponent1.getName(), c};
            return list;
          }
          if (prob > max && personHasSet(turn, whichSet(c)))
          {
            max = prob;
            card = c;
            name = opponent1.getName();
          }
        }
      }
      // return name and card w max prob, if not done already
      String[] list = {name, card};
      return list;
    }
  }
  
  
  
  
  // BOT ASKING FOR A CARD
  public static int ask_bot(int turn)
  {
    String[] person_and_card = choose_card(turn);
    String card = person_and_card[1];
    int index = deck.order.indexOf(card);
    ArrayList<String> set = whichSet(person_and_card[1]);
    Bot currentBot;
    int endturn;
    if (turn == 1)
      currentBot = partner;
    else if (turn == 2)
      currentBot = opponent1;
    else
      currentBot = opponent2;
    
    // if currentBot asked for a card from player
    if (person_and_card[0].equals(player.getName()))
    {
      // if player has the card
      if (player.cards.contains(card))
      {
        currentBot.cards.add(card);
        player.cards.remove(card);
        System.out.println(currentBot.getName() + " took the " + card + " from " + player.getName() + ".");
        // update probs
        if (turn == 2)
        {
          for (Bot b: botList)
          {
            b.player_prob.set(index, 0.0);
            b.partner_prob.set(index, 0.0);
            b.opp1_prob.set(index, 1.0);
            b.opp2_prob.set(index, 0.0);
          }
        }
        else // if turn == 3
        {
          for (Bot b : botList)
          {
            b.player_prob.set(index, 0.0);
            b.partner_prob.set(index, 0.0);
            b.opp1_prob.set(index, 0.0);
            b.opp2_prob.set(index, 1.0);
          }
        }
        endturn = turn;
      }
      // if player doesn't have the card
      else
      {
        System.out.println(currentBot.getName() + " asked " + player.getName() + " for the " + card + ", but " + player.getName() + " did not have it.");
        // update probs
        if (turn == 2)
        {
          for (Bot b : botList)
          {
            b.player_prob.set(index, 0.0);
            b.opp1_prob.set(index, 0.0);
          }
        }
        else // if turn == 3
        {
          for (Bot b : botList)
          {
            b.player_prob.set(index, 0.0);
            b.opp2_prob.set(index, 0.0);
          }
        }
        endturn = 0;
      }
    }
    // if currentBot asked for a card from partner
    else if (person_and_card[0].equals(partner.getName()))
    {
      // if partner has the card
      if (partner.cards.contains(card))
      {
        currentBot.cards.add(card);
        partner.cards.remove(card);
        System.out.println(currentBot.getName() + " took the " + card + " from " + partner.getName() + ".");
        // update probs
        if (turn == 2)
        {
          for (Bot b: botList)
          {
            b.player_prob.set(index, 0.0);
            b.partner_prob.set(index, 0.0);
            b.opp1_prob.set(index, 1.0);
            b.opp2_prob.set(index, 0.0);
          }
        }
        else // if turn == 3
        {
          for (Bot b : botList)
          {
            b.player_prob.set(index, 0.0);
            b.partner_prob.set(index, 0.0);
            b.opp1_prob.set(index, 0.0);
            b.opp2_prob.set(index, 1.0);
          }
        }
        endturn = turn;
      }
      // if partner doesn't have the card
      else
      {
        System.out.println(currentBot.getName() + " asked " + partner.getName() + " for the " + card + ", but " + partner.getName() + " did not have it.");
        // update probs
        if (turn == 2)
        {
          for (Bot b : botList)
          {
            b.partner_prob.set(index, 0.0);
            b.opp1_prob.set(index, 0.0);
          }
        }
        else // if turn == 3
        {
          for (Bot b : botList)
          {
            b.partner_prob.set(index, 0.0);
            b.opp2_prob.set(index, 0.0);
          }
        }
        endturn = 1;
      }
    }
    // if currentBot asked for a card from opponent1
    else if (person_and_card[0].equals(opponent1.getName()))
    {
      // if opponent1 has the card
      if (opponent1.cards.contains(card))
      {
        currentBot.cards.add(card);
        opponent1.cards.remove(card);
        System.out.println(currentBot.getName() + " took the " + card + " from " + opponent1.getName() + ".");
        // update probs
        for (Bot b : botList)
        {
          b.player_prob.set(index, 0.0);
          b.partner_prob.set(index, 1.0);
          b.opp1_prob.set(index, 0.0);
          b.opp2_prob.set(index, 0.0);
        }
        endturn = turn;
      }
      // if opponent1 doesn't have the card
      else
      {
        System.out.println(currentBot.getName() + " asked " + opponent1.getName() + " for the " + card + ", but " + opponent1.getName() + " did not have it.");
        // update probs
        for (Bot b : botList)
        {
          b.partner_prob.set(index, 0.0);
          b.opp1_prob.set(index, 0.0);
        }
        endturn = 2;
      }
    }
    // if currentBot asked for a card from opponent2
    else 
    {
      // if opponent2 has the card
      if (opponent2.cards.contains(card))
      {
        currentBot.cards.add(card);
        opponent2.cards.remove(card);
        System.out.println(currentBot.getName() + " took the " + card + " from " + opponent2.getName() + ".");
        // update probs
        for (Bot b : botList)
        {
          b.player_prob.set(index, 0.0);
          b.partner_prob.set(index, 1.0);
          b.opp1_prob.set(index, 0.0);
          b.opp2_prob.set(index, 0.0);
        }
        endturn = turn;
      }
      // if opponent2 doesn't have the card
      else
      {
        System.out.println(currentBot.getName() + " asked " + opponent2.getName() + " for the " + card + ", but " + opponent2.getName() + " did not have it.");
        // update probs
        for (Bot b : botList)
        {
          b.partner_prob.set(index, 0.0);
          b.opp2_prob.set(index, 0.0);
        }
        endturn = 3;
      }
    }
    // adjust probs for other cards that personAsked might have
    adjustProbsBot(set, turn);
    return endturn;
  }
  
  
  
  
  // HELPER METHOD FOR ASK_BOT - ADJUSTING PROBS FOR OTHER CARDS THAT THE CURRNET BOT MIGHT HAVE
  public static void adjustProbsBot(ArrayList<String> set, int turn)
  {
    double prob = 0.0;
    int index;
    
    if (turn == 1)
    {
      for (Bot b : botList)
      {
        for (String c : set)
        {
          index = deck.order.indexOf(c);
          if (b.partner_prob.get(index) != 0.0 && b.partner_prob.get(index) < 1.0)
            prob++;
        }
        if (prob > 0.0)
        {
          prob = 1/prob;
          for (String c : set)
          {
            index = deck.order.indexOf(c);
            if (b.partner_prob.get(index) != 0.0 && b.partner_prob.get(index) < 1.0)
              b.partner_prob.set(index, b.partner_prob.get(index) + prob);
          }
        }
      }
    }
    else if (turn == 2)
    {
      for (Bot b : botList)
      {
        for (String c : set)
        {
          index = deck.order.indexOf(c);
          if (b.opp1_prob.get(index) != 0.0 && b.opp1_prob.get(index) < 1.0)
            prob++;
        }
        if (prob > 0.0)
        {
          prob = 1/prob;
          for (String c : set)
          {
            index = deck.order.indexOf(c);
            if (b.opp1_prob.get(index) != 0.0 && b.opp1_prob.get(index) < 1.0)
              b.opp1_prob.set(index, b.opp1_prob.get(index) + prob);
          }
        }
      }
    }
    else // if turn == 3
    {
      for (Bot b : botList)
      {
        for (String c : set)
        {
          index = deck.order.indexOf(c);
          if (b.opp2_prob.get(index) != 0.0 && b.opp2_prob.get(index) < 1.0)
            prob++;
        }
        if (prob > 0.0)
        {
          prob = 1/prob;
          for (String c : set)
          {
            index = deck.order.indexOf(c);
            if (b.opp2_prob.get(index) != 0.0 && b.opp2_prob.get(index) < 1.0)
              b.opp2_prob.set(index, b.opp2_prob.get(index) + prob);
          }
        }
      }
    }
  }
  
  
  
  
  // BOT FIGURING OUT IF IT SHOULD CALL A SET
  public static ArrayList<String> check_callstatus(int turn)
  {
    Bot b;
    ArrayList<ArrayList<String>> setlist = new ArrayList<ArrayList<String>>();
    setlist.add(deck.low_d_set);
    setlist.add(deck.high_d_set);
    setlist.add(deck.low_c_set);
    setlist.add(deck.high_c_set);
    setlist.add(deck.low_h_set);
    setlist.add(deck.high_h_set);
    setlist.add(deck.low_s_set);
    setlist.add(deck.high_s_set);
    setlist.add(deck.middle_set);
    if (turn == 1)
      b = partner;
    else if (turn == 2)
      b = opponent1;
    else // if turn == 3
      b = opponent2;
    
    for (ArrayList<String> set : setlist)
    {
      ArrayList<String> s = new ArrayList<String>();
      s.addAll(set);
      for (String c : set)
      {
        if (b.cards.contains(c))
          s.remove(c);
      }
      if (turn == 1)
      {
        for (String c : set)
        {
          int index = deck.order.indexOf(c);
          if (b.player_prob.get(index) == 1.0)
            s.remove(c);
        }
      }
      else if (turn == 2)
      {
        for (String c : set)
        {
          int index = deck.order.indexOf(c);
          if (b.opp2_prob.get(index) == 1.0)
            s.remove(c);
        }
      }
      else // if turn == 3
      {
        for (String c : set)
        {
          int index = deck.order.indexOf(c);
          if (b.opp1_prob.get(index) == 1.0)
            s.remove(c);
        }
      }
      if (s.isEmpty())
        return set;
    }
    return null;
  }
  
  
  
  // WHEN A BOT DECIDES TO CALL A SET
  public static void call_bot(ArrayList<String> set, int turn)
  {
    String setName;
    if (set == deck.low_d_set)
    {
      setName = "low diamonds";
      deck.low_d_ingame = false;
    }
    else if (set == deck.high_d_set)
    {
      setName = "high diamonds";
      deck.high_d_ingame = false;
    }
    else if (set == deck.low_c_set)
    {
      setName = "low clubs";
      deck.low_c_ingame = false;
    }
    else if (set == deck.high_c_set)
    {
      setName = "high clubs";
      deck.high_c_ingame = false;
    }
    else if (set == deck.low_h_set)
    {
      setName = "low hearts";
      deck.low_h_ingame = false;
    }
    else if (set == deck.high_h_set)
    {
      setName = "high hearts";
      deck.high_h_ingame = false;
    }
    else if (set == deck.low_s_set)
    {
      setName = "low spades";
      deck.low_s_ingame = false;
    }
    else if (set == deck.high_s_set)
    {
      setName = "high spades";
      deck.high_s_ingame = false;
    }
    else // if set == deck.middle_set
    {
      setName = "8s and Jokers";
      deck.middle_ingame = false;
    }
    Bot b;
    if (turn == 1)
      b = partner;
    else if (turn == 2)
      b = opponent1;
    else // if turn == 3
      b = opponent2;
      
    System.out.println(b.getName() + " called for the " + setName + ".");
    
    if (turn == 1)
    {
      scores[0]++;
      System.out.println("Success! Your team had the whole set.");
      System.out.println("Your score: " + scores[0] + " Opposing team score: " + scores[1]);
    }
    else
    {
      scores[1]++;
      System.out.println("The opposing team had the whole set.");
      System.out.println("Your score: " + scores[0] + " Opposing team score: " + scores[1]);
    }
    deleteSet(set);
  }
}