package cz.muni.fi.pv256.movio2.uco_410434.manager;


import java.util.ArrayList;
import java.util.List;

import cz.muni.fi.pv256.movio2.uco_410434.model.Film;

/**
 * Singleton used for holding downloaded films.
 */
public class FilmManager {

    private static FilmManager instance;
    private List<Film> topRatedFilmList;
    private List<Film> cinemasFilmsList;

    private FilmManager() {
        topRatedFilmList = new ArrayList<>();
        cinemasFilmsList = new ArrayList<>();
    }

    public static synchronized FilmManager getInstance() {
        if (instance == null) {
            instance = new FilmManager();
        }
        return instance;
    }

    public List<Film> getTopRatedFilms() {
        return topRatedFilmList;
    }

    public List<Film> getFilmsInCinemas() {
        return cinemasFilmsList;
    }

    public boolean hasData() {
        return !topRatedFilmList.isEmpty() && !cinemasFilmsList.isEmpty();
    }
}
