package cz.muni.fi.pv256.movio2.uco_410434.sync;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.io.IOException;
import java.util.Locale;

import cz.muni.fi.pv256.movio2.uco_410434.BuildConfig;
import cz.muni.fi.pv256.movio2.uco_410434.R;
import cz.muni.fi.pv256.movio2.uco_410434.data.PersistedFilmManager;
import cz.muni.fi.pv256.movio2.uco_410434.model.Film;
import cz.muni.fi.pv256.movio2.uco_410434.service.MovieDBService;
import cz.muni.fi.pv256.movio2.uco_410434.service.MovieDBServiceUtil;
import cz.muni.fi.pv256.movio2.uco_410434.ui.FilmListActivity;
import retrofit2.Call;
import retrofit2.Response;

import static android.content.Context.NOTIFICATION_SERVICE;

class MovieDBSync {
    private static final String TAG = MovieDBSync.class.getSimpleName();
    private static String CHANNEL_ID = "movieDbSync";

    static void perform(Context context) {
        PersistedFilmManager filmManager = new PersistedFilmManager(context.getContentResolver());
        MovieDBService service = MovieDBServiceUtil.createService();

        boolean changed = false;
        for (Film persistedFilm : filmManager.getFilms()) {
            Log.d(TAG, "updatingFilm: " + persistedFilm);
            Film fetchedFilm = fetchFilm(service, persistedFilm);
            if (fetchedFilm == null) {
                continue;
            }

            Log.d(TAG, "fetchedFilm: " + fetchedFilm);
            if (!isChanged(fetchedFilm, persistedFilm)) {
                continue;
            }

            filmManager.deleteFilm(persistedFilm);
            filmManager.createFilm(fetchedFilm);
            changed = true;
        }
        Log.d(TAG, "changed: " + changed);
        if (changed) {
            notifyChange(context);
        }
    }

    private static void notifyChange(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        initChannel(notificationManager);

        Notification n = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle("Your saved films have updates")
                .setContentText("Open Movio to check them!")
                .setSmallIcon(R.drawable.notif_icon_complete)
                .setContentIntent(createIntent(context))
                .setAutoCancel(true).build();

        notificationManager.notify(0, n);
    }

    private static PendingIntent createIntent(Context context) {
        Intent resultIntent = new Intent(context, FilmListActivity.class);
        resultIntent.putExtra(FilmListActivity.EXTRA_OPEN_ON_SAVED, true);
        return PendingIntent.getActivity(
                context,
                0,
                resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
    }

    private static void initChannel(NotificationManager notificationManager) {
        if (Build.VERSION.SDK_INT < 26) {
            return;
        }
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                "Sync notifications",
                NotificationManager.IMPORTANCE_DEFAULT);
        channel.setDescription("Notifications about data sync");
        notificationManager.createNotificationChannel(channel);
    }

    private static boolean isChanged(Film fetchedFilm, Film persistedFilm) {
        boolean areSame = equalsNullCheck(fetchedFilm.getTitle(), persistedFilm.getTitle());
        areSame = areSame && equalsNullCheck(fetchedFilm.getVoteAverage(), persistedFilm.getVoteAverage());
        areSame = areSame && equalsNullCheck(fetchedFilm.getBackdropPath(), persistedFilm.getBackdropPath());
        areSame = areSame && equalsNullCheck(fetchedFilm.getReleaseDate(), persistedFilm.getReleaseDate());
        return !areSame;
    }

    private static boolean equalsNullCheck(Object o1, Object o2) {
        if (o1 == null && o2 == null) {
            return true;
        }
        if (o1 == null || o2 == null) {
            return false;
        }
        return o1.equals(o2);
    }

    private static Film fetchFilm(MovieDBService service, Film persistedFilm) {
        Call<Film> call = service.getFilmDetails(persistedFilm.getExternalId(), BuildConfig.MOVIEDB_API_KEY, Locale.getDefault().toString());
        try {
            Response<Film> res = call.execute();
            if (!res.isSuccessful()) {
                Log.e(TAG, "error fetching film: " + persistedFilm + "\n " + res.errorBody().string());
                return null;
            }

            // NORMAL
            return res.body();

            // FOR TESTING
//            Film returnedFilm = res.body();
//            if (returnedFilm == null) {
//                return null;
//            }
//            returnedFilm.setReleaseDate(DateTime.now());
//            return returnedFilm;
        } catch (IOException e) {
            Log.e(TAG, "error fetching film: " + persistedFilm, e);
            return null;
        }
    }
}
