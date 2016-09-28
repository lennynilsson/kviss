package se.bylenny.kviss.list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.subjects.PublishSubject;
import se.bylenny.kviss.R;
import se.bylenny.kviss.model.Entity;


public class QuizListItem extends ListItem<Entity> {

    @BindView(R.id.title)
    TextView textView;

    @BindView(R.id.image)
    ImageView imageView;

    public QuizListItem(Context context) {
        super(LayoutInflater.from(context).inflate(R.layout.list_item_quiz, null), context);
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void bind(int position, final Entity item, final PublishSubject<Entity> subject) {
        textView.setText(item.getTitle());
        Picasso
                .with(context)
                .load(item.getImage())
                .into(imageView);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                subject.onNext(item);
            }
        });
    }
}
