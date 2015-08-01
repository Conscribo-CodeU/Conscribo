package com.codeu.app.conscribo;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by jeff on 8/1/15.
 *
 * This contains all of the different functions utilized when creating a StoryObject or
 * getting the appropriate strings from a StoryObject.
 */
public class Utility {

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

    public static String getLastAuthorFromJSONArray(JSONArray array) {
        String author = "";
        try {
            author = array.getString( array.length() - 1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return author;
    }

    public static String generateStringFromJSONArray(JSONArray array) {
        String stringsFromArray = "";
        for(int i = 0; i < array.length(); i++) {
            try {
                stringsFromArray += array.getString(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return stringsFromArray;
    }

    public static int findGenreDrawable(String genre) {

        // Switchcase to match genre and return drawable id
        switch (genre) {
            case "Sci Fi":
                return R.drawable.scifi;
            case "Fairy Tale":
                return R.drawable.fairytale;
            case "Horror":
                return R.drawable.horror;
            case "Mystery":
                return R.drawable.mystery;
            case "Fantasy":
                return R.drawable.fantasy;
            case "Romance":
                return R.drawable.romance;
            case "Satire":
                return R.drawable.satire;
            case "Western":
                return R.drawable.western;
        }

        return R.drawable.other;
    }


    public static ArrayList<String> convertJSONArrayToStringArrayList(JSONArray array) throws JSONException {
        ArrayList<String> stringList = new ArrayList<String>();

        for(int i = 0; i < array.length(); i++) {
            stringList.add(array.getString(i));
        }

        return stringList;
    }
}