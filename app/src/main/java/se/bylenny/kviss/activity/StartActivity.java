package se.bylenny.kviss.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.concurrent.Callable;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import se.bylenny.kviss.Assets;
import se.bylenny.kviss.R;
import se.bylenny.kviss.model.Entity;
import se.bylenny.kviss.model.Quiz;

public class StartActivity extends AppCompatActivity {

    private static final int NUM_OF_QUESTIONS = 10;
    private static final int NUM_OF_OPTIONS = 5;
    private static final int TIME_LIMIT = 10;
    private static final String EXTRA_ID = "EXTRA_ID";
    @BindView(R.id.activity_start)
    FrameLayout rootView;
    @BindView(R.id.title)
    TextView titleView;
    @BindView(R.id.instructions)
    TextView instructionsView;
    @BindView(R.id.start_button)
    Button buttonView;
    private Entity quiz;
    private Subscription subscription;
    private Action1<Throwable> exitAction = new Action1<Throwable>() {
        @Override
        public void call(Throwable e) {
            e.printStackTrace();
            Toast.makeText(
                    getApplicationContext(),
                    R.string.unexpected_error,
                    Toast.LENGTH_LONG).show();
            finish();
        }
    };

    public static Intent createIntent(Context context, String id) {
        Intent intent = new Intent(context, StartActivity.class);
        intent.putExtra(EXTRA_ID, id);
        return intent;
    }

    private void populate(final Quiz quiz) {
        Observable
                .fromCallable(new Callable<BitmapDrawable>() {
                    @Override
                    public BitmapDrawable call() throws Exception {
                        return new BitmapDrawable(getResources(), Picasso.with(getApplicationContext())
                                .load(quiz.getImage())
                                .get());
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<BitmapDrawable>() {
                    @Override
                    public void call(BitmapDrawable drawable) {
                        rootView.setBackground(drawable);
                    }
                }, exitAction);
        String instructions = String.format(
                getResources().getString(R.string.instructions),
                NUM_OF_QUESTIONS,
                NUM_OF_OPTIONS,
                Math.round(quiz.getTimeLimit() / 1000));
        titleView.setText(quiz.getTitle());
        instructionsView.setText(instructions);
        buttonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startQuiz(quiz);
            }
        });
    }

    private void startQuiz(Quiz quiz) {
        Intent intent = QuizActivity.createIntent(this, quiz);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        subscription = Observable
                .just(getIntent().getStringExtra(EXTRA_ID))
                .map(new Func1<String, Quiz>() {
                    @Override
                    public Quiz call(String id) {
                        return Assets.read(id + "/quiz.json", Quiz.class, getApplicationContext());
                    }
                })
                .subscribe(new Action1<Quiz>() {
                    @Override
                    public void call(Quiz quiz) {
                        populate(quiz);
                    }
                }, exitAction);
    }

    @Override
    protected void onPause() {
        super.onPause();
        subscription.unsubscribe();
        subscription = null;
    }
}
