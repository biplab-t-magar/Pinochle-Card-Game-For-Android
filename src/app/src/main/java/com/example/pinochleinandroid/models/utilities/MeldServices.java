/************************************************************
 * Name:  Biplab Thapa Magar                                *
 * Project:  Pinochle in Java/Android                       *
 * Class:  OPL Fall 2020                                    *
 * Date:  11/18/2020                                        *
 ************************************************************/

package com.example.pinochleinandroid.models.utilities;

import com.example.pinochleinandroid.models.cards.Card;
import com.example.pinochleinandroid.models.cards.GroupOfCards;
import com.example.pinochleinandroid.models.cards.Meld;
import com.example.pinochleinandroid.models.cards.MeldInstance;
import com.example.pinochleinandroid.models.cards.MeldsStorage;
import com.example.pinochleinandroid.models.cards.Rank;
import com.example.pinochleinandroid.models.cards.Suit;

import java.util.ArrayList;
import java.util.Collections;

public class MeldServices {
    private MeldsStorage meldsPlayed;
    private Suit trumpSuit;

    /**
     * Constructor for MeldServices class
     */
    public MeldServices() {
        meldsPlayed = new MeldsStorage();
        trumpSuit = null;
    }

    /**
     * Overloaded constructor for MeldServices class
     * @param meldsPlayed the melds that have already been played by the player
     * @param trumpSuit the trump suit for the current round
     */
    public MeldServices(MeldsStorage meldsPlayed, Suit trumpSuit) {
        this.trumpSuit = trumpSuit;
        this.meldsPlayed = meldsPlayed;
    }

    /**
     * supplies the ArrayList storing all the player's melds
     * @return the ArrayList storing all the player's melds
     */
    public MeldsStorage getMeldsPlayed() {
        return meldsPlayed;
    }

    /**
     * returns the points the given meld type yields in a game of Pinochle
     * @param meld the meld type whose point is to be returned
     * @return the points of the  given meld type
     */
    public int getMeldPoints(Meld meld){
        switch(meld) {
            case FLUSH:
                return 150;
            case ROYAL_MARRIAGE:
                return 40;
            case MARRIAGE:
                return 20;
            case DIX:
                return 10;
            case FOUR_ACES:
                return 100;
            case FOUR_KINGS:
                return 80;
            case FOUR_QUEENS:
                return 60;
            case FOUR_JACKS:
                return 40;
            case PINOCHLE:
                return 40;
            default:
                return 0;
        }
    }

    /**
     * checks if all the cards in the given meld instance are present in the given hand
     * @param hand the hand in which the cards are to be checked
     * @param meldInstance the MeldInstance object containing the cards that are to be looked for in the hand
     * @return true if all cards were found in hand, false otherwise
     */
    public Boolean allCardsPresentInHand(GroupOfCards hand, MeldInstance meldInstance) {
        for(int i = 0; i < meldInstance.getNumOfCards(); i++) {
            //check if each card in the candidate meld is present in the hand
            if(!hand.searchCardById(meldInstance.getCardByPosition(i).getId())) {
                return false;
            }
        }
        return true;
    }

    /**
     * checks if a meld has or has not been played by the player before
     * @param meldInstance the meld instance that is to be checked for duplication
     * @return true if the meld instance has not been played yet, false otherwise
     */
    public Boolean meldIsNotARepeat(MeldInstance meldInstance) {
        for(int i = 0; i < meldInstance.getNumOfCards(); i++) {
            if(meldsPlayed.isCardUsedByMeld(meldInstance.getCardByPosition(i), meldInstance.getMeldType())) {
                return false;
            }
        }
        return true;
    }

    /**
     * checks if all the cards in the meld have not been used together before to create a meld
     * @param meldInstance the meld instance whose cards are to be checked
     * @return true if at least one card from the meld instance has not been played with the other cards in the meld instance to create a meld before false otherwise
     */
    public Boolean meldHasANewCard(MeldInstance meldInstance) {
        return !meldsPlayed.cardsUsedForSameMeld(meldInstance);
    }

    /**
     * sets the trumpSuit for the round
     * @param trumpSuit the suit that's to be set as the trump suit
     * @return true if successfully set as trump suit
     */
    public Boolean setTrumpSuit(Suit trumpSuit) {
        this.trumpSuit = trumpSuit;
        return true;
    }

    /**
     * stores a given meldInstance from the given hand
     * @param hand the hand of the player creating the meld
     * @param meldInstance the instance of the meld to be created
     */
    public Boolean storeMeld(GroupOfCards hand, MeldInstance meldInstance) {
        if(!meldInstance.isValidMeld() || !allCardsPresentInHand(hand, meldInstance) || !meldIsNotARepeat(meldInstance)) {
            return false;
        }
        meldsPlayed.addMeld(meldInstance);
        return true;
    }

    /**
     * compares two different possible hands and finds which one has better possible melds
     * @param hand1 the first hand to be compared
     * @param hand2 the second hand to be compared
     * @return returns the number 1 if hand1 is better, 2 if hand2 is better, 0 if draw
     */
    public int compareHandsForMelds(GroupOfCards hand1, GroupOfCards hand2) {
        //get the points (from highest to lowest) for each possible meld in the two candidate hands

        ArrayList<Integer> hand1Points = potentialPointsFromHand(hand1);
        if (hand1Points == null) {
            return -1;
        }
        ArrayList<Integer> hand2Points = potentialPointsFromHand(hand2);
        if (hand2Points == null) {
            return -1;
        }

        //first, check which hand has the meld that provides the highest points
        if (hand1Points.get(0) > hand2Points.get(0)) {
            return 1;
        } else if (hand1Points.get(0) < hand2Points.get(0)) {
            return 2;
        }
        //if the highest points of each is the same, compare the total potential points produced by all the melds of each hand
        int totalHand1Points = 0;
        int totalHand2Points = 0;
        for(int i = 0; i < hand1Points.size(); i++) {
            totalHand1Points+= hand1Points.get(i);
        }
        for(int i = 0; i < hand2Points.size(); i++) {
            totalHand2Points+= hand2Points.get(i);
        }
        if(totalHand1Points > totalHand2Points) {
            return 1;
        } else if(totalHand2Points > totalHand1Points) {
            return 2;
        }

        //if again the total possible points of each hand are the same, compare the 2nd highest, 3rd highest,...
        //and so on points of each meld until a winner is found
        int i = 0;
        //get the size of the smaller of the two ArrayLists
        int size = Math.min(hand1Points.size(), hand2Points.size());
        while(i < size) {
            if (hand1Points.get(i) > hand2Points.get(i)) {
                return 1;
            } else if (hand1Points.get(i) < hand2Points.get(i)) {
                return 2;
            }
            i++;
        }

        //if no difference between the possible points from each hand was found, return 0, standing for draw
        return 0;
    }

    /**
     * Returns a ArrayList of all the points yielded by all the potential melds in a hand, in descending order
     * @param hand the hand of the player
     * @return the ArrayList containing the possible points in descending order
     */
    public ArrayList<Integer> potentialPointsFromHand(GroupOfCards hand) {
        ArrayList<Integer> countsOfEachMeldType = countMeldsFromHand(hand);
        if (countsOfEachMeldType == null) {
            return null;
        }
        //now, get the ArrayList containing the points for each possible meld in the hand
        ArrayList<Integer> points = new ArrayList<Integer>();
        //loop through all the meld types
        for(int i = 0 ; i < countsOfEachMeldType.size(); i++) {
            //add separate entry for each occurrence of a meld type (what is entered into the ArrayList should be the points a meld gives)
            for(int j = 0; j < countsOfEachMeldType.get(i); j++) {
                points.add(getMeldPoints(Meld.values()[i]));
            }
        }

        if(points.size() == 0) {
            points.add(0);
        }
        //sort the ArrayList
        Collections.sort(points);
        return points;
    }

    /**
     * counts the number of each possible meld type in a given hand
     * @param hand the hand of the player whose melds are to be counted
     * @return a ArrayList of the number of meld instances of a particular meld type, with each index in the ArrayList corresponding to each meld type
     */
    private ArrayList<Integer> countMeldsFromHand(GroupOfCards hand) {
        if(trumpSuit == null) {
            return null;
        }
        //ArrayList to hold each meld type
        ArrayList<Integer> numOfEachMeld = new ArrayList<Integer>();
        //initialize all values in ArrayList to 0
        for(int i = 0; i < Meld.values().length; i++) {
            numOfEachMeld.add(0);
        }
        //now assign the number of possible instances for each meld
        //flush
        numOfEachMeld.set(Meld.FLUSH.ordinal(), getSameSuitMelds(Meld.FLUSH, hand, trumpSuit, Rank.ACE, 5).size());
        //royal marriage
        numOfEachMeld.set(Meld.ROYAL_MARRIAGE.ordinal(), getSameSuitMelds(Meld.ROYAL_MARRIAGE, hand, trumpSuit, Rank.KING, 2).size());
        //marriage
        numOfEachMeld.set(Meld.MARRIAGE.ordinal(), getMarriages(hand).size());
        //dix
        numOfEachMeld.set(Meld.DIX.ordinal(), getDixes(hand).size());
        //FourAces
        numOfEachMeld.set(Meld.FOUR_ACES.ordinal(), getSameRankMelds(Meld.FOUR_ACES, hand, Rank.ACE).size());
        //Four Kings
        numOfEachMeld.set(Meld.FOUR_KINGS.ordinal(), getSameRankMelds(Meld.FOUR_KINGS, hand, Rank.KING).size());
        //Four Queens
        numOfEachMeld.set(Meld.FOUR_QUEENS.ordinal(), getSameRankMelds(Meld.FOUR_QUEENS, hand, Rank.QUEEN).size());
        //Four Jacks
        numOfEachMeld.set(Meld.FOUR_JACKS.ordinal(), getSameRankMelds(Meld.FOUR_JACKS, hand, Rank.JACK).size());
        //Pinochle
        numOfEachMeld.set(Meld.PINOCHLE.ordinal(), getPinochles(hand).size());

        return numOfEachMeld;
    }

    /**
     * gets each possible meld instance of each possible meld type from a given hand
     * @param hand the hand of the player from which melds are to be calculated
     * @return a MeldsStorage object consisting of all the MeldInstances found in the hand
     */
    public MeldsStorage getMeldsFromHand(GroupOfCards hand) {
        if(trumpSuit == null) {
            return null;
        }

        MeldsStorage allPossibleMelds = new MeldsStorage();

        //now push the number of possible instances of each meld

        //flush
        allPossibleMelds.addMelds(getSameSuitMelds(Meld.FLUSH, hand, trumpSuit, Rank.ACE, 5));
        //royal marriage
        allPossibleMelds.addMelds(getSameSuitMelds(Meld.ROYAL_MARRIAGE, hand, trumpSuit, Rank.KING, 2));
        //marriage
        allPossibleMelds.addMelds(getMarriages(hand));
        //dix
        allPossibleMelds.addMelds(getDixes(hand));
        //FourAces
        allPossibleMelds.addMelds(getSameRankMelds(Meld.FOUR_ACES, hand, Rank.ACE));
        //Four Kings
        allPossibleMelds.addMelds(getSameRankMelds(Meld.FOUR_KINGS, hand, Rank.KING));
        //Four Queens
        allPossibleMelds.addMelds(getSameRankMelds(Meld.FOUR_QUEENS, hand, Rank.QUEEN));
        //Four Jacks
        allPossibleMelds.addMelds(getSameRankMelds(Meld.FOUR_JACKS, hand, Rank.JACK));
        //Pinochle
        allPossibleMelds.addMelds(getPinochles(hand));

        return allPossibleMelds;
    }

    /**
     * gets each possible Dix meld instances from the given hand
     * @param hand the hand of the player in which Dixes are to be searched
     * @return a ArrayList containing all the Dixes found in the hand
     */
    private ArrayList<MeldInstance> getDixes(GroupOfCards hand) {
        if(trumpSuit == null) {
            return null;
        }

        ArrayList<MeldInstance> dixes = new ArrayList<MeldInstance>();
        //get all the 9 of Trumps from the hand
        ArrayList<Card> allDixes = hand.getCardsByRankAndSuit(Rank.NINE, trumpSuit);

        //go through each of these 9 of Trumps and discount those which have been used already to make a Dix
        for(int i = 0; i < allDixes.size(); i++) {
            if(meldsPlayed.isCardUsedByMeld(allDixes.get(i), Meld.DIX) == false) {
                //create a meld instance out of the eligible card and push it to dixes
                dixes.add(new MeldInstance(new ArrayList<Card>(allDixes.subList(i, i + 1)), trumpSuit));
            }
        }
        return dixes;
    }

    /**
     * gets each possible Pinochle meld instances from the given hand
     * @param hand the hand of the player from which Pinochle meld instances are to be calculated
     * @return a ArrayList containing all the possible Pinochle meld instances
     */
    private ArrayList<MeldInstance> getPinochles(GroupOfCards hand) {
        if(trumpSuit == null) {
            return null;
        }
        ArrayList<ArrayList<Card>> playableCards = new ArrayList<ArrayList<Card>>();

        //0 index will store the ArrayList of all JD's and 1 index will store the ArrayList of all QS
        playableCards.add(new ArrayList<Card>());
        playableCards.add(new ArrayList<Card>());

        ArrayList<Card> allJackOfDiamonds = hand.getCardsByRankAndSuit(Rank.JACK, Suit.DIAMONDS);
        ArrayList<Card> allQueenOfSpades = hand.getCardsByRankAndSuit(Rank.QUEEN, Suit.SPADES);

        //go through each Jack of Diamonds and discount those which have been used to make a Pinochle before
        for(int i = 0; i < allJackOfDiamonds.size(); i++) {
            if(!meldsPlayed.isCardUsedByMeld(allJackOfDiamonds.get(i), Meld.PINOCHLE)) {
                //store all Jack of Diamonds in the first position
                playableCards.get(0).add(allJackOfDiamonds.get(i));
            }
        }
        //do the same for Queen of Spades
        for(int i = 0; i < allQueenOfSpades.size(); i++) {
            if(!meldsPlayed.isCardUsedByMeld(allQueenOfSpades.get(i), Meld.PINOCHLE)) {
                //store all Queen of Spades in the second position
                playableCards.get(1).add(allQueenOfSpades.get(i));
            }
        }

        return createMeldsFromEligibleCards(playableCards);

    }

    /**
     * gets each possible Marriage meld instances from the given hand
     * @param hand the hand of the player in which Dixes are to be searched
     * @return an ArrayList containing all the Marriages found in the hand
     */
    private ArrayList<MeldInstance> getMarriages(GroupOfCards hand) {
        if(trumpSuit == null) {
            return null;
        }
        ArrayList<MeldInstance> marriages = new ArrayList<MeldInstance>();
        ArrayList<MeldInstance> marriageStore;
        //loop through each suit
        for(int suit = 0; suit < 4; suit++) {
            if(Suit.values()[suit] != trumpSuit) {
                marriageStore = getSameSuitMelds(Meld.MARRIAGE, hand, Suit.values()[suit], Rank.KING, 2);
                marriages.addAll(marriageStore);
            }
        }

        return marriages;
    }

    /**
     * gets each possible Same Rank meld instances from the given hand.
     * @param meld the particular meld type whose instances are being counted
     * @param hand the hand of the player in which Dixes are to be searched
     * @param rank the rank of the same rank meld, (Aces if Four Aces, Kings if Four Kings, and so on)
     * @return an ArrayList containing all the instances of the given meld found in the hand
     */
    private ArrayList<MeldInstance> getSameRankMelds(Meld meld, GroupOfCards hand, Rank rank) {
//        Same Rank melds are those melds formed by combining 4 cards of the same
//                  rank: Four Aces, Four Kings, Four Queens, and Four Jacks
        //this ArrayList holds all possible cards of each suit of the specified rank in the hand
        ArrayList<ArrayList<Card>> cardsOfEachSuit = new ArrayList<ArrayList<Card>>();
        //holds the current suit (in int form) being counted (always start with clubs and increase to other suits from there)
        int suit = 0;

        //for temporarily holding groups of candidate cards
        ArrayList<Card> cardHolder;

        //for keeping track of the instances of a particular card eligible for the meld
        ArrayList<Card> eligibleCards;
        //loop through all the suits
        for(int i = 0; i < 4; i++) {
            eligibleCards  = new ArrayList<Card>();

            //get all the cards with the given rank and with the suit of the current loop
            cardHolder = hand.getCardsByRankAndSuit(rank, Suit.values()[suit]);

            //now check how many of these cards have already been used to create this meld
            for(int j = 0; j < cardHolder.size(); j++) {
                //only add card to eligibleCards if the card has not already been used for this meld
                if(!meldsPlayed.isCardUsedByMeld(cardHolder.get(j), meld)) {
                    eligibleCards.add(cardHolder.get(j));
                }
            }

            //keep track of each instance of the suit of the given rank
            cardsOfEachSuit.add(eligibleCards);

            //go to the next suit of the meld
            suit++;
        }

        return createMeldsFromEligibleCards(cardsOfEachSuit);

    }

    /**
     * gets each possible SameSuit meld instances from the given hand
     * @param hand the hand of the player in which Dixes are to be searched
     * @param meld the meld whose instances are to be returned
     * @param suit the suit of the Same Suit meld
     * @param startingRank the highest rank comprising the meld
     * @param howManyCards the number of cards in the meld
     * @return a ArrayList containing all the SameSuit melds found in the hand
     */
    private ArrayList<MeldInstance> getSameSuitMelds(Meld meld, GroupOfCards hand, Suit suit, Rank startingRank, int howManyCards) {
//        Same Suit melds are those melds that have cards with the same suit but of different ranks,
//                  for example: Flush, Royal Marriage, and Marriage
        //if the number in howManyCards is more than the number of Ranks in the meld
        if((startingRank.ordinal() - howManyCards + 1) <= 0 ) {
//            throw new Exception("Invalid starting rank or number of cards");
            return null;
        }
        if(howManyCards <= 0) {
//            throw new Exception("There cannot be a meld with 0 cards");
            return null;
        }

        //this ArrayList holds all possible cards of each rank of the specified suit in the hand
        //only ranks relevant to the meld are stored
        ArrayList<ArrayList<Card>> cardsOfEachRank = new ArrayList<ArrayList<Card>>();

        //holds the rank to be counted in each iteration
        int rank = startingRank.ordinal();

        //for temporarily holding groups of candidate cards
        ArrayList<Card> cardHolder;

        //for keeping count of the number of instances of a particular card eligible for the meld
        ArrayList<Card> eligibleCards;

        //loop through all the ranks in the meld
        for(int i = 0; i < howManyCards; i++) {
            eligibleCards = new ArrayList<Card>();

            //get all the cards with the given rank and with the suit of the current loop
            cardHolder = hand.getCardsByRankAndSuit(Rank.values()[rank], suit);

            //now check how many of these cards have already been used to create this meld
            for(int j = 0; j < cardHolder.size(); j++) {
                //only add card to eligibleCards if the card has not already been used for this meld
                if(!meldsPlayed.isCardUsedByMeld(cardHolder.get(j), meld)) {
                    eligibleCards.add(cardHolder.get(j));
                }
            }

            //keep track of each instance of the rank of the given suit
            cardsOfEachRank.add(eligibleCards);

            //go to the next rank in the meld
            rank--;

        }

        //since melds must contain at least one new card from hand and since
        //Royal Marriage is the only possible meld that could violate this rule
        //we check that the any pair of King/Queen cards have not been used together to form a Flush
        if(meld == Meld.ROYAL_MARRIAGE) {
            ArrayList<Card> royalMarriagePair = new ArrayList<Card>();
            if(cardsOfEachRank.get(0).size() > 0 && cardsOfEachRank.get(1).size() > 0) {
                royalMarriagePair.add(cardsOfEachRank.get(0).get(0));
                royalMarriagePair.add(cardsOfEachRank.get(1).get(0));

                //if both cards have been used for same meld, switch the cards out if there are other equivalent cards available
                if(meldsPlayed.cardsUsedForSameMeld(royalMarriagePair)) {
                    if(cardsOfEachRank.get(0).size() > 1) {
                        Card temp = cardsOfEachRank.get(0).get(0);
                        cardsOfEachRank.get(0).set(0, cardsOfEachRank.get(0).get(1));
                        cardsOfEachRank.get(0).set(1, temp);
                    } else if(cardsOfEachRank.get(1).size() > 1) {
                        Card temp = cardsOfEachRank.get(1).get(0);
                        cardsOfEachRank.get(1).set(0, cardsOfEachRank.get(1).get(1));
                        cardsOfEachRank.get(1).set(1, temp);
                    } else {
                        cardsOfEachRank.get(0).remove(cardsOfEachRank.get(0).size() - 1);
                        cardsOfEachRank.get(1).remove(cardsOfEachRank.get(1).size() - 1);
                    }
                }
            }

        }
        return createMeldsFromEligibleCards(cardsOfEachRank);

    }

    /**
     * creates meld instances of a meld from the given ArrayList of eligible cards
     * @param cards an ArrayList of ArrayLists that stores all the cards that can make up a meld
     * @return an ArrayList of meld instances created from the inputted cards
     */
    private ArrayList<MeldInstance> createMeldsFromEligibleCards(ArrayList<ArrayList<Card>> cards) {
        //finding the card type that has the least instances in cards (i.e. which card in the potential meld occurs the least number of times)
        //Any cards with occurence more than that minimum value have extraneous instances that represent choices we can make when we create melds
        //min represents number of mutually exclusive possible melds
        int min = cards.get(0).size();
        for(int i = 1; i < cards.size(); i++) {
            if(min > cards.get(i).size()) {
                min = cards.get(i).size();
            }
        }

        if(min == 0) {
            //if any card needed to create the meld is missing, return an empty ArrayList
            cards.clear();
        } else {
            //now, we simply disregard the extra instances of a given card type because it makes no difference which instance of the card
            //we use to create the meld
            for(int i = 0; i < cards.size(); i++) {
                //reduce size of each ArrayList to be the same as min
                cards.set(i, new ArrayList<Card>(cards.get(i).subList(0, min)));
            }
        }

        //now make a meld instance by combining cards
        ArrayList<MeldInstance> allPossibleMelds = new ArrayList<MeldInstance>();
        MeldInstance meldStore;
        //loop through all the instances of a card type
        for(int i = 0; i < min; i++) {
            meldStore = new MeldInstance();
            //loop through each card type needed to create the meld
            for(int j = 0; j < cards.size(); j++) {
                meldStore.addCard(cards.get(j).get(i), trumpSuit);
            }
            allPossibleMelds.add(meldStore);
        }

        return allPossibleMelds;
    }



}
