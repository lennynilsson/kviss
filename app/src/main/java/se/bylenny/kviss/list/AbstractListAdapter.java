package se.bylenny.kviss.list;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.Arrays;
import java.util.List;

import rx.Observable;
import rx.subjects.PublishSubject;

public abstract class AbstractListAdapter<T> extends RecyclerView.Adapter<ListItem<T>> {

    private List<T> list = Arrays.asList();
    private PublishSubject<T> clickPublisher = PublishSubject.create();

    @Override
    public ListItem<T> onCreateViewHolder(ViewGroup parent, int viewType) {
        return createViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(ListItem<T> holder, int position) {
        T item = getItem(position);
        holder.bind(position, item, clickPublisher);
    }

    public void setList(T[] list) {
        this.list = Arrays.asList(list);
        notifyDataSetChanged();
    }

    private T getItem(int position) {
        return list.get(position);
    }

    public Observable<T> asObservable() {
        return clickPublisher;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public abstract ListItem<T> createViewHolder(ViewGroup parent);
}
