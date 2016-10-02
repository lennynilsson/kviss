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

    @BindView(R.id.results)
    TextView resultsView;

    private Context context;

    public HeaderListItem(Context context) {
        super(LayoutInflater.from(context).inflate(R.layout.list_item_title, null));
        this.context = context;
        ButterKnife.bind(this, itemView);
    }

    public void bind(String title, int correct, int total) {
        String text = String.format(context.getString(R.string.result_template), correct, total);
        titleView.setText(title);
        resultsView.setText(text);
    }
}
