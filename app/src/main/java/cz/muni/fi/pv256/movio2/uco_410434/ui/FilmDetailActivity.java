package cz.muni.fi.pv256.movio2.uco_410434.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import cz.muni.fi.pv256.movio2.uco_410434.R;
import cz.muni.fi.pv256.movio2.uco_410434.model.Film;

public class FilmDetailActivity extends AbstractActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_film_detail);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (savedInstanceState == null) {
            Film film = getIntent().getParcelableExtra(FilmDetailFragment.ARG_ITEM);
            FilmDetailFragment fragment = FilmDetailFragment.newInstance(film);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.film_detail_container, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                navigateUpTo(new Intent(this, FilmListActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
