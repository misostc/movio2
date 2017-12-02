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
    private final double rating;
    private final String imageUri;

    public Film(String title, double rating, String imageUri) {
        this.title = title;
        this.rating = rating;
        this.imageUri = imageUri;
    }

    protected Film(Parcel in) {
        this.title = in.readString();
        this.rating = in.readDouble();
        this.imageUri = in.readString();
    }

    public String getTitle() {
        return title;
    }

    public double getRating() {
        return rating;
    }

    public String getImageUri() {
        return imageUri;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeDouble(this.rating);
        dest.writeString(this.imageUri);
    }

    @Override
    public String toString() {
        return "Film{" +
                "title='" + title + '\'' +
                ", rating=" + rating +
                ", imageUri='" + imageUri + '\'' +
                '}';
    }
}
