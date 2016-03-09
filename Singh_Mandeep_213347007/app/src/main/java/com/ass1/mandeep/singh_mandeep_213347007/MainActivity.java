package com.ass1.mandeep.singh_mandeep_213347007;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

    Button startNewGame;
    Button aboutButton;
    Button scoreButton;
    Button exitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (getIntent().getBooleanExtra("EXIT", false)) {
            finish();
        }
        startNewGame = (Button) findViewById(R.id.buttonNewGame);
        aboutButton = (Button) findViewById(R.id.buttonAbout);
        scoreButton = (Button) findViewById(R.id.buttonScoreList);
        exitButton = (Button) findViewById(R.id.buttonExit);

        //On Click New Game Button
        startNewGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //show LevelListDialog
                generateLevelListDialog();
            }
        });

        //On Click About Button
        aboutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get to About Activity
                Intent aboutActivity = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(aboutActivity);
            }
        });

        //On Click Score List Button
        scoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get to Score List Activity
                Intent scoreActivity = new Intent(MainActivity.this, ScoreList.class);
                startActivity(scoreActivity);
            }
        });

        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //exit system
                finish();
                System.exit(0);
            }
        });

    }

    // Create a method to generate the difficulty level dialog
    void generateLevelListDialog() {
        // Instantiate an AlertDialog.Builder with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        // Specify the list in the dialog using the array
        builder.setTitle("Difficulty").setItems(R.array.levels_array,
                new DialogInterface.OnClickListener() {
                    // Chain together various setter methods to set the list
                    // items
                    // The index of the item selected is passed by the parameter
                    // which
                    public void onClick(DialogInterface dialog, int which) {
                        //switch to game activity
                        Intent gameIntent = new Intent(MainActivity.this, GameActivity.class);
                        //change ball speed and racket length
                        switch (which) {
                            case 0:
                                gameIntent.putExtra(getString(R.string.bar_width_offset), 0);
                                gameIntent.putExtra(getString(R.string.ball_speed_offset), 0);

                                break;

                            case 1:
                                gameIntent.putExtra(getString(R.string.bar_width_offset), -50);
                                gameIntent.putExtra(getString(R.string.ball_speed_offset), 20);

                                break;

                            case 2:
                                gameIntent.putExtra(getString(R.string.bar_width_offset), -200);
                                gameIntent.putExtra(getString(R.string.ball_speed_offset), 60);

                                break;

                            default:
                                break;
                        }
                        //start activity
                        startActivity(gameIntent);

                    }
                });
        //create and show list dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // Create a method to generate the about dialog
    void generateAboutAlertDialog() {
        // 1. Instantiate an AlertDialog.Builder with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(this,
                2);
        // 2. Chain together various setter methods to set the dialog
        // characteristics
        builder.setTitle("About This Game").setMessage(
                "In this assignment you will create a simple game");

        // 3. Get the AlertDialog from create()
        AlertDialog dialog = builder.create();

        // 4. Show dialog
        dialog.show();
    }
}