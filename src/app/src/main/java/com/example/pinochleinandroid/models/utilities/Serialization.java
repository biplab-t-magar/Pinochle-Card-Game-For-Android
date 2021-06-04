/************************************************************
 * Name:  Biplab Thapa Magar                                *
 * Project:  Pinochle in Java/Android                       *
 * Class:  OPL Fall 2020                                    *
 * Date:  11/18/2020                                        *
 ************************************************************/

package com.example.pinochleinandroid.models.utilities;

import com.example.pinochleinandroid.models.cards.Card;
import com.example.pinochleinandroid.models.cards.GroupOfCards;
import com.example.pinochleinandroid.models.cards.MeldInstance;
import com.example.pinochleinandroid.models.cards.MeldsStorage;
import com.example.pinochleinandroid.models.cards.Suit;

import java.util.ArrayList;

public class Serialization {
    private Boolean playerObjectsEntered;
    private Boolean playerStringsEntered;
    private GroupOfCards hand;
    private MeldsStorage meldsPlayed;
    private GroupOfCards capturePile;
    private String handString;
    private String meldString;
    private String captureString;

    /**
     * Constructor for the Serialization object
     */
    public Serialization(){
        playerStringsEntered = false;
        playerObjectsEntered = false;
        hand = new GroupOfCards();
        meldsPlayed = new MeldsStorage();
        capturePile = new GroupOfCards();
        handString = "";
        meldString = "";
        captureString = "";
    }

    /**
     * Returns the string representation of the player's hand
     * @return string representation of the player's hand
     */
    public String getHandString() {
        if(playerObjectsEntered == false) {
            return null;
        }
        return handString;
    }

    /**
     * Returns the string representation of the player's capture pile
     * @return string representation of the player's capture pile
     */
    public String getCaptureString(){
        if(playerObjectsEntered == false) {
            return null;
        }
        return captureString;
    }

    /**
     * Returns the string representation of the player's melds
     * @return string representation of the player's melds
     */
    public String getMeldString(){
        if(playerObjectsEntered == false) {
            return null;
        }
        return meldString;
    }

    /**
     * Returns the object representation of the player's hand
     * @return object representation of the player's hand
     */
    public GroupOfCards getHand() {
        if(playerStringsEntered == false) {
            return null;
        }
        return hand;
    }

    /**
     * Returns the object representation of the player's melds played
     * @return object representation of the player's melds played
     */
    public MeldsStorage getMeldsPlayed() {
        if(playerStringsEntered == false) {
            return null;
        }
        return meldsPlayed;
    }

    /**
     * Returns the object representation of the player's capture pile
     * @return object representation of the player's capture pile
     */
    public GroupOfCards getCapturePile(){
        if(playerStringsEntered == false) {
            return null;
        }
        return capturePile;
    }

    /**
     * Receives player hand, meldsPlayed, and capturePile objects in order to serialize them to string form
     * @param hand the hand of a player, of GroupOfCards type
     * @param meldsPlayed the melds played by a player, of MeldsStorage data type
     * @param capturePile the capture pile of a player, of GroupOfCards type
     */
    public void setPlayerObjects(GroupOfCards hand, MeldsStorage meldsPlayed, GroupOfCards capturePile) {
        //first, store all the cards
        this.hand = hand;
        this.meldsPlayed = meldsPlayed;
        this.capturePile = capturePile;
        playerObjectsEntered = true;
        convertObjectsToStrings();
    }

    /**
     * Receives the string representation of the player's hand, melds, and capture pile, (along with other information needed to determine the player's information) in order to convert them object form
     * @param handString the string representation of the player's hand pile
     * @param meldString the string representation of the player's meld pile
     * @param  captureString the string representation of the player's capture pile
     * @param  allRemCards a GroupOfCards object representing all the cards that are not in the stock pile
     * @param  trumpSuit the trump suit of the round
     * @return GroupOfCards that includes all the cards except those in the stock pile and except those possessed by the player
     */
    public GroupOfCards setPlayerStrings(String handString, String meldString, String captureString, GroupOfCards allRemCards, Suit trumpSuit) {
        //first, store all the strings
        this.handString = handString;
        this.meldString = meldString;
        this.captureString = captureString;
        playerStringsEntered = true;

        //calculate hand
        allRemCards = handStrToObject(allRemCards);


        allRemCards = captureStrToObject(allRemCards);

        allRemCards = meldStrToObject(allRemCards, trumpSuit);

        return allRemCards;
    }

    /**
     * Converts the stored hand, capturePile, and meldsPlayed object into string representation
     */
    private void convertObjectsToStrings() {
        if(!playerObjectsEntered) {
            return;
        }
        //for each card in hand, we count how many "complete" meld instances it is part of
        //By "complete" meld instances, we mean those meld instances whose component cards are all still in hand
        //if any card of a previously played instance has already been thrown, it is not a complete meld instance

        //if a card has 0 such meld instances, the card goes to hand string
        //if a card has 1 such meld instances, the card goes to meld string
        //if a card has more than 1 such meld instances, the card goes to meld string marked by an asterisk(*)

        //a vector for the count of how many complete meld instances is each card a part of
        ArrayList<Integer> meldInstanceCount = new ArrayList<>();
        //initialize all counts to 0
        for(int i = 0; i < hand.getNumOfCards(); i++) {
            meldInstanceCount.add(0);
        }

        //vector of every single meld instance a card is part of
        ArrayList<MeldInstance> allMeldsUsingCard;
        //vector of every single "complete" meld instance
        ArrayList<MeldInstance> completeMelds = new ArrayList<>();
        //looping through all cards in hand
        Boolean meldEncounteredBefore;
        for(int i = 0; i < hand.getNumOfCards(); i++) {
            //get all the meld instances that the card is part of
            allMeldsUsingCard = meldsPlayed.getAllMeldsUsingCard(hand.getCardByPosition(i));

            //loop through all the meld instances that use this card and see if all the other cards in each meld instance are still in hand
            //keep count of those meld instances whose component card are all still in hand

            //loop through each meld instance that a card is part of
            for(int j = 0; j < allMeldsUsingCard.size(); j++) {
                meldEncounteredBefore = false;
                if(isACompleteMeldInstance(allMeldsUsingCard.get(j))) {
                    //check if the meld instance has already been encountered (through another card that is part of the meld)
                    for(int k = 0; k < completeMelds.size(); k++) {
                        if(completeMelds.get(k).isIdenticalTo(allMeldsUsingCard.get(j))) {
                            meldEncounteredBefore = true;
                            break;
                        }
                    }
                    //only push the meld if it was not encountered before
                    if(!meldEncounteredBefore) {
                        completeMelds.add(allMeldsUsingCard.get(j));
                    }
                    meldInstanceCount.set(i, meldInstanceCount.get(i) + 1);
                }
            }
        }

        //now, create the hand string
        //create the hand string
        handString = "";
        for(int i = 0; i < hand.getNumOfCards(); i++) {
            //if the card is part of no complete meld instance
            if(meldInstanceCount.get(i) == 0) {
                handString = handString + hand.getCardByPosition(i).getShortCardStr() + " ";
            }
        }

        //create capture string
        captureString = "";
        for(int i = 0; i < capturePile.getNumOfCards(); i++) {
            captureString = captureString + capturePile.getCardByPosition(i).getShortCardStr() + " ";
        }

        //create the meld string
        meldString = "";
        Card meldCard;
        //loop through all the complete meld instances
        for(int i = 0; i < completeMelds.size(); i++) {
            //loop through each card of a complete meld instance
            for(int j = 0; j < completeMelds.get(i).getNumOfCards(); j++) {
                //add each card to the string
                meldCard = completeMelds.get(i).getCardByPosition(j);
                meldString = meldString + meldCard.getShortCardStr();
                //if a card in the meld occurs in another complete meld as well, add an asterisk to it
                //refer to meldInstanceCount for number of occurences of card in other melds
                if(meldInstanceCount.get(hand.getCardPosition(meldCard)) > 1) {
                    meldString = meldString + "*";
                }
                if(j < completeMelds.get(i).getNumOfCards() - 1) {
                    meldString = meldString + " ";
                }
            }
            if(i < completeMelds.size() - 1) {
                meldString = meldString + ", ";
            }
        }

    }

    /**
     * Converts string representation of hand to hand object
     * @param allRemCards all the cards that have not yet been used up to create other card piles (like stock, other player's hand, etc) in the round
     * @return group of all the cards remaining after removing the cards needed by the player's hand
     */
    private GroupOfCards handStrToObject(GroupOfCards allRemCards) {
        //first, get the invidual cards from the hand string and add them to hand
        //none of the cards in the hand string are assumed to be a part of the meld

        //getting individual card strings from hand string
        Card handCard;
        ArrayList<String> handCardStrs;
        handCardStrs = StringUtilities.splitCardsInString(handString);


        //check if all card strings are of correct length (i.e. check if any card contains asterisk )
        for(int i = 0; i < handCardStrs.size(); i++) {

            handCard = StringUtilities.strToCard(handCardStrs.get(i));
            //find first instance of the card in allRemCards
            handCard = allRemCards.getCardsByRankAndSuit(handCard.getRank(), handCard.getSuit()).get(0);
            //remove the instance from allRemCards
            allRemCards.removeCardById(handCard.getId());
            //add hand card to hand
            hand.addCard(handCard);
        }
        return allRemCards;
    }

    /**
     * Converts string representation of capture pile to capture pile object
     * @param allRemCards all the cards that have not yet been used up to create other card piles (like stock, other player's hand, etc) in the round
     * @return group of all the cards remaining after removing the cards needed by the player's capture pile
     */
    private GroupOfCards captureStrToObject(GroupOfCards allRemCards) {
        //first, get the invidual cards from the capture string and add them to capturePile
        //none of the cards in the capture string are assumed to be a part of the meld

        //getting individual card strings from capture string
        Card captureCard;
        ArrayList<String> captureCardStrs;
        captureCardStrs = StringUtilities.splitCardsInString(captureString);


        //check if all card strings are of correct length (i.e. check if any card contains asterisk )
        for(int i = 0; i < captureCardStrs.size(); i++) {
            captureCard = StringUtilities.strToCard(captureCardStrs.get(i));
            //find first instance of the card in allRemCards
            captureCard = allRemCards.getCardsByRankAndSuit(captureCard.getRank(), captureCard.getSuit()).get(0);
            //remove the instance from allRemCards
            allRemCards.removeCardById(captureCard.getId());
            //add capture card to capturePile
            capturePile.addCard(captureCard);

        }
        return allRemCards;
    }

    /**
     * Converts string representation of melds to meld pile object and also adds cards from the string to hand
     * @param allRemCards, all the cards that have not yet been used up to create other card piles (like stock, other player's hand, etc) in the round
     * @return group of all the cards remaining after removing the cards needed by the player's meld and hands
     */
    private GroupOfCards meldStrToObject(GroupOfCards allRemCards, Suit trumpSuit) {
        //first split the string into component melds
        ArrayList<ArrayList<String>> meldVector;
        meldVector = StringUtilities.splitMeldsInString(meldString);



        //for handling repetition of cards in mmeld
        Boolean cardHasAsterisk;
        //to hold a card temporarily
        Card cardHolder;
        //to hold cards temporarily
        ArrayList<Card> cardsHolder;
        MeldInstance meldInstance = new MeldInstance();
        ArrayList<Card> cardsWithAstrk = new ArrayList<>();
        ArrayList<Card> cardsWithoutAstrk = new ArrayList<>();
        ArrayList<Card> cardsExtracted = new ArrayList<>();
        // ArrayList<MeldInstance> meldsToBeStored;
        Boolean cardWasExtracted;
        //loop through each meld instance
        for(int i = 0; i < meldVector.size(); i++) {
            cardsWithAstrk.clear();
            cardsWithoutAstrk.clear();
            meldInstance.removeAllCards();
            //loop through all the cards in a meld instance
            for(int j = 0; j < meldVector.get(i).size(); j++) {
                cardHasAsterisk = false;
                //check if card string is valid
                if(meldVector.get(i).get(j).length() == 3) {
                    if(meldVector.get(i).get(j).charAt(2) == '*') {
                        cardHasAsterisk = true;
                    }
                }
                //if the card is valid
                if(meldVector.get(i).get(j).length() == 2 || meldVector.get(i).get(j).length() == 3) {
                    if(cardHasAsterisk) {
                        cardHolder = StringUtilities.strToCard(meldVector.get(i).get(j).substring(0, 2));
                        cardsWithAstrk.add(cardHolder);
                    } else {
                        cardHolder = StringUtilities.strToCard(meldVector.get(i).get(j));
                        cardsWithoutAstrk.add(cardHolder);
                    }
                }
            }

            //checking if all the cards with asterisks have already been extracted from allRemCards or not
            for(int n = 0; n < cardsWithAstrk.size(); n++) {
                //go through cardsExracted to search for unextracted cards
                cardWasExtracted = false;
                for(int m = 0; m < cardsExtracted.size(); m++) {
                    //if the card was previously extracted
                    if(cardsExtracted.get(m).sameRankAndSuit(cardsWithAstrk.get(n)) == true) {
                        cardWasExtracted = true;
                        cardsWithAstrk.set(n, cardsExtracted.get(m));
                        break;
                    }
                }
                //if the card was not previously extracted, extract it from allRemCards
                if(cardWasExtracted == false) {
                    cardsHolder = allRemCards.getCardsByRankAndSuit(cardsWithAstrk.get(n).getRank(), cardsWithAstrk.get(n).getSuit());

                    cardsWithAstrk.set(n, cardsHolder.get(0));

                    allRemCards.removeCardById(cardsWithAstrk.get(n).getId());
                    cardsExtracted.add(cardsWithAstrk.get(n));
                    //each time a card is extracted, also add it to the hand
                    hand.addCard(cardsWithAstrk.get(n));
                }
                //add the card to the meld instance object
                meldInstance.addCard(cardsWithAstrk.get(n), trumpSuit);
            }
            //now, extracting all non-asterisk cards from allRemCards
            for(int n = 0; n < cardsWithoutAstrk.size(); n++) {
                cardsHolder = allRemCards.getCardsByRankAndSuit(cardsWithoutAstrk.get(n).getRank(), cardsWithoutAstrk.get(n).getSuit());

                cardsWithoutAstrk.set(n, allRemCards.getCardsByRankAndSuit(cardsWithoutAstrk.get(n).getRank(), cardsWithoutAstrk.get(n).getSuit()).get(0));

                allRemCards.removeCardById(cardsWithoutAstrk.get(n).getId());
                cardsExtracted.add(cardsWithoutAstrk.get(n));
                meldInstance.addCard(cardsWithoutAstrk.get(n), trumpSuit);
                //each time a card is extracted, also add it to the hand
                hand.addCard(cardsWithoutAstrk.get(n));
            }

            meldsPlayed.addMeld(meldInstance);
            // //get all the meld instances from meldsPlayed that have previously used any of the asterisked cards
            // for(int n = 0; n < cardsWithAstrk.size(); n++) {

            // }
        }
        return allRemCards;
    }

    /**
     * Checks whether a meld instance is "complete", i.e. whether all the cards that comprise the meld instance are still in the player's hand
     * @param meldInstance the instance of the meld to be checked for completeness
     * @return true if meld instance is complete, false otherwise
     */
    private Boolean isACompleteMeldInstance(MeldInstance meldInstance) {
        for(int i = 0; i < meldInstance.getNumOfCards(); i++) {
            if(!hand.searchCardById(meldInstance.getCardByPosition(i).getId())) {
                return false;
            }
        }
        return true;
    }


}
