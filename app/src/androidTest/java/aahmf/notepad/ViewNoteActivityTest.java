package aahmf.notepad;

import android.support.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

public class ViewNoteActivityTest {

    @Rule
    public ActivityTestRule<ViewNoteActivity> mActivityTestRule = new ActivityTestRule<ViewNoteActivity>(ViewNoteActivity.class);
    private ViewNoteActivity mActivity= null;

    @Before
    public void setUp() throws Exception {
        mActivity =mActivityTestRule.getActivity();
    }


    public void tearDown() throws Exception {
        mActivity = null;
    }
}