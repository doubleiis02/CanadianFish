package FishJava.src;

import java.lang.reflect.Array;
import java.util.*;

public class Player
{
    public String name;
    public List<String> cards;


    public Player(String name, List<String> cards)
    {
        this.name = name;
        this.cards = cards;
    }

    public void sortCards()
    {
        Collections.sort(cards);
    }

    public String checkCards()
    {
        String r = "\nYour cards are:\n";
        for (int i = 0; i < cards.size(); i++)
        {
            r += cards.get(i);
            r += "\n";
        }
        return r;
    }

}
