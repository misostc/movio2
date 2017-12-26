package cz.muni.fi.pv256.movio2.uco_410434.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import cz.muni.fi.pv256.movio2.uco_410434.R;
import cz.muni.fi.pv256.movio2.uco_410434.adapter.FilmsAdapter;
import cz.muni.fi.pv256.movio2.uco_410434.model.EmptyListItem;
import cz.muni.fi.pv256.movio2.uco_410434.model.Film;
import cz.muni.fi.pv256.movio2.uco_410434.model.FilmsListItem;

public class AbstractFilmListFragment extends Fragment {
    protected Context context;
    protected ArrayList<FilmsListItem> filmList = new ArrayList<>();
    protected RecyclerView filmListView;
    private OnFilmSelectListener filmSelectListener;

    public OnFilmSelectListener getFilmSelectListener() {
        return filmSelectListener;
    }

    public void setFilmSelectListener(OnFilmSelectListener filmSelectListener) {
        this.filmSelectListener = filmSelectListener;
    }

    protected void fillListView() {
        LinearLayoutManager filmsLayoutManager = new LinearLayoutManager(context);
        filmListView.setLayoutManager(filmsLayoutManager);

        if (!filmList.isEmpty()) {
            setAdapter(filmListView, filmList);
        }
    }

    protected void fillError() {
        filmList.clear();
        filmList.add(new EmptyListItem());
        fillListView();
    }

    private void setAdapter(RecyclerView view, List<FilmsListItem> filmList) {
        FilmsAdapter filmsAdapter = new FilmsAdapter(this, filmList.toArray(new FilmsListItem[0]));
        view.setAdapter(filmsAdapter);
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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity().getApplicationContext();
    }

    public interface OnFilmSelectListener {
        void onFilmSelect(Film film);
    }

}
