package cz.muni.fi.pv256.movio2.uco_410434.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import cz.muni.fi.pv256.movio2.uco_410434.R;
import cz.muni.fi.pv256.movio2.uco_410434.adapter.FilmsAdapter;
import cz.muni.fi.pv256.movio2.uco_410434.manager.FilmManager;
import cz.muni.fi.pv256.movio2.uco_410434.model.Category;
import cz.muni.fi.pv256.movio2.uco_410434.model.EmptyListItem;
import cz.muni.fi.pv256.movio2.uco_410434.model.Film;
import cz.muni.fi.pv256.movio2.uco_410434.model.FilmsListItem;
import cz.muni.fi.pv256.movio2.uco_410434.service.FilmsDownloadService;
import cz.muni.fi.pv256.movio2.uco_410434.service.MainThreadBus;

public class FilmListFragment extends Fragment {

    public static final String TAG = FilmListFragment.class.getSimpleName();
    private final Handler mHandler = new Handler(Looper.getMainLooper());
    private Context context;
    private OnFilmSelectListener filmSelectListener;
    private ArrayList<FilmsListItem> filmList = new ArrayList<>();
    private RecyclerView filmListView;

    public static FilmListFragment newInstance(OnFilmSelectListener listener) {
        FilmListFragment instance = new FilmListFragment();
        instance.filmSelectListener = listener;
        return instance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getActivity().getApplicationContext();
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
        Log.i(TAG, "completedEvent");
        Runnable r = new Runnable() {
            @Override
            public void run() {
                fillFilmsList();
                fillListView();
            }
        };

        if (Looper.myLooper() == Looper.getMainLooper()) {
            r.run();
        } else {
            mHandler.post(r);
        }
    }

    @Subscribe
    public void onError(FilmsDownloadService.ErrorEvent e) {
        Log.i(TAG, "errorEvent");
        Runnable r = new Runnable() {
            @Override
            public void run() {
                fillError();
            }
        };

        if (Looper.myLooper() == Looper.getMainLooper()) {
            r.run();
        } else {
            mHandler.post(r);
        }
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

    @Override
    public void onDetach() {
        super.onDetach();
        filmSelectListener = null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_film_list, container, false);
        filmListView = view.findViewById(R.id.listview_films);
        filmListView.setItemAnimator(null);
        filmListView.setHasFixedSize(true);
        filmListView.setItemViewCacheSize(20);
        filmListView.setDrawingCacheEnabled(true);
        filmListView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        return view;
    }

    private void fillListView() {
        LinearLayoutManager filmsLayoutManager = new LinearLayoutManager(context);
        filmListView.setLayoutManager(filmsLayoutManager);

        if (!filmList.isEmpty()) {
            setAdapter(filmListView, filmList);
        }
    }

    private void fillError() {
        filmList.clear();
        filmList.add(new EmptyListItem());
        fillListView();
    }

    private void setAdapter(RecyclerView view, List<FilmsListItem> filmList) {
        FilmsAdapter filmsAdapter = new FilmsAdapter(this, filmList.toArray(new FilmsListItem[0]));
        view.setAdapter(filmsAdapter);
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

    public OnFilmSelectListener getFilmSelectListener() {
        return filmSelectListener;
    }

    public void setFilmSelectListener(OnFilmSelectListener filmSelectListener) {
        this.filmSelectListener = filmSelectListener;
    }


    public interface OnFilmSelectListener {
        void onFilmSelect(Film film);
    }

}
