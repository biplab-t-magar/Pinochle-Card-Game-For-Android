/************************************************************
 * Name:  Biplab Thapa Magar                                *
 * Project:  Pinochle in Java/Android                       *
 * Class:  OPL Fall 2020                                    *
 * Date:  11/18/2020                                        *
 ************************************************************/

package com.example.pinochleinandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pinochleinandroid.models.GameModel;
import com.example.pinochleinandroid.models.cards.Card;
import com.example.pinochleinandroid.models.cards.GroupOfCards;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {

    //constants to indicate card pile types, or whether cards should be singly selectable, multiply selectable, or non-selectable
    public static final String SINGLE_SELECTABLE = "single_selectable";
    public static final String MULTI_SELECTABLE = "multi_selectable";
    public static final String NON_SELECTABLE = "non_selectable";

    private GameModel gameModel;
    private int selectedThrowCard;
    private ArrayList<Integer> selectedMeldCards;

    /**
     * The method called when an this Activity is first created
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        gameModel = new GameModel();
        //set the selected cards to unselected
        selectedThrowCard = -1;
        selectedMeldCards = new ArrayList<>();

        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String str = intent.getStringExtra(WelcomeActivity.NEW_OR_LOAD);
        //if user chose to start a new game
        if (intent.getStringExtra(WelcomeActivity.NEW_OR_LOAD).equals("new_game")) {
            gameModel.startNewGame();
            showRoundDeclarationView();
        } else {
            setContentView(R.layout.load_game_layout);
        }

    }

    /**
     * A general click event handler for views that are programmatically bound to click listeners
     * @param v the view that triggered the event that called this handler
     */
    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.card) {
            handleCardSelect(v);
        }
    }

    /**
     * A handler method for the Confirm Quit button that quits the game of Pinochle by changing into the Home Screen activity
     * @param view the view (i.e. the Confirm Quit button) that triggered this handler
     * @return
     */
    public void quitGame(View view) {
        Intent intent = new Intent(this, WelcomeActivity.class);
        startActivity(intent);
    }

    /**
     * An event handler method. It is an overloaded method that calls that wraps the non-event handler method showRoundDeclarationView
     * @param view the view that activated the event that called this event handler
     */
    public void showRoundDeclarationView(View view) {
        showRoundDeclarationView();
    }

    /**
     * An event handler method that generates the view for the main Pinochle game table
     * @param view the view that triggered the event that called this handler
     */
    public void showGameBoard(View view) {
        //check if coin toss is needed
        if (gameModel.isHumansTurn() == null) {
            //if it is unclear who goes next (i.e. if both human and computer player have the same points at the start of a turn)
            //show Coin Toss layout
            setContentView(R.layout.coin_toss_layout);
            return;
        }

        //set the games scores and round scores
        int[] gameScores = gameModel.getGameScores();
        int[] roundScores = gameModel.getRoundScores();

        if (gameModel.getCurrentStage().equals(GameModel.ROUND_END_STAGE)) {
            setContentView(R.layout.play_another_round_layout);


            TextView textView = (TextView) findViewById(R.id.finalCompRoundScore);
            textView.setText(Integer.toString(roundScores[GameModel.COMPUTER_PLAYER]));

            textView = (TextView) findViewById(R.id.finalHumRoundScore);
            textView.setText(Integer.toString(roundScores[GameModel.HUMAN_PLAYER]));

            textView = (TextView) findViewById(R.id.currentCompGameScore);
            textView.setText(Integer.toString(gameScores[GameModel.COMPUTER_PLAYER]));

            textView = (TextView) findViewById(R.id.currentHumGameScore);
            textView.setText(Integer.toString(gameScores[GameModel.HUMAN_PLAYER]));

            textView = (TextView) findViewById(R.id.winner);
            String winner;
            if (gameModel.roundLeader() == GameModel.HUMAN_PLAYER) {
                winner = "The Human player won the round!";
            } else if (gameModel.roundLeader() == GameModel.COMPUTER_PLAYER) {
                winner = "The Computer player won the round!";
            } else {
                winner = "The round resulted in a draw";
            }
            textView.setText(winner);

            return;
        }

        setContentView(R.layout.game_table_layout);
        //set the round number
        TextView textView = (TextView) findViewById(R.id.roundNumber);
        textView.setText(Integer.toString(gameModel.getRoundNumber()));


        //set game scores for computer and human
        textView = (TextView) findViewById(R.id.compGameScore);
        textView.setText(Integer.toString(gameScores[GameModel.COMPUTER_PLAYER]));
        textView = (TextView) findViewById(R.id.humGameScore);
        textView.setText(Integer.toString(gameScores[GameModel.HUMAN_PLAYER]));
        //set round scores for computer and human
        textView = (TextView) findViewById(R.id.compRoundScore);
        textView.setText(Integer.toString(roundScores[GameModel.COMPUTER_PLAYER]));
        textView = (TextView) findViewById(R.id.humRoundScore);
        textView.setText(Integer.toString(roundScores[GameModel.HUMAN_PLAYER]));

        //set the messages to be displayed
        String[] messages = gameModel.getMessages();
        textView = (TextView) findViewById(R.id.computerMessage);
        textView.setText(messages[GameModel.COMPUTER_PLAYER]);
        textView = (TextView) findViewById(R.id.humanMessage);
        textView.setText(messages[GameModel.HUMAN_PLAYER]);

        //set the trump card
        textView = (TextView) findViewById(R.id.trumpCard);
        textView.setText(gameModel.getTrumpCardString());

        String currentStage = gameModel.getCurrentStage();
        //show cards
        //show player hands
        ArrayList<ArrayList<String>> playerHands = gameModel.getPlayerHands();
        //computer hand
        displayCards(findViewById(R.id.compHand), playerHands.get(GameModel.COMPUTER_PLAYER), NON_SELECTABLE);
        //human hand
        //if its the humans turn
        if (gameModel.isHumansTurn()) {
            //if its the human turn to throw a card
            if (currentStage.equals(GameModel.LEAD_CARD_STAGE) || currentStage.equals(GameModel.CHASE_CARD_STAGE)) {
                displayCards(findViewById(R.id.humHand), playerHands.get(GameModel.HUMAN_PLAYER), SINGLE_SELECTABLE);
                //if its the humans turn to create a meld
            } else if (currentStage.equals(GameModel.MELD_STAGE)) {
                displayCards(findViewById(R.id.humHand), playerHands.get(GameModel.HUMAN_PLAYER), MULTI_SELECTABLE);
            } else {
                displayCards(findViewById(R.id.humHand), playerHands.get(GameModel.HUMAN_PLAYER), NON_SELECTABLE);
            }
            //if it is not the human's turn
        } else {
            displayCards(findViewById(R.id.humHand), playerHands.get(GameModel.HUMAN_PLAYER), NON_SELECTABLE);
        }

        //show player capture piles
        ArrayList<ArrayList<String>> playerCapturePiles = gameModel.getPlayerCapturePiles();
        //show computer capture pile
        displayCards(findViewById(R.id.compCapture), playerCapturePiles.get(GameModel.COMPUTER_PLAYER), NON_SELECTABLE);
        //show human capture pile
        displayCards(findViewById(R.id.humCapture), playerCapturePiles.get(GameModel.HUMAN_PLAYER), NON_SELECTABLE);

        //show meld piles
        ArrayList<ArrayList<String>> playerMelds = gameModel.getPlayerMelds();
        //show computer melds
        displayCards(findViewById(R.id.compMeld), playerMelds.get(GameModel.COMPUTER_PLAYER), NON_SELECTABLE);
        //show human melds
        displayCards(findViewById(R.id.humMeld), playerMelds.get(GameModel.HUMAN_PLAYER), NON_SELECTABLE);

        //show stock pile
        ArrayList<String> stock = gameModel.getStock();
        //reverse the stock for display
        Collections.reverse(stock);
        displayCards(findViewById(R.id.stock), stock, NON_SELECTABLE);

        //display lead, chase, or meld cards
        //display meld if meld has been created
        ArrayList<String> cards;
        if ((cards = gameModel.getMeldCards()) != null) {
            //if the human won the round (and therefore is creating the meld and is playing first in the next turn)
            if (gameModel.isHumansTurn()) {
                displayCards(findViewById(R.id.cardsPlayedByHum), cards, NON_SELECTABLE);
            } else {
                displayCards(findViewById(R.id.cardsPlayedByComp), cards, NON_SELECTABLE);
            }
        } else {
            //display lead card
            if ((cards = gameModel.getLeadCard()) != null) {
                //if the human threw the lead card
                if (gameModel.isHumansLeadThrow()) {
                    displayCards(findViewById(R.id.cardsPlayedByHum), cards, NON_SELECTABLE);
                } else {
                    displayCards(findViewById(R.id.cardsPlayedByComp), cards, NON_SELECTABLE);
                }
            }
            //display chase card
            if ((cards = gameModel.getChaseCard()) != null) {
                //if the human threw the lead card
                if (gameModel.isHumansLeadThrow()) {
                    displayCards(findViewById(R.id.cardsPlayedByComp), cards, NON_SELECTABLE);
                } else {
                    displayCards(findViewById(R.id.cardsPlayedByHum), cards, NON_SELECTABLE);
                }
            }

        }

        //show or hide buttons

        if (currentStage.equals(gameModel.LEAD_CARD_STAGE) && gameModel.isHumansTurn()) {
            //show these buttons
            findViewById(R.id.playButton).setVisibility(View.VISIBLE);
            findViewById(R.id.hintButton).setVisibility(View.VISIBLE);
            findViewById(R.id.saveButton).setVisibility(View.VISIBLE);

            //hide these buttons
            findViewById(R.id.nextButton).setVisibility(View.GONE);
        } else if (currentStage.equals(gameModel.LEAD_CARD_STAGE) && !gameModel.isHumansTurn()) {
            //show these buttons
            findViewById(R.id.saveButton).setVisibility(View.VISIBLE);
            findViewById(R.id.nextButton).setVisibility(View.VISIBLE);

            //hide these buttons
            findViewById(R.id.playButton).setVisibility(View.GONE);
            findViewById(R.id.hintButton).setVisibility(View.GONE);
        } else if (currentStage.equals(gameModel.CHASE_CARD_STAGE) && gameModel.isHumansTurn()) {
            //show these buttons
            findViewById(R.id.playButton).setVisibility(View.VISIBLE);
            findViewById(R.id.hintButton).setVisibility(View.VISIBLE);

            //hide these buttons
            findViewById(R.id.nextButton).setVisibility(View.GONE);
            findViewById(R.id.saveButton).setVisibility(View.GONE);

        } else if (currentStage.equals(gameModel.MELD_STAGE) && gameModel.isHumansTurn()) {
            //show these buttons
            findViewById(R.id.playButton).setVisibility(View.VISIBLE);
            findViewById(R.id.hintButton).setVisibility(View.VISIBLE);

            //hide these buttons
            findViewById(R.id.nextButton).setVisibility(View.GONE);
            findViewById(R.id.saveButton).setVisibility(View.GONE);
        } else {
            //show these buttons
            findViewById(R.id.nextButton).setVisibility(View.VISIBLE);

            //hide these buttons
            findViewById(R.id.hintButton).setVisibility(View.GONE);
            findViewById(R.id.saveButton).setVisibility(View.GONE);
            findViewById(R.id.playButton).setVisibility(View.GONE);
        }

    }



    /**
     * Event handler method that displays the result of a coin toss
     * @param view the view that triggered the event that called this handler method
     */
    public void showCoinTossResult(View view) {
        //set to coin toss result layout
        setContentView(R.layout.coin_toss_result_layout);
        String call = "";
        String result = "";
        //if user clicked on the "heads" button
        if (view.getId() == R.id.headsButton) {
            call = "You predicted heads";
            result = gameModel.tossCoin("heads");
            //if user clicked on the "tails" button
        } else if (view.getId() == R.id.tailsButton) {
            call = "You predicted tails";
            result = gameModel.tossCoin("tails");
        }
        TextView textView = (TextView) findViewById(R.id.coinTossCall);
        textView.setText(call);

        textView = (TextView) findViewById(R.id.headsOrTails);
        result = result.substring(0, 1).toUpperCase() + result.substring(1);
        textView.setText(result);

        if (gameModel.isHumansTurn()) {
            result = "The human player goes first";
        } else {
            result = "The computer player goes first";
        }
        textView = (TextView) findViewById(R.id.whoWonToss);
        textView.setText(result);

    }

    /**
     * Event handler method that highlights or unhighlights cards when the human player selects or deselects these cards
     * @param v the view (i.e. the card) that generated the event that triggered this handler
     */
    public void handleCardSelect(View v) {

        //first, get the recycler view that contains the cards
        //we know that if a card is selectable, it is part of the human hand
        RecyclerView recyclerView = findViewById(R.id.humHand);
        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

        //if the card is single selectable
        if (v.getTag(R.string.card_selectable_type).equals(SINGLE_SELECTABLE)) {

            //if a card has already been selected before
            if (selectedThrowCard != -1) {
                ConstraintLayout oldSelection = (ConstraintLayout) layoutManager.findViewByPosition(selectedThrowCard);

                if (oldSelection != null) {
                    TextView previouslySelectedCard = (TextView) oldSelection.getViewById(R.id.card);
                    previouslySelectedCard.setBackgroundResource(R.drawable.card_border);
                }
            }
            //set the new selected throw card
            selectedThrowCard = (Integer) v.getTag(R.string.card_position);
            //add highlight
            v.setBackgroundResource(R.drawable.card_border_selected);

        } else if (v.getTag(R.string.card_selectable_type).equals(MULTI_SELECTABLE)) {
            //if the card is multi-selectable (for melds)
            //if the card is already selected
            if (selectedMeldCards.contains((int) v.getTag(R.string.card_position))) {
                //remove the card from the selected list
                selectedMeldCards.remove((Integer) v.getTag(R.string.card_position));
                //remove highlight
                v.setBackgroundResource(R.drawable.card_border);
            } else {
                //if the card has not yet been selected
                //add to list of selected cards
                selectedMeldCards.add((Integer) v.getTag(R.string.card_position));
                //add highlight
                v.setBackgroundResource(R.drawable.card_border_selected);
            }
        }
    }

    /**
     * The handler for the Play button. It updates the game state model to reflect that the user has made a move
     * @param view the view that generated the event (i.e. the Play button)
     */
    public void playButtonHandler(View view) {
        if (gameModel.getCurrentStage().equals(GameModel.LEAD_CARD_STAGE)) {
            gameModel.humanThrowsLeadCard(selectedThrowCard);
        } else if (gameModel.getCurrentStage().equals(GameModel.CHASE_CARD_STAGE)) {
            gameModel.humanThrowsChaseCard(selectedThrowCard);
        } else if (gameModel.getCurrentStage().equals(GameModel.MELD_STAGE)) {
            gameModel.humanPlaysMeld(selectedMeldCards);
        }
        selectedThrowCard = -1;
        selectedMeldCards.clear();
        showGameBoard(view);
    }

    /**
     * The handler for the Begin Round button. It updates the game model state to reflect that a new round has started and asks updates the view
     * @param view the view (i.e. the Begin Round button) that triggered this handler
     */
    public void beginRoundHandler(View view) {
        gameModel.setUpNewRound();
        showGameBoard(view);
    }

    /**
     * The handler for the Next button. It asks the game model to update itself into the next state based on its current state
     * @param view the view (i.e. the Next button) that triggered this handler
     */
    public void nextButtonHandler(View view) {
        gameModel.goToNextStep();
        showGameBoard(view);
    }

    /**
     * The handler for the Save button. It sets the view to the Save Game view
     * @param view the view (i.e. the Save button) that triggered this handler
     */
    public void saveButtonHandler(View view) {
        setContentView(R.layout.save_game_layout);
    }

    /**
     * The handler for the Save To File button. It saves the current state of the game to a file
     * @param view the view (i.e. the Save to File button) that triggered this handler
     */
    public void saveGame(View view) {
        EditText text = (EditText) findViewById(R.id.fileName);
        String fileName = text.getText().toString();
        fileName = fileName.replaceAll("\\s+", "");
        if (fileName.equals("")) {
            return;
        }
        String saveGameData = gameModel.generateSaveGameData();
        FileOutputStream fos = null;

        try {
            fos = openFileOutput(fileName, MODE_PRIVATE);
            fos.write(saveGameData.getBytes());
            File directory = getFilesDir();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


        //redirect to home screen
        Intent intent = new Intent(this, WelcomeActivity.class);
        startActivity(intent);
    }

    /**
     * The handler for the Load Game button. It loads the state of a game from a file and updates the game model accordingly
     * @param view the view (i.e. the Load Game button) that triggered this handler
     */
    public void loadGame(View view) {
        EditText text = (EditText) findViewById(R.id.loadFileName);
        String fileName = text.getText().toString();
        fileName = fileName.replaceAll("\\s+", "");
        if (fileName.equals("")) {
            return;
        }

        StringBuilder sb = null;

        FileInputStream fis = null;
        try {
            fis = openFileInput(fileName);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            sb = new StringBuilder();
            String str;

            while ((str = br.readLine()) != null) {
                //skip empty lines
                if (!str.equals("\n")) {
                    sb.append(str).append("\n");
                }

            }

        } catch (FileNotFoundException e) {
            findViewById(R.id.invalidSaveFile).setVisibility(View.VISIBLE);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        if (sb != null) {
            gameModel.loadGame(sb.toString());

            showGameBoard(view);
            ;
        }

    }


    /**
     * The handler for the Hint button. It generates the hint to be given to the player and updates the view to display it
     * @param view the view (i.e. the Hint button) that triggered this handler
     */
    public void hintButtonHandler(View view) {
        gameModel.generateHint();
        showGameBoard(view);
    }

    /**
     * The handler for the Quit button. It loads the Confirm Quit view
     * @param view the view (i.e. the Quit button) that triggered this handler
     */
    public void quitButtonHandler(View view) {
        setContentView(R.layout.confirm_quit);
    }

    /**
     * A handler method that shows the view that displays the winner of the round that just finished
     * @param view the view that triggered this handler
     */
    public void showWinnerDeclaration(View view) {
        setContentView(R.layout.winner_declaration);
        int[] scores = gameModel.getGameScores();
        TextView textView = findViewById(R.id.finalCompScore);
        textView.setText(Integer.toString(scores[GameModel.COMPUTER_PLAYER]));

        textView = findViewById(R.id.finalHumScore);
        textView.setText(Integer.toString(scores[GameModel.HUMAN_PLAYER]));

        textView = findViewById(R.id.finalWinner);
        String winner;
        if (gameModel.gameLeader() == GameModel.HUMAN_PLAYER) {
            winner = "The Human player won the game!";
        } else if (gameModel.gameLeader() == GameModel.COMPUTER_PLAYER) {
            winner = "The Computer player won the game!";
        } else {
            winner = "The game resulted in a draw";
        }
        textView.setText(winner);

    }



    /**
     * Shows the round declaration view
     */
    public void showRoundDeclarationView() {
        //load the layout to declare the round number and current scores
        setContentView(R.layout.declare_round_layout);

        TextView roundDeclaration = (TextView) findViewById(R.id.roundDeclaration);

        //set the beginning round declaration label
        String str = "Beginning Round " + gameModel.getRoundNumber();
        roundDeclaration.setText(str);
        int[] gameScores = gameModel.getGameScores();

        //set the score display for Computer and Human player
        TextView score = (TextView) findViewById(R.id.compScoreDeclaration);
        score.setText(Integer.toString(gameScores[GameModel.COMPUTER_PLAYER]));
        score = (TextView) findViewById(R.id.humScoreDeclaration);
        score.setText(Integer.toString(gameScores[GameModel.HUMAN_PLAYER]));
    }



    /**
     * Displays a side scroll view containing the displays of all the cards specified
     * @param cardView the RecyclerView object (that represents the side-scroll view container) to hold the card displays
     * @param cards the cards whose display are to be generated
     * @param cardSelectionType the selection type of the cards (single-selectable, multi-selectable, or non-selectable)
     */
    private void displayCards(RecyclerView cardView, ArrayList<String> cards, String cardSelectionType) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        cardView.setLayoutManager(layoutManager);
        CardViewAdapter adapter = new CardViewAdapter(this, cards, cardSelectionType);
        cardView.setAdapter(adapter);

        if (!cardSelectionType.equals(NON_SELECTABLE)) {
            cardView.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
                @Override
                public void onChildViewAttachedToWindow(@NonNull View view) {
                    TextView card = view.findViewById(R.id.card);
                    if (card.getTag(R.string.card_selectable_type).equals(SINGLE_SELECTABLE) && card.getTag(R.string.card_position).equals(selectedThrowCard)) {
                        card.setBackgroundResource(R.drawable.card_border_selected);
                    } else if (card.getTag(R.string.card_selectable_type).equals(MULTI_SELECTABLE) && selectedMeldCards.contains((int) card.getTag(R.string.card_position))) {
                        card.setBackgroundResource(R.drawable.card_border_selected);
                    }
                }

                @Override
                public void onChildViewDetachedFromWindow(@NonNull View view) {
                    TextView cardView = view.findViewById(R.id.card);
                    cardView.setBackgroundResource(R.drawable.card_border);
                }
            });
        }
    }

}