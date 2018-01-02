package cz.muni.fi.pv256.movio2.uco_410434.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;

import cz.muni.fi.pv256.movio2.uco_410434.R;
import cz.muni.fi.pv256.movio2.uco_410434.model.Film;

public class FilmListActivity extends AbstractActivity implements AbstractFilmListFragment.OnFilmSelectListener {

    private static final String TAG = FilmListActivity.class.getSimpleName();
    private boolean twoPane;
    private OnlineFilmListFragment onlineFragment;
    private PersistedFilmListFragment persistedFragment;
    private AbstractFilmListFragment chosenFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_film_list);

        handlePersistedSwitch(false);

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
    public void onAttachFragment(android.app.Fragment fragment) {
        super.onAttachFragment(fragment);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem item = menu.findItem(R.id.switch_saved);
        SwitchCompat switchView = item.getActionView().findViewById(R.id.persisted_menu_switch);
        switchView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                handlePersistedSwitch(isChecked);
            }
        });
        return true;
    }

    private void handlePersistedSwitch(boolean isChecked) {
        Log.d(TAG, "handlePersistedSwitch: " + isChecked);
        chosenFragment = null;
        if (isChecked) {
            if (persistedFragment == null) {
                persistedFragment = PersistedFilmListFragment.newInstance();
                persistedFragment.setFilmSelectListener(this);
            }
            chosenFragment = persistedFragment;
        } else {
            if (onlineFragment == null) {
                onlineFragment = OnlineFilmListFragment.newInstance();
                onlineFragment.setFilmSelectListener(this);
            }
            chosenFragment = onlineFragment;
        }
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.film_list_container, chosenFragment, OnlineFilmListFragment.TAG)
                .commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "menuItemSelected: " + item);
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
        Log.d(TAG, "onFilmSelect: " + film);
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
