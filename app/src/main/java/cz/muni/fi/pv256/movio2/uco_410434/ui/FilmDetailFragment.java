package cz.muni.fi.pv256.movio2.uco_410434.ui;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatRatingBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import cz.muni.fi.pv256.movio2.uco_410434.R;
import cz.muni.fi.pv256.movio2.uco_410434.model.Film;

public class FilmDetailFragment extends Fragment {

    public static final String TAG = FilmDetailFragment.class.getSimpleName();
    public static final String ARG_ITEM = "arg_item_film";

    private Film item;

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
        View rootView = inflater.inflate(R.layout.fragment_film_detail, container, false);

        if (item != null) {
            ((ImageView) rootView.findViewById(R.id.film_image)).setImageResource(item.getImageResource());
            ((TextView) rootView.findViewById(R.id.film_title)).setText(item.getTitle());
            ((AppCompatRatingBar) rootView.findViewById(R.id.film_rating)).setRating(item.getRating());
            FilmPosterColorUtil.setBackgroundColor(getContext(), item.getImageResource(), rootView.findViewById(R.id.title_container));
        }

        return rootView;
    }

}
