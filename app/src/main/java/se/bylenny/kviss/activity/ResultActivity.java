package se.bylenny.kviss.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import se.bylenny.kviss.R;
import se.bylenny.kviss.list.ResultsAdapter;
import se.bylenny.kviss.model.Quiz;
import se.bylenny.kviss.model.Submission;

public class ResultActivity extends AppCompatActivity {

    private static final String EXTRA_QUIZ = "EXTRA_QUIZ";
    private static final String EXTRA_SUBMISSION = "EXTRA_SUBMISSION";

    @BindView(R.id.result_list)
    RecyclerView resultListView;

    private Quiz quiz;
    private Submission submission;
    private ResultsAdapter adapter;

    public static Intent createIntent(Context context, Quiz quiz, Submission submission) {
        Intent intent = new Intent(context, ResultActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_QUIZ, quiz);
        bundle.putSerializable(EXTRA_SUBMISSION, submission);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();
        quiz = (Quiz) bundle.getSerializable(EXTRA_QUIZ);
        submission = (Submission) bundle.getSerializable(EXTRA_SUBMISSION);
        adapter = new ResultsAdapter(quiz, submission);
        resultListView.setLayoutManager(new LinearLayoutManager(this));
        resultListView.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        Intent intent = MainActivity.createIntent(this);
        navigateUpTo(intent);
    }
}
