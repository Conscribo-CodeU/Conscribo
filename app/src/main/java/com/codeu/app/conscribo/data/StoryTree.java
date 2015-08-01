package com.codeu.app.conscribo.data;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.Date;

/**
 * Created by jeff on 7/30/15.
 *
 * Members of StoryTree {
 *     "objectId":      String
 *     "title":         String
 *     "genre":         String
 *     "creator":       String
 *     <REMOVED> "priorityQueueStoryList": Relation< StoryObject> </REMOVED>
 *     "createdAt":     Date
 *     "updatedAt":     Date
 * }
 */
@ParseClassName("StoryTree")
public class StoryTree extends ParseObject {

    public void makeStoryTree(String title, String genre, String creator){
        setTitle(title);
        setGenre(genre);
        setCreator(creator);
    }


    // Setters
    public void setTitle(String value) {
        put("title", value);
    }
    public void setGenre(String value) {
        put("genre", value);
    }
    public void setCreator(String value) {
        put("creator", value);
    }

    // Getters
    public String getTitle() {
        return getString("title");
    }
    public String getGenre() {
        return getString("genre");
    }
    public String getCreator() {
        return getString("creator");
    }
    public Date getUpdatedTime() {
        return getUpdatedAt();
    }
    public Date getCreatedTime() {
        return getCreatedAt();
    }

    // Query
    public static ParseQuery<StoryTree> getQuery() {
        return ParseQuery.getQuery(StoryTree.class);
    }
}
