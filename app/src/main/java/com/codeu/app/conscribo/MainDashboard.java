package com.codeu.app.conscribo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.ArrayList;


public class MainDashboard extends ActionBarActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final MainDashboard self = this;

        // Enable automaticUser so users don't need to login
        ParseUser.enableAutomaticUser();

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "z4QO1lQCmgjdEpwOEoR9oEXD7hS1O74IFjViof37",
                "rUGcNQi5Vu622hBn6Ft845H5Ghj6JObB3nj5z18v");

        // Mock user input for creating a new story
        String genre = "scifi";
        String sentence = "In a galaxy far far away, there was a hero.";
        String author = "Jeff";
        String title = "Galactic Wars";

        ArrayList<String> story = new ArrayList<String>();
        story.add(sentence);
        ArrayList<String> authors = new ArrayList<String>();
        authors.add(author);



        // Create a ParseObject Sentence
        ParseObject sentenceObject = new ParseObject("Sentence");
        sentenceObject.put("author", author);
        sentenceObject.put("level", 0);
        sentenceObject.put("sentence", sentence);

        sentenceObject.saveInBackground();


        // Create a ParseObject StoryTree and add columns
        ParseObject storyTree = new ParseObject("StoryTree");
        storyTree.put("genre", genre);
        storyTree.put("creator", author);
        storyTree.put("title", title);


        // Create the ParseObject StoryObject and add columns
        ParseObject storyObject = new ParseObject("StoryObject");
        storyObject.put("title", title);
        storyObject.addAll("listSentences", story);
        storyObject.addAll("listAuthors", authors);
        storyObject.put("genre", genre);
        storyObject.put("likes", 0);

        // Add relations to all the ParseObjects
        sentenceObject.put("story", storyObject);
        sentenceObject.put("tree", storyTree);

        storyTree.put("rootSentence", sentenceObject);

        storyObject.put("tree", storyTree);
        storyObject.put("lastSentence", sentenceObject);


        // This should save all relational ParseObjects like storyObject and storyTree

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
