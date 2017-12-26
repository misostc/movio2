package cz.muni.fi.pv256.movio2.uco_410434.data;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cz.muni.fi.pv256.movio2.uco_410434.model.Film;

import static cz.muni.fi.pv256.movio2.uco_410434.data.FilmContract.FilmEntry;

public class PersistedFilmManager {

    private static final String WHERE_ID = FilmEntry._ID + " = ?";
    private Context mContext;

    public PersistedFilmManager(Context context) {
        mContext = context.getApplicationContext();
    }

    public void createFilm(Film film) {
        if (film == null) {
            throw new NullPointerException("film == null");
        }
        if (film.getId() != null) {
            throw new IllegalStateException("film id shouldn't be set");
        }
        if (film.getTitle() == null) {
            throw new IllegalStateException("film title cannot be null");
        }
        if (film.getReleaseDate() == null) {
            throw new IllegalStateException("film release date cannot be null");
        }
        film.setId(ContentUris.parseId(mContext.getContentResolver().insert(FilmEntry.CONTENT_URI, prepareFilmValues(film))));
    }

    public List<Film> getFilms() {
        Cursor cursor = mContext.getContentResolver().query(FilmEntry.CONTENT_URI, FilmEntry.FILM_COLUMNS, "", new String[]{}, null);
        if (cursor != null && cursor.moveToFirst()) {
            List<Film> films = new ArrayList<>(cursor.getCount());
            try {
                while (!cursor.isAfterLast()) {
                    films.add(getFilm(cursor));
                    cursor.moveToNext();
                }
            } finally {
                cursor.close();
            }
            return films;
        }

        return Collections.emptyList();
    }

    public void deleteFilm(Film film) {
        if (film == null) {
            throw new NullPointerException("film == null");
        }
        if (film.getId() == null) {
            throw new IllegalStateException("film id cannot be null");
        }

        mContext.getContentResolver().delete(FilmEntry.CONTENT_URI, WHERE_ID, new String[]{String.valueOf(film.getId())});
    }

    private ContentValues prepareFilmValues(Film film) {
        ContentValues values = new ContentValues();
        values.put(FilmEntry.COLUMN_NAME_TEXT, film.getTitle());
        values.put(FilmEntry.COLUMN_RELEASE_DATE_TEXT, FilmContract.getDbDateString(film.getReleaseDate()));
        values.put(FilmEntry.COLUMN_VOTE_AVERAGE_TEXT, Double.toString(film.getVoteAverage()));
        values.put(FilmEntry.COLUMN_BACKDROP_PATH_TEXT, film.getBackdropPath());
        return values;
    }

    private Film getFilm(Cursor cursor) {
        Film film = new Film();
        film.setId(cursor.getLong(FilmEntry.COL_FILM_ID));
        film.setTitle(cursor.getString(FilmEntry.COL_FILM_NAME));
        film.setReleaseDate(FilmContract.getDateFromDb(cursor.getString(FilmEntry.COL_FILM_RELEASE_DATE)));
        film.setVoteAverage(Double.parseDouble(cursor.getString(FilmEntry.COL_FILM_VOTE_AVERAGE)));
        film.setBackdropPath(cursor.getString(FilmEntry.COL_FILM_BACKDROP_PATH));
        return film;
    }
}
