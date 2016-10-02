package se.bylenny.kviss.builder;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.view.View;

import java.util.LinkedList;

/**
 * Build an activity transition
 */
public class TransitionBuilder {
    private Activity activity;
    private LinkedList<Pair<View, String>> pairs = new LinkedList<>();

    public TransitionBuilder(Activity activity) {
        this.activity = activity;
    }

    public TransitionBuilder with(View view, int transitionResourceId) {
        Pair<View, String> pair = Pair.create(view, activity.getString(transitionResourceId));
        pairs.add(pair);
        return this;
    }

    public Bundle build() {
        Pair<View, String>[] args = new Pair[pairs.size()];
        pairs.toArray(args);
        return ActivityOptionsCompat.makeSceneTransitionAnimation(activity, args).toBundle();
    }
}
