package com.codeu.app.conscribo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codeu.app.conscribo.data.StoryObject;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONException;

import java.util.List;


/**
 *
 */
public class ReadWriteStoryFragment extends Fragment {

    private String mStoryId;
    private StoryObject mStoryObject;
    private static String mStoryText;

    private final String LOGTAG = ReadWriteStoryFragment.class.getSimpleName();

    private final String CONSCRIBO_SHARE_HASHTAG = "#ConscriboApp";

    private boolean hasUser;

    public ReadWriteStoryFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.read_write_menu, menu);

        // Retrieve the share menu item
        MenuItem menuItem = menu.findItem(R.id.action_share);

        // Get the provider and hold onto it to set/change the share intent.
        ShareActionProvider mShareActionProvider =
                (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);

        // Attach an intent to this ShareActionProvider.  You can update this at any time,
        // like when the user selects a new piece of data they might like to share.
        if (mShareActionProvider != null ) {
            mShareActionProvider.setShareIntent(createShareStoryIntent());
        } else {
            Log.d(LOGTAG, "Share Action Provider is null?");
        }
    }

    private Intent createShareStoryIntent(){
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT,
                mStoryText + CONSCRIBO_SHARE_HASHTAG);

        return shareIntent;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        hasUser = Utility.userLoggedIn();

        View rootView = inflater.inflate(R.layout.fragment_read_write_story, container, false);

        // Retrieve intent and check if there is a Story ID
        Intent i = getActivity().getIntent();
        Bundle b = i.getExtras();

        if(b != null && b.containsKey("selectedStoryId")) {
//            Log.v(LOGTAG, "bundle is not empty");
            mStoryId = b.getString("selectedStoryId");
        }

        if( mStoryId == null) {
            // Error in finding the StoryID in the Intent
            Log.e(LOGTAG, "Error finding selectedStoryId from intent");
            Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
            getActivity().finish();
        } else {
            //Query the StoryObject based on id and then set textViews and image.
            ParseQuery query = StoryObject.getQuery();
            query.whereEqualTo("objectId", mStoryId);
            query.getFirstInBackground(new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject parseObject, ParseException e) {
                    if (mStoryObject == null && e == null) {

                        // Set all textViews based on StoryObject
                        setStoryViews((StoryObject) parseObject);
                    } else {
                        Log.e(LOGTAG, "Query for objectId " + mStoryId + " failed");
                        e.printStackTrace();
                        getActivity().finish();

                    }
                }
            });

        }

        createOnClickListeners(rootView);

        return rootView;
    }

    /*
     *  Set all the onClickListeners for the buttons and views
     */
    private void createOnClickListeners(View rootView) {
        ImageButton infoButton = (ImageButton)      rootView.findViewById(R.id.rw_info_button);
        ImageButton likeButton = (ImageButton)      rootView.findViewById(R.id.rw_like_button);
        ImageButton subcribeButton = (ImageButton)  rootView.findViewById(R.id.rw_suscribe_button);
        ImageButton treeButton = (ImageButton)      rootView.findViewById(R.id.rw_tree_button);
        ImageButton writeButton = (ImageButton)     rootView.findViewById(R.id.rw_write_button);
        Button submitButton = (Button)              rootView.findViewById(R.id.rw_sentence_submit);

        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Send story info through the Intent to the StoryInfoActivity
                Intent i = new Intent();
            }
        });

        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        subcribeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        treeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create intent to start TreeBranchActivity
                // Provide treeId in the intent.
                String treeId = mStoryObject.getTree().getObjectId();

                Intent i = new Intent(getActivity(), TreeBranchActivity.class);
                i.putExtra("treeId", treeId);
                startActivity(i);
            }
        });

        writeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        submitButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                // Check if the StoryObject has been properly Queried
                if(!hasUser) {
                    Toast.makeText(getActivity().getApplicationContext(),
                            "Please login before branching off of a story",
                            Toast.LENGTH_SHORT).show();
                }
                else if(mStoryObject == null){
                    Log.e(LOGTAG," mStoryObject is null! Couldn't create contribution");
                } else {
                    // Submit the sentence for a new StoryObject
                    Activity context = getActivity();

                    EditText sentenceInput = (EditText) context
                            .findViewById(R.id.rw_setence_input);
                    EditText authorInput = (EditText) context
                            .findViewById(R.id.rw_contribution_author_input);

                    String sentence = sentenceInput.getText().toString();
                    String author = authorInput.getText().toString();

                    if(author.length() < 4){
                        Toast.makeText(context, "Author's name must be at least 4 characters",
                                Toast.LENGTH_SHORT).show();

                    } else if( !Utility.hasSentenceEnd(sentence)) {
                        Toast.makeText(context, "Please finish your sentence",
                                Toast.LENGTH_SHORT).show();
                    } else {

                        // Lists that contain the sentences and authors of the contribution
                        List<String> sentenceList;
                        List<String> authorList;

                        /*
                        *  Properly transferring all authors and sentences to sentenceList and authorList.
                        */
                        try {
                            sentenceList = Utility.
                                    convertJSONArrayToStringArrayList(mStoryObject.getSentencesJSONArray() );
                            authorList = Utility.
                                    convertJSONArrayToStringArrayList( mStoryObject.getAuthorsJSONArray() );
                            // Add space between previous period and added sentence
                            sentenceList.add(" " + sentence);
                            authorList.add( author );

                            // Create the contributor's StoryObject
                            StoryObject newContributionStory = new StoryObject();
                            newContributionStory.makeStoryObject(mStoryObject.getTitle(),
                                    mStoryObject.getGenre(),
                                    authorList,
                                    sentenceList,
                                    mStoryObject.getDepth() + 1,
                                    ParseUser.getCurrentUser());
                            newContributionStory.setTree(mStoryObject.getTree());

                            newContributionStory.saveInBackground();

                            // Confirm the creation of StoryObject and return to MainDashboard.
                            Toast.makeText(context,
                                    "Your contribution has been created",
                                    Toast.LENGTH_SHORT)
                                    .show();
                            context.finish();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e(LOGTAG, "Failed to convert JSONArray to List<String> " +
                                    "for contribution");
                            context.finish();
                        }
                    }
                }
            }
        });
    }

    private void setStoryViews( StoryObject story) {

        mStoryObject = story;

        TextView titleText = (TextView) getActivity().findViewById(R.id.rw_title_text);
        TextView authorText = (TextView) getActivity().findViewById(R.id.rw_author_text);
        ImageView storyImage = (ImageView) getActivity().findViewById(R.id.rw_story_image);
        TextView sentencesText = (TextView) getActivity().findViewById(R.id.rw_sentences_text);

        // Save the story so it can be shared

        mStoryText = Utility.generateStringFromJSONArray(story.getSentencesJSONArray());

        Log.e(LOGTAG, "Set mStoryText to: " + mStoryText);

        titleText.setText(story.getTitle());
        authorText.setText(Utility.getLastStringFromJSONArray(story.getAuthorsJSONArray()) );
        storyImage.setImageResource(Utility.findGenreDrawable( story.getGenre()));
        sentencesText.setText(mStoryText);
    }


}
