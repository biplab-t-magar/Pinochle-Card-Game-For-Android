/************************************************************
 * Name:  Biplab Thapa Magar                                *
 * Project:  Pinochle in Java/Android                       *
 * Class:  OPL Fall 2020                                    *
 * Date:  11/18/2020                                        *
 ************************************************************/

package com.example.pinochleinandroid.models;

import com.example.pinochleinandroid.models.cards.Card;
import com.example.pinochleinandroid.models.cards.Deck;
import com.example.pinochleinandroid.models.cards.GroupOfCards;
import com.example.pinochleinandroid.models.cards.Meld;
import com.example.pinochleinandroid.models.cards.MeldInstance;
import com.example.pinochleinandroid.models.cards.Rank;
import com.example.pinochleinandroid.models.cards.Suit;
import com.example.pinochleinandroid.models.players.Computer;
import com.example.pinochleinandroid.models.players.Human;
import com.example.pinochleinandroid.models.players.Player;
import com.example.pinochleinandroid.models.utilities.Serialization;
import com.example.pinochleinandroid.models.utilities.StringUtilities;

import java.io.FileOutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class GameModel {
    public static final int NUM_OF_PLAYERS = 2;
    public static final String LEAD_CARD_STAGE = "lead_card_stage";
    public static final String CHASE_CARD_STAGE = "chase_card_stage";
    public static final String MELD_STAGE = "meld_stage";
    public static final String INTERMISSION_STAGE = "intermission_stage";
    public static final String ROUND_END_STAGE = "round_end_stage";
    public static final String OUT_OF_CARDS_STAGE = "out_of_cards_stage";
    public static final int HUMAN_PLAYER = 1;
    public static final int COMPUTER_PLAYER = 0;

    private int roundNumber;
    private Player[] players;
    private int[] roundScores;
    private int[] gameScores;
    private Deck stock;
    private Card trumpCard;
    private Suit trumpSuit;
    private Boolean humansTurn;
    private Boolean humansLeadThrow;
    private Card currentLeadCard;
    private Card currentChaseCard;
    private MeldInstance currentMeld;
    private String coinTossResult;
    private String[] messages;
    private String currentStage;
    private String[] loadedHandStrs;
    private String [] loadedCaptureStrs;
    private String [] loadedMeldStrs;
    private String loadedStockStr;

    /**
     * The default constructor for GameModel class
     */
    public GameModel() {
        //initialize all fields
        roundNumber = -1;
        players = new Player[NUM_OF_PLAYERS];
        roundScores = new int[NUM_OF_PLAYERS];
        gameScores = new int[NUM_OF_PLAYERS];
        stock = null;
        trumpCard = null;
        trumpSuit = null;
        humansTurn = null;
        humansLeadThrow = null;
        currentLeadCard = null;
        currentChaseCard = null;
        currentMeld = null;
        coinTossResult = null;
        messages = new String[NUM_OF_PLAYERS];
        currentStage = null;
        loadedHandStrs = new String[NUM_OF_PLAYERS];
        loadedCaptureStrs = new String[NUM_OF_PLAYERS];
        loadedMeldStrs = new String[NUM_OF_PLAYERS];
        loadedStockStr = "";
    }

    /**
     * sets the GameModel object up for a brand new game of Pinochle
     */
    public void startNewGame() {
        roundNumber = 1;
        //assign game and round scores to 0
        for (int i = 0; i < NUM_OF_PLAYERS; i++) {
            gameScores[i] = 0;
        }
        setUpNewRound();
    }

    /**
     * Returns the current round number in a game of Pinochle
     * @return the current round number in a game of Pinochle
     */
    public int getRoundNumber() {
        return roundNumber;
    }

    /**
     * Returns the current round scores for each player in a round of Pinochle
     * @return the current round scores for each player in a round of Pinochle
     */
    public int[] getRoundScores() {
        return roundScores;
    }

    /**
     * Returns the current game scores for each player in a game of Pinochle
     * @return the current game scores for each player in a game of Pinochle
     */
    public int[] getGameScores() {
        return gameScores;
    }

    /**
     * Returns the current messages to be displayed for each player in a game of Pinochle
     * @return the current messages to be displayed for each player in a game of Pinochle
     */
    public String[] getMessages() {
        return messages;
    }

    /**
     * Informs whether or not it is currently the human's turn to make a move
     * @return true if it is the human's turn, false otherwise
     */
    public Boolean isHumansTurn() {
        return humansTurn;
    }

    /**
     * Informs whether or not the current round had the human player as the lead player
     * @return ture if the current round had the human player as the lead player, false otherwise
     */
    public Boolean isHumansLeadThrow() {
        return humansLeadThrow;
    }

    /**
     * Returns the string representation of the trump card for the current round of Pinochle
     * @return the string representation of the trump card for the current round of Pinochle
     */
    public String getTrumpCardString() {
        if(trumpCard != null) {
            return trumpCard.getShortCardStr();
        } else {
            return " " + trumpSuit.getString().substring(0, 1);
        }
    }

    /**
     * Informs what player currently has the highest score in a round of Pinochle
     * @return the player (represented by an integer) that currently has the highest score in a round of Pinochle, -1 if there is no leader
     */
    public int roundLeader() {
        if (roundScores[HUMAN_PLAYER] > roundScores[COMPUTER_PLAYER]) {
            return HUMAN_PLAYER;
        } else if(roundScores[HUMAN_PLAYER] < roundScores[COMPUTER_PLAYER]) {
            return COMPUTER_PLAYER;
        } else {
            return -1;
        }
    }

    /**
     * Informs what player currently has the highest score in a game of Pinochle
     * @return the player (represented by an integer) that currently has the highest score in a game of Pinochle, -1 if there is no leader
     */
    public int gameLeader() {
        if (gameScores[HUMAN_PLAYER] > gameScores[COMPUTER_PLAYER]) {
            return HUMAN_PLAYER;
        } else if(gameScores[HUMAN_PLAYER] < gameScores[COMPUTER_PLAYER]) {
            return COMPUTER_PLAYER;
        } else {
            return -1;
        }
    }

    /**
     * Tosses a coin and determines who the winner of the coin toss is and updates the game model accordingly
     * @param coinTossPrediction the prediction made by the human on what the coin toss will result in
     * return the result of the coin toss
     */
    public String tossCoin(String coinTossPrediction) {
        Random rand = new Random();
        if (rand.nextInt(2) == 0) {
            coinTossResult = "heads";
        } else {
            coinTossResult = "tails";
        }

        //if the human player accurately called the coin toss, then assign him as the next player
        humansTurn = coinTossPrediction.equals(coinTossResult);
        humansLeadThrow = humansTurn;
        urgeNextMove();

        return coinTossResult;
    }


    /**
     * returns the hands of all the players playing the game of Pinochle
     * @return an ArrayList of the hands of all the players
     */
    public ArrayList<ArrayList<String>> getPlayerHands() {
        ArrayList<ArrayList<String>> playerHands = new ArrayList<>();
        ArrayList<String> handCardStrings;
        ArrayList<Card> handCards;
        for(int i = 0; i < NUM_OF_PLAYERS; i++) {
            handCardStrings = new ArrayList<>();
            handCards = players[i].getHand().getCards();
            for(int j = 0; j < handCards.size(); j++) {
                handCardStrings.add(handCards.get(j).getShortCardStr());
            }
            playerHands.add(handCardStrings);
        }
        return playerHands;
    }

    /**
     * returns the capture pile of all the players playing the game of Pinochle
     * @return an ArrayList of the capture pile of all the players
     */
    public ArrayList<ArrayList<String>> getPlayerCapturePiles() {
        ArrayList<ArrayList<String>> playerCapturePiles = new ArrayList<>();
        ArrayList<String> captureCardStrings;
        ArrayList<Card> captureCards;
        for(int i = 0; i < NUM_OF_PLAYERS; i++) {
            captureCardStrings = new ArrayList<>();
            captureCards = players[i].getCapturePile().getCards();
            for(int j = 0; j < captureCards.size(); j++) {
                captureCardStrings.add(captureCards.get(j).getShortCardStr());
            }
            playerCapturePiles.add(captureCardStrings);
        }
        return playerCapturePiles;
    }

    /**
     * returns the melds of all the players playing the game of Pinochle
     * @return an ArrayList of the melds of all the players
     */
    public ArrayList<ArrayList<String>> getPlayerMelds() {
        ArrayList<ArrayList<String>> playerMelds = new ArrayList<>();

        for(int i = 0; i < NUM_OF_PLAYERS; i++) {
            playerMelds.add(getMeldsByPlayer(i));
        }
        return playerMelds;
    }

    /**
     * returns the stock pile
     * @return the stock pile
     */
    public ArrayList<String> getStock() {
        ArrayList<String> stockString = new ArrayList<>();
        ArrayList<Card> stockCards;

        stockCards = stock.getAllRemainingCards();

        for(int i = 0; i < stockCards.size(); i++) {
            stockString.add(stockCards.get(i).getShortCardStr());
        }

        return stockString;
    }

    /**
     * returns the current lead card in the current round of pinochle
     * @return the current lead card in the current round of pinochle
     */
    public ArrayList<String> getLeadCard() {
        ArrayList<String> leadCard = new ArrayList<>();
        if(currentLeadCard == null) {
            return null;
        }
        leadCard.add(currentLeadCard.getShortCardStr());
        return leadCard;
    }

    /**
     * returns the current chase card in the current round of pinochle
     * @return the current chase card in the current round of pinochle
     */
    public ArrayList<String> getChaseCard() {
        if(currentChaseCard == null) {
            return null;
        }
        ArrayList<String> chaseCard = new ArrayList<>();
        chaseCard.add(currentChaseCard.getShortCardStr());
        return chaseCard;
    }

    /**
     * returns the latest meld played by the latest winner of a turn in the current round of pinochle
     * @return the latest meld played by the latest winner of a turn in the current round of pinochle
     */
    public ArrayList<String> getMeldCards() {
        if(currentMeld == null) {
            return null;
        }
        ArrayList<String> meldCards = new ArrayList<>();
        for (int i = 0; i < currentMeld.getNumOfCards(); i++) {
            meldCards.add(currentMeld.getCardByPosition(i).getShortCardStr());
        }
        return meldCards;
    }

    /**
     * returns the current stage that a game of Pinochle is in
     * @return the current stage that a game of Pinochle is in
     */
    public String getCurrentStage() {
        return currentStage;
    }

    /**
     * Generates the serialized data from the current state of the game model that will be used to write to a save file
     * @return the serialized data from the current state of the game model that will be used to write to a save file
     */
    public String generateSaveGameData() {

        //prepare data to save to file
        String saveData = "";

        Serialization [] serializations = new Serialization[NUM_OF_PLAYERS];

        for(int i = 0; i < NUM_OF_PLAYERS; i++) {
            serializations[i] = new Serialization();
            serializations[i].setPlayerObjects(players[i].getHand(), players[i].getMeldsPlayed(), players[i].getCapturePile());
        }

        //round number
        saveData = saveData + "Round: " + roundNumber + "\n\n";
        //player data
        for(int i = 0; i < NUM_OF_PLAYERS; i++) {
            saveData = saveData + (i == 0 ? "Computer:" : "Human:") + "\n";
            saveData = saveData + "   Score: " + gameScores[i] + " / " + roundScores[i] + "\n";
            saveData = saveData + "   Hand: " + serializations[i].getHandString() + "\n";
            saveData = saveData + "   Capture Pile: " + serializations[i].getCaptureString() + "\n";
            saveData = saveData + "   Melds: " + serializations[i].getMeldString() + "\n\n";
        }

        if(stock.getNumRemaining() == 0) {
            saveData = saveData + "Trump Card: " + trumpSuit.getShortString() + "\n";
        } else {
            saveData = saveData + "Trump Card: " + trumpCard.getShortCardStr() + "\n";
        }

        ArrayList<Card> stockCards = stock.getAllRemainingCards();
        saveData = saveData + "Stock: ";
        //the card at the top of the stock is the card at the end of the vector
        for(int i = stockCards.size() - 1; i >= 0; i--) {
            saveData = saveData + stockCards.get(i).getShortCardStr() + " ";
        }
        saveData = saveData + "\n\n";
        saveData = saveData + "Next Player: " + (humansTurn ? "Human" : "Computer");

        return saveData;
    }



    /**
     * sets the GameModel object state (to resume a game of Pinochle) by parsing data obtained from a save file
     * @param saveFileData the serialized data from the save file
     */
    public void loadGame(String saveFileData) {
        trumpCard = new Card();

        loadGameData(saveFileData);

        //once game data has been loaded, we can start forming objects from our serializations

        //first, create all the cards in the game by creating a dummy deck, which automatically generates all cards
        Deck deck = new Deck();
        //empty out the deck into a GroupOfCards object
        GroupOfCards allCards = new GroupOfCards();
        //to hold cards temporarily
        ArrayList<Card> cardsHolder;
        while(deck.getNumRemaining() > 0) {
            allCards.addCard(deck.takeOneFromTop());
        }
        //now, get a vector of all the stock pile cards from the string
        //this only creates "ghost" cards, with no ids, only rank and suit defined
        ArrayList<Card> stockCards = StringUtilities.strToVectorOfCards(loadedStockStr);

        stock = new Deck();
        stock.clear();

        //create the final stock pile by transferring over matching cards from allCards
        //it is very important here to add the cards in reverse order from how they were listed (so we need to go from right to left)
        //this is beause stock::putCardAtTop() adds the card to the "top" of the pile each time
        for(int i = stockCards.size() - 1; i >= 0; i--) {
            //take the first instance of a matching card returned from allCards
            cardsHolder = allCards.getCardsByRankAndSuit(stockCards.get(i).getRank(), stockCards.get(i).getSuit());
            stockCards.set(i, cardsHolder.get(0));
            //remove the card from allCards
            allCards.removeCardById(stockCards.get(i).getId());
            stock.putCardAtTop(stockCards.get(i));
        }

        //now, we must create the player objects
        //first, deserialize the strings
        //important to receive the version of allCards returned by the Serialization::setPlayerStrings in order to reflect true stock pile
        Serialization [] szs = new Serialization[NUM_OF_PLAYERS];
        for(int i = 0; i < NUM_OF_PLAYERS; i++) {
            szs[i] = new Serialization();
            allCards = szs[i].setPlayerStrings(loadedHandStrs[i], loadedMeldStrs[i], loadedCaptureStrs[i], allCards, trumpSuit);
        }

        //now create the player objects
        players[COMPUTER_PLAYER] = new Computer(szs[COMPUTER_PLAYER].getHand(), szs[COMPUTER_PLAYER].getCapturePile(), szs[COMPUTER_PLAYER].getMeldsPlayed(), trumpSuit);

        players[HUMAN_PLAYER] = new Human(szs[HUMAN_PLAYER].getHand(), szs[HUMAN_PLAYER].getCapturePile(), szs[HUMAN_PLAYER].getMeldsPlayed(), trumpSuit);

        //set the remaining model fields
        humansLeadThrow = humansTurn;
        currentStage = LEAD_CARD_STAGE;

        if(humansTurn) {
            messages[HUMAN_PLAYER] = "It is the human player's turn to throw a lead card";
            messages[COMPUTER_PLAYER] = "";
        } else {
            messages[COMPUTER_PLAYER] = "It is the computer player's turn to throw a lead card";
            messages[HUMAN_PLAYER] = "";
        }
    }

    /**
     * Informs the model to update itself to a state representing the next state in a game of pinochle
     */
    public void goToNextStep() {
        if(currentStage == null) {
            return;
        }

        if(currentStage.equals(LEAD_CARD_STAGE) && !humansTurn) {
            computerThrowsLeadCard();
        } else if(currentStage.equals(CHASE_CARD_STAGE) && !humansTurn) {
            computerThrowsChaseCard();
        } else if (currentStage.equals(MELD_STAGE) && !humansTurn) {
            if(players[COMPUTER_PLAYER].isMeldPossible()) {
                computerPlaysMeld();
            } else {
                nextTurn();
            }
        } else if (currentStage.equals(INTERMISSION_STAGE)) {
            //if it is time to decide winner between chase and lead cards
            if(currentChaseCard != null && currentLeadCard != null) {
                decideTurnWinner();
            } else if (currentMeld == null && currentChaseCard == null && currentLeadCard == null) {
                currentStage = MELD_STAGE;
                urgeNextMove();
            } else if(currentMeld != null) {
                //if a meld has been played by the round winner
                currentMeld = null;
                nextTurn();
            }
        } else if(currentStage.equals(OUT_OF_CARDS_STAGE)) {
            currentStage = ROUND_END_STAGE;
            roundNumber++;
            //increment game scores
            for(int i = 0; i < NUM_OF_PLAYERS; i++) {
                gameScores[i] += roundScores[i];
            }
        }
    }



    /**
     * sets up the GameModel object state for a new round of Pinochle
     */
    public void setUpNewRound() {
        //create players
        //player in index 0 is the computer player
        players[COMPUTER_PLAYER] = new Computer();
        players[HUMAN_PLAYER] = new Human();

        for (int i = 0; i < NUM_OF_PLAYERS; i++) {
            roundScores[i] = 0;
            messages[i] = "";
        }

        //create the stock pile
        stock = new Deck();

        //distribute cards to each player
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                players[HUMAN_PLAYER].takeOneCard(stock.takeOneFromTop());
            }
            for (int j = 0; j < 4; j++) {
                players[COMPUTER_PLAYER].takeOneCard(stock.takeOneFromTop());
            }
        }

        //pick trump card
        trumpCard = stock.takeOneFromTop();
        trumpSuit = trumpCard.getSuit();
        players[COMPUTER_PLAYER].setTrumpSuit(trumpSuit);
        players[HUMAN_PLAYER].setTrumpSuit(trumpSuit);

        //finding out which player goes first
        if(gameScores[HUMAN_PLAYER] < gameScores[COMPUTER_PLAYER]) {
            //if the computer has the higher score so far, human does not go first
            humansTurn = false;
            humansLeadThrow = false;
        } else if (gameScores[HUMAN_PLAYER] > gameScores[COMPUTER_PLAYER])  {
            //if the human has the higher score so far, the human goes first
            humansTurn = true;
            humansLeadThrow = true;
        } else {
            //if human and computer player have the same score, then coin toss is needed
            humansTurn = null;
            humansLeadThrow = null;
        }

        currentStage = LEAD_CARD_STAGE;

        urgeNextMove();

    }



    /**
     * Generates a hint to show to the human player
     */
    public void generateHint() {
        if(currentStage.equals(LEAD_CARD_STAGE) && currentLeadCard == null) {
            players[HUMAN_PLAYER].getHelpForLeadCard();
        } else if (currentStage.equals(CHASE_CARD_STAGE) && currentLeadCard != null) {
            players[HUMAN_PLAYER].getHelpForChaseCard(currentLeadCard);
        } else if(currentStage.equals(MELD_STAGE) && currentMeld == null) {
            players[HUMAN_PLAYER].getHelpForMeld();
        }

        messages[HUMAN_PLAYER] = players[HUMAN_PLAYER].getMessage();
    }

    /**
     * Used to inform the game model that a human player has thrown a lead card
     * @param cardPosition the position in hand of the card played by the human player
     */
    public void humanThrowsLeadCard(int cardPosition) {
        //if no card was selected
        if(cardPosition == -1) {
            messages[HUMAN_PLAYER] = "You did not select a lead card. You must select a card to throw.";
        } else {
            currentLeadCard = players[HUMAN_PLAYER].playLeadCard(cardPosition);
            messages[HUMAN_PLAYER] = players[HUMAN_PLAYER].getMessage();
            //switch turns
            humansTurn = false;
            //change stage
            currentStage = CHASE_CARD_STAGE;
            urgeNextMove();
        }
    }

    /**
     * Used to inform the game model that a human player has thrown a chase card
     * @param cardPosition the position in hand of the card played by the human player
     */
    public void humanThrowsChaseCard(int cardPosition) {
        //if no card was selected
        if(cardPosition == -1) {
            messages[HUMAN_PLAYER] = "You did not select a chase card. You must select a card to throw.";
        } else {
            currentChaseCard = players[HUMAN_PLAYER].playChaseCard(currentLeadCard, cardPosition);
            messages[HUMAN_PLAYER] = players[HUMAN_PLAYER].getMessage();
            currentStage = INTERMISSION_STAGE;
        }
    }

    /**
     * Used to inform the game model that a human player has played a meld
     * @param cardPositions the positions in hand of the cards played by the human player to create a meld
     */
    public void humanPlaysMeld(ArrayList<Integer> cardPositions) {
        if( cardPositions == null || cardPositions.size() == 0) {
            messages[HUMAN_PLAYER] = "You must select at least one card to create a meld.";
        } else {
            MeldInstance meldInstance = players[HUMAN_PLAYER].playMeld(cardPositions);
            //if the cards did not add up to a meld
            if (meldInstance != null) {
                currentMeld = meldInstance;
                currentStage = INTERMISSION_STAGE;
                roundScores[HUMAN_PLAYER] += meldInstance.getMeldPoints();
                messages[HUMAN_PLAYER] = players[HUMAN_PLAYER].getMessage();
                messages[HUMAN_PLAYER] += " You won " + currentMeld.getMeldPoints() + " points for it.";
            } else {
                messages[HUMAN_PLAYER] = players[HUMAN_PLAYER].getMessage();
            }


        }
    }

    /**
     * Used to inform the game model that a computer player should thrown a lead card
     */
    private void computerThrowsLeadCard() {
        currentLeadCard = players[COMPUTER_PLAYER].playLeadCard(-1);
        messages[COMPUTER_PLAYER] = players[COMPUTER_PLAYER].getMessage();
        //switch turns
        humansTurn = true;
        //change stage
        currentStage = CHASE_CARD_STAGE;
        urgeNextMove();
    }

    /**
     * Used to inform the game model that a computer player should thrown a chase card
     */
    private void computerThrowsChaseCard() {
        currentChaseCard = players[COMPUTER_PLAYER].playChaseCard(currentLeadCard, -1);
        messages[COMPUTER_PLAYER] = players[COMPUTER_PLAYER].getMessage();
        currentStage = INTERMISSION_STAGE;
    }

    /**
     * Used to inform the game model that a computer player should played a meld
     */
    public void computerPlaysMeld() {
        if(!players[COMPUTER_PLAYER].isMeldPossible()) {
            return;
        }
        MeldInstance meldInstance = players[COMPUTER_PLAYER].playMeld(null);
        //if the cards did not add up to a meld
        currentMeld = meldInstance;
        currentStage = INTERMISSION_STAGE;
        messages[COMPUTER_PLAYER] = players[COMPUTER_PLAYER].getMessage();
        messages[COMPUTER_PLAYER] += " It won " + currentMeld.getMeldPoints() + " points for it.";
        roundScores[COMPUTER_PLAYER] += meldInstance.getMeldPoints();
    }

    /**
     * gets all the melds played by a player in the round of Pinochle
     * @param player the player whose melds are to be returned
     * @return all the melds played by the player
     */
    private ArrayList<String> getMeldsByPlayer(int player) {
        ArrayList<String> playerMelds = new ArrayList<>();
        ArrayList<ArrayList<MeldInstance>> melds = players[player].getMeldsPlayed().getAllMelds();
        Card meldCard;
        //for all meld types
        for(int i = 0; i < melds.size(); i++) {
            //for all instances of a meld type
            for(int j = 0; j < melds.get(i).size(); j++) {
                //for all cards in a meld instance
                for(int k = 0; k < melds.get(i).get(j).getNumOfCards(); k++) {
                    meldCard = melds.get(i).get(j).getCardByPosition(k);
                    playerMelds.add(meldCard.getShortCardStr());
                }
                //separate melds from each other using a string containing white space
                playerMelds.add("  ");
            }
        }
        return playerMelds;
    }



    /**
     * updates the model state to reflect that a new turn has started in a round of Pinochle
     */
    private void nextTurn() {
        //first, check if players have cards left in their hands
        if(players[HUMAN_PLAYER].getHand().getNumOfCards() == 0 || players[COMPUTER_PLAYER].getHand().getNumOfCards() == 0) {
            currentStage = OUT_OF_CARDS_STAGE;

        } else {
            //set current stage
            currentStage = LEAD_CARD_STAGE;

            //distribute one card each
            if(stock.getNumRemaining() != 0) {
                players[humansTurn? HUMAN_PLAYER : COMPUTER_PLAYER].takeOneCard(stock.takeOneFromTop());
                if(stock.getNumRemaining() == 0) {
                    if(trumpCard != null) {
                        players[!humansTurn? HUMAN_PLAYER : COMPUTER_PLAYER].takeOneCard(trumpCard);
                        trumpCard = null;
                    }
                    //don't pick a card if no cards are left
                } else {
                    players[!humansTurn? HUMAN_PLAYER : COMPUTER_PLAYER].takeOneCard(stock.takeOneFromTop());
                }
            }
        }

        urgeNextMove();
    }



    /**
     * Decides who the winner of a Round is going to be and updates the model state accordingly
     */
    private void decideTurnWinner() {
        if(currentChaseCard != null && currentLeadCard != null) {
            int roundWinner;
            String winner;
            if(leadCardWins()) {
                roundWinner = humansLeadThrow ? HUMAN_PLAYER : COMPUTER_PLAYER;
                 winner = humansLeadThrow ? "human" : "computer";
                messages[roundWinner] = "The " + winner + "'s lead card has won this turn.";
            } else {
                roundWinner = humansLeadThrow ? COMPUTER_PLAYER : HUMAN_PLAYER;
                winner = humansLeadThrow ? "computer" : "human";
                messages[roundWinner] = "The " + winner + "'s chase card has won this turn.";
            }
            int pointsWon = cardPoints(currentChaseCard) + cardPoints(currentLeadCard);
            messages[roundWinner] += "The " + winner + " wins " + pointsWon + " points this turn.";
            roundScores[roundWinner] += pointsWon;
            players[roundWinner].addToCapturePile(currentLeadCard, currentChaseCard);
            //remove message on turn loser's side
            messages[roundWinner == HUMAN_PLAYER ? 0 : 1] = "";

            //set next turn
            humansTurn = roundWinner == HUMAN_PLAYER;
            humansLeadThrow = humansTurn;

            currentLeadCard = null;
            currentChaseCard = null;
        }
    }

    /**
     * returns the points corresponding to a particular card
     * @param card the card whose points are to be determined
     * @return the points that the card is worth
     */
    private int cardPoints(Card card){
        switch (card.getRank()) {
            case ACE:
                return 11;
            case TEN:
                return 10;
            case KING:
                return 4;
            case QUEEN:
                return 3;
            case JACK:
                return 2;
            case NINE:
                return 0;
            default:
                return 0;
        }
    }

    /**
     * Determines whether the lead card wins in a turn of Pinochle or not
     * @return returns true if the lead card beats the chase card, false otherwise
     */
    private Boolean leadCardWins() {
        Boolean a = currentChaseCard != null && currentLeadCard != null;
        if(currentChaseCard != null && currentLeadCard != null) {
            if(currentLeadCard.getSuit() == trumpSuit) {
                //if the chase card is of trump suit
                if(currentChaseCard.getSuit() == trumpSuit) {
                    //if the rank of the lead card is less than that of chase card, lead card loses, else it wins
                    return !(currentLeadCard.hasLessRankThan(currentChaseCard));
                } else {
                    //if the chase card is not of trump suit, lead card wins
                    return true;
                }
            } else {
                //if the lead card is not of trump suit

                //if the chase card is of trump suit, chase card wins
                if(currentChaseCard.getSuit() == trumpSuit) {
                    return false;
                } else if(currentChaseCard.getSuit() == currentLeadCard.getSuit()) {
                    //if the chase card has the same suit as that of the lead card,
                    //then the chase card only wins if it has higher rank than chase card
                    return !(currentLeadCard.hasLessRankThan(currentChaseCard));
                } else {
                    //if the chase card is neither of trump suit nor does it have the same suit as the lead card, lead card wins
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * Updates the game state model to urge the player to make the next
     */
    private void urgeNextMove() {
        if(humansTurn == null) {
            return;
        }
        String whoseTurn = humansTurn ? "human" : "computer";
        int currentTurn = humansTurn ? 1 : 0;
        if(currentStage.equals(LEAD_CARD_STAGE)) {
            messages[currentTurn] = "It is the " + whoseTurn + "'s turn to throw a lead card.";
        } else if (currentStage.equals(CHASE_CARD_STAGE)) {
            messages[currentTurn] = "It is the " + whoseTurn + "'s turn to throw a chase card.";
        } else if (currentStage.equals(MELD_STAGE)){
            if(players[currentTurn].isMeldPossible()) {
                messages[currentTurn] = "It is the " + whoseTurn + "'s turn to play a meld.";
            } else {
                messages[currentTurn] = "The " + whoseTurn + " has no possible melds in hand.";
                currentStage = INTERMISSION_STAGE;
                //create a dummy meld instance
                currentMeld = new MeldInstance();
            }
        } else if (currentStage.equals(OUT_OF_CARDS_STAGE)){
            messages[HUMAN_PLAYER] = "There are no more cards left to play.";
            messages[COMPUTER_PLAYER] = "";
        }
    }

    /**
     * extracts data from the string extracted from a Pinochle save file
     * @param saveFileData the serialized data from the save file
     */
    private void loadGameData(String saveFileData) {


        String [] lines = saveFileData.split("\\n");
        //get rid of empty lines
        ArrayList<String> tempLines = new ArrayList<>();
        for(int i = 0; i < lines.length; i++) {
            //skip empty lines
            if(!(i == 1 || i == 7 || i == 13 || i == 16)) {
                tempLines.add(lines[i].trim());
            }
        }
        lines = tempLines.toArray(new String[tempLines.size()]);

//        ArrayList<String> lines = new ArrayList(Arrays.asList(saveFileLines));
        String data;
        int index;
        int lineNumber = 0;
        //first get round number

        //get round number
        lines[lineNumber] = lines[lineNumber].substring(6);
        lines[lineNumber] = StringUtilities.stripString(lines[lineNumber]);

        roundNumber = Integer.parseInt(lines[lineNumber]);

        //next line
        lineNumber++;

        //lines 1 to 10 (1- 5 in first iteration, 2-6 in second iteration)
        //one iteration for each player
        for(int i = 0; i < NUM_OF_PLAYERS; i++) {
            //skip the "Computer:" / "Human:" lines
            //next line
            lineNumber++;
            //get scores
            lines[lineNumber] = lines[lineNumber].substring(6);
            lines[lineNumber] = StringUtilities.stripString(lines[lineNumber]);
            index = lines[lineNumber].indexOf("/");
            data = lines[lineNumber].substring(0, index);
            //game score

            gameScores[i] = Integer.parseInt(data.trim());

            lines[lineNumber] = lines[lineNumber].substring(index + 1);
            lines[lineNumber] = StringUtilities.stripString(lines[lineNumber]);
            //round score
            roundScores[i] = Integer.parseInt(lines[lineNumber]);

            //next line
            lineNumber++;
            //get Hand

            lines[lineNumber] = lines[lineNumber].substring(5);
            lines[lineNumber] = StringUtilities.stripString(lines[lineNumber]);
            loadedHandStrs[i] = lines[lineNumber];


            //next line
            lineNumber++;
            //get Capture Pile
            lines[lineNumber] = lines[lineNumber].substring(13);
            lines[lineNumber] = StringUtilities.stripString(lines[lineNumber]);
            loadedCaptureStrs[i] = lines[lineNumber];

            //next line
            lineNumber++;
            //get Melds
            lines[lineNumber] = lines[lineNumber].substring(6);
            lines[lineNumber] = StringUtilities.stripString(lines[lineNumber]);
            loadedMeldStrs[i] = lines[lineNumber];
            //next line
            lineNumber++;
        }

        //trump card
        lines[lineNumber] = lines[lineNumber].substring(11);
        lines[lineNumber] = StringUtilities.stripString(lines[lineNumber]);
        if(lines[lineNumber].length() == 1) {
            trumpCard = null;
            trumpSuit = StringUtilities.strToSuit(lines[lineNumber].charAt(0));
        } else if(lines[lineNumber].length() == 2) {
            trumpCard.setRank(StringUtilities.strToRank(lines[lineNumber].charAt(0)));
            trumpCard.setSuit(StringUtilities.strToSuit(lines[lineNumber].charAt(1)));
            trumpSuit = StringUtilities.strToSuit(lines[lineNumber].charAt(1));
        }

        //next line
        lineNumber++;

        //stock
        lines[lineNumber] = lines[lineNumber].substring(6);
        lines[lineNumber] = StringUtilities.stripString(lines[lineNumber]);
        loadedStockStr = lines[lineNumber];

        //next line
        lineNumber++;

        //next player
        lines[lineNumber] = lines[lineNumber].substring(12);
        lines[lineNumber] = StringUtilities.stripString(lines[lineNumber]);
        humansTurn = (lines[lineNumber].equals("Human"));
    }

}
