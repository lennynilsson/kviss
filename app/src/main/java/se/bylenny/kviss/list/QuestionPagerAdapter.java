package se.bylenny.kviss.list;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import se.bylenny.kviss.fragment.QuestionFragment;
import se.bylenny.kviss.model.Question;

public class QuestionPagerAdapter extends FragmentStatePagerAdapter {

    private final Question[] questions;
    private final long timeLimit;

    public QuestionPagerAdapter(FragmentManager fm, Question[] questions, long timeLimit) {
        super(fm);
        this.questions = questions;
        this.timeLimit = timeLimit;
    }

    @Override
    public Fragment getItem(int position) {
        return QuestionFragment.newInstance(questions[position], position, questions.length, timeLimit);
    }

    @Override
    public int getCount() {
        return questions.length;
    }
}
