package cz.muni.fi.pv256.movio2.uco_410434;

import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class FilmsAdapter extends RecyclerView.Adapter<FilmsAdapter.ViewHolder> {

    private Film[] films;

    public FilmsAdapter(Film[] films) {
        this.films = films;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ConstraintLayout v = (ConstraintLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_film, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TextView filmTitle = holder.mLayout.findViewById(R.id.film_title);
        ImageView filmImage = holder.mLayout.findViewById(R.id.film_image);
        AppCompatRatingBar filmRating = holder.mLayout.findViewById(R.id.film_rating);

        Film currentFilm = films[position];

        filmTitle.setText(currentFilm.getTitle());
        filmRating.setRating(currentFilm.getRating());
        filmImage.setImageResource(currentFilm.getImageResource());
    }

    @Override
    public int getItemCount() {
        return films.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ConstraintLayout mLayout;

        public ViewHolder(ConstraintLayout v) {
            super(v);
            mLayout = v;
        }
    }
}
