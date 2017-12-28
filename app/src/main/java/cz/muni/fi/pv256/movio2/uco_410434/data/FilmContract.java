package cz.muni.fi.pv256.movio2.uco_410434.data;

import android.content.ContentUris;
import android.net.Uri;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

class FilmContract {

    public static final String PATH_FILM = "film";
    public static final String DATE_FORMAT = "yyyyMMddHHmm";
    public static String CONTENT_AUTHORITY = "cz.muni.fi.pv256.movio2.uco_410434";
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    /**
     * Converts Date class to a string representation, used for easy comparison and database
     * lookup.
     *
     * @param date The input date
     * @return a DB-friendly representation of the date, using the format defined in DATE_FORMAT.
     */
    public static String getDbDateString(DateTime date) {
        return date.toString(DATE_FORMAT);
    }

    /**
     * Converts a dateText to a long Unix time representation
     *
     * @param dateText the input date string
     * @return the Date object
     */
    public static DateTime getDateFromDb(String dateText) {
        return DateTime.parse(dateText, DateTimeFormat.forPattern(DATE_FORMAT).withOffsetParsed());
    }

    public static class FilmEntry {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_FILM).build();
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_FILM;
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + PATH_FILM;

        public static final String TABLE_NAME = "films";
        public static final String _ID = "id";
        public static final String COLUMN_NAME_TEXT = "name";
        public static final String COLUMN_RELEASE_DATE_TEXT = "release_date";
        public static final String COLUMN_VOTE_AVERAGE_TEXT = "vote_average";
        public static final String COLUMN_BACKDROP_PATH_TEXT = "backdrop_path";
        public static final String COLUMN_EXTERNAL_ID = "external_id";


        public static final String[] FILM_COLUMNS = {
                _ID,
                COLUMN_NAME_TEXT,
                COLUMN_RELEASE_DATE_TEXT,
                COLUMN_VOTE_AVERAGE_TEXT,
                COLUMN_BACKDROP_PATH_TEXT,
                COLUMN_EXTERNAL_ID
        };

        public static final int COL_FILM_ID = 0;
        public static final int COL_FILM_NAME = 1;
        public static final int COL_FILM_RELEASE_DATE = 2;
        public static final int COL_FILM_VOTE_AVERAGE = 3;
        public static final int COL_FILM_BACKDROP_PATH = 4;
        public static final int COL_FILM_EXTERNAL_ID = 5;


        public static Uri buildFilmUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}
