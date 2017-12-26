package cz.muni.fi.pv256.movio2.uco_410434.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cz.muni.fi.pv256.movio2.uco_410434.R;
import cz.muni.fi.pv256.movio2.uco_410434.model.Category;
import cz.muni.fi.pv256.movio2.uco_410434.model.EmptyListItem;
import cz.muni.fi.pv256.movio2.uco_410434.model.Film;
import cz.muni.fi.pv256.movio2.uco_410434.model.FilmsListItem;

public class FilmListFragment extends Fragment {

    public static final String TAG = FilmListFragment.class.getSimpleName();

    private Context context;
    private OnFilmSelectListener filmSelectListener;
    private ArrayList<FilmsListItem> filmList;
    private RecyclerView filmListView;

    {
        filmList = new ArrayList<>();
        filmList.add(new Category("This week opening"));
        filmList.add(new Film("Boss Baby", 3.5f, R.mipmap.bossbaby));
        filmList.add(new Film("Sherlock ", 4.5f, R.mipmap.sherlock));
        filmList.add(new Category("Old Classics"));
        filmList.add(new Film("The Shining", 4.8f, R.mipmap.shining));
        filmList.add(new Film("Casablanca", 3.4f, R.mipmap.casablanca));
    }

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
    public void onDetach() {
        super.onDetach();

        filmSelectListener = null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_film_list, container, false);
        fillListView(view);
        return view;
    }

    private void fillListView(View view) {
        filmListView = view.findViewById(R.id.listview_films);
        LinearLayoutManager filmsLayoutManager = new LinearLayoutManager(context);
        filmListView.setLayoutManager(filmsLayoutManager);

        if (filmList != null && !filmList.isEmpty()) {
            setAdapter(filmListView, filmList);
        }
    }

    private void setAdapter(RecyclerView view, List<FilmsListItem> filmList) {
        FilmsAdapter filmsAdapter = new FilmsAdapter(filmList.toArray(new FilmsListItem[0]));
        view.setAdapter(filmsAdapter);
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
        public void onBindViewHolder(ViewHolder holder, int position) {
            switch (getItemViewType(position)) {
                case FILM:
                    TextView filmTitle = holder.mView.findViewById(R.id.film_title);
                    ImageView filmImage = holder.mView.findViewById(R.id.film_image);
                    AppCompatRatingBar filmRating = holder.mView.findViewById(R.id.film_rating);

                    final Film currentFilm = (Film) items[position];

                    FilmPosterColorUtil.setBackgroundColor(context, currentFilm.getImageResource(), holder.mView.findViewById(R.id.title_container));
                    filmTitle.setText(currentFilm.getTitle());
                    filmRating.setRating(currentFilm.getRating());
                    filmImage.setImageResource(currentFilm.getImageResource());

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
