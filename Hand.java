import java.util.ArrayList;

public class Hand {
    private ArrayList<Card> cards;

    public Hand() {
        cards = new ArrayList<>();
    }

    public void addCard(Card card) {
        cards.add(card);
    }

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

    public boolean isBust() {
        return getScore() > 21;
    }

    public boolean isBlackjack() {
        return cards.size() == 2 && getScore() == 21;
    }

    public void clear() {
        cards.clear();
    }

    public int size() {
        return cards.size();
    }

    public Card getCard(int index) {
        return cards.get(index);
    }

    public ArrayList<Card> getCards() {
        return cards;
    }
}
