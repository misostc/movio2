package cz.muni.fi.pv256.movio2.uco_410434.ui;

import android.content.Intent;
import android.util.Log;

import com.squareup.otto.Subscribe;

import cz.muni.fi.pv256.movio2.uco_410434.manager.FilmManager;
import cz.muni.fi.pv256.movio2.uco_410434.model.Category;
import cz.muni.fi.pv256.movio2.uco_410434.service.FilmsDownloadService;
import cz.muni.fi.pv256.movio2.uco_410434.util.MainThreadBus;

public class OnlineFilmListFragment extends AbstractFilmListFragment {

    public static final String TAG = OnlineFilmListFragment.class.getSimpleName();

    public static OnlineFilmListFragment newInstance() {
        return new OnlineFilmListFragment();
    }

    @Override
    public void onStart() {
        super.onStart();
        MainThreadBus.getInstance().register(this);

        if (FilmManager.getInstance().hasData()) {
            fillFilmsList();
            fillListView();
        } else {
            Intent intent = new Intent(getActivity(), FilmsDownloadService.class);
            getActivity().startService(intent);
        }
        Log.i(TAG, "onStart");
    }


    @Subscribe
    public void onCompleted(FilmsDownloadService.CompletedEvent e) {
        fillFilmsList();
        fillListView();
    }

    @Subscribe
    public void onError(FilmsDownloadService.ErrorEvent e) {
        fillError();
    }


    @Override
    public void onStop() {
        super.onStop();
        Log.i(TAG, "onStop");
    }

    @Override
    public void onPause() {
        super.onPause();
        MainThreadBus.getInstance().unregister(this);
    }

    private void fillFilmsList() {
        Log.i(TAG, "In cinemas: " + FilmManager.getInstance().getFilmsInCinemas().toString());
        Log.i(TAG, "Top rated: " + FilmManager.getInstance().getTopRatedFilms().toString());
        filmList.clear();

        filmList.add(new Category("Now in cinemas"));
        filmList.addAll(FilmManager.getInstance().getFilmsInCinemas());

        filmList.add(new Category("Top Rated"));
        filmList.addAll(FilmManager.getInstance().getTopRatedFilms());
    }


}
