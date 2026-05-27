import java.util.Stack;
import java.util.Collections;
import java.util.ArrayList;

/**
 * Represents a deck of playing cards for Blackjack.
 * Provides methods to build, shuffle, deal, and check the deck state.
 */
public class Deck {
    private Stack<Card> cards;

    private static final String[] suits = {"S", "H", "D", "C"};
    private static final String[] values = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"};

    /**
     * Constructs a new deck, builds and shuffles it.
     *
     * @precondition None.
     * @postcondition Deck is built and shuffled.
     */
    public Deck() {
        cards = new Stack<>();
        build();
        shuffle();
    }

    /**
     * Builds a standard 52-card deck.
     *
     * @precondition None.
     * @postcondition Deck contains 52 cards.
     */
    public void build() {
        cards.clear();
        for (int suit = 0; suit < suits.length; suit++) {
            for (int val = 0; val < values.length; val++) {
                cards.push(new Card(suits[suit], values[val]));
            }
        }
    }

    /**
     * Shuffles the deck randomly.
     *
     * @precondition Deck is built.
     * @postcondition Deck order is randomized.
     */
    public void shuffle() {
        ArrayList<Card> deck = new ArrayList<>(cards);
        Collections.shuffle(deck);
        cards.clear();
        for (int i = 0; i < deck.size(); i++) {
            cards.push(deck.get(i));
        }
    }

    /**
     * Deals (removes and returns) the top card from the deck.
     *
     * @return The top Card from the deck.
     * @precondition Deck is not empty.
     * @postcondition Deck size is reduced by one.
     */
    public Card deal() {
        return cards.pop();
    }

    /**
     * Gets the number of cards remaining in the deck.
     *
     * @return The number of cards left.
     * @precondition None.
     * @postcondition No change to deck state.
     */
    public int cardsRemaining() {
        return cards.size();
    }

    /**
     * Checks if the deck is empty.
     *
     * @return true if the deck is empty, false otherwise.
     * @precondition None.
     * @postcondition No change to deck state.
     */
    public boolean isEmpty() {
        return cards.isEmpty();
    }
}
