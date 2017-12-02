package cz.muni.fi.pv256.movio2.uco_410434;

import android.app.Application;
import android.os.StrictMode;

import net.danlew.android.joda.JodaTimeAndroid;

public class Movio2Application extends Application {

    @Override
    public void onCreate() {
        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectDiskReads()
                    .detectDiskWrites()
                    .detectNetwork()   // or .detectAll() for all detectable problems
                    .penaltyLog()
                    .build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectLeakedSqlLiteObjects()
                    .detectLeakedClosableObjects()
                    .penaltyLog()
                    .penaltyDeath()
                    .build());
        }
        JodaTimeAndroid.init(this);
        super.onCreate();
    }

}
