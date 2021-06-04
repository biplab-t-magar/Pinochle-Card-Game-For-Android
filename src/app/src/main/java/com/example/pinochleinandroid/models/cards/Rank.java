/************************************************************
 * Name:  Biplab Thapa Magar                                *
 * Project:  Pinochle in Java/Android                       *
 * Class:  OPL Fall 2020                                    *
 * Date:  11/18/2020                                        *
 ************************************************************/

package com.example.pinochleinandroid.models.cards;

public enum Rank {
    NINE, JACK, QUEEN, KING, TEN, ACE;

    /**
     * generates the string representation of the rank
     * @return the string representation of the rank
     */
    public String getString() {
        return super.toString().substring(0, 1) + super.toString().toLowerCase();
    }

    /**
     * generates the shortened string representation of the rank
     * @return the shortened string representation of the rank
     */
    public String getShortString() {
        String string = super.toString().substring(0, 1);
        if(string.equals("T")) {
            return "X";
        } else if (string.equals("N")) {
            return "9";
        }
        else {
            return super.toString().substring(0, 1);
        }

    }

}
