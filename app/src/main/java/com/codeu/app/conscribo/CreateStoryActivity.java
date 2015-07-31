package com.codeu.app.conscribo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.codeu.app.conscribo.data.StoryTree;


public class CreateStoryActivity extends ActionBarActivity implements AdapterView.OnItemSelectedListener {

    final private String LOGTAG = CreateStoryActivity.class.getName();

    final private int NULL_CREATOR = 0;
    final private int SHORT_CREATOR = 1;
    final private int NULL_TITLE = 2;
    final private int SHORT_TITLE = 3;
    final private int NULL_GENRE = 4;
    final private int NULL_SENTENCE = 5;
    final private int INVALID_SENTENCE = 6;

    private String mGenreSelected;

    private EditText mCreatorEditText;
    private EditText mTitleEditText;
    private EditText mSentenceEditText;
    private TextView mCharacterCountTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_story);

        // Find and set EditText members for user text input lookup
        mCreatorEditText = (EditText) findViewById(R.id.create_story_username);
        mTitleEditText = (EditText) findViewById(R.id.create_story_title);
        mSentenceEditText = (EditText) findViewById(R.id.create_story_sentence);
        mCharacterCountTextView = (TextView) findViewById(R.id.create_story_character_count);


        // Set Spinner options from genre_option_array with Adapter
        Spinner spinner = (Spinner) findViewById(R.id.genre_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.genre_options_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        // Create the submit button and click listener logic
        createSubmitButtonAndListener();

        // Text Change Listener created for SentenceEditText to update the character count
        mSentenceEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }
            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                updateCharacterCountTextViewText();
            }
        });

    }

    private void updateCharacterCountTextViewText() {
        String updatedCharacterCount = String.format("%d characters",
                mSentenceEditText.getText().toString().length());
        mCharacterCountTextView.setText(updatedCharacterCount);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_story, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void createSubmitButtonAndListener() {

        Button tempStoryMode = (Button) findViewById(R.id.create_story_submit);
        tempStoryMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Check if the submission is valid and then Create Parse Object and save
                if(isValidStorySubmission()) {
                    // Create StoryTree
                    StoryTree tree = new StoryTree();
                    tree.makeStoryTree(mTitleEditText.getText().toString(),
                            mGenreSelected,
                            mCreatorEditText.getText().toString());
                    /*
                    tree.saveInBackground(new SaveCallback() {

                        @Override
                        public void done(ParseException e) {

                            // Query the tree object after it has been saved
                            ParseQuery<ParseObject> query = ParseQuery.getQuery("StoryTree");
                            query.whereEqualTo("title", mTitleEditText.getText().toString());

                            query.getFirstInBackground(new GetCallback<ParseObject>() {
                                public void done(ParseObject object, ParseException e) {

                                    if (object == null) {
                                        Log.d("score", "The getFirst request failed.");
                                    } else {
                                        Log.d("score", "Retrieved the object.");

                                        // Create StoryObject
                                        StoryObject story = new StoryObject();
                                        story.makeStoryObject(mTitleEditText.getText().toString(),
                                                mGenreSelected,
                                                mSentenceEditText.getText().toString(),
                                                mCreatorEditText.getText().toString());
                                        story.setTree(object);

                                        ParseRelation<ParseObject> relation =
                                                object.getRelation("priorityQueueStoryList");
                                        relation.add(story);

                                        object.saveInBackground();

                                    }
                                }
                            });


                        }
                    }); */


//                    story.setTree(tree);


//                    ParseRelation<ParseObject> relation = tree.getRelation("priorityQueueStoryList");
//                    relation.add(story);

                    // First save story and tree in background.
                    /*story.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            finish();
                        }
                    });*/


                    /*
                    tree.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            finish();
                        }
                    });*/

                    // Go back to MainDashboard
                    startActivity(new Intent(getApplicationContext(), MainDashboard.class));
                }
            }
        });
    }

    public boolean isValidStorySubmission() {
        // Check if creator was filled out correctly
        String creatorString =  mCreatorEditText.getText().toString();
        if (creatorString.length() < 4) {
            displaySubmissionErrorToast(SHORT_CREATOR);
            return false;
        }

        // Check if title was filled out correctly
        String titleString =  mTitleEditText.getText().toString();
//                Log.e(LOGTAG, titleString);
        if (titleString.length() < 1) {
            displaySubmissionErrorToast(SHORT_TITLE);
            return false;
        }

        // Check if genre was selected
//                Log.e(LOGTAG, mGenreSelected);
        if(mGenreSelected == null) {
            displaySubmissionErrorToast(NULL_GENRE);
            return false;
        }

        // Check if the sentence is appropriate
        String sentenceInput = mSentenceEditText.getText().toString();
        if (!Application.hasSentenceEnd(sentenceInput)) {
            displaySubmissionErrorToast(INVALID_SENTENCE);
            return false;
        }

        return true;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view,
                               int position, long id) {

        mGenreSelected = getResources()
                .obtainTypedArray(R.array.genre_options_array).getString(position);

        switch(position){
            case Application.SCIFI:
                break;
            case Application.FAIRY:
                break;
            case Application.HORROR:
                break;
            case Application.MYSTERY:
                break;
            case Application.FANTASY:
                break;
            case Application.ROMANCE:
                break;
            case Application.SATIRE:
                break;
            case Application.WESTERN:
                break;
            case Application.OTHER:
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    // Show the Toast that contains the submission error
    public void displaySubmissionErrorToast(int errorCode) {

        String submissionError;
        switch(errorCode) {
            case SHORT_CREATOR:
                submissionError = "Creator's name must be more than 4 characters";
                break;
            case SHORT_TITLE:
                submissionError = "Title must be more than 0 characters";
                break;
            case NULL_GENRE:
                submissionError = "Please select a Genre";
                break;
            case INVALID_SENTENCE:
                submissionError = "Please finish your sentence";
                break;
            default:
                submissionError = "Submission Error";
        }

        Toast.makeText(getApplicationContext(), submissionError, Toast.LENGTH_SHORT).show();
    }
}
