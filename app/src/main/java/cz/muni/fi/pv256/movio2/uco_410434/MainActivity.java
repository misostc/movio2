package cz.muni.fi.pv256.movio2.uco_410434;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    public static final String PREFERENCES = "PREFERENCES";
    private static final String IS_ALTERNATIVE_THEME = "IS_ALTERNATIVE_THEME";

    private RecyclerView filmsRecyclerView;
    private RecyclerView.Adapter filmsAdapter;
    private RecyclerView.LayoutManager filmsLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAlternativeTheme();
        setContentView(R.layout.activity_main);
        setupFilmList();
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

    private void setAlternativeTheme() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFERENCES, MODE_PRIVATE);
        boolean isAlternativeTheme = sharedPreferences.getBoolean(IS_ALTERNATIVE_THEME, false);
        if (isAlternativeTheme) {
            setTheme(R.style.MainAppTheme_Alt);
        } else {
            setTheme(R.style.MainAppTheme);
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

    private void setupFilmList() {
        filmsRecyclerView = findViewById(R.id.films);
        filmsRecyclerView.setHasFixedSize(true);

        filmsLayoutManager = new LinearLayoutManager(this);
        filmsRecyclerView.setLayoutManager(filmsLayoutManager);

        Film[] myDataset = prepareTestFilms();
        filmsAdapter = new FilmsAdapter(myDataset);
        filmsRecyclerView.setAdapter(filmsAdapter);

    }

    private Film[] prepareTestFilms() {
        return new Film[]{
                new Film("Lorem ipsum dolor", 3.5f, R.drawable.rect_sample_film),
                new Film("Etiam mattis urna aliquet ", 1.5f, R.drawable.rect_sample_film_alt),
                new Film("Cras maximus risus", 2.4f, R.drawable.rect_sample_film),
                new Film("Nunc id libero", 4.8f, R.drawable.rect_sample_film_alt),
                new Film("Nunc feugiat ante", 2.4f, R.drawable.rect_sample_film),
                new Film("Proin nec tortor", 4.8f, R.drawable.rect_sample_film_alt),
                new Film("Cras faucibus quam", 2.4f, R.drawable.rect_sample_film),
                new Film("Aenean eleifend mauris", 4.8f, R.drawable.rect_sample_film_alt)
        };
    }
}
