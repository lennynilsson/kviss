package se.bylenny.kviss.list;

import android.content.Context;
import android.view.ViewGroup;

import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.lang.reflect.Type;

import se.bylenny.kviss.Assets;
import se.bylenny.kviss.model.Entity;

public class QuizAdapter extends AbstractListAdapter<Entity> {

    public QuizAdapter(Context context) throws IOException {
        Type listType = new TypeToken<Entity[]>() {
        }.getType();
        Entity[] quizList = Assets.read("index.json", listType, context);
        prefetch(quizList, context);
        setList(quizList);
    }

    private void prefetch(Entity[] quizList, Context context) {
        for (Entity quiz : quizList) {
            Picasso.with(context)
                    .load(quiz.getImage())
                    .fetch();
        }
    }

    @Override
    public ListItem<Entity> createViewHolder(ViewGroup parent) {
        return new QuizListItem(parent.getContext());
    }
}
