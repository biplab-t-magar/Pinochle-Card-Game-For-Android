/************************************************************
 * Name:  Biplab Thapa Magar                                *
 * Project:  Pinochle in Java/Android                       *
 * Class:  OPL Fall 2020                                    *
 * Date:  11/18/2020                                        *
 ************************************************************/

package com.example.pinochleinandroid.models.cards;

public enum Meld {
    FLUSH, ROYAL_MARRIAGE, MARRIAGE, DIX, FOUR_ACES, FOUR_KINGS, FOUR_QUEENS, FOUR_JACKS, PINOCHLE;

    /**
     * generates the string representation of the meld
     * @return the string representation of the meld
     */
    public String getString() {
        String str = super.toString();
        //find index of "_" character, if it exists
        int index = str.indexOf("_");
        //replace "_" character with a " " character
        str = super.toString().replace("_", " ");
        str = str.toLowerCase();
        //create the final string with proper format
        if(index == -1) {
            str = str.substring(0,1).toUpperCase() + str.substring(1);
        } else {
            str = str.substring(0,1).toUpperCase() + str.substring(1, index) + str.substring(index, index + 1).toUpperCase() + str.substring(index + 1);
        }
        return str;
    }

}
