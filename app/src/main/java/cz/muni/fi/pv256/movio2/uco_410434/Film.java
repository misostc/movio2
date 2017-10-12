package cz.muni.fi.pv256.movio2.uco_410434;

public class Film {
    private final String title;
    private final float rating;
    private final int imageResource;

    public Film(String title, float rating, int imageResource) {
        this.title = title;
        this.rating = rating;
        this.imageResource = imageResource;
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
}
