import java.util.ArrayList;

public interface GameMethods
{
  // METHODS
   
  
  /* method: main
   * parameters: n/a
   * return: void
   * runs the game
   */
  public void main(String[] args); 
  
  /* method: whichSet
   * parameters: String (a card)
   * return: ArrayList<String> (a set)
   * finds the set that a card belongs to
   */
  public ArrayList<String> whichSet(String card);
  
  /* method: deleteSet
   * parameters: ArrayList<String> (a set)
   * return: void
   * deletes all the cards and probs of a set after the set is called
   */
  public void deleteSet(ArrayList<String> set);
  
  /* method: ask
   * parameters: String (a card), String (the person that player is asking a card from)
   * return: int (the next turn)
   * when the player decides to ask a person for a card
   */
  public int ask(String card, String who_to_ask);
  
  /* method: call
   * parameters: int[] (represents current scores)
   * return: int[] (represents new scores)
   * when the player decides to call for a set
   */
  public int[] call(int[] scores);
  
  /* method: ask_bot
   * parameters: int (the current turn)
   * return: int (the next turn)
   * when a bot asks for a card
   */
  public int ask_bot(int turn);
  
  /* method: check_callstatus
   * parameters: int (turn corresponding to the bot)
   * return: ArrayList<String> (a set) OR null (if there is no set to be called)
   * when a bot needs to figure out if it should call a set
   */
  public ArrayList<String> check_callstatus(int turn);
  
  /* method: call_bot
   * parameters: ArrayList<String> (a set), int (the current turn OR the turn corresponding to bot)
   * return: void
   * when a bot decides to call for a set
   */
  public void call_bot(ArrayList<String> set, int turn);
  
  
  // HELPER METHODS
  

  /* helper method: statusQuestion
   * parameters: none
   * return: String ("r", "s", or "q")
   * creates a loop for the status input before the game starts
   */
  public String statusQuestion();
  
  /* helper method: optionQuestion
   * parameters: none
   * return: String ("a", "c", or "q")
   * creates a loop for the option input during player's turn
   */
  public String optionQuestion();
  
  /* helper method: personHasSet
   * parameters: int (the current turn), ArrayList<String> (a set)
   * return: boolean (whether or not the person already has a card in the set that the card that they are asking for is in)
   * checks if a person is allowed to ask for the card they are asking for
   */
  public boolean personHasSet(int turn, ArrayList<String> set);
  
  /* helper method: adjustProbsPlayer
   * parameters: ArrayList<String> (a set)
   * return: void
   * adjusts probs for other cards in the set that player might have after player asks for a card
   */
  public void adjustProbsPlayer(ArrayList<String> set);
  
  /* helper method: choose_card
   * parameters: int (the current turn)
   * return; String[] (contains the name of person to ask & the card)
   * when a bot needs to choose which card to ask for
   */
  public String[] choose_card(int turn);
  
  /* helper method: adjustProbsBot
   * parameters: ArrayList<String> (a set), int (the current turn)
   * return: void
   * adjusts probs for other cards that the current bot might have
   */
  public void adjustProbsBot(ArrayList<String> set, int turn);
}