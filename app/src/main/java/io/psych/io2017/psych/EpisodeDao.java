package io.psych.io2017.psych;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface EpisodeDao {

    @Query("SELECT * FROM episode")
    List<Episode> getAll();

    @Query("SELECT * FROM episode WHERE season=:season")
    List<Episode> getAllInSeason(int season);

    @Insert
    void insertAll(Episode ... episodes);

}
