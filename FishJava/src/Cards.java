import java.util.*;

public class Cards
{
    public List<String> low_d_set = new ArrayList<>(List.of("2 of diamonds","3 of diamonds","4 of diamonds","5 of diamonds","6 of diamonds","7 of diamonds")),
            high_d_set = new ArrayList<>(List.of("9 of diamonds", "10 of diamonds", "jack of diamonds", "queen of diamonds", "king of diamonds", "ace of diamonds")),
            low_c_set = new ArrayList<>(List.of("2 of clubs","3 of clubs","4 of clubs","5 of clubs","6 of clubs","7 of clubs")),
            high_c_set = new ArrayList<>(List.of("9 of clubs","10 of clubs","jack of clubs","queen of clubs","king of clubs","ace of clubs")),
            low_h_set = new ArrayList<>(List.of("2 of hearts","3 of hearts","4 of hearts","5 of hearts","6 of hearts","7 of hearts")),
            high_h_set = new ArrayList<>(List.of("9 of hearts","10 of hearts","jack of hearts","queen of hearts","king of hearts","ace of hearts")),
            low_s_set = new ArrayList<>(List.of("2 of spades","3 of spades","4 of spades","5 of spades","6 of spades","7 of spades")),
            high_s_set = new ArrayList<>(List.of("9 of spades","10 of spades","jack of spades","queen of spades","king of spades","ace of spades")),
            middle_set = new ArrayList<>(List.of("black joker", "red joker", "8 of diamonds", "8 of hearts", "8 of spades", "8 of clubs")),
            whole_deck = new ArrayList<>(), order = new ArrayList<>();
    public String rules = "There are no rules available as of now. Sorry!";

    public boolean low_d_ingame = true, low_c_ingame = true,low_h_ingame = true,low_s_ingame = true,
            high_d_ingame = true,high_c_ingame = true,high_h_ingame = true,high_s_ingame = true,middle_ingame = true;

    public Cards()
    {
        whole_deck.addAll(low_c_set); whole_deck.addAll(low_d_set); whole_deck.addAll(low_h_set); whole_deck.addAll(low_s_set);
        whole_deck.addAll((high_c_set)); whole_deck.addAll(high_d_set); whole_deck.addAll(high_h_set); whole_deck.addAll(high_s_set); whole_deck.addAll(middle_set);
        order.addAll(whole_deck);
    }

    public void shuffle()
    {
        Collections.shuffle(whole_deck);
    }

}
