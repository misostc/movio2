package cz.muni.fi.pv256.movio2.uco_410434.ui;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import cz.muni.fi.pv256.movio2.uco_410434.R;
import cz.muni.fi.pv256.movio2.uco_410434.asynctask.FilmsLoadAsyncTask;
import cz.muni.fi.pv256.movio2.uco_410434.manager.FilmManager;
import cz.muni.fi.pv256.movio2.uco_410434.model.Category;
import cz.muni.fi.pv256.movio2.uco_410434.model.EmptyListItem;
import cz.muni.fi.pv256.movio2.uco_410434.model.Film;
import cz.muni.fi.pv256.movio2.uco_410434.model.FilmsListItem;

public class FilmListFragment extends Fragment implements FilmsLoadAsyncTask.Listener {

    public static final String TAG = FilmListFragment.class.getSimpleName();

    private Context context;
    private OnFilmSelectListener filmSelectListener;
    private ArrayList<FilmsListItem> filmList = new ArrayList<>();
    private RecyclerView filmListView;

    private AsyncTask<Void, Integer, FilmsLoadAsyncTask.Result> asyncTask;

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

        if (FilmManager.getInstance().hasData()) {
            fillFilmsList();
            fillListView();
        } else {
            if (asyncTask != null) {
                asyncTask.cancel(true);
            }

            asyncTask = new FilmsLoadAsyncTask(this);
            asyncTask.execute();
        }
        Log.i(TAG, "onStart");
    }

    @Override
    public void onStop() {
        super.onStop();
        if (asyncTask != null) {
            asyncTask.cancel(true);
            asyncTask = null;
        }
        Log.i(TAG, "onStop");
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
        FilmsAdapter filmsAdapter = new FilmsAdapter(filmList.toArray(new FilmsListItem[0]));
        view.setAdapter(filmsAdapter);
    }

    @Override
    public void onCompleted() {
        fillFilmsList();
        fillListView();
    }

    private void fillFilmsList() {
        filmList.clear();

        filmList.add(new Category("Now in cinemas"));
        filmList.addAll(FilmManager.getInstance().getFilmsInCinemas());

        filmList.add(new Category("Top Rated"));
        filmList.addAll(FilmManager.getInstance().getTopRatedFilms());
    }

    @Override
    public void onError() {
        fillError();
    }


    public interface OnFilmSelectListener {
        void onFilmSelect(Film film);
    }

    public class FilmsAdapter extends RecyclerView.Adapter<FilmsAdapter.ViewHolder> {

        private static final int FILM = 0, CATEGORY = 1, EMPTY = 2;

        private FilmsListItem[] items;

        public FilmsAdapter(FilmsListItem[] items) {
            this.items = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v;
            switch (viewType) {
                case FILM:
                    v = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.film_list_film_item, parent, false);
                    break;
                case CATEGORY:
                    v = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.film_list_category_item, parent, false);
                    break;
                case EMPTY:
                    v = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.film_list_empty_item, parent, false);
                    break;
                default:
                    throw new IllegalArgumentException("Unexpected viewType: " + viewType);
            }
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mView.setOnClickListener(null);
            switch (getItemViewType(position)) {
                case FILM:
                    final TextView filmTitle = holder.mView.findViewById(R.id.film_title);
                    final ImageView filmImage = holder.mView.findViewById(R.id.film_image);
                    final AppCompatRatingBar filmRating = holder.mView.findViewById(R.id.film_rating);
                    final Film currentFilm = (Film) items[position];
                    final View titleContainer = holder.mView.findViewById(R.id.title_container);

                    Log.i(TAG, "Binding film " + currentFilm.toString());
                    filmTitle.setText(currentFilm.getTitle());
                    filmRating.setRating((float) currentFilm.getRating());
                    filmImage.setImageDrawable(null);
                    fallbackBGColor(titleContainer, filmImage);
                    if (currentFilm.getImageUri() != null) {
                        Log.i(TAG, "Loading film " + currentFilm.getTitle() + ", image " + currentFilm.getImageUri());
                        Picasso
                                .with(getContext())
                                .load(currentFilm.getImageUri())
                                .placeholder(R.color.indigo_teal_primary)
                                .into(filmImage, new Callback() {
                                    @Override
                                    public void onSuccess() {
                                        Log.i(TAG, "Done loading film " + currentFilm.getTitle() + ", image " + currentFilm.getImageUri());
                                        FilmPosterColorUtil.setBackgroundColor(context, filmImage.getDrawable(), titleContainer);
                                    }

                                    @Override
                                    public void onError() {
                                        Log.e(TAG, "Could not load image " + currentFilm.getImageUri());
                                        fallbackBGColor(titleContainer, filmImage);
                                    }
                                });
                    }

                    holder.mView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            filmSelectListener.onFilmSelect(currentFilm);
                        }
                    });
                    break;
                case CATEGORY:
                    TextView textView = holder.mView.findViewById(R.id.category_text);
                    Category currentCategory = (Category) items[position];
                    textView.setText(currentCategory.getName());
                    break;
            }
        }

        private void fallbackBGColor(View titleContainer, ImageView filmImage) {
            titleContainer.setBackgroundColor(getResources().getColor(R.color.indigo_teal_primary_dark));
            filmImage.setBackgroundColor(getResources().getColor(R.color.indigo_teal_primary));
        }

        @Override
        public int getItemViewType(int position) {
            if (items[position] instanceof Film) {
                return FILM;
            }
            if (items[position] instanceof Category) {
                return CATEGORY;
            }
            if (items[position] instanceof EmptyListItem) {
                return EMPTY;
            }
            return 0;
        }

        @Override
        public int getItemCount() {
            return items.length;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public View mView;

            public ViewHolder(View v) {
                super(v);
                mView = v;
            }
        }
    }
}
