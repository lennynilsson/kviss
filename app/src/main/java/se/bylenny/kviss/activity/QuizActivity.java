package se.bylenny.kviss.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import se.bylenny.kviss.R;
import se.bylenny.kviss.list.QuestionPagerAdapter;
import se.bylenny.kviss.model.Quiz;
import se.bylenny.kviss.model.Submission;

public class QuizActivity extends AppCompatActivity {

    private static final String EXTRA_QUIZ = "EXTRA_QUIZ";

    @BindView(R.id.container)
    ViewPager viewPager;

    private QuestionPagerAdapter questionPagerAdapter;
    private Submission submission;
    private Quiz quiz;
    private boolean hasBackPress = false;

    public static Intent createIntent(Context context, Quiz quiz) {
        Intent intent = new Intent(context, QuizActivity.class);
        intent.putExtra(EXTRA_QUIZ, quiz);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        ButterKnife.bind(this);
        try {
            quiz = (Quiz) getIntent().getSerializableExtra(EXTRA_QUIZ);
            submission = new Submission();
            questionPagerAdapter = new QuestionPagerAdapter(
                    getSupportFragmentManager(), quiz.getQuestions(), quiz.getTimeLimit());
            //NOTE: Hack to disable swipe
            viewPager.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    viewPager.setCurrentItem(viewPager.getCurrentItem());
                    return true;
                }
            });
            viewPager.setAdapter(questionPagerAdapter);
        } catch (NullPointerException e) {
            onError(e);
        }
    }

    private void onError(Throwable e) {
        e.printStackTrace();
        Toast.makeText(
                getApplicationContext(),
                R.string.unexpected_error,
                Toast.LENGTH_LONG).show();
        finish();
    }

    public void submitAnswer(String questionId, String answerId) {
        submission.setAnswer(questionId, answerId);
        goToNextQuestion();
    }

    @Override
    public void onBackPressed() {
        if (hasBackPress) {
            navigateUpTo(MainActivity.createIntent(this));
        } else {
            hasBackPress = true;
            Toast.makeText(this, R.string.press_back, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        hasBackPress = false;
    }

    private void goToNextQuestion() {
        int currentIndex = viewPager.getCurrentItem();
        if (currentIndex + 1 < questionPagerAdapter.getCount()) {
            viewPager.setCurrentItem(currentIndex + 1, true);
        } else {
            submitAnswers();
        }
    }

    private void submitAnswers() {
        Intent intent = ResultActivity.createIntent(this, quiz, submission);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
        startActivity(intent);
    }
}
