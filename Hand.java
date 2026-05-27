import java.util.ArrayList;

/**
 * Represents a hand of cards in Blackjack.
 * Provides methods to add cards, calculate score, and check hand state.
 */
public class Hand {
    private ArrayList<Card> cards;

    /**
     * Constructs an empty hand.
     *
     * @precondition None.
     * @postcondition Hand is initialized and empty.
     */
    public Hand() {
        cards = new ArrayList<>();
    }

    /**
     * Adds a card to the hand.
     *
     * @param card The card to add.
     * @precondition card is not null.
     * @postcondition Card is added to the hand.
     */
    public void addCard(Card card) {
        cards.add(card);
    }

    /**
     * Calculates the total score of the hand, accounting for Aces as 1 or 11.
     *
     * @return The score of the hand.
     * @precondition Hand is initialized.
     * @postcondition No change to hand state.
     */
    public int getScore() {
        int score = 0;
        int aceCount = 0;

        for (Card card : cards) {
            score += card.getBlackjackValue();
            if (card.getValue().equals("A")) {
                aceCount++;
            }
        }
        while (score > 21 && aceCount > 0) {
            score -= 10;
            aceCount--;
        }

        return score;
    }

    /**
     * Checks if the hand is bust (score > 21).
     *
     * @return true if bust, false otherwise.
     * @precondition Hand is initialized.
     * @postcondition No change to hand state.
     */
    public boolean isBust() {
        return getScore() > 21;
    }

    /**
     * Checks if the hand is a Blackjack (two cards totaling 21).
     *
     * @return true if Blackjack, false otherwise.
     * @precondition Hand is initialized.
     * @postcondition No change to hand state.
     */
    public boolean isBlackjack() {
        return cards.size() == 2 && getScore() == 21;
    }

    /**
     * Clears all cards from the hand.
     *
     * @precondition None.
     * @postcondition Hand is empty.
     */
    public void clear() {
        cards.clear();
    }

    /**
     * Gets the number of cards in the hand.
     *
     * @return The number of cards.
     * @precondition Hand is initialized.
     * @postcondition No change to hand state.
     */
    public int size() {
        return cards.size();
    }

    /**
     * Gets the card at the specified index in the hand.
     *
     * @param index The index of the card.
     * @return The Card at the given index.
     * @precondition index is valid (0 <= index < size()).
     * @postcondition No change to hand state.
     */
    public Card getCard(int index) {
        return cards.get(index);
    }

    /**
     * Gets the list of cards in the hand.
     *
     * @return The ArrayList of cards.
     * @precondition Hand is initialized.
     * @postcondition No change to hand state.
     */
    public ArrayList<Card> getCards() {
        return cards;
    }
}
