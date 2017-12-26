package cz.muni.fi.pv256.movio2.uco_410434.adapter;

import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import cz.muni.fi.pv256.movio2.uco_410434.R;
import cz.muni.fi.pv256.movio2.uco_410434.model.Category;
import cz.muni.fi.pv256.movio2.uco_410434.model.EmptyListItem;
import cz.muni.fi.pv256.movio2.uco_410434.model.Film;
import cz.muni.fi.pv256.movio2.uco_410434.model.FilmsListItem;
import cz.muni.fi.pv256.movio2.uco_410434.ui.AbstractFilmListFragment;
import cz.muni.fi.pv256.movio2.uco_410434.ui.FilmPosterColorUtil;

public class FilmsAdapter extends RecyclerView.Adapter<FilmsAdapter.ViewHolder> {

    private static final int FILM = 0, CATEGORY = 1, EMPTY = 2;
    private static final String TAG = FilmsAdapter.class.getSimpleName();

    private AbstractFilmListFragment filmListFragment;
    private FilmsListItem[] items;

    public FilmsAdapter(AbstractFilmListFragment filmListFragment, FilmsListItem[] items) {
        this.filmListFragment = filmListFragment;
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
                return null;
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
                filmRating.setRating((float) currentFilm.getVoteAverage());
                filmImage.setImageDrawable(null);
                fallbackBGColor(titleContainer, filmImage);
                if (currentFilm.getBackdropPath() != null) {
                    holder.mView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
                    Log.i(TAG, "Loading film " + currentFilm.getTitle() + ", image " + currentFilm.getBackdropPath());
                    Picasso
                            .with(filmListFragment.getContext())
                            .load(currentFilm.getBackdropURL())
                            .resize(holder.mView.getMeasuredWidth(), holder.mView.getMeasuredHeight())
                            .placeholder(R.color.indigo_teal_primary)
                            .into(filmImage, new Callback() {
                                @Override
                                public void onSuccess() {
                                    Log.i(TAG, "Done loading film " + currentFilm.getTitle() + ", image " + currentFilm.getBackdropPath());
                                    FilmPosterColorUtil.setBackgroundColor(filmListFragment.getContext(), filmImage.getDrawable(), titleContainer);
                                }

                                @Override
                                public void onError() {
                                    Log.e(TAG, "Could not load image " + currentFilm.getBackdropPath());
                                    fallbackBGColor(titleContainer, filmImage);
                                }
                            });
                }

                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        filmListFragment.getFilmSelectListener().onFilmSelect(currentFilm);
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
        titleContainer.setBackgroundColor(filmListFragment.getResources().getColor(R.color.indigo_teal_primary_dark));
        filmImage.setBackgroundColor(filmListFragment.getResources().getColor(R.color.indigo_teal_primary));
    }

    @Override
    public int getItemViewType(int position) {
        if (items[position] instanceof Film) return FILM;
        if (items[position] instanceof Category) return CATEGORY;
        if (items[position] instanceof EmptyListItem) return EMPTY;
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
