package se.bylenny.kviss;

import android.app.Application;

import com.squareup.picasso.LruCache;
import com.squareup.picasso.Picasso;

import butterknife.ButterKnife;

public class KvissApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ButterKnife.setDebug(BuildConfig.DEBUG);
        Picasso.setSingletonInstance(new Picasso.Builder(this)
                .loggingEnabled(BuildConfig.DEBUG)
                //.indicatorsEnabled(BuildConfig.DEBUG)
                .memoryCache(new LruCache(this))
                .build());
    }
}
