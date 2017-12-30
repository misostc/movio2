package cz.muni.fi.pv256.movio2.uco_410434.data;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.ProviderTestCase2;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import cz.muni.fi.pv256.movio2.uco_410434.model.Film;

@RunWith(AndroidJUnit4.class)
public class PersistedFilmManagerTest extends ProviderTestCase2<FilmProvider> {

    private PersistedFilmManager filmManager;

    public PersistedFilmManagerTest() {
        super(FilmProvider.class, FilmContract.CONTENT_AUTHORITY);
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();
        setContext(InstrumentationRegistry.getTargetContext());
        filmManager = new PersistedFilmManager(getContext().getContentResolver());
        getContext().getContentResolver().delete(
                FilmContract.FilmEntry.CONTENT_URI,
                null,
                null
        );
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        getContext().getContentResolver().delete(
                FilmContract.FilmEntry.CONTENT_URI,
                null,
                null
        );
    }

    @Test
    public void createFilm() throws Exception {
        Film newFilm = new Film();
        newFilm.setTitle("Film test");
        newFilm.setReleaseDate(DateTime.now());
        filmManager.createFilm(newFilm);
        assertNotNull(newFilm.getId());
    }

    @Test
    public void getFilms() throws Exception {
        List<Film> list = filmManager.getFilms();
        assertEquals(0, list.size());

        Film newFilm = new Film();
        newFilm.setTitle("Film test");
        newFilm.setReleaseDate(DateTime.now());
        filmManager.createFilm(newFilm);
        assertNotNull(newFilm.getId());

        List<Film> after = filmManager.getFilms();
        assertEquals(1, after.size());
    }

    @Test
    public void deleteFilm() throws Exception {
        List<Film> list = filmManager.getFilms();
        assertEquals(0, list.size());

        Film newFilm = new Film();
        newFilm.setTitle("Film test");
        newFilm.setReleaseDate(DateTime.now());
        filmManager.createFilm(newFilm);
        assertNotNull(newFilm.getId());

        List<Film> after = filmManager.getFilms();
        assertEquals(1, after.size());

        Film toDelete = new Film();
        toDelete.setId(newFilm.getId());
        filmManager.deleteFilm(toDelete);

        List<Film> afterDelete = filmManager.getFilms();
        assertEquals(0, afterDelete.size());
    }

}