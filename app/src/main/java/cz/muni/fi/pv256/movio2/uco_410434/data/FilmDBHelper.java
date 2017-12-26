package cz.muni.fi.pv256.movio2.uco_410434.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static cz.muni.fi.pv256.movio2.uco_410434.data.FilmContract.FilmEntry.COLUMN_BACKDROP_PATH_TEXT;
import static cz.muni.fi.pv256.movio2.uco_410434.data.FilmContract.FilmEntry.COLUMN_NAME_TEXT;
import static cz.muni.fi.pv256.movio2.uco_410434.data.FilmContract.FilmEntry.COLUMN_RELEASE_DATE_TEXT;
import static cz.muni.fi.pv256.movio2.uco_410434.data.FilmContract.FilmEntry.COLUMN_VOTE_AVERAGE_TEXT;
import static cz.muni.fi.pv256.movio2.uco_410434.data.FilmContract.FilmEntry.TABLE_NAME;
import static cz.muni.fi.pv256.movio2.uco_410434.data.FilmContract.FilmEntry._ID;

class FilmDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "films.db";
    private static final int DATABASE_VERSION = 1;

    public FilmDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY," +
                COLUMN_NAME_TEXT + " TEXT NOT NULL, " +
                COLUMN_RELEASE_DATE_TEXT + " TEXT," +
                COLUMN_VOTE_AVERAGE_TEXT + " TEXT," +
                COLUMN_BACKDROP_PATH_TEXT + " TEXT" +
                " );");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
