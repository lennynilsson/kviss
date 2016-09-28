package se.bylenny.kviss;

import android.app.Application;

import butterknife.ButterKnife;

public class KvissApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ButterKnife.setDebug(BuildConfig.DEBUG);
    }
}
