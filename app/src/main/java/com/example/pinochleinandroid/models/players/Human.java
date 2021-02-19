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

public class Human extends Player {

    /**
     * Default constructor for Human class
     */
    public Human() {
        super();
    }

    /**
     * Constructor for Human class that initializes a human object (and Player base class object) with the given parameters
     * @param hand        the Human player's hand,
     * @param capturePile the Human player's capture pile
     * @param meldsPlayed the record of all the melds that the human player has played
     * @param trumpSuit   the trump suit of the current round
     */
    public Human(GroupOfCards hand, GroupOfCards capturePile, MeldsStorage meldsPlayed, Suit trumpSuit) {
        super(hand, capturePile, meldsPlayed, trumpSuit);
    }

    /**
     * Generates suggestion for lead card and the reasoning behind it. Updates the "message" field of the class with the suggestion
     */
    @Override
    public void getHelpForLeadCard() {
        Card suggestedCard = suggestLeadCard();
        message = "Hint: I recommend that you present " + suggestedCard.getShortCardStr()
                + "(" + getCardPositionInHand(suggestedCard) + ") as your lead card because " + super.getReasoning() + ".";
    }

    /**
     * Generates suggestion for chase card and the reasoning behind it. Updates the "message" field of the class with the suggestion
     */
    @Override
    public void getHelpForChaseCard(Card opponentCard) {
        Card suggestedCard = suggestChaseCard(opponentCard);
        message = "Hint: I recommend that you present " + suggestedCard.getShortCardStr()
                + "(" + getCardPositionInHand(suggestedCard) + ") as your chase card because " + super.getReasoning() + ".";
    }
    
    /**
     * Generates suggestion for meld and the reasoning behind it. Updates the "message" field of the class with the suggestion
     */
    @Override
    public void getHelpForMeld() {
        MeldInstance meldToPlay = suggestMeld();

        message = "Hint: I recommend that you present ";

        if (meldToPlay.getNumOfCards() == 1) {
            message += meldToPlay.getCardByPosition(0).getShortCardStr();
        } else {
            //create reasoning string
            for (int i = 0; i < meldToPlay.getNumOfCards() - 1; i++) {
                message += meldToPlay.getCardByPosition(i).getShortCardStr();
                message += ", ";
            }
            message += "and ";
            message += meldToPlay.getCardByPosition(meldToPlay.getNumOfCards() - 1).getShortCardStr();
        }

        message += " to create a " + meldToPlay.getMeldTypeString() + " meld because " + super.getReasoning() + ".";
    }

    /**
     * Plays a lead card in the specified position in hand and returns the card
     * @param position the position of the card to be played
     * @return the card that was played
     */
    @Override
    public Card playLeadCard(int position) {
        Card card = playFromHand(position);
        message = "You chose to play " + card.getShortCardStr() + " as your lead card.";
        return card;
    }

    /**
     * Plays a chase card in the specified position in hand and returns the card
     * @param opponentCard the lead card thrown by the opponent of the round
     * @param position     the position of the card to be played
     * @return the card that was played
     */
    @Override
    public Card playChaseCard(Card opponentCard, int position) {
        Card card = playFromHand(position);
        message = "You chose to play " + card.getShortCardStr() + " as your chase card.";
        return card;
    }

    /**
     * plays a meld from the player's hand using the cards specified by the given positions, and returns the meld played
     * @param positions the positions of the cards to be used to create the meld
     * @return the meld the user played
     */
    @Override
    public MeldInstance playMeld(ArrayList<Integer> positions) {
        MeldInstance meldInstance = createMeld(positions);
        if (meldInstance == null) {
            message = getReasoning();
        } else {
            message = "You played a " + meldInstance.getMeldTypeString() + " for your meld.";
        }
        return meldInstance;
    }
}
