/**
 * Represents a playing card with a suit and value for Blackjack.
 * Provides methods to get Blackjack value, suit, value, and image path.
 */
public class Card {
    private String suit;
    private String value;

    /**
     * Constructs a Card with the given suit and value.
     *
     * @param suit The suit of the card (S, H, D, C).
     * @param value The value of the card (2-10, J, Q, K, A).
     * @precondition suit and value are valid strings.
     * @postcondition Card object is created.
     */
    public Card(String suit, String value) {
        this.suit = suit;
        this.value = value;
    }

    /**
     * Gets the Blackjack value of the card.
     *
     * @return The integer value for Blackjack rules.
     * @precondition Card value is set.
     * @postcondition No change to card state.
     */
    public int getBlackjackValue() {
        if (value.equals("A")) {
            return 11;
        } else if (value.equals("J") || value.equals("Q") || value.equals("K")) {
            return 10;
        } else {
            return Integer.parseInt(value);
        }
    }

    /**
     * Gets the suit of the card.
     *
     * @return The suit string.
     * @precondition Card is initialized.
     * @postcondition No change to card state.
     */
    public String getSuit() {
        return suit;
    }

    /**
     * Gets the value of the card.
     *
     * @return The value string.
     * @precondition Card is initialized.
     * @postcondition No change to card state.
     */
    public String getValue() {
        return value;
    }

    /**
     * Returns a string representation for GUI display.
     *
     * @return String in the format value-suit.
     * @precondition Card is initialized.
     * @postcondition No change to card state.
     */
    public String toStringGUI() {
        return value + "-" + suit;
    }

    /**
     * Gets the image path for the card's GUI representation.
     *
     * @return The image path string.
     * @precondition Card is initialized.
     * @postcondition No change to card state.
     */
    public String getImagePath() {
        return "/cards/" + toStringGUI() + ".png";
    }
}
