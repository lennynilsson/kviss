package se.bylenny.kviss.list;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import se.bylenny.kviss.R;

public class ResultListItem extends RecyclerView.ViewHolder {

    private final CharSequence correct;
    private final CharSequence given;
    @BindView(R.id.question_title)
    TextView questionView;

    @BindView(R.id.given)
    TextView givenView;

    @BindView(R.id.correct)
    TextView correctView;

    public ResultListItem(Context context) {
        super(LayoutInflater.from(context).inflate(R.layout.list_item_result, null));
        this.correct = context.getResources().getText(R.string.correct);
        this.given = context.getResources().getText(R.string.given);
        ButterKnife.bind(this, itemView);
    }

    public void bind(String question, String givenAnswer, String correctAnswer, boolean isCorrect) {
        questionView.setText(question);
        givenView.setText(correct + givenAnswer);
        correctView.setText(given + correctAnswer);
    }
}
