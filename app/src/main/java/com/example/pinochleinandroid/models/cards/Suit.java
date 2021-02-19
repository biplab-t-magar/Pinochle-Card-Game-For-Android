/************************************************************
 * Name:  Biplab Thapa Magar                                *
 * Project:  Pinochle in Java/Android                       *
 * Class:  OPL Fall 2020                                    *
 * Date:  11/18/2020                                        *
 ************************************************************/

package com.example.pinochleinandroid.models.cards;

import androidx.annotation.NonNull;

public enum Suit {
    CLUBS, DIAMONDS, HEARTS, SPADES;

    /**
     * generates the string representation of the suit
     * @return the string representation of the suit
     */
    public String getString() {
        return super.toString().substring(0, 1) + super.toString().toLowerCase();
    }

    /**
     * generates the shortened string representation of the suit
     * @return the shortened string representation of the suit
     */
    public String getShortString() {
        return super.toString().substring(0, 1);
    }
}
