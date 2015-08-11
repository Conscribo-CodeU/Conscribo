package com.codeu.app.conscribo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.codeu.app.conscribo.data.StoryObject;
import com.codeu.app.conscribo.data.StoryTree;
import com.codeu.app.conscribo.data.UserData;
import com.parse.ParseUser;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {

    ParseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        user = ParseUser.getCurrentUser();

        TextView username = (TextView) findViewById(R.id.profile_username);
        TextView numLikes = (TextView) findViewById(R.id.profile_likes);
        TextView numFavorites = (TextView) findViewById(R.id.profile_favorites);
        TextView numSubscribers = (TextView) findViewById(R.id.profile_subscribers);

        ListView selectedList = (ListView) findViewById(R.id.profile_list);

        ArrayList<StoryObject> contributions = (ArrayList<StoryObject>) user.get("contributions");
        ArrayList<StoryTree> subscriptions = (ArrayList<StoryTree>) user.get("subscriptions");

        username.setText(user.getUsername());

        UserData userData = Utility.getUserData((String) user.get("userdata"));

        numLikes.setText(userData.getLikes() + " Likes");
        //numFavorites.setText(((ArrayList<StoryObject>) user.get("favorites")).size() + " Favorites");
        numSubscribers.setText(userData.getSubscribers().size() + " Subscribers");

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_logged_in_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_help) {
            startActivity(new Intent(this, HelpActivity.class));
            return true;
        }
        else if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        else if (id == R.id.action_profile) {
            startActivity(new Intent(this, ProfileActivity.class));
            return true;
        }
        else if (id == R.id.action_log_out) {
            ParseUser.logOut();
            Intent intent = new Intent(this, DispatchActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
