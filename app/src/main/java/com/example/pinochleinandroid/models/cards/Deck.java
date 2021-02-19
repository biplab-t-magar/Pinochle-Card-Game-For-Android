/************************************************************
 * Name:  Biplab Thapa Magar                                *
 * Project:  Pinochle in Java/Android                       *
 * Class:  OPL Fall 2020                                    *
 * Date:  11/18/2020                                        *
 ************************************************************/

package com.example.pinochleinandroid.models.cards;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class Deck {
    private final static int NUM_OF_EACH_CARD = 2;

    private ArrayList<Card> cards;

    /**
     Default constructor for Deck class. Populates the deck with all the cards in a game of Pinochle and shuffles the cards.
     */
    public Deck() {
        cards = new ArrayList<Card>();
        //populate all the card in the deck (in the default case, with 2 of each separate cards to make a total of 48 cards)
        populate();

        //shuffle the deck
        shuffle();
    }

    /**
     selector function that returns the number of cards remaining
     @return the number of cards currently in the deck
     */
    public int getNumRemaining() {
        //return size of ArrayList of cards
        return cards.size();
    }


    /**
     removes a card from the top of the deck and returns it
     @return a single card from the top of the deck
     */
    public Card takeOneFromTop() {
        //if cards have run out, return null
        if(cards.size() <= 0 ) {
            return null;
        }

        //get card to return
        Card cardAtTop = cards.get(cards.size() - 1);
        //remove the card from the top of the ArrayList
        cards.remove(cards.size() - 1);

        return cardAtTop;
    }


    /**
     returns copy of all the cards that are still stored in the deck object
     @return the ArrayList containing all the cards
     */
    public ArrayList<Card> getAllRemainingCards() {
        return cards;
    }

    /**
     Puts the given card at the top of the deck.
     @param card the card to be put on top of the deck
     */
    public void putCardAtTop(Card card) {
        cards.add(card);
    }

    /**
     Removes all the card from the deck
     */
    public void clear() {
        cards.clear();
    }

    /**
     Populates the Deck with all the cards, each distinct card repeated as many times as specified
     */
    private void populate() {
        //start with id of 0
        int id = 0;
        //loop once for each rank (i.e. loop 6 times for 6 different ranks)
        //note: since Ranks is set as an enum, ranks correspond to integers in the following way:
        // Nine = 0, Jack = 1, Queen = 2, King = 3, Ten = 4, Ace = 5
        for(int i = 0; i < Rank.values().length; i++) {
            //loop once for each suit (i.e. loop 4 times for 4 different suits of each rank)
            //note: since Suits is set as an enum, suits correspond to integers in the following way:
            // Clubs = 0, Diamonds = 1, Hearts = 2, Spades = 3
            for(int j = 0; j < Suit.values().length; j++) {
                //loop once for each number of each card type (2 in default case)
                for(int k = 0; k < NUM_OF_EACH_CARD; k++) {
                    //push a new card to the ArrayList
                    //ints i and j are cast into corresponding Rank and Suit values to pass as arguments into Card constructor
                    cards.add(new Card(id, Rank.values()[i], Suit.values()[j]));
                    id++;
                }
            }
        }
    }

    /**
     Shuffles the order of the cards in the deck
     */
    public void shuffle() {
        //shuffle the ArrayList object containing all the cards
        Collections.shuffle(cards);
    }


}
