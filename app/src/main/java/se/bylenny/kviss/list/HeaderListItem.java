package se.bylenny.kviss.list;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import se.bylenny.kviss.R;

public class HeaderListItem extends RecyclerView.ViewHolder {

    @BindView(R.id.title)
    TextView titleView;

    public HeaderListItem(Context context) {
        super(LayoutInflater.from(context).inflate(R.layout.list_item_title, null));
        ButterKnife.bind(this, itemView);
    }

    public void bind(String title) {
        titleView.setText(title);
    }
}
