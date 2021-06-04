/************************************************************
 * Name:  Biplab Thapa Magar                                *
 * Project:  Pinochle in Java/Android                       *
 * Class:  OPL Fall 2020                                    *
 * Date:  11/18/2020                                        *
 ************************************************************/

package com.example.pinochleinandroid.models.cards;

public class Card {
    private Rank rank;
    private Suit suit;
    private int id;

    /**
     Default constructor for Card class that initializes a card object. Creates a Card with undefined rank, suit, and id
    */
    public Card() {
        id = -1;
        rank = null;
        suit = null;
    }

    /**
     Overloaded constructor for Card class that initializes a card object. Creates a Card with a rank, suit, and id based on the values sent into the parameter
     @param id the id to be assigned to the card
     @param rank the rank to be assigned to the card
     @param suit the suit to be assigned to the card
     */
    public Card(int id, Rank rank, Suit suit) {
        this.id = id;
        this.rank = rank;
        this.suit = suit;
    }

    /**
     A copy constructor for the Card class.
     @param otherCard the card object of which the copy is to be made
     */
    public Card(Card otherCard) {
        id = otherCard.getId();
        rank = otherCard.getRank();
        suit = otherCard.getSuit();
    }

    /**
     A selector function that returns the id of the card
     @return the id of the card
     */
    public int getId() {
        return id;
    }

    /**
     A selector function to that returns the rank of the card
     @return the rank of the card
     */
    public Rank getRank() {
        return rank;
    }

    /**
     a selector function to that returns the suit of the card
     @return the suit of the card
     */
    public Suit getSuit() {
        return suit;
    }

    /**
     a function that returns the rank of the card in string format
     @return the rank of the card as a string
     */
    public String getRankString() {
        return rank.getString();
    }

    /**
     a function that returns the suit of the card in string format
     @return the suit of the card as a string
     */
    public String getSuitString() {
        return suit.getString();
    }

    /**
     a function to that returns the rank and suit of the card in string format
     @return the rank and suit of the card, as a single string, in "<rank> of <suit>"" formation
     */
    public String getCardString() {
        if(rank == null || suit == null) {
            return null;
        }
        return rank.getString() + " of " + suit.getString();
    }

    /**
     a function to that returns the rank and suit of the card in shortened string format
     @return the rank and suit of a card in shortened string format, like 'AS' for an Ace of Spades card
     */

    public String getShortCardStr() {
        if(rank == null || suit == null) {
            return null;
        }
        return rank.getShortString() + suit.getShortString();
    }

    /**
     a mutator function to set the id of the card
     @param id the id to be assigned to the card
     @return true if mutation was successful, false if not
     */
    public Boolean setId(int id) {
        this.id = id;
        return true;
    }

    /**
     a mutator function to that set the rank of the card
     @param rank the rank to be assigned to the card
     @return true if mutation was successful, false if not
     */
    public Boolean setRank(Rank rank) {
        this.rank = rank;
        return true;
    }

    /**
     a mutator function to that set the suit of the card
     @param suit the suit to be assigned to the card
     @return true if mutation was successful, false if not
     */
    public Boolean setSuit(Suit suit) {
        this.suit = suit;
        return true;
    }

    /**
     A function used to know if a card has greater rank than another card
     @param otherCard the card that this card will be compared to
     @return true if this card has greater rank compared to otherCard, false otherwise
     */
    public Boolean hasGreaterRankThan(Card otherCard) {
        if(rank.ordinal() > otherCard.getRank().ordinal()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     A function used to know if a card has less rank than another card
     @param otherCard the card that this card will be compared to
     @return true if this card has greater less compared to otherCard, false otherwise
     */
    public Boolean hasLessRankThan(Card otherCard) {
        if(rank.ordinal() < otherCard.getRank().ordinal()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     Function to determine whether two cards are identical
     @param otherCard the card that this card will be compared to
     @return true if both cards have the same rank, suit, and id; false otherwise
     */
    public Boolean isIdenticalTo(Card otherCard) {
        return id == otherCard.getId() && rank == otherCard.getRank() && suit == otherCard.getSuit();
    }

    /**
     Function to determine whether two cards have the same rank and suit
     @param otherCard the card that this card will be compared to
     @return true if both cards have the same rank and suit; false otherwise
     */
    public Boolean sameRankAndSuit(Card otherCard) {
        return (rank == otherCard.getRank() && suit == otherCard.getSuit());
    }
}
