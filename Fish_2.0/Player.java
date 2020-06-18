import java.util.ArrayList;
import java.util.Collections;

public class Player
{
  private String name;
  public ArrayList<String> cards;
  public int turn;
  
  public Player(String name, ArrayList<String> cards) // gets 14 cards
  {
    this.name = name;
    this.cards = cards;
    turn = 0;
  }
  
  public void sortCards()
  {
    Deck deck = new Deck();
    ArrayList<String> sorted = new ArrayList<String>();
    for (String c : deck.order)
    {
      if (cards.contains(c))
        sorted.add(c);
    }
    cards = sorted;
  }
  
  public String checkCards()
  {
    String r = "\nYour cards are:\n";
    for (String c : cards)
    {
      r += c + "\n";
    }
    return r;
  }
  
  public String getName()
  {
    return name;
  }
}