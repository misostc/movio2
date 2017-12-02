package cz.muni.fi.pv256.movio2.uco_410434.asynctask;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDate;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Locale;

import cz.muni.fi.pv256.movio2.uco_410434.BuildConfig;
import cz.muni.fi.pv256.movio2.uco_410434.manager.FilmManager;
import cz.muni.fi.pv256.movio2.uco_410434.manager.JSONFilmMapper;
import cz.muni.fi.pv256.movio2.uco_410434.model.Film;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FilmsLoadAsyncTask extends AsyncTask<Void, Integer, FilmsLoadAsyncTask.Result> {

    private static final String TAG = FilmsLoadAsyncTask.class.getSimpleName();
    private WeakReference<Listener> listener;

    public FilmsLoadAsyncTask(Listener listener) {
        this.listener = new WeakReference<>(listener);
    }

    @Override
    protected void onPostExecute(Result result) {
        super.onPostExecute(result);
        if (listener.get() != null) {
            if (result.equals(Result.ERROR)) {
                listener.get().onError();
            }
            if (result.equals(Result.OK)) {
                listener.get().onCompleted();
            }
        }
    }

    @Override
    protected Result doInBackground(Void... voids) {
        if (
                returnFilms(getMovieDBURITopRated(), FilmManager.getInstance().getTopRatedFilms())
                        && returnFilms(getMovieDBURIInCinemas(), FilmManager.getInstance().getFilmsInCinemas())
                ) {
            return Result.OK;
        }
        return Result.ERROR;
    }

    @NonNull
    private boolean returnFilms(String movieDBUrI, List<Film> filmsList) {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(movieDBUrI)
                .build();

        Response response = null;
        try {
            try {
                response = client.newCall(request).execute();
            } catch (IOException e) {
                Log.e(TAG, "Failed to load films from moviedb", e);
                return false;
            }

            if (!response.isSuccessful()) {
                Log.e(TAG, "Failed to load films from moviedb " + response.toString());
                return false;
            }

            List<Film> films = JSONFilmMapper.convertFromJson(response.body().charStream());
            filmsList.clear();
            filmsList.addAll(films);

            Log.i(TAG, "Loaded " + films.size() + " films.");
            return true;
        } finally {
            if (response != null && response.body() != null) {
                response.body().close();
            }
        }
    }

    @NonNull
    private String getMovieDBURITopRated() {
        return "https://api.themoviedb.org/3/discover/movie?"
                + "api_key=" + BuildConfig.MOVIEDB_API_KEY
                + "&language=" + Locale.getDefault().toString()
                + "&sort_by=vote_average.desc"
                + "&include_adult=false"
                + "&include_video=false"
                + "&page=1";
    }

    private String getMovieDBURIInCinemas() {
        return "https://api.themoviedb.org/3/discover/movie?"
                + "api_key=" + BuildConfig.MOVIEDB_API_KEY
                + "&language=" + Locale.getDefault().toString()
                + "&sort_by=vote_average.desc"
                + "&include_adult=false"
                + "&include_video=false"
                + "&release_date.gte=" + getLastWeek()
                + "&page=1";
    }

    /**
     * Get day for new releases
     *
     * @return date for the beginning of previous week
     */
    private String getLastWeek() {
        LocalDate today = LocalDate.now();
        return today.withDayOfWeek(DateTimeConstants.MONDAY).minusWeeks(1).toString("yyyy-MM-dd");
    }

    public enum Result {
        OK, ERROR
    }

    public interface Listener {
        void onCompleted();

        void onError();
    }
}
