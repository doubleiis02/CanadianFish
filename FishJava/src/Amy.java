import java.util.*;

public class Amy extends Player
{
    public String name = "Amy";
    public List<String> cards;

    public HashMap<String, Double> carl_hyp = new HashMap<>(), bob_hyp = new HashMap<>(), player_hyp = new HashMap<>();


    public Amy(List<String> c)
    {
        this.cards = c;
        Cards cards = new Cards();
        for (int i = 0; i < cards.whole_deck.size(); i++)
        {
            String card = cards.whole_deck.get(i);
            player_hyp.put(card, (double)(1/54));
            carl_hyp.put(card, (double)(1/54));
            bob_hyp.put(card, (double)(1/54));
        }
    }
}
