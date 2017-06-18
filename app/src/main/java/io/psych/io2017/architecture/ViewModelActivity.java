package io.psych.io2017.architecture;

import android.arch.lifecycle.LifecycleActivity;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

import io.psych.io2017.R;
import io.psych.io2017.psych.Episode;
import io.psych.io2017.psych.Episodes;
import io.psych.io2017.psych.PsychEpisodesListAdapter;

public class ViewModelActivity extends LifecycleActivity {

    private PsychEpisodesViewModel mViewModel;

    private ListView mListView;
    private PsychEpisodesListAdapter mListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_model);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setActionBar(toolbar);
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        mListAdapter = new PsychEpisodesListAdapter();
        mListView = findViewById(R.id.listview);
        mListView.setAdapter(mListAdapter);

        // Note: This method retrieves a ViewModel, but creates one for you if it doesn't exist.
        // This means you have to provide a no-arg constructor which means you can't inject
        // dependencies.  This limitation seems unnecessary and harmful.
        mViewModel = ViewModelProviders.of(this).get(PsychEpisodesViewModel.class);
        mViewModel.getPsychEpisodesLiveData().observe(this, new Observer<List<Episode>>() {
            @Override
            public void onChanged(@Nullable List<Episode> psychEpisodes) {
                updateView();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateView();
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

    private void updateView() {
        mListAdapter.setPsychEpisodes(mViewModel.getEpisodes());
    }

    // Public so that ViewModelProviders can access it.
    public static class PsychEpisodesViewModel extends ViewModel {

        private final MutableLiveData<List<Episode>> mPsychEpisodes = new MutableLiveData<>();

        public PsychEpisodesViewModel() {
            // Note: ViewModelProviders will require a publicly accessible no-arg constructor. If
            // one is not available, either explicitly or by default, then the app will crash.
            // This means no dependency injection.
            mPsychEpisodes.setValue(new ArrayList<Episode>());
            loadEpisodes();
        }

        public List<Episode> getEpisodes() {
            return mPsychEpisodes.getValue();
        }

        public LiveData<List<Episode>> getPsychEpisodesLiveData() {
            return mPsychEpisodes;
        }

        private void loadEpisodes() {
            // Simulate a long-running load operation that loads episodes one-by-one.
            new Thread(new Runnable() {
                @Override
                public void run() {
                    sleep(3000);

                    for (final Episode psychEpisode : Episodes.PSYCH_EPISODES) {
                        // ViewModels must publish their changes to the UI thread.
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                publishNewEpisode(psychEpisode);
                            }
                        });

                        sleep(500);
                    }
                }
            }).start();
        }

        private void publishNewEpisode(@NonNull Episode episode) {
            List<Episode> updatedList = mPsychEpisodes.getValue();
            updatedList.add(episode);
            mPsychEpisodes.setValue(updatedList);
        }

        private void sleep(int sleepTimeInMillis) {
            try {
                Thread.sleep(sleepTimeInMillis);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
