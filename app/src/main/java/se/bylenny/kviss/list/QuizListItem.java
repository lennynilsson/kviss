package se.bylenny.kviss.list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import lombok.Getter;
import rx.subjects.PublishSubject;
import se.bylenny.kviss.R;
import se.bylenny.kviss.model.Entity;

public class QuizListItem extends ListItem<Entity> {

    @Getter
    @BindView(R.id.title)
    TextView textView;

    @Getter
    @BindView(R.id.image)
    ImageView imageView;

    @Getter
    private Entity entity;

    public QuizListItem(Context context) {
        super(LayoutInflater.from(context).inflate(R.layout.list_item_quiz, null), context);
        ButterKnife.bind(this, itemView);
    }

    @Override
    public <I extends ListItem<Entity>> void bind(int position, Entity item, final PublishSubject<I> subject) {
        this.entity = item;
        textView.setText(item.getTitle());
        Picasso
                .with(context)
                .load(item.getImage())
                .into(imageView);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                subject.onNext((I) QuizListItem.this);
            }
        });
    }
}
