/************************************************************
 * Name:  Biplab Thapa Magar                                *
 * Project:  Pinochle in Java/Android                       *
 * Class:  OPL Fall 2020                                    *
 * Date:  11/18/2020                                        *
 ************************************************************/

package com.example.pinochleinandroid.models.players;

import com.example.pinochleinandroid.models.cards.Card;
import com.example.pinochleinandroid.models.cards.GroupOfCards;
import com.example.pinochleinandroid.models.cards.MeldInstance;
import com.example.pinochleinandroid.models.cards.MeldsStorage;
import com.example.pinochleinandroid.models.cards.Suit;

import java.util.ArrayList;

public class Computer extends Player{

    /**
     * Default constructor for Card class that initializes a computer object.
     */
    public Computer() {
        super();
    }

    /**
     * Constructor for Computer class that initializes a computer object (and Player base class object) with the given parameters
     * @param hand the Computer player's hand,
     * @param capturePile the Computer player's capture pile
     * @param meldsPlayed the record of all the melds that the computer player has played
     * @param trumpSuit the trump suit of the current round
     */
    public Computer(GroupOfCards hand, GroupOfCards capturePile, MeldsStorage meldsPlayed, Suit trumpSuit) {
        super(hand, capturePile, meldsPlayed, trumpSuit);
    }

    /**
     * To play a lead card during a turn
     * @param position the position of the card to be played (irrelevant to the Computer object but needed to satisfy method override)
     * @return The card that is to be played as the lead card, of type Card
     */
    @Override
    public Card playLeadCard(int position) {
        Card cardToPlay = suggestLeadCard();
        playFromHand(cardToPlay);
        message = "The computer chose to play " + cardToPlay.getShortCardStr() + " as its lead card" +
                " because " + super.getReasoning() + ".";
        return cardToPlay;
    }

    /**
     * To play a chase card during a turn
     * @param opponentCard the card of the opponent player, of type Card
     * @param position the position of the card to be played (irrelevant to the Computer object but needed to satisfy method override)
     * @return The chase card to be played by the computer player during the turn, of type Card
     */
    @Override
    public Card playChaseCard(Card opponentCard, int position) {
        Card cardToPlay = suggestChaseCard(opponentCard);
        playFromHand(cardToPlay);
        message = "The computer chose to play " + cardToPlay.getShortCardStr() + " as its chase card" +
                " because " + super.getReasoning() + ".";
        return cardToPlay;
    }

    /**
     * Plays a meld by picking a meld
     * @param positions the positions of the cards to be played (irrelevant to the Computer object but needed to satisfy method override)
     * @return the meld played by the computer
     */
    @Override
    public MeldInstance playMeld(ArrayList<Integer> positions) {
        MeldInstance meldToPlay = suggestMeld();
        if(meldToPlay.getNumOfCards() == 0) {
            message = "The computer has no meld to play.";
            return meldToPlay;
        }

        if(createMeld(meldToPlay) == null) {
            System.out.println("Unable to play meld");
        }


        //creating the string that forms the console output that explains reasoning
        message = "The computer chose to play ";

        if(meldToPlay.getNumOfCards() == 1) {
            message += meldToPlay.getCardByPosition(0).getShortCardStr();
        } else {
            for(int i = 0; i < meldToPlay.getNumOfCards() - 1; i++) {
                message += meldToPlay.getCardByPosition(i).getShortCardStr();
                message += ", ";
            }
            message += "and ";
            message += meldToPlay.getCardByPosition(meldToPlay.getNumOfCards() - 1).getShortCardStr();
        }

        message = message + " to create a " + meldToPlay.getMeldTypeString() + " meld because " + super.getReasoning() + ".";
        return meldToPlay;
    }

    /**
     * Dummy Implementations for the abstract function from base class. Does nothing
     */
    @Override
    public void getHelpForLeadCard() {
        //do nothing
    }

    /**
     * Dummy Implementations for the abstract function from base class. Does nothing
     */
    @Override
    public void getHelpForChaseCard(Card opponentCard) {
        //do nothing
    }

    /**
     * Dummy Implementations for the abstract function from base class. Does nothing
     */
    @Override
    public void getHelpForMeld() {
        //do nothing
    }

}
