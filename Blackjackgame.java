import javax.swing.*;

//greyScale Imports
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import java.awt.image.FilteredImageSource;
import java.awt.image.ImageProducer;

/**
 * The main class for the Blackjack game GUI and logic.
 * Handles game state, user interaction, and rendering.
 *
 * <p>Manages the deck, player, dealer, and all game actions including betting, splitting, doubling down, and determining winners.</p>
 */
public class Blackjackgame {
    static private User user2;
    private Deck deck;
    private Player player;
    private Player dealer;
    public int boardWidth = 600;
    public int boardHeight = 600;
    public boolean mainHandGray = false;
    public boolean forceSplit = false;

    public final int cardWidth = 110;
    public final int cardHeight = 154;

    private boolean revealDealerCard = false;
    private boolean waitingForSplitAction = false;
    private boolean handResolved = false;


    //image util class for grey scaling
    /**
     * Converts a colored image to grayscale for UI effects.
     *
     * @param colorImage The original colored Image.
     * @return The grayscale version of the image.
     * @precondition colorImage is not null.
     * @postcondition Returns a processed grayscale image of the input.
     */
    public static Image convertToGrayImage(Image colorImage) {
        GrayFilter filter = new GrayFilter(false, 50);
        ImageProducer producer = new FilteredImageSource(colorImage.getSource(), filter);
        Image grayImage = Toolkit.getDefaultToolkit().createImage(producer);
        MediaTracker tracker = new MediaTracker(new Component() {});
        tracker.addImage(grayImage, 0);
        try {
            tracker.waitForID(0); // Holds up execution until the image is 100% processed
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        return grayImage;
    }

    JFrame frame = new JFrame("Black Jack");
    JPanel gamePanel = new JPanel() {
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            // draw dealer cards
            for (int i = 0; i < dealer.getHand().size(); i++) {
                if (i == 0 && !revealDealerCard) {
                    try {
                        Image hiddenCardImage = new ImageIcon(getClass().getResource("/cards/BACK.png")).getImage();
                        g.drawImage(hiddenCardImage, 20, 20, cardWidth, cardHeight, null);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Card card = dealer.getHand().getCard(i);
                    Image cardImage = new ImageIcon(getClass().getResource(card.getImagePath())).getImage();
                    g.drawImage(cardImage, 20 + (cardWidth + 5) * i, 20, cardWidth, cardHeight, null);
                }
            }

            // draw players hand
            for (int i = 0; i < player.getHand().size(); i++) {
                Card card = player.getHand().getCard(i);
                Image cardImage = new ImageIcon(getClass().getResource(card.getImagePath())).getImage();
                if (player.hasSplitHand() && handResolved) {
                    cardImage = convertToGrayImage(cardImage); 
                }
                else{
                    mainHandGray = false;
                }
                g.drawImage(cardImage, 20 + (cardWidth + 5) * i, 250, cardWidth, cardHeight, null);
            }
            
            // Draw split hand if exists
            if (player.hasSplitHand()) {
                int frameX = frame.getX();
                int frameY = frame.getY();
                frame.setLocation(frameX, frameY - 50);
                frame.setSize(boardWidth, 800);
                for (int i = 0; i < player.getSplitHand().size(); i++) {
                    Card card = player.getSplitHand().getCard(i);
                    Image cardImage = new ImageIcon(getClass().getResource(card.getImagePath())).getImage();
                    if (waitingForSplitAction && player.hasSplitHand() && !handResolved) {
                        cardImage = convertToGrayImage(cardImage);
                    }
                    g.drawImage(cardImage, 20 + (cardWidth + 5) * i, 420, cardWidth, cardHeight, null);
                }
            }
            
            g.setColor(Color.BLACK);
            g.setFont(new Font("Inter", Font.BOLD, 20));
            g.drawString("Balance: $" + player.getBalance(), 20, 650);
            
            if (player.hasSplitHand()) {
                g.drawString("Bet: $" + player.getCurrentBet() + " | Split: $" + player.getSplitBet(), 20, 680);
            } else {
                g.drawString("Bet: $" + player.getCurrentBet(), 20, 710);
            }
        }
    };
    
    JPanel buttonPanel = new JPanel();
    JButton hitButton = new JButton("Hit");
    JButton standButton = new JButton("Stay");
    JButton doubleButton = new JButton("Double");
    JButton splitButton = new JButton("Split");

    /**
     * Constructs the Blackjackgame window and initializes the game state for a user.
     *
     * @param user The User object representing the logged-in player.
     * @precondition user is a valid, authenticated User object.
     * @postcondition Game window is initialized and ready for play.
     */
    public Blackjackgame(User user){
        user2 = user;
        deck = new Deck();
        player = new Player("Player", user2.getBalance());
        dealer = new Player("Dealer");
        frame.setVisible(true);
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        gamePanel.setLayout(new BorderLayout());
        gamePanel.setBackground(new Color(53, 101, 77));
        frame.add(gamePanel);

        hitButton.setFocusable(false);
        standButton.setFocusable(false);
        doubleButton.setFocusable(false);
        splitButton.setFocusable(false);
        
        buttonPanel.add(hitButton);
        buttonPanel.add(standButton);
        buttonPanel.add(doubleButton);
        buttonPanel.add(splitButton);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        hitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                hit();
                gamePanel.repaint();
            }
        });

        standButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                stay();
                gamePanel.repaint();
            }
        });
        
        doubleButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                doubleDown();
                gamePanel.repaint();
            }
        });
        
        splitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                split();
                gamePanel.repaint();
            }
        });
        
        gamePanel.repaint();
    }

    /**
     * Starts a new round of Blackjack.
     *
     * @precondition Game window and player are initialized.
     * @postcondition A new round is started and UI is updated.
     */
    public void start() {
        playRound();
        gamePanel.repaint();
    }

    /**
     * Handles the logic for a single round of Blackjack, including betting and dealing cards.
     *
     * @precondition Game is initialized and player is ready to bet.
     * @postcondition Player and dealer hands are dealt, bets are placed, and UI is updated.
     */
    private void playRound() {
        frame.setSize(boardWidth, boardHeight);
        player.clearHand();
        dealer.clearHand();
        hitButton.setEnabled(true);
        standButton.setEnabled(true);
        doubleButton.setEnabled(true);
        splitButton.setEnabled(true);
        waitingForSplitAction = false;
        handResolved = false;
        revealDealerCard = false;

        if (deck.cardsRemaining() < 10) {
            deck.build();
            deck.shuffle();
        }
        
        int bet = 0;
        boolean validBet = false;
        
        while (!validBet) {
            String input = JOptionPane.showInputDialog(frame, "Balance: $" + player.getBalance() + "\nEnter your bet:");
            
            if (input == null) {
                System.exit(0);
            }
            
            try {
                bet = Integer.parseInt(input.trim());
                
                if (bet <= 0) {
                    JOptionPane.showMessageDialog(frame, "Invalid bet", "Error", JOptionPane.ERROR_MESSAGE);
                } else if (bet > player.getBalance()) {
                    JOptionPane.showMessageDialog(frame, "Invalid bet", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    validBet = true;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(frame, "Invalid bet", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        player.placeBet(bet);
        user2.updateBalance(player.getBalance());
        
        if(forceSplit){
            dealIntialCardsforSplit();
        }
        else{
            dealInitialCards();
        }
        
        if (player.canSplit()) {
            splitButton.setEnabled(true);
        } else {
            splitButton.setEnabled(false);
        }
        
        if (player.getBalance() >= player.getCurrentBet()) {
            doubleButton.setEnabled(true);
        } else {
            doubleButton.setEnabled(false);
        }
        
        if (checkBlackjack()) {
            return;
        }
        
        gamePanel.repaint();
    }

    /**
     * Deals the initial two cards to both player and dealer.
     *
     * @precondition Deck is built and shuffled, player and dealer hands are empty.
     * @postcondition Both player and dealer have two cards each.
     */
    private void dealInitialCards() {
        player.receiveCard(deck.deal());
        dealer.receiveCard(deck.deal());
        player.receiveCard(deck.deal());
        dealer.receiveCard(deck.deal());
    }

    /**
     * Deals initial cards for a forced split scenario.
     *
     * @precondition Deck is built and shuffled, player hand is empty.
     * @postcondition Player receives two identical cards, dealer receives two cards.
     */
    public void dealIntialCardsforSplit() {
        Card card1 = deck.deal();
        player.receiveCard(card1);
        player.receiveCard(card1);
        dealer.receiveCard(deck.deal());
        dealer.receiveCard(deck.deal());
    }
    
    /**
     * Splits the player's hand into two if possible, and deals new cards to each hand.
     *
     * @precondition Player's hand contains two cards of the same value and sufficient balance for split bet.
     * @postcondition Player has two hands, each with two cards, and split bet is placed.
     */
    private void split() {
        if (!player.canSplit()) {
            JOptionPane.showMessageDialog(frame, "Cannot split!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        Card cardForMain = deck.deal();
        Card cardForSplit = deck.deal();
        
        player.split(cardForMain, cardForSplit);
        
        doubleButton.setEnabled(false);
        splitButton.setEnabled(false);
        
        gamePanel.repaint();
        
        if (player.getSplitHand().isBlackjack()) {
            JOptionPane.showMessageDialog(frame, "Split hand has Blackjack!", "Nice!", JOptionPane.INFORMATION_MESSAGE);
        }
        
        waitingForSplitAction = true;
    }
    
    /**
     * Doubles the player's bet and deals one final card to the hand.
     *
     * @precondition Player has sufficient balance to double the bet.
     * @postcondition Player's bet is doubled, one card is added, and round ends.
     */
    private void doubleDown() {
        if (player.getBalance() < player.getCurrentBet()) {
            JOptionPane.showMessageDialog(frame, "Cannot double down - insufficient funds!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        System.out.println("initial balance: " + player.getBalance());
        System.out.println("initial bet: " + player.getCurrentBet());
        Card newCard = deck.deal();
        player.doubleDown(newCard);
        System.out.println("new bet: " + player.getCurrentBet());
        System.out.println("new balance: " + player.getBalance());
        
        doubleButton.setEnabled(false);
        splitButton.setEnabled(false);
        
        gamePanel.repaint();

        determineWinner();
        user2.updateBalance(player.getBalance());
    }

    /**
     * Deals one card to the active hand (main or split) and checks for bust or 21.
     *
     * @precondition Player's hand is active and not bust.
     * @postcondition Card is added to hand, and game state is updated accordingly.
     */
    private void hit() {
        splitButton.setEnabled(false);
        if (!handResolved) {
            Card newCard = deck.deal();
            player.receiveCardToActive(newCard);
            
            if (player.isBust() || player.getScore() == 21) {
                System.out.print("main hand done");
                handResolved = true;
                if(!waitingForSplitAction){
                    dealerTurn();
                    determineWinner();
                };
            }
        } else if (handResolved && waitingForSplitAction) {
            Card newCard = deck.deal();
            player.receiveCardToSplit(newCard);

            if (player.getSplitHand().isBust() || player.getSplitHand().getScore() == 21) {
                    dealerTurn();
                    determineWinner();
            }
        }
        gamePanel.repaint();
    }

    /**
     * Ends the player's turn for the active hand and proceeds to dealer's turn if appropriate.
     *
     * @precondition Player's hand is active.
     * @postcondition Player's turn ends, dealer's turn may begin.
     */
    private void stay() {
        splitButton.setEnabled(false);
        if (!handResolved) {
            handResolved = true;
            if(!waitingForSplitAction){
                dealerTurn();
                determineWinner();
            };
        } else if (handResolved && waitingForSplitAction) {
                dealerTurn();
                determineWinner();
        }
        gamePanel.repaint();
    }

    /**
     * Handles the dealer's turn, drawing cards until the dealer's score is at least 17.
     *
     * @precondition Dealer's hand is initialized.
     * @postcondition Dealer's hand is complete according to Blackjack rules.
     */
    private void dealerTurn() {
        revealDealerCard = true;
        while (dealer.getScore() < 17) {
            Card newCard = deck.deal();
            dealer.receiveCard(newCard);
        }
        gamePanel.repaint();
    }

    /**
     * Checks if either the player or dealer has Blackjack at the start of the round.
     *
     * @return true if either player or dealer has Blackjack, false otherwise.
     * @precondition Both player and dealer have two cards.
     * @postcondition Bets are resolved if Blackjack is found.
     */
    private boolean checkBlackjack() {
        boolean playerBJ = player.isBlackjack();
        boolean dealerBJ = dealer.isBlackjack();

        if (playerBJ && dealerBJ) {
            player.tieBet();
            promptReplay("Both have Blackjack! It's a tie!");
            return true;
        } else if (playerBJ) {
            player.winBet();
            promptReplay("Blackjack! You win!");
            return true;
        } else if (dealerBJ) {
            dealer.winBet();
            promptReplay("Dealer has Blackjack! Dealer wins!");
            return true;
        }
        return false;
    }

    /**
     * Determines the winner of the round, resolves bets, and updates balances.
     *
     * @precondition Both player and dealer have completed their turns.
     * @postcondition Bets are resolved and balances updated.
     */
    private void determineWinner() {
        int playerScore = player.getScore();
        int dealerScore = dealer.getScore();
        boolean playerBust = player.isBust();
        boolean dealerBust = dealer.isBust();
        
        String resultMessage = "";
        String splitResultMessage = "";
        
        if (player.hasSplitHand()) {
            int splitScore = player.getSplitHand().getScore();
            boolean splitBust = player.getSplitHand().isBust();
            
            if (splitBust && dealerBust) {
             //   player.addTie();
                player.tieSplitBet();
                splitResultMessage += "Split hand: Both busted! It's a tie!";
            } else if (splitBust) {
             //   player.addLoss();
                splitResultMessage += "Split hand: You busted! Dealer wins!";
            } else if (dealerBust) {
             //   player.addWin();
                player.winSplitBet();
                splitResultMessage += "Split hand: Dealer busted! You win!";
            } else if (splitScore > dealerScore) {
               // player.addWin();
                player.winSplitBet();
                splitResultMessage += "Split hand: You win!";
            } else if (dealerScore > splitScore) {
               // player.addLoss();
                splitResultMessage += "Split hand: Dealer wins!";
            } else {
               // player.addTie();
                player.tieSplitBet();
                splitResultMessage += "Split hand: It's a tie!";
            }
        }
        
        if (playerBust && dealerBust) {
        //   player.addTie();
        //   dealer.addTie();
            player.tieBet();
            resultMessage = "Main hand: Both busted! It's a tie!";
        } else if (playerBust) {
        //   dealer.addWin();
        // player.addLoss();
            resultMessage = "Main hand: You busted! Dealer wins!";
        } else if (dealerBust) {
        // player.addWin();
        // dealer.addLoss();
            player.winBet();
            resultMessage = "Main hand: Dealer busted! You win!";
        } else if (playerScore > dealerScore) {
        //  player.addWin();
        // dealer.addLoss();
            player.winBet();
            resultMessage = "Main hand: You win!";
        } else if (dealerScore > playerScore) {
        // dealer.addWin();
        // player.addLoss();
            resultMessage = "Main hand: Dealer wins!";
        } else {
        //  player.addTie();
        //  dealer.addTie();
            player.tieBet();
            resultMessage = "Main hand: It's a tie!";
        }
        

        if (user2 != null) {
            user2.updateBalance(player.getBalance());
        }
        if (!splitResultMessage.isEmpty()) {
            promptReplay(resultMessage + "\n" + splitResultMessage);
        }
        else{
            promptReplay(resultMessage);
        }
    }

    /**
     * Prompts the user to play again or exit after a round ends.
     *
     * @param message The message to display in the dialog.
     * @precondition A round has ended and results are available.
     * @postcondition Game either restarts or exits based on user input.
     */
    private void promptReplay(String message) {
        hitButton.setEnabled(false);
        standButton.setEnabled(false);
        doubleButton.setEnabled(false);
        splitButton.setEnabled(false);
        gamePanel.repaint();

        Timer timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int response = JOptionPane.showConfirmDialog(frame, message + "\nPlay again?", "Game Over", JOptionPane.YES_NO_OPTION);
                if (response == JOptionPane.YES_OPTION) {
                    revealDealerCard = false;
                    playRound();
                    gamePanel.repaint();
                } else {
                    System.exit(0);
                }
            }
        });
        timer.setRepeats(false);
        timer.start();
    }
}