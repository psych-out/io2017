package io.psych.io2017.psych;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.support.annotation.NonNull;

@Entity(primaryKeys = {"season", "episode"})
public class Episode {

    @ColumnInfo(name = "season")
    private int season;

    @ColumnInfo(name = "episode")
    private int episode;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "description")
    private String description;

    public Episode(int season, int episode, @NonNull String title, @NonNull String description) {
        this.season = season;
        this.episode = episode;
        this.title = title;
        this.description = description;
    }

    public int getSeason() {
        return season;
    }

    public void setSeason(int season) {
        this.season = season;
    }

    public String getSeasonReadable() {
        return String.format("Season %d", getSeason());
    }

    public int getEpisode() {
        return episode;
    }

    public void setEpisode(int episode) {
        this.episode = episode;
    }

    public String getEpisodeReadable() {
        return String.format("Episode %d", getEpisode());
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
