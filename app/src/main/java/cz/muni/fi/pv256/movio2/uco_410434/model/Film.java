package cz.muni.fi.pv256.movio2.uco_410434.model;


import android.os.Parcel;
import android.os.Parcelable;

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
    private final String title;
    private final double voteAverage;
    private final String backdropPath;

    public Film(String title, double voteAverage, String backdropPath) {
        this.title = title;
        this.voteAverage = voteAverage;
        this.backdropPath = backdropPath;
    }

    protected Film(Parcel in) {
        this.title = in.readString();
        this.voteAverage = in.readDouble();
        this.backdropPath = in.readString();
    }

    public String getTitle() {
        return title;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeDouble(this.voteAverage);
        dest.writeString(this.backdropPath);
    }

    @Override
    public String toString() {
        return "Film{" +
                "title='" + title + '\'' +
                ", voteAverage=" + voteAverage +
                ", backdropPath='" + backdropPath + '\'' +
                '}';
    }

    public String getBackdropURL() {
        return "http://image.tmdb.org/t/p/w300/" + getBackdropPath();
    }
}
