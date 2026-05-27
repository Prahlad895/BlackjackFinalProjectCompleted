public class Card {
    private String suit;
    private String value;

    public Card(String suit, String value) {
        this.suit = suit;
        this.value = value;
    }

    public int getBlackjackValue() {
        if (value.equals("A")) {
            return 11;
        } else if (value.equals("J") || value.equals("Q") || value.equals("K")) {
            return 10;
        } else {
            return Integer.parseInt(value);
        }
    }

    public String getSuit() {
        return suit;
    }

    public String getValue() {
        return value;
    }

    public String toStringGUI() {
        return value + "-" + suit;
    }

    public String getImagePath() {
        return "/cards/" + toStringGUI() + ".png";
    }
}
