package cz.muni.fi.pv256.movio2.uco_410434.ui;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatRatingBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import cz.muni.fi.pv256.movio2.uco_410434.R;
import cz.muni.fi.pv256.movio2.uco_410434.data.PersistedFilmManager;
import cz.muni.fi.pv256.movio2.uco_410434.model.Film;

public class FilmDetailFragment extends Fragment {

    public static final String TAG = FilmDetailFragment.class.getSimpleName();
    public static final String ARG_ITEM = "arg_item_film";

    private Film item;
    private ImageView imageView;
    private View titleContainer;
    private TextView filmTitle;
    private AppCompatRatingBar filmRating;
    private TextView releaseDateView;
    private FloatingActionButton actionButton;
    private PersistedFilmManager filmManager;

    private Handler mHandler = new Handler(Looper.getMainLooper());

    public FilmDetailFragment() {
    }

    public static FilmDetailFragment newInstance() {
        return new FilmDetailFragment();
    }

    public static FilmDetailFragment newInstance(Film film) {
        Bundle args = new Bundle();
        args.putParcelable(ARG_ITEM, film);
        FilmDetailFragment fragment = newInstance();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null && getArguments().containsKey(ARG_ITEM)) {
            Parcelable input = getArguments().getParcelable(ARG_ITEM);
            item = (Film) input;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_film_detail, container, false);
        imageView = rootView.findViewById(R.id.film_image);
        titleContainer = rootView.findViewById(R.id.title_container);
        filmTitle = rootView.findViewById(R.id.film_title);
        filmRating = rootView.findViewById(R.id.film_rating);
        releaseDateView = rootView.findViewById(R.id.relase_date_value);
        actionButton = rootView.findViewById(R.id.save_button);

        filmManager = new PersistedFilmManager(getContext());

        if (item != null) {
            imageView.setImageDrawable(null);
            fallbackBGColor(titleContainer, imageView);
            if (item.getBackdropPath() != null) {
                Picasso
                        .with(getContext())
                        .load(item.getBackdropURL())
                        .placeholder(R.color.indigo_teal_primary)
                        .into(imageView, new Callback() {
                            @Override
                            public void onSuccess() {
                                FilmPosterColorUtil.setBackgroundColor(getContext(), imageView.getDrawable(), titleContainer);
                            }

                            @Override
                            public void onError() {
                                Log.e(TAG, "Could not load image " + item.getBackdropPath());
                                fallbackBGColor(titleContainer, imageView);
                            }
                        });
            }

            filmTitle.setText(item.getTitle());
            filmRating.setRating((float) item.getVoteAverage());
            releaseDateView.setText(item.getReleaseDate().toLocalDate().toString());

            if (item.getId() == null) {
                actionButton.setImageResource(R.drawable.icon_save);
                actionButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        view.setEnabled(false);
                        view.setAlpha(0.5f);
                        saveFilm(item);
                        actionButton.hide();
                    }
                });
            } else {
                actionButton.setImageResource(R.drawable.icon_delete);
                actionButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        view.setEnabled(false);
                        view.setAlpha(0.5f);
                        deleteFilm(item);
                    }
                });
            }
        }

        return rootView;
    }

    private void deleteFilm(final Film item) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                filmManager.deleteFilm(item);
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        actionButton.hide();
                    }
                });
                return null;
            }
        }.execute();
    }

    private void saveFilm(final Film item) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                filmManager.createFilm(item);
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        actionButton.hide();
                    }
                });
                return null;
            }
        }.execute();
    }

    private void fallbackBGColor(View titleContainer, ImageView imageView) {
        titleContainer.setBackgroundColor(getResources().getColor(R.color.indigo_teal_primary_dark));
        imageView.setBackgroundColor(getResources().getColor(R.color.indigo_teal_primary));
    }

}
