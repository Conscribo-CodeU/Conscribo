package com.codeu.app.conscribo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.codeu.app.conscribo.data.BranchNode;
import com.codeu.app.conscribo.data.StoryObject;
import com.codeu.app.conscribo.data.StoryTree;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TreeBranchActivity extends AppCompatActivity {

    private final String LOGTAG = TreeBranchActivity.class.getSimpleName();

    private static StoryTree mQueryStoryTree;

    private StoryObject mSelectedBranch;
    private BranchNode mRootBranchNode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tree_branch);

        // Receive Tree id from Intent
        Intent i = getIntent();
        Bundle b = i.getExtras();

        String treeId = null;

        if (b != null && b.containsKey("selectedTreeId")) {
//            Log.v(LOGTAG, "bundle is not empty");
            treeId = b.getString("selectedTreeId");

        }

        if (treeId == null) {
            // Error in finding the StoryID in the Intent
            Log.e(LOGTAG, "Error finding selectedTreeId from intent");
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
            this.finish();
        } else {

            //Query StoryTree based on treeId
            ParseQuery<StoryTree> queryTree = StoryTree.getQuery();
            queryTree.whereEqualTo("objectId", treeId);
            queryTree.getFirstInBackground(new GetCallback<StoryTree>() {
                @Override
                public void done(StoryTree storyTree, ParseException e) {

                    if(e == null) {
                        // Now proceed to retrieve StoryObjects with queryStories(storyTree)
                        createBranchesParseQueryAdapter(storyTree);
                    } else {
                        e.printStackTrace();
                        Log.e(LOGTAG, "Couldn't retrieve StoryTree with treeId");
                    }
                }
            });
        }
    }

    /*
    *  Create ParseQueryAdapter to ListView?
     */
    private void createBranchesParseQueryAdapter(StoryTree tree) {
        // Check to see if tree is null. If it is then the tree query failed.
        if(tree == null) {

            Log.e(LOGTAG, "tree was null in " +
                    "createBranchesParseQueryAdapter(tree). Finish activity.");
            Toast.makeText(this, "Story tree could not be found!", Toast.LENGTH_SHORT).show();
            finish();

        } else {
            mQueryStoryTree = tree;
        }

        ParseQuery<StoryObject> query = StoryObject.getQuery();
        query.whereEqualTo("tree", mQueryStoryTree);
        query.orderByAscending("depth");

        query.findInBackground(new FindCallback<StoryObject>() {
            @Override
            public void done(List<StoryObject> list, ParseException e) {
                // Once we have the List of StoryObjects we want to order them
                if (e == null) {

                    orderBranches(new ArrayList<StoryObject>(list));

                } else {
                    Log.e(LOGTAG, "Error: " + e.getMessage());
                }
            }
        });

        ListView branchesListView = (ListView) findViewById(R.id.branches_list);
        branchesListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);





        /* NOTE: ParseQueryAdapter will not work for our purposes
        ParseQueryAdapter.QueryFactory<StoryObject> factoryBranches =
                new ParseQueryAdapter.QueryFactory<StoryObject>() {
                    public ParseQuery<StoryObject> create() {


                        return query;
                    }
                };

        mBranchesParseQueryAdapter = new ParseQueryAdapter<StoryObject>(this, factoryBranches){
            @Override
            public View getItemView(StoryObject story, View view, ViewGroup parent) {
                if (view == null) {
                    view = View.inflate(getContext(), R.layout.main_story_list_item, null);
                }

                // Find all relevant views
                TextView textView =

                // Set the content based on the story


                return view;
            }
        }; */

    }


    public void orderBranches(List<StoryObject> branchesList) {

        if(branchesList.size() <= 0) {
            Log.e(LOGTAG, "orderBranches() given an empty branchesList");
            finish();
        }

        // hashmap for parentBranch lookup. Key is depth, author and sentence String is this format
        //     depthauthorsentence
        HashMap<String, BranchNode> parentBranchNodeLookup = new HashMap<String, BranchNode >();
        mRootBranchNode = new BranchNode(branchesList.get(0) );
        parentBranchNodeLookup.put(mRootBranchNode.generateKey(), mRootBranchNode);

        // This should order all the appropriate BranchNode's after their respective parent.
        for(int i = 1; i < branchesList.size(); i++) {
            BranchNode current = new BranchNode(branchesList.get(i));
            String parentKey = current.generateParentKey();

            // Look up parent BranchNode based on parentKey
            if(parentBranchNodeLookup.containsKey(parentKey)) {
                BranchNode parent = parentBranchNodeLookup.get(parentKey);
                BranchNode child = parent.getNext();
                // Insert current node between parent and child
                current.setNext(child);
                parent.setNext(current);
            } else {
                Log.e(LOGTAG, "Error: could find parentKey in parentBranchNodeLookup");
                finish();
            }
        }

        // Now we create an arrayAdapter, define its getView()
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tree_branch, menu);
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
}
