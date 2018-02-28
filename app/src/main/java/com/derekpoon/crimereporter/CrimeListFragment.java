package com.derekpoon.crimereporter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by derekpoon on 12/01/2018.
 */

public class CrimeListFragment extends Fragment {

    private RecyclerView mCrimeRecyclerView;
    private CrimeAdapter mAdapter;
    private boolean mSubtitleVisible;

    private static final String SAVED_SUBTITLE_VISIBLE = "subtitile";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //tell the FragmentManager that your fragment should receive
        //a call to onCreateOptionsMenu
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crime_list, container, false);

        mCrimeRecyclerView = (RecyclerView) view
                .findViewById(R.id.crime_recycler_view);
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        if (savedInstanceState != null) {
            mSubtitleVisible = savedInstanceState.getBoolean(SAVED_SUBTITLE_VISIBLE);
        }

        updateUI();

        return view;
    }

    //In general, onResume() is the safest place to take action to update
    //a fragment's view
    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    //saved the state of the subtitle
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVED_SUBTITLE_VISIBLE, mSubtitleVisible);
    }

    //this populates the Menu instance with the items defined in your file
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime_list, menu);

        //trigger a re-creation of the action items when the user presses
        //on the Show Subtitle action item
        MenuItem subtitleItem = menu.findItem(R.id.menu_item_show_subtitle);
        if (mSubtitleVisible) {
            subtitleItem.setTitle(R.string.hide_subtitle);
        } else {
            subtitleItem.setTitle(R.string.show_subtitle);
        }
    }

    //responding to menu selection
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_new_crime:
                Crime crime = new Crime();
                CrimeLab.get(getActivity()).addCrime(crime);
                Intent intent = CrimePagerActivity
                        .newIntent(getActivity(), crime.getId());
                startActivity(intent);
                return true;
            case R.id.menu_item_show_subtitle:
                mSubtitleVisible = !mSubtitleVisible;
                getActivity().invalidateOptionsMenu();
                updateSubtitle();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //set the subtitle of the toolbar
    private void updateSubtitle() {
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        int crimeCount = crimeLab.getCrimes().size();

        //generates the subtitle string using getString method, which
        //accepts replacement values for the placeholders in the string resource
        String subtitle = getString(R.string.subtitle_format, crimeCount);

        if (!mSubtitleVisible) {
            subtitle = null;
        }

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setSubtitle(subtitle);
    }

    private void updateUI() {
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        List<Crime> crimes = crimeLab.getCrimes();

        if (mAdapter == null) {
            mAdapter = new CrimeAdapter(crimes);
            mCrimeRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }
        //update the subtitle text on returning to CrimeListActivity
        updateSubtitle();
    }

    //ViewHolder maintains a reference to a single view, the TextView
    private class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Crime mCrime;

        private TextView mTitleTextView;
        private TextView mDateTextView;
        private CheckBox mSolvedCheckBox;

        public CrimeHolder(View itemView) {
            super(itemView);
            mTitleTextView = (TextView) itemView.findViewById(R.id.list_item_crime_title_text_view);
            mDateTextView = (TextView) itemView.findViewById(R.id.list_item_crime_date_text_view);
            mSolvedCheckBox = (CheckBox) itemView.findViewById(R.id.list_item_crime_solved_check_box);
            itemView.setOnClickListener(this);
        }

        //when a given Crime, CrimeHolder will now update the
        //title TextView, date TextView and solved Checkbox to reflect
        //the state of the Crime
        public void bindCrime(Crime crime) {
            mCrime = crime;
            mTitleTextView.setText(mCrime.getTitle());
            mDateTextView.setText(mCrime.getDate().toString());
            mSolvedCheckBox.setChecked(mCrime.isSolved());
        }

        //CrimeHolder itself is implementing the OnClickListener interface
        //On the itemView, which is the entire row
        @Override
        public void onClick(View view) {
            Toast.makeText(getActivity(), mCrime.getTitle() + " clicked!", Toast.LENGTH_SHORT).show();
            //CrimeHolder using the newIntent method while passing in the crime ID
//            Intent intent = CrimeActivity.newIntent(getActivity(), mCrime.getId());
            Intent intent = CrimePagerActivity.newIntent(getActivity(), mCrime.getId());
            startActivity(intent);
        }
    }

    //the RecyclerView will copmmunicate with this adapter when a ViewHolder needs to be
    //created or connected with a Crime object.
    private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder> {

        private List<Crime> mCrimes;

        public CrimeAdapter(List<Crime> crimes) {
            mCrimes = crimes;
        }


        //onCreateViewHolder is called by the RecyclerView when it needs a new View
        //to display an item.
        //In this method you create the View and wrap it in a ViewHolder
        @Override
        public CrimeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            //inflate the custom layout list_item_crime
            View view = layoutInflater
                    .inflate(R.layout.list_item_crime, parent, false);
            return new CrimeHolder(view);
        }

        //This method will bind the ViewHolder's View to your model object.
        //it receives the ViewHolder and position in your data set.
        @Override
        public void onBindViewHolder(CrimeHolder holder, int position) {
            Crime crime = mCrimes.get(position);
            holder.bindCrime(crime);
        }

        @Override
        public int getItemCount() {
            return mCrimes.size();
        }
    }

}
