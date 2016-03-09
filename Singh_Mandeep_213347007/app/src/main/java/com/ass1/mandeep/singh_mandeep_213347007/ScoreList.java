package com.ass1.mandeep.singh_mandeep_213347007;

import android.app.Activity;
import android.app.ListActivity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

public class ScoreList extends ListActivity {

    MyOpenHelper myOpenHelper;
    private final static String DATABASE_NAME = "SQLiteDatabasePractical.db";
    private final static int VERSION_NO = 1;
    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        // Constructor for SQLiteOpenHelper
        myOpenHelper = new MyOpenHelper(this, DATABASE_NAME, null, VERSION_NO);
        Intent intent = getIntent();
        /*Getting score and name value from intent*/
        long score = intent.getLongExtra("score", -1);
        String name = intent.getStringExtra("player");

        //Inserting into table only if there is some value in score from intent
        if(score != -1) {
            SQLiteDatabase db = myOpenHelper.getWritableDatabase();

            //Creating ContentValues object to  insert values in table
            ContentValues values = new ContentValues();
            values.put("name", name);
            int intScore = (int) score;
            values.put("score", intScore);

            //inserting
            db.insert(myOpenHelper.TABLE_NAME, null, values);


        }
        //Showing table
        displayDataInTable();
        /*Setting string to be displayed on ListView*/
        //String scoreRow = "No. 1 "+ name + " with "+ String.valueOf(score)+ " sec";
        //String[] values = new String[] { scoreRow };
        //ScoreArrayAdapter adapter = new ScoreArrayAdapter(this, values);
        //setListAdapter(adapter);
    }

    //Method for showing table content
    private void displayDataInTable() {
        List<String> values = queryTable();

        if(values != null)
        {
            ScoreArrayAdapter adapter = new ScoreArrayAdapter(this, values);
            setListAdapter(adapter);
        }
    }

    //Function which returns list of player names sorted by score in descending order
    private List<String> queryTable() {
        List<String> player = new ArrayList<String>();
        SQLiteDatabase db = myOpenHelper.getReadableDatabase();
        Cursor cursor = db.query(myOpenHelper.TABLE_NAME, null, null, null, null, null, "score DESC");

        //Looping till the end of table
        while(cursor.moveToNext())
        {
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            int score = cursor.getInt(cursor.getColumnIndex("score"));
            player.add(name + " - " + score + "s");
        }

        return player;
    }
    public void onBackPressed()
    {
        /*Back button will lead to the menu screen*/
        Intent intent = new Intent(ScoreList.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        this.startActivity(intent);

        return;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_score_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.scoreExit) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
