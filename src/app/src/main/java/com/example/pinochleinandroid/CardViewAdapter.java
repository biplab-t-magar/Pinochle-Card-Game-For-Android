/************************************************************
 * Name:  Biplab Thapa Magar                                *
 * Project:  Pinochle in Java/Android                       *
 * Class:  OPL Fall 2020                                    *
 * Date:  11/18/2020                                        *
 ************************************************************/

package com.example.pinochleinandroid;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pinochleinandroid.models.cards.Card;

import java.util.ArrayList;

public class CardViewAdapter extends RecyclerView.Adapter<CardViewAdapter.ViewHolder> {

    //list of cards
    private ArrayList<String> cards = new ArrayList<>();
    String cardPileType;
    private Context context;

    /**
     * Constructor for the CardViewAdapter class
     * @param context the activity class that contains the handler for items within the RecyclerView (i.e. the side-scrollable container)
     * @param cards the cards that are to be displayed inside the side-scrollable container
     * @param cardPileType string to determine whether a card should be selectable (single or multi) or non-selectable
     */
    public CardViewAdapter(Context context, ArrayList<String> cards, String cardPileType) {
        this.cards = cards;
        this.context = context;
        this.cardPileType = cardPileType;
    }

    /**
     * The method called to generate cards (based on the card layout) and put them inside ViewHolder containers
     * @param parent the view (to be used for inflation) that will contain the card view
     * @param viewType the type of view
     * @return the ViewHolder object containing the view representing a card
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_list_cards, parent, false);
        return new ViewHolder(view);
    }

    /**
     * This function is used to specify the characteristics of the card (view) when it is being generated
     * @param holder the ViewHolder object that will hold all the views (i.e. cards) within the Recycler View (side-scrollable view)
     * @param position the position that the view (card) is to be displayed in the scroll view
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.card.setText(cards.get(position));
        if (cards.get(position).equals("  ")) {
            holder.card.setBackgroundResource(0);
        }
        //if the card is single selectable
        if (cardPileType.equals(GameActivity.SINGLE_SELECTABLE)) {
            //we specify in the tag the the card is a single-selectable card
            holder.card.setTag(R.string.card_selectable_type, GameActivity.SINGLE_SELECTABLE);

            //we also specify in the tag the position for the card so that we can get the card's position when it is selected
            holder.card.setTag(R.string.card_position, position);

            //set click handler
            holder.card.setOnClickListener((View.OnClickListener) context);
            //if the card is multi-selectable
        } else if (cardPileType.equals(GameActivity.MULTI_SELECTABLE)) {
            //we specify in the tag the the card is a single-selectable card
            holder.card.setTag(R.string.card_selectable_type, GameActivity.MULTI_SELECTABLE);

            //we also specify in the tag the position for the card so that we can get the card's position when it is selected
            holder.card.setTag(R.string.card_position, position);

            //set click handler
            holder.card.setOnClickListener((View.OnClickListener) context);
        }

    }

    /**
     * Returns the number of cards to be displayed in the side-scroll view (Recycler View)
     * @return the number of cards to be displayed in the side-scroll view (Recycler View)
     */
    @Override
    public int getItemCount() {
        return cards.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView card;

        /**
         * The constructor for the ViewHolder class
         */
        public ViewHolder(View itemView) {
            super(itemView);
            card = itemView.findViewById(R.id.card);
        }
    }
}
