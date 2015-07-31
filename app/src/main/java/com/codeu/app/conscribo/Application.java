package com.codeu.app.conscribo;

import com.codeu.app.conscribo.data.StoryObject;
import com.codeu.app.conscribo.data.StoryTree;
import com.parse.Parse;
import com.parse.ParseObject;

// This class initializes the Parse settings and contains Conscribo members and utilities
public class Application extends android.app.Application {

    final static public int SCIFI =   0;
    final static public int FAIRY =   1;
    final static public int HORROR =  2;
    final static public int MYSTERY = 3;
    final static public int FANTASY = 4;
    final static public int ROMANCE = 5;
    final static public int SATIRE =  6;
    final static public int WESTERN = 7;
    final static public int OTHER =   8;

    final static public int STORYTREE_CREATED = 1000;

    public Application() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(StoryObject.class);
        ParseObject.registerSubclass(StoryTree.class);
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "z4QO1lQCmgjdEpwOEoR9oEXD7hS1O74IFjViof37",
                "rUGcNQi5Vu622hBn6Ft845H5Ghj6JObB3nj5z18v");

    }

    /* Checks the input sentence for a period at the end and no other place */
    public static boolean hasSentenceEnd(String inputString) {
        // Check to see if last char is the end of a sentence . ! ? "

        if(inputString == null || inputString.length() <= 0) {
            return false;
        }

        char lastChar = inputString.charAt(inputString.length() - 1);
        if( lastChar == '.' || lastChar == '!' || lastChar == '?' || lastChar == '"' ) {
            return true;
        }

        return false;
    }

}
