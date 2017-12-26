package cz.muni.fi.pv256.movio2.uco_410434.model;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.JsonAdapter;

import org.joda.time.DateTime;

import cz.muni.fi.pv256.movio2.uco_410434.util.JsonDateDeserializer;

public class Film implements FilmsListItem, Parcelable {

    public static final Creator<Film> CREATOR = new Creator<Film>() {
        @Override
        public Film createFromParcel(Parcel source) {
            return new Film(source);
        }

        @Override
        public Film[] newArray(int size) {
            return new Film[size];
        }
    };
    private transient Long id;
    private String title;
    @JsonAdapter(JsonDateDeserializer.class)
    private DateTime releaseDate;
    private double voteAverage;
    private String backdropPath;

    public Film() {
    }

    public Film(String title, double voteAverage, String backdropPath) {
        this.id = null;
        this.title = title;
        this.releaseDate = null;
        this.voteAverage = voteAverage;
        this.backdropPath = backdropPath;
    }

    protected Film(Parcel in) {
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.title = in.readString();
        this.releaseDate = (DateTime) in.readSerializable();
        this.voteAverage = in.readDouble();
        this.backdropPath = in.readString();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public DateTime getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(DateTime releaseDate) {
        this.releaseDate = releaseDate;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public String getBackdropURL() {
        return "http://image.tmdb.org/t/p/w300/" + getBackdropPath();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.title);
        dest.writeSerializable(this.releaseDate);
        dest.writeDouble(this.voteAverage);
        dest.writeString(this.backdropPath);
    }

    @Override
    public String toString() {
        return "Film{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", releaseDate=" + releaseDate +
                ", voteAverage=" + voteAverage +
                ", backdropPath='" + backdropPath + '\'' +
                '}';
    }
}
