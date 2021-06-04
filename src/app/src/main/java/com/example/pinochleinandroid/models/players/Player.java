/************************************************************
 * Name:  Biplab Thapa Magar                                *
 * Project:  Pinochle in Java/Android                       *
 * Class:  OPL Fall 2020                                    *
 * Date:  11/18/2020                                        *
 ************************************************************/

package com.example.pinochleinandroid.models.players;

import com.example.pinochleinandroid.models.cards.Card;
import com.example.pinochleinandroid.models.cards.GroupOfCards;
import com.example.pinochleinandroid.models.cards.Meld;
import com.example.pinochleinandroid.models.cards.MeldInstance;
import com.example.pinochleinandroid.models.cards.MeldsStorage;
import com.example.pinochleinandroid.models.cards.Suit;
import com.example.pinochleinandroid.models.utilities.MeldServices;

import java.util.ArrayList;

public abstract class Player {
    private GroupOfCards hand;
    private GroupOfCards capturePile;
    private Suit trumpSuit;
    private MeldServices meldServices;
    private String reasoning;
    protected String message;

    /**
     * Default constructor for Player class
     */
    public Player() {
        hand = new GroupOfCards();
        capturePile = new GroupOfCards();
        trumpSuit = null;
        meldServices = new MeldServices();
        reasoning = "";
        message = "";
    }

    /**
     * Overloaded constructor for Player class. Initializes a Player object with the given parameters
     * @param hand the hand to be assigned to the player
     * @param capturePile the capturePile to be assigned to the player
     * @param meldsPlayed the melds storage containing melds that the player has already played
     * @param trumpSuit the trump suit of the current round
     */

    public Player(GroupOfCards hand, GroupOfCards capturePile, MeldsStorage meldsPlayed, Suit trumpSuit) {
        this.hand = hand;
        this.capturePile = capturePile;
        this.trumpSuit = trumpSuit;
        this.meldServices = new MeldServices(meldsPlayed, trumpSuit);
        reasoning = "";
        message = "";
    }

    /**
     * takes the card passed to it and stores it in player hand
     * @param card the Card to be added to hand
     */
    public void takeOneCard(Card card) {
        hand.addCard(card);
    }

    /**
     * Returns the player's current hand
     * @return the player's current hand, as a GroupOfCards objects
     */
    public GroupOfCards getHand() {
        return hand;
    }

    /**
     * Returns the reasoning generated after the generating Lead card, chase card, or meld suggestion
     * @return the reasoning generated after the generating Lead card, chase card, or meld suggestion
     */
    public String getReasoning() {
        return reasoning;
    }

    /**
     * Returns the message generated after child object of player class makes a move
     * @return the message generated after child object of player class makes a move
     */
    public String getMessage() {
        return message;
    }

    /**
     * returns the melds storage containing the melds the player has played
     * @return the melds played by the player,
     */
    public MeldsStorage getMeldsPlayed() {
        return meldServices.getMeldsPlayed();
    }

    /**
     * returns the player's capture pile
     * @return the player's capture pile
     */
    public GroupOfCards getCapturePile() {
        return capturePile;
    }

    /**
     * returns the number of cards left in the player's hand
     * @return the number of cards left in player's hand
     */
    public int numCardsInHand() {
        return hand.getNumOfCards();
    }

    /**
     * returns whether there are any possible melds the player can play with respect to his current hand
     * @return true if meld is possible, false in not
     */
    public Boolean isMeldPossible() {
        MeldsStorage allPossibleMelds = meldServices.getMeldsFromHand(hand);
        if(allPossibleMelds.getNumOfMelds() == 0) {
            return false;
        }
        return true;
    }

    /**
     * returns the position of the given card in the hand
     * @param card the card whose position in hand is to be returned
     * @return the position of the card
     */
    protected int getCardPositionInHand(Card card) {
        return hand.getCardPosition(card);
    }


    /**
     * sets the trump suit for the current round
     * @param trumpSuit the trump suit for the current round
     * @return true when trump suit has been set
     */
    public Boolean setTrumpSuit(Suit trumpSuit) {
        this.trumpSuit = trumpSuit;
        meldServices.setTrumpSuit(trumpSuit);
        return true;
    }

    /**
     * Plays a card from hand by removing the card from player hand and returning that card
     * @param position the position of the card in hand to be played
     * @return the card that the player chooses to play
     */
    protected Card playFromHand(int position) {
        if(position >= numCardsInHand() || position < 0) {
            return null;
        }
        Card card = hand.getCardByPosition(position);
        hand.removeCardByPosition(position);
        return card;
    }

    /**
     * Plays a card from hand by removing the card from player's hand and returning that card
     * @param card the card that is to be played from hand
     * @return the card that was played from hand
     */
    protected Card playFromHand(Card card) {
        if(!hand.searchCardById(card.getId())) {
            return null;
        }
        hand.removeCardById(card.getId());
        return card;
    }

    /**
     * takes two cards and adds them to the player's capture pile
     * @param card1 the first card to add to the capture pile
     * @param card2 the second card to add to the capture pile
     */
    public void addToCapturePile(Card card1, Card card2) {
        capturePile.addCard(card1);
        capturePile.addCard(card2);
    }

    /**
     * Finds the best cards in hand to play as a lead card, by taking in account the card's rank, suit, and the possible melds in hand left after throwing the card
     * @return the best cards for lead throw in hand
     */
    private ArrayList<Card> bestCardsForLeadThrow() {

        //make sure hand is not empty
        if(hand.getNumOfCards() == 0) {
            return null;
        }
        //making copies of the original hand so we can make comparisons on the copies
        GroupOfCards bestHand;
        GroupOfCards competeHand;

        //these are all the cards that yield the best hand when thrown
        ArrayList<Card> bestCardsToThrow = new ArrayList<Card>();

        bestHand = new GroupOfCards(hand);
        bestHand.removeCardByPosition(0);
        bestCardsToThrow.add(hand.getCardByPosition(0));
        //iterate through all cards to see what hand would look like if we remove each card
        for(int i = 1; i < hand.getNumOfCards(); i++) {
            competeHand = new GroupOfCards(hand);
            competeHand.removeCardByPosition(i);

            //if the competing hand is better
            if(meldServices.compareHandsForMelds(bestHand, competeHand) == 2) {
                bestHand = competeHand;
                //there is a hand that is better than all the hands encountered before, so we empty
                //out bestCardsToThrow and add the new best card
                bestCardsToThrow.clear();
                bestCardsToThrow.add(hand.getCardByPosition(i));
            }
            //if its a draw between the two hands, then we add the other best card to the ArrayList of best cards
            else if(meldServices.compareHandsForMelds(bestHand, competeHand) == 0) {
                bestCardsToThrow.add(hand.getCardByPosition(i));
            }
        }
        return bestCardsToThrow;
    }

    /**
     * suggests a lead card to the player
     * @return the card that is suggested to be played
     */
    protected Card suggestLeadCard() {
        //use the compareHandsForMelds to check what card, when thrown, will preserve the best melds
        //we simulate throwing every single card in the hand to create as many possible hands as there are cards in the hand
        ArrayList<Card> bestCardsToThrow = bestCardsForLeadThrow();
        if (bestCardsToThrow == null) {
            return null;
        }

        //give the reasoning for choosing this set of cards
        reasoning = "throwing this card would preserve the most favorable hand, which has the most high-value melds";

        //now, check the size of bestCardsToThrow. if it is more than one, then we need to further compare
        if(bestCardsToThrow.size() > 1) {
            //if any of them is of suit , throw the trump suit card since that will increase the chance of winning the round
            //without sacrificing melds
            ArrayList<Card> trumpSuitCardsToThrow = new ArrayList<Card>();
            for(int i = 0; i < bestCardsToThrow.size(); i++) {
                if(bestCardsToThrow.get(i).getSuit() == trumpSuit) {
                    trumpSuitCardsToThrow.add(bestCardsToThrow.get(i));
                }
            }

            //if there is at least one trump card, then that card(s) is the new best card(s)
            if(trumpSuitCardsToThrow.size() != 0) {
                bestCardsToThrow = trumpSuitCardsToThrow;
            }
            //if still there are more than one cards, get the card with the greater Rank
            //if even ranks are the same, it does not matter what card we throw
            if(bestCardsToThrow.size() >= 1) {
                //here, we store the best card in the first index of the ArrayList
                for(int i = 1; i < bestCardsToThrow.size(); i++) {
                    if(bestCardsToThrow.get(i).hasGreaterRankThan(bestCardsToThrow.get(0))) {
                        bestCardsToThrow.set(0, bestCardsToThrow.get(i));
                    }
                }
                if(bestCardsToThrow.get(0).getSuit() == trumpSuit) {
                    reasoning = "throwing this card would preserve the most favorable hand (meld-wise) and it would increase the chance of winning because it is a trump card";
                } else {
                    reasoning = "throwing this card would result in a good balance between a favorable hand (meld-wise) and the chance of winning the turn";
                }

            }
        }
        return bestCardsToThrow.get(0);
    }

    /**
     * finds the least valuable card in hand
     * @return returns the least valuable card in the current hand
     */
    private Card getLeastRankedCard() {
        if(hand.getNumOfCards() == 0) {
            return null;
        }
        Card leastRankedCard = new Card();
        //first check to see if there are any non-trump suit cards in hand
        if(hand.getCardsBySuit(trumpSuit).size() == hand.getNumOfCards()) {
            //if there are no non-trump suit cards, simply find the card with the least rank
            leastRankedCard = hand.getCardByPosition(0);
            for(int i = 1; i < hand.getNumOfCards(); i++) {
                if(hand.getCardByPosition(1).hasLessRankThan(leastRankedCard)) {
                    leastRankedCard = hand.getCardByPosition(i);
                }
            }
        } else {
            //if there are non-trump suit cards, find the least ranked among them
            //first, get the first card in hand that's not a trump suit card
            int index;
            for(index = 0; index < hand.getNumOfCards(); index++) {
                if(hand.getCardByPosition(index).getSuit() != trumpSuit) {
                    leastRankedCard = hand.getCardByPosition(index);
                    break;
                }
            }
            //now find the least ranked card that's not of trump suit
            for(int i = index + 1; i < hand.getNumOfCards(); i++) {
                if(hand.getCardByPosition(i).getSuit() != trumpSuit && hand.getCardByPosition(i).hasLessRankThan(leastRankedCard)) {
                    leastRankedCard = hand.getCardByPosition(i);
                }
            }
        }

        return leastRankedCard;
    }

    /**
     * Gets the least ranked card from within a group of cards
     * @param cards the cards from which to find the least ranked card
     * @return the card with the least value
     */
    private Card getLeastRankedFrom(ArrayList<Card> cards) {
        if(cards.size() == 0) {
            return null;
        }
        Card leastRankedCard = cards.get(0);
        for(int i = 1; i < cards.size(); i++) {
            if(cards.get(i).hasLessRankThan(leastRankedCard)) {
                leastRankedCard = cards.get(i);
            }
        }
        return leastRankedCard;
    }

    /**
     * finds the best chase card to play by considering the opponent's card
     * @param opponentCard which specifies the lead card played by the opponent
     * @return the card suggested to be played
     */
    protected Card suggestChaseCard(Card opponentCard) {
        Card cardToThrow;
        //first, see if the opponent's card is of trump suit
        if(opponentCard.getSuit() == trumpSuit) {
            //get the list of all the trump suits in hand
            ArrayList<Card> trumpSuitCards = hand.getCardsBySuit(trumpSuit);
            if(trumpSuitCards.size() != 0) {
                //if we do have a trump suit, we throw the card that's just greater than the opponent's card

                //first, find all the trump suit cards greater than the opponent card
                ArrayList<Card> greaterTrumpSuitCards = new ArrayList<Card>();
                for(int i = 0; i < trumpSuitCards.size(); i++) {
                    if(trumpSuitCards.get(i).hasGreaterRankThan(opponentCard)) {
                        greaterTrumpSuitCards.add(trumpSuitCards.get(i));
                    }
                }
                //if there are no trump suit cards that have higher rank than the opponent's card, there is no way to win
                //So, throw the least ranked card
                if(greaterTrumpSuitCards.size() == 0) {
                    cardToThrow = getLeastRankedCard();
                    reasoning = "there is no way to win this turn, so throwing the least ranked card will increase chances of winning next turn";
                } else {
                    //if there are trump suit cards that have higher rank than the opponent's card, find the smallest one among them
                    cardToThrow = getLeastRankedFrom(greaterTrumpSuitCards);
                    reasoning = "throwing a trump-suit card that is higher than but still closest to the opponent's trump-suit card will be the least expensive winning move";
                }
            } else {
                //if no trump suits in hand when the opponent's card is a trump suit, we cannot win the turn
                //throw the card with the lowest rank
                cardToThrow = getLeastRankedCard();
                reasoning = "there is no way to win this turn, so throwing the least ranked card will increase chances of winning next turn";
            }
        } else {
            //if opponents card is not a trump suit, we try our best not to use trump suit
            //first, get a ArrayList of all cards from hand having the same suit as the opponent's card and having a greater rank than the opponent's card
            ArrayList<Card> sameSuitCards = new ArrayList<Card>();
            for(int i = 0; i < hand.getNumOfCards(); i++) {
                if(hand.getCardByPosition(i).getSuit() == opponentCard.getSuit() && hand.getCardByPosition(i).hasGreaterRankThan(opponentCard)) {
                    sameSuitCards.add(hand.getCardByPosition(i));
                }
            }
            //if same suit cards with ranks greater than that of opponent card were found, then return the card with the smallest rank among them
            if(sameSuitCards.size() > 0) {
                cardToThrow = getLeastRankedFrom(sameSuitCards);
                reasoning = "throwing the card of the same suit as the opponent but with a rank greater than but closest to that of the opponent's card is the least expensive winning move";
            } else {
                //if the there are no cards in hand having the same suit as that of the opponent card, and having a higher rank,
                //then check if there is a trump-suit card in hand

                if(hand.getCardsBySuit(trumpSuit).size() == 0) {
                    //if there are no trumps suits in hand either, there is no chance of winning; simply return the lowest ranked card in hand
                    cardToThrow = getLeastRankedCard();
                    reasoning = "there is no way to win this turn, so throwing the least ranked card will increase chances of winning next turn";
                } else {
                    //if there is at least one trump-suit card, return the lowest-ranked of the trump-suit cards
                    cardToThrow = getLeastRankedFrom(hand.getCardsBySuit(trumpSuit));
                    reasoning = "there is no way of winning without using a trump suit, so throwing the least ranked trump suit is the least expensive way of winning over the opponent's non-trump-suit card";
                }
            }
        }
        return cardToThrow;
    }

    /**
     * returns the best meld to play, given a player's hand
     * @return a MeldInstance object containing the recommended meld
     */
    protected MeldInstance suggestMeld() {
        //first, get all possible melds from the hand
        MeldsStorage allPossibleMelds = meldServices.getMeldsFromHand(hand);

        if(allPossibleMelds.getNumOfMelds() == 0) {
            reasoning = "there are no possible melds to play with";
            return new MeldInstance();
        }

        //this stores all the meld instances that yield the highest points
        ArrayList<MeldInstance> highScoringMelds = new ArrayList<MeldInstance>();

        int highestPointsPossible = 0;
        ArrayList<MeldInstance> allMeldsOfGivenType;
        int meldPoints;
        //loop through all 9 meld types
        for(int i = 0; i < Meld.values().length; i++) {
            //if a particular meld yields the highest points so far, keep track of it
            allMeldsOfGivenType = allPossibleMelds.getAllMeldsByType(Meld.values()[i]);
            if(allMeldsOfGivenType.size() == 0) {
                meldPoints = 0;
            } else {
                meldPoints = meldServices.getMeldPoints(Meld.values()[i]);
            }

            if(meldPoints > highestPointsPossible) {
                highestPointsPossible = meldPoints;
                highScoringMelds.clear();
                //append all the instances of this meld type to highScoringMelds
                highScoringMelds = allMeldsOfGivenType;
            } else if (meldPoints == highestPointsPossible) {
                //append all the instances of this meld type to highScoringMelds
                highScoringMelds.addAll(allMeldsOfGivenType);
            }
        }
        reasoning = "playing this meld will yield the highest possible points from the available hand";
        return highScoringMelds.get(0);
    }



    /**
     * creates a meld from the cards in hand in the given positions
     * @param positions an ArrayList of positions the cards corresponding to which will be used to create the meld instance
     * @return the meld instance created from the cards in the given positions
     */
    protected MeldInstance createMeld(ArrayList<Integer> positions) {
        // be sure   to check if all positions are valid
        for(int i = 0; i < positions.size(); i++) {
            if (positions.get(i) < 0 || positions.get(i) >= numCardsInHand()) {
                reasoning = "All positions must be a valid position between 0 and " + (numCardsInHand() - 1) + ". Please try again.";
                return null;
            }
        }

        MeldInstance meldInstance = new MeldInstance();
        //next, combine all cards from each position to create a MeldInstance object
        for(int i = 0; i < positions.size(); i++) {
            meldInstance.addCard(hand.getCardByPosition(positions.get(i)), trumpSuit);
        }

        return createMeld(meldInstance);

    }

    /**
     * creates a meld for the player using the given meld instance
     * @param meldInstance the meld instance to be used to create the player's meld
     * @return the meld instance created
     */
    protected MeldInstance createMeld(MeldInstance meldInstance) {
        if(meldInstance.isValidMeld() == false) {
            reasoning = "The cards you specified do not combine to make up a meld. Please try again.";
            return null;
        }

        if(!meldServices.allCardsPresentInHand(hand, meldInstance)) {
            reasoning = "All the cards in the meld are not present in the player's hand.";
            return null;
        }

        if(!meldServices.meldIsNotARepeat(meldInstance)) {
            reasoning = "This meld is not valid because at least one card in it has been used to create an instance of this same meld type before.";
            return null;
        }

        if(!meldServices.meldHasANewCard(meldInstance)) {
            reasoning = "The meld must contain at least one new card from hand. Please try again.";
            return null;
        }

        if (meldServices.storeMeld(hand, meldInstance) == false) {
            reasoning = "Unable to store meld.";
            return null;
        }
        return meldInstance;
    }

    /**
     * Abstract method to get help for playing a lead card
     */
    public abstract void getHelpForLeadCard();

    /**
     * Abstract method to get help for playing a chase card
     * @param opponentCard the lead card thrown by the opponent
     */
    public abstract void getHelpForChaseCard(Card opponentCard);

    /**
     * Abstract method to get help for playing a meld
     */
    public abstract void getHelpForMeld();

    /**
     * Abstract method to get prompt player to play a lead card
     * @param position the position of the card to play
     * @return the card played
     */
    public abstract Card playLeadCard(int position);

    /**
     * Abstract method to prompt player to play a chase card
     * @param opponentCard the lead card thrown by the opponent
     * @param position the position of the card to play
     * @return the card played
     */
    public abstract Card playChaseCard(Card opponentCard, int position);

    /**
     * Abstract method to prompt player to play a meld
     * @param positions the positions of the cards to be used to create the meld to be played
     * @return the meld instance played
     */
    public abstract MeldInstance playMeld(ArrayList<Integer> positions);
}
