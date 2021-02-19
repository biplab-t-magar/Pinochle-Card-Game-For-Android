/************************************************************
 * Name:  Biplab Thapa Magar                                *
 * Project:  Pinochle in Java/Android                       *
 * Class:  OPL Fall 2020                                    *
 * Date:  11/18/2020                                        *
 ************************************************************/

package com.example.pinochleinandroid.models.cards;

import java.util.ArrayList;

public class MeldsStorage {
    private ArrayList<ArrayList<MeldInstance>> storage;

    /**
     * Constructor for MeldsStorage class
     */
    public MeldsStorage() {
        storage = new ArrayList<ArrayList<MeldInstance>>();
        for (int i = 0; i < Meld.values().length; i++) {
            storage.add(new ArrayList<MeldInstance>());
        }
    }

    /**
     * Gets the number of melds of the given type stored in this MeldsStorage
     *
     * @param meldType the meld whose instances are to be counted
     * @return the number of meld instances of the given meld
     */
    public int getNumOfMeldsByType(Meld meldType) {
        return storage.get(meldType.ordinal()).size();
    }

    /**
     * gets the number of meld instances stored
     *
     * @return the number of meld instances stored
     */
    public int getNumOfMelds() {
        int numOfMelds = 0;
        for (int i = 0; i < Meld.values().length; i++) {
            numOfMelds += storage.get(i).size();
        }
        return numOfMelds;
    }

    /**
     * Returns the ArrayList of all the meld instances stored
     *
     * @return ArrayList of ArrayLists of MeldInstance objects, comprising all the meld instances stored
     */
    public ArrayList<ArrayList<MeldInstance>> getAllMelds() {
        return storage;
    }

    /**
     * Returns all the meld instances of the given type
     *
     * @param meldType the meld whose instances are to be returned
     * @return all the meld instances of the given type
     */
    public ArrayList<MeldInstance> getAllMeldsByType(Meld meldType) {
        return storage.get(meldType.ordinal());
    }

    /**
     * Finds out whether a card has been used to create a meld instance of a particular meld type
     *
     * @param card     the card which is to be checked
     * @param meldType the meld whose instances the card is to be checked in
     * @return true if card is used by a meld instance of the given meld, false otherwise
     */
    public Boolean isCardUsedByMeld(Card card, Meld meldType) {
        //get int representation of the given meld
        int meldTypeInt = meldType.ordinal();
        //get the number of instances of that particular meld type
        int numOfMeldsPlayed = storage.get(meldTypeInt).size();

        for (int i = 0; i < numOfMeldsPlayed; i++) {
            //if that card is found in a meld instance, return true
            if (storage.get(meldTypeInt).get(i).searchCardById(card.getId()) == true) {
                return true;
            }
        }
        //return false;
        return false;
    }

    /**
     * returns all the meld instances that make use of the given card
     *
     * @param card the card which is part of the meld instances to be returned
     * @return ArrayList of MeldInstances that make use of the cards
     */
    public ArrayList<MeldInstance> getAllMeldsUsingCard(Card card) {
        ArrayList<MeldInstance> allMeldsUsingCard = new ArrayList<MeldInstance>();
        //loop through all meld types
        for (int i = 0; i < storage.size(); i++) {
            //loop through all instances of a meld type
            for (int j = 0; j < storage.get(i).size(); j++) {
                //if card is in the given meld instance, push it as one of the results
                if (storage.get(i).get(j).searchCardById(card.getId()) == true) {
                    allMeldsUsingCard.add(storage.get(i).get(j));
                }
            }
        }
        return allMeldsUsingCard;
    }

    /**
     * checks if a group of cards have been used together to create a meld before
     *
     * @param cards the ArrayList of the cards to be checked
     * @return true if the group of cards have together been used to create a meld before, false otherwise
     */
    public Boolean cardsUsedForSameMeld(ArrayList<Card> cards) {
        //get int representation of the given meld

        //get the number of instances of that particular meld type
        int numOfMeldsPlayed;

        //loop through every meld type
        for (int meldTypeInt = 0; meldTypeInt < Meld.values().length; meldTypeInt++) {
            numOfMeldsPlayed = storage.get(meldTypeInt).size();
            //loop through each instance of a meld type
            for (int i = 0; i < numOfMeldsPlayed; i++) {
                Boolean cardsUsedBySameMeld = true;
                //check if all cards are present in the meld instance
                for (int j = 0; j < cards.size(); j++) {
                    //if one of the cards is not found in the meld instance currently being looped through
                    if (storage.get(meldTypeInt).get(i).searchCardById(cards.get(j).getId()) != true) {
                        cardsUsedBySameMeld = false;
                        break;
                    }
                }
                //if a meld instance with all the cards in it has been found, return true;
                if (cardsUsedBySameMeld == true) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * checks if a group of cards have been used together to create a meld before
     *
     * @param meldInstance a MeldInstance object storing the cards to be checked
     * @return true if the group of cards comprising the meld intance have together been used to create a meld before, false otherwise
     */
    public Boolean cardsUsedForSameMeld(MeldInstance meldInstance) {
        ArrayList<Card> cards = new ArrayList<Card>();
        for (int i = 0; i < meldInstance.getNumOfCards(); i++) {
            cards.add(meldInstance.getCardByPosition(i));
        }
        return cardsUsedForSameMeld(cards);
    }

    /**
     * Find out whether a card has been used to create any meld before
     *
     * @param card the card to be checked for use by meld instances
     * @return true if the card has been used by a meld instance, false if not
     */
    public Boolean isCardUsedByAnyMeld(Card card) {

        for (int meldType = 0; meldType < Meld.values().length; meldType++) {
            if (isCardUsedByMeld(card, Meld.values()[meldType])) {
                return true;
            }
        }
        return false;
    }

    /**
     * Adds a meld instance to the melds storage
     *
     * @param meldInstance the meld instance to be added
     * @return true if successfully added, false if not added
     */
    public Boolean addMeld(MeldInstance meldInstance) {
        if (meldInstance.isValidMeld() == false) {
            return false;
        }

        storage.get(meldInstance.getMeldType().ordinal()).add(meldInstance);
        return true;
    }

    /**
     * Adds multiple meld instances to the melds storage
     *
     * @param meldInstances a ArrayList of all the meld instances
     * @return true if melds were successfully added, false otherwise
     */
    public Boolean addMelds(ArrayList<MeldInstance> meldInstances) {
        Boolean allMeldsAdded = true;
        for (int i = 0; i < meldInstances.size(); i++) {
            if (addMeld(meldInstances.get(i)) == false) {
                allMeldsAdded = false;
            }
        }
        return allMeldsAdded;
    }

    /**
     * Removes a given meld instance from the melds storage
     *
     * @param meldInstance the meld instance to be removed
     * @return returns true of removal was successful, false otherwise
     */
    public Boolean removeMeld(MeldInstance meldInstance) {
        //get meld type of the given meld (in integer representation)
        int meldTypeInt = meldInstance.getMeldType().ordinal();
        //get the amount of melds of that particular meld type
        int numOfMeldsPlayed = storage.get(meldTypeInt).size();
        for (int i = 0; i < numOfMeldsPlayed; i++) {
            if (storage.get(meldTypeInt).get(i) == meldInstance) {
                storage.get(meldTypeInt).remove(i);
                return true;
            }
        }
        return false;
    }

}