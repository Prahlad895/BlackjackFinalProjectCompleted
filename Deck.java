import java.util.Stack;
import java.util.Collections;
import java.util.ArrayList;

public class Deck {
    private Stack<Card> cards;

    private static final String[] suits = {"S", "H", "D", "C"};
    private static final String[] values = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"};

    public Deck() {
        cards = new Stack<>();
        build();
        shuffle();
    }

    public void build() {
        cards.clear();
        for (int suit = 0; suit < suits.length; suit++) {
            for (int val = 0; val < values.length; val++) {
                cards.push(new Card(suits[suit], values[val]));
            }
        }
    }

    public void shuffle() {
        ArrayList<Card> deck = new ArrayList<>(cards);
        Collections.shuffle(deck);
        cards.clear();
        for (int i = 0; i < deck.size(); i++) {
            cards.push(deck.get(i));
        }
    }

    public Card deal() {
        return cards.pop();
    }

    public int cardsRemaining() {
        return cards.size();
    }

    public boolean isEmpty() {
        return cards.isEmpty();
    }
}
