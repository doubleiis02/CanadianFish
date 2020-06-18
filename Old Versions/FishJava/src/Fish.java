package FishJava.src;

import java.util.*;

public class Fish
{
    // MAIN METHOD
    public static void main(String[] args)
    {
        Scanner input = new Scanner(System.in);
        Cards cards = new Cards();
        System.out.println("Hi there. What would you like to do? \nType r to read the rules\nType s to start a new game\nType q to quit\n");
        String status = input.nextLine();
        while (!status.equals("q"))
        {
            if (status.equals("s"))
            {
                System.out.println("What is your name?");
                String name = input.nextLine();
                System.out.println("Hi "+ name);
                cards.shuffle();
                // setup players
                Player player = new Player(name, cards.whole_deck.subList(0, 14));
                Carl carl = new Carl(cards.whole_deck.subList(14, 27));
                Bob bob = new Bob(cards.whole_deck.subList(27, 40));
                Amy amy = new Amy(cards.whole_deck.subList(40, 54));

                HashMap<String, Integer> turns = new HashMap<>();
                turns.put("Player", 0); turns.put("Carl", 1); turns.put("Bob", 2); turns.put("Amy", 3);
                int[] scores = {0, 0}; // scores, pos 0 = teamscore, pos 1 = oppscore

                player.sortCards();
                System.out.print(player.checkCards());
                initializeHyps(player, carl, bob, amy);
                System.out.println("The game will begin. Your team member is Carl.");
                boolean gameover = false;
                int turn = 0;

            }

        }

    }


    // INITIALIZING HYPS BEFORE GAME STARTS
    public static void initializeHyps(Player player, Carl carl, Bob bob, Amy amy)
    {
        Cards c = new Cards();
        for (int i = 0; i < c.whole_deck.size(); i++)
        {
            if (carl.cards.contains(c.whole_deck.get(i)))
            {
                carl.player_hyp.put(c.whole_deck.get(i), 0.0);
                carl.amy_hyp.put(c.whole_deck.get(i), 0.0);
                carl.bob_hyp.put(c.whole_deck.get(i), 0.0);
            }
            if (amy.cards.contains(c.whole_deck.get(i)))
            {
                amy.player_hyp.put(c.whole_deck.get(i), 0.0);
                amy.carl_hyp.put(c.whole_deck.get(i), 0.0);
                amy.bob_hyp.put(c.whole_deck.get(i), 0.0);
            }
            if (bob.cards.contains(c.whole_deck.get(i)))
            {
                bob.player_hyp.put(c.whole_deck.get(i), 0.0);
                bob.carl_hyp.put(c.whole_deck.get(i), 0.0);
                bob.amy_hyp.put(c.whole_deck.get(i), 0.0);
            }
        }
    }


    // FINDING THE SET THAT A CARD BELONGS TO
    public static List<String> whichSet(String card, Cards cards)
    {
        if (cards.low_d_set.contains(card))
            return cards.low_d_set;
        else if (cards.high_d_set.contains(card))
            return cards.high_d_set;
        else if (cards.low_c_set.contains(card))
            return cards.low_c_set;
        else if (cards.high_c_set.contains(card))
            return cards.high_c_set;
        else if (cards.low_s_set.contains(card))
            return cards.low_s_set;
        else if (cards.high_s_set.contains(card))
            return cards.high_s_set;
        else if (cards.low_h_set.contains(card))
            return cards.low_h_set;
        else if (cards.high_h_set.contains(card))
            return cards.high_h_set;
        else
            return cards.middle_set;
    }


    // DELETING ALL CARDS OF A SET AFTER A TEAM HAS DECLARED IT
    public static void deleteSet(List<String> set, Player player, Carl carl, Bob bob, Amy amy)
    {
        Cards cards = new Cards();
        for (int i = 0; i < cards.whole_deck.size(); i++)
        {
            String c = cards.whole_deck.get(i);
            if (set.contains(c))
            {
                // updating hyps
                carl.player_hyp.put(c, 0.0);
                carl.bob_hyp.put(c, 0.0);
                carl.amy_hyp.put(c, 0.0);
                bob.player_hyp.put(c, 0.0);
                bob.carl_hyp.put(c, 0.0);
                bob.amy_hyp.put(c, 0.0);
                amy.player_hyp.put(c, 0.0);
                amy.carl_hyp.put(c, 0.0);
                amy.bob_hyp.put(c, 0.0);
                // taking out cards
                player.cards.remove(c);
                carl.cards.remove(c);
                bob.cards.remove(c);
                amy.cards.remove(c);
            }
        }
    }


    // WHEN PLAYER ASKS SOMEONE FOR A CARD
    public static int ask(String card, String who_to_ask, Player player, Carl carl, Bob bob, Amy amy, HashMap<String, Integer> turns)
    {
        Scanner input = new Scanner(System.in);
        Cards cards = new Cards();
        // converting who_to_ask from a String to player type
        Player personAsked = new Player("null", cards.whole_deck);
        if (who_to_ask.equals(player.name))
            personAsked = player;
        if (who_to_ask.equals(carl.name))
            personAsked = carl;
        if (who_to_ask.equals(bob.name))
            personAsked = bob;
        if (who_to_ask.equals(amy.name))
            personAsked = amy;

        // if person put in an invalid person to ask
        if (personAsked == player || personAsked == carl || personAsked.cards.isEmpty())
        {
            System.out.println("You cannot ask this person.");
            who_to_ask = input.nextLine();
            // converting who_to_ask from a String to player type AGAIN
            if (who_to_ask.equals(player.name))
                personAsked = player;
            else if (who_to_ask.equals(carl.name))
                personAsked = carl;
            else if (who_to_ask.equals(bob.name))
                personAsked = bob;
            else
                personAsked = amy;

            if (personAsked == player || personAsked == carl || personAsked.cards.isEmpty())
            {
                System.out.println("You're stupid. You lose your turn.");
                return (int)(Math.random()*2) + 2;
            }
        }
        if (player.cards.contains(card))
        {
            System.out.println("You have that card and therefore cannot ask someone else for it.");
            carl.player_hyp.put(card, 1.0);
            bob.player_hyp.put(card, 1.0);
            amy.player_hyp.put(card, 1.0);
            System.out.println("Which card will you ask for?");
            card = input.nextLine();
            if (player.cards.contains(card))
            {
                carl.player_hyp.put(card, 1.0);
                bob.player_hyp.put(card, 1.0);
                amy.player_hyp.put(card,1.0);
                System.out.println("You're stupid. You lose your turn.");
                return (int)(Math.random()*2) + 2;
            }
        }
        List<String> set = whichSet(card, cards);
        boolean playerHasSet = false;
        for (int i = 0; i < player.cards.size(); i++)
        {
            if (set.contains(player.cards.get(i)))
            {
                playerHasSet = true;
                break;
            }
        }

        if (playerHasSet)
        {
            if (personAsked.cards.contains(card))
            {
                player.cards.add(card);
                personAsked.cards.remove(card);
                System.out.println("You took the " + card + " from " + who_to_ask);
                carl.player_hyp.put(card, 1.0);
                bob.player_hyp.put(card, 1.0);
                amy.player_hyp.put(card, 1.0);
                if (who_to_ask.equals("Bob"))
                {
                    carl.bob_hyp.put(card, 0.0);
                    amy.bob_hyp.put(card, 0.0);
                }
                if (who_to_ask.equals("Amy"))
                {
                    carl.amy_hyp.put(card, 0.0);
                    bob.amy_hyp.put(card, 0.0);
                }

                // changing carl.player_hyp(other cards in set that player might have)
                double num = 0, n;
                for (int i = 0; i < cards.whole_deck.size(); i++)
                {
                    String c = cards.whole_deck.get(i);
                    if (!c.equals(card) && set.contains(c) && !carl.cards.contains(c)
                            && carl.player_hyp.get(c) != 0 && carl.player_hyp.get(c) != 1)
                    {
                        num += 1;
                    }
                }
                if (num > 0)
                {
                    n = 1 / num;
                    for (int i = 0; i < cards.whole_deck.size(); i++)
                    {
                        String c = cards.whole_deck.get(i);
                        n = (carl.player_hyp.get(c) + n) / 2;
                        if (!c.equals(card) && set.contains(c) && !carl.cards.contains(c)
                                && carl.player_hyp.get(c) != 0 && carl.player_hyp.get(c) != 1)
                        {
                            carl.player_hyp.put(c, n);
                        }

                    }
                }

                // changing bob.player_hyp(other cards in set that player might have)
                num = 0;
                for (int i = 0; i < cards.whole_deck.size(); i++)
                {
                    String c = cards.whole_deck.get(i);
                    if (!c.equals(card) && set.contains(c) && !bob.cards.contains(c)
                            && bob.player_hyp.get(c) != 0 && bob.player_hyp.get(c) != 1)
                    {
                        num += 1;
                    }
                }
                if (num > 0)
                {
                    n = 1 / num;
                    for (int i = 0; i < cards.whole_deck.size(); i++)
                    {
                        String c = cards.whole_deck.get(i);
                        n = (bob.player_hyp.get(c) + n) / 2;
                        if (!c.equals(card) && set.contains(c) && !bob.cards.contains(c)
                                && bob.player_hyp.get(c) != 0 && bob.player_hyp.get(c) != 1)
                        {
                            bob.player_hyp.put(c, n);
                        }

                    }
                }

                // changing amy.player_hyp(other cards in set that player might have)
                num = 0;
                for (int i = 0; i < cards.whole_deck.size(); i++)
                {
                    String c = cards.whole_deck.get(i);
                    if (!c.equals(card) && set.contains(c) && !amy.cards.contains(c)
                            && amy.player_hyp.get(c) != 0 && amy.player_hyp.get(c) != 1)
                    {
                        num += 1;
                    }
                }
                if (num > 0)
                {
                    n = 1 / num;
                    for (int i = 0; i < cards.whole_deck.size(); i++)
                    {
                        String c = cards.whole_deck.get(i);
                        n = (amy.player_hyp.get(c) + n) / 2;
                        if (!c.equals(card) && set.contains(c) && !amy.cards.contains(c)
                                && amy.player_hyp.get(c) != 0 && amy.player_hyp.get(c) != 1)
                        {
                            amy.player_hyp.put(c, n);
                        }

                    }
                }
                return turns.get("Player");
            }
            else
            {
                System.out.println(who_to_ask + " does not have that card.");
                carl.player_hyp.put(card, 0.0);
                amy.player_hyp.put(card, 0.0);
                bob.player_hyp.put(card, 0.0);
                if (who_to_ask.equals("Bob"))
                {
                    carl.bob_hyp.put(card, 0.0);
                    amy.bob_hyp.put(card, 0.0);
                }
                if (who_to_ask.equals("Amy"))
                {
                    carl.amy_hyp.put(card, 0.0);
                    bob.amy_hyp.put(card, 0.0);
                }

                )// changing carl.player_hyp(other cards in set that player might have)
                double num = 0, n;
                for (int i = 0; i < cards.whole_deck.size(); i++)
                {
                    String c = cards.whole_deck.get(i);
                    if (!c.equals(card) && set.contains(c) && !carl.cards.contains(c)
                            && carl.player_hyp.get(c) != 0 && carl.player_hyp.get(c) != 1)
                    {
                        num += 1;
                    }
                }
                if (num > 0)
                {
                    n = 1 / num;
                    for (int i = 0; i < cards.whole_deck.size(); i++)
                    {
                        String c = cards.whole_deck.get(i);
                        n = (carl.player_hyp.get(c) + n) / 2;
                        if (!c.equals(card) && set.contains(c) && !carl.cards.contains(c)
                                && carl.player_hyp.get(c) != 0 && carl.player_hyp.get(c) != 1)
                        {
                            carl.player_hyp.put(c, n);
                        }

                    }
                }

                // changing bob.player_hyp(other cards in set that player might have)
                num = 0;
                for (int i = 0; i < cards.whole_deck.size(); i++)
                {
                    String c = cards.whole_deck.get(i);
                    if (!c.equals(card) && set.contains(c) && !bob.cards.contains(c)
                            && bob.player_hyp.get(c) != 0 && bob.player_hyp.get(c) != 1)
                    {
                        num += 1;
                    }
                }
                if (num > 0)
                {
                    n = 1 / num;
                    for (int i = 0; i < cards.whole_deck.size(); i++)
                    {
                        String c = cards.whole_deck.get(i);
                        n = (bob.player_hyp.get(c) + n) / 2;
                        if (!c.equals(card) && set.contains(c) && !bob.cards.contains(c)
                                && bob.player_hyp.get(c) != 0 && bob.player_hyp.get(c) != 1)
                        {
                            bob.player_hyp.put(c, n);
                        }

                    }
                }

                // changing amy.player_hyp(other cards in set that player might have)
                num = 0;
                for (int i = 0; i < cards.whole_deck.size(); i++)
                {
                    String c = cards.whole_deck.get(i);
                    if (!c.equals(card) && set.contains(c) && !amy.cards.contains(c)
                            && amy.player_hyp.get(c) != 0 && amy.player_hyp.get(c) != 1)
                    {
                        num += 1;
                    }
                }
                if (num > 0)
                {
                    n = 1 / num;
                    for (int i = 0; i < cards.whole_deck.size(); i++)
                    {
                        String c = cards.whole_deck.get(i);
                        n = (amy.player_hyp.get(c) + n) / 2;
                        if (!c.equals(card) && set.contains(c) && !amy.cards.contains(c)
                                && amy.player_hyp.get(c) != 0 && amy.player_hyp.get(c) != 1)
                        {
                            amy.player_hyp.put(c, n);
                        }

                    }
                }
                return turns.get("Player");
            }
        }
        else
        {
            System.out.println("You have chosen to ask for a card in a set you do not have. Your turn is over.");
            for (int i = 0; i < cards.whole_deck.size(); i++)
            {
                if (set.contains(cards.whole_deck.get(i)))
                {
                    carl.player_hyp.put(cards.whole_deck.get(i), 0.0);
                    bob.player_hyp.put(cards.whole_deck.get(i), 0.0);
                    amy.player_hyp.put(cards.whole_deck.get(i), 0.0);
                }
            }
            return turns.get(who_to_ask);
        }
    }

    // WHEN PLAYER DECIDES TO CALL A SET
    public static int[] call(int[] scores, Cards cards, Player player, Carl carl, Bob bob, Amy amy)
    {
        // choosing which set
        Scanner input = new Scanner(System.in);
        List<Integer> possibleSets = new ArrayList<>();
        System.out.println("Which value would you like to call?");
        if (cards.low_h_ingame)
        {
            System.out.println("Type 1 for low hearts");
            possibleSets.add(1);
        }
        if (cards.low_c_ingame)
        {
            System.out.println("Type 2 for low clubs");
            possibleSets.add(2);
        }
        if (cards.low_s_ingame)
        {
            System.out.println("Type 3 for low spades");
            possibleSets.add(3);
        }
        if (cards.low_d_ingame)
        {
            System.out.println("Type 4 for low diamonds");
            possibleSets.add(4);
        }
        if (cards.high_h_ingame)
        {
            System.out.println("Type 5 for high hearts");
            possibleSets.add(5);
        }
        if (cards.high_c_ingame)
        {
            System.out.println("Type 6 for high clubs");
            possibleSets.add(6);
        }
        if (cards.high_s_ingame)
        {
            System.out.println("Type 7 for high spades");
            possibleSets.add(7);
        }
        if (cards.high_d_ingame)
        {
            System.out.println("Type 8 for high diamonds");
            possibleSets.add(8);
        }
        if (cards.middle_ingame)
        {
            System.out.println("Type 9 for 8s and Jokers\n");
            possibleSets.add(9);
        }
        int inp = input.nextInt();

        if (!possibleSets.contains(inp))
        {
            System.out.println("Try again. Type a number between 1 to 9.");
            inp = input.nextInt();
        }


        // turning number into set
        List<String> set = cards.whole_deck;
        switch(inp)
        {
            case 1:
                set = cards.low_h_set;
                cards.low_h_ingame = false;
                break;
            case 2:
                set = cards.low_c_set;
                cards.low_c_ingame = false;
                break;
            case 3:
                set = cards.low_s_set;
                cards.low_s_ingame = false;
                break;
            case 4:
                set = cards.low_d_set;
                cards.low_d_ingame = false;
                break;
            case 5:
                set = cards.high_h_set;
                cards.high_h_ingame = false;
                break;
            case 6:
                set = cards.high_c_set;
                cards.high_c_ingame = false;
                break;
            case 7:
                set = cards.high_s_set;
                cards.high_s_ingame = false;
                break;
            case 8:
                set = cards.high_d_set;
                cards.high_d_ingame = false;
                break;
            case 9:
                set = cards.middle_set;
                cards.middle_ingame = false;
        }

        // upon calling....

        for (int i = 0; i < set.size(); i++)
        {
            String c = set.get(i);
            if (!player.cards.contains(c) && !carl.cards.contains(c))
            {
                scores[1] += 1; // add 1 to oppscore
                System.out.println("Oh no :( Your team did not have the whole set. Opposing team gets the set. Your score: " + scores[0] + " Opposing team score: " + scores[1]);
                deleteSet(set, player, carl, bob, amy);
                return scores;
            }
        }
        scores[0] += 1; // add 1 to teamscore
        System.out.println("Success! Your team had the whole set. Your score: " + scores[0] + " Opposing team score: " + scores[1]);
        deleteSet(set, player, carl, bob, amy);
        return scores;
    }

    // WHEN A BOT NEEDS TO CHOOSE WHAT CARD TO ASK FOR
    public static String[] choose_card(Player bot, Player player, Carl carl, Bob bob, Amy amy)
    {
        // return the person to ask and the card....as a String[]
        List<Player> playersToAsk = new ArrayList<>();
        List<String> emptycards = new ArrayList<>();
        if (bot == Carl)
        {
            if (amy.cards.equals(emptycards))
                playersToAsk.add(bob);
            else if (bob.cards.equals(emptycards))
                playersToAsk.add(amy);
            else
            {
                playersToAsk.add(bob);
                playersToAsk.add(amy);
            }
        }
        else
        {
            if (carl.cards.equals(emptycards))
                playersToAsk.add(player);
            else if (player.cards.equals(emptycards))
                playersToAsk.add(carl);
            else
            {
                playersToAsk.add(player); playersToAsk.add(carl);
            }
        }

        // looking for ones


        // looking for the next greatest values

    }

}
