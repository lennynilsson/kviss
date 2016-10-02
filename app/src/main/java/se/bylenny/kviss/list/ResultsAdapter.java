package se.bylenny.kviss.list;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import se.bylenny.kviss.model.Entity;
import se.bylenny.kviss.model.Question;
import se.bylenny.kviss.model.Quiz;
import se.bylenny.kviss.model.Submission;

public class ResultsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int HEADER = 0;
    private static final int ITEM = 1;
    private final Quiz quiz;
    private final Submission submission;

    public ResultsAdapter(Quiz quiz, Submission submission) {
        this.quiz = quiz;
        this.submission = submission;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case HEADER:
                return new HeaderListItem(parent.getContext());
            case ITEM:
                return new ResultListItem(parent.getContext());
            default:
                return null;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return 0 == position ? HEADER : ITEM;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case HEADER:
                int correct = countCorrectAnswers();
                int total = quiz.getQuestions().length;
                ((HeaderListItem) holder).bind(quiz.getTitle(), correct, total);
                break;
            case ITEM:
                // Compensate for title index
                Question question = quiz.getQuestions()[position - 1];
                Entity[] options = question.getOptions();
                String questionTitle = question.getTitle();
                String givenId = submission.getAnswer(question.getId());
                String correctId = question.getCorrectAnswerId();
                String givenAnswer = getTitleForId(options, givenId);
                String correctAnswer = getTitleForId(options, correctId);
                boolean isCorrect = null != givenId && 0 == correctId.compareTo(givenId);
                ((ResultListItem) holder).bind(
                        questionTitle, givenAnswer, correctAnswer, isCorrect);
                break;
        }
    }

    private int countCorrectAnswers() {
        int count = 0;
        for (Question question : quiz.getQuestions()) {
            String answer = submission.getAnswer(question.getId());
            if (null != answer && 0 == question.getCorrectAnswerId().compareTo(answer)) {
                count++;
            }
        }
        return count;
    }

    private String getTitleForId(Entity[] options, String id) {
        if (null != id) {
            for (Entity entity : options) {
                if (0 == entity.getId().compareTo(id)) {
                    return entity.getTitle();
                }
            }
        }
        return null;
    }

    @Override
    public int getItemCount() {
        // Compensate for title index
        return quiz.getQuestions().length + 1;
    }
}
