package se.bylenny.kviss.list;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.Arrays;
import java.util.List;

import rx.Observable;
import rx.subjects.PublishSubject;

public abstract class AbstractListAdapter<T, I extends ListItem<T>> extends RecyclerView.Adapter<I> {

    private List<T> list = Arrays.asList();
    private PublishSubject<I> clickPublisher = PublishSubject.create();

    @Override
    public I onCreateViewHolder(ViewGroup parent, int viewType) {
        return createViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(I holder, int position) {
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

    public Observable<I> asObservable() {
        return clickPublisher;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public abstract I createViewHolder(ViewGroup parent);
}
