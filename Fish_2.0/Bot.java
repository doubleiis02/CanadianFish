import java.util.ArrayList;

public class Bot
{
  private String name;
  public ArrayList<String> cards;
  private int numCards;
  public int turn;
  
  public ArrayList<Double> player_prob;
  public ArrayList<Double> partner_prob;
  public ArrayList<Double> opp1_prob;
  public ArrayList<Double> opp2_prob;
  
  public Bot(String name, ArrayList<String> cards, int numCards, int turn)
  {
    this.name = name;
    this.cards = cards;
    this.numCards = numCards;
    this.turn = turn;
    
    // one of these will be useless bc it corresponds to the person themself
    player_prob = new ArrayList<Double>();
    partner_prob = new ArrayList<Double>();
    opp1_prob = new ArrayList<Double>(); 
    opp2_prob = new ArrayList<Double>(); 
    
    double prob;
    if (numCards == 13)
      prob = 1.0/41;
    else
      prob = 1.0/40;
    
    Deck deck = new Deck();
    for (String currentCard : deck.order)
    {
      if (cards.contains(currentCard))
      {
        player_prob.add(0.0);
        partner_prob.add(0.0);
        opp1_prob.add(0.0);
        opp2_prob.add(0.0);
      }
      else
      {
        player_prob.add(prob);
        partner_prob.add(prob);
        opp1_prob.add(prob);
        opp2_prob.add(prob);
      }
    }
  }
  
  public String getName()
  {
    return name;
  }
}