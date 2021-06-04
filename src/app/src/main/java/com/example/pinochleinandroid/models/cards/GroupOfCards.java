/************************************************************
 * Name:  Biplab Thapa Magar                                *
 * Project:  Pinochle in Java/Android                       *
 * Class:  OPL Fall 2020                                    *
 * Date:  11/18/2020                                        *
 ************************************************************/

package com.example.pinochleinandroid.models.cards;

import java.lang.reflect.Array;
import java.security.acl.Group;
import java.util.ArrayList;

public class GroupOfCards {
    protected ArrayList<Card> cards;

    /**
     * Default Constructor for GroupOfCards class.
     */
    public GroupOfCards() {
        cards = new ArrayList<Card>();
    }

    /**
     * Overloaded constructed for GroupOfCards class. Adds the given cards to the GroupOfCards object
     *
     * @param cards the ArrayList of cards to be stored
     */
    public GroupOfCards(ArrayList<Card> cards) {
        this.cards = cards;
    }

    /**
     * Copy constructor for the GroupOfCards class.
     *
     * @param otherGroupOfCards the GroupOfCards object of which the copy is to be made
     */
    public GroupOfCards(GroupOfCards otherGroupOfCards) {
        cards = new ArrayList<Card>();
        for (int i = 0; i < otherGroupOfCards.getNumOfCards(); i++) {
            //add copies of all the cards in the ArrayList
            cards.add(new Card(otherGroupOfCards.getCardByPosition(i)));
        }
    }

    /**
     * checks if a card is present within the group of cards
     * @param id the id of the card to be checked
     * @return true if card is found, false otherwise
     */
    public Boolean searchCardById(int id) {
        for (int i = 0; i < cards.size(); i++) {
            if (cards.get(i).getId() == id) {
                return true;
            }
        }
        return false;
    }

    /**
     * To get the card in the group of cards when the id of the card is specified
     * @param id the id of the card to be returned
     * @return the card whose id has been passed as argument
     */
    public Card getCardById(int id) {
        Card card = null;
        for (int i = 0; i < cards.size(); i++) {
            if (cards.get(i).getId() == id) {
                card = cards.get(i);
                break;
            }
        }
        return card;
    }

    /**
     * returns all the cards in the group of cards of the given rank
     * @param rank the rank of the cards to be returned
     * @return all the cards with the given rank
     */
    public ArrayList<Card> getCardsByRank(Rank rank) {
        ArrayList<Card> foundCards = new ArrayList<Card>();
        for (int i = 0; i < cards.size(); i++) {
            if (cards.get(i).getRank() == rank) {
                foundCards.add(cards.get(i));
            }
        }
        return foundCards;
    }

    /**
     * returns all the cards of the given from the group of cards
     * @param suit the suit of the cards to be returned
     * @return all the cards with the given suit
     */
    public ArrayList<Card> getCardsBySuit(Suit suit) {
        ArrayList<Card> foundCards = new ArrayList<Card>();
        for (int i = 0; i < cards.size(); i++) {
            if (cards.get(i).getSuit() == suit) {
                foundCards.add(cards.get(i));
            }
        }
        return foundCards;
    }

    /**
     * returns all the cards stored in this group of cards
     * @return all the cards in this group of cards
     */
    public ArrayList<Card> getCards() {
        return cards;
    }

    /**
     * gets all the cards with the given rank and suit that are present in this group of cards
     * @param rank the rank of the cards to be returned
     * @param suit the suit of the cards to be returned
     * @return all the cards of the given rank and suit that are found in this group of cards
     */
    public ArrayList<Card> getCardsByRankAndSuit(Rank rank, Suit suit) {
        ArrayList<Card> foundCards = new ArrayList<Card>();
        for (int i = 0; i < cards.size(); i++) {
            if (cards.get(i).getRank() == rank && cards.get(i).getSuit() == suit) {
                foundCards.add(cards.get(i));
            }
        }
        return foundCards;
    }

    /**
     * returns the card that is present in the given position
     * @param position the position of the needed card in this group of cards
     * @return the card in the given position
     */
    public Card getCardByPosition(int position) {
        if (position >= cards.size() || position < 0) {
            return null;
        }
        return cards.get(position);
    }

    /**
     * Gets the position of the given card in this group of cards
     * @param card the card whose position is to be returned
     * @return the position of the given card
     */
    public int getCardPosition(Card card) {
        for (int i = 0; i < cards.size(); i++) {
            if (cards.get(i).isIdenticalTo(card)) {
                return i;
            }
        }
        //if card not found, return -1
        return -1;
    }

    /**
     * to get the number of cards in this Group of Cards
     * @return the number of cards contained in this group of cards
     */
    public int getNumOfCards() {
        return cards.size();
    }

    /**
     * to find out if this GroupOfCards object is identical to another GroupOfCards object
     * @param otherGroupOfCards the GroupOfCards object that this object will be compared to
     * @return true if both the Group of Cards have identical cards, false otherwise
     */
    public Boolean isIdenticalTo(GroupOfCards otherGroupOfCards) {
        if (cards.size() != otherGroupOfCards.getNumOfCards()) {
            return false;
        }
        for (int i = 0; i < getNumOfCards(); i++) {
            if (otherGroupOfCards.searchCardById(cards.get(i).getId()) == false) {
                return false;
            }
        }
        return true;

    }

    /**
     * Adds a card to the group of cards
     * @param card the card to be added
     * @return true when card is added succesfully, false otherwise
     */
    public Boolean addCard(Card card) {
        cards.add(card);
        return true;
    }

    /**
     * removes a card of the given id from the group of cards
     * @param id the id of the card to be removed
     * @return return true when card is removed successfully, false otherwise
     */
    public Boolean removeCardById(int id) {
        for (int i = 0; i < cards.size(); i++) {
            if (cards.get(i).getId() == id) {
                cards.remove(i);
            }
        }
        return true;
    }

    /**
     * removes a card from the given position in the group of cards hand
     * @param position the position of the card in the group of cards, of int type
     * @return true when card is successfully removed
     */
    public Boolean removeCardByPosition(int position) {
        if (position >= cards.size()) {
            return false;
        }
        cards.remove(position);
        return true;
    }


}
