package com.codeu.app.conscribo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codeu.app.conscribo.data.StoryObject;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;


/**
 * A placeholder fragment containing a simple view.
 */
public class ReadWriteStoryFragment extends Fragment {

    private String mStoryId;

    private final String LOGTAG = ReadWriteStoryFragment.class.getName();

    public ReadWriteStoryFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_read_write_story, container, false);


        // Retrieve intent and check if there is a Story ID
        Intent i = getActivity().getIntent();
        Bundle b = i.getExtras();

        if(!b.isEmpty() ) {
            Log.v(LOGTAG, "bundle is not empty");
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
                    if (parseObject == null) {

                        Log.e(LOGTAG, "Query for objectId " + mStoryId + " failed");
                        e.printStackTrace();
                        getActivity().finish();
                    } else {
                        // Set all textViews based on StoryObject
                        setStoryViews((StoryObject) parseObject);
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
        ImageButton suscribeButton = (ImageButton)  rootView.findViewById(R.id.rw_suscribe_button);
        ImageButton treeButton = (ImageButton)      rootView.findViewById(R.id.rw_tree_button);
        ImageButton writeButton = (ImageButton)     rootView.findViewById(R.id.rw_write_button);

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

        suscribeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        treeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        writeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    private void setStoryViews( StoryObject story) {
        TextView titleText = (TextView) getActivity().findViewById(R.id.rw_title_text);
        TextView authorText = (TextView) getActivity().findViewById(R.id.rw_author_text);
        ImageView storyImage = (ImageView) getActivity().findViewById(R.id.rw_story_image);
        TextView sentencesText = (TextView) getActivity().findViewById(R.id.rw_sentences_text);

        titleText.setText(story.getTitle());
        authorText.setText(Utility.getLastAuthorFromJSONArray( story.getAuthorsJSONArray() ) );
        storyImage.setImageResource(Utility.findGenreDrawable( story.getGenre()));
        sentencesText.setText(Utility.generateStringFromJSONArray( story.getSentencesJSONArray()) );
    }
}
