package se.bylenny.kviss.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func1;
import se.bylenny.kviss.Assets;
import se.bylenny.kviss.R;
import se.bylenny.kviss.builder.TransitionBuilder;
import se.bylenny.kviss.model.Quiz;

public class StartActivity extends AppCompatActivity {
    private static final String EXTRA_ID = "EXTRA_ID";
    private static Context context;

    @BindView(R.id.image)
    ImageView imageView;

    @BindView(R.id.activity_start)
    FrameLayout rootView;

    @BindView(R.id.title)
    TextView titleView;

    @BindView(R.id.instructions)
    TextView instructionsView;

    @BindView(R.id.start_button)
    Button buttonView;

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
        StartActivity.context = context;
        Intent intent = new Intent(context, StartActivity.class);
        intent.putExtra(EXTRA_ID, id);
        return intent;
    }

    private void populate(final Quiz quiz) {
        Picasso
                .with(context)
                .load(quiz.getImage())
                .into(imageView);
        String instructions = String.format(
                getResources().getString(R.string.instructions),
                quiz.getQuestions().length,
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
        Bundle bundle = new TransitionBuilder(this)
                .with(rootView, R.string.transition_background)
                .build();
        startActivity(intent, bundle);
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
