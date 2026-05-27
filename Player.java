public class Player {
    private String name;
    private Hand hand;
    private Hand splitHand;
    private boolean playingSplitHand;
    private int balance;
    private int currentBet;
    private int splitBet;

    public Player(String name, int balance) {
        this.name = name;
        this.balance = balance;
        this.hand = new Hand();
        this.splitHand = null;
        this.playingSplitHand = false;
        balance = 1000;
        currentBet = 0;
        splitBet = 0;
    }
    
    public Player(String name) {
        this.name = name;
        this.balance = 1000;
        this.hand = new Hand();
        this.splitHand = null;
        this.playingSplitHand = false;
        balance = 1000;
        currentBet = 0;
        splitBet = 0;
    }
    public void receiveCard(Card card) {
        hand.addCard(card);
    }

    public void receiveCardToSplit(Card card) {
        if (splitHand != null) {
            splitHand.addCard(card);
        }
    }

    public int getScore() {
        return hand.getScore();
    }
    public boolean isBust() {
        return hand.isBust();
    }

    public boolean isBlackjack() {
        return hand.isBlackjack();
    }

    public void clearHand() {
        hand.clear();
        splitHand = null;
        playingSplitHand = false;
        splitBet = 0;
    }

    public String getName() {
        return name;
    }

    public Hand getHand() {
        return hand;
    }


    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public int getCurrentBet() {
        return currentBet;
    }

    public int setCurrentBet(int amount) {
        currentBet = amount;
        return currentBet;
    }

    public void placeBet(int amount) {
        if (amount > 0 && amount <= balance) {
            currentBet = amount;
            balance -= amount;
        }
    }

    public void doubleDown(Card card) {
        if (balance >= currentBet) {
            balance -= currentBet;
            currentBet *= 2;
            hand.addCard(card);
        }
    }

    public boolean canSplit() {
        if (hand.size() != 2 || splitHand != null) return false;
        if (balance < currentBet) return false;
        Card c1 = hand.getCard(0);
        Card c2 = hand.getCard(1);
        return c1.getValue().equals(c2.getValue());
    }

    public void split(Card cardForMain, Card cardForSplit) {
        splitHand = new Hand();
        Card splitCard = hand.getCards().remove(1);
        hand.addCard(cardForMain);
        splitHand.addCard(splitCard);
        splitHand.addCard(cardForSplit);
        splitBet = currentBet;
        balance -= splitBet;
    }

    public boolean hasSplitHand() {
        return splitHand != null;
    }

    public boolean isPlayingSplitHand() {
        return playingSplitHand;
    }

    public void switchToSplitHand() {
        playingSplitHand = true;
    }

    public Hand getSplitHand() {
        return splitHand;
    }

    public int getSplitBet() {
        return splitBet;
    }

    public Hand getActiveHand() {
        return playingSplitHand ? splitHand : hand;
    }

    public void receiveCardToActive(Card card) {
        getActiveHand().addCard(card);
    }

    public int getActiveScore() {
        return getActiveHand().getScore();
    }

    public boolean isActiveBust() {
        return getActiveHand().isBust();
    }

    public void winBet() {
        balance += currentBet * 2;
    }

    public void winSplitBet() {
        balance += splitBet * 2;
    }

    public void tieBet() {
        balance += currentBet;
    }

    public void tieSplitBet() {
        balance += splitBet;
    }

    public void loseSplitBet() {
        // splitBet already deducted on split
    }
}
