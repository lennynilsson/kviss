package se.bylenny.kviss.list;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import se.bylenny.kviss.R;

public class ResultListItem extends RecyclerView.ViewHolder {

    @BindView(R.id.question_title)
    TextView questionView;

    @BindView(R.id.given)
    TextView givenView;

    @BindView(R.id.correct)
    TextView correctView;

    private Context context;
    private String emptyAnswer;

    public ResultListItem(Context context) {
        super(LayoutInflater.from(context).inflate(R.layout.list_item_result, null));
        this.context = context;
        this.emptyAnswer = context.getString(R.string.empty_answer);
        ButterKnife.bind(this, itemView);
    }

    public void bind(String question, String givenAnswer, String correctAnswer, boolean isCorrect) {
        questionView.setText(question);
        String correctedGivenAnswer = null == givenAnswer
                ? emptyAnswer
                : givenAnswer;
        givenView.setText(correctAnswer);
        correctView.setText(correctedGivenAnswer);
        int color = ContextCompat.getColor(context, isCorrect
                ? R.color.colorPositive
                : R.color.colorNegative);
        givenView.setTextColor(color);
        correctView.setTextColor(color);
    }
}
