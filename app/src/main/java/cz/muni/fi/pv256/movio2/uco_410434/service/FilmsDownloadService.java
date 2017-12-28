package cz.muni.fi.pv256.movio2.uco_410434.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDate;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import cz.muni.fi.pv256.movio2.uco_410434.BuildConfig;
import cz.muni.fi.pv256.movio2.uco_410434.R;
import cz.muni.fi.pv256.movio2.uco_410434.manager.FilmManager;
import cz.muni.fi.pv256.movio2.uco_410434.model.Film;
import cz.muni.fi.pv256.movio2.uco_410434.util.MainThreadBus;
import retrofit2.Call;
import retrofit2.Response;


public class FilmsDownloadService extends IntentService {

    public static final String CHANNEL_ID = "default";
    private static final String TAG = FilmsDownloadService.class.getSimpleName();
    private NotificationManager notificationManager;

    public FilmsDownloadService() {
        super(FilmsDownloadService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        MainThreadBus.getInstance().register(this);
        initNotificationManager();
        notifyStart();
        try {
            MovieDBService movieDBService = MovieDBServiceUtil.createService();
            Call<List<Film>> loadedLastWeek = movieDBService.getTopRatedSince(BuildConfig.MOVIEDB_API_KEY, Locale.getDefault().toString(), getDateThreshold(), getToday());
            Call<List<Film>> loadedTopRated = movieDBService.getTopRated(BuildConfig.MOVIEDB_API_KEY, Locale.getDefault().toString());
            List<Film> targetListCurrent = FilmManager.getInstance().getFilmsInCinemas();
            List<Film> targetListTop = FilmManager.getInstance().getTopRatedFilms();
            executeCall(loadedLastWeek, targetListCurrent);
            executeCall(loadedTopRated, targetListTop);
            notifyEnd();
            MainThreadBus.getInstance().post(new CompletedEvent());
        } catch (Throwable e) {
            notifyError();
            MainThreadBus.getInstance().post(new ErrorEvent());
            Log.e(TAG, "Error downloading films", e);
        }
        MainThreadBus.getInstance().unregister(this);
    }

    private void executeCall(Call<List<Film>> call, List<Film> targetList) throws java.io.IOException {
        Response<List<Film>> response = call.execute();
        if (response != null && response.isSuccessful()) {
            targetList.clear();
            List<Film> body = response.body();
            Log.i(TAG, "Loaded films:" + body.toString());
            targetList.addAll(body);
            return;
        }
        throw new IOException("Films not loaded, response:" + response.toString());
    }

    private void initNotificationManager() {
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT < 26) {
            return;
        }
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                "Download notifications",
                NotificationManager.IMPORTANCE_DEFAULT);
        channel.setDescription("Notifications about film downloads");
        notificationManager.createNotificationChannel(channel);
    }


    private String getDateThreshold() {
        LocalDate today = LocalDate.now();
        return today.withDayOfWeek(DateTimeConstants.MONDAY).minusWeeks(1).toString(MovieDBService.API_DATE_FORMAT);
    }

    private String getToday() {
        return LocalDate.now().toString(MovieDBService.API_DATE_FORMAT);
    }

    private void notifyStart() {
        Notification n = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Downloading films")
                .setContentText("Obtaining films from MovieDB")
                .setSmallIcon(R.drawable.notif_icon)
                .setAutoCancel(true).build();

        notificationManager.notify(0, n);
    }


    private void notifyEnd() {
        notificationManager.cancelAll();
        Notification n = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Downloading films complete")
                .setContentText("Check out the downloaded films!")
                .setSmallIcon(R.drawable.notif_icon_complete)
                .setAutoCancel(true).build();

        notificationManager.notify(0, n);
    }

    private void notifyError() {
        notificationManager.cancelAll();
        Notification n = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Problem downloading films")
                .setContentText("Check your internet and try again.")
                .setSmallIcon(R.drawable.notif_icon_error)
                .setAutoCancel(true).build();

        notificationManager.notify(0, n);
    }

    public class CompletedEvent {
    }

    public class ErrorEvent {
    }
}
