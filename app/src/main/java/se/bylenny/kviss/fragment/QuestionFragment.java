package se.bylenny.kviss.fragment;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import se.bylenny.kviss.R;
import se.bylenny.kviss.activity.QuizActivity;
import se.bylenny.kviss.model.Entity;
import se.bylenny.kviss.model.Question;

public class QuestionFragment extends Fragment {
    private static final String QUESTION = "QUESTION";
    private static final String INDEX = "INDEX";
    private static final String TOTAL = "TOTAL";
    private static final String TIME_LEFT = "TIME_LEFT";
    private static final String TIME = "TIME";
    private static final String EMPTY_ANSWER = null;
    private static final long RESOLUTION = 100;
    @BindView(R.id.question_text)
    TextView questionView;
    @BindView(R.id.index)
    TextView indexView;
    @BindView(R.id.timer)
    ProgressBar timerView;
    @BindView(R.id.answers)
    LinearLayout answersView;
    private Action1<Throwable> exitAction = new Action1<Throwable>() {
        @Override
        public void call(Throwable e) {
            e.printStackTrace();
            Toast.makeText(
                    getContext(),
                    R.string.unexpected_error,
                    Toast.LENGTH_LONG).show();
            getActivity().finish();
        }
    };
    private long timeLeft;
    private long totalTime;
    private Subscription subscription;
    private Question question;

    public QuestionFragment() {
    }

    public static QuestionFragment newInstance(Question question, int index, int total, long time) {
        QuestionFragment fragment = new QuestionFragment();
        Bundle args = new Bundle();
        args.putSerializable(QUESTION, question);
        args.putInt(INDEX, index);
        args.putInt(TOTAL, total);
        args.putLong(TIME, time);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_question, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle args = getArguments();
        this.timeLeft = this.totalTime = args.getLong(TIME);
        this.question = (Question) args.getSerializable(QUESTION);
        populateViews(args.getInt(INDEX), args.getInt(TOTAL));
    }

    private void populateViews(int index, int total) {
        questionView.setText(question.getTitle());
        indexView.setText(String.format("%d/%d", (index + 1), total));
        timerView.setMax((int) (totalTime));
        LayoutInflater inflater = LayoutInflater.from(getContext());
        for (final Entity option : question.getOptions()) {
            Button button = (Button) inflater.inflate(R.layout.button_option, answersView, false);
            button.setText(option.getTitle());
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    submitAnswer(option.getId());
                }
            });
            answersView.addView(button);
        }
    }

    private void submitAnswer(String optionId) {
        if (getActivity() instanceof QuizActivity) {
            ((QuizActivity) getActivity()).submitAnswer(question.getId(), optionId);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(TIME_LEFT, timeLeft);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (null != savedInstanceState) {
            timeLeft = savedInstanceState.getLong(TIME_LEFT, totalTime);
        }
    }

    // NOTE: This is a hack since this is the only hook for when the pager switches fragment
    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
        clearSubscription();
        if (menuVisible) {
            startSubscription();
        }
    }

    private void startSubscription() {
        if (null == subscription) {
            subscription = Observable.interval(RESOLUTION, TimeUnit.MILLISECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<Long>() {
                        @Override
                        public void call(Long time) {
                            timeLeft -= RESOLUTION;
                            int progress = (int) (totalTime - timeLeft);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                timerView.setProgress(progress, true);
                            } else {
                                timerView.setProgress(progress);
                            }
                            if (timeLeft <= 0) {
                                onOutOfTime();
                            }
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable e) {
                            e.printStackTrace();
                        }
                    });
        }
    }

    private void clearSubscription() {
        if (null != subscription) {
            subscription.unsubscribe();
            subscription = null;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isMenuVisible()) {
            startSubscription();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        clearSubscription();
    }

    private void onOutOfTime() {
        submitAnswer(EMPTY_ANSWER);
    }

}
