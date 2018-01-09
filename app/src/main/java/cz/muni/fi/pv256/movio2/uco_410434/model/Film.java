package cz.muni.fi.pv256.movio2.uco_410434.model;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;

import org.joda.time.DateTime;

import cz.muni.fi.pv256.movio2.uco_410434.util.JsonDateDeserializer;

public class Film implements FilmsListItem, Parcelable {

    public static final Parcelable.Creator<Film> CREATOR = new Parcelable.Creator<Film>() {
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
    @SerializedName("id")
    private Long externalId;

    public Film() {
    }

    protected Film(Parcel in) {
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.title = in.readString();
        this.releaseDate = (DateTime) in.readSerializable();
        this.voteAverage = in.readDouble();
        this.backdropPath = in.readString();
        this.externalId = (Long) in.readValue(Long.class.getClassLoader());
    }

    public String getBackdropURL() {
        return "http://image.tmdb.org/t/p/w250_and_h141_bestv2/" + getBackdropPath();
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

    public Long getExternalId() {
        return externalId;
    }

    public void setExternalId(Long externalId) {
        this.externalId = externalId;
    }

    @Override
    public String toString() {
        return "Film{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", releaseDate=" + releaseDate +
                ", voteAverage=" + voteAverage +
                ", backdropPath='" + backdropPath + '\'' +
                ", externalId=" + externalId +
                '}';
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
        dest.writeValue(this.externalId);
    }
}
