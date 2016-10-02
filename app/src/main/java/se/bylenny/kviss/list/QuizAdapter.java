package se.bylenny.kviss.list;

import android.content.Context;
import android.view.ViewGroup;

import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;

import se.bylenny.kviss.Assets;
import se.bylenny.kviss.model.Entity;

public class QuizAdapter extends AbstractListAdapter<Entity, QuizListItem> {

    public QuizAdapter(Context context) throws IOException {
        Type listType = new TypeToken<Entity[]>() {
        }.getType();
        Entity[] quizList = Assets.read("index.json", listType, context);
        setList(quizList);
    }

    @Override
    public QuizListItem createViewHolder(ViewGroup parent) {
        return new QuizListItem(parent.getContext());
    }
}
