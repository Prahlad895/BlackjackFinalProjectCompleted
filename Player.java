/**
 * Represents a player in the Blackjack game.
 * Handles hand management, betting, splitting, and balance operations.
 */
public class Player {
    private String name;
    private Hand hand;
    private Hand splitHand;
    private boolean playingSplitHand;
    private int balance;
    private int currentBet;
    private int splitBet;

    /**
     * Constructs a Player with a name and starting balance.
     *
     * @param name The player's name.
     * @param balance The starting balance for the player.
     * @precondition name is not null; balance is non-negative.
     * @postcondition Player is initialized with a new hand and balance.
     */
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
    
    /**
     * Constructs a Player with a name and default balance.
     *
     * @param name The player's name.
     * @precondition name is not null.
     * @postcondition Player is initialized with a new hand and default balance.
     */
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
    /**
     * Adds a card to the player's main hand.
     *
     * @param card The card to add.
     * @precondition card is not null.
     * @postcondition Card is added to the main hand.
     */
    public void receiveCard(Card card) {
        hand.addCard(card);
    }

    /**
     * Adds a card to the player's split hand.
     *
     * @param card The card to add.
     * @precondition splitHand is not null; card is not null.
     * @postcondition Card is added to the split hand.
     */
    public void receiveCardToSplit(Card card) {
        if (splitHand != null) {
            splitHand.addCard(card);
        }
    }

    /**
     * Gets the score of the player's main hand.
     *
     * @return The score of the main hand.
     * @precondition hand is initialized.
     * @postcondition No change to player state.
     */
    public int getScore() {
        return hand.getScore();
    }
    /**
     * Checks if the player's main hand is bust.
     *
     * @return true if bust, false otherwise.
     * @precondition hand is initialized.
     * @postcondition No change to player state.
     */
    public boolean isBust() {
        return hand.isBust();
    }

    /**
     * Checks if the player's main hand is a Blackjack.
     *
     * @return true if Blackjack, false otherwise.
     * @precondition hand is initialized.
     * @postcondition No change to player state.
     */
    public boolean isBlackjack() {
        return hand.isBlackjack();
    }

    /**
     * Clears the player's hands and resets split state and bets.
     *
     * @precondition None.
     * @postcondition Both hands are empty, split state and bets reset.
     */
    public void clearHand() {
        hand.clear();
        splitHand = null;
        playingSplitHand = false;
        splitBet = 0;
    }

    /**
     * Gets the player's name.
     *
     * @return The player's name.
     * @precondition Player is initialized.
     * @postcondition No change to player state.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the player's main hand.
     *
     * @return The main Hand object.
     * @precondition Player is initialized.
     * @postcondition No change to player state.
     */
    public Hand getHand() {
        return hand;
    }


    /**
     * Gets the player's current balance.
     *
     * @return The player's balance.
     * @precondition Player is initialized.
     * @postcondition No change to player state.
     */
    public int getBalance() {
        return balance;
    }

    /**
     * Sets the player's balance.
     *
     * @param balance The new balance to set.
     * @precondition balance is non-negative.
     * @postcondition Player's balance is updated.
     */
    public void setBalance(int balance) {
        this.balance = balance;
    }

    /**
     * Gets the player's current bet amount.
     *
     * @return The current bet.
     * @precondition Player is initialized.
     * @postcondition No change to player state.
     */
    public int getCurrentBet() {
        return currentBet;
    }

    /**
     * Sets the player's current bet amount.
     *
     * @param amount The amount to set as current bet.
     * @return The updated current bet.
     * @precondition amount is non-negative.
     * @postcondition Player's current bet is updated.
     */
    public int setCurrentBet(int amount) {
        currentBet = amount;
        return currentBet;
    }

    /**
     * Places a bet by deducting the amount from the player's balance.
     *
     * @param amount The amount to bet.
     * @precondition amount > 0 and amount <= balance.
     * @postcondition Bet is placed and balance is reduced.
     */
    public void placeBet(int amount) {
        if (amount > 0 && amount <= balance) {
            currentBet = amount;
            balance -= amount;
        }
    }

    /**
     * Doubles the player's current bet and adds a card to the hand.
     *
     * @param card The card to add.
     * @precondition balance >= currentBet.
     * @postcondition Bet is doubled, card is added, and balance is reduced.
     */
    public void doubleDown(Card card) {
        if (balance >= currentBet) {
            balance -= currentBet;
            currentBet *= 2;
            hand.addCard(card);
        }
    }

    /**
     * Checks if the player can split their hand.
     *
     * @return true if split is possible, false otherwise.
     * @precondition Player has two cards of the same value and enough balance.
     * @postcondition No change to player state.
     */
    public boolean canSplit() {
        if (hand.size() != 2 || splitHand != null) return false;
        if (balance < currentBet) return false;
        Card c1 = hand.getCard(0);
        Card c2 = hand.getCard(1);
        return c1.getValue().equals(c2.getValue());
    }

    /**
     * Splits the player's hand into two and deals new cards to each.
     *
     * @param cardForMain Card to add to the main hand.
     * @param cardForSplit Card to add to the split hand.
     * @precondition canSplit() returns true.
     * @postcondition Player has two hands and split bet is placed.
     */
    public void split(Card cardForMain, Card cardForSplit) {
        splitHand = new Hand();
        Card splitCard = hand.getCards().remove(1);
        hand.addCard(cardForMain);
        splitHand.addCard(splitCard);
        splitHand.addCard(cardForSplit);
        splitBet = currentBet;
        balance -= splitBet;
    }

    /**
     * Checks if the player currently has a split hand.
     *
     * @return true if split hand exists, false otherwise.
     * @precondition Player is initialized.
     * @postcondition No change to player state.
     */
    public boolean hasSplitHand() {
        return splitHand != null;
    }

    /**
     * Checks if the player is currently playing the split hand.
     *
     * @return true if playing split hand, false otherwise.
     * @precondition Player is initialized.
     * @postcondition No change to player state.
     */
    public boolean isPlayingSplitHand() {
        return playingSplitHand;
    }

    /**
     * Switches the active hand to the split hand.
     *
     * @precondition splitHand is not null.
     * @postcondition playingSplitHand is set to true.
     */
    public void switchToSplitHand() {
        playingSplitHand = true;
    }

    /**
     * Gets the player's split hand.
     *
     * @return The split Hand object.
     * @precondition splitHand is not null.
     * @postcondition No change to player state.
     */
    public Hand getSplitHand() {
        return splitHand;
    }

    /**
     * Gets the player's split bet amount.
     *
     * @return The split bet amount.
     * @precondition Player has split hand.
     * @postcondition No change to player state.
     */
    public int getSplitBet() {
        return splitBet;
    }

    /**
     * Gets the currently active hand (main or split).
     *
     * @return The active Hand object.
     * @precondition Player is initialized.
     * @postcondition No change to player state.
     */
    public Hand getActiveHand() {
        return playingSplitHand ? splitHand : hand;
    }

    /**
     * Adds a card to the currently active hand.
     *
     * @param card The card to add.
     * @precondition getActiveHand() is not null; card is not null.
     * @postcondition Card is added to the active hand.
     */
    public void receiveCardToActive(Card card) {
        getActiveHand().addCard(card);
    }

    /**
     * Gets the score of the currently active hand.
     *
     * @return The score of the active hand.
     * @precondition getActiveHand() is not null.
     * @postcondition No change to player state.
     */
    public int getActiveScore() {
        return getActiveHand().getScore();
    }

    /**
     * Checks if the currently active hand is bust.
     *
     * @return true if active hand is bust, false otherwise.
     * @precondition getActiveHand() is not null.
     * @postcondition No change to player state.
     */
    public boolean isActiveBust() {
        return getActiveHand().isBust();
    }

    /**
     * Awards winnings for the main bet to the player's balance.
     *
     * @precondition Player has won the main bet.
     * @postcondition Balance is increased by twice the current bet.
     */
    public void winBet() {
        balance += currentBet * 2;
    }

    /**
     * Awards winnings for the split bet to the player's balance.
     *
     * @precondition Player has won the split bet.
     * @postcondition Balance is increased by twice the split bet.
     */
    public void winSplitBet() {
        balance += splitBet * 2;
    }

    /**
     * Returns the main bet to the player's balance in case of a tie.
     *
     * @precondition Main hand is a tie.
     * @postcondition Balance is increased by the current bet.
     */
    public void tieBet() {
        balance += currentBet;
    }

    /**
     * Returns the split bet to the player's balance in case of a tie.
     *
     * @precondition Split hand is a tie.
     * @postcondition Balance is increased by the split bet.
     */
    public void tieSplitBet() {
        balance += splitBet;
    }

    /**
     * Handles loss of the split bet (no action needed as bet is already deducted).
     *
     * @precondition Player has lost the split bet.
     * @postcondition No change to balance (bet already deducted).
     */
    public void loseSplitBet() {
        // splitBet already deducted on split
    }
}
