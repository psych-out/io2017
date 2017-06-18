package io.psych.io2017.psych;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * {@code ListAdapter} that displays episode synopsis for the TV show Psych.
 */
public class PsychEpisodesListAdapter extends BaseAdapter {

    private final List<Episode> mPsychEpisodes = new ArrayList<>();

    public void setPsychEpisodes(@NonNull List<Episode> psychEpisodes) {
        mPsychEpisodes.clear();
        mPsychEpisodes.addAll(psychEpisodes);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mPsychEpisodes.size();
    }

    @Override
    public Episode getItem(int position) {
        return mPsychEpisodes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (null == convertView) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_2, parent, false);
        }

        Episode episode = getItem(position);
        ((TextView) convertView.findViewById(android.R.id.text1)).setText(episode.getTitle());
        ((TextView) convertView.findViewById(android.R.id.text2)).setText(episode.getSeasonReadable());

        return convertView;
    }
}
