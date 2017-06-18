package io.psych.io2017.room;

import android.app.Activity;
import android.app.LoaderManager;
import android.arch.persistence.room.Room;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Loader;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toolbar;

import java.util.List;

import io.psych.io2017.R;
import io.psych.io2017.psych.Episode;
import io.psych.io2017.psych.Episodes;
import io.psych.io2017.psych.PsychDatabase;
import io.psych.io2017.psych.PsychEpisodesListAdapter;

public class RoomActivity extends Activity {

    private static final String TAG = "RoomActivity";
    private static final int LOADER_CREATE_DATABASE = 1000;
    private static final int LOADER_QUERY_EPISODES = 1001;

    private PsychDatabase mPsychDatabase;

    private ListView mListView;
    private PsychEpisodesListAdapter mListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setActionBar(toolbar);
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        mListAdapter = new PsychEpisodesListAdapter();
        mListView = findViewById(R.id.listview);
        mListView.setAdapter(mListAdapter);

        getLoaderManager().initLoader(LOADER_CREATE_DATABASE, null, mPsychDatabaseLoaderCallbacks);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (android.R.id.home == item.getItemId()) {
            finish();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void onDatabaseLoaded(@NonNull PsychDatabase psychDatabase) {
        mPsychDatabase = psychDatabase;

        getLoaderManager().initLoader(LOADER_QUERY_EPISODES, null, mEpisodesLoaderCallbacks);
    }

    private void displayEpisodes(@NonNull List<Episode> episodes) {
        mListAdapter.setPsychEpisodes(episodes);
    }

    private LoaderManager.LoaderCallbacks<PsychDatabase> mPsychDatabaseLoaderCallbacks = new LoaderManager.LoaderCallbacks<PsychDatabase>() {
        @Override
        public Loader<PsychDatabase> onCreateLoader(int i, Bundle bundle) {
            Log.d(TAG, "Creating PsychDatabaseLoader.");
            return new PsychDatabaseLoader(RoomActivity.this);
        }

        @Override
        public void onLoadFinished(Loader<PsychDatabase> loader, PsychDatabase psychDatabase) {
            Log.d(TAG, "Done loading PsychDatabase.");
            onDatabaseLoaded(psychDatabase);
        }

        @Override
        public void onLoaderReset(Loader<PsychDatabase> loader) { }
    };

    private static class PsychDatabaseLoader extends AsyncTaskLoader<PsychDatabase> {

        private final Context mContext;
        private PsychDatabase mPsychDatabase;

        public PsychDatabaseLoader(Context context) {
            super(context);
            mContext = context.getApplicationContext();
            forceLoad();
        }

        @Override
        public PsychDatabase loadInBackground() {
            createDatabase();

            if (isDatabaseEmpty()) {
                fillDatabase();
            }

            return mPsychDatabase;
        }

        private void createDatabase() {
            Log.d(TAG, "Creating Psych Database.");
            mPsychDatabase = Room.databaseBuilder(
                    mContext,
                    PsychDatabase.class,
                    "psych")
                    .build();
        }

        private boolean isDatabaseEmpty() {
            return mPsychDatabase.episodeQuery().getAll().size() == 0;
        }

        private void fillDatabase() {
            Log.d(TAG, "Doing initial Psych Database fill.");
            mPsychDatabase
                    .episodeQuery()
                    .insertAll(Episodes.PSYCH_EPISODES.toArray(new Episode[]{}));
        }
    }

    private LoaderManager.LoaderCallbacks<List<Episode>> mEpisodesLoaderCallbacks = new LoaderManager.LoaderCallbacks<List<Episode>>() {
        @Override
        public Loader<List<Episode>> onCreateLoader(int i, Bundle bundle) {
            return new EpisodesLoader(RoomActivity.this, mPsychDatabase);
        }

        @Override
        public void onLoadFinished(Loader<List<Episode>> loader, List<Episode> episodes) {
            displayEpisodes(episodes);
        }

        @Override
        public void onLoaderReset(Loader<List<Episode>> loader) {}
    };

    private static class EpisodesLoader extends AsyncTaskLoader<List<Episode>> {

        private final PsychDatabase mPsychDatabase;

        public EpisodesLoader(Context context, @NonNull PsychDatabase database) {
            super(context);
            mPsychDatabase = database;
            forceLoad();
        }

        @Override
        public List<Episode> loadInBackground() {
            Log.d(TAG, "Querying season 1 Episodes.");
            List<Episode> episodes = mPsychDatabase.episodeQuery().getAllInSeason(1);
            Log.d(TAG, "Retrieved " + episodes.size() + " Episodes.");
            return episodes;
        }
    }
}
