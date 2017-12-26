package cz.muni.fi.pv256.movio2.uco_410434.service;

import android.os.Handler;
import android.os.Looper;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

public class MainThreadBus extends Bus {
    private static MainThreadBus instance = new MainThreadBus();
    private final Handler mHandler = new Handler(Looper.getMainLooper());

    protected MainThreadBus() {
        super(ThreadEnforcer.ANY);
    }

    public static MainThreadBus getInstance() {
        return instance;
    }

    @Override
    public void post(final Object event) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            super.post(event);
        } else {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    MainThreadBus.super.post(event);
                }
            });
        }
    }

}