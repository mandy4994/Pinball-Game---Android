package com.ass1.mandeep.singh_mandeep_213347007;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class GameActivity extends Activity {
    // width of the game table
    private int tableWidth;
    // height of the game table
    private int tableHeight;
    // location of the racket in Y axis
    private int racketY;
    // height and width of the racket
    private int RACKET_HEIGHT;
    private int RACKET_WIDTH;
    // size of the ball
    private int BALL_SIZE = 12;
    // Moving speed of the ball in y axis
    private int ySpeed = 20;
    // A random number
    Random rand = new Random();
    // Return a value with [-0.5,0.5], which is used to control the moving direction of the ball
    // rand.nextDouble() generate a number between 0 and 1
    private double xyRate = rand.nextDouble() - 0.5;
    // The speed that the ball moves in the x axis
    private int xSpeed;
    // Location of the ball in the x, y axis
    // rand.nextInt(n) returns a pseudo-random uniformly distributed int in the half-open range [0, n).
    private int ballX = rand.nextInt(200) + 20;
    private int ballY = rand.nextInt(10) + 20;
    // location of the racket in the x axis
    private int racketX = rand.nextInt(200);
    // if the game is over or not
    private boolean isLose = false;

    // Define a variable to track the game time
    private long initialTime;
    int barWidth, ballSpeed;
    Bitmap ballBitmap;
    Bitmap batBitmap;
    boolean isDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Remove the window title
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        // Make the game full screen
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // Create a new object from the GameView class
        // The GameView class is defined later as an inner class. It defines the method to draw the ball and racket.
        final GameView gameView = new GameView(this);
        // Apply the gameView object for the activity
        setContentView(gameView);

        gameView.setBackgroundResource(R.drawable.bg);
        // Access the window manager to retrieve the dimensions window.
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        // Set the width and height of the game table to the screen size
        tableWidth = metrics.widthPixels;
        tableHeight = metrics.heightPixels;
        racketY = (int) (tableHeight*0.85);
        Intent intent = getIntent();
        barWidth = intent.getIntExtra(getString(R.string.bar_width_offset), 10);
        ballSpeed = intent.getIntExtra(getString(R.string.ball_speed_offset), 500);
        ySpeed += ballSpeed;
        ballBitmap = BitmapFactory.decodeResource(getResources(),
                R.drawable.ballpng);
        batBitmap = BitmapFactory.decodeResource(getResources(),
                R.drawable.bat);

        BALL_SIZE = ballBitmap.getHeight();
        RACKET_HEIGHT =batBitmap.getHeight();
        RACKET_WIDTH = batBitmap.getWidth() + barWidth;
        racketY = tableHeight - (5*RACKET_HEIGHT);
        xSpeed = (int) (ySpeed * xyRate * 2);
        // Read the time when the game starts
        initialTime = System.currentTimeMillis();

        // Launch a new thread for game
        final Handler handler = new Handler()
        {
            public void handleMessage(Message msg)
            {
                if (msg.what == 0x123)
                {
                    // call invalidate() method of the view class to draw the view object
                    gameView.invalidate();
                }
            }
        };

        final Timer timer = new Timer();
        timer.schedule(new TimerTask()
        {

            @Override
            public void run()
            {
                // If the ball hits the table on the left, change the direction of the ball
                if (ballX <= 0 || ballX >= tableWidth - BALL_SIZE)
                {
                    xSpeed = -xSpeed;
                }
                // If ball hits the bottom and is outside the racket, then stop the game
                if (ballY >= racketY - BALL_SIZE
                        && (ballX < racketX || ballX > racketX
                        + RACKET_WIDTH))
                {
                    timer.cancel();
                    // Set the tag to be true which indicates that the game is over
                    isLose = true;
                }
                // If the ball hits the racket, then change the direction of the ball
                else if (ballY <= 0
                        || (ballY >= racketY - BALL_SIZE
                        && ballX > racketX && ballX <= racketX
                        + RACKET_WIDTH))
                {
                    ySpeed = -ySpeed;
                }
                // update the location of the ball
                ballY += ySpeed;
                ballX += xSpeed;
                // Send the message to handler to draw the gameView object
                handler.sendEmptyMessage(0x123);
            }
        }, 0, 100);
    }



    class GameView extends View
    {
        Paint paint = new Paint();

        // Constructor
        public GameView(Context context)
        {
            super(context);

            setFocusable(true);

        }


/*        @Override
        public void draw(Canvas canvas) {
            Bitmap bg = BitmapFactory.decodeResource(this.getResources(), R.drawable.bg);
            canvas.drawBitmap(bg, 0, 0, null);
        }*/

        // Override the onDraw method of the View class to configure
        public void onDraw(Canvas canvas)
        {
            /*Bitmap bg = BitmapFactory.decodeResource(this.getResources(), R.drawable.bg);
            Rect newrect = new Rect(0, 0, canvas.getWidth(), canvas.getHeight());*/

            /*canvas.drawBitmap(bg, null, newrect, paint);*/
            // Configure the paint which will be used to draw the view
            paint.setStyle(Paint.Style.FILL);
            paint.setAntiAlias(true);
            // If the game is over
            if (isLose)
            {
                /*paint.setColor(Color.RED);
                paint.setTextSize(20);*/

                if(!isDialog) {
                    // Get the time spent in the game
                    long timeSpent = System.currentTimeMillis() - initialTime;
                    timeSpent = (long) (timeSpent / 1000.0);

                    generateScoreDialog(timeSpent);
                /*String string = "Game Over! Time spent " + String.valueOf(timeSpent);
                canvas.drawText(string, 50, 200, paint);*/
                }
            }
            // Otherwise
            else
            {
                // set the color of the ball
                /*paint.setColor(Color.rgb(240, 240, 80));
                canvas.drawCircle(ballX, ballY, BALL_SIZE, paint);*/
                Bitmap ball = BitmapFactory.decodeResource(this.getResources(), R.drawable.ballpng);
                canvas.drawBitmap(ball, ballX, ballY, paint);
                // set the color of the racket
                /*paint.setColor(Color.rgb(80, 80, 200));
                canvas.drawRect(racketX, racketY, racketX + RACKET_WIDTH,
                        racketY + RACKET_HEIGHT, paint);*/
                /*Rect rect = new Rect(racketX, racketY, racketX + RACKET_WIDTH,
                        racketY + RACKET_HEIGHT) ;
                Bitmap bat = BitmapFactory.decodeResource(this.getResources(), R.drawable.bat);
                canvas.drawBitmap(bat, null, rect, paint );*/
                //paint.setColor(Color.rgb(80, 80, 200));
                /*canvas.drawRect(racketX, racketY, racketX + RACKET_WIDTH,
                        racketY + RACKET_HEIGHT, null);*/
                canvas.drawBitmap(batBitmap,racketX, racketY,null);

            }
        }


        // Override the onTouchEvent method in the View class
        @Override
        public boolean onTouchEvent(MotionEvent event) {
            super.onTouchEvent(event);
            // Get the location of user's figure
            float x = event.getX();
            // Determine if the user's figure is on racket or not
            if (x > racketX && x < racketX + RACKET_WIDTH) {
                // if yes, set the location of the racket to user's figure location so that the user can drag the racket
                racketX = (int) x - RACKET_WIDTH / 2;
                // draw the game view object
                invalidate();
            }
            return true;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_game, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.reset) {
            finish();
            Intent gameIntent = new Intent(GameActivity.this, GameActivity.class);
            gameIntent.putExtra(getString(R.string.bar_width_offset), (barWidth));
            gameIntent.putExtra(getString(R.string.ball_speed_offset), (ballSpeed));
            gameIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(gameIntent);
            return true;
        }
        if (id == R.id.exit) {
            //generate a exit dialog
            finish();
            return true;
        }
        if (id == R.id.level) {
            //generate a difficulty dialog
            generateLevelListDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    // Create a method to generate the difficulty level dialog
    void generateLevelListDialog() {
        // Instantiate an AlertDialog.Builder with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(GameActivity.this);

        // Specify the list in the dialog using the array
        builder.setTitle("Difficulty").setItems(R.array.levels_array,
                new DialogInterface.OnClickListener() {
                    // Chain together various setter methods to set the list
                    // items
                    // The index of the item selected is passed by the parameter
                    // which
                    public void onClick(DialogInterface dialog, int which) {
                        //change ball speed and racket length
                        Intent gameIntent = new Intent(GameActivity.this, GameActivity.class);
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
                        finish();
                        startActivity(gameIntent);

                    }
                });
        //create and show list dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    // Dialog box when game is over, displays time
    void generateScoreDialog(final long score) {
        // Instantiate an AlertDialog.Builder with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(GameActivity.this);

        // Specify the list in the dialog using the array
        builder.setTitle("Game Over!").setMessage("Total Time : "+ String.valueOf(score)+ " sec")
        .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                generatePlayerNameDialog(score);
               /* Intent intent = new Intent(getApplicationContext(), ScoreList.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("EXIT", false);
                intent.putExtra("score", score);
                startActivity(intent); */
            }
        })
                .setNegativeButton("Reset Game", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        Intent gameIntent = new Intent(GameActivity.this, GameActivity.class);
                        gameIntent.putExtra(getString(R.string.bar_width_offset), (barWidth));
                        gameIntent.putExtra(getString(R.string.ball_speed_offset), (ballSpeed));
                        gameIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(gameIntent);
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert);
        //create and show list dialog
        AlertDialog dialog = builder.create();
        dialog.show();
        isDialog = true;
    }

    private void generatePlayerNameDialog(final long score) {
        AlertDialog.Builder builder = new AlertDialog.Builder(GameActivity.this);
        // Get the layout inflater
        //LayoutInflater inflater = (GameActivity.this).getLayoutInflater();
        final EditText inputName = new EditText(this);
        inputName.setHint("Name");

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setTitle("Enter Player's Name").setView(inputName)
                // Add action buttons
                .setPositiveButton("Show Score List", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(getApplicationContext(), ScoreList.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("EXIT", false);
                        intent.putExtra("score", score);
                        intent.putExtra("player", inputName.getText().toString());
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
