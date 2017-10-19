package cz.muni.fi.pv256.movio2.uco_410434.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import cz.muni.fi.pv256.movio2.uco_410434.R;


public abstract class AbstractActivity extends AppCompatActivity {

    protected static final String PREFERENCES = "PREFERENCES";
    protected static final String IS_ALTERNATIVE_THEME = "IS_ALTERNATIVE_THEME";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAlternativeTheme();
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
}
