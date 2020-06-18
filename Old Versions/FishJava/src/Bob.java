package FishJava.src;

import java.util.*;

public class Bob extends Player
{
    public String name = "Bob";
    public List<String> cards;

    public HashMap<String, Double> carl_hyp = new HashMap<>(), amy_hyp = new HashMap<>(), player_hyp = new HashMap<>();


    public Bob(List<String> c)
    {
        this.cards = c;
        Cards cards = new Cards();
        for (int i = 0; i < cards.whole_deck.size(); i++)
        {
            String card = cards.whole_deck.get(i);
            player_hyp.put(card, (double)(1/54));
            carl_hyp.put(card, (double)(1/54));
            amy_hyp.put(card, (double)(1/54));
        }
    }
}
