package cz.muni.fi.pv256.movio2.uco_410434.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuItem;

import cz.muni.fi.pv256.movio2.uco_410434.R;
import cz.muni.fi.pv256.movio2.uco_410434.model.Film;

public class FilmListActivity extends AbstractActivity implements FilmListFragment.OnFilmSelectListener {

    private boolean twoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_film_list);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.film_list_container, FilmListFragment.newInstance(this), FilmListFragment.TAG)
                    .commit();
        }

        if (findViewById(R.id.film_detail_container) != null) {
            twoPane = true;
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.film_detail_container, FilmDetailFragment.newInstance(), FilmDetailFragment.TAG)
                        .commit();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.change_theme:
                changeActiveTheme();
                restartActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void changeActiveTheme() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFERENCES, MODE_PRIVATE);
        boolean isAlternativeTheme = sharedPreferences.getBoolean(IS_ALTERNATIVE_THEME, false);
        isAlternativeTheme = !isAlternativeTheme;
        sharedPreferences.edit().putBoolean(IS_ALTERNATIVE_THEME, isAlternativeTheme).apply();
    }

    private void restartActivity() {
        Intent intent = new Intent(this, this.getClass());
        startActivity(intent);
        finish();
    }

    @Override
    public void onFilmSelect(Film film) {
        if (twoPane) {
            FragmentManager fm = getSupportFragmentManager();

            FilmDetailFragment fragment = FilmDetailFragment.newInstance(film);
            fm.beginTransaction()
                    .replace(R.id.film_detail_container, fragment, FilmDetailFragment.TAG)
                    .commit();

        } else {
            Intent intent = new Intent(this, FilmDetailActivity.class);
            intent.putExtra(FilmDetailFragment.ARG_ITEM, film);
            startActivity(intent);
        }
    }
}
