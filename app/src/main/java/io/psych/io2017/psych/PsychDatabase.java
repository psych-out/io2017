package io.psych.io2017.psych;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {Episode.class}, version = 1)
public abstract class PsychDatabase extends RoomDatabase {
    public abstract EpisodeDao episodeQuery();
}
