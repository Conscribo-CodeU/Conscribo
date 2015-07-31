package com.codeu.app.conscribo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.codeu.app.conscribo.data.StoryObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;


public class MainDashboard extends ActionBarActivity {

    ListView mListView;

    ParseQueryAdapter<StoryObject> mParseQueryAdapter;

    Activity self;

    final private String LOGTAG = MainDashboard.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        /*
        // Query Test
        // Result: SUCCESS! I successfully Queried a list of StoryObjects for the new story feed
        ParseQuery<StoryObject> query = StoryObject.getQuery();
        query.setLimit(Application.MAIN_DASHBOARDS_MAX_POSTS);
        query.orderByDescending("createdAt");
        query.findInBackground(new FindCallback<StoryObject>() {
            @Override
            public void done(List<StoryObject> list, ParseException e) {
                for(StoryObject story : list) {
                    Log.v("TEST", story.getTitle());
                }
            }
        });
        */

        self = this;

        // Get reference to ListView and set ChoiceModeSingle
        mListView = (ListView) findViewById(R.id.listview_main);
        mListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        createNewStoriesQueryAdapter();

        // Set ParseQueryAdapter to ListView
        mListView.setAdapter(mParseQueryAdapter);

        // Set up click listen on list items to have certain ones be selected and highlighted
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView< ?> parent, View view, int position, long id) {

                final StoryObject item = mParseQueryAdapter.getItem(position);
            }
        });

        // Set up buttons
        Button tempStoryModeButton = (Button) findViewById(R.id.story_mode_button);
        tempStoryModeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(self, StoryMode.class));
            }
        });

        Button tempCreateStoryButton = (Button) findViewById(R.id.create_story_button);
        tempCreateStoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(self, CreateStoryActivity.class));
            }
        });


    }

    private void createNewStoriesQueryAdapter() {
        /*
        *   Generate QueryFactor for ParseQueryAdapter with constraints:
        *   - query <int> MAIN_DASHBOARD_MAXPOSTS number of posts
        *   - descending order of createdAt
        */
        ParseQueryAdapter.QueryFactory<StoryObject> factoryNewStories =
                new ParseQueryAdapter.QueryFactory<StoryObject>() {
                    public ParseQuery<StoryObject> create() {

                        ParseQuery<StoryObject> query = StoryObject.getQuery();
                        query.setLimit(Application.MAIN_DASHBOARDS_MAX_POSTS);
                        query.orderByDescending("createdAt");
                        return query;
                    }
                };



        /*
        *   Set up the ParseQueryAdapter with QueryFactory
        */
        mParseQueryAdapter = new ParseQueryAdapter<StoryObject>(this, factoryNewStories) {
            @Override
            public View getItemView(StoryObject story, View view, ViewGroup parent) {
                if (view == null) {
                    view = View.inflate(getContext(), R.layout.main_story_list_item, null);
                }


                //Find all relevant views
                TextView titleView = (TextView)     view.findViewById( R.id.list_story_title);
                TextView authorView = (TextView)    view.findViewById( R.id.list_story_author);
                TextView likesView = (TextView)     view.findViewById( R.id.list_story_likes);
                ImageView genreImage = (ImageView)  view.findViewById( R.id.list_story_image);
                TextView blurb = (TextView)         view.findViewById( R.id.list_story_blurb);

                //  Set the content based on the story
                titleView.setText(story.getTitle());
                authorView.setText(getLastAuthorFromJSONArray(story.getAuthorsJSONArray()));
                likesView.setText(Integer.toString(story.getLikes()) +  " likes" );
                genreImage.setImageResource( findGenreDrawable( story.getGenre() ) );
                blurb.setText( generateStringFromJSONArray( story.getSentencesJSONArray() ) );

                return view;
            }
        };

        mParseQueryAdapter.addOnQueryLoadListener(new ParseQueryAdapter.OnQueryLoadListener<StoryObject>() {
            @Override
            public void onLoading() {
            }

            @Override
            public void onLoaded(List<StoryObject> list, Exception e) {
            }
        });

        // Turn off autoloading and default pagination
        mParseQueryAdapter.setAutoload(false);
        mParseQueryAdapter.setPaginationEnabled(false);

    }

    private int findGenreDrawable(String genre) {

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

    private String getLastAuthorFromJSONArray(JSONArray array) {
        String author = "";
        try {
            author = array.getString( array.length() - 1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return author;
    }

    private String generateStringFromJSONArray(JSONArray array) {
        String blurb = "";
        for(int i = 0; i < array.length(); i++) {
            try {
                blurb += array.getString(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return blurb;
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

    @Override
    public void onResume() {
        super.onResume();
        mParseQueryAdapter.loadObjects();
    }


}
