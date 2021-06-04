/************************************************************
 * Name:  Biplab Thapa Magar                                *
 * Project:  Pinochle in Java/Android                       *
 * Class:  OPL Fall 2020                                    *
 * Date:  11/18/2020                                        *
 ************************************************************/

package com.example.pinochleinandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.pinochleinandroid.models.GameModel;

public class WelcomeActivity extends AppCompatActivity {
    public static final String NEW_OR_LOAD = "com.example.pinochleinandroid.NEW_OR_LOAD";

    /**
     * The method called when an this Activity is first created
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

    }

    /**
     * A handler method that loads the activity that starts a game of pinochle
     * @param view the view (i.e. the New Game button or Load Game button) that triggered this handler
     */
    public void startGame(View view) {
        //redirect to coin toss activity
        Intent intent = new Intent(this, GameActivity.class);
        if (view.getId() == R.id.newGameButton) {
            intent.putExtra(NEW_OR_LOAD, "new_game");
        } else if (view.getId() == R.id.loadGameButton) {
            intent.putExtra(NEW_OR_LOAD, "load_game");
        }

        startActivity(intent);

    }

}