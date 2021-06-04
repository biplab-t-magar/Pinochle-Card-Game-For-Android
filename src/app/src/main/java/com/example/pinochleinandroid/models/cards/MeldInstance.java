/************************************************************
 * Name:  Biplab Thapa Magar                                *
 * Project:  Pinochle in Java/Android                       *
 * Class:  OPL Fall 2020                                    *
 * Date:  11/18/2020                                        *
 ************************************************************/

package com.example.pinochleinandroid.models.cards;

import java.util.ArrayList;

public class MeldInstance extends GroupOfCards {
    private Meld meldType;
    private Boolean meldIsValid;
    private Suit trumpSuit;

    /**
     * default constructor for the MeldInstance class. Initializes all fields to null
     */
    public MeldInstance() {
        meldType = null;
        trumpSuit = null;
        meldIsValid = false;
    }

    /**
     * Overloaded Constructor for the MeldInstance class
     * @param cards the ArrayList of all cards that are part of the MeldInstance object
     * @param trumpSuit the trumpSuit of the current round
     */
    public MeldInstance(ArrayList<Card> cards, Suit trumpSuit){
        super(cards);
        this.trumpSuit = trumpSuit;
        meldIsValid = checkMeldValidity();
    }

    /**
     * returns the Meld type of this meld instance
     * @return the meld type of this current instance, of enum type Meld; null if meld is invalid
     */
    public Meld getMeldType() {
        if(meldIsValid == false) {
            return null;
        }
        return meldType;
    }

    /**
     * to get the string representation of the meld formed by this meldinstance
     * @return the string representation of the meld type
     */
    public String getMeldTypeString() {
        if(!meldIsValid) {
            return null;
        }

        return meldType.getString();
    }

    /**
     * returns the points corresponding to this meld instance
     * @return the points that playing this meld would yield
     */
    public int getMeldPoints() {
        if(!meldIsValid) {
            return 0;
        }
        switch(meldType) {
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
     * checks if this meld instance forms a valid meld or not
     * @return true if the meld intance forms a valid meld, false if not
     */
    public Boolean isValidMeld() {
        return meldIsValid;
    }

    /**
     * adds a card to the meld instance and updates the validity of the new meld formed
     * @param card the card to be added
     * @param trumpSuit the trump suit of the round
     * @return true when card is added successfully, false otherwise
     */
    public Boolean addCard(Card card, Suit trumpSuit) {
        cards.add(card);
        this.trumpSuit = trumpSuit;
        meldIsValid = checkMeldValidity();
        return true;
    }

    /**
     * removes a card of the given id from this meld instance
     * @param id the GroupOfCards object that this object will be compared to
     * @return true if the card was found and removed, false otherwise
     */
    @Override
    public Boolean removeCardById(int id) {
        for(int i = 0; i < cards.size(); i++) {
            if(cards.get(i).getId() == id) {
                cards.remove(i);
                meldIsValid = checkMeldValidity();
                return true;
            }
        }

        return false;
    }

    /**
     * removes the card in the given position
     * @param position the position of the card to be removed
     * @return true when the card is removed successfully, false otherwise
     */
    @Override
    public Boolean removeCardByPosition(int position) {
        if(position >= cards.size() || position < 0) {
            return false;
        }
        if(cards.size()  < 1) {
            return false;
        }
        cards.remove(position);
        meldIsValid = checkMeldValidity();
        return true;
    }


    /**
     * empties the meld intance object of all cards
     * @return returns true when successfully emptied
     */
    public Boolean removeAllCards() {
        meldIsValid = false;
        cards.clear();
        return true;
    }



    /**
     * determines, given all the cards stored in the object, if a valid meld is formed by them
     * @return true if valid meld is formed by the cards, false otherwise
     */
    private Boolean checkMeldValidity() {
        //if the number of cards does not correspond to any possible meld, return false
        if(cards.size() < 1 || cards.size() == 3 || cards.size() > 5) {
            return false;
        }

        //checking melds from most common to least common
        //check if Dix
        if(isDix()) {
            meldType = Meld.DIX;
            return true;
        }

        //check if Marriage
        if(isAnyMarriage()) {
            meldType = typeOfMarriage();
            return true;
        }

        //check if Pinochle
        if(isPinochle()) {
            meldType = Meld.PINOCHLE;
            return true;
        }

        //check if Four Aces
        if(cards.get(0).getRank() == Rank.ACE && isFours()) {
            meldType = Meld.FOUR_ACES;
            return true;
        }

        //check if Four Kings
        if(cards.get(0).getRank() == Rank.KING && isFours()) {
            meldType = Meld.FOUR_KINGS;
            return true;
        }

        //check if Four Queens
        if(cards.get(0).getRank() == Rank.QUEEN && isFours()) {
            meldType = Meld.FOUR_QUEENS;
            return true;
        }

        //check if Four Jacks
        if(cards.get(0).getRank() == Rank.JACK && isFours()) {
            meldType = Meld.FOUR_JACKS;
            return true;
        }

        //check if Flush
        if(isFlush()) {
            meldType = Meld.FLUSH;
            return true;
        }

        meldType = null;
        //if none of the Melds match
        return false;

    }

    /**
     * determines whether the cards stored form a Dix meld
     * @return true if the cards form a Dix, false otherwise
     */
    private Boolean isDix() {
        //Note: A Dix is a card of rank Nine and a suit the same as the trump suit

        //check size
        if(cards.size() != 1) {
            return false;
        }

        //if card is not of rank Nine or not of trump suit, return false
        if(cards.get(0).getRank() != Rank.NINE || cards.get(0).getSuit() != trumpSuit) {
            return false;
        }
        return true;
    }

    /**
     * determines whether the cards stored form any type of marriage meld (royal or otherwise)
     * @return true if the cards form any type of marriage, false otherwise
     */
    private Boolean isAnyMarriage() {
        //check size of meld
        if(cards.size() != 2) {
            return false;
        }

        //If cards are not of the same suit, return false
        if(cards.get(0).getSuit() != cards.get(1).getSuit()) {
            return false;
        }

        //if one card is a King and the other a Queen, return true
        if(cards.get(0).getRank() == Rank.KING && cards.get(1).getRank() == Rank.QUEEN) {
            return true;
        }
        if(cards.get(1).getRank() == Rank.KING && cards.get(0).getRank() == Rank.QUEEN) {
            return true;
        }

        //if none of the conditions match return false
        return false;
    }

    /**
     * determines what type of marriage meld the cards stored make
     * @return the type of marriage meld that the cards comprise
     */
    private Meld typeOfMarriage() {
        //Note: A Marriage contains a King and Queen of any other suit besides the Trump suit

        if(!isAnyMarriage()) {
            return null;
        }

        //If first card is trump suit, return false (only first card is checked because both cards are the same suit)
        //ensured by isAnyMarriage
        if(cards.get(0).getSuit() == trumpSuit) {
            return Meld.ROYAL_MARRIAGE;
        } else {
            return Meld.MARRIAGE;
        }

    }

    /**
     * determines whether the cards stored form a Pinochle meld
     * @return true if the cards form a Pinochle, false otherwise
     */
    private Boolean isPinochle() {
        //Note: A flush contains Queen of Spades and Jack of Diamonds

        //check size
        if(cards.size() != 2) {
            return false;
        }

        //if one card is queen of spades and the other is jack of diamonds, return true
        if(cards.get(0).getRank() == Rank.QUEEN && cards.get(0).getSuit() == Suit.SPADES) {
            if(cards.get(1).getRank() == Rank.JACK && cards.get(1).getSuit() == Suit.DIAMONDS) {
                return true;
            }
        }
        //checking for vice versa
        if(cards.get(1).getRank() == Rank.QUEEN && cards.get(1).getSuit() == Suit.SPADES) {
            if(cards.get(0).getRank() == Rank.JACK && cards.get(0).getSuit() == Suit.DIAMONDS) {
                return true;
            }
        }
        return false;
    }

    /**
     * determines whether the cards stored forms any one of the Fours melds (Four Kings, Four Aces, Four Queens, or Four Jacks)
     * @return true if the cards form a Fours meld, false otherwise
     */
    private Boolean isFours() {
        //Note: Fours are types of melds consisting of Four melds of the same Rank but of different suits
        //These melds are: Four Aces, Four Kings, Four Queens, and Four Jacks
        //This general function checks only checks whether it is a Fours type of meld,
        //it does not care what the specific meld is
        //check card size
        if(cards.size() != 4) {
            return false;
        }
        //we are comparing the ranks of the remaining three card with that of the first card
        Rank whatRank = cards.get(0).getRank();

        //this array of flags is used to keep track of the different suits encountered
        //as we go through the ArrayList of cards
        //it tells us if any suit has been repeated, in which case, it is not a Fours type meld
        //and so, we can return false
        //flag[0] corresponds to Clubs, flag[1] to Diamonds, flag[2] to Hearts, and flag[3] to Spades (based
        //on the order of enums as listed in Card.h)
        //all flags set fo false initially
        Boolean [] flags = {false, false, false, false};

        //loop once for each card
        for(int i = 0; i < 4; i++) {
            //if rank doesn't match in a card, return false
            if(cards.get(i).getRank() != whatRank) {
                return false;
            }
            //if the suit has already been encountered before, return false
            if(flags[cards.get(i).getSuit().ordinal()] == true) {
                return false;
            }
            //switch flag
            flags[cards.get(i).getSuit().ordinal()] = !flags[cards.get(i).getSuit().ordinal()];
        }
        //if all the cards are of the same rank and all their suits are unique, it is a Fours meld
        return true;

    }

    /**
     * determines whether the cards stored form a Flush meld
     * @return true if the cards form a Flush, false otherwise
     */
    private Boolean isFlush() {
        //checking if the meld is a Flush
        //Note: A flush contains five cards: Ace, Ten, King, Queen, and Jack, all of Trump suit

        //check size of meld
        if(cards.size() != 5) {
            return false;
        }
        //This array of flags will keep track of the cards encountered in this
        //group of cards. If a card of trump suit is encountered, corresponding flag is switched.
        //flag[0] corresponds to Nine, flag[1] to Joker, flag[2] to Queen, and
        //so on in ascending order based on the order of enums as listed in Card.h
        //All flags except flag[0] set to false initially, because Nine (represented by flag[0]) is not part of the Flush meld
        //If a card of non-trump suit is encountered, directly return false

        Boolean [] flags = {true, false, false, false, false, false};

        //loop once for each card
        for(int i = 0; i < 5; i++) {
            //if this card is not of the trump suit
            if(cards.get(i).getSuit() != trumpSuit) {
                return false;
            }
            //if the card has already been encountered before in this group of cards (or if this card is a Nine card)
            //then return false
            if (flags[cards.get(i).getRank().ordinal()]) {
                return false;
            }

            //if this card is of the trump suit, hasn't been encountered before, and if it is not a Nine card,
            //then switch flag
            flags[cards.get(i).getRank().ordinal()] = !flags[cards.get(i).getRank().ordinal()];
        }
        //if all the cards are unique, all are of trump suit, and none of them are of rank Nine, then
        //the cards make up a Flush meld.
        return true;
    }
}
