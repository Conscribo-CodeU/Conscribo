package com.codeu.app.conscribo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.parse.ParseUser;

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

        username.setText(user.getUsername());
        numLikes.setText(user.get("likes") + " Likes");
        numFavorites.setText(user.get("favorites") + " Favorites");
        numSubscribers.setText(user.get("subscribers") + " Subscribers");

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
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
