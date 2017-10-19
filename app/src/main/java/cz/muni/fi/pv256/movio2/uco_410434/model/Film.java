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
    private final float rating;
    private final int imageResource;

    public Film(String title, float rating, int imageResource) {
        this.title = title;
        this.rating = rating;
        this.imageResource = imageResource;
    }

    protected Film(Parcel in) {
        this.title = in.readString();
        this.rating = in.readFloat();
        this.imageResource = in.readInt();
    }

    public String getTitle() {
        return title;
    }

    public float getRating() {
        return rating;
    }

    public int getImageResource() {
        return imageResource;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeFloat(this.rating);
        dest.writeInt(this.imageResource);
    }
}
