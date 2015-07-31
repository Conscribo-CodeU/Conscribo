package com.codeu.app.conscribo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainDashboardFragment extends Fragment {

    ListView mListView;

    public MainDashboardFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mListView = (ListView) getActivity().findViewById(R.id.listview_main);


        // Query Story Objects from Parse from descending order by dateUpdated


        // Use an adapter to hook up to the listItemView and listView


        return inflater.inflate(R.layout.fragment_main, container, false);
    }
}
