package cz.muni.fi.pv256.movio2.uco_410434.ui;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;

import cz.muni.fi.pv256.movio2.uco_410434.data.PersistedFilmManager;
import cz.muni.fi.pv256.movio2.uco_410434.model.Category;

public class PersistedFilmListFragment extends AbstractFilmListFragment {

    private Handler mHandler = new Handler(Looper.getMainLooper());
    private PersistedFilmManager filmsManager;

    public static PersistedFilmListFragment newInstance() {
        return new PersistedFilmListFragment();
    }

    @Override
    public void onStart() {
        super.onStart();

        if (filmList.isEmpty()) {
            filmsManager = new PersistedFilmManager(getContext());
            new LoadFilmsAsyncTask().execute();
        }
    }

    private class LoadFilmsAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            filmList.add(new Category("Persisted Films"));
            filmList.addAll(filmsManager.getFilms());

            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    fillListView();
                }
            });

            return null;
        }
    }
}
