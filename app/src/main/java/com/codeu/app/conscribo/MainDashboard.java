package com.codeu.app.conscribo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class MainDashboard extends ActionBarActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final MainDashboard self = this;

        /*
        ParseUser user = ParseUser.getCurrentUser();
        ParseRelation<ParseObject> relation = user.getRelation("likes");
        relation.add(post);
        user.saveInBackground();
        */

        // Mock user input for creating a new story
        String genre = "scifi";
        String sentence = "In a galaxy far far away, there was a hero.";
        String author = "Jeff";
        String title = "Galactic Wars" +((int) (Math.random() * 1000));

        /*
        // Create a ParseObject StoryTree and add columns
        ParseObject storyTree = new ParseObject("StoryTree");
        storyTree.put("genre", genre);
        storyTree.put("creator", author);
        storyTree.put("title", title);

        // Create the ParseObject StoryObject and add columns
        ParseObject storyObject = new ParseObject("StoryObject");
        storyObject.put("title", title);
        storyObject.add("listAuthors", author);
        storyObject.add("listSentences", sentence);
        storyObject.put("genre", genre);
        storyObject.put("depth", 0);
        storyObject.put("likes", 0);


        // Give pointer to storyObject to point to its tree
        storyObject.put("tree", storyTree);

        // Save both in background
//        storyObject.saveInBackground();



        */

        Button tempStoryMode = (Button) findViewById(R.id.story_mode_button);
        tempStoryMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(self, StoryMode.class));
            }
        });


        Button tempCreateStoryButton = (Button) findViewById(R.id.create_story_button);
        tempCreateStoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent (self, CreateStoryActivity.class));
            }
        });
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_login) {
            startActivity(new Intent(this, LoginActivity.class));
            return true;
        }
        else if (id == R.id.action_help) {
            startActivity(new Intent(this, HelpActivity.class));
            return true;
        }
        else if (id == R.id.action_refresh) {
            return true;
        }
        else if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



}
