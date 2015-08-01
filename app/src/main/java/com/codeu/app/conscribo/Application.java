package com.codeu.app.conscribo;

import com.codeu.app.conscribo.data.StoryObject;
import com.codeu.app.conscribo.data.StoryTree;
import com.parse.Parse;
import com.parse.ParseObject;

// This class initializes the Parse settings and contains Conscribo global members and utilities
public class Application extends android.app.Application {

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

    public class Constants {
        // Used to check Parcel Object id
        final static public String STORY_OBJECT_PARCEL = "StoryObject";
        final static public String STORY_TREE_PARCEL = "StoryTree";

        // Used for Create Story Activity to index match the genre_options string array
        final static public int SCIFI =   0;
        final static public int FAIRY =   1;
        final static public int HORROR =  2;
        final static public int MYSTERY = 3;
        final static public int FANTASY = 4;
        final static public int ROMANCE = 5;
        final static public int SATIRE =  6;
        final static public int WESTERN = 7;
        final static public int OTHER =   8;

        // Constant that determines the max number of posts to be queried in Main Dashboard
        final static public int MAIN_DASHBOARDS_MAX_POSTS = 20;
    }
}

