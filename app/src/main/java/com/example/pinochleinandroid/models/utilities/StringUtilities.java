/************************************************************
 * Name:  Biplab Thapa Magar                                *
 * Project:  Pinochle in Java/Android                       *
 * Class:  OPL Fall 2020                                    *
 * Date:  11/18/2020                                        *
 ************************************************************/

package com.example.pinochleinandroid.models.utilities;

import com.example.pinochleinandroid.models.cards.Card;
import com.example.pinochleinandroid.models.cards.Rank;
import com.example.pinochleinandroid.models.cards.Suit;

import java.util.ArrayList;

public class StringUtilities {
    /**
     * Removes all white space from the beginning and end of a string
     * @param str the string which is to be stripped of white space on both ends
     * @return the string without white space on either end
     */
    public static String stripString(String str) {
        String strippedString = "";
        //strip from front
        for(int i = 0; i < str.length(); i++) {
            if(str.charAt(i) != ' ' && str.charAt(i) != '\t') {
                strippedString = str.substring(i);
                break;
            }
        }
        //strip from back
        str = strippedString;
        for(int i = str.length() - 1; i >= 0; i--) {
            if(str.charAt(i) != ' ' && str.charAt(i) != '\t') {
                strippedString = str.substring(0, i + 1);
                break;
            }
        }
        return strippedString;
    }

    /**
     * checks if a string represents a valid card (does not include asterisked cards)
     * @param str the string whose validity is to be checked
     * @return true if string is a valid card, false otherwise
     */
    public static Boolean isAValidCardStr(String str) {
        //all cards are represented by two characters
        if(str.length() != 2) {
            return false;
        }
        //first character of a string must be a valid rank
        if(str.charAt(0) != 'A' && str.charAt(0) != 'X' && str.charAt(0) != 'K' && str.charAt(0) != 'Q' && str.charAt(0) != 'J' && str.charAt(0) != '9') {
            return false;
        }
        //second character of a string must be a valid suit
        if(str.charAt(1) != 'C' && str.charAt(1) != 'D' && str.charAt(1) != 'H' && str.charAt(1) != 'S') {
            return false;
        }
        return true;
    }

    /**
     * Checks if a character represents a valid rank
     * @param rank the character to be checked for validity
     * @return true if the character represent a valid rank, false otherwise
     */
    public static Boolean isAValidRankStr(char rank) {
        if(rank != 'A' && rank != 'X' && rank != 'K' && rank != 'Q' && rank != 'J' && rank != '9') {
            return false;
        }
        return true;
    }

    /**
     * Checks if a character represents a valid suit
     * @param suit the character to be checked for validity
     * @return true if the character represent a valid suit, false otherwise
     */
    public static Boolean isAValidSuitStr(char suit) {
        if(suit != 'C' && suit != 'D' && suit != 'H' && suit != 'S') {
            return false;
        }
        return true;
    }

    /**
     * Checks if a string represents valid card (also includes asterisked cards)
     * @param str the string to be checked for validity
     * @return true if the string represents a valid card string, false otherwise
     */
    public static Boolean checkCardStrValidity(String str) {
        //check if character has asterisk
        if(str.length() == 3 && str.charAt(2) != '*') {
            return false;
        }
        //check if character has correct number of characters
        if(str.length() > 3) {
            return false;
        }
        //check if string (excluding asterisk) is a valid card
        if(!isAValidCardStr(str.substring(0, 2))) {
            return false;
        }
        return true;
    }

    /**
     * splits a string of cards into a ArrayList of individual card strings
     * @param str the string to be split into component card strings
     * @return the ArrayList of individual card strings
     */
    public static ArrayList<String> splitCardsInString(String str) {
        //extract card strings (including asterisk if present)
        //throw exception if any string besides card, white space, or * occurs

        //remove white space from both ends of the string
        str = stripString(str);
        String cardStr = "";
        ArrayList<String> separatedCardStrs = new ArrayList<>();
        for(int i = 0; i < str.length(); i++) {
            if(str.charAt(i) != ' ') {
                cardStr = cardStr + str.charAt(i);
            } else {
                if(checkCardStrValidity(cardStr) == false) {
                    return null;
                }
                //if the string is valid, add it the result
                separatedCardStrs.add(cardStr);
                cardStr = "";
            }
        }
        //store last card string
        if(cardStr != "") {
            if(checkCardStrValidity(cardStr) == false) {
                return null;
            }
            //if the string is valid, add it the result
            separatedCardStrs.add(cardStr);
        }
        return separatedCardStrs;
    }

    /**
     * gets a ArrayList of cards from a string representing a series of cards
     * @param str the string containing the cards
     * @return the ArrayList consisting of each card extracted from the string
     */
    public static ArrayList<Card> strToVectorOfCards(String str) {
        ArrayList<Card> cards = new ArrayList<>();
        ArrayList<String> cardStrs = splitCardsInString(str);
        for(int i = 0; i < cardStrs.size(); i++) {
            cards.add(strToCard(cardStrs.get(i)));
        }
        return cards;
    }

    /**
     * separates each meld listed in a string into individual meld strings
     * @param str the string representation of melds
     * @return ArrayList containing each individual meld in the string, which are in turn stored as ArrayLists of individual card strings
     */
    public static ArrayList<ArrayList<String>> splitMeldsInString(String str) {
        //extract all individual cards of all individual melds
        //throw exception if not a valid meld string

        //remove white space from both ends of the string
        str = stripString(str);
        String meldStr = "";
        ArrayList<ArrayList<String>> allMelds = new ArrayList<>();
        int meldIndex = 0;
        for(int i = 0; i < str.length(); i++) {
            if(str.charAt(i) != ',') {
                meldStr = meldStr + str.charAt(i);
            } else {
                //add a new meld entry to allMelds ArrayList
                allMelds.add(new ArrayList<String>());
                allMelds.set(meldIndex, splitCardsInString(meldStr));
                meldStr = "";
                meldIndex++;
            }
        }
        //store last meld in the string
        if(meldStr.length() != 0) {
            allMelds.add(new ArrayList<String>());
            allMelds.set(meldIndex, splitCardsInString(meldStr));
        }
        return allMelds;

    }

    /**
     * converts a string representation of a card to a Card object
     * @param str, the string representation of the card
     * @return the card object represented by the string
     */
    public static Card strToCard(String str) {
        if(!isAValidCardStr(str)) {
            return null;
        }
        Card card = new Card();
        Rank rank;
        Suit suit;
        rank = strToRank(str.charAt(0));
        suit = strToSuit(str.charAt(1));

        card.setRank(rank);
        card.setSuit(suit);

        return card;
    }

    /**
     * converts a string representation of a rank to a Rank enum type
     * @param rank the character representation of the rank
     * @return the Rank represented by the string
     */
    public static Rank strToRank(char rank) {
        switch (rank) {
            case 'A':
                return Rank.ACE;
            case 'X':
                return Rank.TEN;
            case 'K':
                return Rank.KING;
            case 'Q':
                return Rank.QUEEN;
            case 'J':
                return Rank.JACK;
            case '9':
                return Rank.NINE;
            default:
                return null;
        }
    }

    /**
     * converts a string representation of a suit to a Suit enum type
     * @param suit the character representation of the suit
     * @return the Suit represented by the string
     */
    public static Suit strToSuit(char suit) {
        switch (suit) {
            case 'C':
                return Suit.CLUBS;
            case 'S':
                return Suit.SPADES;
            case 'H':
                return Suit.HEARTS;
            case 'D':
                return Suit.DIAMONDS;
            default:
                return null;
        }
    }

}
