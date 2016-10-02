package se.bylenny.kviss.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscription;
import rx.functions.Action1;
import se.bylenny.kviss.R;
import se.bylenny.kviss.builder.TransitionBuilder;
import se.bylenny.kviss.list.QuizAdapter;
import se.bylenny.kviss.list.QuizListItem;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.quiz_list)
    RecyclerView quizListView;

    private QuizAdapter adapter;
    private Subscription subscription;

    public static Intent createIntent(Context context) {
        return new Intent(context, MainActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        try {
            adapter = new QuizAdapter(this);
            quizListView.setLayoutManager(new LinearLayoutManager(this));
            quizListView.setItemViewCacheSize(20);
            quizListView.setDrawingCacheEnabled(true);
            quizListView.setAdapter(adapter);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, R.string.unexpected_error, Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        subscription = adapter.asObservable().subscribe(new Action1<QuizListItem>() {
            @Override
            public void call(QuizListItem item) {
                Intent intent = StartActivity.createIntent(
                        getApplicationContext(), item.getEntity().getId());
                Bundle bundle = new TransitionBuilder(MainActivity.this)
                        .with(item.getImageView(), R.string.transition_background)
                        .with(item.getTextView(), R.string.transition_title)
                        .build();
                startActivity(intent, bundle);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        subscription.unsubscribe();
        subscription = null;
    }
}
