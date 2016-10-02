package se.bylenny.kviss.list;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import rx.subjects.PublishSubject;

public abstract class ListItem<T> extends RecyclerView.ViewHolder {

    protected final Context context;

    public ListItem(View itemView, Context context) {
        super(itemView);
        this.context = context;
    }

    public abstract <I extends ListItem<T>> void bind(int position, T item, PublishSubject<I> subject);
}
