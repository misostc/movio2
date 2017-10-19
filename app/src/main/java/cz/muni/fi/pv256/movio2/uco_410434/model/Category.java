package cz.muni.fi.pv256.movio2.uco_410434.model;

public class Category implements FilmsListItem {
    private final String name;

    public Category(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
